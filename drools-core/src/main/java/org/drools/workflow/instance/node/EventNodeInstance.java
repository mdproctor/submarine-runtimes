package org.drools.workflow.instance.node;

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

import org.drools.process.core.context.variable.VariableScope;
import org.drools.process.core.event.EventTransformer;
import org.drools.process.instance.context.variable.VariableScopeInstance;
import org.drools.workflow.core.Node;
import org.drools.workflow.core.node.EventNode;
import org.drools.workflow.instance.NodeInstance;
import org.drools.workflow.instance.impl.NodeInstanceImpl;

/**
 * Runtime counterpart of an event node.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class EventNodeInstance extends NodeInstanceImpl implements EventNodeInstanceInterface {

    private static final long serialVersionUID = 400L;

    public void triggerEvent(String type, Object event) {
    	String variableName = getEventNode().getVariableName();
    	if (variableName != null) {
    		VariableScopeInstance variableScopeInstance = (VariableScopeInstance)
    			resolveContextInstance(VariableScope.VARIABLE_SCOPE, variableName);
    		if (variableScopeInstance == null) {
    			throw new IllegalArgumentException(
					"Could not find variable for event node: " + variableName);
    		}
    		EventTransformer transformer = getEventNode().getEventTransformer();
    		if (transformer != null) {
    			event = transformer.transformEvent(event);
    		}
    		variableScopeInstance.setVariable(variableName, event);
    	}
    	trigger(null, null);
    }
    
    public void internalTrigger(final NodeInstance from, String type) {
        if (type != null) {
            throw new IllegalArgumentException(
                "An event node does not accept incoming connections!");
        }
        if (from != null) {
            throw new IllegalArgumentException(
                "An event can only be triggered by its parent!");
        }
        triggerCompleted();
    }
    
    public EventNode getEventNode() {
        return (EventNode) getNode();
    }

    public void triggerCompleted() {
        triggerCompleted(Node.CONNECTION_DEFAULT_TYPE, true);
    }
    
}
