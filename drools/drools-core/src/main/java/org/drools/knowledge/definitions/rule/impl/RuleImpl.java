package org.drools.knowledge.definitions.rule.impl;

import org.drools.rule.Rule;

public class RuleImpl implements org.drools.knowledge.definitions.rule.Rule {
	private Rule rule;
	
	public RuleImpl(Rule rule) {
		this.rule = rule;
	}

	public String getName() {
		return this.rule.getName();
	}
	
	public String getPackageName() {
		return this.rule.getPackage();
	}
}
