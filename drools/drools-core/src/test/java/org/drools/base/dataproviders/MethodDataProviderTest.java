package org.drools.base.dataproviders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.drools.Cheese;
import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.base.ClassObjectType;
import org.drools.base.resolvers.DeclarationVariable;
import org.drools.base.resolvers.GlobalVariable;
import org.drools.base.resolvers.LiteralValue;
import org.drools.base.resolvers.ValueHandler;
import org.drools.common.DefaultFactHandle;
import org.drools.common.PropagationContextImpl;
import org.drools.reteoo.ReteTuple;
import org.drools.reteoo.ReteooRuleBase;
import org.drools.reteoo.ReteooWorkingMemory;
import org.drools.rule.Column;
import org.drools.rule.Declaration;
import org.drools.rule.Package;
import org.drools.spi.ColumnExtractor;
import org.drools.spi.Extractor;
import org.drools.spi.PropagationContext;
import org.drools.spi.Tuple;

public class MethodDataProviderTest extends TestCase {

    private PropagationContext  context;
    private ReteooWorkingMemory workingMemory;
    private Map                 declarations;
    private Map                 globals;

    protected void setUp() {
        context = new PropagationContextImpl( 0,
                                              PropagationContext.ASSERTION,
                                              null,
                                              null );
        workingMemory = new ReteooWorkingMemory( 1,
                                                 (ReteooRuleBase) RuleBaseFactory.newRuleBase() );

        declarations = new HashMap();
        globals = new HashMap();
    }

    public void testWithDeclarationsHelloWorld() throws Exception {

        Column column = new Column( 0,
                                    new ClassObjectType( Cheese.class ) );

        Extractor ex = new ColumnExtractor( new ClassObjectType( TestVariable.class ) );
        Declaration varDec = new Declaration( "var",
                                              ex,
                                              column );
        declarations.put( "var",
                          varDec );

        column = new Column( 1,
                             new ClassObjectType( Cheese.class ) );
        ex = new ColumnExtractor( new ClassObjectType( String.class ) );
        Declaration var2Dec = new Declaration( "var2",
                                               ex,
                                               column );
        declarations.put( "var2",
                          var2Dec );

        List args = new ArrayList();
        args.add( new LiteralValue( "boo" ) );
        args.add( new LiteralValue( new Integer( 42 ) ) );
        args.add( new DeclarationVariable( var2Dec ) );

        MethodInvoker invoker = new MethodInvoker( "helloWorld",
                                                   new DeclarationVariable( varDec ),
                                                   (ValueHandler[]) args.toArray( new ValueHandler[args.size()] ) );

        MethodDataProvider prov = new MethodDataProvider( invoker );

        TestVariable var = new TestVariable();
        FactHandle varHandle = workingMemory.assertObject( var );
        FactHandle var2Handle = workingMemory.assertObject( "hola" );

        Tuple tuple = new ReteTuple( new ReteTuple( (DefaultFactHandle) varHandle ),
                                         (DefaultFactHandle) var2Handle );

        Iterator it = prov.getResults( tuple,
                                       workingMemory,
                                       context );

        Object result = it.next();
        assertEquals( "boo42hola",
                      result );

    }

    public void testWithGlobals() throws Exception {
        globals.put( "foo",
                     TestVariable.class );

        Package pkg = new Package( "nothing" );
        pkg.addGlobal( "foo",
                       TestVariable.class );
        RuleBase rb = RuleBaseFactory.newRuleBase();
        rb.addPackage( pkg );

        WorkingMemory wm = rb.newWorkingMemory();

        wm.setGlobal( "foo",
                      new TestVariable() );

        MethodInvoker invoker = new MethodInvoker( "otherMethod",
                                                   new GlobalVariable( "foo", TestVariable.class ),
                                                   new ValueHandler[0] );        
        
        
        MethodDataProvider prov = new MethodDataProvider( invoker );

        Iterator it = prov.getResults( null,
                                       wm,
                                       null );
        assertTrue( it.hasNext() );
        assertEquals( "boo",
                      it.next() );
    }

}
