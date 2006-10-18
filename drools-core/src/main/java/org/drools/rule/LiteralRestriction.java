package org.drools.rule;

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

import org.drools.WorkingMemory;
import org.drools.common.InternalWorkingMemory;
import org.drools.reteoo.ReteTuple;
import org.drools.rule.VariableConstraint.VariableContextEntry;
import org.drools.spi.Evaluator;
import org.drools.spi.Extractor;
import org.drools.spi.FieldValue;
import org.drools.spi.Restriction;
import org.drools.spi.Tuple;

public class LiteralRestriction
    implements
    Restriction {

    /**
     * 
     */
    private static final long          serialVersionUID     = 320;

    private final FieldValue           field;

    private final Evaluator            evaluator;

    private static final Declaration[] requiredDeclarations = new Declaration[0];

    public LiteralRestriction(final FieldValue field,
                              final Evaluator evaluator) {
        this.field = field;
        this.evaluator = evaluator;
    }

    public Evaluator getEvaluator() {
        return this.evaluator;
    }

    public FieldValue getField() {
        return this.field;
    }
    
    public boolean isAllowed(Extractor extractor, 
                             Object object,
                             InternalWorkingMemory workingMemoiry) {
        return this.evaluator.evaluate( extractor, object, field );
    }
    
    public boolean isAllowedCachedLeft(ContextEntry context, Object object) {
        throw  new UnsupportedOperationException("cannot call isAllowed(ContextEntry context)");        
    }    
    
    public boolean isAllowedCachedRight(ReteTuple tuple, ContextEntry context) {
        throw  new UnsupportedOperationException("cannot call isAllowed(ContextEntry context)");        
    }      
    
    /**
     * Literal constraints cannot have required declarations, so always return an empty array.
     * @return
     *      Return an empty <code>Declaration[]</code>
     */
    public Declaration[] getRequiredDeclarations() {
        return LiteralRestriction.requiredDeclarations;
    }



    public String toString() {
        return "[LiteralRestriction evaluator=" + this.evaluator + " value=" + this.field + "]";
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + this.evaluator.hashCode();
        result = PRIME * result + this.field.getValue().hashCode();
        return result;
    }

    public boolean equals(final Object object) {
        if ( this == object ) {
            return true;
        }
        if ( object == null || object.getClass() != LiteralRestriction.class ) {
            return false;
        }
        final LiteralRestriction other = (LiteralRestriction) object;

        return this.field.equals( other.field ) && this.evaluator.equals( other.evaluator );
    }

};