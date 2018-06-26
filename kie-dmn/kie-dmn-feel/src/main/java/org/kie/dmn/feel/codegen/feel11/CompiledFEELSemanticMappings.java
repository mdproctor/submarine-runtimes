/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.dmn.feel.codegen.feel11;

import java.math.BigDecimal;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.kie.dmn.api.feel.runtime.events.FEELEvent;
import org.kie.dmn.feel.lang.EvaluationContext;
import org.kie.dmn.feel.lang.ast.InfixOpNode;
import org.kie.dmn.feel.lang.ast.RangeNode;
import org.kie.dmn.feel.runtime.Range;
import org.kie.dmn.feel.runtime.UnaryTest;
import org.kie.dmn.feel.runtime.events.ASTEventBase;
import org.kie.dmn.feel.runtime.impl.RangeImpl;
import org.kie.dmn.feel.util.EvalHelper;
import org.kie.dmn.feel.util.Msg;

/**
 * The purpose of this class is to offer import .* methods to compiled FEEL classes compiling expressions.
 * Implementing DMN FEEL spec chapter 10.3.2.12 Semantic mappings
 */
public class CompiledFEELSemanticMappings {

    /**
     * Represents a [n..m] construct
     */
    public static RangeImpl range(
            EvaluationContext ctx,
            Range.RangeBoundary lowBoundary,
            Object lowEndPoint,
            Object highEndPoint,
            Range.RangeBoundary highBoundary) {

        Comparable left = asComparable(lowEndPoint);
        Comparable right = asComparable(highEndPoint);
        if (left == null || right == null || !compatible(left, right)) {
            ctx.notifyEvt( () -> new ASTEventBase(
                    FEELEvent.Severity.ERROR,
                    Msg.createMessage(
                            Msg.INCOMPATIBLE_TYPE_FOR_RANGE,
                            left.getClass().getSimpleName()
                    ),
                    null));
            return null;
        }
        return new RangeImpl(
                lowBoundary,
                left,
                right,
                highBoundary);
    }

    public static Boolean includes(
            EvaluationContext ctx,
            Object range,
            Object param) {
        if (range instanceof Range) {
            return ((Range) range).includes(param);
        } else if (range instanceof List) {
            return ((List) range).contains(param);
        } else if (range == null){
            ctx.notifyEvt( () -> new ASTEventBase(
                    FEELEvent.Severity.ERROR,
                    Msg.createMessage(
                            Msg.IS_NULL,
                            "range"),
                    null));
            return null;
        } else {
            return list(range).contains(param);
        }
    }

    private static boolean compatible(Comparable left, Comparable right) {
        Class<?> leftClass = left.getClass();
        Class<?> rightClass = right.getClass();
        return leftClass.isAssignableFrom(rightClass)
                || rightClass.isAssignableFrom(leftClass);
    }

    private static Comparable asComparable(Object s) {
        if (s instanceof Comparable) {
            return (Comparable) s;
        } else if (s instanceof Period) {
            // period has special semantics
            return new RangeNode.ComparablePeriod((Period) s);
        } else {
            // FIXME report error
            return null;
        }
    }
    
    public static <T> T coerceTo(Class<?> paramType, Object value) {
        Object actual;
        if( paramType.isAssignableFrom( value.getClass() ) ) {
            actual = value;
        } else {
            // try to coerce
            if( value instanceof Number ) {
                if( paramType == byte.class || paramType == Byte.class ) {
                    actual = ((Number)value).byteValue();
                } else if( paramType == short.class || paramType == Short.class ) {
                    actual = ((Number) value).shortValue();
                } else if( paramType == int.class || paramType == Integer.class ) {
                    actual = ((Number) value).intValue();
                } else if( paramType == long.class || paramType == Long.class ) {
                    actual = ((Number) value).longValue();
                } else if( paramType == float.class || paramType == Float.class ) {
                    actual = ((Number) value).floatValue();
                } else if( paramType == double.class || paramType == Double.class ) {
                    actual = ((Number) value).doubleValue();
                } else {
                    throw new IllegalArgumentException( "Unable to coerce parameter "+value+". Expected "+paramType+" but found "+value.getClass() );
                }
            } else if ( value instanceof String
                    && ((String) value).length() == 1
                    && (paramType == char.class || paramType == Character.class) ) {
                actual = ((String) value).charAt(0);
            } else if ( value instanceof Boolean && paramType == boolean.class ) {
                // Because Boolean can be also null, boolean.class is not assignable from Boolean.class. So we must coerce this.
                actual = value;
            } else {
                throw new IllegalArgumentException( "Unable to coerce parameter "+value+". Expected "+paramType+" but found "+value.getClass() );
            }
        }
        return (T) actual;
    }
    
    /**
     * Represent a [e1, e2, e3] construct.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> List<T> list(T... a) {
        ArrayList<T> result = new ArrayList<T>();
        if (a == null) {
            result.add(null);
            return result;
        }
        for (T elem : a) {
            result.add(elem);
        }
        return result;
    }

    /**
     * FEEL spec Table 38
     * Delegates to {@link InfixOpNode} except evaluationcontext
     */
    public static Object and(Object left, Object right) {
        return InfixOpNode.and(left, right, null);
    }
    
    public static Object and(boolean left, Object right) {
        if ( left == true ) {
            return EvalHelper.getBooleanOrNull( right );
        } else {
            return false;
        }
    }
    
    public static Object and(boolean left, boolean right) {
        return left && right;
    }

    /**
     * FEEL spec Table 38
     * Delegates to {@link InfixOpNode} except evaluationcontext
     */
    public static Object or(Object left, Object right) {
        return InfixOpNode.or(left, right, null);
    }
    
    public static Object or(Object left, boolean right) {
        if ( right == true ) {
            return true;
        } else {
            return EvalHelper.getBooleanOrNull( left );
        }
    }
    
    public static Object or(boolean left, boolean right) {
        return left || right;
    }

    /**
     * FEEL spec Table 45
     * Delegates to {@link InfixOpNode} except evaluationcontext
     */
    public static Object add(Object left, Object right) {
        return InfixOpNode.add(left, right, null);
    }

    /**
     * FEEL spec Table 45
     * Delegates to {@link InfixOpNode} except evaluationcontext
     */
    public static Object sub(Object left, Object right) {
        return InfixOpNode.sub(left, right, null);
    }

    /**
     * FEEL spec Table 45
     * Delegates to {@link InfixOpNode} except evaluationcontext
     */
    public static Object mult(Object left, Object right) {
        return InfixOpNode.mult(left, right, null);
    }

    /**
     * FEEL spec Table 45
     * Delegates to {@link InfixOpNode} except evaluationcontext
     */
    public static Object div(Object left, Object right) {
        return InfixOpNode.div(left, right, null);
    }

    // to ground to null if right = 0
    public static Object div(Object left, BigDecimal right) {
        return right.signum() == 0 ? null : InfixOpNode.div(left, right, null);
    }

    /**
     * FEEL spec Table 42 and derivations
     * Delegates to {@link EvalHelper} except evaluationcontext
     */
    public static Boolean lte(Object left, Object right) {
        return EvalHelper.compare(left, right, null, (l, r) -> l.compareTo(r) <= 0);
    }

    /**
     * FEEL spec Table 42 and derivations
     * Delegates to {@link EvalHelper} except evaluationcontext
     */
    public static Boolean lt(Object left, Object right) {
        return EvalHelper.compare(left, right, null, (l, r) -> l.compareTo(r) < 0);
    }

    /**
     * FEEL spec Table 42 and derivations
     * Delegates to {@link EvalHelper} except evaluationcontext
     */
    public static Boolean gte(Object left, Object right) {
        return EvalHelper.compare(left, right, null, (l, r) -> l.compareTo(r) >= 0);
    }

    /**
     * FEEL spec Table 42 and derivations
     * Delegates to {@link EvalHelper} except evaluationcontext
     */
    public static Boolean gt(Object left, Object right) {
        return EvalHelper.compare(left, right, null, (l, r) -> l.compareTo(r) > 0);
    }

    /**
     * FEEL spec Table 41: Specific semantics of equality
     * Delegates to {@link EvalHelper} except evaluationcontext
     */
    public static Boolean eq(Object left, Object right) {
        return EvalHelper.isEqual(left, right, null);
    }

    public static Boolean between(EvaluationContext ctx,
                                  Object value, Object start, Object end) {
        if ( value == null ) { ctx.notifyEvt(() -> new ASTEventBase(FEELEvent.Severity.ERROR, Msg.createMessage(Msg.IS_NULL, "value"), null) ); return null; }
        if ( start == null ) { ctx.notifyEvt(() -> new ASTEventBase(FEELEvent.Severity.ERROR, Msg.createMessage(Msg.IS_NULL, "start"), null) ); return null; }
        if ( end == null )   { ctx.notifyEvt(() -> new ASTEventBase(FEELEvent.Severity.ERROR, Msg.createMessage(Msg.IS_NULL, "end"), null) ); return null; }

        if (!value.getClass().isAssignableFrom(start.getClass())) {
            ctx.notifyEvt(() -> new ASTEventBase(FEELEvent.Severity.ERROR, Msg.createMessage(Msg.X_TYPE_INCOMPATIBLE_WITH_Y_TYPE, "value", "start"), null) );
            return null;
        }

        if (!value.getClass().isAssignableFrom(end.getClass())) {
            ctx.notifyEvt(() -> new ASTEventBase(FEELEvent.Severity.ERROR, Msg.createMessage(Msg.X_TYPE_INCOMPATIBLE_WITH_Y_TYPE, "value", "end"), null) );
            return null;
        }

        return gte(value, start) && lte(value, end);
    }

    /**
     * FEEL spec Table 39
     */
    public static Boolean ne(Object left, Object right) {
        return not(EvalHelper.isEqual(left, right, null));
    }

    public static Boolean not(Object arg, UnaryTest test) {
        return not(test.apply(null, arg));
    }

    private static Boolean not(Object arg) {
        if (Boolean.TRUE.equals(arg)) {
            return Boolean.FALSE;
        }
        if (Boolean.FALSE.equals(arg)) {
            return Boolean.TRUE;
        }
        return null;
    }
}
