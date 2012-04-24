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

package org.drools.reteoo;

import org.drools.base.DefaultKnowledgeHelperFactory;
import org.drools.base.FieldFactory;
import org.drools.base.FieldDataFactory;
import org.drools.base.KnowledgeHelperFactory;
import org.drools.common.AgendaFactory;
import org.drools.common.DefaultAgendaFactory;
import org.drools.core.util.TripleFactory;
import org.drools.core.util.TripleFactoryImpl;
import org.drools.reteoo.builder.DefaultNodeFactory;
import org.drools.reteoo.builder.NodeFactory;
import org.drools.rule.LogicTransformer;
import org.drools.spi.FactHandleFactory;

public class ReteooComponentFactory {

    private static FactHandleFactory handleFactory = new ReteooFactHandleFactory();


    public static FactHandleFactory getFactHandleFactoryService() {
         return handleFactory;
    }

    public static void setHandleFactoryProvider( FactHandleFactory provider ) {
        ReteooComponentFactory.handleFactory = provider;
    }

    public static void setDefaultHandleFactoryProvider() {
        ReteooComponentFactory.handleFactory = new ReteooFactHandleFactory();
    }




    private static NodeFactory nodeFactory = new DefaultNodeFactory();

    public static NodeFactory getNodeFactoryService() {
        return nodeFactory;
    }

    public static void setNodeFactoryProvider( NodeFactory provider ) {
        ReteooComponentFactory.nodeFactory = provider;
    }

    public static void setDefaultNodeFactoryProvider() {
        ReteooComponentFactory.nodeFactory = new DefaultNodeFactory();
    }


    private static RuleBuilderFactory ruleBuilderFactory = new ReteooRuleBuilderFactory();

    public static RuleBuilderFactory getRuleBuilderFactory() {
        return ruleBuilderFactory;
    }

    public static void setRuleBuilderProvider( RuleBuilderFactory provider ) {
        ReteooComponentFactory.ruleBuilderFactory = provider;
    }

    public static void setDefaultRuleBuilderProvider() {
        ReteooComponentFactory.ruleBuilderFactory = new ReteooRuleBuilderFactory();
    }




    private static AgendaFactory agendaFactory = new DefaultAgendaFactory();

    public static AgendaFactory getAgendaFactory() {
         return agendaFactory;
    }

    public static void setAgendaFactory( AgendaFactory provider ) {
        ReteooComponentFactory.agendaFactory = provider;
    }

    public static void setDefaultAgendaFactory() {
        ReteooComponentFactory.agendaFactory = new DefaultAgendaFactory();
    }



    private static FieldDataFactory fieldFactory = FieldFactory.getInstance();

    public static FieldDataFactory getFieldFactory() {
        return fieldFactory;
    }

    public static void setFieldDataFactory( FieldDataFactory provider ) {
        ReteooComponentFactory.fieldFactory = provider;
    }

    public static void setDefaultFieldFactory() {
        ReteooComponentFactory.fieldFactory = FieldFactory.getInstance();
    }




    private static TripleFactory tripleFactory = new TripleFactoryImpl();

    public static synchronized TripleFactory getTripleFactory() {
         return tripleFactory;
    }

    public static void setTripleFactory( TripleFactory provider ) {
        ReteooComponentFactory.tripleFactory = provider;
    }

    public static void setDefaultTripleFactory() {
        ReteooComponentFactory.tripleFactory = new TripleFactoryImpl();
    }



    private static KnowledgeHelperFactory knowledgeHelperFactory = new DefaultKnowledgeHelperFactory();

    public static KnowledgeHelperFactory getKnowledgeHelperFactory() {
         return knowledgeHelperFactory;
    }

    public static void setKnowledgeHelperFactory( KnowledgeHelperFactory provider ) {
        ReteooComponentFactory.knowledgeHelperFactory = provider;
    }

    public static void setDefaultKnowledgeHelperFactory() {
        ReteooComponentFactory.knowledgeHelperFactory = new DefaultKnowledgeHelperFactory();
    }





    private static LogicTransformer logicTransformer = LogicTransformer.getInstance();

    public static LogicTransformer getLogicTransformer() {
        return logicTransformer;
    }

    public static void setLogicTransformer( LogicTransformer provider ) {
        ReteooComponentFactory.logicTransformer = provider;
    }

    public static void setDefaultLogicTransformer() {
        ReteooComponentFactory.logicTransformer = LogicTransformer.getInstance();
    }




}
