package org.drools.modelcompiler.builder.generator.visitor.accumulate;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.drools.compiler.compiler.Dialect;
import org.drools.compiler.compiler.DialectCompiletimeRegistry;
import org.drools.compiler.lang.descr.AccumulateDescr;
import org.drools.compiler.lang.descr.PatternDescr;
import org.drools.compiler.lang.descr.RuleDescr;
import org.drools.compiler.rule.builder.PatternBuilder;
import org.drools.compiler.rule.builder.RuleBuildContext;
import org.drools.compiler.rule.builder.dialect.java.JavaAccumulateBuilder;
import org.drools.compiler.rule.builder.dialect.java.JavaRuleClassBuilder;
import org.drools.core.definitions.InternalKnowledgePackage;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.rule.Declaration;
import org.drools.core.rule.Pattern;
import org.drools.core.rule.RuleConditionElement;
import org.drools.core.spi.DeclarationScopeResolver;
import org.drools.javaparser.JavaParser;
import org.drools.javaparser.ast.CompilationUnit;
import org.drools.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.drools.javaparser.ast.expr.ClassExpr;
import org.drools.javaparser.ast.expr.MethodCallExpr;
import org.drools.javaparser.ast.expr.NameExpr;
import org.drools.modelcompiler.builder.GeneratedClassWithPackage;
import org.drools.modelcompiler.builder.PackageModel;
import org.drools.modelcompiler.builder.generator.RuleContext;

import static org.drools.javaparser.ast.NodeList.nodeList;
import static org.drools.modelcompiler.builder.generator.DrlxParseUtil.toVar;
import static org.drools.modelcompiler.builder.generator.DslMethodNames.ACC_FUNCTION_CALL;
import static org.drools.modelcompiler.builder.generator.DslMethodNames.ACC_WITH_EXTERNAL_DECLRS_CALL;
import static org.drools.modelcompiler.builder.generator.DslMethodNames.BIND_AS_CALL;

public class LegacyAccumulate {

    private final AccumulateDescr descr;
    private final PatternDescr basePattern;
    private final RuleBuildContext ruleBuildContext;
    private final JavaAccumulateBuilder javaAccumulateBuilder = new JavaAccumulateBuilder();
    private final RuleContext context;
    private final PackageModel packageModel;
    private final Set<String> externalDeclrs;

    public LegacyAccumulate(RuleContext context, AccumulateDescr descr, PatternDescr basePattern, Set<String> externalDeclrs) {
        this.descr = descr;
        this.basePattern = basePattern;
        this.context = context;
        this.packageModel = context.getPackageModel();
        this.externalDeclrs = externalDeclrs;

        final DialectCompiletimeRegistry dialectCompiletimeRegistry = packageModel.getDialectCompiletimeRegistry();
        final Dialect defaultDialect = dialectCompiletimeRegistry.getDialect("java");
        final InternalKnowledgePackage pkg = packageModel.getPkg();
        final RuleDescr ruleDescr = context.getRuleDescr();

        ruleBuildContext = new RuleBuildContext(context.getKbuilder(), ruleDescr, dialectCompiletimeRegistry, pkg, defaultDialect);
        ruleBuildContext.setDeclarationResolver( new DelegateDeclarationScopeResolver(ruleBuildContext.getDeclarationResolver(), context, externalDeclrs ) );
    }

    public void build() {

        new PatternBuilder().build(ruleBuildContext, basePattern);

        final Set<String> imports = ruleBuildContext.getPkg().getImports().keySet();
        final String packageName = ruleBuildContext.getPkg().getName();

        GeneratedClassWithPackage generatedClassWithPackage = createAllAccumulateClass(imports, packageName);
        packageModel.addGeneratedAccumulateClasses(generatedClassWithPackage);

        GeneratedClassWithPackage invokerGenerated = createInvokerClass(imports, packageName);
        packageModel.addGeneratedAccumulateClasses(invokerGenerated);

        String typeWithPackage = String.format("%s.%s", packageName, invokerGenerated.getGeneratedClass().getName().asString());
        ClassExpr accFunctionName = new ClassExpr(JavaParser.parseType(typeWithPackage));
        MethodCallExpr accFunctionCall = new MethodCallExpr(null, ACC_FUNCTION_CALL, nodeList(accFunctionName));

        if (!externalDeclrs.isEmpty()) {
            accFunctionCall = new MethodCallExpr(accFunctionCall, ACC_WITH_EXTERNAL_DECLRS_CALL);
            for (String externalDeclr : externalDeclrs) {
                accFunctionCall.addArgument( toVar(externalDeclr) );
            }
        }

        NameExpr bindingVariable = new NameExpr(toVar(basePattern.getIdentifier()));
        accFunctionCall = new MethodCallExpr(accFunctionCall, BIND_AS_CALL, nodeList(bindingVariable));

        context.addExpression(accFunctionCall);
    }

    private GeneratedClassWithPackage createInvokerClass(Set<String> imports, String packageName) {
        final String invokerClass = ruleBuildContext.getInvokers().values().iterator().next();
        final CompilationUnit parsedInvokedClass = JavaParser.parse(invokerClass);

        Set<String> allImports = new HashSet<>();
        allImports.addAll(imports);
        allImports.addAll(parsedInvokedClass
                                  .getImports()
                                  .stream()
                                  .map(importDeclaration -> importDeclaration.getName().toString())
                                  .collect(Collectors.toList()));

        return new GeneratedClassWithPackage(
                (ClassOrInterfaceDeclaration) parsedInvokedClass.getType(0), packageName, allImports, Collections.emptyList()
        );
    }

    private GeneratedClassWithPackage createAllAccumulateClass(Set<String> imports, String packageName) {
        final String allAccumulatesClass = new JavaRuleClassBuilder().buildRule(ruleBuildContext);
        final CompilationUnit parsedAccumulateClass = JavaParser.parse(allAccumulatesClass);
        return new GeneratedClassWithPackage(
                (ClassOrInterfaceDeclaration) parsedAccumulateClass.getType(0), packageName, imports, Collections.emptyList()
        );
    }

    private static class DelegateDeclarationScopeResolver extends DeclarationScopeResolver {
        private final DeclarationScopeResolver delegate;
        private final RuleContext context;
        private final Set<String> externalDeclrs;

        private DelegateDeclarationScopeResolver( DeclarationScopeResolver delegate, RuleContext context, Set<String> externalDeclrs ) {
            this.delegate = delegate;
            this.context = context;
            this.externalDeclrs = externalDeclrs;
        }

        @Override
        public void setRule( RuleImpl rule ) {
            delegate.setRule( rule );
        }

        @Override
        public RuleConditionElement peekBuildStack() {
            return delegate.peekBuildStack();
        }

        @Override
        public RuleConditionElement popBuildStack() {
            return delegate.popBuildStack();
        }

        @Override
        public void pushOnBuildStack( RuleConditionElement element ) {
            delegate.pushOnBuildStack( element );
        }

        @Override
        public Declaration getDeclaration( String identifier ) {
            return delegate.getDeclaration( identifier );
        }

        @Override
        public Class<?> resolveVarType( String identifier ) {
            return delegate.resolveVarType( identifier );
        }

        @Override
        public String normalizeValueForUnit( String value ) {
            return delegate.normalizeValueForUnit( value );
        }

        @Override
        public boolean hasDataSource( String name ) {
            return delegate.hasDataSource( name );
        }

        @Override
        public boolean available( RuleImpl rule, String name ) {
            return delegate.available( rule, name );
        }

        @Override
        public boolean isDuplicated( RuleImpl rule, String name, String type ) {
            return delegate.isDuplicated( rule, name, type );
        }

        @Override
        public Map<String, Declaration> getDeclarations( RuleImpl rule ) {
            Map<String, Declaration> declarationMap = new HashMap<>();
            for (String externalDeclr : externalDeclrs) {
                context.getDeclarationById( externalDeclr ).ifPresent( dSpec -> declarationMap.put(externalDeclr, dSpec.asDeclaration()) );
            }
            return declarationMap;
        }

        @Override
        public Map<String, Declaration> getDeclarations( RuleImpl rule, String consequenceName ) {
            return delegate.getDeclarations( rule, consequenceName );
        }

        @Override
        public Map<String, Class<?>> getDeclarationClasses( RuleImpl rule ) {
            return delegate.getDeclarationClasses( rule );
        }

        public static Map<String, Class<?>> getDeclarationClasses( Map<String, Declaration> declarations ) {
            return DeclarationScopeResolver.getDeclarationClasses( declarations );
        }

        @Override
        public Pattern findPatternByIndex( int index ) {
            return delegate.findPatternByIndex( index );
        }
    }
}
