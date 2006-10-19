package org.drools.reteoo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.drools.RuleBaseConfiguration;
import org.drools.common.BaseNode;
import org.drools.common.BetaConstraints;
import org.drools.common.DefaultBetaConstraints;
import org.drools.common.DefaultFactHandle;
import org.drools.common.InternalFactHandle;
import org.drools.common.InternalWorkingMemory;
import org.drools.common.NodeMemory;
import org.drools.common.PropagationContextImpl;
import org.drools.spi.AlphaNodeFieldConstraint;
import org.drools.spi.DataProvider;
import org.drools.spi.PropagationContext;

public class FromNode extends TupleSource
    implements
    TupleSink,
    NodeMemory {
    /**
     * 
     */
    private static final long          serialVersionUID = 320;

    private DataProvider               dataProvider;
    private TupleSource                tupleSource;
    private AlphaNodeFieldConstraint[] constraints;
    private BetaConstraints            binder;

    public FromNode(final int id,
                    final DataProvider dataProvider,
                    final TupleSource tupleSource,
                    final AlphaNodeFieldConstraint[] constraints,
                    final BetaConstraints binder) {
        super( id );
        this.dataProvider = dataProvider;
        this.tupleSource = tupleSource;
        this.constraints = constraints;
        if ( binder == null ) {
            this.binder = new DefaultBetaConstraints();
        } else {
            this.binder = binder;
        }
    }

    /**
     * This method isn't as efficient as it could be, as its using the standard join node mechanisms - so everything is bidirectionally
     * linked. As FactHandle's are never retracted, this relationship does not need to be maintined - but as this optimisation would 
     * need refactoring, I've used the standard join node mechanism for now. 
     * 
     */
    public void assertTuple(final ReteTuple leftTuple,
                            final PropagationContext context,
                            final InternalWorkingMemory workingMemory) {
        final BetaMemory memory = (BetaMemory) workingMemory.getNodeMemory( this );

        memory.add( workingMemory,
                    leftTuple );

        for ( final Iterator it = this.dataProvider.getResults( leftTuple,
                                                          workingMemory,
                                                          context ); it.hasNext(); ) {
            final Object object = it.next();

            // First alpha node filters
            boolean isAllowed = true;
            for ( int i = 0, length = this.constraints.length; i < length; i++ ) {
                if ( !this.constraints[i].isAllowed( object,
                                                     leftTuple,
                                                     workingMemory ) ) {
                    isAllowed = false;
                    break;
                }
            }

            if ( !isAllowed ) {
                continue;
            }

            final InternalFactHandle handle = workingMemory.getFactHandleFactory().newFactHandle( object );
            final ObjectMatches objectMatches = new ObjectMatches( (DefaultFactHandle) handle );

            if ( this.binder.isAllowed( handle,
                                   leftTuple,
                                   workingMemory ) ) {
                final TupleMatch tupleMatch = new CompositeTupleMatch( leftTuple,
                                                                       objectMatches );

                leftTuple.addTupleMatch( (DefaultFactHandle) handle,
                                         tupleMatch );

                this.sink.propagateAssertTuple( leftTuple,
                                                handle,
                                                tupleMatch,
                                                context,
                                                workingMemory );
            }
        }
    }

    /**
     * This could be made more intelligent by finding out if the modified Fact is depended upon by the requiredDeclarations.
     * If it isn't then we can continue to just propagate as a normal modify, without having to retrieve and check values 
     * from the DataProvider.
     */
    public void modifyTuple(final ReteTuple leftTuple,
                            final PropagationContext context,
                            final InternalWorkingMemory workingMemory) {
        final BetaMemory memory = (BetaMemory) workingMemory.getNodeMemory( this );

        // We remove the tuple as now its modified it needs to go to the top of
        // the stack, which is added back in else where
        memory.remove( workingMemory,
                       leftTuple );

        final Map matches = leftTuple.getTupleMatches();

        if ( matches.isEmpty() ) {
            // No child propagations, so try as a new assert, will ensure the
            // tuple is added to the top of the memory
            assertTuple( leftTuple,
                         context,
                         workingMemory );
        } else {
            // first purge the network of all future uses of the 'from' facts           
            for ( final Iterator it = matches.values().iterator(); it.hasNext(); ) {
                final TupleMatch tupleMatch = (TupleMatch) it.next();
                tupleMatch.getTuple().retractChildEntries( context,
                                                           workingMemory );
                workingMemory.getFactHandleFactory().destroyFactHandle( tupleMatch.getObjectMatches().getFactHandle() );
            }

            // now all existing matches must now be cleared and the DataProvider re-processed.
            leftTuple.clearTupleMatches();

            assertTuple( leftTuple,
                         context,
                         workingMemory );

        }
    }

    public void retractTuple(final ReteTuple leftTuple,
                             final PropagationContext context,
                             final InternalWorkingMemory workingMemory) {
        final BetaMemory memory = (BetaMemory) workingMemory.getNodeMemory( this );
        memory.remove( workingMemory,
                       leftTuple );

        final Map matches = leftTuple.getTupleMatches();

        if ( !matches.isEmpty() ) {
            for ( final Iterator it = matches.values().iterator(); it.hasNext(); ) {
                final TupleMatch tupleMatch = (TupleMatch) it.next();
                tupleMatch.getTuple().retractChildEntries( context,
                                                           workingMemory );
                workingMemory.getFactHandleFactory().destroyFactHandle( tupleMatch.getObjectMatches().getFactHandle() );
            }
        }
    }

    public List getPropagatedTuples(final InternalWorkingMemory workingMemory,
                                    final TupleSink sink) {
        // TODO Auto-generated method stub
        return null;
    }

    public void attach() {
        this.tupleSource.addTupleSink( this );
    }

    public void attach(final InternalWorkingMemory[] workingMemories) {
        attach();

        for ( int i = 0, length = workingMemories.length; i < length; i++ ) {
            final InternalWorkingMemory workingMemory = workingMemories[i];
            final PropagationContext propagationContext = new PropagationContextImpl( workingMemory.getNextPropagationIdCounter(),
                                                                                      PropagationContext.RULE_ADDITION,
                                                                                      null,
                                                                                      null );
            this.tupleSource.updateNewNode( workingMemory,
                                            propagationContext );
        }
    }

    public void remove(final BaseNode node,
                       final InternalWorkingMemory[] workingMemories) {
        // FIXME
        //        if ( !node.isInUse() ) {
        //            getTupleSinks().remove( node );
        //        }
        //        removeShare();
        //
        //        if ( !this.isInUse() ) {
        //            for ( int i = 0, length = workingMemories.length; i < length; i++ ) {
        //                workingMemories[i].clearNodeMemory( this );
        //            }
        //        }
        //        this.tupleSource.remove( this,
        //                                 workingMemories );
    }

    public void updateNewNode(final InternalWorkingMemory workingMemory,
                              final PropagationContext context) {
        this.attachingNewNode = true;

        final BetaMemory memory = (BetaMemory) workingMemory.getNodeMemory( this );

        // @todo:as there is no right memory

        //        for ( final Iterator it = memory.getRightObjectMemory().iterator(); it.hasNext(); ) {
        //            final ObjectMatches objectMatches = (ObjectMatches) it.next();
        //            final DefaultFactHandle handle = objectMatches.getFactHandle();
        //            for ( TupleMatch tupleMatch = objectMatches.getFirstTupleMatch(); tupleMatch != null; tupleMatch = (TupleMatch) tupleMatch.getNext() ) {
        //                final ReteTuple tuple = new ReteTuple( tupleMatch.getTuple(),
        //                                                       handle );
        //                final TupleSink sink = (TupleSink) this.tupleSinks.get( this.tupleSinks.size() - 1 );
        //                if ( sink != null ) {
        //                    tupleMatch.addJoinedTuple( tuple );
        //                    sink.assertTuple( tuple,
        //                                      context,
        //                                      workingMemory );
        //                } else {
        //                    throw new RuntimeException( "Possible BUG: trying to propagate an assert to a node that was the last added node" );
        //                }
        //            }
        //        }

        this.attachingNewNode = false;
    }

    public Object createMemory(final RuleBaseConfiguration config) {
        return new BetaMemory( config,
                               this.binder );
    }
}
