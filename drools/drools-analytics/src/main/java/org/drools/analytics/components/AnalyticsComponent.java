package org.drools.analytics.components;

/**
 * 
 * @author Toni Rikkola
 */
public abstract class AnalyticsComponent implements
		Comparable<AnalyticsComponent> {

	protected String ruleName;
	protected int id;

	protected AnalyticsComponent parent;
	protected int orderNumber; // Order number of this instance under parent.

	public abstract AnalyticsComponentType getComponentType();

	public int compareTo(AnalyticsComponent o) {
		if (id == o.getId()) {
			return 0;
		}

		return (id > o.getId() ? 1 : -1);
	}

	public AnalyticsComponent(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public AnalyticsComponent getParent() {
		return parent;
	}

	public void setParent(AnalyticsComponent parent) {
		this.parent = parent;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
}