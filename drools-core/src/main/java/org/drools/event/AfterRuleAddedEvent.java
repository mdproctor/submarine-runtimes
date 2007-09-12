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

import org.drools.rule.Package;
import org.drools.rule.Rule;

public class AfterRuleAddedEvent extends RuleBaseEvent {

    private static final long serialVersionUID = 400L;

    public AfterRuleAddedEvent(final Package pkg, final Rule rule) {
        super( pkg, rule );
    }
    
    public Object getSource() {
        return super.getRule();
    }
    
    public String toString() {
        return "[AfterRuleAdded: package=" + getPackage() + " rule=" + getRule() + "]";
    }
}