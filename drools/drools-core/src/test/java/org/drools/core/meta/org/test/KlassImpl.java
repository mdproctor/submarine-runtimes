/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.23 at 11:05:22 PM CDT 
//
package org.drools.core.meta.org.test;

import org.drools.core.metadata.MetadataHolder;

import java.util.ArrayList;
import java.util.List;

public class KlassImpl implements Klass, MetadataHolder {

    protected String prop;
    protected AnotherKlass another;
    protected AnotherKlass oneAnother;
    protected List<AnotherKlass> manyAnothers = new ArrayList<AnotherKlass>();


    public KlassImpl() {
        super();
    }

    public KlassImpl( final String prop ) {
        this.prop = prop;
    }

    public String getProp() {
        return prop;
    }
    public void setProp(String value) {
        this.prop = value;
    }

    private final Klass_ _ = new Klass_( this );

    public Klass_ get_() {
        return _;
    }

    public AnotherKlass getAnother() {
        return another;
    }

    public void setAnother( AnotherKlass another ) {
        this.another = another;
    }

    public AnotherKlass getOneAnother() {
        return oneAnother;
    }

    public void setOneAnother( AnotherKlass oneAnother ) {
        this.oneAnother = oneAnother;
    }

    public List<AnotherKlass> getManyAnothers() {
        return manyAnothers;
    }

    public void setManyAnothers( List<AnotherKlass> manyAnothers ) {
        this.manyAnothers = manyAnothers;
    }
}

