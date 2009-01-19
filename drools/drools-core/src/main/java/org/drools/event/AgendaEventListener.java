package org.drools.event;

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

import java.util.EventListener;

import org.drools.WorkingMemory;
import org.drools.event.rule.ActivationCancelledCause;

public interface AgendaEventListener
    extends
    EventListener {
    void activationCreated(ActivationCreatedEvent event,
                           WorkingMemory workingMemory);

    void activationCancelled(ActivationCancelledEvent event,
                             WorkingMemory workingMemory);

    void beforeActivationFired(BeforeActivationFiredEvent event,
                               WorkingMemory workingMemory);

    void afterActivationFired(AfterActivationFiredEvent event,
                              WorkingMemory workingMemory);

    void agendaGroupPopped(AgendaGroupPoppedEvent event,
                           WorkingMemory workingMemory);

    void agendaGroupPushed(AgendaGroupPushedEvent event,
                           WorkingMemory workingMemory);
}