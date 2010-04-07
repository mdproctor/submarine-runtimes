package org.drools.base;

import java.util.ArrayList;
import java.util.List;

import org.drools.rule.Query;
import org.drools.rule.Variable;

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

public final class DroolsQuery extends Arguments {
    private final String         name;
    private QueryResultCollector resultsCollector;
    private Query                query;

    public DroolsQuery(final String name,
                       QueryResultCollector resultsCollector) {
        this( name,
              null,
              resultsCollector );
    }

    public DroolsQuery(final String name,
                       final Object[] params,
                       QueryResultCollector resultsCollector) {
        super( params );
        this.name = name;
        this.resultsCollector = resultsCollector;
    }

    public String getName() {
        return this.name;
    }

    public void setQuery(Query query) {
        // this is set later, as we don't yet know which Query will match this DroolsQuery propagation
        this.query = query;
    }

    public Query getQuery() {
        return this.query;
    }

    public QueryResultCollector getQueryResultCollector() {
        return this.resultsCollector;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( !super.equals( obj ) ) return false;
        if ( getClass() != obj.getClass() ) return false;
        DroolsQuery other = (DroolsQuery) obj;
        if ( name == null ) {
            if ( other.name != null ) return false;
        } else if ( !name.equals( other.name ) ) return false;
        return true;
    }

}