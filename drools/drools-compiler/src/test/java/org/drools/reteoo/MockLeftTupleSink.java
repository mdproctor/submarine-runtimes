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

import org.drools.FactException;
import org.drools.RuleBaseConfiguration;
import org.drools.common.InternalFactHandle;
import org.drools.common.InternalWorkingMemory;
import org.drools.common.Memory;
import org.drools.common.MemoryFactory;
import org.drools.common.RuleBasePartitionId;
import org.drools.common.UpdateContext;
import org.drools.reteoo.builder.BuildContext;
import org.drools.spi.PropagationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockLeftTupleSink extends LeftTupleSource
    implements
    LeftTupleSinkNode,
    MemoryFactory {
    private static final long serialVersionUID = 510l;
    private final List        asserted         = new ArrayList();
    private final List        retracted        = new ArrayList();

    private LeftTupleSinkNode     previousTupleSinkNode;
    private LeftTupleSinkNode     nextTupleSinkNode;

    public MockLeftTupleSink() {
        super( 0, RuleBasePartitionId.MAIN_PARTITION, false );
    }

    public MockLeftTupleSink(final int id) {
        super(id, RuleBasePartitionId.MAIN_PARTITION, false);
    }

    public void assertLeftTuple(final LeftTuple tuple,
                            final PropagationContext context,
                            final InternalWorkingMemory workingMemory) {
        this.asserted.add( new Object[]{tuple, context, workingMemory} );

    }

    public void retractLeftTuple(final LeftTuple tuple,
                             final PropagationContext context,
                             final InternalWorkingMemory workingMemory) {
        this.retracted.add( new Object[]{tuple, context, workingMemory} );
    }

    public List getAsserted() {
        return this.asserted;
    }

    public List getRetracted() {
        return this.retracted;
    }

    public void ruleAttached() {
        // TODO Auto-generated method stub
    }

    public int getId() {
        return this.id;
    }
    
    public void updateSink(final LeftTupleSink sink,
                           final PropagationContext context,
                           final InternalWorkingMemory workingMemory) throws FactException {
        // TODO Auto-generated method stub

    }

    protected void doRemove(final RuleRemovalContext context,
                            final ReteooBuilder builder,
                            final InternalWorkingMemory[] workingMemories) {
    }

    protected void doCollectAncestors(NodeSet nodeSet) { }

    public void attach(BuildContext buildContext) {
        // TODO Auto-generated method stub

    }

    public List getPropagatedTuples(final ReteooWorkingMemory workingMemory,
                                    final LeftTupleSink sink) {
        // TODO Auto-generated method stub
        return Collections.EMPTY_LIST;
    }

    /**
     * Returns the next node
     * @return
     *      The next TupleSinkNode
     */
    public LeftTupleSinkNode getNextLeftTupleSinkNode() {
        return this.nextTupleSinkNode;
    }

    /**
     * Sets the next node 
     * @param next
     *      The next TupleSinkNode
     */
    public void setNextLeftTupleSinkNode(final LeftTupleSinkNode next) {
        this.nextTupleSinkNode = next;
    }

    /**
     * Returns the previous node
     * @return
     *      The previous TupleSinkNode
     */
    public LeftTupleSinkNode getPreviousLeftTupleSinkNode() {
        return this.previousTupleSinkNode;
    }

    /**
     * Sets the previous node 
     * @param previous
     *      The previous TupleSinkNode
     */
    public void setPreviousLeftTupleSinkNode(final LeftTupleSinkNode previous) {
        this.previousTupleSinkNode = previous;
    }

    public boolean isLeftTupleMemoryEnabled() {
        return false;
    }

    public void setLeftTupleMemoryEnabled(boolean tupleMemoryEnabled) {
    }

    @Override
    public void networkUpdated(UpdateContext updateContext) {
    }

    public short getType() {
        return 0;
    }

    public void modifyLeftTuple(InternalFactHandle factHandle,
                                ModifyPreviousTuples modifyPreviousTuples,
                                PropagationContext context,
                                InternalWorkingMemory workingMemory) {
    }

    public void modifyLeftTuple(LeftTuple leftTuple,
                                PropagationContext context,
                                InternalWorkingMemory workingMemory) {
    }
    
    public LeftTuple createLeftTuple(InternalFactHandle factHandle,
                                     LeftTupleSink sink,
                                     boolean leftTupleMemoryEnabled) {
        return new LeftTupleImpl(factHandle, sink, leftTupleMemoryEnabled );
    }    
    
    public LeftTuple createLeftTuple(LeftTuple leftTuple,
                                     LeftTupleSink sink,
                                     PropagationContext pctx,
                                     boolean leftTupleMemoryEnabled) {
        return new LeftTupleImpl(leftTuple,sink, pctx, leftTupleMemoryEnabled );
    }

    public LeftTuple createLeftTuple(LeftTuple leftTuple,
                                     RightTuple rightTuple,
                                     LeftTupleSink sink) {
        return new LeftTupleImpl(leftTuple, rightTuple, sink );
    }   
    
    public LeftTuple createLeftTuple(LeftTuple leftTuple,
                                     RightTuple rightTuple,
                                     LeftTuple currentLeftChild,
                                     LeftTuple currentRightChild,
                                     LeftTupleSink sink,
                                     boolean leftTupleMemoryEnabled) {
        return new LeftTupleImpl(leftTuple, rightTuple, currentLeftChild, currentRightChild, sink, leftTupleMemoryEnabled );        
    }

    public LeftTupleSource getLeftTupleSource() {
        return null;
    }

    @Override
    protected ObjectTypeNode getObjectTypeNode() {
        return null;
    }

    public Memory createMemory(RuleBaseConfiguration config, InternalWorkingMemory wm) {
        return null;
    }

    @Override
    public LeftTuple createPeer(LeftTuple original) {
        return null;
    }

}
