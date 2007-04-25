package org.drools.rule.builder;

import java.util.List;
import java.util.Map;

import org.codehaus.jfdi.interpreter.TypeResolver;
import org.drools.base.ClassFieldExtractorCache;
import org.drools.lang.descr.BaseDescr;
import org.drools.lang.descr.FunctionDescr;
import org.drools.lang.descr.RuleDescr;
import org.drools.rule.Package;
import org.drools.rule.Rule;

public interface Dialect {
    Map getBuilders();

    TypeResolver getTypeResolver();

    ClassFieldExtractorCache getClassFieldExtractorCache();    
    
    ConditionalElementBuilder getEvalBuilder();

    AccumulateBuilder getAccumulateBuilder();

    PredicateBuilder getPredicateBuilder();

    ReturnValueBuilder getReturnValueBuilder();

    ConsequenceBuilder getConsequenceBuilder();

    RuleClassBuilder getRuleClassBuilder();

    FromBuilder getFromBuilder();
    
    Object getBuilder(Class clazz);

    List[] getExpressionIdentifiers(final RuleBuildContext context,
                              final BaseDescr descr,
                              final Object content);
    
    List[] getBlockIdentifiers(final RuleBuildContext context,
                               final BaseDescr descr,
                               final String text);    

    void compileAll();

    void addRule(final RuleBuilder builder,
                          final Rule rule,
                          final RuleDescr ruleDescr);

    void addFunction(final FunctionDescr functionDescr,
                     TypeResolver typeResolver);

    List getResults();

    void init(Package pkg);

    void init(RuleDescr ruleDescr);
}
