package org.drools.workflow.instance.impl.factory;

import org.drools.knowledge.definitions.process.Node;
import org.drools.process.instance.NodeInstance;
import org.drools.process.instance.NodeInstanceContainer;
import org.drools.process.instance.WorkflowProcessInstance;
import org.drools.workflow.instance.impl.NodeInstanceFactory;
import org.drools.workflow.instance.impl.NodeInstanceImpl;

public class CreateNewNodeFactory implements NodeInstanceFactory {
    
    public final Class<? extends NodeInstance> cls;
    
    public CreateNewNodeFactory(Class<? extends NodeInstance> cls){
        this.cls = cls;
    }
    
    public NodeInstance getNodeInstance(Node node, WorkflowProcessInstance processInstance, NodeInstanceContainer nodeInstanceContainer) {     
        try {
            NodeInstanceImpl nodeInstance = (NodeInstanceImpl) this.cls.newInstance();
            nodeInstance.setNodeId(node.getId());
            nodeInstance.setNodeInstanceContainer(nodeInstanceContainer);
            nodeInstance.setProcessInstance(processInstance);
            return nodeInstance;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException("Unable to instantiate node: '"
                + this.cls.getName() + "':" + e.getMessage());
        }
	}

}
