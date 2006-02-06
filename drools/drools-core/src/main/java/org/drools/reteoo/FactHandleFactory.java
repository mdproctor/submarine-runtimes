package org.drools.reteoo;
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

import org.drools.FactHandle;

/**
 * Factory Interface to return new <code>FactHandle</code>s
 * 
 *  @see FactHandle
 * 
 * @author <a href="mailto:mark.proctor@jboss.com">Mark Proctor</a>
 * @author <a href="mailto:bob@werken.com">Bob McWhirter</a>
 */
public interface FactHandleFactory
    extends
    Serializable {
    /**
     * Construct a handle with a new id.
     * 
     * @return The handle.
     */
    FactHandle newFactHandle();

    /**
     * Construct a handle with a specified id.
     * 
     * @param id
     *            The id to use.
     * @return The handle.
     */
    FactHandle newFactHandle(long id);
    
    /**
     * Increases the recency of the FactHandle
     * 
     * @param factHandle
     *      The fact handle to have its recency increased.
     */
    public void increaseFactHandleRecency(FactHandle factHandle);
    
    /**
     * @return a fresh instance of the fact handle factory, with any IDs reset etc.
     */
    FactHandleFactory newInstance();    
}
