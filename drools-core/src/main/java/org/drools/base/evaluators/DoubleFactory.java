package org.drools.base.evaluators;

import org.drools.base.BaseEvaluator;
import org.drools.spi.Evaluator;

public class DoubleFactory {

    public static Evaluator getDoubleEvaluator(int operator) {
        switch ( operator ) {
            case Evaluator.EQUAL :
                return DoubleEqualEvaluator.INSTANCE;
            case Evaluator.NOT_EQUAL :
                return DoubleNotEqualEvaluator.INSTANCE;
            case Evaluator.LESS :
                return DoubleLessEvaluator.INSTANCE;
            case Evaluator.LESS_OR_EQUAL :
                return DoubleLessOrEqualEvaluator.INSTANCE;
            case Evaluator.GREATER :
                return DoubleGreaterEvaluator.INSTANCE;
            case Evaluator.GREATER_OR_EQUAL :
                return DoubleGreaterOrEqualEvaluator.INSTANCE;
            default :
                throw new RuntimeException( "Operator '" + operator + "' does not exist for DoubleEvaluator" );
        }
    }

    static class DoubleEqualEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new DoubleEqualEvaluator();

        private DoubleEqualEvaluator() {
            super( Evaluator.DOUBLE_TYPE,
                   Evaluator.EQUAL );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            if ( object1 == null ) return object2 == null;
            return ((Number) object1).equals( object2 );
        }

        public String toString() {
            return "Double ==";
        }
    }

    static class DoubleNotEqualEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new DoubleNotEqualEvaluator();

        private DoubleNotEqualEvaluator() {
            super( Evaluator.DOUBLE_TYPE,
                   Evaluator.NOT_EQUAL );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            if ( object1 == null ) return object2 != null;
            return !((Number) object1).equals( object2 );
        }

        public String toString() {
            return "Double !=";
        }
    }

    static class DoubleLessEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new DoubleLessEvaluator();

        private DoubleLessEvaluator() {
            super( Evaluator.DOUBLE_TYPE,
                   Evaluator.LESS );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            return ((Number) object1).doubleValue() < ((Number) object2).doubleValue();
        }

        public String toString() {
            return "Double <";
        }
    }

    static class DoubleLessOrEqualEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new DoubleLessOrEqualEvaluator();

        private DoubleLessOrEqualEvaluator() {
            super( Evaluator.DOUBLE_TYPE,
                   Evaluator.LESS_OR_EQUAL );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            return ((Number) object1).doubleValue() <= ((Number) object2).doubleValue();
        }

        public String toString() {
            return "Double <=";
        }
    }

    static class DoubleGreaterEvaluator extends BaseEvaluator {
        public final static Evaluator INSTANCE = new DoubleGreaterEvaluator();

        private DoubleGreaterEvaluator() {
            super( Evaluator.DOUBLE_TYPE,
                   Evaluator.GREATER );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            return ((Number) object1).doubleValue() > ((Number) object2).doubleValue();
        }

        public String toString() {
            return "Double >";
        }
    }

    static class DoubleGreaterOrEqualEvaluator extends BaseEvaluator {
        private final static Evaluator INSTANCE = new DoubleGreaterOrEqualEvaluator();

        private DoubleGreaterOrEqualEvaluator() {
            super( Evaluator.DOUBLE_TYPE,
                   Evaluator.GREATER_OR_EQUAL );
        }

        public boolean evaluate(Object object1,
                                Object object2) {
            return ((Number) object1).doubleValue() >= ((Number) object2).doubleValue();
        }

        public String toString() {
            return "Double >=";
        }
    }
}
