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

package org.drools.core.runtime.rule.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.QueryResultsRow;

public class FlatQueryResultRow
    implements
    QueryResultsRow {

    private Map<String, FactHandle> idFacthandleMap;
    private Map<String, Object> idResultMap;

    public FlatQueryResultRow(Map<String, FactHandle> idFactHandleMap,
                              Map<String, Object> idResultMap) {
        this.idFacthandleMap = idFactHandleMap;
        this.idResultMap = idResultMap;
    }

    @Override
    public Object get(String identifier) {
        return this.idResultMap.get( identifier );
    }

    @Override
    public FactHandle getFactHandle(String identifier) {
        return this.idFacthandleMap.get( identifier );
    }

    public List<String> getIdentifiers() {
        return new ArrayList<String>(this.idFacthandleMap.keySet());
    }

}
