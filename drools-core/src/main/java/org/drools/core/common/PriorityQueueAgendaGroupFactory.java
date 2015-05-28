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

package org.drools.core.common;

import org.drools.core.impl.InternalKnowledgeBase;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class PriorityQueueAgendaGroupFactory implements AgendaGroupFactory, Externalizable {

    private static final AgendaGroupFactory INSTANCE = new PriorityQueueAgendaGroupFactory();

    public static AgendaGroupFactory getInstance() {
        return INSTANCE;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException { }

    public void writeExternal(ObjectOutput out) throws IOException { }

    public InternalAgendaGroup createAgendaGroup(String name, InternalKnowledgeBase kBase) {
        return new AgendaGroupQueueImpl( name, kBase );
    }
}
