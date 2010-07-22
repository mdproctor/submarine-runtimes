package org.drools.rule;

/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayInputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.drools.RuntimeDroolsException;
import org.drools.common.DroolsObjectInput;
import org.drools.core.util.DroolsClassLoader;
import org.drools.core.util.StringUtils;
import org.drools.spi.Wireable;

public class JavaDialectRuntimeData
    implements
    DialectRuntimeData,
    Externalizable {

    /**
     *
     */
    private static final long                    serialVersionUID = 400L;

    private static final ProtectionDomain        PROTECTION_DOMAIN;

    private Map                                  invokerLookups;

    private Map                                  store;

    private DialectRuntimeRegistry               registry;

    private transient PackageClassLoader         classLoader;

    private transient DroolsCompositeClassLoader rootClassLoader;

    private boolean                              dirty;

    private List<String>                         wireList         = Collections.<String> emptyList();

    static {
        PROTECTION_DOMAIN = (ProtectionDomain) AccessController.doPrivileged( new PrivilegedAction() {
            public Object run() {
                return JavaDialectRuntimeData.class.getProtectionDomain();
            }
        } );
    }

    public JavaDialectRuntimeData() {
        this.invokerLookups = new HashMap();
        this.store = new HashMap();
        this.dirty = false;
    }

    /**
     * Handles the write serialization of the PackageCompilationData. Patterns in Rules may reference generated data which cannot be serialized by
     * default methods. The PackageCompilationData holds a reference to the generated bytecode. The generated bytecode must be restored before any Rules.
     *
     */
    public void writeExternal(ObjectOutput stream) throws IOException {
        stream.writeInt( this.store.size() );
        for ( Iterator it = this.store.entrySet().iterator(); it.hasNext(); ) {
            Entry entry = (Entry) it.next();
            stream.writeObject( entry.getKey() );
            stream.writeObject( entry.getValue() );
        }

        stream.writeInt( this.invokerLookups.size() );
        for ( Iterator it = this.invokerLookups.entrySet().iterator(); it.hasNext(); ) {
            Entry entry = (Entry) it.next();
            stream.writeObject( entry.getKey() );
            stream.writeObject( entry.getValue() );
        }
    }

    /**
     * Handles the read serialization of the PackageCompilationData. Patterns in Rules may reference generated data which cannot be serialized by
     * default methods. The PackageCompilationData holds a reference to the generated bytecode; which must be restored before any Rules.
     * A custom ObjectInputStream, able to resolve classes against the bytecode, is used to restore the Rules.
     *
     */
    public void readExternal(ObjectInput stream) throws IOException,
                                                ClassNotFoundException {
        DroolsObjectInput droolsStream = (DroolsObjectInput) stream;
        for ( int i = 0, length = stream.readInt(); i < length; i++ ) {
            this.store.put( stream.readObject(),
                            stream.readObject() );
        }
        for ( int i = 0, length = stream.readInt(); i < length; i++ ) {
            this.invokerLookups.put( stream.readObject(),
                                     stream.readObject() );
        }
        // mark it as dirty, so that it reloads everything.
        this.dirty = true;
    }

    public void onAdd(DialectRuntimeRegistry registry,
                      DroolsCompositeClassLoader rootClassLoader) {
        this.registry = registry;
        this.rootClassLoader = rootClassLoader;
        this.classLoader = new PackageClassLoader( this,
                                                   this.rootClassLoader );
        this.rootClassLoader.addClassLoader( this.classLoader );
    }

    public void onRemove() {
        this.rootClassLoader.removeClassLoader( this.classLoader );
    }

    public void onBeforeExecute() {
        if ( isDirty() ) {
            reload();
        } else if ( !this.wireList.isEmpty() ) {
            try {
                // wire all remaining resources
                for ( String resourceName : this.wireList ) {
                    wire( convertResourceToClassName( resourceName ) );
                }
            } catch ( Exception e ) {
                throw new RuntimeDroolsException( "Unable to wire up JavaDialect",
                                                  e );
            }
        }

        this.wireList.clear();
    }

    public DialectRuntimeData clone(DialectRuntimeRegistry registry,
                                    DroolsCompositeClassLoader rootClassLoader) {
        DialectRuntimeData cloneOne = new JavaDialectRuntimeData();
        cloneOne.merge( registry,
                        this );
        cloneOne.onAdd( registry,
                        rootClassLoader );
        return cloneOne;
    }

    public void merge(DialectRuntimeRegistry registry,
                      DialectRuntimeData newData) {
        this.registry = registry;
        JavaDialectRuntimeData newJavaData = (JavaDialectRuntimeData) newData;

        // First update the binary files
        // @todo: this probably has issues if you add classes in the incorrect order - functions, rules, invokers.
        for ( String resourceName : newJavaData.list() ) {
            write( resourceName,
                   newJavaData.read( resourceName ) );
            //            // no need to wire, as we already know this is done in a merge
            //            if ( getStore().put( resourceName,
            //                                 newJavaData.read( resourceName ) ) != null ) {
            //                // we are updating an existing class so reload();
            //                this.dirty = true;
            //            }
            //            if ( this.dirty == false ) {
            //                // only build up the wireList if we aren't going to reload
            //                this.wireList.add( resourceName );
            //            }
        }

        //        if ( this.dirty ) {
        //            // no need to keep wireList if we are going to reload;
        //            this.wireList.clear();
        //        }

        // Add invokers
        putAllInvokers( newJavaData.getInvokers() );

    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    protected Map getStore() {
        if ( store == null ) {
            store = new HashMap();
        }
        return store;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public void removeRule(Package pkg,
                           Rule rule) {
        
        if ( !(rule instanceof Query) ) {
            // Query's don't have a consequence, so skip those
            final String consequenceName = rule.getConsequence().getClass().getName();
    
            // check for compiled code and remove if present.
            if ( remove( consequenceName ) ) {
                removeClasses( rule.getLhs() );
    
                // Now remove the rule class - the name is a subset of the consequence name
                String sufix = StringUtils.ucFirst( rule.getConsequence().getName() ) + "ConsequenceInvoker";
                remove( consequenceName.substring( 0,
                                                   consequenceName.indexOf( sufix ) ) );
            }
        }
    }

    public void removeFunction(Package pkg,
                               Function function) {
        remove( pkg.getName() + "." + StringUtils.ucFirst( function.getName() ) );
    }

    private void removeClasses(final ConditionalElement ce) {
        if ( ce instanceof GroupElement ) {
            final GroupElement group = (GroupElement) ce;
            for ( final Iterator it = group.getChildren().iterator(); it.hasNext(); ) {
                final Object object = it.next();
                if ( object instanceof ConditionalElement ) {
                    removeClasses( (ConditionalElement) object );
                } else if ( object instanceof Pattern ) {
                    removeClasses( (Pattern) object );
                }
            }
        } else if ( ce instanceof EvalCondition ) {
            remove( ((EvalCondition) ce).getEvalExpression().getClass().getName() );
        }
    }

    private void removeClasses(final Pattern pattern) {
        for ( final Iterator it = pattern.getConstraints().iterator(); it.hasNext(); ) {
            final Object object = it.next();
            if ( object instanceof PredicateConstraint ) {
                remove( ((PredicateConstraint) object).getPredicateExpression().getClass().getName() );
            } else if ( object instanceof ReturnValueConstraint ) {
                remove( ((ReturnValueConstraint) object).getExpression().getClass().getName() );
            }
        }
    }

    public byte[] read(final String resourceName) {
        byte[] bytes = null;

        if ( !getStore().isEmpty() ) {
            bytes = (byte[]) getStore().get( resourceName );
        }
        return bytes;
    }

    public void write(final String resourceName,
                      final byte[] clazzData) throws RuntimeDroolsException {
        if ( getStore().put( resourceName,
                             clazzData ) != null ) {
            this.dirty = true;

            if ( !this.wireList.isEmpty() ) {
                this.wireList.clear();
            }
        } else if ( !this.dirty ) {
            try {
                if ( this.wireList == Collections.<String> emptyList() ) {
                    this.wireList = new ArrayList<String>();
                }
                this.wireList.add( resourceName );
            } catch ( final Exception e ) {
                e.printStackTrace();
                throw new RuntimeDroolsException( e );
            }
        }
    }

    public void wire(final String className) throws ClassNotFoundException,
                                            InstantiationException,
                                            IllegalAccessException {
        final Object invoker = getInvokers().get( className );
        if ( invoker != null ) {
            wire( className,
                  invoker );
        }
    }

    public void wire(final String className,
                     final Object invoker) throws ClassNotFoundException,
                                          InstantiationException,
                                          IllegalAccessException {
        final Class clazz = this.rootClassLoader.loadClass( className );

        if ( clazz != null ) {
            if ( invoker instanceof Wireable ) {
                ((Wireable) invoker).wire( clazz.newInstance() );
            }
            //
            //if ( invoker instanceof ReturnValueRestriction ) {
            //((ReturnValueRestriction) invoker).setReturnValueExpression( (ReturnValueExpression) clazz.newInstance() );
            //} else if ( invoker instanceof PredicateConstraint ) {
            //((PredicateConstraint) invoker).setPredicateExpression( (PredicateExpression) clazz.newInstance() );
            //} else if ( invoker instanceof EvalCondition ) {
            //((EvalCondition) invoker).setEvalExpression( (EvalExpression) clazz.newInstance() );
            //} else if ( invoker instanceof Accumulate ) {
            //((Accumulate) invoker).setAccumulator( (Accumulator) clazz.newInstance() );
            //} else if ( invoker instanceof Rule ) {
            //((Rule) invoker).setConsequence( (Consequence) clazz.newInstance() );
            //} else if ( invoker instanceof JavaAccumulatorFunctionExecutor ) {
            //((JavaAccumulatorFunctionExecutor) invoker).setExpression( (ReturnValueExpression) clazz.newInstance() );
            //} else if ( invoker instanceof DroolsAction ) {
            //((DroolsAction) invoker).setMetaData( "Action",
            //              clazz.newInstance() );
            //} else if ( invoker instanceof ReturnValueConstraintEvaluator ) {
            //((ReturnValueConstraintEvaluator) invoker).setEvaluator( (ReturnValueEvaluator) clazz.newInstance() );
            //}
        } else {
            throw new ClassNotFoundException( className );
        }
    }

    public boolean remove(final String resourceName) throws RuntimeDroolsException {
        getInvokers().remove( resourceName );
        if ( getStore().remove( convertClassToResourcePath( resourceName ) ) != null ) {
            this.wireList.remove( resourceName );
            // we need to make sure the class is removed from the classLoader
            // reload();
            this.dirty = true;
            return true;
        }
        return false;
    }

    public String[] list() {
        String[] names = new String[getStore().size()];
        int i = 0;

        for ( Object object : getStore().keySet() ) {
            names[i++] = (String) object;
        }
        return names;
    }

    /**
     * This class drops the classLoader and reloads it. During this process  it must re-wire all the invokeables.
     * @throws RuntimeDroolsException
     */
    public void reload() throws RuntimeDroolsException {
        // drops the classLoader and adds a new one
        this.rootClassLoader.removeClassLoader( this.classLoader );
        this.classLoader = new PackageClassLoader( this,
                                                   this.rootClassLoader );
        this.rootClassLoader.addClassLoader( this.classLoader );

        // Wire up invokers
        try {
            for ( final Object object : getInvokers().entrySet() ) {
                Entry entry = (Entry) object;
                wire( (String) entry.getKey(),
                      entry.getValue() );
            }
        } catch ( final ClassNotFoundException e ) {
            throw new RuntimeDroolsException( e );
        } catch ( final InstantiationError e ) {
            throw new RuntimeDroolsException( e );
        } catch ( final IllegalAccessException e ) {
            throw new RuntimeDroolsException( e );
        } catch ( final InstantiationException e ) {
            throw new RuntimeDroolsException( e );
        }

        this.dirty = false;
    }

    public void clear() {
        getStore().clear();
        getInvokers().clear();
        reload();
    }

    public String toString() {
        return this.getClass().getName() + getStore().toString();
    }

    public void putInvoker(final String className,
                           final Object invoker) {
        getInvokers().put( className,
                           invoker );
    }

    public void putAllInvokers(final Map invokers) {
        getInvokers().putAll( invokers );

    }

    public Map getInvokers() {
        if ( this.invokerLookups == null ) {
            this.invokerLookups = new HashMap();
        }
        return this.invokerLookups;
    }

    public void removeInvoker(final String className) {
        getInvokers().remove( className );
    }

    public static class PackageClassLoader extends ClassLoader
        implements
        DroolsClassLoader {
        private JavaDialectRuntimeData store;
        DroolsCompositeClassLoader     rootClassLoader;

        private Map<String, Object>    cache           = new HashMap<String, Object>();
        private long                   successfulCalls = 0;
        private long                   failedCalls     = 0;
        private long                   cacheHits       = 0;

        public PackageClassLoader(JavaDialectRuntimeData store,
                                  DroolsCompositeClassLoader rootClassLoader) {
            super( rootClassLoader );
            this.rootClassLoader = rootClassLoader;
            this.store = store;
        }

        public Class< ? > fastFindClass(final String name) {
            Class< ? > cls = findLoadedClass( name );

            if ( cls == null ) {
                final byte[] clazzBytes = this.store.read( convertClassToResourcePath( name ) );
                if ( clazzBytes != null ) {
                    String pkgName = name.substring( 0,
                                                     name.lastIndexOf( '.' ) );
                    if ( getPackage( pkgName ) == null ) {
                        definePackage( pkgName,
                                       "",
                                       "",
                                       "",
                                       "",
                                       "",
                                       "",
                                       null );
                    }
                    cls = defineClass( name,
                                       clazzBytes,
                                       0,
                                       clazzBytes.length,
                                       PROTECTION_DOMAIN );
                }
            }

            return cls;
        }

        public Class< ? > loadClass(final String name,
                                    final boolean resolve) throws ClassNotFoundException {
            try {
                if( cache.containsKey( name ) ) {
                    this.cacheHits++;
                    Object result = cache.get( name );
                    if( result instanceof ClassNotFoundException ) {
                        throw (ClassNotFoundException) result;
                    } else {
                        return (Class<?>) result;
                    }
                }
                Class< ? > cls = fastFindClass( name );
                if ( cls == null ) {
                    final ClassLoader parent = getParent();
                    if ( parent != null ) {
                        cls = Class.forName( name,
                                             true,
                                             parent );
                    }
                }
                if ( resolve && cls != null ) {
                    resolveClass( cls );
                }
                if ( cls != null ) {
                    this.successfulCalls++;
                } else {
                    this.failedCalls++;
                }
                cache.put( name, cls );
                return cls;
            } catch ( ClassNotFoundException e ) {
                this.failedCalls++;
                cache.put( name, e );
                throw e;
            }
        }

        protected Class< ? > findClass(final String name) throws ClassNotFoundException {
            return fastFindClass( name );
        }

        public InputStream getResourceAsStream(final String name) {
            final byte[] clsBytes = this.store.read( name );
            if ( clsBytes != null ) {
                return new ByteArrayInputStream( clsBytes );
            }
            return null;
        }
        
        public void reset() {
            this.cacheHits = this.failedCalls = this.successfulCalls = 0;
            this.cache.clear();
        }

        public void printStats() {
            System.out.println( "CacheHits = " + this.cacheHits + "\nSuccessful=" + this.successfulCalls + "\nFailed=" + this.failedCalls + "\n" );

        }
    }

    /**
     * Please do not use - internal
     * org/my/Class.xxx -> org.my.Class
     */
    public static String convertResourceToClassName(final String pResourceName) {
        return stripExtension( pResourceName ).replace( '/',
                                                        '.' );
    }

    /**
     * Please do not use - internal
     * org.my.Class -> org/my/Class.class
     */
    public static String convertClassToResourcePath(final String pName) {
        return pName.replace( '.',
                              '/' ) + ".class";
    }

    /**
     * Please do not use - internal
     * org/my/Class.xxx -> org/my/Class
     */
    public static String stripExtension(final String pResourceName) {
        final int i = pResourceName.lastIndexOf( '.' );
        final String withoutExtension = pResourceName.substring( 0,
                                                                 i );
        return withoutExtension;
    }

}