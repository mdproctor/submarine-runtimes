package org.drools.spi;

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

import java.io.Serializable;
import java.lang.reflect.Method;

import org.drools.base.ValueType;
import org.drools.common.InternalWorkingMemory;

public interface Extractor
    extends
    Serializable {

    public Object getValue(InternalWorkingMemory workingMemory, Object object);

    public char getCharValue(InternalWorkingMemory workingMemory, Object object);

    public int getIntValue(InternalWorkingMemory workingMemory, Object object);

    public byte getByteValue(InternalWorkingMemory workingMemory, Object object);

    public short getShortValue(InternalWorkingMemory workingMemory, Object object);

    public long getLongValue(InternalWorkingMemory workingMemory, Object object);

    public float getFloatValue(InternalWorkingMemory workingMemory, Object object);

    public double getDoubleValue(InternalWorkingMemory workingMemory, Object object);

    public boolean getBooleanValue(InternalWorkingMemory workingMemory, Object object);
    
    public boolean isNullValue(InternalWorkingMemory workingMemory, Object object);
    
    public boolean isGlobal();

    public ValueType getValueType();

    public Class getExtractToClass();

    public Method getNativeReadMethod();

    public int getHashCode(InternalWorkingMemory workingMemory, Object object);

}