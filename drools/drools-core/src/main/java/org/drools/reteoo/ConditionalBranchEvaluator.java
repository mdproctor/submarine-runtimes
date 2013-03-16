package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.core.common.RuleBasePartitionId;
import org.drools.rule.EvalCondition;
import org.drools.core.spi.Tuple;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ConditionalBranchEvaluator implements Externalizable {

    private EvalCondition condition;

    private ConditionalBranchEvaluator elseBranchEvaluator;

    private ConditionalExecution conditionalExecution;

    public ConditionalBranchEvaluator() { }

    public ConditionalBranchEvaluator( EvalCondition condition,
                                       RuleBasePartitionId partitionId,
                                       LeftTupleSink tupleSink,
                                       boolean breaking,
                                       ConditionalBranchEvaluator elseBranchEvaluator ) {
        this.condition = condition;
        this.elseBranchEvaluator = elseBranchEvaluator;
        this.conditionalExecution = new ConditionalExecution( partitionId, tupleSink, breaking );
    }

    public static class ConditionalExecution implements Externalizable {
        private LeftTupleSinkPropagator sink;

        private boolean breaking;

        public ConditionalExecution() { }

        private ConditionalExecution( RuleBasePartitionId partitionId,
                                      LeftTupleSink tupleSink,
                                      boolean breaking ) {
            this.sink = new SingleLeftTupleSinkAdapter( partitionId, tupleSink );
            this.breaking = breaking;
        }

        public LeftTupleSinkPropagator getSink() {
            return sink;
        }

        public boolean isBreaking() {
            return breaking;
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(sink);
            out.writeBoolean(breaking);
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            sink = (LeftTupleSinkPropagator) in.readObject();
            breaking = in.readBoolean();
        }

        @Override
        public String toString() {
            return ( breaking ? "break[" : "do[" ) + sink.getSinks()[0] +"]";
        }
    }

    public ConditionalExecution evaluate( Tuple tuple,
                                          WorkingMemory workingMemory,
                                          Object context ) {

        if ( condition.isAllowed( tuple, workingMemory, context ) ) {
            return conditionalExecution;
        }
        return elseBranchEvaluator == null ? null : elseBranchEvaluator.evaluate( tuple, workingMemory, context );
    }

    public Object createContext() {
        return condition.createContext();
    }

    @Override
    public int hashCode() {
        return condition.hashCode() + ( elseBranchEvaluator == null ? 0 : elseBranchEvaluator.hashCode() );
    }

    @Override
    public boolean equals(Object obj) {
        if ( !(obj instanceof ConditionalBranchEvaluator) ) {
            return false;
        }
        ConditionalBranchEvaluator other = (ConditionalBranchEvaluator) obj;
        if ( !condition.equals(other.condition) ) {
            return false;
        }
        return elseBranchEvaluator == null ? other.elseBranchEvaluator == null : elseBranchEvaluator.equals(other.elseBranchEvaluator);
    }

    @Override
    public String toString() {
        return "if ( " + condition + " ) " + conditionalExecution + (elseBranchEvaluator != null ? " else " + elseBranchEvaluator.toString() : "");
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(condition);
        out.writeObject(conditionalExecution);
        out.writeObject(elseBranchEvaluator);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        condition = (EvalCondition) in.readObject();
        conditionalExecution = (ConditionalExecution) in.readObject();
        elseBranchEvaluator = (ConditionalBranchEvaluator) in.readObject();
    }

}
