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


import org.drools.core.spi.FactHandleFactory;
import org.kie.api.time.SessionClock;

public class SharedTemporalWorkingMemoryContext<T extends SessionClock>  extends SharedWorkingMemoryContext {
    protected T                                   sessionClock;
    
    public SharedTemporalWorkingMemoryContext(FactHandleFactory handleFactory, T sessionClock) {
        super( handleFactory );
        this.sessionClock = sessionClock;
    }

    public T getSessionClock() {
        return sessionClock;
    }
    
            
    
}
