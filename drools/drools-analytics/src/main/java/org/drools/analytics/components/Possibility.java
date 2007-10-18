package org.drools.analytics.components;

import java.util.Set;

import org.drools.analytics.report.components.Cause;

/**
 * 
 * @author Toni Rikkola
 */
public interface Possibility extends Cause {

	public Set<Cause> getItems();
	
	public int getAmountOfItems();
}
