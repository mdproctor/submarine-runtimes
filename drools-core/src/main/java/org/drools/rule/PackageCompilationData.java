package org.drools.rule;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.drools.RuntimeDroolsException;
import org.drools.spi.Consequence;
import org.drools.spi.EvalExpression;
import org.drools.spi.PredicateExpression;
import org.drools.spi.ReturnValueExpression;

public class PackageCompilationData
    implements
    Externalizable {

    private Map                          invokerLookups = new HashMap();

    private Object                       AST;

    private Map                          store          = new HashMap();

    private transient PackageClassLoader classLoader;

    private transient ClassLoader        parentClassLoader;

    public PackageCompilationData() {
        this( null );
    }

    public PackageCompilationData(ClassLoader parentClassLoader) {
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
    public void writeExternal(ObjectOutput stream) throws IOException {
        stream.writeObject( this.store );
        stream.writeObject( this.AST );

        // Rules must be restored by an ObjectInputStream that can resolve using a given ClassLoader to handle seaprately by storing as
        // a byte[]        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( this.invokerLookups );
        stream.writeObject( bos.toByteArray() );
    }

    /**
     * Handles the read serialization of the PackageCompilationData. Patterns in Rules may reference generated data which cannot be serialized by 
     * default methods. The PackageCompilationData holds a reference to the generated bytecode; which must be restored before any Rules.
     * A custom ObjectInputStream, able to resolve classes against the bytecode, is used to restore the Rules.
     * 
     */
    public void readExternal(ObjectInput stream) throws IOException,
                                                ClassNotFoundException {
        init( null );

        this.store = (Map) stream.readObject();
        this.AST = stream.readObject();

        // Return the rules stored as a byte[]
        byte[] bytes = (byte[]) stream.readObject();

        //  Use a custom ObjectInputStream that can resolve against a given classLoader
        ObjectInputStreamWithLoader streamWithLoader = new ObjectInputStreamWithLoader( new ByteArrayInputStream( bytes ),
                                                                                        this.classLoader );
        this.invokerLookups = (Map) streamWithLoader.readObject();
    }

    private static class ObjectInputStreamWithLoader extends ObjectInputStream {
        private final ClassLoader classLoader;

        public ObjectInputStreamWithLoader(InputStream in,
                                           ClassLoader classLoader) throws IOException {
            super( in );
            this.classLoader = classLoader;
            enableResolveObject( true );
        }

        protected Class resolveClass(ObjectStreamClass desc) throws IOException,
                                                            ClassNotFoundException {
            if ( this.classLoader == null ) {
                return super.resolveClass( desc );
            } else {
                String name = desc.getName();
                return this.classLoader.loadClass( name );
            }
        }
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public byte[] read(final String resourceName) {
        byte[] bytes = null;

        if ( this.store != null ) {
            return (byte[]) this.store.get( resourceName.replace( '.',
                                                                  '/' ) + ".class" );
        }
        return bytes;
    }

    public void write(final String resourceName,
                      final byte[] clazzData) throws RuntimeDroolsException {
        if ( store.put( resourceName.replace( '.',
                                              '/' ) + ".class",
                        clazzData ) != null ) {
            // we are updating an existing class so reload();
            reload();
        } else {
            try {
                wire( resourceName );
            } catch ( Exception e ) {

            }
        }

    }

    public void remove(final String resourceName) throws RuntimeDroolsException {
        if ( store.remove( resourceName.replace( '.',
                                                 '/' ) + ".class" ) != null ) {
            // we need to make sure the class is removed from the classLoader
            reload();
        }
    }

    public String[] list() {
        if ( store == null ) {
            return new String[0];
        }
        final List names = new ArrayList();

        for ( final Iterator it = store.keySet().iterator(); it.hasNext(); ) {
            final String name = (String) it.next();
            names.add( name.replace( '/',
                                     '.' ).substring( 0,
                                                      name.length() - 6 ) );
        }

        return (String[]) names.toArray( new String[store.size()] );
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
            for ( Iterator it = this.invokerLookups.keySet().iterator(); it.hasNext(); ) {
                wire( (String) it.next() );
            }
        } catch ( ClassNotFoundException e ) {
            throw new RuntimeDroolsException( e );
        } catch ( InstantiationError e ) {
            throw new RuntimeDroolsException( e );
        } catch ( IllegalAccessException e ) {
            throw new RuntimeDroolsException( e );
        } catch ( InstantiationException e ) {
            throw new RuntimeDroolsException( e );
        }
    }

    public void wire(String className) throws ClassNotFoundException,
                                      InstantiationException,
                                      IllegalAccessException {
        Object invoker = this.invokerLookups.get( className );
        wire( className,
              invoker );
    }

    public void wire(String className,
                     Object invoker) throws ClassNotFoundException,
                                    InstantiationException,
                                    IllegalAccessException {
        Class clazz = this.classLoader.findClass( className );
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
        return this.getClass().getName() + store.toString();
    }

    public void putInvoker(String className,
                           Object invoker) {
        this.invokerLookups.put( className,
                                 invoker );
    }
    
    public void putAllInvokers(Map invokers) {
        this.invokerLookups.putAll( invokers );
        
    }
    
    public Map getInvokers() {
        return this.invokerLookups;
    }

    public void removeInvoker(String className) {
        this.invokerLookups.remove( className );
    }

    public Object getAST() {
        return AST;
    }

    public void setAST(Object ast) {
        AST = ast;
    }

    public class PackageClassLoader extends ClassLoader {

        public PackageClassLoader(ClassLoader parentClassLoader) {
            super( parentClassLoader );
        }

        private Class fastFindClass(final String name) {
            final byte[] clazzBytes = read( name );
            if ( clazzBytes != null ) {
                return defineClass( name,
                                    clazzBytes,
                                    0,
                                    clazzBytes.length );
            }

            return null;
        }

        /**
         * Javadocs recommend that this method not be overloaded. We overload this so that we can prioritise the fastFindClass 
         * over method calls to parent.loadClass(name, false); and c = findBootstrapClass0(name); which the default implementation
         * would first - hence why we call it "fastFindClass" instead of standard findClass, this indicates that we give it a 
         * higher priority than normal.
         * 
         */
        protected synchronized Class loadClass(String name,
                                               boolean resolve) throws ClassNotFoundException {
            Class clazz = findLoadedClass( name );

            if ( clazz == null ) {
                clazz = fastFindClass( name );

                if ( clazz == null ) {

                    final ClassLoader parent = getParent();
                    if ( parent != null ) {
                        clazz = parent.loadClass( name );
                    } else {
                        throw new ClassNotFoundException( name );
                    }
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
    }

}
