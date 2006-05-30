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
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.drools.CheckedDroolsException;
import org.drools.RuntimeDroolsException;
import org.drools.common.ObjectInputStreamWithLoader;
import org.drools.spi.Consequence;
import org.drools.spi.EvalExpression;
import org.drools.spi.PredicateExpression;
import org.drools.spi.ReturnValueExpression;

public class PackageCompilationData
    implements
    Externalizable {

    /**
     * 
     */
    private static final long            serialVersionUID = -4351259299237235523L;

    private Map                          invokerLookups   = new HashMap();

    private Object                       AST;

    private Map                          store            = new HashMap();

    private transient PackageClassLoader classLoader;

    private transient ClassLoader        parentClassLoader;

    public PackageCompilationData() {
        this( null );
    }

    public PackageCompilationData(final ClassLoader parentClassLoader) {
        init( parentClassLoader );
    }

    private void init(ClassLoader parentClassLoader) {
        if ( parentClassLoader == null ) {
            parentClassLoader = Thread.currentThread().getContextClassLoader();

            if ( parentClassLoader == null ) {
                parentClassLoader = getClass().getClassLoader();
            }
        }

        this.parentClassLoader = parentClassLoader;
        this.classLoader = new PackageClassLoader( this.parentClassLoader );
    }

    /**
     * Handles the write serialization of the PackageCompilationData. Patterns in Rules may reference generated data which cannot be serialized by 
     * default methods. The PackageCompilationData holds a reference to the generated bytecode. The generated bytecode must be restored before any Rules.
     * 
     */
    public void writeExternal(final ObjectOutput stream) throws IOException {
        stream.writeObject( this.store );
        stream.writeObject( this.AST );

        // Rules must be restored by an ObjectInputStream that can resolve using a given ClassLoader to handle seaprately by storing as
        // a byte[]        
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( this.invokerLookups );
        stream.writeObject( bos.toByteArray() );
    }

    /**
     * Handles the read serialization of the PackageCompilationData. Patterns in Rules may reference generated data which cannot be serialized by 
     * default methods. The PackageCompilationData holds a reference to the generated bytecode; which must be restored before any Rules.
     * A custom ObjectInputStream, able to resolve classes against the bytecode, is used to restore the Rules.
     * 
     */
    public void readExternal(final ObjectInput stream) throws IOException,
                                                      ClassNotFoundException {
        init( null );

        this.store = (Map) stream.readObject();
        this.AST = stream.readObject();

        // Return the rules stored as a byte[]
        final byte[] bytes = (byte[]) stream.readObject();

        //  Use a custom ObjectInputStream that can resolve against a given classLoader
        final ObjectInputStreamWithLoader streamWithLoader = new ObjectInputStreamWithLoader( new ByteArrayInputStream( bytes ),
                                                                                              this.classLoader );
        this.invokerLookups = (Map) streamWithLoader.readObject();
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public byte[] read(final String resourceName) {
        final byte[] bytes = null;

        if ( this.store != null ) {
            return (byte[]) this.store.get( resourceName.replace( '.',
                                                                  '/' ) + ".class" );
        }
        return bytes;
    }

    public void write(final String resourceName,
                      final byte[] clazzData) throws RuntimeDroolsException {
        if ( this.store.put( resourceName.replace( '.',
                                                   '/' ) + ".class",
                             clazzData ) != null ) {
            // we are updating an existing class so reload();
            reload();
        } else {
            try {
                wire( resourceName );
            } catch ( final Exception e ) {
                throw new RuntimeDroolsException( e );
            }
        }

    }

    public void remove(final String resourceName) throws RuntimeDroolsException {
        this.invokerLookups.remove( resourceName );
        if ( this.store.remove( resourceName.replace( '.',
                                                      '/' ) + ".class" ) != null ) {
            // we need to make sure the class is removed from the classLoader
            reload();
        }
    }

    public String[] list() {
        if ( this.store == null ) {
            return new String[0];
        }
        final List names = new ArrayList();

        for ( final Iterator it = this.store.keySet().iterator(); it.hasNext(); ) {
            final String name = (String) it.next();
            names.add( name.replace( '/',
                                     '.' ).substring( 0,
                                                      name.length() - 6 ) );
        }

        return (String[]) names.toArray( new String[this.store.size()] );
    }

    /**
     * This class drops the classLoader and reloads it. During this process  it must re-wire all the invokeables.
     * @throws CheckedDroolsException
     */
    public void reload() throws RuntimeDroolsException {
        // drops the classLoader and adds a new one
        this.classLoader = new PackageClassLoader( this.parentClassLoader );

        // Wire up invokers
        try {
            for ( final Iterator it = this.invokerLookups.keySet().iterator(); it.hasNext(); ) {
                wire( (String) it.next() );
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
    }

    public void clear() {
        this.store.clear();
        this.invokerLookups.clear();
        this.AST = null;
        reload();
    }

    public void wire(final String className) throws ClassNotFoundException,
                                            InstantiationException,
                                            IllegalAccessException {
        final Object invoker = this.invokerLookups.get( className );
        wire( className,
              invoker );
    }

    public void wire(final String className,
                     final Object invoker) throws ClassNotFoundException,
                                          InstantiationException,
                                          IllegalAccessException {
        final Class clazz = this.classLoader.findClass( className );
        if ( invoker instanceof ReturnValueConstraint ) {
            ((ReturnValueConstraint) invoker).setReturnValueExpression( (ReturnValueExpression) clazz.newInstance() );
        } else if ( invoker instanceof PredicateConstraint ) {
            ((PredicateConstraint) invoker).setPredicateExpression( (PredicateExpression) clazz.newInstance() );
        } else if ( invoker instanceof EvalCondition ) {
            ((EvalCondition) invoker).setEvalExpression( (EvalExpression) clazz.newInstance() );
        } else if ( invoker instanceof Rule ) {
            ((Rule) invoker).setConsequence( (Consequence) clazz.newInstance() );
        }
    }

    public String toString() {
        return this.getClass().getName() + this.store.toString();
    }

    public void putInvoker(final String className,
                           final Object invoker) {
        this.invokerLookups.put( className,
                                 invoker );
    }

    public void putAllInvokers(final Map invokers) {
        this.invokerLookups.putAll( invokers );

    }

    public Map getInvokers() {
        return this.invokerLookups;
    }

    public void removeInvoker(final String className) {
        this.invokerLookups.remove( className );
    }

    public Object getAST() {
        return this.AST;
    }

    public void setAST(final Object ast) {
        this.AST = ast;
    }

    /**
     * Lifted and adapted from Jakarta commons-jci
     * 
     * @author mproctor
     *
     */
    public class PackageClassLoader extends ClassLoader {

        public PackageClassLoader(final ClassLoader parentClassLoader) {
            super( parentClassLoader );
        }

        public Class fastFindClass(final String name) {
            final Class clazz = findLoadedClass( name );

            if ( clazz == null ) {
                final byte[] clazzBytes = read( name );
                if ( clazzBytes != null ) {
                    return defineClass( name,
                                        clazzBytes,
                                        0,
                                        clazzBytes.length );
                }
            }

            return clazz;
        }

        /**
         * Javadocs recommend that this method not be overloaded. We overload this so that we can prioritise the fastFindClass 
         * over method calls to parent.loadClass(name, false); and c = findBootstrapClass0(name); which the default implementation
         * would first - hence why we call it "fastFindClass" instead of standard findClass, this indicates that we give it a 
         * higher priority than normal.
         * 
         */
        protected synchronized Class loadClass(final String name,
                                               final boolean resolve) throws ClassNotFoundException {
            Class clazz = fastFindClass( name );

            if ( clazz == null ) {
                final ClassLoader parent = getParent();
                if ( parent != null ) {
                    clazz = parent.loadClass( name );
                } else {
                    throw new ClassNotFoundException( name );
                }
            }

            if ( resolve ) {
                resolveClass( clazz );
            }

            return clazz;
        }

        protected Class findClass(final String name) throws ClassNotFoundException {
            final Class clazz = fastFindClass( name );
            if ( clazz == null ) {
                throw new ClassNotFoundException( name );
            }
            return clazz;
        }

        public InputStream getResourceAsStream(final String name) {
            final byte[] bytes = (byte[]) PackageCompilationData.this.store.get( name );
            if ( bytes != null ) {
                return new ByteArrayInputStream( bytes );
            } else {
                return super.getResourceAsStream( name );
            }
        }
    }

}