package org.drools.reteoo;

import java.beans.IntrospectionException;
import java.util.Iterator;

import org.drools.Cheese;
import org.drools.DroolsTestCase;
import org.drools.FactException;
import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.base.ClassFieldExtractor;
import org.drools.base.ClassObjectType;
import org.drools.common.BetaNodeBinder;
import org.drools.common.PropagationContextImpl;
import org.drools.rule.Declaration;
import org.drools.rule.PredicateConstraint;
import org.drools.rule.Rule;
import org.drools.spi.Extractor;
import org.drools.spi.FieldExtractor;
import org.drools.spi.MockConstraint;
import org.drools.spi.ObjectType;
import org.drools.spi.PredicateExpression;
import org.drools.spi.PropagationContext;
import org.drools.spi.Tuple;

public class NotNodeTest extends DroolsTestCase {
    Rule               rule;
    PropagationContext context;
    WorkingMemoryImpl  workingMemory;
    MockObjectSource   objectSource;
    MockTupleSource    tupleSource;
    MockObjectSink      sink;
    NotNode           node;
    RightInputAdapterNode  ria;
    BetaMemory         memory;
    MockConstraint    constraint = new MockConstraint(); 

    /**
     * Setup the BetaNode used in each of the tests
     * @throws IntrospectionException 
     */
    public void setUp() throws IntrospectionException {
        this.rule = new Rule( "test-rule" );
        this.context = new PropagationContextImpl( 0,
                                                   PropagationContext.ASSERTION,
                                                   null,
                                                   null );
        this.workingMemory = new WorkingMemoryImpl( new RuleBaseImpl() );                                       

        // string1Declaration is bound to column 3 
        this.node = new NotNode( 15,
                                 new MockTupleSource( 5 ),
                                 new MockObjectSource( 8 ),
                                 new BetaNodeBinder( this.constraint ) );
        
        this.ria = new RightInputAdapterNode(2, 0, this.node);
        this.ria.attach();
        
        this.sink = new MockObjectSink();
        this.ria.addObjectSink( this.sink );

        this.memory = (BetaMemory) this.workingMemory.getNodeMemory( this.node );
    }

    /**
     * Test assertion with both Objects and Tuples
     * 
     * @throws AssertionException
     */
    public void testNotStandard() throws FactException {
        // assert tuple
        Cheese cheddar = new Cheese( "cheddar", 10 );
        FactHandleImpl f0 = (FactHandleImpl) workingMemory.assertObject( cheddar );

        ReteTuple tuple1 = new ReteTuple( f0 );
        
        assertNull( tuple1.getLinkedTuples() );        
        
        this.node.assertTuple( tuple1,
                               this.context,
                               this.workingMemory );        

        // no matching objects, so should propagate
        assertLength( 1,
                      this.sink.getAsserted() );
        
        assertLength( 0,
                      this.sink.getRetracted() );        

        assertNotNull( tuple1.getLinkedTuples() );
        
        assertEquals( f0,
                      ((Object[])this.sink.getAsserted().get( 0 ))[0] );
        
        // assert will match, so propagated tuple should be retracted
        Cheese brie = new Cheese( "brie", 10 );        
        FactHandleImpl f1 = (FactHandleImpl) workingMemory.assertObject( brie );
        
        this.node.assertObject( f1,
                                this.context,
                                this.workingMemory );        

        // check no as assertions, but should be one retraction
        assertLength( 1,
                      this.sink.getAsserted() );

        assertLength( 1,
                      this.sink.getRetracted() );        
        
        assertEquals( f0,
                      ((Object[])this.sink.getRetracted().get( 0 ))[0] );                        

        // assert tuple, will have matches, so no propagation
        FactHandleImpl f2 = (FactHandleImpl) workingMemory.assertObject( new Cheese( "gouda", 10 ) );
        ReteTuple tuple2 = new ReteTuple( f2 );
        this.node.assertTuple( tuple2,
                               this.context,
                               this.workingMemory );

        // check no propagations 
        assertLength( 1,
                      this.sink.getAsserted() );

        assertLength( 1,
                      this.sink.getRetracted() );        
        

        // check memory sizes
        assertEquals( 2,
                      this.memory.getLeftTupleMemory().size() );
        assertEquals( 1,
                      this.memory.getRightFactHandleMemory().size() );
        
        // When this is retracter both tuples should assert
        this.node.retractObject( f1, context, workingMemory );
        
        // check no propagations 
        assertLength( 3,
                      this.sink.getAsserted() );

        assertLength( 1,
                      this.sink.getRetracted() );        
    }
    
    /**
     * Test assertion with both Objects and Tuples
     * 
     * @throws AssertionException
     */
    public void testNotWithConstraints() throws FactException {
        this.constraint.isAllowed = false;
        
        // assert tuple
        Cheese cheddar = new Cheese( "cheddar", 10 );
        FactHandleImpl f0 = (FactHandleImpl) workingMemory.assertObject( cheddar );

        ReteTuple tuple1 = new ReteTuple( f0 );
        
        assertNull( tuple1.getLinkedTuples() );        
        
        this.node.assertTuple( tuple1,
                               this.context,
                               this.workingMemory );        

        // no matching objects, so should propagate
        assertLength( 1,
                      this.sink.getAsserted() );
        
        assertLength( 0,
                      this.sink.getRetracted() );        

        assertNotNull( tuple1.getLinkedTuples() );
        
        assertEquals( f0,
                      ((Object[])this.sink.getAsserted().get( 0 ))[0] );
        
        // assert will not match, so activation should stay propagated
        Cheese brie = new Cheese( "brie", 10 );        
        FactHandleImpl f1 = (FactHandleImpl) workingMemory.assertObject( brie );
        
        this.node.assertObject( f1,
                                this.context,
                                this.workingMemory );        

        // check no as assertions, but should be one retraction
        assertLength( 1,
                      this.sink.getAsserted() );

        assertLength( 0,
                      this.sink.getRetracted() );      
        
        // assert tuple, will have no matches, so do assert propagation
        FactHandleImpl f2 = (FactHandleImpl) workingMemory.assertObject( new Cheese( "gouda", 10 ) );
        ReteTuple tuple2 = new ReteTuple( f2 );
        this.node.assertTuple( tuple2,
                               this.context,
                               this.workingMemory );        
        
        // check no as assertions, but should be one retraction
        assertLength( 2,
                      this.sink.getAsserted() );

        assertLength( 0,
                      this.sink.getRetracted() );           
    }  
}
