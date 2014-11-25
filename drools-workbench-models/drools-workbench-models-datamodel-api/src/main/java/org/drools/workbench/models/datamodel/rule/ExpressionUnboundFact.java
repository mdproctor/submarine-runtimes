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

package org.drools.workbench.models.datamodel.rule;

public class ExpressionUnboundFact extends ExpressionPart {

    private FactPattern fact;

    public ExpressionUnboundFact() {
    }

    public ExpressionUnboundFact( FactPattern fact ) {
        this(fact, fact.getFactType());
    }

    public ExpressionUnboundFact( FactPattern fact, String classType ) {
        super( fact.getFactType(), classType, fact.getFactType() );
        this.fact = fact;
    }

    public FactPattern getFact() {
        return fact;
    }

    @Override
    public void accept( ExpressionVisitor visitor ) {
        visitor.visit( this );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ExpressionUnboundFact that = (ExpressionUnboundFact) o;

        if (fact != null ? !fact.equals(that.fact) : that.fact != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (fact != null ? fact.hashCode() : 0);
        return result;
    }
}
