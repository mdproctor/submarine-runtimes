package org.drools.rule;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DialectRuntimeRegistry
    implements
    Externalizable {
    private Map<String, DialectRuntimeData> dialects;

    private Map                             lineMappings;

    public DialectRuntimeRegistry() {
        this.dialects = new HashMap<String, DialectRuntimeData>();
    }

    /**
     * Handles the write serialization of the PackageCompilationData. Patterns in Rules may reference generated data which cannot be serialized by
     * default methods. The PackageCompilationData holds a reference to the generated bytecode. The generated bytecode must be restored before any Rules.
     *
     */
    public void writeExternal(final ObjectOutput stream) throws IOException {
        stream.writeObject( this.dialects );
        stream.writeObject( this.lineMappings );
    }

    /**
     * Handles the read serialization of the PackageCompilationData. Patterns in Rules may reference generated data which cannot be serialized by
     * default methods. The PackageCompilationData holds a reference to the generated bytecode; which must be restored before any Rules.
     * A custom ObjectInputStream, able to resolve classes against the bytecode, is used to restore the Rules.
     *
     */
    public void readExternal(final ObjectInput stream) throws IOException,
                                                      ClassNotFoundException {
        this.dialects = (Map<String, DialectRuntimeData>) stream.readObject();
        this.lineMappings = (Map) stream.readObject();
    }

    public void onAdd(CompositeClassLoader rootClassLoader) {
        //this.classLoader = rootClassLoader;
        for ( Iterator it = this.dialects.values().iterator(); it.hasNext(); ) {
            DialectRuntimeData data = (DialectRuntimeData) it.next();
            data.onAdd( this,
                            rootClassLoader );
        }
    }
    
    public void onRemove() {
        for ( Iterator it = this.dialects.values().iterator(); it.hasNext(); ) {
            DialectRuntimeData data = (DialectRuntimeData) it.next();
            data.onRemove( );
        }
    }    

    public void setDialectData(String name,
                               DialectRuntimeData data) {
        this.dialects.put( name,
                           data );
    }

    public DialectRuntimeData getDialectData(String dialect) {
        return this.dialects.get( dialect );
    }

    public DialectRuntimeData removeRule(final Package pkg,
                                         final Rule rule) {
        DialectRuntimeData dialect = this.dialects.get( rule.getDialect() );
        dialect.removeRule( pkg,
                            rule );
        return dialect;
    }

    public DialectRuntimeData removeFunction(final Package pkg,
                                             final Function function) {
        DialectRuntimeData dialect = this.dialects.get( function.getDialect() );
        dialect.removeFunction( pkg,
                                function );
        return dialect;
    }

    public void merge(DialectRuntimeRegistry newDatas,
                      CompositeClassLoader rootClassLoader) {
        for ( Entry<String, DialectRuntimeData> entry : newDatas.dialects.entrySet() ) {
            DialectRuntimeData data = this.dialects.get( entry.getKey() );
            if ( data == null ) {
                DialectRuntimeData dialectData = entry.getValue().clone( this,
                                                                         rootClassLoader );
                //dialectData.setDialectRuntimeRegistry( this );
                this.dialects.put( entry.getKey(),
                                   dialectData );
            } else {
                data.merge( this,
                            entry.getValue() );
            }
        }

        getLineMappings().putAll( newDatas.getLineMappings() );
    }

    public boolean isDirty() {
        return true;
    }

    public void onBeforeExecute() {
        for ( Iterator it = this.dialects.values().iterator(); it.hasNext(); ) {
            DialectRuntimeData data = (DialectRuntimeData) it.next();
            data.onBeforeExecute();
        }
        //        // detect if any dialect is dirty, if so reload() them all
        //        boolean isDirty = false;
        //        for ( Iterator it = this.dialects.values().iterator(); it.hasNext(); ) {
        //            DialectRuntimeData data = (DialectRuntimeData) it.next();
        //            if ( data.isDirty() ) {
        //                isDirty = true;
        //                break;
        //            }
        //        }
        //
        //        //if ( isDirty ) {
        //        this.classLoader = new CompositeClassLoader();
        //        for ( Iterator it = this.dialects.values().iterator(); it.hasNext(); ) {
        //            DialectRuntimeData data = (DialectRuntimeData) it.next();
        //            data.reload();
        //        }
        //        //}
    }

    public void clear() {
        this.dialects.clear();
    }

    public LineMappings getLineMappings(final String className) {
        return (LineMappings) getLineMappings().get( className );
    }

    public Map getLineMappings() {
        if ( this.lineMappings == null ) {
            this.lineMappings = new HashMap();
        }
        return this.lineMappings;
    }


}
