/*
 * Copyright 2010 JBoss Inc
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

package org.drools.core.util.debug;

import java.util.Stack;

import org.drools.core.common.NetworkNode;
import org.drools.reteoo.LeftInputAdapterNode;

public class LeftInputAdapterNodeVisitor extends AbstractNetworkNodeVisitor {
    
    public static final LeftInputAdapterNodeVisitor INSTANCE = new LeftInputAdapterNodeVisitor();
    
    protected LeftInputAdapterNodeVisitor() {
    }

    @Override
    protected void doVisit(NetworkNode node,
                           Stack<NetworkNode> nodeStack,
                           StatefulKnowledgeSessionInfo info) {
        LeftInputAdapterNode an = (LeftInputAdapterNode) node;
        DefaultNodeInfo ni = (DefaultNodeInfo) info.getNodeInfo( node );
        ni.setMemoryEnabled( false );
    }

}
