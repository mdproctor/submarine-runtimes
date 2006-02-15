package org.drools.leaps;

/*
 * Copyright 2006 Alexander Bagerman
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.drools.rule.Rule;
import org.drools.spi.PropagationContext;

/**
 * Tuple/rule wrapper for such combinations were they are pending due
 * to NOT conditions being satisfied by "blocking" facts. As blocking
 * facts retracted counter goes down and when the last one removed 
 * AgendaItem submitted to Agenda for firing (if it's still valid)
 * 
 * @author Alexander Bagerman
 * 
 */
class PendingActivation {
	private LeapsTuple tuple;

	private int blockingFactsCount;

	private PropagationContext context;

	private Rule rule;

	protected PendingActivation(LeapsTuple tuple, int blockingFactsCount,
			PropagationContext context, Rule rule) {
		this.tuple = tuple;
		this.blockingFactsCount = blockingFactsCount;
	}

	protected boolean containsBlockingFacts() {
		return this.blockingFactsCount > 0;
	}

	protected void decrementBlockingFactsCount() {
		this.blockingFactsCount--;
	}

	protected PropagationContext getContext() {
		return this.context;
	}

	protected Rule getRule() {
		return this.rule;
	}

	protected LeapsTuple getTuple() {
		return this.tuple;
	}
}
