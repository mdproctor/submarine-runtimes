package org.drools.base.extractors;

import org.drools.RuntimeDroolsException;
import org.drools.base.BaseClassFieldExtractor;

public abstract class BaseObjectClassFieldExtractor extends BaseClassFieldExtractor {

    private static final long serialVersionUID = 91214567753008212L;
    
    public BaseObjectClassFieldExtractor(Class clazz,
                                          String fieldName) {
        super( clazz,
               fieldName );
    }

    public abstract Object getValue(Object object);

    public boolean getBooleanValue(Object object) {
        // this can be improved by generating specific 
        // bytecode generation in the subclass, avoiding the if instanceof
        Object value = getValue( object );
        if( value instanceof Boolean ) {
            return ((Boolean) value).booleanValue();
        }
        throw new RuntimeDroolsException("Conversion to boolean not supported from "+value.getClass().getName());
    }

    public byte getByteValue(Object object) {
        // this can be improved by generating specific 
        // bytecode generation in the subclass, avoiding the if instanceof
        Object value = getValue( object );
        if( value instanceof Number ) {
            return ((Number) value).byteValue();
        }
        throw new RuntimeDroolsException("Conversion to byte not supported from "+value.getClass().getName());
    }

    public char getCharValue(Object object) {
        // this can be improved by generating specific 
        // bytecode generation in the subclass, avoiding the if instanceof
        Object value = getValue( object );
        if( value instanceof Character ) {
            return ((Character) value).charValue();
        }
        throw new RuntimeDroolsException("Conversion to char not supported from "+value.getClass().getName());
    }

    public double getDoubleValue(Object object) {
        // this can be improved by generating specific 
        // bytecode generation in the subclass, avoiding the if instanceof
        Object value = getValue( object );
        if( value instanceof Number ) {
            return ((Number) value).doubleValue();
        }
        throw new RuntimeDroolsException("Conversion to double not supported from "+value.getClass().getName());
    }

    public float getFloatValue(Object object) {
        // this can be improved by generating specific 
        // bytecode generation in the subclass, avoiding the if instanceof
        Object value = getValue( object );
        if( value instanceof Number ) {
            return ((Number) value).floatValue();
        }
        throw new RuntimeDroolsException("Conversion to float not supported from "+value.getClass().getName());
    }

    public int getIntValue(Object object) {
        // this can be improved by generating specific 
        // bytecode generation in the subclass, avoiding the if instanceof
        Object value = getValue( object );
        if( value instanceof Number ) {
            return ((Number) value).intValue();
        }
        throw new RuntimeDroolsException("Conversion to int not supported from "+value.getClass().getName());
    }

    public long getLongValue(Object object) {
        // this can be improved by generating specific 
        // bytecode generation in the subclass, avoiding the if instanceof
        Object value = getValue( object );
        if( value instanceof Number ) {
            return ((Number) value).longValue();
        }
        throw new RuntimeDroolsException("Conversion to long not supported from "+value.getClass().getName());
    }

    public short getShortValue(Object object) {
        // this can be improved by generating specific 
        // bytecode generation in the subclass, avoiding the if instanceof
        Object value = getValue( object );
        if( value instanceof Number ) {
            return ((Number) value).shortValue();
        }
        throw new RuntimeDroolsException("Conversion to short not supported from "+value.getClass().getName());
    }

}
