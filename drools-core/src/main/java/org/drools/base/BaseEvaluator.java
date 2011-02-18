/**
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

package org.drools.base;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.drools.base.evaluators.Operator;
import org.drools.common.InternalFactHandle;
import org.drools.spi.Evaluator;
import org.drools.time.Interval;

/**
 * BaseEvaluator is an Object Comparator that is operator aware
 *
 * @author mproctor
 *
 */
public abstract class BaseEvaluator
    implements
    Evaluator, Externalizable {

    private Operator  operator;

    private ValueType type;

    public BaseEvaluator() {
    }

    public BaseEvaluator(final ValueType type,
                         final Operator operator) {
        this.type = type;
        this.operator = operator;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        operator    = (Operator)in.readObject();
        type        = (ValueType)in.readObject();
        if (type != null)
            type   = ValueType.determineValueType(type.getClassType());
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(operator);
        out.writeObject(type);
    }

    public Operator getOperator() {
        return this.operator;
    }

    public ValueType getValueType() {
        return this.type;
    }

    public ValueType getCoercedValueType() {
        return this.type;
    }

    /**
     * Most evaluators operate on the fact attributes,
     * so, by default, the implementation unwraps it,
     * but subclasses can override this behaviour.
     */
    public Object prepareLeftObject(InternalFactHandle handle) {
        return handle.getObject();
    }
    
    /**
     * Most evaluators operate o symmetrical types of object
     * on both sides, i.e., either they operate on facts on 
     * both sides, or fact handles on both sides. But eventually,
     * some operators (like 'after') may differ in behaviour and 
     * so, they are allowed to override this method.
     */
    public Object prepareRightObject(InternalFactHandle handle) {
        return prepareLeftObject( handle );
    }
    
    public boolean isTemporal() {
        return false;
    }
    
    public Interval getInterval() {
        // returns an open bound interval
        return new Interval();
    }

    public boolean equals(final Object object) {
        if ( this == object ) {
            return true;
        }
        if ( object == null || !(object instanceof BaseEvaluator) ) {
            return false;
        }

        final Evaluator other = (Evaluator) object;
        return this.getOperator() == other.getOperator() && this.getValueType() == other.getValueType();
    }

    public int hashCode() {
        return (this.getValueType().hashCode()) ^ (this.getOperator().hashCode()) ^ (this.getClass().hashCode());
    }

}
