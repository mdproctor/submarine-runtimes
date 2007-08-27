package org.drools.analytics.result;

import java.io.Serializable;

/**
 * 
 * @author Toni Rikkola
 */
public class AnalysisError extends AnalysisMessage implements Serializable {
	private static final long serialVersionUID = -7589664008092901491L;
	
	private static int errorIndex = 0;

	public AnalysisError() {
		id = errorIndex++;
	}
}
