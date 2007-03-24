package org.drools.clp;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.TokenStream;
import org.drools.Person;
import org.drools.compiler.SwitchingCommonTokenStream;

import junit.framework.TestCase;

public class CompiledParserTest extends TestCase {
   private CLPParser parser;
   
    public void test1() throws Exception { 
        BlockExecutionEngine engine = ( BlockExecutionEngine ) parse("(bind ?x (+ 20 11) ) (modify ?p (age ?x) )").rhs();
        ExecutionContext context = new ExecutionContext(null, null, 2);
        
        Person p = new Person("mark");        
        Map vars = new HashMap();
                
        //vars.put( "?x", new LocalVariableValue( "?x", 0 ) );        
        vars.put( "?p", new ObjectLiteralValue( p ) );
        engine.replaceTempTokens( vars );
        
        engine.execute( context );
        
        assertEquals( 31, p.getAge() );
    }
    
    public void testIf() throws Exception {
        BlockExecutionEngine engine = ( BlockExecutionEngine ) parse("(if (< ?x ?y ) then (modify ?p (age 15) ) else (modify ?p (age 5)))").rhs();
        ExecutionContext context = new ExecutionContext(null, null, 2);
        
        Person p = new Person("mark");        
        Map vars = new HashMap();
        
        vars.put( "?x", new LongLiteralValue( 10 ) );
        vars.put( "?y", new LocalVariableValue( "?y", 0 ) );     
        vars.put( "?p", new ObjectLiteralValue( p ) );
        engine.replaceTempTokens( vars );
        
        context.setLocalVariable( 0, new Long( 20 ) );
        
        engine.execute( context );        
        assertEquals( 15, p.getAge() );
        
        context.setLocalVariable( 0, new Long( 7 ) );
        engine.execute( context );
        assertEquals( 5, p.getAge() );
    }
    
    public void testWhile() throws Exception {
        BlockExecutionEngine engine = ( BlockExecutionEngine ) parse("(while (< ?x ?y) do (bind ?x (+ ?x 1) ) )").rhs();
        ExecutionContext context = new ExecutionContext(null, null, 2);
        
        Map vars = new HashMap();
        
        vars.put( "?x", new LocalVariableValue( "?x", 0 ) );  
        vars.put( "?y", new LocalVariableValue( "?y", 1 ) );     
        engine.replaceTempTokens( vars );
        
        context.setLocalVariable( 0, new Long( 0 ) );
        context.setLocalVariable( 1, new Long( 10 ) );
        
        engine.execute( context );        
        assertEquals( new BigDecimal(10), context.getLocalVariable( 0 ) );
    }    
    
    private CLPParser parse(final String text) throws Exception {
        this.parser = newParser( newTokenStream( newLexer( newCharStream( text ) ) ) );
        return this.parser;
    }

    private CLPParser parse(final String source,
                            final String text) throws Exception {
        this.parser = newParser( newTokenStream( newLexer( newCharStream( text ) ) ) );
        this.parser.setSource( source );
        return this.parser;
    }

    private Reader getReader(final String name) throws Exception {
        final InputStream in = getClass().getResourceAsStream( name );

        return new InputStreamReader( in );
    }

    private CLPParser parseResource(final String name) throws Exception {
        Reader reader = getReader( name );

        final StringBuffer text = new StringBuffer();

        final char[] buf = new char[1024];
        int len = 0;

        while ( (len = reader.read( buf )) >= 0 ) {
            text.append( buf,
                         0,
                         len );
        }

        return parse( name,
                      text.toString() );
    }

    private CharStream newCharStream(final String text) {
        return new ANTLRStringStream( text );
    }

    private CLPLexer newLexer(final CharStream charStream) {
        return new CLPLexer( charStream );
    }

    private TokenStream newTokenStream(final Lexer lexer) {
        return new SwitchingCommonTokenStream( lexer );
    }

    private CLPParser newParser(final TokenStream tokenStream) {
        final CLPParser p = new CLPParser( tokenStream );
        p.setFunctionRegistry( new FunctionRegistry( BuiltinFunctions.getInstance() )  );
        //p.setParserDebug( true );
        return p;
    }

    private void assertEqualsIgnoreWhitespace(final String expected,
                                              final String actual) {
        final String cleanExpected = expected.replaceAll( "\\s+",
                                                          "" );
        final String cleanActual = actual.replaceAll( "\\s+",
                                                      "" );

        assertEquals( cleanExpected,
                      cleanActual );
    }    
}
