package org.drools.base;

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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.drools.base.field.BooleanFieldImpl;
import org.drools.base.field.DoubleFieldImpl;
import org.drools.base.field.LongFieldImpl;
import org.drools.base.field.ObjectFieldImpl;
import org.drools.spi.FieldValue;

public class FieldFactory {
    private static final FieldFactory INSTANCE = new FieldFactory();

    public static FieldFactory getInstance() {
        return FieldFactory.INSTANCE;
    }

    private FieldFactory() {

    }

    public static FieldValue getFieldValue(final String value,
                                           ValueType valueType) {
        FieldValue field = null;
        if ( value == null ) {
            valueType = ValueType.NULL_TYPE;
        }

        if ( valueType == ValueType.NULL_TYPE ) {
            field = new ObjectFieldImpl( null );
        } else if ( valueType == ValueType.CHAR_TYPE ) {
            field = new LongFieldImpl( value.charAt( 0 ) );
        } else if ( valueType == ValueType.BYTE_TYPE ) {
            field = new LongFieldImpl( Long.parseLong( value ) );
        } else if ( valueType == ValueType.SHORT_TYPE ) {
            field = new LongFieldImpl( Long.parseLong( value ) );
        } else if ( valueType == ValueType.INTEGER_TYPE ) {
            field = new LongFieldImpl( Long.parseLong( stripNumericType( value ) ) );
        } else if ( valueType == ValueType.LONG_TYPE ) {
            field = new LongFieldImpl( Long.parseLong( stripNumericType( value ) ) );
        } else if ( valueType == ValueType.FLOAT_TYPE ) {
            field = new DoubleFieldImpl( Float.parseFloat( stripNumericType( value ) ) );
        } else if ( valueType == ValueType.DOUBLE_TYPE ) {
            field = new DoubleFieldImpl( Double.parseDouble( stripNumericType( value ) ) );
        } else if ( valueType == ValueType.BOOLEAN_TYPE ) {
            field = new BooleanFieldImpl( Boolean.valueOf( value ).booleanValue() );
        } else if ( valueType == ValueType.STRING_TYPE ) {
            field = new ObjectFieldImpl( value.intern() );
        } else if ( valueType == ValueType.DATE_TYPE ) {
            //MN: I think its fine like this, seems to work !
            field = new ObjectFieldImpl( value );
        } else if ( valueType == ValueType.ARRAY_TYPE ) {
            //MN: I think its fine like this.
            field = new ObjectFieldImpl( value );
        } else if ( valueType == ValueType.OBJECT_TYPE ) {
            field = new ObjectFieldImpl( value );
        } else if ( valueType == ValueType.BIG_DECIMAL_TYPE ) {
            field = new ObjectFieldImpl( new BigDecimal( value ) );
        } else if ( valueType == ValueType.BIG_INTEGER_TYPE ) {
            field = new ObjectFieldImpl( new BigInteger( value ) );
        }

        return field;
    }

    private static String stripNumericType(String value) {
        // incase a user adds a f or l, strip it as its not needed
        if ( Character.getType( value.charAt( value.length() - 1 ) ) != Character.DECIMAL_DIGIT_NUMBER ) {
            value = value.substring( 0,
                                     value.length() - 1 );
        }

        return value;
    }

}