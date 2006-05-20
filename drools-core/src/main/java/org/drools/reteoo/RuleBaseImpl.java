package org.drools.reteoo;

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
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.drools.FactException;
import org.drools.FactHandle;
import org.drools.PackageIntegrationException;
import org.drools.RuleBase;
import org.drools.RuleBaseConfiguration;
import org.drools.RuleIntegrationException;
import org.drools.WorkingMemory;
import org.drools.common.ObjectInputStreamWithLoader;
import org.drools.common.PropagationContextImpl;
import org.drools.rule.CompositePackageClassLoader;
import org.drools.rule.InvalidPatternException;
import org.drools.rule.Package;
import org.drools.rule.PackageCompilationData;
import org.drools.rule.Rule;
import org.drools.spi.ClassObjectTypeResolver;
import org.drools.spi.FactHandleFactory;
import org.drools.spi.ObjectTypeResolver;
import org.drools.spi.PropagationContext;

/**
 * Implementation of <code>RuleBase</code>.
 * 
 * @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 * @author <a href="mailto:mark.proctor@jboss.com">Mark Proctor</a> 
 * 
 * @version $Id: RuleBaseImpl.java,v 1.5 2005/08/14 22:44:12 mproctor Exp $
 */
public class RuleBaseImpl
    implements
    RuleBase,
    Externalizable {
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------
    private RuleBaseConfiguration                 config;

    private Map                                   pkgs;

    private transient CompositePackageClassLoader packageClassLoader;

    /** The root Rete-OO for this <code>RuleBase</code>. */
    private Rete                                  rete;

    private ReteooBuilder                         reteooBuilder;

    /** The fact handle factory. */
    private FactHandleFactory                     factHandleFactory;

    private Map                                   globals;

    // @todo: replace this with a weak HashSet
    /**
     * WeakHashMap to keep references of WorkingMemories but allow them to be
     * garbage collected
     */
    private transient Map                         workingMemories;

    /** Special value when adding to the underlying map. */
    private static final Object                   PRESENT = new Object();
    
    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     * 
     * @param rete
     *            The rete network.
     */
    public RuleBaseImpl() {
        this( null, new DefaultFactHandleFactory() );
    }

    /**
     * @param factHandleFactory
     */
    public RuleBaseImpl(FactHandleFactory factHandleFactory) {
        this( null, factHandleFactory );
    }

    /**
     * @param config
     */
    public RuleBaseImpl(RuleBaseConfiguration config) {
        this( config, new DefaultFactHandleFactory() );
    }

    /**
     * Construct.
     * 
     * @param rete
     *            The rete network.
     */
    public RuleBaseImpl(RuleBaseConfiguration config, FactHandleFactory factHandleFactory) {
        this.config = ( config != null ) ? config : new RuleBaseConfiguration();
        this.config.makeImmutable();
        
        ObjectTypeResolver resolver = new ClassObjectTypeResolver();
        this.rete = new Rete( resolver );
        this.reteooBuilder = new ReteooBuilder( this,
                                                resolver );
        this.factHandleFactory = factHandleFactory;

        this.packageClassLoader = new CompositePackageClassLoader( Thread.currentThread().getContextClassLoader() );
        this.pkgs = new HashMap();
        this.globals = new HashMap();
        this.workingMemories = new WeakHashMap();
    }

    /**
     * Handles the write serialization of the Package. Patterns in Rules may reference generated data which cannot be serialized by default methods.
     * The Package uses PackageCompilationData to hold a reference to the generated bytecode. The generated bytecode must be restored before any Rules.
     * 
     */
    public void writeExternal(ObjectOutput stream) throws IOException {
        stream.writeObject( this.pkgs );

        // Rules must be restored by an ObjectInputStream that can resolve using a given ClassLoader to handle seaprately by storing as
        // a byte[]
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( this.rete );
        out.writeObject( this.reteooBuilder );
        out.writeObject( this.factHandleFactory );
        out.writeObject( this.globals );
        out.writeObject( this.config );

        stream.writeObject( bos.toByteArray() );
    }

    /**
     * Handles the read serialization of the Package. Patterns in Rules may reference generated data which cannot be serialized by default methods.
     * The Package uses PackageCompilationData to hold a reference to the generated bytecode; which must be restored before any Rules.
     * A custom ObjectInputStream, able to resolve classes against the bytecode in the PackageCompilationData, is used to restore the Rules.
     * 
     */
    public void readExternal(ObjectInput stream) throws IOException,
                                                ClassNotFoundException {
        // PackageCompilationData must be restored before Rules as it has the ClassLoader needed to resolve the generated code references in Rules
        this.pkgs = (Map) stream.readObject();

        this.packageClassLoader = new CompositePackageClassLoader( Thread.currentThread().getContextClassLoader() );
        for ( Iterator it = this.pkgs.values().iterator(); it.hasNext(); ) {
            this.packageClassLoader.addClassLoader( ((Package) it.next()).getPackageCompilationData().getClassLoader() );
        }

        // Return the rules stored as a byte[]
        byte[] bytes = (byte[]) stream.readObject();

        //  Use a custom ObjectInputStream that can resolve against a given classLoader
        ObjectInputStreamWithLoader streamWithLoader = new ObjectInputStreamWithLoader( new ByteArrayInputStream( bytes ),
                                                                                        this.packageClassLoader );

        this.rete = (Rete) streamWithLoader.readObject();
        this.reteooBuilder = (ReteooBuilder) streamWithLoader.readObject();

        this.reteooBuilder.setRuleBase( this );
        this.reteooBuilder.setRete( this.rete );

        this.factHandleFactory = (FactHandleFactory) streamWithLoader.readObject();
        this.globals = (Map) streamWithLoader.readObject();
        
        this.config = (RuleBaseConfiguration) streamWithLoader.readObject();

        this.workingMemories = new WeakHashMap();
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * @see RuleBase
     */
    public WorkingMemory newWorkingMemory() {
        return newWorkingMemory( true );
    }

    /**
     * @see RuleBase
     */
    public WorkingMemory newWorkingMemory(boolean keepReference) {
        WorkingMemoryImpl workingMemory = new WorkingMemoryImpl( this );
        if ( keepReference ) {
            this.workingMemories.put( workingMemory,
                                      RuleBaseImpl.PRESENT );
        }

        InitialFactHandle handle = new InitialFactHandle( (FactHandleImpl) workingMemory.newFactHandle() );

        PropagationContext propagationContext = new PropagationContextImpl( workingMemory.getNextPropagationIdCounter(),
                                                                            PropagationContext.ASSERTION,
                                                                            null,
                                                                            null );

        assertObject( handle,
                      handle.getObject(),
                      propagationContext,
                      workingMemory );

        return workingMemory;
    }

    void disposeWorkingMemory(WorkingMemory workingMemory) {
        this.workingMemories.remove( workingMemory );
    }

    /**
     * @see RuleBase
     */
    public FactHandleFactory getFactHandleFactory() {
        return this.factHandleFactory;
    }

    public FactHandleFactory newFactHandleFactory() {
        return this.factHandleFactory.newInstance();
    }

    /**
     * Retrieve the Rete-OO network for this <code>RuleBase</code>.
     * 
     * @return The RETE-OO network.
     */
    Rete getRete() {
        return this.rete;
    }

    /**
     * Assert a fact object.
     * 
     * @param handle
     *            The handle.
     * @param object
     *            The fact.
     * @param workingMemory
     *            The working-memory.
     * 
     * @throws FactException
     *             If an error occurs while performing the assertion.
     */
    void assertObject(FactHandle handle,
                      Object object,
                      PropagationContext context,
                      WorkingMemoryImpl workingMemory) throws FactException {
        getRete().assertObject( (FactHandleImpl) handle,
                                context,
                                workingMemory );
    }

    void modifyObject(FactHandle handle,
                      PropagationContext context,
                      WorkingMemoryImpl workingMemory) throws FactException {
        getRete().modifyObject( (FactHandleImpl) handle,
                                context,
                                workingMemory );
    }

    /**
     * Retract a fact object.
     * 
     * @param handle
     *            The handle.
     * @param workingMemory
     *            The working-memory.
     * 
     * @throws FactException
     *             If an error occurs while performing the retraction.
     */
    void retractObject(FactHandle handle,
                       PropagationContext context,
                       WorkingMemoryImpl workingMemory) throws FactException {
        getRete().retractObject( (FactHandleImpl) handle,
                                 context,
                                 workingMemory );
    }

    public Package[] getPackages() {
        return (Package[]) this.pkgs.values().toArray( new Package[this.pkgs.size()] );
    }

    public Map getGlobals() {
        return this.globals;
    }

    /**
     * Add a <code>Package</code> to the network. Iterates through the
     * <code>Package</code> adding Each individual <code>Rule</code> to the
     * network. Before update network each referenced <code>WorkingMemory</code>
     * is locked.
     * 
     * @param pkg
     *            The package to add.
     * @throws PackageIntegrationException 
     * 
     * @throws RuleIntegrationException
     *             if an error prevents complete construction of the network for
     *             the <code>Rule</code>.
     * @throws FactException
     * @throws InvalidPatternException
     */
    public void addPackage(Package newPkg) throws PackageIntegrationException {
        newPkg.checkValidity();
        Package pkg = (Package) this.pkgs.get( newPkg.getName() );
        
        // Iterate each workingMemory and lock it
        // This is so we don't update the Rete network during propagation
        for ( Iterator it = this.workingMemories.keySet().iterator(); it.hasNext(); ) {
            WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) it.next();
            workingMemory.getLock().lock();
        }  
        
        if ( pkg != null ) {
            mergePackage( pkg,
                          newPkg );
        } else {
            this.pkgs.put( newPkg.getName(),
                           newPkg );
        }

        Map newGlobals = newPkg.getGlobals();

        // Check that the global data is valid, we cannot change the type
        // of an already declared global variable
        for ( Iterator it = newGlobals.keySet().iterator(); it.hasNext(); ) {
            String identifier = (String) it.next();
            Class type = (Class) newGlobals.get( identifier );
            if ( this.globals.containsKey( identifier ) && !this.globals.get( identifier ).equals( type ) ) {
                throw new PackageIntegrationException( pkg );
            }
        }
        this.globals.putAll( newGlobals );

        Rule[] rules = newPkg.getRules();

        for ( int i = 0; i < rules.length; ++i ) {
            addRule( rules[i] );
        }

        this.packageClassLoader.addClassLoader( newPkg.getPackageCompilationData().getClassLoader() );
        
        // Iterate each workingMemory and attempt to fire any rules, that were activated as a result 
        // of the new rule addition. Unlock after fireAllRules();
        for ( Iterator it = this.workingMemories.keySet().iterator(); it.hasNext(); ) {
            WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) it.next();

            workingMemory.fireAllRules();
            workingMemory.getLock().unlock();
        }        
    }

    private void mergePackage(Package pkg,
                              Package newPkg) throws PackageIntegrationException {
        Map globals = pkg.getGlobals();
        List imports = pkg.getImports();

        // First update the binary files
        // @todo: this probably has issues if you add classes in the incorrect order - functions, rules, invokers.
        PackageCompilationData compilationData = pkg.getPackageCompilationData();
        PackageCompilationData newCompilationData = newPkg.getPackageCompilationData();
        String[] files = newCompilationData.list();
        for ( int i = 0, length = files.length; i < length; i++ ) {
            compilationData.write( files[i],
                                   newCompilationData.read( files[i] ) );
        }

        // Merge imports
        imports.addAll( newPkg.getImports() );

        // Add invokers
        compilationData.putAllInvokers( newCompilationData.getInvokers() );

        // Add globals
        for ( Iterator it = newPkg.getGlobals().keySet().iterator(); it.hasNext(); ) {
            String identifier = (String) it.next();
            Class type = (Class) globals.get( identifier );
            if ( globals.containsKey( identifier ) && !globals.get( identifier ).equals( type ) ) {
                throw new PackageIntegrationException( "Unable to merge new Package",
                                                       newPkg );
            }
        }
        globals.putAll( newPkg.getGlobals() );
    }

    private void addRule(Rule rule) throws InvalidPatternException {
        if ( !rule.isValid() ) {
            throw new IllegalArgumentException( "The rule called " + rule.getName() + " is not valid. Check for compile errors reported." );
        }

        // This adds the rule. ReteBuilder has a reference to the WorkingMemories and will propagate any existing facts.
        this.reteooBuilder.addRule( rule );
    }   

    public void removePackage(String packageName) {
        Package pkg = (Package) this.pkgs.get( packageName );
        // Iterate each workingMemory and lock it
        // This is so we don't update the Rete network during propagation
        for ( Iterator it = this.workingMemories.keySet().iterator(); it.hasNext(); ) {
            WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) it.next();
            workingMemory.getLock().lock();
        }         
        
        Rule[] rules = pkg.getRules();

            for ( int i = 0; i < rules.length; ++i ) {
                removeRule( rules[i] );
            }

        this.packageClassLoader.removeClassLoader( pkg.getPackageCompilationData().getClassLoader() );

        pkg.clear();

        // getting the list of referenced globals 
        Set referencedGlobals = new HashSet();
        for( Iterator it = this.pkgs.values().iterator(); it.hasNext(); ) {
            org.drools.rule.Package pkgref = (org.drools.rule.Package) it.next();
            if(pkgref != pkg) {
                referencedGlobals.addAll( pkgref.getGlobals().keySet() );
            }
        }
        // removing globals declared inside the package that are not shared
        for( Iterator it = pkg.getGlobals().keySet().iterator(); it.hasNext(); ) {
            String globalName = (String) it.next();
            if( !referencedGlobals.contains( globalName ) ) {
                this.globals.remove( globalName );
            }
        }
        // removing the package itself from the list
        this.pkgs.remove( pkg.getName() );

        // Iterate and unlock
        for ( Iterator it = this.workingMemories.keySet().iterator(); it.hasNext(); ) {
            WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) it.next();
            workingMemory.getLock().unlock();
        }         
    }

    public void removeRule(String packageName,
                           String ruleName) {
        Package pkg = (Package) this.pkgs.get( packageName );
        Rule rule = pkg.getRule( ruleName );
        // Iterate each workingMemory and lock it
        // This is so we don't update the Rete network during propagation
        for ( Iterator it = this.workingMemories.keySet().iterator(); it.hasNext(); ) {
            WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) it.next();
            workingMemory.getLock().lock();
        }         
        removeRule( rule );
        pkg.removeRule( rule );

        // Iterate and unlock
        for ( Iterator it = this.workingMemories.keySet().iterator(); it.hasNext(); ) {
            WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) it.next();
            workingMemory.getLock().unlock();
        }         
    }

    private void removeRule(Rule rule) {
        this.reteooBuilder.removeRule( rule );
    }

    public Set getWorkingMemories() {
        return this.workingMemories.keySet();
    }
    
    public RuleBaseConfiguration getConfiguration() {
        return this.config;
    }

    //    /**
    //     * This is to allow the RuleBase to be serializable.
    //     */
    //    private void readObject(ObjectInputStream is) throws ClassNotFoundException,
    //                                                 IOException,
    //                                                 Exception {
    //        //always perform the default de-serialization first
    //        is.defaultReadObject();
    //        
    //        this.lock = new Object();
    //
    //        ObjectTypeResolver resolver = new ClassObjectTypeResolver();
    //        this.rete = new Rete( resolver );
    //        this.reteooBuilder = new ReteooBuilder( this,
    //                                                resolver );
    //        this.globals = new HashMap();
    //        this.workingMemories = new WeakHashMap();
    //
    //        Package[] packages = this.getPackages();
    //        this.pkgs.clear();
    //        for ( int i = 0; i < packages.length; i++ ) {
    //            this.addPackage( packages[i] );
    //        }
    //    }

}
