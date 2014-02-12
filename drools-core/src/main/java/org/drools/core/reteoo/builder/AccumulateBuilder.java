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

package org.drools.core.reteoo.builder;

import java.util.ArrayList;
import java.util.List;

import org.drools.core.common.BetaConstraints;
import org.drools.core.common.TupleStartEqualsConstraint;
import org.drools.core.reteoo.AccumulateNode;
import org.drools.core.reteoo.LeftTupleSource;
import org.drools.core.reteoo.ObjectSource;
import org.drools.core.reteoo.QueryRiaFixerNode;
import org.drools.core.reteoo.RightInputAdapterNode;
import org.drools.core.rule.Accumulate;
import org.drools.core.rule.GroupElement;
import org.drools.core.rule.RuleConditionElement;
import org.drools.core.spi.AlphaNodeFieldConstraint;

public class AccumulateBuilder
        implements
        ReteooComponentBuilder {

    /**
     * @inheritDoc
     */
    public void build(final BuildContext context,
                      final BuildUtils utils,
                      final RuleConditionElement rce) {
        final Accumulate accumulate = (Accumulate) rce;
        boolean existSubNetwort = false;
        context.pushRuleComponent( accumulate );

        final List resultBetaConstraints = context.getBetaconstraints();
        final List resultAlphaConstraints = context.getAlphaConstraints();

        RuleConditionElement source = accumulate.getSource();
        if( source instanceof GroupElement ) {
            GroupElement ge = (GroupElement) source;
            if( ge.isAnd() && ge.getChildren().size() == 1 ) {
                source = (RuleConditionElement) ge.getChildren().get( 0 );
            }
        }

        // get builder for the pattern
        final ReteooComponentBuilder builder = utils.getBuilderFor( source );

        // save tuple source and current pattern offset for later if needed
        LeftTupleSource tupleSource = context.getTupleSource();
        final int currentPatternIndex = context.getCurrentPatternOffset();

        // builds the source pattern
        builder.build( context,
                       utils,
                       source );

        // if object source is null, then we need to adapt tuple source into a subnetwork
        if ( context.getObjectSource() == null ) {
            // attach right input adapter node to convert tuple source into an object source
            RightInputAdapterNode riaNode = context.getComponentFactory().getNodeFactoryService().buildRightInputNode( context.getNextId(),
                                                                                                                       context.getTupleSource(),
                                                                                                                       tupleSource,
                                                                                                                       context );

            // attach right input adapter node to convert tuple source into an object source
            context.setObjectSource( (ObjectSource) utils.attachNode( context,
                                                                      riaNode ) );

            // restore tuple source from before the start of the sub network
            context.setTupleSource( tupleSource );

            // create a tuple start equals constraint and set it in the context
            final TupleStartEqualsConstraint constraint = TupleStartEqualsConstraint.getInstance();
            final List betaConstraints = new ArrayList();
            betaConstraints.add( constraint );
            context.setBetaconstraints( betaConstraints );
            existSubNetwort = true;
        }

        NodeFactory nfactory = context.getComponentFactory().getNodeFactoryService();

        if ( !context.getKnowledgeBase().getConfiguration().isPhreakEnabled() && !context.isTupleMemoryEnabled() && existSubNetwort ) {
            // If there is a RIANode, so need to handle. This only happens with queries, so need to worry about sharing
            context.setTupleSource( (LeftTupleSource) utils.attachNode( context, nfactory.buildQueryRiaFixerNode( context.getNextId(), context.getTupleSource(), context ) ) );
        }

        final BetaConstraints resultsBinder = utils.createBetaNodeConstraint( context,
                                                                              resultBetaConstraints,
                                                                              true );
        final BetaConstraints sourceBinder = utils.createBetaNodeConstraint( context,
                                                                             context.getBetaconstraints(),
                                                                             false );

        AccumulateNode accNode = nfactory.buildAccumulateNode(context.getNextId(),
                                                              context.getTupleSource(),
                                                              context.getObjectSource(),
                                                              (AlphaNodeFieldConstraint[]) resultAlphaConstraints.toArray(new AlphaNodeFieldConstraint[resultAlphaConstraints.size()]),
                                                              sourceBinder,
                                                              resultsBinder,
                                                              accumulate,
                                                              existSubNetwort,
                                                              context);

        context.setTupleSource( (LeftTupleSource) utils.attachNode( context,
                                                                    accNode ) );

        // source pattern was bound, so nulling context
        context.setObjectSource( null );
        context.setCurrentPatternOffset( currentPatternIndex );
        context.popRuleComponent();
    }

    /**
     * @inheritDoc
     */
    public boolean requiresLeftActivation(final BuildUtils utils,
                                          final RuleConditionElement rce) {
        return true;
    }

}
