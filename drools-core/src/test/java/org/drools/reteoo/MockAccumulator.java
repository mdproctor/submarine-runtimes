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
package org.drools.reteoo;

import java.util.Collections;
import java.util.List;

import org.drools.WorkingMemory;

/**
 * A Mock accumulator object.
 * 
 * @author etirelli
 *
 */
public class MockAccumulator
    implements
    Accumulator {
    
    private static final long serialVersionUID = 8959310397185256783L;
    
    private ReteTuple leftTuple = null;
    private List      matchingObjects = Collections.EMPTY_LIST;
    private WorkingMemory workingMemory = null;

    /* (non-Javadoc)
     * @see org.drools.reteoo.Accumulator#accumulate(org.drools.reteoo.ReteTuple, java.util.List, org.drools.WorkingMemory)
     */
    public Object accumulate(ReteTuple leftTuple,
                             List matchingObjects,
                             WorkingMemory workingMemory) {
        this.leftTuple = leftTuple;
        this.matchingObjects = matchingObjects;
        this.workingMemory = workingMemory;
        return matchingObjects;
    }

    public ReteTuple getLeftTuple() {
        return leftTuple;
    }

    public List getMatchingObjects() {
        return matchingObjects;
    }

    public WorkingMemory getWorkingMemory() {
        return workingMemory;
    }

}
