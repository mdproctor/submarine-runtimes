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

package org.drools.core.event.rule.impl;

import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.internal.runtime.KnowledgeRuntime;
import org.kie.api.runtime.rule.AgendaGroup;

public class AgendaGroupPoppedEventImpl extends AgendaGroupEventImpl implements AgendaGroupPoppedEvent {

    public AgendaGroupPoppedEventImpl(AgendaGroup agendaGroup, KnowledgeRuntime kruntime) {
        super( agendaGroup, kruntime );
    }

    @Override
    public String toString() {
        return "==>[AgendaGroupPoppedEvent: getAgendaGroup()=" + getAgendaGroup() + ", getKnowledgeRuntime()="
                + getKieRuntime() + "]";
    }
}
