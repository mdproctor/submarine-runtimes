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

package org.drools.common;

import org.drools.RuleBaseConfiguration;
import org.drools.base.ClassFieldAccessorCache;
import org.drools.base.ClassFieldAccessorStore;
import org.drools.base.ClassObjectType;
import org.drools.base.evaluators.EvaluatorRegistry;
import org.drools.base.evaluators.Operator;
import org.drools.core.util.AbstractHashTable.FieldIndex;
import org.drools.core.util.AbstractHashTable.Index;
import org.drools.core.util.index.IndexUtil.ConstraintType;
import org.drools.core.util.index.LeftTupleIndexHashTable;
import org.drools.core.util.index.LeftTupleList;
import org.drools.core.util.LinkedList;
import org.drools.core.util.LinkedListEntry;
import org.drools.core.util.index.RightTupleIndexHashTable;
import org.drools.core.util.index.RightTupleList;
import org.drools.reteoo.BetaMemory;
import org.drools.reteoo.NodeTypeEnums;
import org.drools.rule.Declaration;
import org.drools.rule.IndexableConstraint;
import org.drools.rule.MvelConstraintTestUtil;
import org.drools.rule.Pattern;
import org.drools.spi.BetaNodeFieldConstraint;
import org.drools.spi.InternalReadAccessor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public abstract class BaseBetaConstraintsTest {

    public static EvaluatorRegistry registry = new EvaluatorRegistry();

    protected BetaNodeFieldConstraint getConstraint(String identifier,
                                                    Operator operator,
                                                    String fieldName,
                                                    Class clazz) {
        ClassFieldAccessorStore store = new ClassFieldAccessorStore();
        store.setClassFieldAccessorCache( new ClassFieldAccessorCache( Thread.currentThread().getContextClassLoader() ) );
        store.setEagerWire( true );
        InternalReadAccessor extractor = store.getReader( clazz,
                                                          fieldName,
                                                          getClass().getClassLoader() );
        Declaration declaration = new Declaration( identifier,
                                                   extractor,
                                                   new Pattern( 0,
                                                                new ClassObjectType( clazz ) ) );

        String expression = fieldName + " " + operator.getOperatorString() + " " + identifier;
        return new MvelConstraintTestUtil(expression, operator.getOperatorString(), declaration, extractor);
    }

    protected void checkBetaConstraints(BetaNodeFieldConstraint[] constraints,
                                        Class cls) {
        checkBetaConstraints(constraints, cls, NodeTypeEnums.JoinNode);
    }

    protected void checkBetaConstraints(BetaNodeFieldConstraint[] constraints,
                                        Class cls,
                                        short betaNodeType) {
        RuleBaseConfiguration config = new RuleBaseConfiguration();
        int depth = config.getCompositeKeyDepth();

        BetaConstraints betaConstraints;

        try {
            betaConstraints = (BetaConstraints) cls.getConstructor( new Class[]{BetaNodeFieldConstraint[].class, RuleBaseConfiguration.class} ).newInstance( constraints, config );
        } catch ( Exception e ) {
            throw new RuntimeException( "could not invoke constructor for " + cls.getName() );
        }

        betaConstraints.initIndexes(depth, betaNodeType);

        //BetaConstraints betaConstraints = new DefaultBetaConstraints(constraints, config );

        constraints = betaConstraints.getConstraints();

        List<Integer> list = new ArrayList<Integer>();

        // get indexed positions
        for ( int i = 0; i < constraints.length && list.size() < depth; i++ ) {
            if ( ((IndexableConstraint)constraints[i]).isIndexable(betaNodeType) ) {
                list.add( i );
            }
        }

        // convert to array
        int[] indexedPositions = new int[list.size()];
        for ( int i = 0; i < list.size(); i++ ) {
            indexedPositions[i] = i;
        }

        assertEquals( (indexedPositions.length > 0),
                      betaConstraints.isIndexed() );
        assertEquals( indexedPositions.length,
                      betaConstraints.getIndexCount() );
        BetaMemory betaMemory = betaConstraints.createBetaMemory( config, NodeTypeEnums.JoinNode );

        if ( indexedPositions.length > 0 ) {
            if (((IndexableConstraint)constraints[indexedPositions[0]]).getConstraintType() == ConstraintType.EQUAL) {
                LeftTupleIndexHashTable tupleHashTable = (LeftTupleIndexHashTable) betaMemory.getLeftTupleMemory();
                assertTrue( tupleHashTable.isIndexed() );
                Index index = tupleHashTable.getIndex();

                for ( int i = 0; i < indexedPositions.length; i++ ) {
                    checkSameConstraintForIndex( (IndexableConstraint)constraints[indexedPositions[i]],
                                                 index.getFieldIndex( i ) );
                }

                RightTupleIndexHashTable factHashTable = (RightTupleIndexHashTable) betaMemory.getRightTupleMemory();
                assertTrue( factHashTable.isIndexed() );
                index = factHashTable.getIndex();

                for ( int i = 0; i < indexedPositions.length; i++ ) {
                    checkSameConstraintForIndex( (IndexableConstraint)constraints[indexedPositions[i]],
                                                 index.getFieldIndex( i ) );
                }
            } else {

            }
        } else {
            LeftTupleList tupleHashTable = (LeftTupleList) betaMemory.getLeftTupleMemory();
            assertFalse( tupleHashTable.isIndexed() );

            RightTupleList factHashTable = (RightTupleList) betaMemory.getRightTupleMemory();
            assertFalse( factHashTable.isIndexed() );
        }
    }

    protected void checkSameConstraintForIndex(IndexableConstraint constraint,
                                               FieldIndex fieldIndex) {
        assertSame( constraint.getRequiredDeclarations()[0],
                    fieldIndex.getDeclaration() );
        assertSame( constraint.getFieldExtractor(),
                    fieldIndex.getExtractor() );
    }

    protected BetaNodeFieldConstraint[] convertToConstraints(LinkedList list) {
        final BetaNodeFieldConstraint[] array = new BetaNodeFieldConstraint[list.size()];
        int i = 0;
        for ( LinkedListEntry entry = (LinkedListEntry) list.getFirst(); entry != null; entry = (LinkedListEntry) entry.getNext() ) {
            array[i++] = (BetaNodeFieldConstraint) entry.getObject();
        }
        return array;
    }
}
