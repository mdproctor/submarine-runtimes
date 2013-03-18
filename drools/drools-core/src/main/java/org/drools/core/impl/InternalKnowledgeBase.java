package org.drools.core.impl;

import org.drools.core.RuleBase;
import org.kie.KieBase;
import org.kie.internal.KnowledgeBase;

public interface InternalKnowledgeBase extends KnowledgeBase, KieBase {

    RuleBase getRuleBase();

}
