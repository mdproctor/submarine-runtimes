package org.drools.lang;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.FailedPredicateException;
import org.antlr.runtime.MismatchedNotSetException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.MismatchedTreeNodeException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.drools.compiler.DroolsParserException;

/**
 * Helper class that generates DroolsParserException with user friendly error
 * messages.
 * 
 * @author porcelli
 * @see DroolsParserException
 */
public class DroolsParserExceptionFactory {
    public final static String                        MISMATCHED_TOKEN_MESSAGE_COMPLETE       = "Line %1$d:%2$d mismatched input '%3$s' expecting '%4$s'%5$s";
    public final static String                        MISMATCHED_TOKEN_MESSAGE_PART           = "Line %1$d:%2$d mismatched input '%3$s'%4$s";
    public final static String                        MISMATCHED_TREE_NODE_MESSAGE_COMPLETE   = "Line %1$d:%2$d mismatched tree node '%3$s' expecting '%4$s'%5$s";
    public final static String                        MISMATCHED_TREE_NODE_MESSAGE_PART       = "Line %1$d:%2$d mismatched tree node '%3$s'%4$s";
    public final static String                        NO_VIABLE_ALT_MESSAGE                   = "Line %1$d:%2$d no viable alternative at input '%3$s'%4$s";
    public final static String                        EARLY_EXIT_MESSAGE                      = "Line %1$d:%2$d required (...)+ loop did not match anything at input '%3$s'%4$s";
    public final static String                        MISMATCHED_SET_MESSAGE                  = "Line %1$d:%2$d mismatched input '%3$s' expecting one of the following tokens: '%4$s'%5$s.";
    public final static String                        MISMATCHED_NOT_SET_MESSAGE              = "Line %1$d:%2$d mismatched input '%3$s' not expecting any of the following tokens: '%4$s'%5$s";
    public final static String                        FAILED_PREDICATE_MESSAGE                = "Line %1$d:%2$d rule '%3$s' failed predicate: {%4$s}?%5$s";
    public final static String                        TRAILING_SEMI_COLON_NOT_ALLOWED_MESSAGE = "Line %1$d:%2$d trailing semi-colon not allowed%3$s";
    public final static String                        PARSER_LOCATION_MESSAGE_COMPLETE        = " in %1$s %2$s";
    public final static String                        PARSER_LOCATION_MESSAGE_PART            = " in %1$s";
    public final static String                        UNEXPECTED_EXCEPTION                    = "Line %1$d:%2$d unexpected exception at input '%3$s'. Exception: %4$s. Stack trace:\n %5$s";

    private String[]                                  tokenNames                              = null;
    private Stack<Map<DroolsParaphraseTypes, String>> paraphrases                             = null;

    /**
     * DroolsParserErrorMessages constructor.
     * 
     * @param tokenNames
     *            tokenNames generated by ANTLR
     * @param paraphrases
     *            paraphrases parser structure
     */
    public DroolsParserExceptionFactory(String[] tokenNames,
                                        Stack<Map<DroolsParaphraseTypes, String>> paraphrases) {
        this.tokenNames = tokenNames;
        this.paraphrases = paraphrases;
    }

    /**
     * This method creates a DroolsParserException for trailing semicolon
     * exception, full of information.
     * 
     * @param line
     *            line number
     * @param column
     *            column position
     * @param offset
     *            char offset
     * @return DroolsParserException filled.
     */
    public DroolsParserException createTrailingSemicolonException( int line,
                                                                   int column,
                                                                   int offset ) {
        String message = String
                .format(
                         DroolsParserExceptionFactory.TRAILING_SEMI_COLON_NOT_ALLOWED_MESSAGE,
                         line,
                         column,
                         formatParserLocation() );

        return new DroolsParserException( "ERR 104",
                                          message,
                                          line,
                                          column,
                                          offset,
                                          null );
    }

    /**
     * This method creates a DroolsParserException full of information.
     * 
     * @param e
     *            original exception
     * @return DroolsParserException filled.
     */
    public DroolsParserException createDroolsException( RecognitionException e ) {
        List<String> codeAndMessage = createErrorMessage( e );
        return new DroolsParserException( codeAndMessage.get( 1 ),
                                          codeAndMessage
                                                  .get( 0 ),
                                          e.line,
                                          e.charPositionInLine,
                                          e.index,
                                          e );
    }

    /**
     * This will take a RecognitionException, and create a sensible error
     * message out of it
     */
    private List<String> createErrorMessage( RecognitionException e ) {
        List<String> codeAndMessage = new ArrayList<String>( 2 );
        String message = "";
        if ( e instanceof MismatchedTokenException ) {
            MismatchedTokenException mte = (MismatchedTokenException) e;
            String expecting = mte instanceof DroolsMismatchedTokenException ? ((DroolsMismatchedTokenException)mte).getTokenText() : getBetterToken( mte.expecting );
            if ( tokenNames != null && mte.expecting >= 0 && mte.expecting < tokenNames.length ) {
                message = String
                        .format(
                                 DroolsParserExceptionFactory.MISMATCHED_TOKEN_MESSAGE_COMPLETE,
                                 e.line,
                                 e.charPositionInLine,
                                 getBetterToken( e.token ),
                                 expecting,
                                 formatParserLocation() );
                codeAndMessage.add( message );
                codeAndMessage.add( "ERR 102" );
            } else {
                message = String
                        .format(
                                 DroolsParserExceptionFactory.MISMATCHED_TOKEN_MESSAGE_PART,
                                 e.line,
                                 e.charPositionInLine,
                                 getBetterToken( e.token ),
                                 formatParserLocation() );
                codeAndMessage.add( message );
                codeAndMessage.add( "ERR 102" );
            }
        } else if ( e instanceof MismatchedTreeNodeException ) {
            MismatchedTreeNodeException mtne = (MismatchedTreeNodeException) e;
            if ( mtne.expecting >= 0 && mtne.expecting < tokenNames.length ) {
                message = String
                        .format(
                                 DroolsParserExceptionFactory.MISMATCHED_TREE_NODE_MESSAGE_COMPLETE,
                                 e.line,
                                 e.charPositionInLine,
                                 getBetterToken( e.token ),
                                 getBetterToken( mtne.expecting ),
                                 formatParserLocation() );
                codeAndMessage.add( message );
                codeAndMessage.add( "ERR 106" );
            } else {
                message = String
                        .format(
                                 DroolsParserExceptionFactory.MISMATCHED_TREE_NODE_MESSAGE_PART,
                                 e.line,
                                 e.charPositionInLine,
                                 getBetterToken( e.token ),
                                 formatParserLocation() );
                codeAndMessage.add( message );
                codeAndMessage.add( "ERR 106" );
            }
        } else if ( e instanceof NoViableAltException ) {
            // NoViableAltException nvae = (NoViableAltException) e;
            message = String.format(
                                     DroolsParserExceptionFactory.NO_VIABLE_ALT_MESSAGE,
                                     e.line,
                                     e.charPositionInLine,
                                     getBetterToken( e.token ),
                                     formatParserLocation() );
            codeAndMessage.add( message );
            codeAndMessage.add( "ERR 101" );
        } else if ( e instanceof EarlyExitException ) {
            // EarlyExitException eee = (EarlyExitException) e;
            message = String.format(
                                     DroolsParserExceptionFactory.EARLY_EXIT_MESSAGE,
                                     e.line,
                                     e.charPositionInLine,
                                     getBetterToken( e.token ),
                                     formatParserLocation() );
            codeAndMessage.add( message );
            codeAndMessage.add( "ERR 105" );
        } else if ( e instanceof MismatchedSetException ) {
            MismatchedSetException mse = (MismatchedSetException) e;
            String expected = expectedTokensAsString( mse.expecting );
            message = String.format(
                                     DroolsParserExceptionFactory.MISMATCHED_SET_MESSAGE,
                                     e.line,
                                     e.charPositionInLine,
                                     getBetterToken( e.token ),
                                     expected,
                                     formatParserLocation() );
            codeAndMessage.add( message );
            codeAndMessage.add( "ERR 107" );
        } else if ( e instanceof DroolsMismatchedSetException ) {
            DroolsMismatchedSetException mse = (DroolsMismatchedSetException) e;
            String expected = Arrays.asList( mse.getTokenText() ).toString();
            message = String.format(
                                     DroolsParserExceptionFactory.MISMATCHED_SET_MESSAGE,
                                     e.line,
                                     e.charPositionInLine,
                                     getBetterToken( e.token ),
                                     expected,
                                     formatParserLocation() );
            codeAndMessage.add( message );
            codeAndMessage.add( "ERR 107" );
        } else if ( e instanceof MismatchedNotSetException ) {
            MismatchedNotSetException mse = (MismatchedNotSetException) e;
            String expected = expectedTokensAsString( mse.expecting );
            message = String.format(
                                     DroolsParserExceptionFactory.MISMATCHED_NOT_SET_MESSAGE,
                                     e.line,
                                     e.charPositionInLine,
                                     getBetterToken( e.token ),
                                     expected,
                                     formatParserLocation() );
            codeAndMessage.add( message );
            codeAndMessage.add( "ERR 108" );
        } else if ( e instanceof FailedPredicateException ) {
            FailedPredicateException fpe = (FailedPredicateException) e;
            message = String.format(
                                     DroolsParserExceptionFactory.FAILED_PREDICATE_MESSAGE,
                                     e.line,
                                     e.charPositionInLine,
                                     fpe.ruleName,
                                     fpe.predicateText,
                                     formatParserLocation() );
            codeAndMessage.add( message );
            codeAndMessage.add( "ERR 103" );
        }
        if ( codeAndMessage.get( 0 ).length() == 0 ) {
            codeAndMessage.add( "?????" );
        }
        return codeAndMessage;
    }

    public DroolsParserException createDroolsException( Exception e,
                                                        Token token ) {
        StringWriter sw = new StringWriter();
        e.printStackTrace( new PrintWriter(sw) );
        return new DroolsParserException( String.format(
                                                         DroolsParserExceptionFactory.UNEXPECTED_EXCEPTION,
                                                         token.getLine(),
                                                         token.getCharPositionInLine(),
                                                         getBetterToken( token ),
                                                         e.toString(),
                                                         sw.toString() ),
                                          e );

    }

    private String expectedTokensAsString( BitSet set ) {
        StringBuilder buf = new StringBuilder();
        buf.append( "{ " );
        int i = 0;
        for ( int token : set.toArray() ) {
            if ( i > 0 ) buf.append( ", " );
            buf.append( getBetterToken( token ) );
            i++;
        }
        buf.append( " }" );
        String expected = buf.toString();
        return expected;
    }

    /**
     * This will take Paraphrases stack, and create a sensible location
     */
    private String formatParserLocation() {
        StringBuilder sb = new StringBuilder();
        if ( paraphrases != null ) {
            for ( Map<DroolsParaphraseTypes, String> map : paraphrases ) {
                for ( Entry<DroolsParaphraseTypes, String> activeEntry : map
                        .entrySet() ) {
                    if ( activeEntry.getValue().length() == 0 ) {
                        sb.append( String.format( PARSER_LOCATION_MESSAGE_PART,
                                                  getLocationName( activeEntry.getKey() ) ) );
                    } else {
                        sb.append( String.format( PARSER_LOCATION_MESSAGE_COMPLETE,
                                                  getLocationName( activeEntry.getKey() ),
                                                  activeEntry
                                                          .getValue() ) );
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Returns a string based on Paraphrase Type
     * 
     * @param type
     *            Paraphrase Type
     * @return a string representing the
     */
    private String getLocationName( DroolsParaphraseTypes type ) {
        switch ( type ) {
            case PACKAGE :
                return "package";
            case IMPORT :
                return "import";
            case FUNCTION_IMPORT :
                return "function import";
            case GLOBAL :
                return "global";
            case FUNCTION :
                return "function";
            case QUERY :
                return "query";
            case TEMPLATE :
                return "template";
            case RULE :
                return "rule";
            case RULE_ATTRIBUTE :
                return "rule attribute";
            case PATTERN :
                return "pattern";
            case EVAL :
                return "eval";
            default :
                return "";
        }
    }

    /**
     * Helper method that creates a user friendly token definition
     * 
     * @param token
     *            token
     * @return user friendly token definition
     */
    private String getBetterToken( Token token ) {
        if ( token == null ) {
            return "";
        }
        return getBetterToken( token.getType(),
                               token.getText() );
    }

    /**
     * Helper method that creates a user friendly token definition
     * 
     * @param tokenType
     *            token type
     * @return user friendly token definition
     */
    private String getBetterToken( int tokenType ) {
        return getBetterToken( tokenType,
                               null );
    }

    /**
     * Helper method that creates a user friendly token definition
     * 
     * @param tokenType
     *            token type
     * @param defaultValue
     *            default value for identifier token, may be null
     * @return user friendly token definition
     */
    private String getBetterToken( int tokenType,
                                   String defaultValue ) {
        switch ( tokenType ) {
            case DRLLexer.DECIMAL :
                return defaultValue == null ? "int" : defaultValue;
            case DRLLexer.FLOAT :
                return defaultValue == null ? "float" : defaultValue;
            case DRLLexer.STRING :
                return defaultValue == null ? "string" : defaultValue;
            case DRLLexer.BOOL :
                return defaultValue == null ? "boolean" : defaultValue;
            case DRLLexer.NULL :
                return "null";
            case DRLLexer.SEMICOLON :
                return ";";
            case DRLLexer.COLON :
                return ":";
            case DRLLexer.EQUALS :
                return "==";
            case DRLLexer.NOT_EQUALS :
                return "!=";
            case DRLLexer.GREATER :
                return ">";
            case DRLLexer.GREATER_EQUALS :
                return ">=";
            case DRLLexer.LESS :
                return "<";
            case DRLLexer.LESS_EQUALS :
                return "<=";
            case DRLLexer.ARROW :
                return "->";
            case DRLLexer.ID :
                return defaultValue == null ? "identifier" : defaultValue;
            case DRLLexer.LEFT_PAREN :
                return "(";
            case DRLLexer.RIGHT_PAREN :
                return ")";
            case DRLLexer.LEFT_SQUARE :
                return "[";
            case DRLLexer.RIGHT_SQUARE :
                return "]";
            case DRLLexer.LEFT_CURLY :
                return "{";
            case DRLLexer.RIGHT_CURLY :
                return "}";
            case DRLLexer.COMMA :
                return ",";
            case DRLLexer.DOT :
                return ".";
            case DRLLexer.DOUBLE_AMPER :
                return "&&";
            case DRLLexer.DOUBLE_PIPE :
                return "||";
            case DRLLexer.MISC :
                return defaultValue == null ? "misc" : defaultValue;
            case DRLLexer.EOF :
                return "<eof>";
            default :
                return defaultValue;
        }
    }

}