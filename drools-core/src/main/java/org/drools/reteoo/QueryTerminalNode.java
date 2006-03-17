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

import java.util.LinkedList;

import org.drools.common.PropagationContextImpl;
import org.drools.rule.Rule;
import org.drools.spi.PropagationContext;

/**
 * Leaf Rete-OO node responsible for enacting <code>Action</code> s on a
 * matched <code>Rule</code>.
 * 
 * @see org.drools.rule.Rule
 * 
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 */
final class QueryTerminalNode extends BaseNode
    implements
    TupleSink,
    NodeMemory {
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** The rule to invoke upon match. */
    private final Rule        rule;
    private final TupleSource tupleSource;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     * 
     * @param inputSource
     *            The parent tuple source.
     * @param rule
     *            The rule.
     */
    QueryTerminalNode(int id,
                      TupleSource source,
                      Rule rule) {
        super( id );
        this.rule = rule;
        this.tupleSource = source;
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Retrieve the <code>Action</code> associated with this node.
     * 
     * @return The <code>Action</code> associated with this node.
     */
    public Rule getRule() {
        return this.rule;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // org.drools.impl.TupleSink
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Assert a new <code>Tuple</code>.
     * 
     * @param tuple
     *            The <code>Tuple</code> being asserted.
     * @param workingMemory
     *            The working memory seesion.
     * @throws AssertionException
     *             If an error occurs while asserting.
     */
    public void assertTuple(ReteTuple tuple,
                            PropagationContext context,
                            WorkingMemoryImpl workingMemory) {
        LinkedList list = (LinkedList) workingMemory.getNodeMemory( this );
        if ( list.isEmpty() ) {
            workingMemory.setQueryResults( this.rule.getName(),
                                           this );
        }
        list.add( tuple );
    }

    public void retractTuple(ReteTuple tuple,
                             PropagationContext context,
                             WorkingMemoryImpl workingMemory) {
    }

    public void modifyTuple(ReteTuple tuple,
                            PropagationContext context,
                            WorkingMemoryImpl workingMemory) {

    }

    public String toString() {
        return "[QueryTerminalNode: rule=" + this.rule.getName() + "]";
    }

    public void ruleAttached() {

    }

    public void attach() {
        tupleSource.addTupleSink( this );
    }

    public void attach(WorkingMemoryImpl[] workingMemories) {
        attach();

        for (int i = 0, length = workingMemories.length; i < length; i++) { 
            WorkingMemoryImpl workingMemory = workingMemories[i];
            PropagationContext propagationContext = new PropagationContextImpl( workingMemory.getNextPropagationIdCounter(),
                                                                                PropagationContext.RULE_ADDITION,
                                                                                null,
                                                                                null );            
            this.tupleSource.updateNewNode( workingMemory, propagationContext );
        }   
    }

    public void remove(BaseNode node,
                       WorkingMemoryImpl[] workingMemories) {
        for ( int i = 0, length = workingMemories.length; i < length; i++) {
            workingMemories[i].clearNodeMemory( this );    
        }        
        tupleSource.remove( this,
                            workingMemories );
    }

    public void updateNewNode(WorkingMemoryImpl workingMemory,
                              PropagationContext context) {
        // There are no child nodes to update, do nothing.
    }

    public Object createMemory() {
        //return new QueryTerminalNodeMemory();
        return new LinkedList();
    }

}
