package org.drools.semantics.java.builder;

import org.drools.lang.descr.RuleDescr;

public interface ConsequenceBuilder {

    public abstract void buildConsequence(final BuildContext context,
                                          final BuildUtils utils,
                                          final RuleDescr ruleDescr);

}