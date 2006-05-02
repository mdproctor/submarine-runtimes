package org.drools.common;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.drools.FactException;
import org.drools.PackageIntegrationException;
import org.drools.RuleBase;
import org.drools.RuleIntegrationException;
import org.drools.WorkingMemory;
import org.drools.rule.CompositePackageClassLoader;
import org.drools.rule.InvalidPatternException;
import org.drools.rule.Package;
import org.drools.rule.PackageCompilationData;
import org.drools.rule.Rule;
import org.drools.spi.FactHandleFactory;

/**
 * Implementation of <code>RuleBase</code>.
 * 
 * @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 * @author <a href="mailto:mark.proctor@jboss.com">Mark Proctor</a> 
 * 
 * @version $Id: RuleBaseImpl.java,v 1.5 2005/08/14 22:44:12 mproctor Exp $
 */
abstract public class AbstractRuleBase
    implements
    RuleBase,
    Externalizable {
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    private Map                                   pkgs;

    private transient CompositePackageClassLoader packageClassLoader;

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
    private static final Object     PRESENT          = new Object();

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     * 
     * @param rete
     *            The rete network.
     */
    public AbstractRuleBase(FactHandleFactory factHandleFactory) {
        this.factHandleFactory = factHandleFactory;

        this.packageClassLoader = new CompositePackageClassLoader( Thread.currentThread().getContextClassLoader() );
        this.pkgs = new HashMap();
        this.globals = new HashMap();
        this.workingMemories = new WeakHashMap();       
    }

    /**
     * Handles the write serialization of the Package. Patterns in Rules may
     * reference generated data which cannot be serialized by default methods.
     * The Package uses PackageCompilationData to hold a reference to the
     * generated bytecode. The generated bytecode must be restored before any
     * Rules.
     * 
     */
    public void writeExternal(ObjectOutput stream) throws IOException {
        stream.writeObject( this.pkgs );

        // Rules must be restored by an ObjectInputStream that can resolve using a given ClassLoader to handle seaprately by storing as
        // a byte[]
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( this.factHandleFactory );
        out.writeObject( this.globals );

        stream.writeObject( bos.toByteArray() );
    }
    /**
     * Handles the read serialization of the Package. Patterns in Rules may
     * reference generated data which cannot be serialized by default methods.
     * The Package uses PackageCompilationData to hold a reference to the
     * generated bytecode; which must be restored before any Rules. A custom
     * ObjectInputStream, able to resolve classes against the bytecode in the
     * PackageCompilationData, is used to restore the Rules.
     * 
     */
    public void readExternal( ObjectInput stream ) throws IOException,
            ClassNotFoundException {
        // PackageCompilationData must be restored before Rules as it has the
        // ClassLoader needed to resolve the generated code references in Rules
        this.pkgs = (Map) stream.readObject( );

        this.packageClassLoader = new CompositePackageClassLoader( Thread.currentThread( )
                                                                         .getContextClassLoader( ) );
        for (Iterator it = this.pkgs.values( ).iterator( ); it.hasNext( );) {
            this.packageClassLoader.addClassLoader( ( (Package) it.next( ) ).getPackageCompilationData( )
                                                                            .getClassLoader( ) );
        }

        // Return the rules stored as a byte[]
        byte[] bytes = (byte[]) stream.readObject( );

        // Use a custom ObjectInputStream that can resolve against a given
        // classLoader
        ObjectInputStreamWithLoader streamWithLoader = new ObjectInputStreamWithLoader( new ByteArrayInputStream( bytes ),
                                                                                        this.packageClassLoader );

        this.factHandleFactory = (FactHandleFactory) streamWithLoader.readObject( );
        this.globals = (Map) streamWithLoader.readObject( );

        this.workingMemories = new WeakHashMap( );
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
    abstract public WorkingMemory newWorkingMemory(boolean keepReference);
    
    public void disposeWorkingMemory(WorkingMemory workingMemory) {
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
            AbstractWorkingMemory workingMemory = (AbstractWorkingMemory) it.next();
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
            AbstractWorkingMemory workingMemory = (AbstractWorkingMemory) it.next();

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
        for ( Iterator it = globals.keySet().iterator(); it.hasNext(); ) {
            String identifier = (String) it.next();
            Class type = (Class) globals.get( identifier );
            if ( globals.containsKey( identifier ) && !globals.get( identifier ).equals( type ) ) {
                throw new PackageIntegrationException( "Unable to merge new Package",
                                                       newPkg );
            }
        }
    }

    protected void addRule(Rule rule) throws InvalidPatternException {
        if ( !rule.isValid() ) {
            throw new IllegalArgumentException( "The rule called " + rule.getName() + " is not valid. Check for compile errors reported." );
        }
    }   

    public void removePackage(String packageName) {
        Package pkg = (Package) this.pkgs.get( packageName );
        // Iterate each workingMemory and lock it
        // This is so we don't update the Rete network during propagation
        for ( Iterator it = this.workingMemories.keySet().iterator(); it.hasNext(); ) {
            AbstractWorkingMemory workingMemory = (AbstractWorkingMemory) it.next();
            workingMemory.getLock().lock();
        }         
        
        Rule[] rules = pkg.getRules();

            for ( int i = 0; i < rules.length; ++i ) {
                removeRule( rules[i] );
            }

        this.packageClassLoader.removeClassLoader( pkg.getPackageCompilationData().getClassLoader() );

        pkg.clear();

        // Iterate and unlock
        for ( Iterator it = this.workingMemories.keySet().iterator(); it.hasNext(); ) {
            AbstractWorkingMemory workingMemory = (AbstractWorkingMemory) it.next();
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
            AbstractWorkingMemory workingMemory = (AbstractWorkingMemory) it.next();
            workingMemory.getLock().lock();
        }         
        removeRule( rule );
        pkg.removeRule( rule );

        // Iterate and unlock
        for ( Iterator it = this.workingMemories.keySet().iterator(); it.hasNext(); ) {
            AbstractWorkingMemory workingMemory = (AbstractWorkingMemory) it.next();
            workingMemory.getLock().unlock();
        }         
    }

    abstract protected void removeRule(Rule rule);

    protected void addWorkingMemory( WorkingMemory workingMemory, boolean keepReference ) {
        if (keepReference) {
            this.workingMemories.put( workingMemory, PRESENT );
        }
    }
    public Set getWorkingMemories() {
        return this.workingMemories.keySet();
    }
}
