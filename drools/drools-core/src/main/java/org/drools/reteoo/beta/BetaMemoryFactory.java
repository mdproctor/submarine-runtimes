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

package org.drools.reteoo.beta;

import javax.naming.OperationNotSupportedException;

import org.drools.RuleBaseConfiguration;
import org.drools.base.ValueType;
import org.drools.base.evaluators.Operator;
import org.drools.common.BetaNodeBinder;
import org.drools.common.InstanceEqualsConstraint;
import org.drools.rule.VariableConstraint;
import org.drools.spi.FieldConstraint;

/**
 * MemoryFactory
 * A factory for Beta memories, both left and right
 *
 * @author <a href="mailto:tirelli@post.com">Edson Tirelli</a>
 *
 * Created: 12/02/2006
 */
public class BetaMemoryFactory {

    protected BetaMemoryFactory() {
    }

    /**
     * Creates and returns a new BetaLeftMemory instance. If indexing
     * is enabled, the returned memory will be indexed according to
     * the constraints in the given binder.
     *   
     * @param binder the binder whose constraints needs to be indexed
     * 
     * @return the newly created BetaLeftMemory 
     */
    public static BetaLeftMemory newLeftMemory(final RuleBaseConfiguration config,
                                               final BetaNodeBinder binder) {
        BetaLeftMemory memory = null;
        BetaLeftMemory innerMostMemory = null;
        final FieldConstraint[] constraints = (binder != null) ? binder.getConstraints() : null;
        if ( (constraints != null) && (config.getBooleanProperty( RuleBaseConfiguration.PROPERTY_INDEX_LEFT_BETA_MEMORY )) ) {
            for ( int i = 0; i < constraints.length; i++ ) {
                if ( constraints[i] instanceof VariableConstraint ) {
                    final VariableConstraint bvc = (VariableConstraint) constraints[i];
                    BetaLeftMemory innerMemory = null;
                    ValueType valueType = bvc.getEvaluator().getValueType();
                    if ( valueType == ValueType.BOOLEAN_TYPE ) {
                        innerMemory = new BooleanConstrainedLeftMemory( bvc.getFieldExtractor(),
                                                                        bvc.getRequiredDeclarations()[0],
                                                                        bvc.getEvaluator() );
                    } else {
                        //@todo : edson is this ok?
                        if ( bvc.getEvaluator().getOperator() == Operator.EQUAL ) {
                            innerMemory = new ObjectEqualConstrLeftMemory( bvc.getFieldExtractor(),
                                                                           bvc.getRequiredDeclarations()[0],
                                                                           bvc.getEvaluator() );
                        } else if ( bvc.getEvaluator().getOperator() == Operator.NOT_EQUAL ) {
                            innerMemory = new ObjectNotEqualConstrLeftMemory( bvc.getFieldExtractor(),
                                                                              bvc.getRequiredDeclarations()[0],
                                                                              bvc.getEvaluator() );
                        }
                    }

                    if ( innerMemory != null ) {
                        if ( innerMostMemory != null ) {
                            try {
                                innerMostMemory.setInnerMemory( innerMemory );
                                innerMostMemory = innerMemory;
                            } catch ( final OperationNotSupportedException e ) {
                                throw new RuntimeException( "BUG: Exception was not supposed to be raised",
                                                            e );
                            }
                        } else {
                            memory = innerMemory;
                            innerMostMemory = memory;
                        }
                    }
                }
            }
        }
        if ( memory == null ) {
            memory = new DefaultLeftMemory();
        }
        return memory;
    }

    /**
     * Creates and returns a new BetaRightMemory instance. If indexing
     * is enabled, the returned memory will be indexed according to
     * the constraints in the given binder.
     *   
     * @param binder the binder whose constraints needs to be indexed
     * 
     * @return the newly created BetaRightMemory 
     */
    public static BetaRightMemory newRightMemory(final RuleBaseConfiguration config,
                                                 final BetaNodeBinder binder) {
        BetaRightMemory memory = null;
        BetaRightMemory innerMostMemory = null;
        final FieldConstraint[] constraints = (binder != null) ? binder.getConstraints() : null;
        if ( (constraints != null) && (config.getBooleanProperty( RuleBaseConfiguration.PROPERTY_INDEX_RIGHT_BETA_MEMORY )) ) {
            for ( int i = 0; i < constraints.length; i++ ) {
                BetaRightMemory innerMemory = null;
                if ( constraints[i] instanceof VariableConstraint ) {
                    final VariableConstraint bvc = (VariableConstraint) constraints[i];
                    ValueType valueType = bvc.getEvaluator().getValueType();
                    if ( valueType == ValueType.BOOLEAN_TYPE ) {
                        if ( valueType == ValueType.BOOLEAN_TYPE ) {
                            innerMemory = new BooleanConstrainedRightMemory( bvc.getFieldExtractor(),
                                                                             bvc.getRequiredDeclarations()[0],
                                                                             bvc.getEvaluator() );
                        }
                    } else {
                        //@todo : edson is this ok?                        
                        if ( bvc.getEvaluator().getOperator() == Operator.EQUAL ) {
                            innerMemory = new ObjectEqualConstrRightMemory( bvc.getFieldExtractor(),
                                                                            bvc.getRequiredDeclarations()[0],
                                                                            bvc.getEvaluator() );
                        } else if ( bvc.getEvaluator().getOperator() == Operator.NOT_EQUAL ) {
                            innerMemory = new ObjectNotEqualConstrRightMemory( bvc.getFieldExtractor(),
                                                                               bvc.getRequiredDeclarations()[0],
                                                                               bvc.getEvaluator() );
                        }
                    }
                } else if ( constraints[i] instanceof InstanceEqualsConstraint ) {
                    final InstanceEqualsConstraint iec = (InstanceEqualsConstraint) constraints[i];
                    innerMemory = new InstanceEqualConstrRightMemory( iec.getOtherColumn() );
                }
                if ( innerMemory != null ) {
                    if ( innerMostMemory != null ) {
                        try {
                            innerMostMemory.setInnerMemory( innerMemory );
                            innerMostMemory = innerMemory;
                        } catch ( final OperationNotSupportedException e ) {
                            throw new RuntimeException( "BUG: Exception was not supposed to be raised",
                                                        e );
                        }
                    } else {
                        memory = innerMemory;
                        innerMostMemory = memory;
                    }
                }
            }
        }
        if ( memory == null ) {
            memory = new DefaultRightMemory();
        }
        return memory;
    }
}
