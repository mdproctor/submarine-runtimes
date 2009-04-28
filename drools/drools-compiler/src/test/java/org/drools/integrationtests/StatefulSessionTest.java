package org.drools.integrationtests;

import junit.framework.TestCase;
import org.drools.Cheese;
import org.drools.FactHandle;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.RuleBase;
import org.drools.RuleBaseConfiguration;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.common.DisconnectedFactHandle;
import org.drools.compiler.PackageBuilder;
import org.drools.concurrent.Future;
import org.drools.io.ResourceFactory;
import org.drools.rule.Package;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.drools.runtime.impl.BatchExecutionResultImpl;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatefulSessionTest extends TestCase {
    final List list = new ArrayList();

    protected RuleBase getRuleBase() throws Exception {

        return RuleBaseFactory.newRuleBase( RuleBase.RETEOO,
                                            null );
    }

    protected RuleBase getRuleBase(final RuleBaseConfiguration config) throws Exception {

        return RuleBaseFactory.newRuleBase( RuleBase.RETEOO,
                                            config );
    }

    public void testSingleObjectAssert() throws Exception {
        StatefulSession session = getSession();

        final Cheese stilton = new Cheese( "stilton",
                                           5 );        

        Future futureAssert = session.asyncInsert( stilton );               
        Future futureFireAllRules = session.asyncFireAllRules();
        
        int i = 0;
        while ( !futureFireAllRules.isDone() ) {
            Thread.sleep( 100 );
            if (i++ > 5) {
                fail( "Future should have finished by now" );
            }
        }        
               
        assertTrue( futureAssert.getObject() instanceof FactHandle );
        assertEquals( "stilton",
                      list.get( 0 ) );
        
    }
    
    public void testInsertObject() throws Exception {
        String str = "";
        str += "package org.drools \n";
        str += "import org.drools.Cheese \n";
        str += "rule rule1 \n";
        str += "  when \n";
        str += "    $c : Cheese() \n";
        str += " \n";
        str += "  then \n";
        str += "    $c.setPrice( 30 ); \n";
        str += "end\n";
        
        Cheese stilton = new Cheese( "stilton", 5 );
        
        final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add( ResourceFactory.newByteArrayResource( str.getBytes() ), ResourceType.DRL );
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );
        
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();        
        
        Command insertCmd = CommandFactory.newInsertObject( stilton, "outStilton" );
        Command fireCmd = CommandFactory.newFireAllRules();
        
        Command cmds = CommandFactory.newBatchExecution( Arrays.asList( new Command[] { insertCmd,fireCmd} ) );
        
        ExecutionResults result = ksession.execute( cmds );
        stilton = ( Cheese ) result.getValue( "outStilton" );
        assertEquals( 30,
                      stilton.getPrice() );      
        
        Object o = ksession.getObject( (FactHandle) result.getFactHandle( "outStilton" ) );
        assertSame( o, stilton );
    }    
    
    public void testArrayObjectAssert() throws Exception {
        StatefulSession session = getSession();

        final Cheese stilton = new Cheese( "stilton",
                                           5 );        

        Future futureAssert = session.asyncInsert( new Object[] { stilton } );                
        Future futureFireAllRules = session.asyncFireAllRules();
        
        int i = 0;
        while ( !futureFireAllRules.isDone() ) {
            Thread.sleep( 100 );
            if (i++ > 5) {
                fail( "Future should have finished by now" );
            }
        }        
               
        
        assertTrue( futureAssert.getObject() instanceof List );
        assertTrue( ((List)futureAssert.getObject()).get( 0 ) instanceof FactHandle );

        assertEquals( "stilton",
                      list.get( 0 ) );        
    } 
    
    public void testCollectionObjectAssert() throws Exception {
        StatefulSession session = getSession();

        final Cheese stilton = new Cheese( "stilton",
                                           5 );        

        List collection = new ArrayList();
        collection.add( stilton );
        Future futureAssert = session.asyncInsert( collection );
        
        Future futureFireAllRules = session.asyncFireAllRules();
        int i = 0;
        while ( !futureFireAllRules.isDone() ) {
            Thread.sleep( 100 );
            if (i++ > 5) {
                fail( "Future should have finished by now" );
            }
        }        
               
        assertTrue( futureAssert.getObject() instanceof List );
        assertTrue( ((List)futureAssert.getObject()).get( 0 ) instanceof FactHandle );
        
        assertEquals( "stilton",
                      list.get( 0 ) );
    }   
    
    public void testHasExceptionSingleAssert()throws Exception {

        StatefulSession session = getExceptionSession();

        final Cheese brie = new Cheese( "brie",
                                        12 );      

        Future futureAssert = session.asyncInsert( brie );

        Future futureFireAllRules = session.asyncFireAllRules();
        int i = 0;
        while ( !futureFireAllRules.isDone() ) {
            Thread.sleep( 100 );
            if (i++ > 5) {
                fail( "Future should have finished by now" );
            }
        }   
        

        assertTrue( futureAssert.getObject() instanceof FactHandle );        
        assertTrue( futureFireAllRules.exceptionThrown() );
        assertTrue( futureFireAllRules.getException() instanceof Exception );
    }
    
    public void testHasExceptionArrayAssert()throws Exception {

        StatefulSession session = getExceptionSession();

        final Cheese brie = new Cheese( "brie",
                                        12 );      

        Future futureAssert = session.asyncInsert( new Object[] { brie } );                
        Future futureFireAllRules = session.asyncFireAllRules();
        
        int i = 0;
        while ( !futureFireAllRules.isDone() ) {
            Thread.sleep( 300 );
            if (i++ > 5) {
                fail( "Future should have finished by now" );
            }
        }        
                       
        assertTrue( futureAssert.getObject() instanceof List );
        assertTrue( ((List)futureAssert.getObject()).get( 0 ) instanceof FactHandle );        
        assertTrue( futureFireAllRules.getException() instanceof Exception );
    }   
    
    public void testHasExceptionCollectionAssert()throws Exception {

        StatefulSession session = getExceptionSession();

        final Cheese brie = new Cheese( "brie",
                                        12 );      

        List collection = new ArrayList();
        collection.add( brie );
        Future futureAssert = session.asyncInsert( collection );             
        Future futureFireAllRules = session.asyncFireAllRules();
        
        int i = 0;
        while ( !futureFireAllRules.isDone() ) {
            Thread.sleep( 100 );
            if (i++ > 5) {
                fail( "Future should have finished by now" );
            }
        }        
                       
        assertTrue( futureAssert.getObject() instanceof List );
        assertTrue( ((List)futureAssert.getObject()).get( 0 ) instanceof FactHandle );        
        assertTrue( futureFireAllRules.getException() instanceof Exception );
    }    
    
    public void testSequentialException() {
        RuleBaseConfiguration config = new RuleBaseConfiguration();
        config.setSequential( true );
        RuleBase ruleBase = RuleBaseFactory.newRuleBase( config );
        
        try {
            ruleBase.newStatefulSession();
            fail("cannot have a stateful session with sequential set to true" );
        } catch ( Exception e ) {
            
        }
    }
    
    private StatefulSession getExceptionSession() throws Exception {
        final PackageBuilder builder = new PackageBuilder();
        builder.addPackageFromDrl( new InputStreamReader( getClass().getResourceAsStream( "test_ConsequenceException.drl" ) ) );
        final Package pkg = builder.getPackage();

        RuleBase ruleBase = getRuleBase();
        ruleBase.addPackage( pkg );
        ruleBase    = SerializationHelper.serializeObject(ruleBase);
        return ruleBase.newStatefulSession();
    }

    private StatefulSession getSession() throws Exception {
        final PackageBuilder builder = new PackageBuilder();
        builder.addPackageFromDrl( new InputStreamReader( getClass().getResourceAsStream( "literal_rule_test.drl" ) ) );
        final Package pkg = builder.getPackage();

        RuleBase ruleBase = getRuleBase();
        ruleBase.addPackage( pkg );
        ruleBase    = SerializationHelper.serializeObject(ruleBase);
        StatefulSession session = ruleBase.newStatefulSession();

//        session    = SerializationHelper.serializeObject(session);
        session.setGlobal( "list",
                           this.list );
        return session;
    }
      
}
