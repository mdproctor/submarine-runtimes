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



import org.drools.spi.FieldValue;

public class FieldImpl
    implements
    FieldValue {
    private Object value;

    public FieldImpl(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public boolean equals(Object other) {
        if ( this == other ) {
            return true;
        }
        if ( !(other instanceof FieldImpl) ) {
            return false;
        }
        FieldImpl field = (FieldImpl) other;

        return (((this.value == null) && (field.value == null)) || ((this.value != null) && (this.value.equals( field.value ))));
    }

    public int hashCode() {
        if ( this.value != null ) {
            return this.value.hashCode();
        } else {
            return 0;
        }
    }
}