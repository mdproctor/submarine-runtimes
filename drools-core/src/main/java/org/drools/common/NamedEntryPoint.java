/**
 * 
 */
package org.drools.common;

import org.drools.FactException;
import org.drools.FactHandle;
import org.drools.RuleBaseConfiguration.AssertBehaviour;
import org.drools.RuntimeDroolsException;
import org.drools.WorkingMemory;
import org.drools.WorkingMemoryEntryPoint;
import org.drools.base.ShadowProxy;
import org.drools.reteoo.EntryPointNode;
import org.drools.reteoo.ObjectTypeConf;
import org.drools.rule.EntryPoint;
import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.FactHandleFactory;
import org.drools.spi.PropagationContext;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

public class NamedEntryPoint
    implements
    InternalWorkingMemoryEntryPoint {
    /** The arguments used when adding/removing a property change listener. */
    protected final Object[]                addRemovePropertyChangeListenerArgs = new Object[]{this};

    private static final long               serialVersionUID                    = 500;

    protected ObjectStore                   objectStore;

    protected transient InternalRuleBase    ruleBase;

    protected EntryPoint                    entryPoint;
    protected EntryPointNode                entryPointNode;

    private ObjectTypeConfigurationRegistry typeConfReg;

    private AbstractWorkingMemory     wm;

    private FactHandleFactory               handleFactory;

    protected ReentrantLock           lock;

    public NamedEntryPoint() {
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        objectStore     = (ObjectStore)in.readObject();
        entryPoint      = (EntryPoint)in.readObject();
        entryPointNode      = (EntryPointNode)in.readObject();
        typeConfReg      = (ObjectTypeConfigurationRegistry)in.readObject();
        wm      = (AbstractWorkingMemory)in.readObject();
        handleFactory      = (FactHandleFactory)in.readObject();
        lock      = (ReentrantLock)in.readObject();
        
        ruleBase = (InternalRuleBase)wm.getRuleBase();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(objectStore);
        out.writeObject(entryPoint);
        out.writeObject(entryPointNode);
        out.writeObject(typeConfReg);
        out.writeObject(wm);
        out.writeObject(handleFactory);
        out.writeObject(lock);

    }

    public NamedEntryPoint(EntryPoint entryPoint,
                           EntryPointNode entryPointNode,
                           AbstractWorkingMemory wm) {
        this( entryPoint,
              entryPointNode,
              wm,
              new ReentrantLock() );
    }

    public NamedEntryPoint(EntryPoint entryPoint,
                           EntryPointNode entryPointNode,
                           AbstractWorkingMemory wm,
                           ReentrantLock lock) {
        this.entryPoint = entryPoint;
        this.entryPointNode = entryPointNode;
        this.wm = wm;
        this.ruleBase = (InternalRuleBase) this.wm.getRuleBase();
        this.lock = lock;
        this.typeConfReg = new ObjectTypeConfigurationRegistry( this.ruleBase );
        this.handleFactory = this.wm.getFactHandleFactory();
        this.objectStore = new SingleThreadedObjectStore( this.ruleBase.getConfiguration(),
                                                          this.lock );
    }

    /**
     * @see WorkingMemory
     */
    public FactHandle insert(final Object object) throws FactException {
        return insert( object, /* Not-Dynamic */
                       false,
                       false,
                       null,
                       null );
    }

    public FactHandle insert(final Object object,
                             final boolean dynamic) throws FactException {
        return insert( object,
                       dynamic,
                       false,
                       null,
                       null );
    }

    protected FactHandle insert(final Object object,
                                final boolean dynamic,
                                boolean logical,
                                final Rule rule,
                                final Activation activation) throws FactException {
        if ( object == null ) {
            // you cannot assert a null object
            return null;
        }

        ObjectTypeConf typeConf = this.typeConfReg.getObjectTypeConf( this.entryPoint,
                                                                      object );

        InternalFactHandle handle = this.handleFactory.newFactHandle( object,
                                                                      typeConf.isEvent(),
                                                                      wm );
        this.objectStore.addHandle( handle,
                                    object );

        if ( dynamic ) {
            addPropertyChangeListener( object );
        }

        try {
            this.lock.lock();
            insert( handle,
                    object,
                    rule,
                    activation );

        } finally {
            this.lock.unlock();
        }
        return handle;
    }

    protected void insert(final InternalFactHandle handle,
                          final Object object,
                          final Rule rule,
                          final Activation activation) {
        this.ruleBase.executeQueuedActions();

        if ( activation != null ) {
            // release resources so that they can be GC'ed
            activation.getPropagationContext().releaseResources();
        }
        final PropagationContext propagationContext = new PropagationContextImpl( this.wm.getNextPropagationIdCounter(),
                                                                                  PropagationContext.ASSERTION,
                                                                                  rule,
                                                                                  activation,
                                                                                  -1,
                                                                                  -1,
                                                                                  this.entryPoint );

        this.entryPointNode.assertObject( handle,
                                          propagationContext,
                                          this.typeConfReg.getObjectTypeConf( this.entryPoint,
                                                                              object ),
                                          this.wm );

        this.wm.executeQueuedActions();

        this.wm.getWorkingMemoryEventSupport().fireObjectInserted( propagationContext,
                                                                   handle,
                                                                   object,
                                                                   wm );
    }

    public void update(final FactHandle handle,
                       final Object object) throws FactException {
        update( handle,
                object,
                null,
                null );
    }

    protected void update(final FactHandle factHandle,
                          final Object object,
                          final Rule rule,
                          final Activation activation) throws FactException {
        try {
            this.lock.lock();
            this.ruleBase.executeQueuedActions();

            final InternalFactHandle handle = (InternalFactHandle) factHandle;
            final Object originalObject = (handle.isShadowFact()) ? ((ShadowProxy) handle.getObject()).getShadowedObject() : handle.getObject();

            if ( handle.getId() == -1 || object == null ) {
                // the handle is invalid, most likely already retracted, so
                // return
                // and we cannot assert a null object
                return;
            }

            ObjectTypeConf typeConf = this.typeConfReg.getObjectTypeConf( this.entryPoint,
                                                                          object );

            if ( activation != null ) {
                // release resources so that they can be GC'ed
                activation.getPropagationContext().releaseResources();
            }
            // Nowretract any trace of the original fact
            final PropagationContext propagationContext = new PropagationContextImpl( this.wm.getNextPropagationIdCounter(),
                                                                                      PropagationContext.MODIFICATION,
                                                                                      rule,
                                                                                      activation,
                                                                                      -1,
                                                                                      -1,
                                                                                      this.entryPoint );

            this.entryPointNode.retractObject( handle,
                                               propagationContext,
                                               typeConf,
                                               this.wm );

            if ( (originalObject != object) || (this.ruleBase.getConfiguration().getAssertBehaviour() != AssertBehaviour.IDENTITY) ) {
                this.objectStore.removeHandle( handle );

                // set anyway, so that it updates the hashCodes
                handle.setObject( object );
                this.objectStore.addHandle( handle,
                                            object );
            }

            this.handleFactory.increaseFactHandleRecency( handle );

            this.entryPointNode.assertObject( handle,
                                              propagationContext,
                                              typeConf,
                                              this.wm );

            this.wm.getWorkingMemoryEventSupport().fireObjectUpdated( propagationContext,
                                                                      factHandle,
                                                                      originalObject,
                                                                      object,
                                                                      this.wm );

            propagationContext.clearRetractedTuples();

            this.wm.executeQueuedActions();
        } finally {
            this.lock.unlock();
        }
    }

    public void retract(final FactHandle handle) throws FactException {
        retract( handle,
                 true,
                 true,
                 null,
                 null );
    }

    public void retract(final FactHandle factHandle,
                        final boolean removeLogical,
                        final boolean updateEqualsMap,
                        final Rule rule,
                        final Activation activation) throws FactException {
        try {
            this.lock.lock();
            this.ruleBase.executeQueuedActions();

            final InternalFactHandle handle = (InternalFactHandle) factHandle;
            if ( handle.getId() == -1 ) {
                // can't retract an already retracted handle
                return;
            }
            removePropertyChangeListener( handle );

            if ( activation != null ) {
                // release resources so that they can be GC'ed
                activation.getPropagationContext().releaseResources();
            }
            final PropagationContext propagationContext = new PropagationContextImpl( this.wm.getNextPropagationIdCounter(),
                                                                                      PropagationContext.RETRACTION,
                                                                                      rule,
                                                                                      activation,
                                                                                      -1,
                                                                                      -1,
                                                                                      this.entryPoint );

            this.entryPointNode.retractObject( handle,
                                               propagationContext,
                                               this.wm );

            final Object object = handle.getObject();

            this.wm.getWorkingMemoryEventSupport().fireObjectRetracted( propagationContext,
                                                                        handle,
                                                                        object,
                                                                        this.wm );

            this.objectStore.removeHandle( handle );

            this.handleFactory.destroyFactHandle( handle );

            this.wm.executeQueuedActions();
        } finally {
            this.lock.unlock();
        }
    }

    public void modifyRetract(final FactHandle factHandle) {
        modifyRetract( factHandle,
                       null,
                       null );
    }

    public void modifyRetract(final FactHandle factHandle,
                              final Rule rule,
                              final Activation activation) {
        try {
            this.lock.lock();
            this.ruleBase.executeQueuedActions();

            final InternalFactHandle handle = (InternalFactHandle) factHandle;
            // final Object originalObject = (handle.isShadowFact()) ?
            // ((ShadowProxy) handle.getObject()).getShadowedObject() :
            // handle.getObject();

            if ( handle.getId() == -1 ) {
                // the handle is invalid, most likely already retracted, so
                // return
                return;
            }

            if ( activation != null ) {
                // release resources so that they can be GC'ed
                activation.getPropagationContext().releaseResources();
            }
            // Nowretract any trace of the original fact
            final PropagationContext propagationContext = new PropagationContextImpl( this.wm.getNextPropagationIdCounter(),
                                                                                      PropagationContext.MODIFICATION,
                                                                                      rule,
                                                                                      activation,
                                                                                      -1,
                                                                                      -1,
                                                                                      entryPoint );

            this.entryPointNode.retractObject( handle,
                                               propagationContext,
                                               this.wm );

        } finally {
            this.lock.unlock();
        }
    }

    public void modifyInsert(final FactHandle factHandle,
                             final Object object) {
        modifyInsert( factHandle,
                      object,
                      null,
                      null );
    }

    public void modifyInsert(final FactHandle factHandle,
                             final Object object,
                             final Rule rule,
                             final Activation activation) {
        this.modifyInsert( EntryPoint.DEFAULT,
                           factHandle,
                           object,
                           rule,
                           activation );
    }

    protected void modifyInsert(final EntryPoint entryPoint,
                                final FactHandle factHandle,
                                final Object object,
                                final Rule rule,
                                final Activation activation) {
        try {
            this.lock.lock();
            this.ruleBase.executeQueuedActions();

            final InternalFactHandle handle = (InternalFactHandle) factHandle;
            final Object originalObject = (handle.isShadowFact()) ? ((ShadowProxy) handle.getObject()).getShadowedObject() : handle.getObject();

            this.handleFactory.increaseFactHandleRecency( handle );

            if ( activation != null ) {
                // release resources so that they can be GC'ed
                activation.getPropagationContext().releaseResources();
            }
            // Nowretract any trace of the original fact
            final PropagationContext propagationContext = new PropagationContextImpl( this.wm.getNextPropagationIdCounter(),
                                                                                      PropagationContext.MODIFICATION,
                                                                                      rule,
                                                                                      activation,
                                                                                      -1,
                                                                                      -1,
                                                                                      entryPoint );

            this.entryPointNode.assertObject( handle,
                                              propagationContext,
                                              this.wm );

            this.wm.getWorkingMemoryEventSupport().fireObjectUpdated( propagationContext,
                                                                      factHandle,
                                                                      originalObject,
                                                                      object,
                                                                      this.wm );

            propagationContext.clearRetractedTuples();

            this.wm.executeQueuedActions();
        } finally {
            this.lock.unlock();
        }
    }

    protected void addPropertyChangeListener(final Object object) {
        try {
            final Method method = object.getClass().getMethod( "addPropertyChangeListener",
                                                               AbstractWorkingMemory.ADD_REMOVE_PROPERTY_CHANGE_LISTENER_ARG_TYPES );

            method.invoke( object,
                           this.addRemovePropertyChangeListenerArgs );
        } catch ( final NoSuchMethodException e ) {
            System.err.println( "Warning: Method addPropertyChangeListener not found" + " on the class " + object.getClass() + " so Drools will be unable to process JavaBean" + " PropertyChangeEvents on the asserted Object" );
        } catch ( final IllegalArgumentException e ) {
            System.err.println( "Warning: The addPropertyChangeListener method" + " on the class " + object.getClass() + " does not take" + " a simple PropertyChangeListener argument" + " so Drools will be unable to process JavaBean"
                                + " PropertyChangeEvents on the asserted Object" );
        } catch ( final IllegalAccessException e ) {
            System.err.println( "Warning: The addPropertyChangeListener method" + " on the class " + object.getClass() + " is not public" + " so Drools will be unable to process JavaBean" + " PropertyChangeEvents on the asserted Object" );
        } catch ( final InvocationTargetException e ) {
            System.err.println( "Warning: The addPropertyChangeListener method" + " on the class " + object.getClass() + " threw an InvocationTargetException" + " so Drools will be unable to process JavaBean"
                                + " PropertyChangeEvents on the asserted Object: " + e.getMessage() );
        } catch ( final SecurityException e ) {
            System.err.println( "Warning: The SecurityManager controlling the class " + object.getClass() + " did not allow the lookup of a" + " addPropertyChangeListener method" + " so Drools will be unable to process JavaBean"
                                + " PropertyChangeEvents on the asserted Object: " + e.getMessage() );
        }
    }

    protected void removePropertyChangeListener(final FactHandle handle) {
        Object object = null;
        try {
            object = ((InternalFactHandle) handle).getObject();

            if ( object != null ) {
                final Method mehod = object.getClass().getMethod( "removePropertyChangeListener",
                                                                  AbstractWorkingMemory.ADD_REMOVE_PROPERTY_CHANGE_LISTENER_ARG_TYPES );

                mehod.invoke( object,
                              this.addRemovePropertyChangeListenerArgs );
            }
        } catch ( final NoSuchMethodException e ) {
            // The removePropertyChangeListener method on the class
            // was not found so Drools will be unable to
            // stop processing JavaBean PropertyChangeEvents
            // on the retracted Object
        } catch ( final IllegalArgumentException e ) {
            throw new RuntimeDroolsException( "Warning: The removePropertyChangeListener method on the class " + object.getClass() + " does not take a simple PropertyChangeListener argument so Drools will be unable to stop processing JavaBean"
                                              + " PropertyChangeEvents on the retracted Object" );
        } catch ( final IllegalAccessException e ) {
            throw new RuntimeDroolsException( "Warning: The removePropertyChangeListener method on the class " + object.getClass() + " is not public so Drools will be unable to stop processing JavaBean PropertyChangeEvents on the retracted Object" );
        } catch ( final InvocationTargetException e ) {
            throw new RuntimeDroolsException( "Warning: The removePropertyChangeL istener method on the class " + object.getClass() + " threw an InvocationTargetException so Drools will be unable to stop processing JavaBean"
                                              + " PropertyChangeEvents on the retracted Object: " + e.getMessage() );
        } catch ( final SecurityException e ) {
            throw new RuntimeDroolsException( "Warning: The SecurityManager controlling the class " + object.getClass() + " did not allow the lookup of a removePropertyChangeListener method so Drools will be unable to stop processing JavaBean"
                                              + " PropertyChangeEvents on the retracted Object: " + e.getMessage() );
        }
    }

    public WorkingMemoryEntryPoint getWorkingMemoryEntryPoint(String name) {
        return this.wm.getWorkingMemoryEntryPoint( name );
    }

    public ObjectTypeConfigurationRegistry getObjectTypeConfigurationRegistry() {
        return this.typeConfReg;
    }

}