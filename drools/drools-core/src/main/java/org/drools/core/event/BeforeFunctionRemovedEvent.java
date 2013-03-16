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

package org.drools.core.event;

import org.drools.RuleBase;
import org.drools.core.rule.Package;

public class BeforeFunctionRemovedEvent extends RuleBaseEvent {

    private static final long serialVersionUID = 510l;

    public BeforeFunctionRemovedEvent(final RuleBase ruleBase,
                                      final Package pkg,
                                      final String function) {
        super( ruleBase,
               pkg,
               function );
    }

    public Object getSource() {
        return super.getRule();
    }

    public String toString() {
        return "[BeforeFunctionRemoved: package=" + getPackage() + " function=" + getFunction() + "]";
    }
}
