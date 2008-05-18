package org.drools.reteoo;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.drools.rule.ContextEntry;
import org.drools.util.ObjectHashMap;

public class BetaMemory {

    private static final long serialVersionUID = 400L;

    private LeftTupleMemory   leftTupleMemory;
    private RightTupleMemory  rightTupleMemory;
    private ObjectHashMap     createdHandles;
    private ContextEntry[]    context;

    public BetaMemory() {
    }

    public BetaMemory(final LeftTupleMemory tupleMemory,
                      final RightTupleMemory objectMemory,
                      final ContextEntry[] context) {
        this.leftTupleMemory = tupleMemory;
        this.rightTupleMemory = objectMemory;
        this.context = context;
    }

    public RightTupleMemory getRightTupleMemory() {
        return this.rightTupleMemory;
    }

    public LeftTupleMemory getLeftTupleMemory() {
        return this.leftTupleMemory;
    }
    
    public ObjectHashMap getCreatedHandles() {
        if( this.createdHandles == null ) {
            this.createdHandles = new ObjectHashMap();
        }
        return this.createdHandles;
    }

    /**
     * @return the context
     */
    public ContextEntry[] getContext() {
        return context;
    }
}
