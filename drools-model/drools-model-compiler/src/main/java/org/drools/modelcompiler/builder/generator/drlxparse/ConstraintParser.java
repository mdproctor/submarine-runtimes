package org.drools.modelcompiler.builder.generator.drlxparse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.drools.core.util.DateUtils;
import org.drools.core.util.index.IndexUtil;
import org.drools.javaparser.ast.NodeList;
import org.drools.javaparser.ast.body.MethodDeclaration;
import org.drools.javaparser.ast.drlx.OOPathExpr;
import org.drools.javaparser.ast.drlx.expr.DrlxExpression;
import org.drools.javaparser.ast.drlx.expr.HalfBinaryExpr;
import org.drools.javaparser.ast.drlx.expr.HalfPointFreeExpr;
import org.drools.javaparser.ast.drlx.expr.PointFreeExpr;
import org.drools.javaparser.ast.expr.BigDecimalLiteralExpr;
import org.drools.javaparser.ast.expr.BigIntegerLiteralExpr;
import org.drools.javaparser.ast.expr.BinaryExpr;
import org.drools.javaparser.ast.expr.CastExpr;
import org.drools.javaparser.ast.expr.EnclosedExpr;
import org.drools.javaparser.ast.expr.Expression;
import org.drools.javaparser.ast.expr.FieldAccessExpr;
import org.drools.javaparser.ast.expr.IntegerLiteralExpr;
import org.drools.javaparser.ast.expr.LiteralExpr;
import org.drools.javaparser.ast.expr.MethodCallExpr;
import org.drools.javaparser.ast.expr.NameExpr;
import org.drools.javaparser.ast.expr.ObjectCreationExpr;
import org.drools.javaparser.ast.expr.StringLiteralExpr;
import org.drools.javaparser.ast.expr.ThisExpr;
import org.drools.javaparser.ast.expr.UnaryExpr;
import org.drools.javaparser.ast.nodeTypes.NodeWithArguments;
import org.drools.javaparser.ast.nodeTypes.NodeWithOptionalScope;
import org.drools.modelcompiler.builder.PackageModel;
import org.drools.modelcompiler.builder.errors.InvalidExpressionErrorResult;
import org.drools.modelcompiler.builder.errors.ParseExpressionErrorResult;
import org.drools.modelcompiler.builder.generator.DrlxParseUtil;
import org.drools.modelcompiler.builder.generator.ModelGenerator;
import org.drools.modelcompiler.builder.generator.RuleContext;
import org.drools.modelcompiler.builder.generator.TypedExpression;
import org.drools.modelcompiler.builder.generator.expressiontyper.ExpressionTyper;
import org.drools.modelcompiler.builder.generator.expressiontyper.ExpressionTyperContext;
import org.drools.modelcompiler.builder.generator.expressiontyper.TypedExpressionResult;

import static org.drools.core.util.StringUtils.lcFirst;
import static org.drools.javaparser.ast.expr.BinaryExpr.Operator.GREATER;
import static org.drools.javaparser.ast.expr.BinaryExpr.Operator.GREATER_EQUALS;
import static org.drools.javaparser.ast.expr.BinaryExpr.Operator.LESS;
import static org.drools.javaparser.ast.expr.BinaryExpr.Operator.LESS_EQUALS;
import static org.drools.modelcompiler.builder.generator.DrlxParseUtil.getLiteralExpressionType;
import static org.drools.modelcompiler.builder.generator.DrlxParseUtil.isPrimitiveExpression;
import static org.drools.modelcompiler.builder.generator.DrlxParseUtil.toClassOrInterfaceType;
import static org.drools.modelcompiler.builder.generator.DrlxParseUtil.toNewBigDecimalExpr;

public class ConstraintParser {

    public static final boolean GENERATE_EXPR_ID = true;

    private RuleContext context;
    private PackageModel packageModel;

    public ConstraintParser(RuleContext context, PackageModel packageModel) {
        this.context = context;
        this.packageModel = packageModel;
    }

    public DrlxParseResult drlxParse(Class<?> patternType, String bindingId, String expression) {
        return drlxParse(patternType, bindingId, expression, false);
    }

    public DrlxParseResult drlxParse(Class<?> patternType, String bindingId, String expression, boolean isPositional) {
        return drlxParse(patternType, bindingId, new ConstraintExpression(expression), isPositional);
    }

    public DrlxParseResult drlxParse(Class<?> patternType, String bindingId, ConstraintExpression expression, boolean isPositional) {
        DrlxExpression drlx = DrlxParseUtil.parseExpression( expression.getExpression() );
        DrlxParseResult drlxParseResult = getDrlxParseResult(patternType, bindingId, expression, drlx.getExpr(), drlx.getBind() != null, isPositional );

        drlxParseResult.accept(result -> {
            if (drlx.getBind() != null) {
                String bindId = drlx.getBind().asString();
                context.addDeclaration( bindId, result.getExprRawClass() );
                result.setExprBinding( bindId );
            }

        });

        return drlxParseResult;
    }

    private DrlxParseResult getDrlxParseResult(Class<?> patternType, String bindingId, ConstraintExpression constraint, Expression drlxExpr, boolean hasBind, boolean isPositional ) {
        boolean isEnclosed = false;
        while (drlxExpr instanceof EnclosedExpr) {
            drlxExpr = (( EnclosedExpr ) drlxExpr).getInner();
            isEnclosed = true;
        }

        if (drlxExpr instanceof MethodCallExpr && !(( MethodCallExpr ) drlxExpr).getScope().isPresent() && (( MethodCallExpr ) drlxExpr).getNameAsString().equals( "eval" )) {
            drlxExpr = (( MethodCallExpr ) drlxExpr).getArgument( 0 );
        }

        String expression = constraint.getExpression();
        String exprId;
        if ( GENERATE_EXPR_ID ) {
            exprId = context.getExprId( patternType, hasBind ? expression.substring( expression.indexOf( ':' )+1 ).trim() : expression );
        }

        if ( drlxExpr instanceof BinaryExpr ) {
            return parseBinaryExpr( (BinaryExpr) drlxExpr, patternType, bindingId, constraint, drlxExpr, hasBind, isPositional, isEnclosed, expression, exprId );
        }

        if ( drlxExpr instanceof UnaryExpr ) {
            return parseUnaryExpr( (UnaryExpr) drlxExpr, patternType, bindingId, isPositional, exprId );
        }

        if ( drlxExpr instanceof PointFreeExpr ) {
            return parsePointFreeExpr( (PointFreeExpr) drlxExpr, patternType, bindingId, constraint, hasBind, isPositional, exprId );
        }

        if (drlxExpr instanceof MethodCallExpr) {
            MethodCallExpr methodCallExpr = (MethodCallExpr) drlxExpr;

            Optional<MethodDeclaration> functionCall = packageModel.getFunctions().stream().filter(m -> m.getName().equals(methodCallExpr.getName())).findFirst();
            if (functionCall.isPresent()) {
                // when the methodCallExpr will be placed in the model/DSL, any parameter being a "this" need to be implemented as _this by convention.
                List<ThisExpr> rewriteThisExprs = recurseCollectArguments(methodCallExpr).stream()
                        .filter(ThisExpr.class::isInstance)
                        .map(ThisExpr.class::cast)
                        .collect(Collectors.toList());
                for (ThisExpr t : rewriteThisExprs) {
                    methodCallExpr.replace(t, new NameExpr("_this"));
                }

                Class<?> returnType = DrlxParseUtil.getClassFromContext(context.getTypeResolver(), functionCall.get().getType().asString());
                NodeList<Expression> arguments = methodCallExpr.getArguments();
                List<String> usedDeclarations = new ArrayList<>();
                for (Expression arg : arguments) {
                    if (arg instanceof NameExpr && !arg.toString().equals("_this")) {
                        usedDeclarations.add(arg.toString());
                    } else if (arg instanceof CastExpr) {
                        usedDeclarations.add((((CastExpr)arg).getExpression().toString()));
                    } else if (arg instanceof MethodCallExpr) {
                        TypedExpressionResult typedExpressionResult = new ExpressionTyper(context, null, bindingId, isPositional).toTypedExpression(arg);
                        usedDeclarations.addAll(typedExpressionResult.getUsedDeclarations());
                    }
                }
                return new DrlxParseSuccess(patternType, exprId, bindingId, methodCallExpr, returnType).setUsedDeclarations(usedDeclarations);
            }
        }

        if (drlxExpr instanceof FieldAccessExpr) {
            return parseFieldAccessExpr( ( FieldAccessExpr ) drlxExpr, patternType, bindingId, exprId );
        }

        if (drlxExpr instanceof NameExpr) {
            return parseNameExpr( (NameExpr) drlxExpr, patternType, bindingId, drlxExpr, hasBind, expression, exprId );
        }

        if (drlxExpr instanceof OOPathExpr ) {
            return new DrlxParseSuccess( patternType, exprId, bindingId, drlxExpr, null );
        }

        if (drlxExpr instanceof LiteralExpr ) {
            return new DrlxParseSuccess( patternType, exprId, bindingId, drlxExpr, getLiteralExpressionType( (( LiteralExpr ) drlxExpr) ) );
        }

        if (patternType != null) {
            ExpressionTyperContext expressionTyperContext = new ExpressionTyperContext();
            ExpressionTyper expressionTyper = new ExpressionTyper(context, patternType, bindingId, isPositional, expressionTyperContext);
            TypedExpressionResult leftTypedExpressionResult = expressionTyper.toTypedExpression(drlxExpr);
            Optional<TypedExpression> optLeft = leftTypedExpressionResult.getTypedExpression();
            if ( !optLeft.isPresent() ) {
                return new DrlxParseFail( new InvalidExpressionErrorResult( "Unable to parse left part of expression: " + expression ) );
            }
            TypedExpression left = optLeft.get();
            Expression combo = left.getExpression();
            for(Expression e : leftTypedExpressionResult.getPrefixExpressions()) {
                combo = new BinaryExpr(e, combo, BinaryExpr.Operator.AND );
            }
            return new DrlxParseSuccess(patternType, exprId, bindingId, combo, left.getType())
                    .setUsedDeclarations( expressionTyperContext.getUsedDeclarations() );
        } else {
            final ExpressionTyperContext expressionTyperContext = new ExpressionTyperContext();
            final ExpressionTyper expressionTyper = new ExpressionTyper(context, null, bindingId, isPositional, expressionTyperContext);

            TypedExpressionResult leftTypedExpressionResult = expressionTyper.toTypedExpression(drlxExpr);
            Optional<TypedExpression> optLeft = leftTypedExpressionResult.getTypedExpression();
            if ( !optLeft.isPresent() ) {
                return new DrlxParseFail( new InvalidExpressionErrorResult( "Unable to parse left part of expression: " + expression ) );
            }

            TypedExpression left = optLeft.get();
            return new DrlxParseSuccess(null, exprId, bindingId, drlxExpr, left.getType()).setUsedDeclarations( expressionTyperContext.getUsedDeclarations() );
        }
    }

    private DrlxParseResult parseNameExpr( NameExpr nameExpr, Class<?> patternType, String bindingId, Expression drlxExpr, boolean hasBind, String expression, String exprId ) {
        TypedExpression converted = DrlxParseUtil.toMethodCallWithClassCheck(context, nameExpr, bindingId, patternType, context.getTypeResolver());
        if (converted == null) {
            return new DrlxParseFail();
        }
        Expression withThis = DrlxParseUtil.prepend(new NameExpr("_this"), converted.getExpression());

        if (hasBind) {
            return new DrlxParseSuccess(patternType, exprId, bindingId, null, converted.getType() )
                    .setLeft( new TypedExpression( withThis, converted.getType() ) )
                    .addReactOnProperty( lcFirst(nameExpr.getNameAsString()) );
        } else if (context.hasDeclaration( expression )) {
            return new DrlxParseSuccess(patternType, exprId, bindingId, context.getVarExpr( drlxExpr.toString() ), context.getDeclarationById( expression ).get().getDeclarationClass() );
        } else {
            return new DrlxParseSuccess(patternType, exprId, bindingId, withThis, converted.getType() )
                    .addReactOnProperty( nameExpr.getNameAsString() );
        }
    }

    private DrlxParseResult parseFieldAccessExpr( FieldAccessExpr fieldCallExpr, Class<?> patternType, String bindingId, String exprId ) {
        TypedExpression converted = DrlxParseUtil.toMethodCallWithClassCheck(context, fieldCallExpr, bindingId, patternType, context.getTypeResolver());
        Expression withThis = DrlxParseUtil.prepend(new NameExpr("_this"), converted.getExpression());
        return new DrlxParseSuccess(patternType, exprId, bindingId, withThis, converted.getType()).setLeft(converted );
    }

    private DrlxParseResult parsePointFreeExpr( PointFreeExpr pointFreeExpr, Class<?> patternType, String bindingId, ConstraintExpression constraint, boolean hasBind, boolean isPositional, String exprId ) {
        TypedExpressionResult typedExpressionResult = new ExpressionTyper(context, patternType, bindingId, isPositional).toTypedExpression(pointFreeExpr);

        return typedExpressionResult.getTypedExpression().<DrlxParseResult>map(typedExpression -> {
            boolean isTemporal = ModelGenerator.temporalOperators.contains(pointFreeExpr.getOperator().asString());
            Object rightLiteral = null;
            if (isTemporal && pointFreeExpr.getRight().size() == 1) {
                Expression rightExpr = pointFreeExpr.getRight().get(0);
                if (rightExpr instanceof StringLiteralExpr ) {
                    String value = (( StringLiteralExpr ) rightExpr).getValue();
                    rightLiteral = DateUtils.parseDate(value).getTime() + "L";
                }
            }

            return new DrlxParseSuccess(patternType, exprId, bindingId, typedExpression.getExpression(), typedExpression.getType())
                    .setUsedDeclarations(typedExpressionResult.getUsedDeclarations())
                    .setUsedDeclarationsOnLeft( Collections.emptyList())
                    .setReactOnProperties(typedExpressionResult.getReactOnProperties())
                    .setLeft(typedExpression.getLeft())
                    .setRight(typedExpression.getRight())
                    .setRightLiteral(rightLiteral)
                    .setStatic(typedExpression.isStatic())
                    .setTemporal( isTemporal )
                    .setValidExpression(true);
        }).orElseGet( () -> new DrlxParseFail( new ParseExpressionErrorResult(pointFreeExpr) ));
    }

    private DrlxParseResult parseUnaryExpr( UnaryExpr unaryExpr, Class<?> patternType, String bindingId, boolean isPositional, String exprId ) {
        TypedExpressionResult typedExpressionResult = new ExpressionTyper(context, patternType, bindingId, isPositional).toTypedExpression(unaryExpr);
        return typedExpressionResult.getTypedExpression().<DrlxParseResult>map(left -> new DrlxParseSuccess(patternType, exprId, bindingId, left.getExpression(), left.getType())
                .setUsedDeclarations( typedExpressionResult.getUsedDeclarations() ).setReactOnProperties( typedExpressionResult.getReactOnProperties() )
                .setLeft( left ) ).orElseGet( () -> new DrlxParseFail( new ParseExpressionErrorResult(unaryExpr) ));
    }

    private DrlxParseResult parseBinaryExpr( BinaryExpr binaryExpr, Class<?> patternType, String bindingId, ConstraintExpression constraint, Expression drlxExpr, boolean hasBind, boolean isPositional, boolean isEnclosed, String expression, String exprId ) {
        BinaryExpr.Operator operator = binaryExpr.getOperator();

        if ((operator == BinaryExpr.Operator.AND || operator == BinaryExpr.Operator.OR) && !(binaryExpr.getRight() instanceof HalfPointFreeExpr) && !(binaryExpr.getRight() instanceof HalfBinaryExpr)) {
            DrlxParseResult leftResult = getDrlxParseResult(patternType, bindingId, constraint, binaryExpr.getLeft(), hasBind, isPositional );
            DrlxParseResult rightResult = getDrlxParseResult(patternType, bindingId, constraint, binaryExpr.getRight(), hasBind, isPositional );
            return leftResult.combineWith( rightResult, operator );
        }

        IndexUtil.ConstraintType decodeConstraintType = DrlxParseUtil.toConstraintType(operator );

        final ExpressionTyperContext expressionTyperContext = new ExpressionTyperContext();
        final ExpressionTyper expressionTyper = new ExpressionTyper(context, patternType, bindingId, isPositional, expressionTyperContext);

        TypedExpressionResult leftTypedExpressionResult = expressionTyper.toTypedExpression(binaryExpr.getLeft());
        Optional<TypedExpression> optLeft = leftTypedExpressionResult.getTypedExpression();
        if ( !optLeft.isPresent() ) {
            return new DrlxParseFail( new InvalidExpressionErrorResult( "Unable to parse left part of expression: " + expression ) );
        }

        TypedExpression left = optLeft.get();
        List<String> usedDeclarationsOnLeft = hasBind ? new ArrayList<>( expressionTyperContext.getUsedDeclarations() ) : null;

        TypedExpression right;
        if (constraint.isNameClashingUnification()) {
            String name = constraint.getUnificationField();
            right = new TypedExpression( new NameExpr( name ), left.getType() );
            expressionTyperContext.addUsedDeclarations( name );
        } else {
            TypedExpressionResult rightExpressionResult = expressionTyper.toTypedExpression( binaryExpr.getRight() );
            Optional<TypedExpression> optRight = rightExpressionResult.getTypedExpression();
            if ( !optRight.isPresent() ) {
                return new DrlxParseFail( new ParseExpressionErrorResult( drlxExpr ) );
            }
            right = optRight.get();
        }

        CoercedExpression.CoercedExpressionResult coerced;
        try {
            coerced = new CoercedExpression(left, right).coerce();
        } catch (CoercedExpression.CoercedExpressionException e) {
            return new DrlxParseFail(e.getInvalidExpressionErrorResult());
        }

        left = coerced.getCoercedLeft();
        right = coerced.getCoercedRight();

        Expression combo;

        if(left.isPrimitive()) {
            combo = new BinaryExpr( left.getExpression(), right.getExpression(), operator );
        } else {
            switch (operator) {
                case EQUALS:
                case NOT_EQUALS:
                    combo = getEqualityExpression(left, right, operator);
                    break;
                default:
                    if (left.getExpression() == null || right.getExpression() == null) {
                        return new DrlxParseFail(new ParseExpressionErrorResult(drlxExpr));
                    }
                    combo = handleSpecialComparisonCases(operator, left, right);
            }
        }

        for(Expression e : leftTypedExpressionResult.getPrefixExpressions()) {
            combo = new BinaryExpr(e, combo, BinaryExpr.Operator.AND );
        }


        boolean isBetaNode = right.getExpression() != null && context.getDeclarationById( getExpressionSymbol( right.getExpression() ) ).isPresent();
        if (isEnclosed) {
            combo = new EnclosedExpr( combo );
        }

        boolean requiresSplit = operator == BinaryExpr.Operator.AND && binaryExpr.getRight() instanceof HalfBinaryExpr && !isBetaNode;
        return new DrlxParseSuccess(patternType, exprId, bindingId, combo, left.getType()).setDecodeConstraintType( decodeConstraintType )
                .setUsedDeclarations( expressionTyperContext.getUsedDeclarations() ).setUsedDeclarationsOnLeft( usedDeclarationsOnLeft ).setUnification( constraint.isUnification() )
                .setReactOnProperties( expressionTyperContext.getReactOnProperties() ).setLeft( left ).setRight( right ).setBetaNode(isBetaNode).setRequiresSplit( requiresSplit );
    }

    private static String getExpressionSymbol(Expression expr) {
        if (expr instanceof MethodCallExpr && (( MethodCallExpr ) expr).getScope().isPresent()) {
            return getExpressionSymbol( (( MethodCallExpr ) expr).getScope().get() );
        }
        if (expr instanceof FieldAccessExpr ) {
            return getExpressionSymbol( (( FieldAccessExpr ) expr).getScope() );
        }
        return expr.toString();
    }

    private static Expression getEqualityExpression( TypedExpression left, TypedExpression right, BinaryExpr.Operator operator ) {
        if(isAnyOperandBigDecimal(left, right) || isAnyOperandBigInteger(left, right)) {
            return compareBigDecimal(operator, left, right);
        }

        final Expression rightExpression = right.getExpression();
        final Expression leftExpression = left.getExpression();

        if (isPrimitiveExpression(rightExpression) && isPrimitiveExpression(leftExpression)) {
            if (left.getType() != String.class) {
                return new BinaryExpr(leftExpression, rightExpression, operator == BinaryExpr.Operator.EQUALS ? BinaryExpr.Operator.EQUALS : BinaryExpr.Operator.NOT_EQUALS );
            }
        }

        String equalsMethod = !left.getType().equals( right.getType() ) && Number.class.isAssignableFrom( left.getRawClass() ) ?
                "org.drools.modelcompiler.util.EvaluationUtil.areNumbersNullSafeEquals" :
                "org.drools.modelcompiler.util.EvaluationUtil.areNullSafeEquals";

        MethodCallExpr methodCallExpr = new MethodCallExpr( null, equalsMethod );
        methodCallExpr.addArgument(left.getExpression());
        methodCallExpr.addArgument(right.getExpression()); // don't create NodeList with static method because missing "parent for child" would null and NPE
        return operator == BinaryExpr.Operator.EQUALS ? methodCallExpr : new UnaryExpr(methodCallExpr, UnaryExpr.Operator.LOGICAL_COMPLEMENT );
    }

    private static Expression handleSpecialComparisonCases(BinaryExpr.Operator operator, TypedExpression left, TypedExpression right ) {
        if ((isAnyOperandBigDecimal(left, right) || isAnyOperandBigInteger(left, right)) && (isComparisonOperator(operator))) {
            return compareBigDecimal(operator, left, right);
        }

        if ( isComparisonOperator( operator ) ) {
            MethodCallExpr compareMethod = null;
            if ( left.getType() == String.class && right.getType() == String.class ) {
                compareMethod = new MethodCallExpr( null, "org.drools.modelcompiler.util.EvaluationUtil.compareStringsAsNumbers" );
            } else if ( isNumericType( left.getRawClass() ) || isNumericType( right.getRawClass() ) ) {
                compareMethod = new MethodCallExpr( null, "org.drools.modelcompiler.util.EvaluationUtil.compareNumbers" );
            } else if ( Comparable.class.isAssignableFrom( left.getRawClass() ) && Comparable.class.isAssignableFrom( right.getRawClass() ) ) {
                compareMethod = new MethodCallExpr( null, "org.drools.modelcompiler.util.EvaluationUtil.compare" );
            }

            if (compareMethod != null) {
                compareMethod.addArgument( left.getExpression() );
                compareMethod.addArgument( right.getExpression() );
                compareMethod.addArgument( new StringLiteralExpr( operator.asString() ) );
                return compareMethod;
            }
        }

        return new BinaryExpr( left.getExpression(), right.getExpression(), operator );
    }

    private static boolean isNumericType(Class<?> type) {
        return Number.class.isAssignableFrom( type ) && type != BigInteger.class && type != BigDecimal.class;
    }

    private static boolean isAnyOperandBigDecimal(TypedExpression left, TypedExpression right) {
        return left.getType() == BigDecimal.class || right.getType() == BigDecimal.class;
    }

    private static boolean isAnyOperandBigInteger(TypedExpression left, TypedExpression right) {
        return left.getType() == BigInteger.class || right.getType() == BigInteger.class;
    }

    private static Expression compareBigDecimal(BinaryExpr.Operator operator, TypedExpression left, TypedExpression right) {
        final TypedExpression convertedLeft = left.cloneWithNewExpression(convertExpressionToBigDecimal(left));
        final TypedExpression convertedRight = right.cloneWithNewExpression(convertExpressionToBigDecimal(right));
        final MethodCallExpr methodCallExpr = new MethodCallExpr(convertedLeft.getExpression(), "compareTo");
        methodCallExpr.addArgument(convertedRight.getExpression());
        return new BinaryExpr(methodCallExpr, new IntegerLiteralExpr(0), operator);
    }

    private static Expression convertExpressionToBigDecimal(TypedExpression te) {
        final Expression convertedExpression;
        if(te.getType() == BigInteger.class) {
            if(te.getExpression() instanceof BigIntegerLiteralExpr) {
                convertedExpression = toNewBigDecimalExpr(new StringLiteralExpr(((BigIntegerLiteralExpr) te.getExpression()).asBigInteger().toString()));
            } else {
                convertedExpression = new ObjectCreationExpr(null, toClassOrInterfaceType(BigDecimal.class),
                                             NodeList.nodeList(te.getExpression()));
            }
        }
        else if(te.getType() != BigDecimal.class) {
            convertedExpression = new MethodCallExpr(new NameExpr(BigDecimal.class.getCanonicalName()), "valueOf")
                    .addArgument(te.getExpression());
        } else if(te.getExpression() instanceof BigDecimalLiteralExpr) {
            convertedExpression = toNewBigDecimalExpr(new StringLiteralExpr(((BigDecimalLiteralExpr) te.getExpression()).asBigDecimal().toString()));
        } else {
            convertedExpression = te.getExpression();
        }
        return convertedExpression;
    }

    private static boolean isComparisonOperator( BinaryExpr.Operator op ) {
        return op == LESS || op == GREATER || op == LESS_EQUALS || op == GREATER_EQUALS;

    }

    private static List<Expression> recurseCollectArguments(NodeWithArguments<?> methodCallExpr) {
        List<Expression> res = new ArrayList<>( methodCallExpr.getArguments() );
        if ( methodCallExpr instanceof NodeWithOptionalScope ) {
            NodeWithOptionalScope<?> nodeWithOptionalScope = (NodeWithOptionalScope) methodCallExpr;
            if ( nodeWithOptionalScope.getScope().isPresent() ) {
                Object scope = nodeWithOptionalScope.getScope().get();
                if (scope instanceof NodeWithArguments) {
                    res.addAll(recurseCollectArguments((NodeWithArguments<?>) scope));
                }
            }
        }
        return res;
    }
}
