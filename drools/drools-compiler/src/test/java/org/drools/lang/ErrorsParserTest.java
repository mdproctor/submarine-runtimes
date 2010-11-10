package org.drools.lang;

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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import junit.framework.TestCase;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.Tree;
import org.drools.base.evaluators.EvaluatorRegistry;
import org.drools.compiler.DroolsParserException;
import org.drools.lang.dsl.DefaultExpander;

public class ErrorsParserTest extends TestCase {

    private DRL5xParser parser;

    protected void setUp() throws Exception {
        super.setUp();
        this.parser = null;

        // initializes pluggable operators
        new EvaluatorRegistry();
    }

    protected void tearDown() throws Exception {
        this.parser = null;
        super.tearDown();
    }

    public void testPartialAST() throws Exception {
        parseResource( "pattern_partial.drl" );

        Tree object = (Tree) this.parser.compilation_unit().getTree();

        System.out.println( this.getName() );
        for ( String message : this.parser.getErrorMessages() ) {
            System.out.println( message );
        }

        assertTrue( this.parser.hasErrors() );

        // FIXME check for generated Tree
        // final PackageDescr pkg = this.parser.getPackageDescr();
        // assertEquals( 1,
        // pkg.getRules().size() );
        // final RuleDescr rule = (RuleDescr) pkg.getRules().get( 0 );
        //
        // assertEquals( 1,
        // rule.getLhs().getDescrs().size() );
        // final PatternDescr pattern = (PatternDescr)
        // rule.getLhs().getDescrs().get( 0 );
        //
        // assertNotNull( pattern );
        // assertEquals( "Bar",
        // pattern.getObjectType() );
        // assertEquals( "foo3",
        // pattern.getIdentifier() );
    }

    public void testNotBindindShouldBarf() throws Exception {
        final DRL5xParser parser = parseResource( "not_with_binding_error.drl" );
        parser.compilation_unit();
        System.out.println( this.getName() );
        for ( String message : this.parser.getErrorMessages() ) {
            System.out.println( message );
        }

        assertTrue( parser.hasErrors() );
    }

    public void testExpanderErrorsAfterExpansion() throws Exception {

        final String name = "expander_post_errors.dslr";
        final Expander expander = new DefaultExpander();
        final String expanded = expander.expand( this.getReader( name ) );

        final DRL5xParser parser = parse( name,
                                        expanded );
        parser.compilation_unit();
        assertTrue( parser.hasErrors() );
//        for ( String message : this.parser.getErrorMessages() ) {
//            System.out.println( message );
//        }

        final DroolsParserException err = (DroolsParserException) parser.getErrors().get( 0 );
        assertEquals( 1,
                      parser.getErrors().size() );
        assertEquals( 6,
                      err.getLineNumber() );
    }

    public void testInvalidSyntax_Catches() throws Exception {
        parseResource( "invalid_syntax.drl" ).compilation_unit();
        System.out.println( this.getName() );
        for ( String message : this.parser.getErrorMessages() ) {
            System.out.println( message );
        }
        assertTrue( this.parser.hasErrors() );
    }

    public void testMultipleErrors() throws Exception {
        parseResource( "multiple_errors.drl" ).compilation_unit();
        System.out.println( this.getName() );
        for ( String message : this.parser.getErrorMessages() ) {
            System.out.println( message );
        }
        assertTrue( this.parser.hasErrors() );
        assertEquals( 2,
                      this.parser.getErrors().size() );
    }

    public void testPackageGarbage() throws Exception {

        parseResource( "package_garbage.drl" ).compilation_unit();
        System.out.println( this.getName() );
        for ( String message : this.parser.getErrorMessages() ) {
            System.out.println( message );
        }
        assertTrue( this.parser.hasErrors() );
    }

    public void testEvalWithSemicolon() throws Exception {
        parseResource( "eval_with_semicolon.drl" ).compilation_unit();
        System.out.println( this.getName() );
        for ( String message : this.parser.getErrorMessages() ) {
            System.out.println( message );
        }
        assertTrue( this.parser.hasErrors() );
        assertEquals( 1,
                      this.parser.getErrorMessages().size() );
        assertEquals( "ERR 104",
                      this.parser.getErrors().get( 0 ).getErrorCode() );
    }

    public void testLexicalError() throws Exception {
        parseResource( "lex_error.drl" ).compilation_unit();
        System.out.println( this.getName() );
        for ( String message : this.parser.getErrorMessages() ) {
            System.out.println( message );
        }
        assertTrue( parser.hasErrors() );
    }

    public void testTempleteError() throws Exception {
        parseResource( "template_test_error.drl" ).compilation_unit();
        System.out.println( this.getName() );
        for ( String message : this.parser.getErrorMessages() ) {
            System.out.println( message );
        }
        assertTrue( parser.hasErrors() );
    }

    public void testRuleParseLhs2() throws Exception {
        final String text = "Message( Message.HELLO )\n";
        parse( text ).lhs_pattern();
        System.out.println( this.getName() );
        for ( String message : this.parser.getErrorMessages() ) {
            System.out.println( message );
        }
        assertTrue( parser.hasErrors() );
    }

    public void testErrorMessageForMisplacedParenthesis() throws Exception {
        final DRL5xParser parser = parseResource( "misplaced_parenthesis.drl" );
        parser.compilation_unit();

        System.out.println( this.getName() );
        for ( String message : this.parser.getErrorMessages() ) {
            System.out.println( message );
        }
        assertTrue( "Parser should have raised errors",
                    parser.hasErrors() );

        assertEquals( 2,
                      parser.getErrors().size() );

        assertEquals( "ERR 101",
                      parser.getErrors().get( 0 ).getErrorCode() );
        assertEquals( "ERR 102",
                      parser.getErrors().get( 1 ).getErrorCode() );
    }

    public void testNPEOnParser() throws Exception {
        final DRL5xParser parser = parseResource( "npe_on_parser.drl" );
        parser.compilation_unit();
        System.out.println( this.getName() );
        for ( String message : this.parser.getErrorMessages() ) {
            System.out.println( message );
        }
        assertTrue( "Parser should have raised errors",
                    parser.hasErrors() );

        assertEquals( 1,
                      parser.getErrors().size() );

        assertTrue( parser.getErrors().get( 0 ).getErrorCode().equals( "ERR 101" ) );
    }

    public void testCommaMisuse() throws Exception {
        final DRL5xParser parser = parseResource( "comma_misuse.drl" );
        try {
            parser.compilation_unit();
//            System.out.println( this.getName() );
//            for ( String message : this.parser.getErrorMessages() ) {
//                System.out.println( message );
//            }
            assertTrue( "Parser should have raised errors",
                        parser.hasErrors() );
            assertEquals( 4,
                          parser.getErrors().size() );

        } catch ( NullPointerException npe ) {
            fail( "Should not raise NPE" );
        }
    }

    private DRL5xParser parse(final String text) throws Exception {
        this.parser = newParser( newTokenStream( newLexer( newCharStream( text ) ) ) );
        return this.parser;
    }

    private DRL5xParser parse(final String source,
                            final String text) throws Exception {
        this.parser = newParser( newTokenStream( newLexer( newCharStream( text ) ) ) );
        // this.parser.setSource( source );
        return this.parser;
    }

    private Reader getReader(final String name) throws Exception {
        final InputStream in = getClass().getResourceAsStream( name );

        return new InputStreamReader( in );
    }

    private DRL5xParser parseResource(final String name) throws Exception {

        final Reader reader = getReader( name );

        final StringBuilder text = new StringBuilder();

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

    private DRL5xLexer newLexer(final CharStream charStream) {
        return new DRL5xLexer( charStream );
    }

    private TokenStream newTokenStream(final Lexer lexer) {
        return new CommonTokenStream( lexer );
    }

    private DRL5xParser newParser(final TokenStream tokenStream) {
        final DRL5xParser p = new DRL5xParser( tokenStream );
        p.setTreeAdaptor( new DroolsTreeAdaptor() );
        return p;
    }
}