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

package org.drools.model.impl;

import org.drools.model.From;
import org.drools.model.Variable;
import org.drools.model.functions.Function1;

public class FromImpl<T> implements From<T> {

    private final Variable<T> variable;
    private final Function1<T, ?> provider;

    public FromImpl( Variable<T> variable, Function1<T, ?> provider ) {
        this.variable = variable;
        this.provider = provider;
    }

    @Override
    public Variable<T> getVariable() {
        return variable;
    }

    @Override
    public Function1<T, ?> getProvider() {
        return provider;
    }
}
