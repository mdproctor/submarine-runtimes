/*
 * Copyright 2010 JBoss Inc
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

package org.drools.rule;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.drools.util.CompositeClassLoader;

public class DialectRuntimeRegistry
    implements
    Externalizable {
    private Map<String, DialectRuntimeData> dialects;

    private static final long serialVersionUID = 510l;

    private Map<String, LineMappings>            lineMappings;

    public DialectRuntimeRegistry() {
        this.dialects = new HashMap<String, DialectRuntimeData>();
    }

    /**
     * Handles the write serialization of the PackageCompilationData. Patterns in Rules may reference generated data which cannot be serialized by
     * default methods. The PackageCompilationData holds a reference to the generated bytecode. The generated bytecode must be restored before any Rules.
     */
    public void writeExternal(final ObjectOutput stream) throws IOException {
        stream.writeObject( this.dialects );
        stream.writeObject( this.lineMappings );
    }

    /**
     * Handles the read serialization of the PackageCompilationData. Patterns in Rules may reference generated data which cannot be serialized by
     * default methods. The PackageCompilationData holds a reference to the generated bytecode; which must be restored before any Rules.
     * A custom ObjectInputStream, able to resolve classes against the bytecode, is used to restore the Rules.
     */
    public void readExternal(final ObjectInput stream) throws IOException,
                                                      ClassNotFoundException {
        this.dialects = (Map<String, DialectRuntimeData>) stream.readObject();
        this.lineMappings = (Map<String, LineMappings>) stream.readObject();
    }

    public void onAdd(CompositeClassLoader rootClassLoader) {
        //this.classLoader = rootClassLoader;
        for (DialectRuntimeData data : this.dialects.values()) {
            data.onAdd(this, rootClassLoader);
        }
    }

    public void onRemove() {
        for (DialectRuntimeData data : this.dialects.values()) {
            data.onRemove();
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

    public void merge( DialectRuntimeRegistry newDatas,
                       CompositeClassLoader rootClassLoader ) {
        // false to preserve backward compatibility, should proabably be true
        merge( newDatas, rootClassLoader, false );
    }

    public void merge( DialectRuntimeRegistry newDatas,
                       CompositeClassLoader rootClassLoader,
                       boolean excludeClasses ) {
        for ( Entry<String, DialectRuntimeData> entry : newDatas.dialects.entrySet() ) {
            DialectRuntimeData data = this.dialects.get( entry.getKey() );
            if ( data == null ) {
                DialectRuntimeData dialectData = entry.getValue().clone( this,
                                                                         rootClassLoader,
                                                                         excludeClasses );
                //dialectData.setDialectRuntimeRegistry( this );
                this.dialects.put( entry.getKey(),
                                   dialectData );
            } else {
                data.merge( this,
                            entry.getValue(),
                            excludeClasses );
            }
        }

        getLineMappings().putAll( newDatas.getLineMappings() );
    }

    public boolean isDirty() {
        return true;
    }

    public void onBeforeExecute() {
        // Java dialect MUST be the first to be processed.
        DialectRuntimeData data = this.dialects.get( "java" );
        boolean isJavaDirty = false;
        if( data != null ) {
            isJavaDirty = data.isDirty();
            data.onBeforeExecute();
        }

        // then, all others
        for ( Map.Entry<String, DialectRuntimeData> entry : this.dialects.entrySet() ) {
            if( ! "java".equals( entry.getKey() ) ) {
                data = entry.getValue();
                data.setDirty(isJavaDirty);
                data.onBeforeExecute();
            }
        }
    }

    public void clear() {
        this.dialects.clear();
    }

    public LineMappings getLineMappings(final String className) {
        return getLineMappings().get( className );
    }

    public Map<String, LineMappings> getLineMappings() {
        if ( this.lineMappings == null ) {
            this.lineMappings = new HashMap<String, LineMappings>();
        }
        return this.lineMappings;
    }


}
