//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.23 at 11:05:22 PM CDT 
//
package org.drools.core.meta.org.test;

import org.drools.core.metadata.MetadataHolder;

public class KlassImpl implements Klass, MetadataHolder {

    protected String prop;

    public KlassImpl() {
        super();
    }

    public KlassImpl(final String prop) {
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

}

