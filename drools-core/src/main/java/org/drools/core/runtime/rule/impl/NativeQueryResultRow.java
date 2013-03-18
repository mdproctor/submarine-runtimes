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

package org.drools.core.runtime.rule.impl;

import org.drools.core.QueryResult;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.QueryResultsRow;

public class NativeQueryResultRow
    implements
    QueryResultsRow {
    private QueryResult result;

    public NativeQueryResultRow(QueryResult result) {
        this.result = result;
    }

    public Object get(String identifier) {
        return this.result.get( identifier );
    }
    
    public FactHandle getFactHandle(String identifier) {
        return this.result.getFactHandle( identifier );
    }

}
