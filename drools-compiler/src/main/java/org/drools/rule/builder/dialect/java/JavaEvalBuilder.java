/*
 * Copyright 2006 JBoss Inc
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

package org.drools.rule.builder.dialect.java;

import java.util.List;
import java.util.Map;

import org.drools.lang.descr.BaseDescr;
import org.drools.lang.descr.EvalDescr;
import org.drools.rule.ConditionalElement;
import org.drools.rule.Declaration;
import org.drools.rule.EvalCondition;
import org.drools.rule.builder.ConditionalElementBuilder;
import org.drools.rule.builder.RuleBuildContext;

/**
 * @author etirelli
 *
 */
public class JavaEvalBuilder extends AbstractJavaBuilder
    implements
    ConditionalElementBuilder {

    /**
     * Builds and returns an Eval Conditional Element
     * 
     * @param context The current build context
     * @param utils The current build utils instance
     * @param patternBuilder not used by EvalBuilder
     * @param descr The Eval Descriptor to build the eval conditional element from
     * 
     * @return the Eval Conditional Element
     */
    public ConditionalElement build(final RuleBuildContext context,
                                    final BaseDescr descr) {
        // it must be an EvalDescr
        final EvalDescr evalDescr = (EvalDescr) descr;

        final String className = "eval" + context.getNextId();

        evalDescr.setClassMethodName( className );

        final List[] usedIdentifiers = context.getDialect().getExpressionIdentifiers( context,
                                                                                      evalDescr,
                                                                                      evalDescr.getContent() );

        final Declaration[] declarations = new Declaration[usedIdentifiers[0].size()];
        for ( int i = 0, size = usedIdentifiers[0].size(); i < size; i++ ) {
            declarations[i] = context.getDeclarationResolver().getDeclaration( (String) usedIdentifiers[0].get( i ) );
        }

        final EvalCondition eval = new EvalCondition( declarations );

        final Map map = createVariableContext( className,
                                         (String) evalDescr.getContent(),
                                         context,
                                         declarations,
                                         null,
                                         (String[]) usedIdentifiers[1].toArray( new String[usedIdentifiers[1].size()] ) );

        generatTemplates( "evalMethod",
                          "evalInvoker",
                          context,
                          className,
                          map,
                          eval,
                          descr );

        return eval;
    }
}
