/*
 * Copyright 2011 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.drools.examples.workitemconsequence;

import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.io.ResourceFactory;
import org.kie.io.ResourceType;
import org.kie.runtime.StatefulKnowledgeSession;
import org.kie.runtime.process.WorkItem;
import org.kie.runtime.process.WorkItemHandler;
import org.kie.runtime.process.WorkItemManager;

/**
 * A simple demonstration of using a Work Item handler as an action
 */
public class WorkItemConsequenceExample2 {

    public static void main(final String[] args) {

        final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add( ResourceFactory.newClassPathResource( "WorkItemConsequence2.drl",
                                                            WorkItemConsequenceExample2.class ),
                                                            ResourceType.DRL );

        final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );

        if ( kbuilder.hasErrors() ) {
            throw new RuntimeException( "Compilation error.\n" + kbuilder.getErrors().toString() );
        }

        final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
        ksession.getWorkItemManager().registerWorkItemHandler( "GreetingWorkItemHandler",
                                                               new GreetingWorkItemHandler() );

        //        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "log/WorkItemConsequence2.log");

        ksession.insert( new Greeting( "Michael" ) );

        ksession.fireAllRules();

        //        logger.close();

        ksession.dispose(); // Stateful rule session must always be disposed when finished

    }

    public static class Greeting {

        private String name;
        private String response;

        public Greeting(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public String getResponse() {
            return this.response;
        }

    }

    public static class GreetingWorkItemHandler
        implements
        WorkItemHandler {

        public void executeWorkItem(WorkItem workItem,
                                    WorkItemManager manager) {
            String name = (String) workItem.getParameter( "name" );
            String response = "Hello, " + name;
            workItem.getResults().put( "response",
                                       response );
        }

        public void abortWorkItem(WorkItem workItem,
                                  WorkItemManager manager) {
        }

    }

}
