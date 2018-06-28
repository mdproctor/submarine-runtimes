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

package org.drools.model.constraints;

import org.drools.model.Variable;
import org.drools.model.functions.temporal.TemporalPredicate;
import org.drools.model.impl.ModelComponent;
import org.drools.model.view.FixedTemporalExprViewItem;

public class FixedTemporalConstraint<A> extends TemporalConstraint {

    private final long value;

    public FixedTemporalConstraint( String exprId, Variable<A> var1, long value, TemporalPredicate temporalPredicate ) {
        super(exprId, var1, temporalPredicate);
        this.value = value;
    }

    public FixedTemporalConstraint( FixedTemporalExprViewItem<A> expr ) {
        this( expr.getExprId(), expr.getFirstVariable(), expr.getValue(), expr.getTemporalPredicate() );
    }

    @Override
    public Variable[] getVariables() {
        return new Variable[] { var1 };
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean isEqualTo( ModelComponent o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        FixedTemporalConstraint<?> that = ( FixedTemporalConstraint<?> ) o;

        if ( !ModelComponent.areEqualInModel( var1, that.var1 ) ) return false;
        if ( value != that.value ) return false;
        return temporalPredicate.equals( that.temporalPredicate );
    }
}
