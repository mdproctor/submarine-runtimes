// $ANTLR 3.0b7 C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g 2007-03-06 05:09:16

	package org.drools.clp;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class CLPLexer extends Lexer {
    public static final int LEFT_PAREN=4;
    public static final int RIGHT_CURLY=24;
    public static final int T29=29;
    public static final int BOOL=11;
    public static final int T33=33;
    public static final int HexDigit=16;
    public static final int T36=36;
    public static final int WS=14;
    public static final int MISC=26;
    public static final int STRING=8;
    public static final int FLOAT=10;
    public static final int T28=28;
    public static final int T35=35;
    public static final int T34=34;
    public static final int VAR=7;
    public static final int UnicodeEscape=17;
    public static final int T37=37;
    public static final int EscapeSequence=15;
    public static final int INT=9;
    public static final int EOF=-1;
    public static final int T32=32;
    public static final int NULL=12;
    public static final int EOL=13;
    public static final int LEFT_SQUARE=21;
    public static final int Tokens=40;
    public static final int T31=31;
    public static final int SH_STYLE_SINGLE_LINE_COMMENT=19;
    public static final int OctalEscape=18;
    public static final int MULTI_LINE_COMMENT=25;
    public static final int T38=38;
    public static final int T27=27;
    public static final int RIGHT_PAREN=6;
    public static final int T30=30;
    public static final int LEFT_CURLY=23;
    public static final int T39=39;
    public static final int RIGHT_SQUARE=22;
    public static final int ID=5;
    public static final int C_STYLE_SINGLE_LINE_COMMENT=20;
    public CLPLexer() {;} 
    public CLPLexer(CharStream input) {
        super(input);
        ruleMemo = new HashMap[38+1];
     }
    public String getGrammarFileName() { return "C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g"; }

    // $ANTLR start T27
    public void mT27() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T27;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:6:7: ( ';' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:6:7: ';'
            {
            match(';'); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T27

    // $ANTLR start T28
    public void mT28() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T28;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:7:7: ( 'defrule' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:7:7: 'defrule'
            {
            match("defrule"); if (failed) return ;


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T28

    // $ANTLR start T29
    public void mT29() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T29;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:8:7: ( 'and' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:8:7: 'and'
            {
            match("and"); if (failed) return ;


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T29

    // $ANTLR start T30
    public void mT30() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T30;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:9:7: ( 'or' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:9:7: 'or'
            {
            match("or"); if (failed) return ;


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T30

    // $ANTLR start T31
    public void mT31() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T31;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:10:7: ( 'not' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:10:7: 'not'
            {
            match("not"); if (failed) return ;


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T31

    // $ANTLR start T32
    public void mT32() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T32;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:11:7: ( 'exists' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:11:7: 'exists'
            {
            match("exists"); if (failed) return ;


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T32

    // $ANTLR start T33
    public void mT33() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T33;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:12:7: ( 'test' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:12:7: 'test'
            {
            match("test"); if (failed) return ;


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T33

    // $ANTLR start T34
    public void mT34() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T34;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:13:7: ( '<-' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:13:7: '<-'
            {
            match("<-"); if (failed) return ;


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T34

    // $ANTLR start T35
    public void mT35() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T35;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:14:7: ( '&' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:14:7: '&'
            {
            match('&'); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T35

    // $ANTLR start T36
    public void mT36() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T36;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:15:7: ( '|' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:15:7: '|'
            {
            match('|'); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T36

    // $ANTLR start T37
    public void mT37() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T37;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:16:7: ( '~' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:16:7: '~'
            {
            match('~'); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T37

    // $ANTLR start T38
    public void mT38() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T38;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:17:7: ( ':' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:17:7: ':'
            {
            match(':'); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T38

    // $ANTLR start T39
    public void mT39() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = T39;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:18:7: ( '=' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:18:7: '='
            {
            match('='); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end T39

    // $ANTLR start VAR
    public void mVAR() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = VAR;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:461:8: ( '?' ID )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:461:8: '?' ID
            {
            match('?'); if (failed) return ;
            mID(); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end VAR

    // $ANTLR start ID
    public void mID() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = ID;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:465:4: ( ('a'..'z'|'A'..'Z'|'_'|'$'|'\\u00c0'..'\\u00ff') ( ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'\\u00c0'..'\\u00ff'))* )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:465:4: ('a'..'z'|'A'..'Z'|'_'|'$'|'\\u00c0'..'\\u00ff') ( ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'\\u00c0'..'\\u00ff'))*
            {
            if ( input.LA(1)=='$'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00FF') ) {
                input.consume();
            failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:465:50: ( ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'\\u00c0'..'\\u00ff'))*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);
                if ( ((LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')||(LA1_0>='\u00C0' && LA1_0<='\u00FF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:465:51: ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'\\u00c0'..'\\u00ff')
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00FF') ) {
            	        input.consume();
            	    failed=false;
            	    }
            	    else {
            	        if (backtracking>0) {failed=true; return ;}
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end ID

    // $ANTLR start NULL
    public void mNULL() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = NULL;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:468:8: ( 'null' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:468:8: 'null'
            {
            match("null"); if (failed) return ;


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end NULL

    // $ANTLR start WS
    public void mWS() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = WS;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:470:17: ( ( ' ' | '\\t' | '\\f' | EOL ) )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:470:17: ( ' ' | '\\t' | '\\f' | EOL )
            {
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:470:17: ( ' ' | '\\t' | '\\f' | EOL )
            int alt2=4;
            switch ( input.LA(1) ) {
            case ' ':
                alt2=1;
                break;
            case '\t':
                alt2=2;
                break;
            case '\f':
                alt2=3;
                break;
            case '\n':
            case '\r':
                alt2=4;
                break;
            default:
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("470:17: ( ' ' | '\\t' | '\\f' | EOL )", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:470:19: ' '
                    {
                    match(' '); if (failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:471:19: '\\t'
                    {
                    match('\t'); if (failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:472:19: '\\f'
                    {
                    match('\f'); if (failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:473:19: EOL
                    {
                    mEOL(); if (failed) return ;

                    }
                    break;

            }

            if ( backtracking==0 ) {
               _channel=HIDDEN; 
            }

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end WS

    // $ANTLR start EOL
    public void mEOL() throws RecognitionException {
        try {
            ruleNestingLevel++;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:480:6: ( ( ( '\\r\\n' )=> '\\r\\n' | '\\r' | '\\n' ) )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:480:6: ( ( '\\r\\n' )=> '\\r\\n' | '\\r' | '\\n' )
            {
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:480:6: ( ( '\\r\\n' )=> '\\r\\n' | '\\r' | '\\n' )
            int alt3=3;
            int LA3_0 = input.LA(1);
            if ( (LA3_0=='\r') ) {
                int LA3_1 = input.LA(2);
                if ( (LA3_1=='\n') ) {
                    alt3=1;
                }
                else {
                    alt3=2;}
            }
            else if ( (LA3_0=='\n') ) {
                alt3=3;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("480:6: ( ( '\\r\\n' )=> '\\r\\n' | '\\r' | '\\n' )", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:480:14: ( '\\r\\n' )=> '\\r\\n'
                    {
                    match("\r\n"); if (failed) return ;


                    }
                    break;
                case 2 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:481:25: '\\r'
                    {
                    match('\r'); if (failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:482:25: '\\n'
                    {
                    match('\n'); if (failed) return ;

                    }
                    break;

            }


            }

        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end EOL

    // $ANTLR start INT
    public void mINT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = INT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:487:4: ( ( '-' )? ( '0' .. '9' )+ )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:487:4: ( '-' )? ( '0' .. '9' )+
            {
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:487:4: ( '-' )?
            int alt4=2;
            int LA4_0 = input.LA(1);
            if ( (LA4_0=='-') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:487:5: '-'
                    {
                    match('-'); if (failed) return ;

                    }
                    break;

            }

            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:487:10: ( '0' .. '9' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);
                if ( ((LA5_0>='0' && LA5_0<='9')) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:487:11: '0' .. '9'
            	    {
            	    matchRange('0','9'); if (failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
            	    if (backtracking>0) {failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end INT

    // $ANTLR start FLOAT
    public void mFLOAT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = FLOAT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:491:4: ( ( '-' )? ( '0' .. '9' )+ '.' ( '0' .. '9' )+ )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:491:4: ( '-' )? ( '0' .. '9' )+ '.' ( '0' .. '9' )+
            {
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:491:4: ( '-' )?
            int alt6=2;
            int LA6_0 = input.LA(1);
            if ( (LA6_0=='-') ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:491:5: '-'
                    {
                    match('-'); if (failed) return ;

                    }
                    break;

            }

            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:491:10: ( '0' .. '9' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);
                if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:491:11: '0' .. '9'
            	    {
            	    matchRange('0','9'); if (failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
            	    if (backtracking>0) {failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);

            match('.'); if (failed) return ;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:491:26: ( '0' .. '9' )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);
                if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:491:27: '0' .. '9'
            	    {
            	    matchRange('0','9'); if (failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
            	    if (backtracking>0) {failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end FLOAT

    // $ANTLR start STRING
    public void mSTRING() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = STRING;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:495:8: ( ( '\"' ( EscapeSequence | ~ ('\\\\'|'\"'))* '\"' ) | ( '\\'' ( EscapeSequence | ~ ('\\\\'|'\\''))* '\\'' ) )
            int alt11=2;
            int LA11_0 = input.LA(1);
            if ( (LA11_0=='\"') ) {
                alt11=1;
            }
            else if ( (LA11_0=='\'') ) {
                alt11=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("494:1: STRING : ( ( '\"' ( EscapeSequence | ~ ('\\\\'|'\"'))* '\"' ) | ( '\\'' ( EscapeSequence | ~ ('\\\\'|'\\''))* '\\'' ) );", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:495:8: ( '\"' ( EscapeSequence | ~ ('\\\\'|'\"'))* '\"' )
                    {
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:495:8: ( '\"' ( EscapeSequence | ~ ('\\\\'|'\"'))* '\"' )
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:495:9: '\"' ( EscapeSequence | ~ ('\\\\'|'\"'))* '\"'
                    {
                    match('\"'); if (failed) return ;
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:495:13: ( EscapeSequence | ~ ('\\\\'|'\"'))*
                    loop9:
                    do {
                        int alt9=3;
                        int LA9_0 = input.LA(1);
                        if ( (LA9_0=='\\') ) {
                            alt9=1;
                        }
                        else if ( ((LA9_0>='\u0000' && LA9_0<='!')||(LA9_0>='#' && LA9_0<='[')||(LA9_0>=']' && LA9_0<='\uFFFE')) ) {
                            alt9=2;
                        }


                        switch (alt9) {
                    	case 1 :
                    	    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:495:15: EscapeSequence
                    	    {
                    	    mEscapeSequence(); if (failed) return ;

                    	    }
                    	    break;
                    	case 2 :
                    	    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:495:32: ~ ('\\\\'|'\"')
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();
                    	    failed=false;
                    	    }
                    	    else {
                    	        if (backtracking>0) {failed=true; return ;}
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop9;
                        }
                    } while (true);

                    match('\"'); if (failed) return ;

                    }


                    }
                    break;
                case 2 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:496:8: ( '\\'' ( EscapeSequence | ~ ('\\\\'|'\\''))* '\\'' )
                    {
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:496:8: ( '\\'' ( EscapeSequence | ~ ('\\\\'|'\\''))* '\\'' )
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:496:9: '\\'' ( EscapeSequence | ~ ('\\\\'|'\\''))* '\\''
                    {
                    match('\''); if (failed) return ;
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:496:14: ( EscapeSequence | ~ ('\\\\'|'\\''))*
                    loop10:
                    do {
                        int alt10=3;
                        int LA10_0 = input.LA(1);
                        if ( (LA10_0=='\\') ) {
                            alt10=1;
                        }
                        else if ( ((LA10_0>='\u0000' && LA10_0<='&')||(LA10_0>='(' && LA10_0<='[')||(LA10_0>=']' && LA10_0<='\uFFFE')) ) {
                            alt10=2;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:496:16: EscapeSequence
                    	    {
                    	    mEscapeSequence(); if (failed) return ;

                    	    }
                    	    break;
                    	case 2 :
                    	    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:496:33: ~ ('\\\\'|'\\'')
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFE') ) {
                    	        input.consume();
                    	    failed=false;
                    	    }
                    	    else {
                    	        if (backtracking>0) {failed=true; return ;}
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recover(mse);    throw mse;
                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);

                    match('\''); if (failed) return ;

                    }


                    }
                    break;

            }

            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end STRING

    // $ANTLR start HexDigit
    public void mHexDigit() throws RecognitionException {
        try {
            ruleNestingLevel++;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:500:12: ( ('0'..'9'|'a'..'f'|'A'..'F'))
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:500:12: ('0'..'9'|'a'..'f'|'A'..'F')
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
                input.consume();
            failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }

        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end HexDigit

    // $ANTLR start EscapeSequence
    public void mEscapeSequence() throws RecognitionException {
        try {
            ruleNestingLevel++;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:504:9: ( '\\\\' ('b'|'t'|'n'|'f'|'r'|'\\\"'|'\\''|'\\\\') | UnicodeEscape | OctalEscape )
            int alt12=3;
            int LA12_0 = input.LA(1);
            if ( (LA12_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\"':
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    alt12=1;
                    break;
                case 'u':
                    alt12=2;
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    alt12=3;
                    break;
                default:
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("502:1: fragment EscapeSequence : ( '\\\\' ('b'|'t'|'n'|'f'|'r'|'\\\"'|'\\''|'\\\\') | UnicodeEscape | OctalEscape );", 12, 1, input);

                    throw nvae;
                }

            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("502:1: fragment EscapeSequence : ( '\\\\' ('b'|'t'|'n'|'f'|'r'|'\\\"'|'\\''|'\\\\') | UnicodeEscape | OctalEscape );", 12, 0, input);

                throw nvae;
            }
            switch (alt12) {
                case 1 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:504:9: '\\\\' ('b'|'t'|'n'|'f'|'r'|'\\\"'|'\\''|'\\\\')
                    {
                    match('\\'); if (failed) return ;
                    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                        input.consume();
                    failed=false;
                    }
                    else {
                        if (backtracking>0) {failed=true; return ;}
                        MismatchedSetException mse =
                            new MismatchedSetException(null,input);
                        recover(mse);    throw mse;
                    }


                    }
                    break;
                case 2 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:505:9: UnicodeEscape
                    {
                    mUnicodeEscape(); if (failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:506:9: OctalEscape
                    {
                    mOctalEscape(); if (failed) return ;

                    }
                    break;

            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end EscapeSequence

    // $ANTLR start OctalEscape
    public void mOctalEscape() throws RecognitionException {
        try {
            ruleNestingLevel++;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:511:9: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt13=3;
            int LA13_0 = input.LA(1);
            if ( (LA13_0=='\\') ) {
                int LA13_1 = input.LA(2);
                if ( ((LA13_1>='0' && LA13_1<='3')) ) {
                    int LA13_2 = input.LA(3);
                    if ( ((LA13_2>='0' && LA13_2<='7')) ) {
                        int LA13_4 = input.LA(4);
                        if ( ((LA13_4>='0' && LA13_4<='7')) ) {
                            alt13=1;
                        }
                        else {
                            alt13=2;}
                    }
                    else {
                        alt13=3;}
                }
                else if ( ((LA13_1>='4' && LA13_1<='7')) ) {
                    int LA13_3 = input.LA(3);
                    if ( ((LA13_3>='0' && LA13_3<='7')) ) {
                        alt13=2;
                    }
                    else {
                        alt13=3;}
                }
                else {
                    if (backtracking>0) {failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("509:1: fragment OctalEscape : ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) );", 13, 1, input);

                    throw nvae;
                }
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("509:1: fragment OctalEscape : ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) );", 13, 0, input);

                throw nvae;
            }
            switch (alt13) {
                case 1 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:511:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); if (failed) return ;
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:511:14: ( '0' .. '3' )
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:511:15: '0' .. '3'
                    {
                    matchRange('0','3'); if (failed) return ;

                    }

                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:511:25: ( '0' .. '7' )
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:511:26: '0' .. '7'
                    {
                    matchRange('0','7'); if (failed) return ;

                    }

                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:511:36: ( '0' .. '7' )
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:511:37: '0' .. '7'
                    {
                    matchRange('0','7'); if (failed) return ;

                    }


                    }
                    break;
                case 2 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:512:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); if (failed) return ;
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:512:14: ( '0' .. '7' )
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:512:15: '0' .. '7'
                    {
                    matchRange('0','7'); if (failed) return ;

                    }

                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:512:25: ( '0' .. '7' )
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:512:26: '0' .. '7'
                    {
                    matchRange('0','7'); if (failed) return ;

                    }


                    }
                    break;
                case 3 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:513:9: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); if (failed) return ;
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:513:14: ( '0' .. '7' )
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:513:15: '0' .. '7'
                    {
                    matchRange('0','7'); if (failed) return ;

                    }


                    }
                    break;

            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end OctalEscape

    // $ANTLR start UnicodeEscape
    public void mUnicodeEscape() throws RecognitionException {
        try {
            ruleNestingLevel++;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:518:9: ( '\\\\' 'u' HexDigit HexDigit HexDigit HexDigit )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:518:9: '\\\\' 'u' HexDigit HexDigit HexDigit HexDigit
            {
            match('\\'); if (failed) return ;
            match('u'); if (failed) return ;
            mHexDigit(); if (failed) return ;
            mHexDigit(); if (failed) return ;
            mHexDigit(); if (failed) return ;
            mHexDigit(); if (failed) return ;

            }

        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end UnicodeEscape

    // $ANTLR start BOOL
    public void mBOOL() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = BOOL;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:522:4: ( ( 'true' | 'false' ) )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:522:4: ( 'true' | 'false' )
            {
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:522:4: ( 'true' | 'false' )
            int alt14=2;
            int LA14_0 = input.LA(1);
            if ( (LA14_0=='t') ) {
                alt14=1;
            }
            else if ( (LA14_0=='f') ) {
                alt14=2;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("522:4: ( 'true' | 'false' )", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:522:5: 'true'
                    {
                    match("true"); if (failed) return ;


                    }
                    break;
                case 2 :
                    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:522:12: 'false'
                    {
                    match("false"); if (failed) return ;


                    }
                    break;

            }


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end BOOL

    // $ANTLR start SH_STYLE_SINGLE_LINE_COMMENT
    public void mSH_STYLE_SINGLE_LINE_COMMENT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = SH_STYLE_SINGLE_LINE_COMMENT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:526:4: ( '#' ( options {greedy=false; } : . )* EOL )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:526:4: '#' ( options {greedy=false; } : . )* EOL
            {
            match('#'); if (failed) return ;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:526:8: ( options {greedy=false; } : . )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);
                if ( (LA15_0=='\r') ) {
                    alt15=2;
                }
                else if ( (LA15_0=='\n') ) {
                    alt15=2;
                }
                else if ( ((LA15_0>='\u0000' && LA15_0<='\t')||(LA15_0>='\u000B' && LA15_0<='\f')||(LA15_0>='\u000E' && LA15_0<='\uFFFE')) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:526:35: .
            	    {
            	    matchAny(); if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);

            mEOL(); if (failed) return ;
            if ( backtracking==0 ) {
               _channel=HIDDEN; 
            }

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end SH_STYLE_SINGLE_LINE_COMMENT

    // $ANTLR start C_STYLE_SINGLE_LINE_COMMENT
    public void mC_STYLE_SINGLE_LINE_COMMENT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = C_STYLE_SINGLE_LINE_COMMENT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:532:4: ( '//' ( options {greedy=false; } : . )* EOL )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:532:4: '//' ( options {greedy=false; } : . )* EOL
            {
            match("//"); if (failed) return ;

            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:532:9: ( options {greedy=false; } : . )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);
                if ( (LA16_0=='\r') ) {
                    alt16=2;
                }
                else if ( (LA16_0=='\n') ) {
                    alt16=2;
                }
                else if ( ((LA16_0>='\u0000' && LA16_0<='\t')||(LA16_0>='\u000B' && LA16_0<='\f')||(LA16_0>='\u000E' && LA16_0<='\uFFFE')) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:532:36: .
            	    {
            	    matchAny(); if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            mEOL(); if (failed) return ;
            if ( backtracking==0 ) {
               _channel=HIDDEN; 
            }

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end C_STYLE_SINGLE_LINE_COMMENT

    // $ANTLR start LEFT_PAREN
    public void mLEFT_PAREN() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = LEFT_PAREN;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:538:4: ( '(' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:538:4: '('
            {
            match('('); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end LEFT_PAREN

    // $ANTLR start RIGHT_PAREN
    public void mRIGHT_PAREN() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = RIGHT_PAREN;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:542:4: ( ')' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:542:4: ')'
            {
            match(')'); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end RIGHT_PAREN

    // $ANTLR start LEFT_SQUARE
    public void mLEFT_SQUARE() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = LEFT_SQUARE;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:546:4: ( '[' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:546:4: '['
            {
            match('['); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end LEFT_SQUARE

    // $ANTLR start RIGHT_SQUARE
    public void mRIGHT_SQUARE() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = RIGHT_SQUARE;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:550:4: ( ']' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:550:4: ']'
            {
            match(']'); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end RIGHT_SQUARE

    // $ANTLR start LEFT_CURLY
    public void mLEFT_CURLY() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = LEFT_CURLY;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:554:4: ( '{' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:554:4: '{'
            {
            match('{'); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end LEFT_CURLY

    // $ANTLR start RIGHT_CURLY
    public void mRIGHT_CURLY() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = RIGHT_CURLY;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:558:4: ( '}' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:558:4: '}'
            {
            match('}'); if (failed) return ;

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end RIGHT_CURLY

    // $ANTLR start MULTI_LINE_COMMENT
    public void mMULTI_LINE_COMMENT() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = MULTI_LINE_COMMENT;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:562:4: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:562:4: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); if (failed) return ;

            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:562:9: ( options {greedy=false; } : . )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);
                if ( (LA17_0=='*') ) {
                    int LA17_1 = input.LA(2);
                    if ( (LA17_1=='/') ) {
                        alt17=2;
                    }
                    else if ( ((LA17_1>='\u0000' && LA17_1<='.')||(LA17_1>='0' && LA17_1<='\uFFFE')) ) {
                        alt17=1;
                    }


                }
                else if ( ((LA17_0>='\u0000' && LA17_0<=')')||(LA17_0>='+' && LA17_0<='\uFFFE')) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:562:35: .
            	    {
            	    matchAny(); if (failed) return ;

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            match("*/"); if (failed) return ;

            if ( backtracking==0 ) {
               _channel=HIDDEN; 
            }

            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end MULTI_LINE_COMMENT

    // $ANTLR start MISC
    public void mMISC() throws RecognitionException {
        try {
            ruleNestingLevel++;
            int _type = MISC;
            int _start = getCharIndex();
            int _line = getLine();
            int _charPosition = getCharPositionInLine();
            int _channel = Token.DEFAULT_CHANNEL;
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:566:7: ( ('!'|'@'|'$'|'%'|'^'|'&'|'*'|'_'|'-'|'+'|'?'|'|'|','|'='|'/'|'\\''|'\\\\'))
            // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:567:3: ('!'|'@'|'$'|'%'|'^'|'&'|'*'|'_'|'-'|'+'|'?'|'|'|','|'='|'/'|'\\''|'\\\\')
            {
            if ( input.LA(1)=='!'||(input.LA(1)>='$' && input.LA(1)<='\'')||(input.LA(1)>='*' && input.LA(1)<='-')||input.LA(1)=='/'||input.LA(1)=='='||(input.LA(1)>='?' && input.LA(1)<='@')||input.LA(1)=='\\'||(input.LA(1)>='^' && input.LA(1)<='_')||input.LA(1)=='|' ) {
                input.consume();
            failed=false;
            }
            else {
                if (backtracking>0) {failed=true; return ;}
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }


            }


            if ( backtracking==0 ) {

                      if ( token==null && ruleNestingLevel==1 ) {
                          emit(_type,_line,_charPosition,_channel,_start,getCharIndex()-1);
                      }

                      
            }
        }
        finally {
            ruleNestingLevel--;
        }
    }
    // $ANTLR end MISC

    public void mTokens() throws RecognitionException {
        // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:10: ( T27 | T28 | T29 | T30 | T31 | T32 | T33 | T34 | T35 | T36 | T37 | T38 | T39 | VAR | ID | NULL | WS | INT | FLOAT | STRING | BOOL | SH_STYLE_SINGLE_LINE_COMMENT | C_STYLE_SINGLE_LINE_COMMENT | LEFT_PAREN | RIGHT_PAREN | LEFT_SQUARE | RIGHT_SQUARE | LEFT_CURLY | RIGHT_CURLY | MULTI_LINE_COMMENT | MISC )
        int alt18=31;
        alt18 = dfa18.predict(input);
        switch (alt18) {
            case 1 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:10: T27
                {
                mT27(); if (failed) return ;

                }
                break;
            case 2 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:14: T28
                {
                mT28(); if (failed) return ;

                }
                break;
            case 3 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:18: T29
                {
                mT29(); if (failed) return ;

                }
                break;
            case 4 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:22: T30
                {
                mT30(); if (failed) return ;

                }
                break;
            case 5 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:26: T31
                {
                mT31(); if (failed) return ;

                }
                break;
            case 6 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:30: T32
                {
                mT32(); if (failed) return ;

                }
                break;
            case 7 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:34: T33
                {
                mT33(); if (failed) return ;

                }
                break;
            case 8 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:38: T34
                {
                mT34(); if (failed) return ;

                }
                break;
            case 9 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:42: T35
                {
                mT35(); if (failed) return ;

                }
                break;
            case 10 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:46: T36
                {
                mT36(); if (failed) return ;

                }
                break;
            case 11 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:50: T37
                {
                mT37(); if (failed) return ;

                }
                break;
            case 12 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:54: T38
                {
                mT38(); if (failed) return ;

                }
                break;
            case 13 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:58: T39
                {
                mT39(); if (failed) return ;

                }
                break;
            case 14 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:62: VAR
                {
                mVAR(); if (failed) return ;

                }
                break;
            case 15 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:66: ID
                {
                mID(); if (failed) return ;

                }
                break;
            case 16 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:69: NULL
                {
                mNULL(); if (failed) return ;

                }
                break;
            case 17 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:74: WS
                {
                mWS(); if (failed) return ;

                }
                break;
            case 18 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:77: INT
                {
                mINT(); if (failed) return ;

                }
                break;
            case 19 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:81: FLOAT
                {
                mFLOAT(); if (failed) return ;

                }
                break;
            case 20 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:87: STRING
                {
                mSTRING(); if (failed) return ;

                }
                break;
            case 21 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:94: BOOL
                {
                mBOOL(); if (failed) return ;

                }
                break;
            case 22 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:99: SH_STYLE_SINGLE_LINE_COMMENT
                {
                mSH_STYLE_SINGLE_LINE_COMMENT(); if (failed) return ;

                }
                break;
            case 23 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:128: C_STYLE_SINGLE_LINE_COMMENT
                {
                mC_STYLE_SINGLE_LINE_COMMENT(); if (failed) return ;

                }
                break;
            case 24 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:156: LEFT_PAREN
                {
                mLEFT_PAREN(); if (failed) return ;

                }
                break;
            case 25 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:167: RIGHT_PAREN
                {
                mRIGHT_PAREN(); if (failed) return ;

                }
                break;
            case 26 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:179: LEFT_SQUARE
                {
                mLEFT_SQUARE(); if (failed) return ;

                }
                break;
            case 27 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:191: RIGHT_SQUARE
                {
                mRIGHT_SQUARE(); if (failed) return ;

                }
                break;
            case 28 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:204: LEFT_CURLY
                {
                mLEFT_CURLY(); if (failed) return ;

                }
                break;
            case 29 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:215: RIGHT_CURLY
                {
                mRIGHT_CURLY(); if (failed) return ;

                }
                break;
            case 30 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:227: MULTI_LINE_COMMENT
                {
                mMULTI_LINE_COMMENT(); if (failed) return ;

                }
                break;
            case 31 :
                // C:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\clp\\CLP.g:1:246: MISC
                {
                mMISC(); if (failed) return ;

                }
                break;

        }

    }


    protected DFA18 dfa18 = new DFA18(this);
    public static final String DFA18_eotS =
        "\2\uffff\6\36\6\uffff\1\37\1\36\1\uffff\1\37\1\55\1\uffff\1\37\2"+
        "\uffff\1\37\10\uffff\2\36\1\63\5\36\4\uffff\1\36\4\uffff\1\36\1"+
        "\73\1\uffff\1\36\1\75\5\36\3\uffff\1\36\1\uffff\1\104\3\36\2\uffff"+
        "\1\36\1\111\1\112\2\uffff";
    public static final String DFA18_eofS =
        "\113\uffff";
    public static final String DFA18_minS =
        "\1\11\1\uffff\1\145\1\156\1\162\1\157\1\170\1\145\6\uffff\1\44\1"+
        "\141\1\uffff\1\60\1\56\1\uffff\1\0\2\uffff\1\52\10\uffff\1\146\1"+
        "\144\1\60\1\154\1\164\1\151\1\165\1\163\4\uffff\1\154\4\uffff\1"+
        "\162\1\60\1\uffff\1\154\1\60\1\163\1\145\1\164\1\163\1\165\3\uffff"+
        "\1\164\1\uffff\1\60\1\145\1\154\1\163\2\uffff\1\145\2\60\2\uffff";
    public static final String DFA18_maxS =
        "\1\u00ff\1\uffff\1\145\1\156\1\162\1\165\1\170\1\162\6\uffff\1\u00ff"+
        "\1\141\1\uffff\2\71\1\uffff\1\ufffe\2\uffff\1\57\10\uffff\1\146"+
        "\1\144\1\u00ff\1\154\1\164\1\151\1\165\1\163\4\uffff\1\154\4\uffff"+
        "\1\162\1\u00ff\1\uffff\1\154\1\u00ff\1\163\1\145\1\164\1\163\1\165"+
        "\3\uffff\1\164\1\uffff\1\u00ff\1\145\1\154\1\163\2\uffff\1\145\2"+
        "\u00ff\2\uffff";
    public static final String DFA18_acceptS =
        "\1\uffff\1\1\6\uffff\1\10\1\11\1\12\1\13\1\14\1\15\2\uffff\1\21"+
        "\2\uffff\1\24\1\uffff\1\17\1\26\1\uffff\1\30\1\31\1\32\1\33\1\34"+
        "\1\35\1\17\1\37\10\uffff\1\11\1\12\1\15\1\16\1\uffff\1\22\1\23\1"+
        "\36\1\27\2\uffff\1\4\7\uffff\1\3\1\17\1\5\1\uffff\1\17\4\uffff\1"+
        "\7\1\17\3\uffff\1\6\1\2";
    public static final String DFA18_specialS =
        "\113\uffff}>";
    public static final String[] DFA18_transition = {
        "\2\20\1\uffff\2\20\22\uffff\1\20\1\37\1\23\1\26\1\25\1\37\1\11\1"+
        "\24\1\30\1\31\3\37\1\21\1\uffff\1\27\12\22\1\14\1\1\1\10\1\15\1"+
        "\uffff\1\16\1\37\32\36\1\32\1\37\1\33\1\37\1\25\1\uffff\1\3\2\36"+
        "\1\2\1\6\1\17\7\36\1\5\1\4\4\36\1\7\6\36\1\34\1\12\1\35\1\13\101"+
        "\uffff\100\36",
        "",
        "\1\40",
        "\1\41",
        "\1\42",
        "\1\44\5\uffff\1\43",
        "\1\45",
        "\1\47\14\uffff\1\46",
        "",
        "",
        "",
        "",
        "",
        "",
        "\1\53\34\uffff\32\53\4\uffff\1\53\1\uffff\32\53\105\uffff\100\53",
        "\1\54",
        "",
        "\12\22",
        "\1\56\1\uffff\12\22",
        "",
        "\uffff\23",
        "",
        "",
        "\1\57\4\uffff\1\60",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "\1\61",
        "\1\62",
        "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36\105\uffff\100\36",
        "\1\64",
        "\1\65",
        "\1\66",
        "\1\67",
        "\1\70",
        "",
        "",
        "",
        "",
        "\1\71",
        "",
        "",
        "",
        "",
        "\1\72",
        "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36\105\uffff\100\36",
        "",
        "\1\74",
        "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36\105\uffff\100\36",
        "\1\76",
        "\1\77",
        "\1\100",
        "\1\101",
        "\1\102",
        "",
        "",
        "",
        "\1\103",
        "",
        "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36\105\uffff\100\36",
        "\1\105",
        "\1\106",
        "\1\107",
        "",
        "",
        "\1\110",
        "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36\105\uffff\100\36",
        "\12\36\7\uffff\32\36\4\uffff\1\36\1\uffff\32\36\105\uffff\100\36",
        "",
        ""
    };

    class DFA18 extends DFA {
        public DFA18(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 18;
            this.eot = DFA.unpackEncodedString(DFA18_eotS);
            this.eof = DFA.unpackEncodedString(DFA18_eofS);
            this.min = DFA.unpackEncodedStringToUnsignedChars(DFA18_minS);
            this.max = DFA.unpackEncodedStringToUnsignedChars(DFA18_maxS);
            this.accept = DFA.unpackEncodedString(DFA18_acceptS);
            this.special = DFA.unpackEncodedString(DFA18_specialS);
            int numStates = DFA18_transition.length;
            this.transition = new short[numStates][];
            for (int i=0; i<numStates; i++) {
                transition[i] = DFA.unpackEncodedString(DFA18_transition[i]);
            }
        }
        public String getDescription() {
            return "1:1: Tokens : ( T27 | T28 | T29 | T30 | T31 | T32 | T33 | T34 | T35 | T36 | T37 | T38 | T39 | VAR | ID | NULL | WS | INT | FLOAT | STRING | BOOL | SH_STYLE_SINGLE_LINE_COMMENT | C_STYLE_SINGLE_LINE_COMMENT | LEFT_PAREN | RIGHT_PAREN | LEFT_SQUARE | RIGHT_SQUARE | LEFT_CURLY | RIGHT_CURLY | MULTI_LINE_COMMENT | MISC );";
        }
    }
 

}