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

package org.drools.spi;

import org.drools.WorkingMemory;

/**
 * Consequence to be fired upon successful match of a <code>Rule</code>.
 * 
 * @see org.drools.rule.Rule
 * 
 */
public interface Consequence
    extends
    Invoker {
    
    String getName();
    
    /**
     * Execute the consequence for the supplied matching <code>Tuple</code>.
     * 
     * @param knowledgeHelper
     * @param workingMemory
     *            The working memory session.
     * @throws ConsequenceException
     *             If an error occurs while attempting to invoke the
     *             consequence.
     */
    void evaluate(KnowledgeHelper knowledgeHelper,
                  WorkingMemory workingMemory) throws Exception;
    
}
