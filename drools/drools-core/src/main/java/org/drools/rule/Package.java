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
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.drools.facttemplates.FactTemplate;
import org.drools.spi.TypeResolver;

/**
 * Collection of related <code>Rule</code>s.
 * 
 * @see Rule
 * 
 * @author <a href="mail:bob@werken.com">bob mcwhirter </a>
 * 
 * @version $Id: Package.java,v 1.1 2005/07/26 01:06:31 mproctor Exp $
 */
public class Package
    implements
    Externalizable {
    // ------------------------------------------------------------
    // Constants`
    // ------------------------------------------------------------

    /**
     * 
     */
    private static final long      serialVersionUID = 320;

    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** Name of the pkg. */
    private String                 name;

    /** Set of all rule-names in this <code>Package</code>. */
    private Map                    rules;

    private List                   imports;
    
    private List                   functions;

    private List                   functionImports;

    private Map                    globals;

    private Map                    factTemplates;

    // @todo: add attributes to Package
    //private Map                   attributes;

    private PackageCompilationData packageCompilationData;

    /** This is to indicate the the package has no errors during the compilation/building phase */
    private boolean                valid            = true;

    /** This will keep a summary error message as to why this package is not valid */
    private String                 errorSummary;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Default constructor - for Externalizable. This should never be used by a user, as it 
     * will result in an invalid state for the instance.
     */
    public Package() {

    }

    /**
     * Construct.
     * 
     * @param name
     *            The name of this <code>Package</code>.
     */
    public Package(final String name) {
        this( name,
              null );
    }

    /**
     * Construct.
     * 
     * @param name
     *            The name of this <code>Package</code>.
     */
    public Package(final String name,
                   final ClassLoader parentClassLoader) {
        this.name = name;
        this.imports = new ArrayList( 1 );
        this.functionImports = Collections.EMPTY_LIST;
        this.rules = new LinkedHashMap();
        this.globals = Collections.EMPTY_MAP;
        this.factTemplates = Collections.EMPTY_MAP;
        this.functions = Collections.EMPTY_LIST;
        this.packageCompilationData = new PackageCompilationData( parentClassLoader );
    }

    /**
     * Handles the write serialization of the Package. Patterns in Rules may reference generated data which cannot be serialized by default methods.
     * The Package uses PackageCompilationData to hold a reference to the generated bytecode. The generated bytecode must be restored before any Rules.
     * 
     */
    public void writeExternal(final ObjectOutput stream) throws IOException {
        stream.writeObject( this.packageCompilationData );
        stream.writeObject( this.name );
        stream.writeObject( this.imports );
        stream.writeObject( this.functionImports );
        stream.writeObject( this.globals );

        // Rules must be restored by an ObjectInputStream that can resolve using a given ClassLoader to handle seaprately by storing as
        // a byte[]
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( this.rules );
        stream.writeObject( bos.toByteArray() );
    }

    /**
     * Handles the read serialization of the Package. Patterns in Rules may reference generated data which cannot be serialized by default methods.
     * The Package uses PackageCompilationData to hold a reference to the generated bytecode; which must be restored before any Rules.
     * A custom ObjectInputStream, able to resolve classes against the bytecode in the PackageCompilationData, is used to restore the Rules.
     * 
     */
    public void readExternal(final ObjectInput stream) throws IOException,
                                                      ClassNotFoundException {
        // PackageCompilationData must be restored before Rules as it has the ClassLoader needed to resolve the generated code references in Rules
        this.packageCompilationData = (PackageCompilationData) stream.readObject();
        this.name = (String) stream.readObject();
        this.imports = (List) stream.readObject();
        this.functionImports = (List) stream.readObject();
        this.globals = (Map) stream.readObject();

        // Return the rules stored as a byte[]
        final byte[] bytes = (byte[]) stream.readObject();

        //  Use a custom ObjectInputStream that can resolve against a given classLoader
        final ObjectInputStreamWithLoader streamWithLoader = new ObjectInputStreamWithLoader( new ByteArrayInputStream( bytes ),
                                                                                              this.packageCompilationData.getClassLoader() );

        this.rules = (Map) streamWithLoader.readObject();
    }

    private static class ObjectInputStreamWithLoader extends ObjectInputStream {
        private final ClassLoader classLoader;

        public ObjectInputStreamWithLoader(final InputStream in,
                                           final ClassLoader classLoader) throws IOException {
            super( in );
            this.classLoader = classLoader;
            enableResolveObject( true );
        }

        protected Class resolveClass(final ObjectStreamClass desc) throws IOException,
                                                                  ClassNotFoundException {
            if ( this.classLoader == null ) {
                return super.resolveClass( desc );
            } else {
                final String name = desc.getName();
                return this.classLoader.loadClass( name );
            }
        }
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Retrieve the name of this <code>Package</code>.
     * 
     * @return The name of this <code>Package</code>.
     */
    public String getName() {
        return this.name;
    }

    public void addImport(final String importEntry) {
        this.imports.add( importEntry );
    }

    public void removeImport(final String importEntry) {
        this.imports.remove( importEntry );
    }

    public List getImports() {
        return this.imports;
    }

    public void addFunctionImport(final String functionImport) {
        if ( this.functionImports == Collections.EMPTY_LIST ) {
            this.functionImports = new ArrayList( 1 );
        }
        this.functionImports.add( functionImport );
    }
    
    public void addFunction(String functionName) {
        if ( this.functions == Collections.EMPTY_LIST ) {
            this.functions = new ArrayList( 1 );
        }
        
        this.functions.add( functionName );
    }    
    
    public List getFunctions() {
        return this.functions;
    }

    public void removeFunctionImport(final String functionImport) {
        this.functionImports.remove( functionImport );
    }

    public List getFunctionImports() {
        return this.functionImports;
    }

    public void addGlobal(final String identifier,
                          final Class clazz) {
        if ( this.globals == Collections.EMPTY_MAP ) {
            this.globals = new HashMap( 1 );
        }
        this.globals.put( identifier,
                          clazz );
    }

    public void removeGlobal(final String identifier) {
        this.globals.remove( identifier );
    }

    public Map getGlobals() {
        return this.globals;
    }

    public void removeFunction(final String functionName) {
        this.functions.remove( functionName );
        this.packageCompilationData.remove( this.name + "." + ucFirst( functionName ) );
    }

    public FactTemplate getFactTemplate(final String name) {
        return (FactTemplate) this.factTemplates.get( name );
    }

    public void addFactTemplate(FactTemplate factTemplate) {
        if ( this.factTemplates == Collections.EMPTY_MAP ) {
            this.factTemplates = new HashMap( 1 );
        }
        this.factTemplates.put( factTemplate.getName(),
                                factTemplate );
    }

    /**
     * Add a <code>Rule</code> to this <code>Package</code>.
     * 
     * @param rule
     *            The rule to add.
     * 
     * @throws DuplicateRuleNameException
     *             If the <code>Rule</code> attempting to be added has the
     *             same name as another previously added <code>Rule</code>.
     * @throws InvalidRuleException
     *             If the <code>Rule</code> is not valid.
     */
    public void addRule(final Rule rule) {
        final String name = rule.getName();

        this.rules.put( name,
                        rule );
        rule.setLoadOrder( this.rules.size() );
    }

    public void removeRule(final Rule rule) {
        this.rules.remove( rule.getName() );
        final String consequenceName = rule.getConsequence().getClass().getName();
        this.packageCompilationData.remove( consequenceName );

        removeClasses( rule.getLhs() );

        // Now remove the rule class - the name is a subset of the consequence name
        this.packageCompilationData.remove( consequenceName.substring( 0,
                                                                       consequenceName.indexOf( "ConsequenceInvoker" ) ) );
    }

    private void removeClasses(final ConditionalElement ce) {
        if ( ce instanceof GroupElement ) {
            final GroupElement group = (GroupElement) ce;
            for ( final Iterator it = group.getChildren().iterator(); it.hasNext(); ) {
                final Object object = it.next();
                if ( object instanceof ConditionalElement ) {
                    removeClasses( (ConditionalElement) object );
                } else if ( object instanceof Column ) {
                    removeClasses( (Column) object );
                }
            }
        } else if ( ce instanceof EvalCondition ) {
            this.packageCompilationData.remove( ((EvalCondition) ce).getEvalExpression().getClass().getName() );
        }
    }

    private void removeClasses(final Column column) {
        for ( final Iterator it = column.getConstraints().iterator(); it.hasNext(); ) {
            final Object object = it.next();
            if ( object instanceof PredicateConstraint ) {
                this.packageCompilationData.remove( ((PredicateConstraint) object).getPredicateExpression().getClass().getName() );
            } else if ( object instanceof ReturnValueConstraint ) {
                this.packageCompilationData.remove( ((ReturnValueConstraint) object).getExpression().getClass().getName() );
            }
        }
    }

    /**
     * Retrieve a <code>Rule</code> by name.
     * 
     * @param name
     *            The name of the <code>Rule</code> to retrieve.
     * 
     * @return The named <code>Rule</code>, or <code>null</code> if not
     *         such <code>Rule</code> has been added to this
     *         <code>Package</code>.
     */
    public Rule getRule(final String name) {
        return (Rule) this.rules.get( name );
    }

    /**
     * Retrieve all <code>Rules</code> in this <code>Package</code>.
     * 
     * @return An array of all <code>Rules</code> in this <code>Package</code>.
     */
    public Rule[] getRules() {
        return (Rule[]) this.rules.values().toArray( new Rule[this.rules.size()] );
    }

    public PackageCompilationData getPackageCompilationData() {
        return this.packageCompilationData;
    }

    public String toString() {
        return "[Package name=" + this.name + "]";
    }

    /** Once this is called, the package will be marked as invalid */
    public void setError(final String summary) {
        this.errorSummary = summary;
        this.valid = false;
    }

    /**
     * @return true (default) if there are no build/structural problems.
     */
    public boolean isValid() {
        return this.valid;
    }

    /** This will throw an exception if the package is not valid */
    public void checkValidity() {
        if ( !isValid() ) {
            throw new InvalidRulePackage( this.getErrorSummary() );
        }
    }

    /**
     * This will return the error summary (if any) if the package is invalid.
     */
    public String getErrorSummary() {
        return this.errorSummary;
    }

    public boolean equals(final Object object) {
        if ( this == object ) {
            return true;
        }

        if ( object == null || !(object instanceof Package) ) {
            return false;
        }

        final Package other = (Package) object;

        return (this.name.equals( other.name ));
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    private String ucFirst(final String name) {
        return name.toUpperCase().charAt( 0 ) + name.substring( 1 );
    }

    public void clear() {
        this.rules.clear();
        this.packageCompilationData.clear();
    }
}