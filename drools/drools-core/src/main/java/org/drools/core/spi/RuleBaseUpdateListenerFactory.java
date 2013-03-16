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

package org.drools.core.spi;

import org.drools.core.StatefulSession;
import org.drools.core.util.ClassUtils;

/** 
 * Factory class that will return a RuleBaseUpdateListener based on the provided string className
 */
public class RuleBaseUpdateListenerFactory {
    
    public static RuleBaseUpdateListener createListener(String className, StatefulSession session) {
        try {
            RuleBaseUpdateListener listener = ( RuleBaseUpdateListener ) ClassUtils.instantiateObject( className );
            listener.setSession( session );
            
            return listener;
        } catch ( Throwable e ) {
            throw new RuntimeException("Unable to instantiate RuleBaseUpdateListener '" + className + "'", e );
        }
    }

}
