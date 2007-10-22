package org.drools.brms.client.modeldriven.testing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This contains lists of rules to include in the scenario (or exclude, as the case may be !).
 * This will be used to filter the rule engines behaviour under test.
 * @author Michael Neale
 */
public class ScenarioRules implements Serializable {

	/**
	 * the rules to include or exclude
	 */
	public String[] rules = new String[0];

	/**
	 * true if only the rules in the list should be allowed to fire. Otherwise
	 * it is exclusive (ie all rules can fire BUT the ones in the list).
	 */
	public boolean inclusive = false;

	/**
	 * @gwt.typeArgs <java.lang.String, java.lang.Integer>
	 */
	public Map firingCounts = new HashMap();

	public ScenarioRules() {}
	public ScenarioRules(String[] ruleList, boolean inclusive) {
		this.rules = ruleList;
		this.inclusive = inclusive;
	}

}
