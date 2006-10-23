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

package org.drools.base.extractors;

import java.lang.reflect.Method;

import org.drools.RuntimeDroolsException;
import org.drools.base.BaseClassFieldExtractor;

/**
 * A Base class for primitive boolean class field
 * extractors. This class centralizes type conversions.
 *  
 * @author etirelli
 */
public abstract class BaseBooleanClassFieldExtractor extends BaseClassFieldExtractor {

    private static final long serialVersionUID = 9104214567753008212L;

    public BaseBooleanClassFieldExtractor(final Class clazz,
                                          final String fieldName) {
        super( clazz,
               fieldName );
    }

    public Object getValue(final Object object) {
        return getBooleanValue( object ) ? Boolean.TRUE : Boolean.FALSE;
    }

    public abstract boolean getBooleanValue(Object object);

    public byte getByteValue(final Object object) {
        throw new RuntimeDroolsException( "Conversion to byte not supported from boolean" );
    }

    public char getCharValue(final Object object) {
        throw new RuntimeDroolsException( "Conversion to char not supported from boolean" );
    }

    public double getDoubleValue(final Object object) {
        throw new RuntimeDroolsException( "Conversion to double not supported from boolean" );
    }

    public float getFloatValue(final Object object) {
        throw new RuntimeDroolsException( "Conversion to float not supported from boolean" );
    }

    public int getIntValue(final Object object) {
        throw new RuntimeDroolsException( "Conversion to int not supported from boolean" );
    }

    public long getLongValue(final Object object) {
        throw new RuntimeDroolsException( "Conversion to long not supported from boolean" );
    }

    public short getShortValue(final Object object) {
        throw new RuntimeDroolsException( "Conversion to short not supported from boolean" );
    }
    
    public Method getNativeReadMethod() {
        try {
            return this.getClass().getDeclaredMethod( "getBooleanValue", new Class[] { Object.class } );
        } catch ( Exception e ) {
            throw new RuntimeDroolsException("This is a bug. Please report to development team: "+e.getMessage(), e);
        }
    }

}
