package org.drools.workflow.core.impl;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.drools.knowledge.definitions.process.Node;
import org.drools.knowledge.definitions.process.NodeContainer;
import org.drools.knowledge.definitions.process.WorkflowProcess;
import org.drools.process.core.impl.ProcessImpl;

/**
 * Default implementation of a RuleFlow process.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class WorkflowProcessImpl extends ProcessImpl implements WorkflowProcess, org.drools.workflow.core.NodeContainer {

    private static final long serialVersionUID = 400L;

    private org.drools.workflow.core.NodeContainer nodeContainer;
    public WorkflowProcessImpl() {
        nodeContainer = (org.drools.workflow.core.NodeContainer) createNodeContainer();
    }
    
    protected NodeContainer createNodeContainer() {
        return new NodeContainerImpl();
    }
    
    public Node[] getNodes() {
        return nodeContainer.getNodes();
    }

    public Node getNode(final long id) {
        return nodeContainer.getNode(id);
    }
    
    public Node internalGetNode(long id) {
    	return getNode(id);
    }

    public void removeNode(final Node node) {
        nodeContainer.removeNode(node);
        ((org.drools.workflow.core.Node) node).setNodeContainer(null);
    }

    public void addNode(final Node node) {
        nodeContainer.addNode(node);
        ((org.drools.workflow.core.Node) node).setNodeContainer(this);
    }

}
