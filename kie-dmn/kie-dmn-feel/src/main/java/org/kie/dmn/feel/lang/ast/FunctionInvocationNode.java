/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.kie.dmn.feel.lang.ast;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.lang.EvaluationContext;
import org.kie.dmn.feel.lang.Type;
import org.kie.dmn.feel.runtime.FEELFunction;
import org.kie.dmn.feel.runtime.UnaryTest;
import org.kie.dmn.feel.util.Msg;

public class FunctionInvocationNode
        extends BaseNode {


    private BaseNode name;
    private ListNode params;

    public FunctionInvocationNode(ParserRuleContext ctx, BaseNode name, ListNode params) {
        super( ctx );
        this.name = name;
        this.params = params;
    }

    public BaseNode getName() {
        return name;
    }

    public void setName(BaseNode name) {
        this.name = name;
    }

    public ListNode getParams() {
        return params;
    }

    public void setParams(ListNode params) {
        this.params = params;
    }

    @Override
    public Object evaluate(EvaluationContext ctx) {
        FEELFunction function = null;
        Object value = null;
        if ( name instanceof NameRefNode ) {
            // simple name
            value = ctx.getValue( name.getText() );
        } else {
            QualifiedNameNode qn = (QualifiedNameNode) name;
            String[] qns = qn.getPartsAsStringArray();
            value = ctx.getValue( qns );
        }
        if ( value instanceof FEELFunction ) {
            function = (FEELFunction) value;
            if ( function != null ) {
                Object[] p = params.getElements().stream().map( e -> e.evaluate( ctx ) ).toArray( Object[]::new );
                List<String> functionNameParts = (name instanceof NameRefNode) ? Arrays.asList(name.getText()) : Arrays.asList(((QualifiedNameNode) name).getPartsAsStringArray());
                Object result = invokeTheFunction(functionNameParts, function, ctx, p);
                return result;
            } else {
                ctx.notifyEvt( astEvent(Severity.ERROR, Msg.createMessage(Msg.FUNCTION_NOT_FOUND, name.getText())) );
            }
        } else if( value instanceof UnaryTest ) {
            if( params.getElements().size() == 1 ) {
                Object p = params.getElements().get( 0 ).evaluate( ctx );
                return ((UnaryTest) value).apply( ctx, p );
            } else {
                ctx.notifyEvt( astEvent(Severity.ERROR, Msg.createMessage(Msg.CAN_T_INVOKE_AN_UNARY_TEST_WITH_S_PARAMETERS_UNARY_TESTS_REQUIRE_1_SINGLE_PARAMETER, params.getElements().size()) ) );
            }
        }
        return null;
    }

    private Object invokeTheFunction(List<String> names, FEELFunction fn, EvaluationContext ctx, Object[] params) {
        if (names.size() == 1) {
            Object result = fn.invokeReflectively(ctx, params);
            return result;
        } else {
            try {
                Object newRoot = ctx.getValue(names.get(0));
                ctx.enterFrame();
                try {
                    Map<String, Object> asMap = ((Map<String, Object>) newRoot);
                    asMap.forEach(ctx::setValue);
                } catch (ClassCastException e) {
                    ctx.setRootObject(newRoot); // gracefully handle the less common scenario.
                }
                return invokeTheFunction(names.subList(1, names.size()), fn, ctx, params);
            } finally {
                ctx.exitFrame();
            }
        }
    }

    @Override
    public Type getResultType() {
        return name.getResultType();
    }

    @Override
    public ASTNode[] getChildrenNode() {
        return new ASTNode[] { name, params };
    }

}
