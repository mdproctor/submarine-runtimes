package org.kie.dmn.feel.codegen.feel11;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import org.kie.dmn.feel.lang.CompositeType;
import org.kie.dmn.feel.lang.Type;
import org.kie.dmn.feel.lang.ast.InfixOpNode.InfixOperator;
import org.kie.dmn.feel.lang.impl.JavaBackedType;
import org.kie.dmn.feel.lang.impl.MapBackedType;
import org.kie.dmn.feel.lang.types.BuiltInType;
import org.kie.dmn.feel.parser.feel11.FEEL_1_1BaseVisitor;
import org.kie.dmn.feel.parser.feel11.FEEL_1_1Parser;
import org.kie.dmn.feel.parser.feel11.FEEL_1_1Parser.NameRefContext;
import org.kie.dmn.feel.parser.feel11.ParserHelper;
import org.kie.dmn.feel.util.EvalHelper;

public class DirectCompilerVisitor extends FEEL_1_1BaseVisitor<DirectCompilerResult> {

    private static final Expression DECIMAL_128 = JavaParser.parseExpression("java.math.MathContext.DECIMAL128");
    // TODO as this is now compiled it might not be needed for this compilation strategy, just need the layer 0 of input Types, but to be checked.
    private ScopeHelper scopeHelper;

    private static class ScopeHelper {

        Deque<Map<String, Type>> stack;

        public ScopeHelper() {
            this.stack = new ArrayDeque<>();
            this.stack.push(new HashMap<>());
        }

        public void addTypes(Map<String, Type> inputTypes) {
            stack.peek().putAll(inputTypes);
        }

        public void addType(String name, Type type) {
            stack.peek().put(name,
                             type);
        }

        public void pushScope() {
            stack.push(new HashMap<>());
        }

        public void popScope() {
            stack.pop();
        }

        public Optional<Type> resolveType(String name) {
            return stack.stream()
                        .map(scope -> Optional.ofNullable(scope.get(name)))
                        .flatMap(o -> o.isPresent() ? Stream.of(o.get()) : Stream.empty())
                        .findFirst();
        }
    }

    public DirectCompilerVisitor(Map<String, Type> inputTypes) {
        this.scopeHelper = new ScopeHelper();
        this.scopeHelper.addTypes(inputTypes);
    }

    @Override
    public DirectCompilerResult visitNumberLiteral(FEEL_1_1Parser.NumberLiteralContext ctx) {
        ObjectCreationExpr result = new ObjectCreationExpr();
        result.setType(JavaParser.parseClassOrInterfaceType(BigDecimal.class.getCanonicalName()));
        String originalText = ParserHelper.getOriginalText(ctx);
        result.addArgument(originalText);
        result.addArgument(DECIMAL_128);
        String constantName = "K_"+originalText;
        VariableDeclarator vd = new VariableDeclarator(JavaParser.parseClassOrInterfaceType(BigDecimal.class.getCanonicalName()), constantName);
        vd.setInitializer(result);
        FieldDeclaration fd = new FieldDeclaration();
        fd.setModifier(Modifier.PUBLIC, true);
        fd.setModifier(Modifier.STATIC, true);
        fd.setModifier(Modifier.FINAL, true);
        fd.addVariable(vd);
        return DirectCompilerResult.of(new NameExpr(constantName), BuiltInType.NUMBER, fd);
    }
    
    @Override
    public DirectCompilerResult visitBooleanLiteral(FEEL_1_1Parser.BooleanLiteralContext ctx) {
        Expression result = null;
        String literalText = ParserHelper.getOriginalText(ctx);
        // FEEL spec grammar rule 36. Boolean literal = "true" | "false" ;
        switch (literalText) {
            case "true":
                result = new BooleanLiteralExpr(true);
                break;
            case "false":
                result = new BooleanLiteralExpr(false);
                break;
            default:
                throw new IllegalArgumentException("Reached for a boolean literal but was: "+literalText);
        }
        return DirectCompilerResult.of(result, BuiltInType.BOOLEAN);
    }
    
    @Override
    public DirectCompilerResult visitSignedUnaryExpression(FEEL_1_1Parser.SignedUnaryExpressionContext ctx) {
        DirectCompilerResult unaryExpr = visit( ctx.unaryExpression() );
        if ( unaryExpr.resultType != BuiltInType.NUMBER ) {
            throw new IllegalArgumentException("signedunary should be only over a FEEL NUMBER (bigdecimal).");
        }
        if ( !ctx.start.getText().equals("-") ) {
            throw new IllegalArgumentException("FEEL spec Table 50: Semantics of negative numbers defines only -e.");
        }
        // therefore, unaryExpr is a bigdecimal and operator is `-`.
        MethodCallExpr result = new MethodCallExpr(unaryExpr.expression, "negate");
        return DirectCompilerResult.of(result, unaryExpr.resultType, unaryExpr.getFieldDeclarations() );
    }

    @Override
    public DirectCompilerResult visitNullLiteral(FEEL_1_1Parser.NullLiteralContext ctx) {
        NullLiteralExpr result = new NullLiteralExpr();
        return DirectCompilerResult.of(result, BuiltInType.UNKNOWN);
    }

    @Override
    public DirectCompilerResult visitStringLiteral(FEEL_1_1Parser.StringLiteralContext ctx) {
        StringLiteralExpr expr = new StringLiteralExpr(EvalHelper.unescapeString(ParserHelper.getOriginalText(ctx)));
        return DirectCompilerResult.of(expr, BuiltInType.STRING);
    }
    
    @Override
    public DirectCompilerResult visitPrimaryParens(FEEL_1_1Parser.PrimaryParensContext ctx) {
        DirectCompilerResult expr = visit( ctx.expression() );
        EnclosedExpr result = new EnclosedExpr(expr.expression);
        return DirectCompilerResult.of(result, expr.resultType, expr.getFieldDeclarations());
    }

//    @Override
//    public DirectCompilerResult visitLogicalNegation(FEEL_1_1Parser.LogicalNegationContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitPowExpression(FEEL_1_1Parser.PowExpressionContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitMultExpression(FEEL_1_1Parser.MultExpressionContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }

    @Override
    public DirectCompilerResult visitAddExpression(FEEL_1_1Parser.AddExpressionContext ctx) {
        DirectCompilerResult left = visit( ctx.additiveExpression() );
        DirectCompilerResult right = visit( ctx.multiplicativeExpression() );
        
        String opText = ctx.op.getText();
        InfixOperator op = InfixOperator.determineOperator(opText);
        if ( op == InfixOperator.ADD ) {
            return visitAdd(left, right);
        } else if ( op == InfixOperator.SUB ) {
            return visitSub(left, right);
        } else {
            throw new UnsupportedOperationException(); // parser problem.
        }
    }
    
    /**
     * PLEASE NOTICE:
     * operation may perform a check for null literal values, but might need this utility for runtime purposes.
     */
    private Expression groundToNullIfAnyIsNull(Expression originalOperation, Expression... arguments) {
        // Q: What is heavier, checking a list of arguments each one is not null, or just doing the operation on the arguments and try-catch the NPE, please?
        // A: raising exceptions is a lot heavier
        BinaryExpr nullChecks = Stream.of(arguments)
                                      .map(e -> new BinaryExpr(e, new NullLiteralExpr(), BinaryExpr.Operator.EQUALS))
                                      .reduce( (x, y) -> new BinaryExpr(x, y, BinaryExpr.Operator.OR) )
                                      .get();
        
        return new ConditionalExpr(new EnclosedExpr(nullChecks), new NullLiteralExpr(), originalOperation);
    }
    
    private DirectCompilerResult visitAdd( DirectCompilerResult left, DirectCompilerResult right ) {
        if ( left.expression instanceof NullLiteralExpr || right.expression instanceof NullLiteralExpr ) {
            // optimization: if either left or right is a null literal, just null
            return DirectCompilerResult.of(new NullLiteralExpr(), BuiltInType.STRING, DirectCompilerResult.unifyFDs(left, right));
        } else if ( left.resultType == BuiltInType.STRING && right.resultType == BuiltInType.STRING ) {
            BinaryExpr plusCall = new BinaryExpr(left.expression, right.expression, BinaryExpr.Operator.PLUS);
            Expression result = groundToNullIfAnyIsNull(plusCall, left.expression, right.expression);
            return DirectCompilerResult.of(result, BuiltInType.STRING, DirectCompilerResult.unifyFDs(left, right));
        } else if ( left.resultType == BuiltInType.NUMBER && right.resultType == BuiltInType.NUMBER ) {
            MethodCallExpr addCall = new MethodCallExpr(left.expression, "add");
            addCall.addArgument(right.expression);
            addCall.addArgument(DECIMAL_128);
            Expression result = groundToNullIfAnyIsNull(addCall, left.expression, right.expression);
            return DirectCompilerResult.of(result, BuiltInType.NUMBER, DirectCompilerResult.unifyFDs(left, right));
        } else {
            throw new UnsupportedOperationException("TODO"); // TODO
        }
    }
    
    private DirectCompilerResult visitSub( DirectCompilerResult left, DirectCompilerResult right ) {
        if ( left.expression instanceof NullLiteralExpr || right.expression instanceof NullLiteralExpr ) {
            // optimization: if either left or right is a null literal, just null
            return DirectCompilerResult.of(new NullLiteralExpr(), BuiltInType.STRING, DirectCompilerResult.unifyFDs(left, right));
        } else if ( left.resultType == BuiltInType.STRING && right.resultType == BuiltInType.STRING ) {
            // DMN spec Table 45
            // Subtraction is undefined.
            // TODO incosistent when FEEL is evaluated, because for now is important to check the actual java code produced
            BinaryExpr postFixMinus = new BinaryExpr(left.expression, new StringLiteralExpr("-"), BinaryExpr.Operator.PLUS);
            BinaryExpr plusCall = new BinaryExpr(postFixMinus, right.expression, BinaryExpr.Operator.PLUS);
            Expression result = groundToNullIfAnyIsNull(plusCall, left.expression, right.expression);
            return DirectCompilerResult.of(result, BuiltInType.STRING, DirectCompilerResult.unifyFDs(left, right));
        } else if ( left.resultType == BuiltInType.NUMBER && right.resultType == BuiltInType.NUMBER ) {
            MethodCallExpr subtractCall = new MethodCallExpr(left.expression, "subtract");
            subtractCall.addArgument(right.expression);
            subtractCall.addArgument(DECIMAL_128);
            Expression result = groundToNullIfAnyIsNull(subtractCall, left.expression, right.expression);
            return DirectCompilerResult.of(result, BuiltInType.NUMBER, DirectCompilerResult.unifyFDs(left, right));
        } else {
            throw new UnsupportedOperationException("TODO"); // TODO
        }
    }

//    @Override
//    public DirectCompilerResult visitRelExpressionBetween(FEEL_1_1Parser.RelExpressionBetweenContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitExpressionList(FEEL_1_1Parser.ExpressionListContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitRelExpressionValueList(FEEL_1_1Parser.RelExpressionValueListContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitInterval(FEEL_1_1Parser.IntervalContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitPositiveUnaryTestIneq(FEEL_1_1Parser.PositiveUnaryTestIneqContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitSimpleUnaryTests(FEEL_1_1Parser.SimpleUnaryTestsContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitRelExpressionTestList(FEEL_1_1Parser.RelExpressionTestListContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//    
//    @Override
//    public DirectCompilerResult visitRelExpressionValue(RelExpressionValueContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitPositiveUnaryTestNull(FEEL_1_1Parser.PositiveUnaryTestNullContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitPositiveUnaryTestDash(FEEL_1_1Parser.PositiveUnaryTestDashContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitCompExpression(FEEL_1_1Parser.CompExpressionContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }

    @Override
    public DirectCompilerResult visitCondOr(FEEL_1_1Parser.CondOrContext ctx) {
        DirectCompilerResult left = visit( ctx.left );
        DirectCompilerResult right = visit( ctx.right );
        MethodCallExpr result = new MethodCallExpr(new NameExpr(CompiledFEELUtils.class.getSimpleName()), "or");
        result.addArgument(left.expression);
        result.addArgument(right.expression);
        return DirectCompilerResult.of(result, BuiltInType.BOOLEAN);
    }

    @Override
    public DirectCompilerResult visitCondAnd(FEEL_1_1Parser.CondAndContext ctx) {
        DirectCompilerResult left = visit( ctx.left );
        DirectCompilerResult right = visit( ctx.right );
        MethodCallExpr result = new MethodCallExpr(new NameExpr(CompiledFEELUtils.class.getSimpleName()), "and");
        result.addArgument(left.expression);
        result.addArgument(right.expression);
        return DirectCompilerResult.of(result, BuiltInType.BOOLEAN);
    }

//    @Override
//    public DirectCompilerResult visitList(FEEL_1_1Parser.ListContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitNameDefinition(FEEL_1_1Parser.NameDefinitionContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitKeyString(FEEL_1_1Parser.KeyStringContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitContextEntry(FEEL_1_1Parser.ContextEntryContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitContextEntries(FEEL_1_1Parser.ContextEntriesContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitContext(FEEL_1_1Parser.ContextContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitFormalParameters(FEEL_1_1Parser.FormalParametersContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitFunctionDefinition(FEEL_1_1Parser.FunctionDefinitionContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitIterationContext(FEEL_1_1Parser.IterationContextContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitIterationContexts(FEEL_1_1Parser.IterationContextsContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitForExpression(FEEL_1_1Parser.ForExpressionContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }

    @Override
    public DirectCompilerResult visitQualifiedName(FEEL_1_1Parser.QualifiedNameContext ctx) {
        List<NameRefContext> parts = ctx.nameRef();
        DirectCompilerResult nameRef0 = visitNameRef(parts.get(0));
        Type typeCursor = nameRef0.resultType;
        Expression exprCursor = nameRef0.expression;
        for (NameRefContext acc : parts.subList(1, parts.size())) {
            String accText = ParserHelper.getOriginalText(acc);
            if (typeCursor instanceof CompositeType) {
                CompositeType compositeType = (CompositeType) typeCursor;

                // setting next typeCursor
                typeCursor = compositeType.getFields().get(accText);

                // setting next exprCursor
                if (compositeType instanceof MapBackedType) {
                    CastExpr castExpr = new CastExpr(JavaParser.parseType(Map.class.getCanonicalName()), exprCursor);
                    EnclosedExpr enclosedExpr = new EnclosedExpr(castExpr);
                    MethodCallExpr getExpr = new MethodCallExpr(enclosedExpr, "get");
                    getExpr.addArgument(new StringLiteralExpr(accText));
                    exprCursor = getExpr;
                } else if (compositeType instanceof JavaBackedType) {
                    JavaBackedType javaBackedType = (JavaBackedType) compositeType;
                    Method accessor = EvalHelper.getGenericAccessor(javaBackedType.getWrapped(), accText);
                    CastExpr castExpr = new CastExpr(JavaParser.parseType(javaBackedType.getWrapped().getCanonicalName()), exprCursor);
                    EnclosedExpr enclosedExpr = new EnclosedExpr(castExpr);
                    exprCursor = new MethodCallExpr(enclosedExpr, accessor.getName());
                } else {
                    throw new UnsupportedOperationException("A Composite type is either MapBacked or JavaBAcked");
                }
            } else {
                throw new UnsupportedOperationException("Trying to access" + accText + " but typeCursor not a CompositeType " + typeCursor);
            }
        }
        return DirectCompilerResult.of(exprCursor, typeCursor);
    }

    @Override
    public DirectCompilerResult visitIfExpression(FEEL_1_1Parser.IfExpressionContext ctx) {
        DirectCompilerResult c = visit( ctx.c );
        DirectCompilerResult t = visit( ctx.t );
        DirectCompilerResult e = visit( ctx.e );
        
//        String snippet = "(e1 instanceof Boolean) ? ((boolean) e1 ? e2 : e3 ) : "+CompiledFEELUtils.class.getCanonicalName()+".conditionWasNotBoolean(feelExprCtx)";
//        
//        Expression parsed = JavaParser.parseExpression(snippet);
//        for ( NameExpr ne : parsed.getChildNodesByType(NameExpr.class) ) {
//            switch (ne.getNameAsString()) {
//                case "e1":
//                    ne.replace(ne, c.expression);
//                    break;
//                case "e2":
//                    ne.replace(ne, t.expression);
//                    break;
//                case "e3":
//                    ne.replace(ne, e.expression);
//                    break;
//            }
//        }
//        return DirectCompilerResult.of(parsed, BuiltInType.UNKNOWN);
        
        Expression errorExpression = JavaParser.parseExpression(CompiledFEELUtils.class.getCanonicalName()+".conditionWasNotBoolean(feelExprCtx)");
        MethodCallExpr castC = new MethodCallExpr(new ClassExpr(JavaParser.parseType(Boolean.class.getSimpleName())), "cast");
        castC.addArgument(new EnclosedExpr(c.expression));
        ConditionalExpr safeInternal = new ConditionalExpr(castC, new EnclosedExpr(t.expression), new EnclosedExpr(e.expression));
        MethodCallExpr instanceOfBoolean = new MethodCallExpr(new ClassExpr(JavaParser.parseType(Boolean.class.getSimpleName())), "isInstance");
        instanceOfBoolean.addArgument(new EnclosedExpr(c.expression));
        ConditionalExpr result = new ConditionalExpr(instanceOfBoolean, safeInternal, errorExpression);
        return DirectCompilerResult.of(result, BuiltInType.UNKNOWN, DirectCompilerResult.unifyFDs(c, t, e));
    }

//    @Override
//    public DirectCompilerResult visitQuantExprSome(FEEL_1_1Parser.QuantExprSomeContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitQuantExprEvery(FEEL_1_1Parser.QuantExprEveryContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }

    // this is never directly covered in test because qualifiedName visitor "ingest" it directly.
    @Override
    public DirectCompilerResult visitNameRef(FEEL_1_1Parser.NameRefContext ctx) {
        String nameRefText = ParserHelper.getOriginalText(ctx);
        Type type = scopeHelper.resolveType(nameRefText).orElse(BuiltInType.UNKNOWN);
        NameExpr scope = new NameExpr("feelExprCtx");
        MethodCallExpr getFromScope = new MethodCallExpr(scope, "getValue");
        getFromScope.addArgument(new StringLiteralExpr(nameRefText));
        return DirectCompilerResult.of(getFromScope, type);
    }

//    @Override
//    public DirectCompilerResult visitPositionalParameters(FEEL_1_1Parser.PositionalParametersContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitNamedParameter(FEEL_1_1Parser.NamedParameterContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitNamedParameters(FEEL_1_1Parser.NamedParametersContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitParametersEmpty(FEEL_1_1Parser.ParametersEmptyContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitParametersNamed(FEEL_1_1Parser.ParametersNamedContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitParametersPositional(FEEL_1_1Parser.ParametersPositionalContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitPrimaryName(FEEL_1_1Parser.PrimaryNameContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    private String getFunctionName(DirectCompilerResult name) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    private DirectCompilerResult buildFunctionCall(ParserRuleContext ctx, DirectCompilerResult name, ListNode params) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    private DirectCompilerResult buildNotCall(ParserRuleContext ctx, DirectCompilerResult name, ListNode params) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitType(FEEL_1_1Parser.TypeContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitRelExpressionInstanceOf(FEEL_1_1Parser.RelExpressionInstanceOfContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
//
//    @Override
//    public DirectCompilerResult visitFilterPathExpression(FEEL_1_1Parser.FilterPathExpressionContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }

    @Override
    public DirectCompilerResult visitExpressionTextual(FEEL_1_1Parser.ExpressionTextualContext ctx) {
        DirectCompilerResult expr = visit( ctx.expr );
        return expr;
    }

//    @Override
//    public DirectCompilerResult visitUenpmPrimary(FEEL_1_1Parser.UenpmPrimaryContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }

    @Override
    public DirectCompilerResult visitCompilation_unit(FEEL_1_1Parser.Compilation_unitContext ctx) {
        return visit( ctx.expression() );
    }

//    @Override
//    public DirectCompilerResult visitNegatedUnaryTests(FEEL_1_1Parser.NegatedUnaryTestsContext ctx) {
//        throw new UnsupportedOperationException("TODO"); // TODO
//    }
}
