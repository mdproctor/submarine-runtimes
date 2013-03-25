/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.guvnor.models.commons.shared.rule;

/**
 * For setting a field on a bound LHS variable or a global. If setting a field
 * on a fact bound variable, this will NOT notify the engine of any changes
 * (unless done outside of the engine).
 */
public class ActionSetField extends ActionFieldList {

    public ActionSetField( final String var ) {
        this.variable = var;
    }

    public ActionSetField() {
    }

    private String variable;

    public String getVariable() {
        return variable;
    }

    public void setVariable( String variable ) {
        this.variable = variable;
    }
}
