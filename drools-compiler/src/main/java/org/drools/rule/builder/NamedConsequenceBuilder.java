package org.drools.rule.builder;

import org.drools.compiler.lang.descr.BaseDescr;
import org.drools.compiler.lang.descr.NamedConsequenceDescr;
import org.drools.rule.NamedConsequence;
import org.drools.rule.Pattern;

public class NamedConsequenceBuilder implements RuleConditionBuilder {

    public NamedConsequence build(RuleBuildContext context, BaseDescr descr) {
        return build( context, descr, null );
    }

    public NamedConsequence build(RuleBuildContext context, BaseDescr descr, Pattern prefixPattern) {
        NamedConsequenceDescr namedConsequence = (NamedConsequenceDescr) descr;
        return new NamedConsequence( namedConsequence.getName(), namedConsequence.isBreaking() );
    }
}
