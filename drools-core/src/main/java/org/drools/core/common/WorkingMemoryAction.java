/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
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

import org.drools.core.marshalling.impl.MarshallerWriteContext;
import org.drools.core.marshalling.impl.ProtobufMessages;
import org.drools.core.phreak.PropagationEntry;

import java.io.IOException;

public interface WorkingMemoryAction extends PropagationEntry {
    short WorkingMemoryReteAssertAction  = 1;
    short DeactivateCallback             = 2;
    short PropagateAction                = 3;
    short LogicalRetractCallback         = 4;
    short WorkingMemoryReteExpireAction  = 5;
    short SignalProcessInstanceAction    = 6;
    short SignalAction                   = 7;
    short WorkingMemoryBehahviourRetract = 8;

    ProtobufMessages.ActionQueue.Action serialize(MarshallerWriteContext context) throws IOException;
}
