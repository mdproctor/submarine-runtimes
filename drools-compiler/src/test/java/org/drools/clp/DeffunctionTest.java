package org.drools.clp;

import java.math.BigDecimal;

import org.drools.clp.functions.AddFunction;

import junit.framework.TestCase;

public class DeffunctionTest extends TestCase {
    public void test1() {
        Deffunction function = new Deffunction("x");
        
        ValueHandler a = new LocalVariableValue("a", 0); 
        function.addParameter( a );
        
        ValueHandler b = new LocalVariableValue("b", 1);
        function.addParameter( b );
        
        Function add = new AddFunction();
        FunctionCaller caller = new FunctionCaller(add);
        caller.addParameter( a );
        caller.addParameter( b );
                
        function.addFunction( caller );
                
        ExecutionContext context = new ExecutionContext(null, null, 2);
        ValueHandler q = new LocalVariableValue("q", 0);
        context.setLocalVariable( 0, new Integer( 10 ) );
        
        ValueHandler w = new LocalVariableValue("w", 1);
        context.setLocalVariable( 1, new Integer( 7 ) );      
        
        assertEquals( new BigDecimal( 17 ), function.execute( new ValueHandler[] { q, w }, context ) );
    }
    
    
}
