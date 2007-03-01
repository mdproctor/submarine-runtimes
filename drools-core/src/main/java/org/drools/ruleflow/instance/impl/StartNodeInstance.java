package org.drools.ruleflow.instance.impl;
/*
 * Copyright 2005 JBoss Inc
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

import org.drools.ruleflow.core.IStartNode;
import org.drools.ruleflow.instance.IRuleFlowNodeInstance;

/**
 * Runtime counterpart of a start node.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class StartNodeInstance extends RuleFlowNodeInstance {

	public void trigger(IRuleFlowNodeInstance from) {
		triggerCompleted();
	}
	
	public IStartNode getStartNode() {
		return (IStartNode) getNode();
	}

	public void triggerCompleted() {
		getProcessInstance().getNodeInstance(getStartNode().getTo().getTo()).trigger(this);
	}
}
