package org.drools.common;

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
import java.util.HashSet;
import java.util.Set;

import org.drools.WorkingMemory;
import org.drools.base.evaluators.Operator;
import org.drools.common.InstanceNotEqualsConstraint.InstanceNotEqualsConstraintContextEntry;
import org.drools.reteoo.BetaMemory;
import org.drools.reteoo.ObjectHashTable;
import org.drools.reteoo.ReteTuple;
import org.drools.rule.ContextEntry;
import org.drools.rule.Declaration;
import org.drools.rule.LiteralConstraint;
import org.drools.rule.VariableConstraint;
import org.drools.spi.BetaNodeFieldConstraint;
import org.drools.spi.Constraint;
import org.drools.spi.Evaluator;
import org.drools.spi.AlphaNodeFieldConstraint;
import org.drools.spi.FieldExtractor;
import org.drools.spi.Tuple;
import org.drools.util.FactHashTable;
import org.drools.util.FieldIndexHashTable;
import org.drools.util.LinkedList;
import org.drools.util.LinkedListEntry;
import org.drools.util.LinkedListNode;
import org.drools.util.TupleHashTable;

public class EmptyBetaConstraints
    implements
    Serializable, BetaConstraints {

    private static final BetaConstraints INSTANCE = new EmptyBetaConstraints();
    
    public static BetaConstraints getInstance() {
        return INSTANCE;
    }
    
    /**
     * 
     */
    private static final long               serialVersionUID         = 320L;   
    
    private EmptyBetaConstraints() {
    }

    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#updateFromTuple(org.drools.reteoo.ReteTuple)
     */
    public void updateFromTuple(ReteTuple tuple) {
    }

    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#updateFromFactHandle(org.drools.common.InternalFactHandle)
     */
    public void updateFromFactHandle(InternalFactHandle handle) {
    }
    
    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#isAllowedCachedLeft(java.lang.Object)
     */
    public boolean isAllowedCachedLeft(Object object ) {
        return true;
    }    
    
    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#isAllowedCachedRight(org.drools.reteoo.ReteTuple)
     */
    public boolean isAllowedCachedRight(ReteTuple tuple) {
        return true;
    }   
    
    public boolean isIndexed() {
        return false;
    }
    
    public boolean isEmpty() {
        return true;   
    }
    
    public BetaMemory createBetaMemory()  {
        BetaMemory memory = new BetaMemory( new TupleHashTable(),
                                            new FactHashTable() );
        
        return memory;        
    }    

    public int hashCode() {
        return 1;
    }

    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#getConstraints()
     */
    public LinkedList getConstraints() {
        LinkedList list =  new LinkedList();
        return list;
    }

    /**
     * Determine if another object is equal to this.
     * 
     * @param object
     *            The object to test.
     * 
     * @return <code>true</code> if <code>object</code> is equal to this,
     *         otherwise <code>false</code>.
     */
    public boolean equals(final Object object) {
        if ( this == object ) {
            return true;
        }

        return ( object != null && getClass() == object.getClass() );
    }

}