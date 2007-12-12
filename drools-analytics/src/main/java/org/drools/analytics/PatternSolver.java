package org.drools.analytics;

import org.drools.analytics.components.OperatorDescr;
import org.drools.analytics.components.Pattern;

/**
 * Takes a list of Constraints and makes possibilities from them.
 * 
 * @author Toni Rikkola
 */
class PatternSolver extends Solver {

	private Pattern pattern;

	public PatternSolver(Pattern pattern) {
		super(OperatorDescr.Type.OR);
		this.pattern = pattern;
	}

	public Pattern getPattern() {
		return pattern;
	}
}
