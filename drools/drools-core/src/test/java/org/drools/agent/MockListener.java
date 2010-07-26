/**
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

package org.drools.agent;

import java.util.ArrayList;
import java.util.List;

public class MockListener
        implements
        AgentEventListener {

    public List exceptions = new ArrayList();

    public void debug(String message) {


    }

    public void exception(String message, Throwable e) {
        exceptions.add(e);
    }

    public void exception(Throwable e) {
        exceptions.add(e);

    }

    public void info(String message) {


    }

    public void setAgentName(String name) {


    }

    public void warning(String message) {


    }

    public void debug(String message,
                      Object object) {
        // TODO Auto-generated method stub

    }

    public void info(String message,
                     Object object) {
        // TODO Auto-generated method stub

    }

    public void warning(String message,
                        Object object) {
        // TODO Auto-generated method stub

    }

}
