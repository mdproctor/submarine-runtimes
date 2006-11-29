// $ANTLR 3.0b5 D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g 2006-11-29 22:26:48

	package org.drools.lang;
	import java.util.List;
	import java.util.ArrayList;
	import java.util.Iterator;
	import java.util.Map;	
	import java.util.HashMap;	
	import java.util.StringTokenizer;
	import org.drools.lang.descr.*;
	import org.drools.compiler.SwitchingCommonTokenStream;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class DRLParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ID", "CURLY_CHUNK", "RHS", "INT", "BOOL", "STRING", "FLOAT", "EOL", "WS", "EscapeSequence", "HexDigit", "UnicodeEscape", "OctalEscape", "SH_STYLE_SINGLE_LINE_COMMENT", "C_STYLE_SINGLE_LINE_COMMENT", "NO_CURLY", "LEFT_PAREN", "RIGHT_PAREN", "LEFT_SQUARE", "RIGHT_SQUARE", "NO_PAREN", "MULTI_LINE_COMMENT", "IGNORE", "';'", "'package'", "'import'", "'function'", "'.'", "'.*'", "'global'", "','", "'query'", "'end'", "'template'", "'rule'", "'when'", "':'", "'attributes'", "'salience'", "'no-loop'", "'auto-focus'", "'activation-group'", "'agenda-group'", "'duration'", "'from'", "'accumulate'", "'init'", "'action'", "'result'", "'collect'", "'or'", "'||'", "'&'", "'|'", "'=='", "'>'", "'>='", "'<'", "'<='", "'!='", "'contains'", "'matches'", "'excludes'", "'null'", "'->'", "'and'", "'&&'", "'exists'", "'not'", "'eval'", "'use'"
    };
    public static final int LEFT_PAREN=20;
    public static final int BOOL=8;
    public static final int HexDigit=14;
    public static final int CURLY_CHUNK=5;
    public static final int WS=12;
    public static final int STRING=9;
    public static final int FLOAT=10;
    public static final int NO_CURLY=19;
    public static final int UnicodeEscape=15;
    public static final int EscapeSequence=13;
    public static final int INT=7;
    public static final int EOF=-1;
    public static final int IGNORE=26;
    public static final int RHS=6;
    public static final int EOL=11;
    public static final int LEFT_SQUARE=22;
    public static final int OctalEscape=16;
    public static final int SH_STYLE_SINGLE_LINE_COMMENT=17;
    public static final int MULTI_LINE_COMMENT=25;
    public static final int RIGHT_PAREN=21;
    public static final int RIGHT_SQUARE=23;
    public static final int NO_PAREN=24;
    public static final int ID=4;
    public static final int C_STYLE_SINGLE_LINE_COMMENT=18;

        public DRLParser(TokenStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g"; }

    
    	private ExpanderResolver expanderResolver;
    	private Expander expander;
    	private boolean expanderDebug = false;
    	private PackageDescr packageDescr;
    	private List errors = new ArrayList();
    	private String source = "unknown";
    	private int lineOffset = 0;
    	private DescrFactory factory = new DescrFactory();
    	private boolean parserDebug = false;
    	
    	public void setParserDebug(boolean parserDebug) {
    		this.parserDebug = parserDebug;
    	}
    	
    	public void debug(String message) {
    		if ( parserDebug ) 
    			System.err.println( "drl parser: " + message );
    	}
    	
    	public void setSource(String source) {
    		this.source = source;
    	}
    	public DescrFactory getFactory() {
    		return factory;
    	}	
    
    	/**
    	 * This may be set to enable debuggin of DSLs/expanders.
    	 * If set to true, expander stuff will be sent to the Std out.
    	 */	
    	public void setExpanderDebug(boolean status) {
    		expanderDebug = status;
    	}
    	public String getSource() {
    		return this.source;
    	}
    	
    	public PackageDescr getPackageDescr() {
    		return packageDescr;
    	}
    	
    	private int offset(int line) {
    		return line + lineOffset;
    	}
    	
    	/**
    	 * This will set the offset to record when reparsing. Normally is zero of course 
    	 */
    	public void setLineOffset(int i) {
    	 	this.lineOffset = i;
    	}
    	
    	public void setExpanderResolver(ExpanderResolver expanderResolver) {
    		this.expanderResolver = expanderResolver;
    	}
    	
    	public ExpanderResolver getExpanderResolver() {
    		return expanderResolver;
    	}
    	
    	/** Expand the LHS */
    	private String runWhenExpander(String text, int line) throws RecognitionException {
    		String expanded = text.trim();
    		if (expanded.startsWith(">")) {
    			expanded = expanded.substring(1);  //escape !!
    		} else {
    			try {
    				expanded = expander.expand( "when", text );			
    			} catch (Exception e) {
    				this.errors.add(new ExpanderException("Unable to expand: " + text + ". Due to " + e.getMessage(), line));
    				return "";
    			}
    		}
    		if (expanderDebug) {
    			System.out.println("Expanding LHS: " + text + " ----> " + expanded + " --> from line: " + line);
    		}
    		return expanded;	
    		
    	}
    	
        	/** This will apply a list of constraints to an LHS block */
        	private String applyConstraints(List constraints, String block) {
        		//apply the constraints as a comma seperated list inside the previous block
        		//the block will end in something like "foo()" and the constraint patterns will be put in the ()
        		if (constraints == null) {
        			return block;
        		}
        		StringBuffer list = new StringBuffer();    		
        		for (Iterator iter = constraints.iterator(); iter.hasNext();) {
    				String con = (String) iter.next();
    				list.append("\n\t\t");
    				list.append(con);
    				if (iter.hasNext()) {
    					list.append(",");					
    				}			
    			}
        		if (block.endsWith("()")) {
        			return block.substring(0, block.length() - 2) + "(" + list.toString() + ")";
        		} else {
        			return block + "(" + list.toString() + ")";
        		}
        	}  	
    
            /** Reparse the results of the expansion */
        	private void reparseLhs(String text, AndDescr descrs) throws RecognitionException {
        		CharStream charStream = new ANTLRStringStream( text );
        		DRLLexer lexer = new DRLLexer( charStream );
        		TokenStream tokenStream = new CommonTokenStream( lexer );
        		DRLParser parser = new DRLParser( tokenStream );
        		parser.setLineOffset( descrs.getLine() );
        		parser.normal_lhs_block(descrs);
                
                    if (parser.hasErrors()) {
        			this.errors.addAll(parser.getErrors());
        		}
    		if (expanderDebug) {
    			System.out.println("Reparsing LHS: " + text + " --> successful:" + !parser.hasErrors());
    		}    		
        		
        	}
    	
    	/** Expand a line on the RHS */
    	private String runThenExpander(String text, int startLine) {
    		//System.err.println( "expand THEN [" + text + "]" );
    		StringTokenizer lines = new StringTokenizer( text, "\n\r" );
    
    		StringBuffer expanded = new StringBuffer();
    		
    		String eol = System.getProperty( "line.separator" );
    				
    		while ( lines.hasMoreTokens() ) {
    			startLine++;
    			String line = lines.nextToken();
    			line = line.trim();
    			if ( line.length() > 0 ) {
    				if ( line.startsWith( ">" ) ) {
    					expanded.append( line.substring( 1 ) );
    					expanded.append( eol );
    				} else {
    					try {
    						expanded.append( expander.expand( "then", line ) );
    						expanded.append( eol );
    					} catch (Exception e) {
    						this.errors.add(new ExpanderException("Unable to expand: " + line + ". Due to " + e.getMessage(), startLine));			
    					}
    				}
    			}
    		}
    		
    		if (expanderDebug) {
    			System.out.println("Expanding RHS: " + text + " ----> " + expanded.toString() + " --> from line starting: " + startLine);
    		}		
    		
    		return expanded.toString();
    	}
    	
    
    	
    	private String getString(Token token) {
    		String orig = token.getText();
    		return orig.substring( 1, orig.length() -1 );
    	}
    	
    	public void reportError(RecognitionException ex) {
    	        // if we've already reported an error and have not matched a token
                    // yet successfully, don't report any errors.
                    if ( errorRecovery ) {
                            return;
                    }
                    errorRecovery = true;
    
    		ex.line = offset(ex.line); //add the offset if there is one
    		errors.add( ex ); 
    	}
         	
         	/** return the raw RecognitionException errors */
         	public List getErrors() {
         		return errors;
         	}
         	
         	/** Return a list of pretty strings summarising the errors */
         	public List getErrorMessages() {
         		List messages = new ArrayList();
     		for ( Iterator errorIter = errors.iterator() ; errorIter.hasNext() ; ) {
         	     		messages.add( createErrorMessage( (RecognitionException) errorIter.next() ) );
         	     	}
         	     	return messages;
         	}
         	
         	/** return true if any parser errors were accumulated */
         	public boolean hasErrors() {
      		return ! errors.isEmpty();
         	}
         	
         	/** This will take a RecognitionException, and create a sensible error message out of it */
         	public String createErrorMessage(RecognitionException e)
            {
    		StringBuffer message = new StringBuffer();		
                    message.append( source + ":"+e.line+":"+e.charPositionInLine+" ");
                    if ( e instanceof MismatchedTokenException ) {
                            MismatchedTokenException mte = (MismatchedTokenException)e;
                            message.append("mismatched token: "+
                                                               e.token+
                                                               "; expecting type "+
                                                               tokenNames[mte.expecting]);
                    }
                    else if ( e instanceof MismatchedTreeNodeException ) {
                            MismatchedTreeNodeException mtne = (MismatchedTreeNodeException)e;
                            message.append("mismatched tree node: "+
                                                               mtne.foundNode+
                                                               "; expecting type "+
                                                               tokenNames[mtne.expecting]);
                    }
                    else if ( e instanceof NoViableAltException ) {
                            NoViableAltException nvae = (NoViableAltException)e;
    			message.append( "Unexpected token '" + e.token.getText() + "'" );
                            /*
                            message.append("decision=<<"+nvae.grammarDecisionDescription+">>"+
                                                               " state "+nvae.stateNumber+
                                                               " (decision="+nvae.decisionNumber+
                                                               ") no viable alt; token="+
                                                               e.token);
                                                               */
                    }
                    else if ( e instanceof EarlyExitException ) {
                            EarlyExitException eee = (EarlyExitException)e;
                            message.append("required (...)+ loop (decision="+
                                                               eee.decisionNumber+
                                                               ") did not match anything; token="+
                                                               e.token);
                    }
                    else if ( e instanceof MismatchedSetException ) {
                            MismatchedSetException mse = (MismatchedSetException)e;
                            message.append("mismatched token '"+
                                                               e.token+
                                                               "' expecting set "+mse.expecting);
                    }
                    else if ( e instanceof MismatchedNotSetException ) {
                            MismatchedNotSetException mse = (MismatchedNotSetException)e;
                            message.append("mismatched token '"+
                                                               e.token+
                                                               "' expecting set "+mse.expecting);
                    }
                    else if ( e instanceof FailedPredicateException ) {
                            FailedPredicateException fpe = (FailedPredicateException)e;
                            message.append("rule "+fpe.ruleName+" failed predicate: {"+
                                                               fpe.predicateText+"}?");
                    } else if (e instanceof GeneralParseException) {
    			message.append(" " + e.getMessage());
    		}
                   	return message.toString();
            }   
            
            void checkTrailingSemicolon(String text, int line) {
            	if (text.trim().endsWith( ";" ) ) {
            		this.errors.add( new GeneralParseException( "Trailing semi-colon not allowed", offset(line) ) );
            	}
            }
          



    // $ANTLR start opt_semicolon
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:281:1: opt_semicolon : ( ';' )? ;
    public void opt_semicolon() throws RecognitionException {   
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:282:4: ( ( ';' )? )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:282:4: ( ';' )?
            {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:282:4: ( ';' )?
            int alt1=2;
            int LA1_0 = input.LA(1);
            if ( (LA1_0==27) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:282:4: ';'
                    {
                    match(input,27,FOLLOW_27_in_opt_semicolon39); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ;
    }
    // $ANTLR end opt_semicolon


    // $ANTLR start compilation_unit
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:285:1: compilation_unit : prolog ( statement )* ;
    public void compilation_unit() throws RecognitionException {   
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:286:4: ( prolog ( statement )* )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:286:4: prolog ( statement )*
            {
            pushFollow(FOLLOW_prolog_in_compilation_unit51);
            prolog();
            _fsp--;

            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:287:3: ( statement )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);
                if ( ((LA2_0>=29 && LA2_0<=30)||LA2_0==33||LA2_0==35||(LA2_0>=37 && LA2_0<=38)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:287:5: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_compilation_unit58);
            	    statement();
            	    _fsp--;


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ;
    }
    // $ANTLR end compilation_unit


    // $ANTLR start prolog
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:290:1: prolog : (name= package_statement )? ;
    public void prolog() throws RecognitionException {   
        String name = null;


        
        		String packageName = "";
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:294:4: ( (name= package_statement )? )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:294:4: (name= package_statement )?
            {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:294:4: (name= package_statement )?
            int alt3=2;
            int LA3_0 = input.LA(1);
            if ( (LA3_0==28) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:294:6: name= package_statement
                    {
                    pushFollow(FOLLOW_package_statement_in_prolog83);
                    name=package_statement();
                    _fsp--;

                     packageName = name; 

                    }
                    break;

            }

             
            			this.packageDescr = new PackageDescr( name ); 
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ;
    }
    // $ANTLR end prolog


    // $ANTLR start statement
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:300:1: statement : ( import_statement | function_import_statement | global | function | t= template | r= rule | q= query ) ;
    public void statement() throws RecognitionException {   
        FactTemplateDescr t = null;

        RuleDescr r = null;

        QueryDescr q = null;


        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:302:2: ( ( import_statement | function_import_statement | global | function | t= template | r= rule | q= query ) )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:302:2: ( import_statement | function_import_statement | global | function | t= template | r= rule | q= query )
            {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:302:2: ( import_statement | function_import_statement | global | function | t= template | r= rule | q= query )
            int alt4=7;
            switch ( input.LA(1) ) {
            case 29:
                int LA4_1 = input.LA(2);
                if ( (LA4_1==30) ) {
                    alt4=2;
                }
                else if ( (LA4_1==ID) ) {
                    alt4=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("302:2: ( import_statement | function_import_statement | global | function | t= template | r= rule | q= query )", 4, 1, input);

                    throw nvae;
                }
                break;
            case 33:
                alt4=3;
                break;
            case 30:
                alt4=4;
                break;
            case 37:
                alt4=5;
                break;
            case 38:
                alt4=6;
                break;
            case 35:
                alt4=7;
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("302:2: ( import_statement | function_import_statement | global | function | t= template | r= rule | q= query )", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:302:4: import_statement
                    {
                    pushFollow(FOLLOW_import_statement_in_statement107);
                    import_statement();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:303:10: function_import_statement
                    {
                    pushFollow(FOLLOW_function_import_statement_in_statement118);
                    function_import_statement();
                    _fsp--;


                    }
                    break;
                case 3 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:304:4: global
                    {
                    pushFollow(FOLLOW_global_in_statement123);
                    global();
                    _fsp--;


                    }
                    break;
                case 4 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:305:4: function
                    {
                    pushFollow(FOLLOW_function_in_statement128);
                    function();
                    _fsp--;


                    }
                    break;
                case 5 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:306:10: t= template
                    {
                    pushFollow(FOLLOW_template_in_statement141);
                    t=template();
                    _fsp--;

                    this.packageDescr.addFactTemplate( t ); 

                    }
                    break;
                case 6 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:307:4: r= rule
                    {
                    pushFollow(FOLLOW_rule_in_statement150);
                    r=rule();
                    _fsp--;

                    this.packageDescr.addRule( r ); 

                    }
                    break;
                case 7 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:308:4: q= query
                    {
                    pushFollow(FOLLOW_query_in_statement160);
                    q=query();
                    _fsp--;

                    this.packageDescr.addRule( q ); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ;
    }
    // $ANTLR end statement


    // $ANTLR start package_statement
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:312:1: package_statement returns [String packageName] : 'package' name= dotted_name opt_semicolon ;
    public String package_statement() throws RecognitionException {   
        String packageName = null;

        String name = null;


        
        		packageName = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:317:3: ( 'package' name= dotted_name opt_semicolon )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:317:3: 'package' name= dotted_name opt_semicolon
            {
            match(input,28,FOLLOW_28_in_package_statement188); 
            pushFollow(FOLLOW_dotted_name_in_package_statement192);
            name=dotted_name();
            _fsp--;

            pushFollow(FOLLOW_opt_semicolon_in_package_statement194);
            opt_semicolon();
            _fsp--;

            
            			packageName = name;
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return packageName;
    }
    // $ANTLR end package_statement


    // $ANTLR start import_statement
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:324:1: import_statement : 'import' name= import_name opt_semicolon ;
    public void import_statement() throws RecognitionException {   
        String name = null;


        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:325:4: ( 'import' name= import_name opt_semicolon )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:325:4: 'import' name= import_name opt_semicolon
            {
            match(input,29,FOLLOW_29_in_import_statement211); 
            pushFollow(FOLLOW_import_name_in_import_statement215);
            name=import_name();
            _fsp--;

            pushFollow(FOLLOW_opt_semicolon_in_import_statement217);
            opt_semicolon();
            _fsp--;

            
            			if (packageDescr != null) 
            				packageDescr.addImport( name );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ;
    }
    // $ANTLR end import_statement


    // $ANTLR start function_import_statement
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:332:1: function_import_statement : 'import' 'function' name= import_name opt_semicolon ;
    public void function_import_statement() throws RecognitionException {   
        String name = null;


        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:333:4: ( 'import' 'function' name= import_name opt_semicolon )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:333:4: 'import' 'function' name= import_name opt_semicolon
            {
            match(input,29,FOLLOW_29_in_function_import_statement233); 
            match(input,30,FOLLOW_30_in_function_import_statement235); 
            pushFollow(FOLLOW_import_name_in_function_import_statement239);
            name=import_name();
            _fsp--;

            pushFollow(FOLLOW_opt_semicolon_in_function_import_statement241);
            opt_semicolon();
            _fsp--;

            
            			if (packageDescr != null) 
            				packageDescr.addFunctionImport( name );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ;
    }
    // $ANTLR end function_import_statement


    // $ANTLR start import_name
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:341:1: import_name returns [String name] : id= ID ( '.' id= ID )* (star= '.*' )? ;
    public String import_name() throws RecognitionException {   
        String name = null;

        Token id=null;
        Token star=null;

        
        		name = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:346:3: (id= ID ( '.' id= ID )* (star= '.*' )? )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:346:3: id= ID ( '.' id= ID )* (star= '.*' )?
            {
            id=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_import_name273); 
             name=id.getText(); 
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:346:32: ( '.' id= ID )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);
                if ( (LA5_0==31) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:346:34: '.' id= ID
            	    {
            	    match(input,31,FOLLOW_31_in_import_name279); 
            	    id=(Token)input.LT(1);
            	    match(input,ID,FOLLOW_ID_in_import_name283); 
            	     name = name + "." + id.getText(); 

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:346:85: (star= '.*' )?
            int alt6=2;
            int LA6_0 = input.LA(1);
            if ( (LA6_0==32) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:346:86: star= '.*'
                    {
                    star=(Token)input.LT(1);
                    match(input,32,FOLLOW_32_in_import_name293); 
                     name = name + star.getText(); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return name;
    }
    // $ANTLR end import_name


    // $ANTLR start global
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:350:1: global : 'global' type= dotted_name id= ID opt_semicolon ;
    public void global() throws RecognitionException {   
        Token id=null;
        String type = null;


        
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:354:3: ( 'global' type= dotted_name id= ID opt_semicolon )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:354:3: 'global' type= dotted_name id= ID opt_semicolon
            {
            match(input,33,FOLLOW_33_in_global317); 
            pushFollow(FOLLOW_dotted_name_in_global321);
            type=dotted_name();
            _fsp--;

            id=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_global325); 
            pushFollow(FOLLOW_opt_semicolon_in_global327);
            opt_semicolon();
            _fsp--;

            
            			packageDescr.addGlobal( id.getText(), type );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ;
    }
    // $ANTLR end global


    // $ANTLR start function
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:361:1: function : loc= 'function' (retType= dotted_name )? name= ID '(' ( (paramType= dotted_name )? paramName= argument ( ',' (paramType= dotted_name )? paramName= argument )* )? ')' body= CURLY_CHUNK ;
    public void function() throws RecognitionException {   
        Token loc=null;
        Token name=null;
        Token body=null;
        String retType = null;

        String paramType = null;

        String paramName = null;


        
        		FunctionDescr f = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:366:3: (loc= 'function' (retType= dotted_name )? name= ID '(' ( (paramType= dotted_name )? paramName= argument ( ',' (paramType= dotted_name )? paramName= argument )* )? ')' body= CURLY_CHUNK )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:366:3: loc= 'function' (retType= dotted_name )? name= ID '(' ( (paramType= dotted_name )? paramName= argument ( ',' (paramType= dotted_name )? paramName= argument )* )? ')' body= CURLY_CHUNK
            {
            loc=(Token)input.LT(1);
            match(input,30,FOLLOW_30_in_function354); 
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:366:18: (retType= dotted_name )?
            int alt7=2;
            int LA7_0 = input.LA(1);
            if ( (LA7_0==ID) ) {
                int LA7_1 = input.LA(2);
                if ( (LA7_1==ID||LA7_1==LEFT_SQUARE||LA7_1==31) ) {
                    alt7=1;
                }
            }
            switch (alt7) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:366:19: retType= dotted_name
                    {
                    pushFollow(FOLLOW_dotted_name_in_function359);
                    retType=dotted_name();
                    _fsp--;


                    }
                    break;

            }

            name=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_function365); 
            
            			//System.err.println( "function :: " + name.getText() );
            			f = new FunctionDescr( name.getText(), retType );
            			f.setLocation(offset(loc.getLine()), loc.getCharPositionInLine());
            		
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_function374); 
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:373:4: ( (paramType= dotted_name )? paramName= argument ( ',' (paramType= dotted_name )? paramName= argument )* )?
            int alt11=2;
            int LA11_0 = input.LA(1);
            if ( (LA11_0==ID) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:373:6: (paramType= dotted_name )? paramName= argument ( ',' (paramType= dotted_name )? paramName= argument )*
                    {
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:373:6: (paramType= dotted_name )?
                    int alt8=2;
                    alt8 = dfa8.predict(input);
                    switch (alt8) {
                        case 1 :
                            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:373:7: paramType= dotted_name
                            {
                            pushFollow(FOLLOW_dotted_name_in_function384);
                            paramType=dotted_name();
                            _fsp--;


                            }
                            break;

                    }

                    pushFollow(FOLLOW_argument_in_function390);
                    paramName=argument();
                    _fsp--;

                    
                    					f.addParameter( paramType, paramName );
                    				
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:377:5: ( ',' (paramType= dotted_name )? paramName= argument )*
                    loop10:
                    do {
                        int alt10=2;
                        int LA10_0 = input.LA(1);
                        if ( (LA10_0==34) ) {
                            alt10=1;
                        }


                        switch (alt10) {
                    	case 1 :
                    	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:377:7: ',' (paramType= dotted_name )? paramName= argument
                    	    {
                    	    match(input,34,FOLLOW_34_in_function404); 
                    	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:377:11: (paramType= dotted_name )?
                    	    int alt9=2;
                    	    alt9 = dfa9.predict(input);
                    	    switch (alt9) {
                    	        case 1 :
                    	            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:377:12: paramType= dotted_name
                    	            {
                    	            pushFollow(FOLLOW_dotted_name_in_function409);
                    	            paramType=dotted_name();
                    	            _fsp--;


                    	            }
                    	            break;

                    	    }

                    	    pushFollow(FOLLOW_argument_in_function415);
                    	    paramName=argument();
                    	    _fsp--;

                    	    
                    	    						f.addParameter( paramType, paramName );
                    	    					

                    	    }
                    	    break;

                    	default :
                    	    break loop10;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_function439); 
            body=(Token)input.LT(1);
            match(input,CURLY_CHUNK,FOLLOW_CURLY_CHUNK_in_function445); 
            
            			//strip out '{','}'
            			String bodys = body.getText();
            			bodys = bodys.substring(1,bodys.length()-1);
            			f.setText( bodys );
            
            			packageDescr.addFunction( f );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ;
    }
    // $ANTLR end function


    // $ANTLR start query
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:397:1: query returns [QueryDescr query] : loc= 'query' queryName= word ( normal_lhs_block[lhs] ) 'end' ;
    public QueryDescr query() throws RecognitionException {   
        QueryDescr query = null;

        Token loc=null;
        String queryName = null;


        
        		query = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:402:3: (loc= 'query' queryName= word ( normal_lhs_block[lhs] ) 'end' )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:402:3: loc= 'query' queryName= word ( normal_lhs_block[lhs] ) 'end'
            {
            loc=(Token)input.LT(1);
            match(input,35,FOLLOW_35_in_query476); 
            pushFollow(FOLLOW_word_in_query480);
            queryName=word();
            _fsp--;

             
            			query = new QueryDescr( queryName, null ); 
            			query.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            			AndDescr lhs = new AndDescr(); query.setLhs( lhs ); 
            			lhs.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            		
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:409:3: ( normal_lhs_block[lhs] )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:411:4: normal_lhs_block[lhs]
            {
            pushFollow(FOLLOW_normal_lhs_block_in_query494);
            normal_lhs_block(lhs);
            _fsp--;


            }

            match(input,36,FOLLOW_36_in_query509); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return query;
    }
    // $ANTLR end query


    // $ANTLR start template
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:418:1: template returns [FactTemplateDescr template] : loc= 'template' templateName= ID opt_semicolon (slot= template_slot )+ 'end' opt_semicolon ;
    public FactTemplateDescr template() throws RecognitionException {   
        FactTemplateDescr template = null;

        Token loc=null;
        Token templateName=null;
        FieldTemplateDescr slot = null;


        
        		template = null;		
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:423:3: (loc= 'template' templateName= ID opt_semicolon (slot= template_slot )+ 'end' opt_semicolon )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:423:3: loc= 'template' templateName= ID opt_semicolon (slot= template_slot )+ 'end' opt_semicolon
            {
            loc=(Token)input.LT(1);
            match(input,37,FOLLOW_37_in_template535); 
            templateName=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_template539); 
            pushFollow(FOLLOW_opt_semicolon_in_template541);
            opt_semicolon();
            _fsp--;

            
            			template = new FactTemplateDescr(templateName.getText());
            			template.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );			
            		
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:428:3: (slot= template_slot )+
            int cnt12=0;
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);
                if ( (LA12_0==ID) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:429:4: slot= template_slot
            	    {
            	    pushFollow(FOLLOW_template_slot_in_template556);
            	    slot=template_slot();
            	    _fsp--;

            	    
            	    				template.addFieldTemplate(slot);
            	    			

            	    }
            	    break;

            	default :
            	    if ( cnt12 >= 1 ) break loop12;
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
            } while (true);

            match(input,36,FOLLOW_36_in_template571); 
            pushFollow(FOLLOW_opt_semicolon_in_template573);
            opt_semicolon();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return template;
    }
    // $ANTLR end template


    // $ANTLR start template_slot
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:437:1: template_slot returns [FieldTemplateDescr field] : fieldType= dotted_name name= ID opt_semicolon ;
    public FieldTemplateDescr template_slot() throws RecognitionException {   
        FieldTemplateDescr field = null;

        Token name=null;
        String fieldType = null;


        
        		field = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:443:4: (fieldType= dotted_name name= ID opt_semicolon )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:443:4: fieldType= dotted_name name= ID opt_semicolon
            {
            pushFollow(FOLLOW_dotted_name_in_template_slot605);
            fieldType=dotted_name();
            _fsp--;

            name=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_template_slot609); 
            pushFollow(FOLLOW_opt_semicolon_in_template_slot611);
            opt_semicolon();
            _fsp--;

            
            			
            			
            			field = new FieldTemplateDescr(name.getText(), fieldType);
            			field.setLocation( offset(name.getLine()), name.getCharPositionInLine() );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return field;
    }
    // $ANTLR end template_slot


    // $ANTLR start rule
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:452:1: rule returns [RuleDescr rule] : loc= 'rule' ruleName= word rule_attributes[rule] (loc= 'when' ( ':' )? ( normal_lhs_block[lhs] ) )? rhs= RHS ;
    public RuleDescr rule() throws RecognitionException {   
        RuleDescr rule = null;

        Token loc=null;
        Token rhs=null;
        String ruleName = null;


        
        		rule = null;
        		String consequence = "";
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:458:3: (loc= 'rule' ruleName= word rule_attributes[rule] (loc= 'when' ( ':' )? ( normal_lhs_block[lhs] ) )? rhs= RHS )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:458:3: loc= 'rule' ruleName= word rule_attributes[rule] (loc= 'when' ( ':' )? ( normal_lhs_block[lhs] ) )? rhs= RHS
            {
            loc=(Token)input.LT(1);
            match(input,38,FOLLOW_38_in_rule642); 
            pushFollow(FOLLOW_word_in_rule646);
            ruleName=word();
            _fsp--;

             
            			debug( "start rule: " + ruleName );
            			rule = new RuleDescr( ruleName, null ); 
            			rule.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            		
            pushFollow(FOLLOW_rule_attributes_in_rule655);
            rule_attributes(rule);
            _fsp--;

            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:465:3: (loc= 'when' ( ':' )? ( normal_lhs_block[lhs] ) )?
            int alt14=2;
            int LA14_0 = input.LA(1);
            if ( (LA14_0==39) ) {
                alt14=1;
            }
            switch (alt14) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:465:5: loc= 'when' ( ':' )? ( normal_lhs_block[lhs] )
                    {
                    loc=(Token)input.LT(1);
                    match(input,39,FOLLOW_39_in_rule664); 
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:465:16: ( ':' )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);
                    if ( (LA13_0==40) ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:465:16: ':'
                            {
                            match(input,40,FOLLOW_40_in_rule666); 

                            }
                            break;

                    }

                     
                    				AndDescr lhs = new AndDescr(); rule.setLhs( lhs ); 
                    				lhs.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
                    			
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:470:4: ( normal_lhs_block[lhs] )
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:472:5: normal_lhs_block[lhs]
                    {
                    pushFollow(FOLLOW_normal_lhs_block_in_rule684);
                    normal_lhs_block(lhs);
                    _fsp--;


                    }


                    }
                    break;

            }

            rhs=(Token)input.LT(1);
            match(input,RHS,FOLLOW_RHS_in_rule707); 
            
            				consequence = rhs.getText();
            				//strip out "then", "end"
            				consequence = consequence.substring(4,consequence.length()-3);
            				
            				if ( expander != null ) {
            					String expanded = runThenExpander( consequence, offset(rhs.getLine()) );
            					rule.setConsequence( expanded );
            				} else { 
            					rule.setConsequence( consequence ); 
            				}
            				rule.setConsequenceLocation(offset(rhs.getLine()), rhs.getCharPositionInLine());
            				debug( "end rule: " + ruleName );
            			

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return rule;
    }
    // $ANTLR end rule


    // $ANTLR start rule_attributes
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:495:1: rule_attributes[RuleDescr rule] : ( 'attributes' ':' )? ( ( ',' )? a= rule_attribute )* ;
    public void rule_attributes(RuleDescr rule) throws RecognitionException {   
        AttributeDescr a = null;


        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:497:4: ( ( 'attributes' ':' )? ( ( ',' )? a= rule_attribute )* )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:497:4: ( 'attributes' ':' )? ( ( ',' )? a= rule_attribute )*
            {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:497:4: ( 'attributes' ':' )?
            int alt15=2;
            int LA15_0 = input.LA(1);
            if ( (LA15_0==41) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:497:5: 'attributes' ':'
                    {
                    match(input,41,FOLLOW_41_in_rule_attributes732); 
                    match(input,40,FOLLOW_40_in_rule_attributes734); 

                    }
                    break;

            }

            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:498:4: ( ( ',' )? a= rule_attribute )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);
                if ( (LA17_0==34||(LA17_0>=42 && LA17_0<=47)) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:498:6: ( ',' )? a= rule_attribute
            	    {
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:498:6: ( ',' )?
            	    int alt16=2;
            	    int LA16_0 = input.LA(1);
            	    if ( (LA16_0==34) ) {
            	        alt16=1;
            	    }
            	    switch (alt16) {
            	        case 1 :
            	            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:498:6: ','
            	            {
            	            match(input,34,FOLLOW_34_in_rule_attributes743); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_rule_attribute_in_rule_attributes748);
            	    a=rule_attribute();
            	    _fsp--;

            	    
            	    					rule.addAttribute( a );
            	    				

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ;
    }
    // $ANTLR end rule_attributes


    // $ANTLR start rule_attribute
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:507:1: rule_attribute returns [AttributeDescr d] : (a= salience | a= no_loop | a= agenda_group | a= duration | a= activation_group | a= auto_focus );
    public AttributeDescr rule_attribute() throws RecognitionException {   
        AttributeDescr d = null;

        AttributeDescr a = null;


        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:512:4: (a= salience | a= no_loop | a= agenda_group | a= duration | a= activation_group | a= auto_focus )
            int alt18=6;
            switch ( input.LA(1) ) {
            case 42:
                alt18=1;
                break;
            case 43:
                alt18=2;
                break;
            case 46:
                alt18=3;
                break;
            case 47:
                alt18=4;
                break;
            case 45:
                alt18=5;
                break;
            case 44:
                alt18=6;
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("507:1: rule_attribute returns [AttributeDescr d] : (a= salience | a= no_loop | a= agenda_group | a= duration | a= activation_group | a= auto_focus );", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:512:4: a= salience
                    {
                    pushFollow(FOLLOW_salience_in_rule_attribute789);
                    a=salience();
                    _fsp--;

                     d = a; 

                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:513:5: a= no_loop
                    {
                    pushFollow(FOLLOW_no_loop_in_rule_attribute799);
                    a=no_loop();
                    _fsp--;

                     d = a; 

                    }
                    break;
                case 3 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:514:5: a= agenda_group
                    {
                    pushFollow(FOLLOW_agenda_group_in_rule_attribute810);
                    a=agenda_group();
                    _fsp--;

                     d = a; 

                    }
                    break;
                case 4 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:515:5: a= duration
                    {
                    pushFollow(FOLLOW_duration_in_rule_attribute823);
                    a=duration();
                    _fsp--;

                     d = a; 

                    }
                    break;
                case 5 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:516:5: a= activation_group
                    {
                    pushFollow(FOLLOW_activation_group_in_rule_attribute837);
                    a=activation_group();
                    _fsp--;

                     d = a; 

                    }
                    break;
                case 6 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:517:5: a= auto_focus
                    {
                    pushFollow(FOLLOW_auto_focus_in_rule_attribute848);
                    a=auto_focus();
                    _fsp--;

                     d = a; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end rule_attribute


    // $ANTLR start salience
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:522:1: salience returns [AttributeDescr d ] : loc= 'salience' i= INT opt_semicolon ;
    public AttributeDescr salience() throws RecognitionException {   
        AttributeDescr d = null;

        Token loc=null;
        Token i=null;

        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:527:3: (loc= 'salience' i= INT opt_semicolon )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:527:3: loc= 'salience' i= INT opt_semicolon
            {
            loc=(Token)input.LT(1);
            match(input,42,FOLLOW_42_in_salience882); 
            i=(Token)input.LT(1);
            match(input,INT,FOLLOW_INT_in_salience886); 
            pushFollow(FOLLOW_opt_semicolon_in_salience888);
            opt_semicolon();
            _fsp--;

            
            			d = new AttributeDescr( "salience", i.getText() );
            			d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end salience


    // $ANTLR start no_loop
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:534:1: no_loop returns [AttributeDescr d] : ( (loc= 'no-loop' opt_semicolon ) | (loc= 'no-loop' t= BOOL opt_semicolon ) );
    public AttributeDescr no_loop() throws RecognitionException {   
        AttributeDescr d = null;

        Token loc=null;
        Token t=null;

        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:539:3: ( (loc= 'no-loop' opt_semicolon ) | (loc= 'no-loop' t= BOOL opt_semicolon ) )
            int alt19=2;
            int LA19_0 = input.LA(1);
            if ( (LA19_0==43) ) {
                int LA19_1 = input.LA(2);
                if ( (LA19_1==BOOL) ) {
                    alt19=2;
                }
                else if ( (LA19_1==RHS||LA19_1==27||LA19_1==34||LA19_1==39||(LA19_1>=42 && LA19_1<=47)) ) {
                    alt19=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("534:1: no_loop returns [AttributeDescr d] : ( (loc= 'no-loop' opt_semicolon ) | (loc= 'no-loop' t= BOOL opt_semicolon ) );", 19, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("534:1: no_loop returns [AttributeDescr d] : ( (loc= 'no-loop' opt_semicolon ) | (loc= 'no-loop' t= BOOL opt_semicolon ) );", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:539:3: (loc= 'no-loop' opt_semicolon )
                    {
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:539:3: (loc= 'no-loop' opt_semicolon )
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:540:4: loc= 'no-loop' opt_semicolon
                    {
                    loc=(Token)input.LT(1);
                    match(input,43,FOLLOW_43_in_no_loop923); 
                    pushFollow(FOLLOW_opt_semicolon_in_no_loop925);
                    opt_semicolon();
                    _fsp--;

                    
                    				d = new AttributeDescr( "no-loop", "true" );
                    				d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
                    			

                    }


                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:547:3: (loc= 'no-loop' t= BOOL opt_semicolon )
                    {
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:547:3: (loc= 'no-loop' t= BOOL opt_semicolon )
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:548:4: loc= 'no-loop' t= BOOL opt_semicolon
                    {
                    loc=(Token)input.LT(1);
                    match(input,43,FOLLOW_43_in_no_loop950); 
                    t=(Token)input.LT(1);
                    match(input,BOOL,FOLLOW_BOOL_in_no_loop954); 
                    pushFollow(FOLLOW_opt_semicolon_in_no_loop956);
                    opt_semicolon();
                    _fsp--;

                    
                    				d = new AttributeDescr( "no-loop", t.getText() );
                    				d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
                    			

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end no_loop


    // $ANTLR start auto_focus
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:558:1: auto_focus returns [AttributeDescr d] : ( (loc= 'auto-focus' opt_semicolon ) | (loc= 'auto-focus' t= BOOL opt_semicolon ) );
    public AttributeDescr auto_focus() throws RecognitionException {   
        AttributeDescr d = null;

        Token loc=null;
        Token t=null;

        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:563:3: ( (loc= 'auto-focus' opt_semicolon ) | (loc= 'auto-focus' t= BOOL opt_semicolon ) )
            int alt20=2;
            int LA20_0 = input.LA(1);
            if ( (LA20_0==44) ) {
                int LA20_1 = input.LA(2);
                if ( (LA20_1==BOOL) ) {
                    alt20=2;
                }
                else if ( (LA20_1==RHS||LA20_1==27||LA20_1==34||LA20_1==39||(LA20_1>=42 && LA20_1<=47)) ) {
                    alt20=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("558:1: auto_focus returns [AttributeDescr d] : ( (loc= 'auto-focus' opt_semicolon ) | (loc= 'auto-focus' t= BOOL opt_semicolon ) );", 20, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("558:1: auto_focus returns [AttributeDescr d] : ( (loc= 'auto-focus' opt_semicolon ) | (loc= 'auto-focus' t= BOOL opt_semicolon ) );", 20, 0, input);

                throw nvae;
            }
            switch (alt20) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:563:3: (loc= 'auto-focus' opt_semicolon )
                    {
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:563:3: (loc= 'auto-focus' opt_semicolon )
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:564:4: loc= 'auto-focus' opt_semicolon
                    {
                    loc=(Token)input.LT(1);
                    match(input,44,FOLLOW_44_in_auto_focus1002); 
                    pushFollow(FOLLOW_opt_semicolon_in_auto_focus1004);
                    opt_semicolon();
                    _fsp--;

                    
                    				d = new AttributeDescr( "auto-focus", "true" );
                    				d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
                    			

                    }


                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:571:3: (loc= 'auto-focus' t= BOOL opt_semicolon )
                    {
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:571:3: (loc= 'auto-focus' t= BOOL opt_semicolon )
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:572:4: loc= 'auto-focus' t= BOOL opt_semicolon
                    {
                    loc=(Token)input.LT(1);
                    match(input,44,FOLLOW_44_in_auto_focus1029); 
                    t=(Token)input.LT(1);
                    match(input,BOOL,FOLLOW_BOOL_in_auto_focus1033); 
                    pushFollow(FOLLOW_opt_semicolon_in_auto_focus1035);
                    opt_semicolon();
                    _fsp--;

                    
                    				d = new AttributeDescr( "auto-focus", t.getText() );
                    				d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
                    			

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end auto_focus


    // $ANTLR start activation_group
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:582:1: activation_group returns [AttributeDescr d] : loc= 'activation-group' name= STRING opt_semicolon ;
    public AttributeDescr activation_group() throws RecognitionException {   
        AttributeDescr d = null;

        Token loc=null;
        Token name=null;

        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:587:3: (loc= 'activation-group' name= STRING opt_semicolon )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:587:3: loc= 'activation-group' name= STRING opt_semicolon
            {
            loc=(Token)input.LT(1);
            match(input,45,FOLLOW_45_in_activation_group1077); 
            name=(Token)input.LT(1);
            match(input,STRING,FOLLOW_STRING_in_activation_group1081); 
            pushFollow(FOLLOW_opt_semicolon_in_activation_group1083);
            opt_semicolon();
            _fsp--;

            
            			d = new AttributeDescr( "activation-group", getString( name ) );
            			d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end activation_group


    // $ANTLR start agenda_group
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:594:1: agenda_group returns [AttributeDescr d] : loc= 'agenda-group' name= STRING opt_semicolon ;
    public AttributeDescr agenda_group() throws RecognitionException {   
        AttributeDescr d = null;

        Token loc=null;
        Token name=null;

        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:599:3: (loc= 'agenda-group' name= STRING opt_semicolon )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:599:3: loc= 'agenda-group' name= STRING opt_semicolon
            {
            loc=(Token)input.LT(1);
            match(input,46,FOLLOW_46_in_agenda_group1112); 
            name=(Token)input.LT(1);
            match(input,STRING,FOLLOW_STRING_in_agenda_group1116); 
            pushFollow(FOLLOW_opt_semicolon_in_agenda_group1118);
            opt_semicolon();
            _fsp--;

            
            			d = new AttributeDescr( "agenda-group", getString( name ) );
            			d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end agenda_group


    // $ANTLR start duration
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:607:1: duration returns [AttributeDescr d] : loc= 'duration' i= INT ;
    public AttributeDescr duration() throws RecognitionException {   
        AttributeDescr d = null;

        Token loc=null;
        Token i=null;

        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:612:3: (loc= 'duration' i= INT )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:612:3: loc= 'duration' i= INT
            {
            loc=(Token)input.LT(1);
            match(input,47,FOLLOW_47_in_duration1150); 
            i=(Token)input.LT(1);
            match(input,INT,FOLLOW_INT_in_duration1154); 
            
            			d = new AttributeDescr( "duration", i.getText() );
            			d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end duration


    // $ANTLR start normal_lhs_block
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:620:1: normal_lhs_block[AndDescr descrs] : (d= lhs )* ;
    public void normal_lhs_block(AndDescr descrs) throws RecognitionException {   
        BaseDescr d = null;


        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:622:3: ( (d= lhs )* )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:622:3: (d= lhs )*
            {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:622:3: (d= lhs )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);
                if ( (LA21_0==ID||LA21_0==LEFT_PAREN||(LA21_0>=71 && LA21_0<=73)) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:622:5: d= lhs
            	    {
            	    pushFollow(FOLLOW_lhs_in_normal_lhs_block1180);
            	    d=lhs();
            	    _fsp--;

            	     descrs.addDescr( d ); 

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ;
    }
    // $ANTLR end normal_lhs_block


    // $ANTLR start lhs
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:676:1: lhs returns [BaseDescr d] : l= lhs_or ;
    public BaseDescr lhs() throws RecognitionException {   
        BaseDescr d = null;

        BaseDescr l = null;


        
        		d=null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:680:4: (l= lhs_or )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:680:4: l= lhs_or
            {
            pushFollow(FOLLOW_lhs_or_in_lhs1218);
            l=lhs_or();
            _fsp--;

             d = l; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end lhs


    // $ANTLR start lhs_column
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:684:1: lhs_column returns [BaseDescr d] : (f= fact_binding | f= fact );
    public BaseDescr lhs_column() throws RecognitionException {   
        BaseDescr d = null;

        BaseDescr f = null;


        
        		d=null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:688:4: (f= fact_binding | f= fact )
            int alt22=2;
            int LA22_0 = input.LA(1);
            if ( (LA22_0==ID) ) {
                int LA22_1 = input.LA(2);
                if ( (LA22_1==40) ) {
                    alt22=1;
                }
                else if ( (LA22_1==LEFT_PAREN||LA22_1==LEFT_SQUARE||LA22_1==31) ) {
                    alt22=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("684:1: lhs_column returns [BaseDescr d] : (f= fact_binding | f= fact );", 22, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("684:1: lhs_column returns [BaseDescr d] : (f= fact_binding | f= fact );", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:688:4: f= fact_binding
                    {
                    pushFollow(FOLLOW_fact_binding_in_lhs_column1246);
                    f=fact_binding();
                    _fsp--;

                     d = f; 

                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:689:4: f= fact
                    {
                    pushFollow(FOLLOW_fact_in_lhs_column1255);
                    f=fact();
                    _fsp--;

                     d = f; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end lhs_column


    // $ANTLR start from_statement
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:692:1: from_statement returns [FromDescr d] : 'from' ds= from_source ;
    public FromDescr from_statement() throws RecognitionException {   
        FromDescr d = null;

        DeclarativeInvokerDescr ds = null;


        
        		d=factory.createFrom();
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:697:4: ( 'from' ds= from_source )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:697:4: 'from' ds= from_source
            {
            match(input,48,FOLLOW_48_in_from_statement1283); 
            pushFollow(FOLLOW_from_source_in_from_statement1287);
            ds=from_source();
            _fsp--;

            
             			d.setDataSource(ds);
             		
             		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end from_statement


    // $ANTLR start from_source
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:707:1: from_source returns [DeclarativeInvokerDescr ds] : ( (var= ID '.' field= ID arg= square_chunk ) | (var= ID '.' method= ID args= paren_chunk ) | (functionName= ID args= paren_chunk ) | (var= ID '.' field= ID ) );
    public DeclarativeInvokerDescr from_source() throws RecognitionException {   
        DeclarativeInvokerDescr ds = null;

        Token var=null;
        Token field=null;
        Token method=null;
        Token functionName=null;
        String arg = null;

        String args = null;


        
        		ds = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:712:3: ( (var= ID '.' field= ID arg= square_chunk ) | (var= ID '.' method= ID args= paren_chunk ) | (functionName= ID args= paren_chunk ) | (var= ID '.' field= ID ) )
            int alt23=4;
            int LA23_0 = input.LA(1);
            if ( (LA23_0==ID) ) {
                int LA23_1 = input.LA(2);
                if ( (LA23_1==31) ) {
                    int LA23_2 = input.LA(3);
                    if ( (LA23_2==ID) ) {
                        switch ( input.LA(4) ) {
                        case ID:
                        case RHS:
                        case RIGHT_PAREN:
                        case 27:
                        case 36:
                        case 54:
                        case 55:
                        case 69:
                        case 70:
                        case 71:
                        case 72:
                        case 73:
                            alt23=4;
                            break;
                        case LEFT_PAREN:
                            int LA23_6 = input.LA(5);
                            if ( (LA23_6==RIGHT_PAREN) ) {
                                alt23=2;
                            }
                            else if ( (LA23_6==ID||LA23_6==LEFT_PAREN||(LA23_6>=71 && LA23_6<=73)) ) {
                                alt23=4;
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("707:1: from_source returns [DeclarativeInvokerDescr ds] : ( (var= ID '.' field= ID arg= square_chunk ) | (var= ID '.' method= ID args= paren_chunk ) | (functionName= ID args= paren_chunk ) | (var= ID '.' field= ID ) );", 23, 6, input);

                                throw nvae;
                            }
                            break;
                        case LEFT_SQUARE:
                            alt23=1;
                            break;
                        default:
                            NoViableAltException nvae =
                                new NoViableAltException("707:1: from_source returns [DeclarativeInvokerDescr ds] : ( (var= ID '.' field= ID arg= square_chunk ) | (var= ID '.' method= ID args= paren_chunk ) | (functionName= ID args= paren_chunk ) | (var= ID '.' field= ID ) );", 23, 4, input);

                            throw nvae;
                        }

                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("707:1: from_source returns [DeclarativeInvokerDescr ds] : ( (var= ID '.' field= ID arg= square_chunk ) | (var= ID '.' method= ID args= paren_chunk ) | (functionName= ID args= paren_chunk ) | (var= ID '.' field= ID ) );", 23, 2, input);

                        throw nvae;
                    }
                }
                else if ( (LA23_1==LEFT_PAREN) ) {
                    alt23=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("707:1: from_source returns [DeclarativeInvokerDescr ds] : ( (var= ID '.' field= ID arg= square_chunk ) | (var= ID '.' method= ID args= paren_chunk ) | (functionName= ID args= paren_chunk ) | (var= ID '.' field= ID ) );", 23, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("707:1: from_source returns [DeclarativeInvokerDescr ds] : ( (var= ID '.' field= ID arg= square_chunk ) | (var= ID '.' method= ID args= paren_chunk ) | (functionName= ID args= paren_chunk ) | (var= ID '.' field= ID ) );", 23, 0, input);

                throw nvae;
            }
            switch (alt23) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:712:3: (var= ID '.' field= ID arg= square_chunk )
                    {
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:712:3: (var= ID '.' field= ID arg= square_chunk )
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:712:4: var= ID '.' field= ID arg= square_chunk
                    {
                    var=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_from_source1332); 
                    match(input,31,FOLLOW_31_in_from_source1334); 
                    field=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_from_source1338); 
                    pushFollow(FOLLOW_square_chunk_in_from_source1343);
                    arg=square_chunk();
                    _fsp--;

                    
                              		  FieldAccessDescr fa;
                    		          fa = new FieldAccessDescr(var.getText(), field.getText(), arg);	
                    			  fa.setLocation( offset(var.getLine()), var.getCharPositionInLine() );
                    			  ds = fa;
                    			 

                    }


                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:722:3: (var= ID '.' method= ID args= paren_chunk )
                    {
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:722:3: (var= ID '.' method= ID args= paren_chunk )
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:722:4: var= ID '.' method= ID args= paren_chunk
                    {
                    var=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_from_source1369); 
                    match(input,31,FOLLOW_31_in_from_source1371); 
                    method=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_from_source1375); 
                    pushFollow(FOLLOW_paren_chunk_in_from_source1379);
                    args=paren_chunk();
                    _fsp--;

                    
                    			  MethodAccessDescr ma = new MethodAccessDescr(var.getText(), method.getText());	
                    			  ma.setLocation( offset(var.getLine()), var.getCharPositionInLine() );
                    			  ma.setArguments(args);
                    			  ds = ma;
                    			

                    }


                    }
                    break;
                case 3 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:731:3: (functionName= ID args= paren_chunk )
                    {
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:731:3: (functionName= ID args= paren_chunk )
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:731:4: functionName= ID args= paren_chunk
                    {
                    functionName=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_from_source1400); 
                    pushFollow(FOLLOW_paren_chunk_in_from_source1404);
                    args=paren_chunk();
                    _fsp--;

                    
                    			FunctionCallDescr fc = new FunctionCallDescr(functionName.getText());
                    			fc.setLocation( offset(functionName.getLine()), functionName.getCharPositionInLine() );			
                    			fc.setArguments(args);
                    			ds = fc;
                    			

                    }


                    }
                    break;
                case 4 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:741:3: (var= ID '.' field= ID )
                    {
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:741:3: (var= ID '.' field= ID )
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:741:4: var= ID '.' field= ID
                    {
                    var=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_from_source1427); 
                    match(input,31,FOLLOW_31_in_from_source1429); 
                    field=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_from_source1433); 
                    
                              		  FieldAccessDescr fa;
                    		          fa = new FieldAccessDescr(var.getText(), field.getText());	
                    			  fa.setLocation( offset(var.getLine()), var.getCharPositionInLine() );
                    			  ds = fa;
                    			 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ds;
    }
    // $ANTLR end from_source


    // $ANTLR start accumulate_statement
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:753:1: accumulate_statement returns [AccumulateDescr d] : loc= 'from' 'accumulate' '(' column= lhs_column ',' 'init' text= paren_chunk ',' 'action' text= paren_chunk ',' 'result' text= paren_chunk ')' ;
    public AccumulateDescr accumulate_statement() throws RecognitionException {   
        AccumulateDescr d = null;

        Token loc=null;
        BaseDescr column = null;

        String text = null;


        
        		d = factory.createAccumulate();
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:758:10: (loc= 'from' 'accumulate' '(' column= lhs_column ',' 'init' text= paren_chunk ',' 'action' text= paren_chunk ',' 'result' text= paren_chunk ')' )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:758:10: loc= 'from' 'accumulate' '(' column= lhs_column ',' 'init' text= paren_chunk ',' 'action' text= paren_chunk ',' 'result' text= paren_chunk ')'
            {
            loc=(Token)input.LT(1);
            match(input,48,FOLLOW_48_in_accumulate_statement1486); 
            match(input,49,FOLLOW_49_in_accumulate_statement1488); 
             
            			d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            		
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_accumulate_statement1498); 
            pushFollow(FOLLOW_lhs_column_in_accumulate_statement1502);
            column=lhs_column();
            _fsp--;

            match(input,34,FOLLOW_34_in_accumulate_statement1504); 
            
            		        d.setSourceColumn( (ColumnDescr)column );
            		
            match(input,50,FOLLOW_50_in_accumulate_statement1513); 
            pushFollow(FOLLOW_paren_chunk_in_accumulate_statement1517);
            text=paren_chunk();
            _fsp--;

            match(input,34,FOLLOW_34_in_accumulate_statement1519); 
            
            		        d.setInitCode( text.substring(1, text.length()-1) );
            		
            match(input,51,FOLLOW_51_in_accumulate_statement1528); 
            pushFollow(FOLLOW_paren_chunk_in_accumulate_statement1532);
            text=paren_chunk();
            _fsp--;

            match(input,34,FOLLOW_34_in_accumulate_statement1534); 
            
            		        d.setActionCode( text.substring(1, text.length()-1) );
            		
            match(input,52,FOLLOW_52_in_accumulate_statement1543); 
            pushFollow(FOLLOW_paren_chunk_in_accumulate_statement1547);
            text=paren_chunk();
            _fsp--;

            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_accumulate_statement1549); 
            
            		        d.setResultCode( text.substring(1, text.length()-1) );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end accumulate_statement


    // $ANTLR start collect_statement
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:780:1: collect_statement returns [CollectDescr d] : loc= 'from' 'collect' '(' column= lhs_column ')' ;
    public CollectDescr collect_statement() throws RecognitionException {   
        CollectDescr d = null;

        Token loc=null;
        BaseDescr column = null;


        
        		d = factory.createCollect();
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:785:10: (loc= 'from' 'collect' '(' column= lhs_column ')' )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:785:10: loc= 'from' 'collect' '(' column= lhs_column ')'
            {
            loc=(Token)input.LT(1);
            match(input,48,FOLLOW_48_in_collect_statement1592); 
            match(input,53,FOLLOW_53_in_collect_statement1594); 
             
            			d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            		
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_collect_statement1604); 
            pushFollow(FOLLOW_lhs_column_in_collect_statement1608);
            column=lhs_column();
            _fsp--;

            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_collect_statement1610); 
            
            		        d.setSourceColumn( (ColumnDescr)column );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end collect_statement


    // $ANTLR start fact_binding
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:866:1: fact_binding returns [BaseDescr d] : id= ID ':' fe= fact_expression[id.getText()] ;
    public BaseDescr fact_binding() throws RecognitionException {   
        BaseDescr d = null;

        Token id=null;
        BaseDescr fe = null;


        
        		d=null;
        		boolean multi=false;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:872:4: (id= ID ':' fe= fact_expression[id.getText()] )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:872:4: id= ID ':' fe= fact_expression[id.getText()]
            {
            id=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_fact_binding1645); 
            match(input,40,FOLLOW_40_in_fact_binding1655); 
            pushFollow(FOLLOW_fact_expression_in_fact_binding1659);
            fe=fact_expression(id.getText());
            _fsp--;

            
             			d=fe;
             		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end fact_binding


    // $ANTLR start fact_expression
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:880:2: fact_expression[String id] returns [BaseDescr pd] : ( '(' fe= fact_expression_in_paren[id] ')' | f= fact );
    public BaseDescr fact_expression(String id) throws RecognitionException {   
        BaseDescr pd = null;

        BaseDescr fe = null;

        BaseDescr f = null;


        
         		pd = null;
         		boolean multi = false;
         	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:885:5: ( '(' fe= fact_expression_in_paren[id] ')' | f= fact )
            int alt24=2;
            int LA24_0 = input.LA(1);
            if ( (LA24_0==LEFT_PAREN) ) {
                alt24=1;
            }
            else if ( (LA24_0==ID) ) {
                alt24=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("880:2: fact_expression[String id] returns [BaseDescr pd] : ( '(' fe= fact_expression_in_paren[id] ')' | f= fact );", 24, 0, input);

                throw nvae;
            }
            switch (alt24) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:885:5: '(' fe= fact_expression_in_paren[id] ')'
                    {
                    match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_fact_expression1691); 
                    pushFollow(FOLLOW_fact_expression_in_paren_in_fact_expression1695);
                    fe=fact_expression_in_paren(id);
                    _fsp--;

                    match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_fact_expression1698); 
                     pd=fe; 

                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:886:6: f= fact
                    {
                    pushFollow(FOLLOW_fact_in_fact_expression1709);
                    f=fact();
                    _fsp--;

                    
                     			((ColumnDescr)f).setIdentifier( id );
                     			pd = f;
                     		

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return pd;
    }
    // $ANTLR end fact_expression


    // $ANTLR start fact_expression_in_paren
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:894:2: fact_expression_in_paren[String id] returns [BaseDescr pd] : ( '(' fe= fact_expression_in_paren[id] ')' | f= fact ( ('or'|'||')f= fact )* );
    public BaseDescr fact_expression_in_paren(String id) throws RecognitionException {   
        BaseDescr pd = null;

        BaseDescr fe = null;

        BaseDescr f = null;


        
         		pd = null;
         		boolean multi = false;
         	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:899:5: ( '(' fe= fact_expression_in_paren[id] ')' | f= fact ( ('or'|'||')f= fact )* )
            int alt26=2;
            int LA26_0 = input.LA(1);
            if ( (LA26_0==LEFT_PAREN) ) {
                alt26=1;
            }
            else if ( (LA26_0==ID) ) {
                alt26=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("894:2: fact_expression_in_paren[String id] returns [BaseDescr pd] : ( '(' fe= fact_expression_in_paren[id] ')' | f= fact ( ('or'|'||')f= fact )* );", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:899:5: '(' fe= fact_expression_in_paren[id] ')'
                    {
                    match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_fact_expression_in_paren1740); 
                    pushFollow(FOLLOW_fact_expression_in_paren_in_fact_expression_in_paren1744);
                    fe=fact_expression_in_paren(id);
                    _fsp--;

                    match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_fact_expression_in_paren1746); 
                     pd=fe; 

                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:900:6: f= fact ( ('or'|'||')f= fact )*
                    {
                    pushFollow(FOLLOW_fact_in_fact_expression_in_paren1757);
                    f=fact();
                    _fsp--;

                    
                     			((ColumnDescr)f).setIdentifier( id );
                     			pd = f;
                     		
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:905:4: ( ('or'|'||')f= fact )*
                    loop25:
                    do {
                        int alt25=2;
                        int LA25_0 = input.LA(1);
                        if ( ((LA25_0>=54 && LA25_0<=55)) ) {
                            alt25=1;
                        }


                        switch (alt25) {
                    	case 1 :
                    	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:905:6: ('or'|'||')f= fact
                    	    {
                    	    if ( (input.LA(1)>=54 && input.LA(1)<=55) ) {
                    	        input.consume();
                    	        errorRecovery=false;
                    	    }
                    	    else {
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_fact_expression_in_paren1770);    throw mse;
                    	    }

                    	    	if ( ! multi ) {
                    	     					BaseDescr first = pd;
                    	     					pd = new OrDescr();
                    	     					((OrDescr)pd).addDescr( first );
                    	     					multi=true;
                    	     				}
                    	     			
                    	    pushFollow(FOLLOW_fact_in_fact_expression_in_paren1787);
                    	    f=fact();
                    	    _fsp--;

                    	    
                    	     				((ColumnDescr)f).setIdentifier( id );
                    	     				((OrDescr)pd).addDescr( f );
                    	     			

                    	    }
                    	    break;

                    	default :
                    	    break loop25;
                        }
                    } while (true);


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return pd;
    }
    // $ANTLR end fact_expression_in_paren


    // $ANTLR start fact
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:921:1: fact returns [BaseDescr d] : id= dotted_name loc= '(' (c= constraints )? endLoc= ')' ;
    public BaseDescr fact() throws RecognitionException {   
        BaseDescr d = null;

        Token loc=null;
        Token endLoc=null;
        String id = null;

        List c = null;


        
        		d=null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:925:5: (id= dotted_name loc= '(' (c= constraints )? endLoc= ')' )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:925:5: id= dotted_name loc= '(' (c= constraints )? endLoc= ')'
            {
            pushFollow(FOLLOW_dotted_name_in_fact1826);
            id=dotted_name();
            _fsp--;

             
             			d = new ColumnDescr( id ); 
             		
            loc=(Token)input.LT(1);
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_fact1839); 
            
             				d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
             			
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:931:7: (c= constraints )?
            int alt27=2;
            int LA27_0 = input.LA(1);
            if ( (LA27_0==ID) ) {
                alt27=1;
            }
            switch (alt27) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:931:9: c= constraints
                    {
                    pushFollow(FOLLOW_constraints_in_fact1847);
                    c=constraints();
                    _fsp--;

                    
                    		 			for ( Iterator cIter = c.iterator() ; cIter.hasNext() ; ) {
                     						((ColumnDescr)d).addDescr( (BaseDescr) cIter.next() );
                     					}
                     				

                    }
                    break;

            }

            endLoc=(Token)input.LT(1);
            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_fact1868); 
            
             					d.setEndLocation( offset(endLoc.getLine()), endLoc.getCharPositionInLine() );	
             				

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end fact


    // $ANTLR start constraints
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:945:1: constraints returns [List constraints] : ( constraint[constraints] | predicate[constraints] ) ( ',' ( constraint[constraints] | predicate[constraints] ) )* ;
    public List constraints() throws RecognitionException {   
        List constraints = null;

        
        		constraints = new ArrayList();
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:949:4: ( ( constraint[constraints] | predicate[constraints] ) ( ',' ( constraint[constraints] | predicate[constraints] ) )* )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:949:4: ( constraint[constraints] | predicate[constraints] ) ( ',' ( constraint[constraints] | predicate[constraints] ) )*
            {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:949:4: ( constraint[constraints] | predicate[constraints] )
            int alt28=2;
            int LA28_0 = input.LA(1);
            if ( (LA28_0==ID) ) {
                int LA28_1 = input.LA(2);
                if ( (LA28_1==40) ) {
                    int LA28_2 = input.LA(3);
                    if ( (LA28_2==ID) ) {
                        int LA28_4 = input.LA(4);
                        if ( (LA28_4==68) ) {
                            alt28=2;
                        }
                        else if ( (LA28_4==RIGHT_PAREN||LA28_4==34||(LA28_4>=58 && LA28_4<=66)) ) {
                            alt28=1;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("949:4: ( constraint[constraints] | predicate[constraints] )", 28, 4, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("949:4: ( constraint[constraints] | predicate[constraints] )", 28, 2, input);

                        throw nvae;
                    }
                }
                else if ( (LA28_1==RIGHT_PAREN||LA28_1==34||(LA28_1>=58 && LA28_1<=66)) ) {
                    alt28=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("949:4: ( constraint[constraints] | predicate[constraints] )", 28, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("949:4: ( constraint[constraints] | predicate[constraints] )", 28, 0, input);

                throw nvae;
            }
            switch (alt28) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:949:5: constraint[constraints]
                    {
                    pushFollow(FOLLOW_constraint_in_constraints1901);
                    constraint(constraints);
                    _fsp--;


                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:949:29: predicate[constraints]
                    {
                    pushFollow(FOLLOW_predicate_in_constraints1904);
                    predicate(constraints);
                    _fsp--;


                    }
                    break;

            }

            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:950:3: ( ',' ( constraint[constraints] | predicate[constraints] ) )*
            loop30:
            do {
                int alt30=2;
                int LA30_0 = input.LA(1);
                if ( (LA30_0==34) ) {
                    alt30=1;
                }


                switch (alt30) {
            	case 1 :
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:950:5: ',' ( constraint[constraints] | predicate[constraints] )
            	    {
            	    match(input,34,FOLLOW_34_in_constraints1912); 
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:950:9: ( constraint[constraints] | predicate[constraints] )
            	    int alt29=2;
            	    int LA29_0 = input.LA(1);
            	    if ( (LA29_0==ID) ) {
            	        int LA29_1 = input.LA(2);
            	        if ( (LA29_1==40) ) {
            	            int LA29_2 = input.LA(3);
            	            if ( (LA29_2==ID) ) {
            	                int LA29_4 = input.LA(4);
            	                if ( (LA29_4==68) ) {
            	                    alt29=2;
            	                }
            	                else if ( (LA29_4==RIGHT_PAREN||LA29_4==34||(LA29_4>=58 && LA29_4<=66)) ) {
            	                    alt29=1;
            	                }
            	                else {
            	                    NoViableAltException nvae =
            	                        new NoViableAltException("950:9: ( constraint[constraints] | predicate[constraints] )", 29, 4, input);

            	                    throw nvae;
            	                }
            	            }
            	            else {
            	                NoViableAltException nvae =
            	                    new NoViableAltException("950:9: ( constraint[constraints] | predicate[constraints] )", 29, 2, input);

            	                throw nvae;
            	            }
            	        }
            	        else if ( (LA29_1==RIGHT_PAREN||LA29_1==34||(LA29_1>=58 && LA29_1<=66)) ) {
            	            alt29=1;
            	        }
            	        else {
            	            NoViableAltException nvae =
            	                new NoViableAltException("950:9: ( constraint[constraints] | predicate[constraints] )", 29, 1, input);

            	            throw nvae;
            	        }
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("950:9: ( constraint[constraints] | predicate[constraints] )", 29, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt29) {
            	        case 1 :
            	            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:950:10: constraint[constraints]
            	            {
            	            pushFollow(FOLLOW_constraint_in_constraints1915);
            	            constraint(constraints);
            	            _fsp--;


            	            }
            	            break;
            	        case 2 :
            	            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:950:34: predicate[constraints]
            	            {
            	            pushFollow(FOLLOW_predicate_in_constraints1918);
            	            predicate(constraints);
            	            _fsp--;


            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop30;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return constraints;
    }
    // $ANTLR end constraints


    // $ANTLR start constraint
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:953:1: constraint[List constraints] : (fb= ID ':' )? f= ID (rd= constraint_expression (con= ('&'|'|')rd= constraint_expression )* )? ;
    public void constraint(List constraints) throws RecognitionException {   
        Token fb=null;
        Token f=null;
        Token con=null;
        RestrictionDescr rd = null;


        
        		BaseDescr d = null;
        		FieldConstraintDescr fc = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:959:3: ( (fb= ID ':' )? f= ID (rd= constraint_expression (con= ('&'|'|')rd= constraint_expression )* )? )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:959:3: (fb= ID ':' )? f= ID (rd= constraint_expression (con= ('&'|'|')rd= constraint_expression )* )?
            {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:959:3: (fb= ID ':' )?
            int alt31=2;
            int LA31_0 = input.LA(1);
            if ( (LA31_0==ID) ) {
                int LA31_1 = input.LA(2);
                if ( (LA31_1==40) ) {
                    alt31=1;
                }
            }
            switch (alt31) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:959:5: fb= ID ':'
                    {
                    fb=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_constraint1947); 
                    match(input,40,FOLLOW_40_in_constraint1949); 

                    }
                    break;

            }

            f=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_constraint1959); 
            
            			if ( fb != null ) {
            				d = new FieldBindingDescr( f.getText(), fb.getText() );
            				d.setLocation( offset(f.getLine()), f.getCharPositionInLine() );
            				constraints.add( d );
            			} 
            			fc = new FieldConstraintDescr(f.getText());
            			fc.setLocation( offset(f.getLine()), f.getCharPositionInLine() );
            		
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:970:3: (rd= constraint_expression (con= ('&'|'|')rd= constraint_expression )* )?
            int alt33=2;
            int LA33_0 = input.LA(1);
            if ( ((LA33_0>=58 && LA33_0<=66)) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:971:4: rd= constraint_expression (con= ('&'|'|')rd= constraint_expression )*
                    {
                    pushFollow(FOLLOW_constraint_expression_in_constraint1975);
                    rd=constraint_expression();
                    _fsp--;

                    
                    				fc.addRestriction(rd);
                    				constraints.add(fc);
                    			
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:976:4: (con= ('&'|'|')rd= constraint_expression )*
                    loop32:
                    do {
                        int alt32=2;
                        int LA32_0 = input.LA(1);
                        if ( ((LA32_0>=56 && LA32_0<=57)) ) {
                            alt32=1;
                        }


                        switch (alt32) {
                    	case 1 :
                    	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:977:5: con= ('&'|'|')rd= constraint_expression
                    	    {
                    	    con=(Token)input.LT(1);
                    	    if ( (input.LA(1)>=56 && input.LA(1)<=57) ) {
                    	        input.consume();
                    	        errorRecovery=false;
                    	    }
                    	    else {
                    	        MismatchedSetException mse =
                    	            new MismatchedSetException(null,input);
                    	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_constraint1994);    throw mse;
                    	    }

                    	    
                    	    					if (con.getText().equals("&") ) {								
                    	    						fc.addRestriction(new RestrictionConnectiveDescr(RestrictionConnectiveDescr.AND));	
                    	    					} else {
                    	    						fc.addRestriction(new RestrictionConnectiveDescr(RestrictionConnectiveDescr.OR));	
                    	    					}							
                    	    				
                    	    pushFollow(FOLLOW_constraint_expression_in_constraint2011);
                    	    rd=constraint_expression();
                    	    _fsp--;

                    	    
                    	    					fc.addRestriction(rd);
                    	    				

                    	    }
                    	    break;

                    	default :
                    	    break loop32;
                        }
                    } while (true);


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ;
    }
    // $ANTLR end constraint


    // $ANTLR start constraint_expression
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:993:1: constraint_expression returns [RestrictionDescr rd] : op= ('=='|'>'|'>='|'<'|'<='|'!='|'contains'|'matches'|'excludes') (bvc= ID | lc= enum_constraint | lc= literal_constraint | rvc= retval_constraint ) ;
    public RestrictionDescr constraint_expression() throws RecognitionException {   
        RestrictionDescr rd = null;

        Token op=null;
        Token bvc=null;
        String lc = null;

        String rvc = null;


        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:995:3: (op= ('=='|'>'|'>='|'<'|'<='|'!='|'contains'|'matches'|'excludes') (bvc= ID | lc= enum_constraint | lc= literal_constraint | rvc= retval_constraint ) )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:995:3: op= ('=='|'>'|'>='|'<'|'<='|'!='|'contains'|'matches'|'excludes') (bvc= ID | lc= enum_constraint | lc= literal_constraint | rvc= retval_constraint )
            {
            op=(Token)input.LT(1);
            if ( (input.LA(1)>=58 && input.LA(1)<=66) ) {
                input.consume();
                errorRecovery=false;
            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recoverFromMismatchedSet(input,mse,FOLLOW_set_in_constraint_expression2063);    throw mse;
            }

            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1005:3: (bvc= ID | lc= enum_constraint | lc= literal_constraint | rvc= retval_constraint )
            int alt34=4;
            switch ( input.LA(1) ) {
            case ID:
                int LA34_1 = input.LA(2);
                if ( (LA34_1==31) ) {
                    alt34=2;
                }
                else if ( (LA34_1==RIGHT_PAREN||LA34_1==34||(LA34_1>=56 && LA34_1<=57)) ) {
                    alt34=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("1005:3: (bvc= ID | lc= enum_constraint | lc= literal_constraint | rvc= retval_constraint )", 34, 1, input);

                    throw nvae;
                }
                break;
            case INT:
            case BOOL:
            case STRING:
            case FLOAT:
            case 67:
                alt34=3;
                break;
            case LEFT_PAREN:
                alt34=4;
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("1005:3: (bvc= ID | lc= enum_constraint | lc= literal_constraint | rvc= retval_constraint )", 34, 0, input);

                throw nvae;
            }

            switch (alt34) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1005:5: bvc= ID
                    {
                    bvc=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_constraint_expression2130); 
                    
                    				rd = new VariableRestrictionDescr(op.getText(), bvc.getText());
                    			

                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1010:4: lc= enum_constraint
                    {
                    pushFollow(FOLLOW_enum_constraint_in_constraint_expression2146);
                    lc=enum_constraint();
                    _fsp--;

                     
                    				rd  = new LiteralRestrictionDescr(op.getText(), lc, true);
                    			

                    }
                    break;
                case 3 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1015:4: lc= literal_constraint
                    {
                    pushFollow(FOLLOW_literal_constraint_in_constraint_expression2169);
                    lc=literal_constraint();
                    _fsp--;

                     
                    				rd  = new LiteralRestrictionDescr(op.getText(), lc);
                    			

                    }
                    break;
                case 4 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1019:5: rvc= retval_constraint
                    {
                    pushFollow(FOLLOW_retval_constraint_in_constraint_expression2183);
                    rvc=retval_constraint();
                    _fsp--;

                     
                    				rd = new ReturnValueRestrictionDescr(op.getText(), rvc);							
                    			

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return rd;
    }
    // $ANTLR end constraint_expression


    // $ANTLR start literal_constraint
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1026:1: literal_constraint returns [String text] : (t= STRING | t= INT | t= FLOAT | t= BOOL | t= 'null' ) ;
    public String literal_constraint() throws RecognitionException {   
        String text = null;

        Token t=null;

        
        		text = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1030:4: ( (t= STRING | t= INT | t= FLOAT | t= BOOL | t= 'null' ) )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1030:4: (t= STRING | t= INT | t= FLOAT | t= BOOL | t= 'null' )
            {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1030:4: (t= STRING | t= INT | t= FLOAT | t= BOOL | t= 'null' )
            int alt35=5;
            switch ( input.LA(1) ) {
            case STRING:
                alt35=1;
                break;
            case INT:
                alt35=2;
                break;
            case FLOAT:
                alt35=3;
                break;
            case BOOL:
                alt35=4;
                break;
            case 67:
                alt35=5;
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("1030:4: (t= STRING | t= INT | t= FLOAT | t= BOOL | t= 'null' )", 35, 0, input);

                throw nvae;
            }

            switch (alt35) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1030:6: t= STRING
                    {
                    t=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_literal_constraint2222); 
                     text = getString( t ); 

                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1031:5: t= INT
                    {
                    t=(Token)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_literal_constraint2233); 
                     text = t.getText(); 

                    }
                    break;
                case 3 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1032:5: t= FLOAT
                    {
                    t=(Token)input.LT(1);
                    match(input,FLOAT,FOLLOW_FLOAT_in_literal_constraint2246); 
                     text = t.getText(); 

                    }
                    break;
                case 4 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1033:5: t= BOOL
                    {
                    t=(Token)input.LT(1);
                    match(input,BOOL,FOLLOW_BOOL_in_literal_constraint2257); 
                     text = t.getText(); 

                    }
                    break;
                case 5 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1034:5: t= 'null'
                    {
                    t=(Token)input.LT(1);
                    match(input,67,FOLLOW_67_in_literal_constraint2269); 
                     text = null; 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return text;
    }
    // $ANTLR end literal_constraint


    // $ANTLR start enum_constraint
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1038:1: enum_constraint returns [String text] : (cls= ID '.' en= ID ) ;
    public String enum_constraint() throws RecognitionException {   
        String text = null;

        Token cls=null;
        Token en=null;

        
        		text = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1042:4: ( (cls= ID '.' en= ID ) )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1042:4: (cls= ID '.' en= ID )
            {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1042:4: (cls= ID '.' en= ID )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1042:5: cls= ID '.' en= ID
            {
            cls=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_enum_constraint2300); 
            match(input,31,FOLLOW_31_in_enum_constraint2302); 
            en=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_enum_constraint2306); 

            }

             text = cls.getText() + "." + en.getText(); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return text;
    }
    // $ANTLR end enum_constraint


    // $ANTLR start predicate
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1046:1: predicate[List constraints] : decl= ID ':' field= ID '->' text= paren_chunk ;
    public void predicate(List constraints) throws RecognitionException {   
        Token decl=null;
        Token field=null;
        String text = null;


        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1048:3: (decl= ID ':' field= ID '->' text= paren_chunk )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1048:3: decl= ID ':' field= ID '->' text= paren_chunk
            {
            decl=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_predicate2328); 
            match(input,40,FOLLOW_40_in_predicate2330); 
            field=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_predicate2334); 
            match(input,68,FOLLOW_68_in_predicate2336); 
            pushFollow(FOLLOW_paren_chunk_in_predicate2340);
            text=paren_chunk();
            _fsp--;

            
            		        String body = text.substring(1, text.length()-1);
            			PredicateDescr d = new PredicateDescr(field.getText(), decl.getText(), body );
            			constraints.add( d );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return ;
    }
    // $ANTLR end predicate


    // $ANTLR start paren_chunk
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1056:1: paren_chunk returns [String text] : loc= '(' ')' ;
    public String paren_chunk() throws RecognitionException {   
        String text = null;

        Token loc=null;

        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1058:10: (loc= '(' ')' )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1058:10: loc= '(' ')'
            {
            
            		    ((CommonTokenStream)input).setTokenTypeChannel(WS, Token.DEFAULT_CHANNEL);
            	        
            loc=(Token)input.LT(1);
            match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_paren_chunk2374); 
            
            		    int parenCounter = 1;
            		    StringBuffer buf = new StringBuffer();
            		    buf.append(loc.getText());
            
                                do {
                                    Token nextToken = input.LT(1);
                                    buf.append( nextToken.getText() );
                                    
                                    int nextTokenId = nextToken.getType();
                                    if( nextTokenId == RIGHT_PAREN ) {
                                        parenCounter--;
                                    } else if( nextTokenId == LEFT_PAREN ) {
                                        parenCounter++;
                                    }
                                    if( parenCounter == 0 ) {
                                        break;
                                    }
                                    input.consume();
            		    } while( true );
            		    text = buf.toString();
            		    ((CommonTokenStream)input).setTokenTypeChannel(WS, Token.HIDDEN_CHANNEL);
            		
            match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_paren_chunk2397); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return text;
    }
    // $ANTLR end paren_chunk


    // $ANTLR start square_chunk
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1089:1: square_chunk returns [String text] : loc= '[' ']' ;
    public String square_chunk() throws RecognitionException {   
        String text = null;

        Token loc=null;

        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1091:10: (loc= '[' ']' )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1091:10: loc= '[' ']'
            {
            
            		    ((CommonTokenStream)input).setTokenTypeChannel(WS, Token.DEFAULT_CHANNEL);
            	        
            loc=(Token)input.LT(1);
            match(input,LEFT_SQUARE,FOLLOW_LEFT_SQUARE_in_square_chunk2428); 
            
            		    int parenCounter = 1;
            		    StringBuffer buf = new StringBuffer();
            		    buf.append(loc.getText());
            
                                do {
                                    Token nextToken = input.LT(1);
                                    buf.append( nextToken.getText() );
                                    
                                    int nextTokenId = nextToken.getType();
                                    if( nextTokenId == RIGHT_SQUARE ) {
                                        parenCounter--;
                                    } else if( nextTokenId == LEFT_SQUARE ) {
                                        parenCounter++;
                                    }
                                    if( parenCounter == 0 ) {
                                        break;
                                    }
                                    input.consume();
            		    } while( true );
            		    text = buf.toString();
            		    ((CommonTokenStream)input).setTokenTypeChannel(WS, Token.HIDDEN_CHANNEL);
            		
            match(input,RIGHT_SQUARE,FOLLOW_RIGHT_SQUARE_in_square_chunk2451); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return text;
    }
    // $ANTLR end square_chunk


    // $ANTLR start retval_constraint
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1121:1: retval_constraint returns [String text] : c= paren_chunk ;
    public String retval_constraint() throws RecognitionException {   
        String text = null;

        String c = null;


        
        		text = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1126:3: (c= paren_chunk )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1126:3: c= paren_chunk
            {
            pushFollow(FOLLOW_paren_chunk_in_retval_constraint2478);
            c=paren_chunk();
            _fsp--;

             text = c.substring(1, c.length()-1); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return text;
    }
    // $ANTLR end retval_constraint


    // $ANTLR start lhs_or
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1134:1: lhs_or returns [BaseDescr d] : left= lhs_and ( ('or'|'||')right= lhs_and )* ;
    public BaseDescr lhs_or() throws RecognitionException {   
        BaseDescr d = null;

        BaseDescr left = null;

        BaseDescr right = null;


        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1139:3: (left= lhs_and ( ('or'|'||')right= lhs_and )* )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1139:3: left= lhs_and ( ('or'|'||')right= lhs_and )*
            {
             OrDescr or = null; 
            pushFollow(FOLLOW_lhs_and_in_lhs_or2515);
            left=lhs_and();
            _fsp--;

            d = left; 
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1141:3: ( ('or'|'||')right= lhs_and )*
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);
                if ( ((LA36_0>=54 && LA36_0<=55)) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1141:5: ('or'|'||')right= lhs_and
            	    {
            	    if ( (input.LA(1)>=54 && input.LA(1)<=55) ) {
            	        input.consume();
            	        errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_lhs_or2524);    throw mse;
            	    }

            	    pushFollow(FOLLOW_lhs_and_in_lhs_or2534);
            	    right=lhs_and();
            	    _fsp--;

            	    
            	    				if ( or == null ) {
            	    					or = new OrDescr();
            	    					or.addDescr( left );
            	    					d = or;
            	    				}
            	    				
            	    				or.addDescr( right );
            	    			

            	    }
            	    break;

            	default :
            	    break loop36;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end lhs_or


    // $ANTLR start lhs_and
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1155:1: lhs_and returns [BaseDescr d] : left= lhs_unary ( ('and'|'&&')right= lhs_unary )* ;
    public BaseDescr lhs_and() throws RecognitionException {   
        BaseDescr d = null;

        BaseDescr left = null;

        BaseDescr right = null;


        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1160:3: (left= lhs_unary ( ('and'|'&&')right= lhs_unary )* )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1160:3: left= lhs_unary ( ('and'|'&&')right= lhs_unary )*
            {
             AndDescr and = null; 
            pushFollow(FOLLOW_lhs_unary_in_lhs_and2574);
            left=lhs_unary();
            _fsp--;

             d = left; 
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1162:3: ( ('and'|'&&')right= lhs_unary )*
            loop37:
            do {
                int alt37=2;
                int LA37_0 = input.LA(1);
                if ( ((LA37_0>=69 && LA37_0<=70)) ) {
                    alt37=1;
                }


                switch (alt37) {
            	case 1 :
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1162:5: ('and'|'&&')right= lhs_unary
            	    {
            	    if ( (input.LA(1)>=69 && input.LA(1)<=70) ) {
            	        input.consume();
            	        errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recoverFromMismatchedSet(input,mse,FOLLOW_set_in_lhs_and2583);    throw mse;
            	    }

            	    pushFollow(FOLLOW_lhs_unary_in_lhs_and2593);
            	    right=lhs_unary();
            	    _fsp--;

            	    
            	    				if ( and == null ) {
            	    					and = new AndDescr();
            	    					and.addDescr( left );
            	    					d = and;
            	    				}
            	    				
            	    				and.addDescr( right );
            	    			

            	    }
            	    break;

            	default :
            	    break loop37;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end lhs_and


    // $ANTLR start lhs_unary
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1176:1: lhs_unary returns [BaseDescr d] : (u= lhs_exist | u= lhs_not | u= lhs_eval | u= lhs_column ( (fm= from_statement ) | (ac= accumulate_statement ) | (cs= collect_statement ) )? | '(' u= lhs ')' ) opt_semicolon ;
    public BaseDescr lhs_unary() throws RecognitionException {   
        BaseDescr d = null;

        BaseDescr u = null;

        FromDescr fm = null;

        AccumulateDescr ac = null;

        CollectDescr cs = null;


        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1180:4: ( (u= lhs_exist | u= lhs_not | u= lhs_eval | u= lhs_column ( (fm= from_statement ) | (ac= accumulate_statement ) | (cs= collect_statement ) )? | '(' u= lhs ')' ) opt_semicolon )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1180:4: (u= lhs_exist | u= lhs_not | u= lhs_eval | u= lhs_column ( (fm= from_statement ) | (ac= accumulate_statement ) | (cs= collect_statement ) )? | '(' u= lhs ')' ) opt_semicolon
            {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1180:4: (u= lhs_exist | u= lhs_not | u= lhs_eval | u= lhs_column ( (fm= from_statement ) | (ac= accumulate_statement ) | (cs= collect_statement ) )? | '(' u= lhs ')' )
            int alt39=5;
            switch ( input.LA(1) ) {
            case 71:
                alt39=1;
                break;
            case 72:
                alt39=2;
                break;
            case 73:
                alt39=3;
                break;
            case ID:
                alt39=4;
                break;
            case LEFT_PAREN:
                alt39=5;
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("1180:4: (u= lhs_exist | u= lhs_not | u= lhs_eval | u= lhs_column ( (fm= from_statement ) | (ac= accumulate_statement ) | (cs= collect_statement ) )? | '(' u= lhs ')' )", 39, 0, input);

                throw nvae;
            }

            switch (alt39) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1180:6: u= lhs_exist
                    {
                    pushFollow(FOLLOW_lhs_exist_in_lhs_unary2630);
                    u=lhs_exist();
                    _fsp--;


                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1181:5: u= lhs_not
                    {
                    pushFollow(FOLLOW_lhs_not_in_lhs_unary2638);
                    u=lhs_not();
                    _fsp--;


                    }
                    break;
                case 3 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1182:5: u= lhs_eval
                    {
                    pushFollow(FOLLOW_lhs_eval_in_lhs_unary2646);
                    u=lhs_eval();
                    _fsp--;


                    }
                    break;
                case 4 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1183:5: u= lhs_column ( (fm= from_statement ) | (ac= accumulate_statement ) | (cs= collect_statement ) )?
                    {
                    pushFollow(FOLLOW_lhs_column_in_lhs_unary2654);
                    u=lhs_column();
                    _fsp--;

                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1183:18: ( (fm= from_statement ) | (ac= accumulate_statement ) | (cs= collect_statement ) )?
                    int alt38=4;
                    int LA38_0 = input.LA(1);
                    if ( (LA38_0==48) ) {
                        switch ( input.LA(2) ) {
                            case 49:
                                alt38=2;
                                break;
                            case 53:
                                alt38=3;
                                break;
                            case ID:
                                alt38=1;
                                break;
                        }

                    }
                    switch (alt38) {
                        case 1 :
                            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1184:14: (fm= from_statement )
                            {
                            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1184:14: (fm= from_statement )
                            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1184:15: fm= from_statement
                            {
                            pushFollow(FOLLOW_from_statement_in_lhs_unary2674);
                            fm=from_statement();
                            _fsp--;

                            fm.setColumn((ColumnDescr) u); u=fm;

                            }


                            }
                            break;
                        case 2 :
                            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1185:14: (ac= accumulate_statement )
                            {
                            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1185:14: (ac= accumulate_statement )
                            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1185:15: ac= accumulate_statement
                            {
                            pushFollow(FOLLOW_accumulate_statement_in_lhs_unary2696);
                            ac=accumulate_statement();
                            _fsp--;

                            ac.setResultColumn((ColumnDescr) u); u=ac;

                            }


                            }
                            break;
                        case 3 :
                            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1186:14: (cs= collect_statement )
                            {
                            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1186:14: (cs= collect_statement )
                            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1186:15: cs= collect_statement
                            {
                            pushFollow(FOLLOW_collect_statement_in_lhs_unary2717);
                            cs=collect_statement();
                            _fsp--;

                            cs.setResultColumn((ColumnDescr) u); u=cs;

                            }


                            }
                            break;

                    }


                    }
                    break;
                case 5 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1188:5: '(' u= lhs ')'
                    {
                    match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_lhs_unary2740); 
                    pushFollow(FOLLOW_lhs_in_lhs_unary2744);
                    u=lhs();
                    _fsp--;

                    match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_lhs_unary2746); 

                    }
                    break;

            }

             d = u; 
            pushFollow(FOLLOW_opt_semicolon_in_lhs_unary2756);
            opt_semicolon();
            _fsp--;


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end lhs_unary


    // $ANTLR start lhs_exist
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1193:1: lhs_exist returns [BaseDescr d] : loc= 'exists' ( '(' column= lhs_column ')' | column= lhs_column ) ;
    public BaseDescr lhs_exist() throws RecognitionException {   
        BaseDescr d = null;

        Token loc=null;
        BaseDescr column = null;


        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1197:4: (loc= 'exists' ( '(' column= lhs_column ')' | column= lhs_column ) )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1197:4: loc= 'exists' ( '(' column= lhs_column ')' | column= lhs_column )
            {
            loc=(Token)input.LT(1);
            match(input,71,FOLLOW_71_in_lhs_exist2780); 
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1197:17: ( '(' column= lhs_column ')' | column= lhs_column )
            int alt40=2;
            int LA40_0 = input.LA(1);
            if ( (LA40_0==LEFT_PAREN) ) {
                alt40=1;
            }
            else if ( (LA40_0==ID) ) {
                alt40=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("1197:17: ( '(' column= lhs_column ')' | column= lhs_column )", 40, 0, input);

                throw nvae;
            }
            switch (alt40) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1197:18: '(' column= lhs_column ')'
                    {
                    match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_lhs_exist2783); 
                    pushFollow(FOLLOW_lhs_column_in_lhs_exist2787);
                    column=lhs_column();
                    _fsp--;

                    match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_lhs_exist2789); 

                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1197:46: column= lhs_column
                    {
                    pushFollow(FOLLOW_lhs_column_in_lhs_exist2795);
                    column=lhs_column();
                    _fsp--;


                    }
                    break;

            }

             
            			d = new ExistsDescr( (ColumnDescr) column ); 
            			d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end lhs_exist


    // $ANTLR start lhs_not
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1204:1: lhs_not returns [NotDescr d] : loc= 'not' ( '(' column= lhs_column ')' | column= lhs_column ) ;
    public NotDescr lhs_not() throws RecognitionException {   
        NotDescr d = null;

        Token loc=null;
        BaseDescr column = null;


        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1208:4: (loc= 'not' ( '(' column= lhs_column ')' | column= lhs_column ) )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1208:4: loc= 'not' ( '(' column= lhs_column ')' | column= lhs_column )
            {
            loc=(Token)input.LT(1);
            match(input,72,FOLLOW_72_in_lhs_not2825); 
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1208:14: ( '(' column= lhs_column ')' | column= lhs_column )
            int alt41=2;
            int LA41_0 = input.LA(1);
            if ( (LA41_0==LEFT_PAREN) ) {
                alt41=1;
            }
            else if ( (LA41_0==ID) ) {
                alt41=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("1208:14: ( '(' column= lhs_column ')' | column= lhs_column )", 41, 0, input);

                throw nvae;
            }
            switch (alt41) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1208:15: '(' column= lhs_column ')'
                    {
                    match(input,LEFT_PAREN,FOLLOW_LEFT_PAREN_in_lhs_not2828); 
                    pushFollow(FOLLOW_lhs_column_in_lhs_not2832);
                    column=lhs_column();
                    _fsp--;

                    match(input,RIGHT_PAREN,FOLLOW_RIGHT_PAREN_in_lhs_not2835); 

                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1208:44: column= lhs_column
                    {
                    pushFollow(FOLLOW_lhs_column_in_lhs_not2841);
                    column=lhs_column();
                    _fsp--;


                    }
                    break;

            }

            
            			d = new NotDescr( (ColumnDescr) column ); 
            			d.setLocation( offset(loc.getLine()), loc.getCharPositionInLine() );
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end lhs_not


    // $ANTLR start lhs_eval
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1215:1: lhs_eval returns [BaseDescr d] : loc= 'eval' c= paren_chunk ;
    public BaseDescr lhs_eval() throws RecognitionException {   
        BaseDescr d = null;

        Token loc=null;
        String c = null;


        
        		d = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1219:4: (loc= 'eval' c= paren_chunk )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1219:4: loc= 'eval' c= paren_chunk
            {
            loc=(Token)input.LT(1);
            match(input,73,FOLLOW_73_in_lhs_eval2869); 
            pushFollow(FOLLOW_paren_chunk_in_lhs_eval2873);
            c=paren_chunk();
            _fsp--;

             
            		        String body = c.substring(1, c.length()-1);
            			checkTrailingSemicolon( body, offset(loc.getLine()) );
            			d = new EvalDescr( body ); 
            		

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return d;
    }
    // $ANTLR end lhs_eval


    // $ANTLR start dotted_name
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1227:1: dotted_name returns [String name] : id= ID ( '.' id= ID )* ( '[' ']' )* ;
    public String dotted_name() throws RecognitionException {   
        String name = null;

        Token id=null;

        
        		name = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1232:3: (id= ID ( '.' id= ID )* ( '[' ']' )* )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1232:3: id= ID ( '.' id= ID )* ( '[' ']' )*
            {
            id=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_dotted_name2904); 
             name=id.getText(); 
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1232:32: ( '.' id= ID )*
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);
                if ( (LA42_0==31) ) {
                    alt42=1;
                }


                switch (alt42) {
            	case 1 :
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1232:34: '.' id= ID
            	    {
            	    match(input,31,FOLLOW_31_in_dotted_name2910); 
            	    id=(Token)input.LT(1);
            	    match(input,ID,FOLLOW_ID_in_dotted_name2914); 
            	     name = name + "." + id.getText(); 

            	    }
            	    break;

            	default :
            	    break loop42;
                }
            } while (true);

            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1232:85: ( '[' ']' )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);
                if ( (LA43_0==LEFT_SQUARE) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1232:87: '[' ']'
            	    {
            	    match(input,LEFT_SQUARE,FOLLOW_LEFT_SQUARE_in_dotted_name2923); 
            	    match(input,RIGHT_SQUARE,FOLLOW_RIGHT_SQUARE_in_dotted_name2925); 
            	     name = name + "[]";

            	    }
            	    break;

            	default :
            	    break loop43;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return name;
    }
    // $ANTLR end dotted_name


    // $ANTLR start argument
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1235:1: argument returns [String name] : id= ID ( '[' ']' )* ;
    public String argument() throws RecognitionException {   
        String name = null;

        Token id=null;

        
        		name = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1240:3: (id= ID ( '[' ']' )* )
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1240:3: id= ID ( '[' ']' )*
            {
            id=(Token)input.LT(1);
            match(input,ID,FOLLOW_ID_in_argument2955); 
             name=id.getText(); 
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1240:32: ( '[' ']' )*
            loop44:
            do {
                int alt44=2;
                int LA44_0 = input.LA(1);
                if ( (LA44_0==LEFT_SQUARE) ) {
                    alt44=1;
                }


                switch (alt44) {
            	case 1 :
            	    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1240:34: '[' ']'
            	    {
            	    match(input,LEFT_SQUARE,FOLLOW_LEFT_SQUARE_in_argument2961); 
            	    match(input,RIGHT_SQUARE,FOLLOW_RIGHT_SQUARE_in_argument2963); 
            	     name = name + "[]";

            	    }
            	    break;

            	default :
            	    break loop44;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return name;
    }
    // $ANTLR end argument


    // $ANTLR start word
    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1244:1: word returns [String word] : (id= ID | 'import' | 'use' | 'rule' | 'query' | 'salience' | 'no-loop' | 'when' | 'then' | 'end' | str= STRING );
    public String word() throws RecognitionException {   
        String word = null;

        Token id=null;
        Token str=null;

        
        		word = null;
        	
        try {
            // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1248:4: (id= ID | 'import' | 'use' | 'rule' | 'query' | 'salience' | 'no-loop' | 'when' | 'then' | 'end' | str= STRING )
            int alt45=11;
            switch ( input.LA(1) ) {
            case ID:
                alt45=1;
                break;
            case 29:
                alt45=2;
                break;
            case 74:
                alt45=3;
                break;
            case 38:
                alt45=4;
                break;
            case 35:
                alt45=5;
                break;
            case 42:
                alt45=6;
                break;
            case 43:
                alt45=7;
                break;
            case 39:
                alt45=8;
                break;
            case RHS:
                alt45=9;
                break;
            case 36:
                alt45=10;
                break;
            case STRING:
                alt45=11;
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("1244:1: word returns [String word] : (id= ID | 'import' | 'use' | 'rule' | 'query' | 'salience' | 'no-loop' | 'when' | 'then' | 'end' | str= STRING );", 45, 0, input);

                throw nvae;
            }

            switch (alt45) {
                case 1 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1248:4: id= ID
                    {
                    id=(Token)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_word2991); 
                     word=id.getText(); 

                    }
                    break;
                case 2 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1249:4: 'import'
                    {
                    match(input,29,FOLLOW_29_in_word3003); 
                     word="import"; 

                    }
                    break;
                case 3 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1250:4: 'use'
                    {
                    match(input,74,FOLLOW_74_in_word3012); 
                     word="use"; 

                    }
                    break;
                case 4 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1251:4: 'rule'
                    {
                    match(input,38,FOLLOW_38_in_word3024); 
                     word="rule"; 

                    }
                    break;
                case 5 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1252:4: 'query'
                    {
                    match(input,35,FOLLOW_35_in_word3035); 
                     word="query"; 

                    }
                    break;
                case 6 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1253:4: 'salience'
                    {
                    match(input,42,FOLLOW_42_in_word3045); 
                     word="salience"; 

                    }
                    break;
                case 7 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1254:5: 'no-loop'
                    {
                    match(input,43,FOLLOW_43_in_word3053); 
                     word="no-loop"; 

                    }
                    break;
                case 8 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1255:4: 'when'
                    {
                    match(input,39,FOLLOW_39_in_word3061); 
                     word="when"; 

                    }
                    break;
                case 9 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1256:4: 'then'
                    {
                    match(input,RHS,FOLLOW_RHS_in_word3072); 
                     word="then"; 

                    }
                    break;
                case 10 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1257:4: 'end'
                    {
                    match(input,36,FOLLOW_36_in_word3083); 
                     word="end"; 

                    }
                    break;
                case 11 :
                    // D:\\dev\\jbossrules\\trunk\\drools-compiler\\src\\main\\resources\\org\\drools\\lang\\DRL.g:1258:4: str= STRING
                    {
                    str=(Token)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_word3097); 
                     word=getString(str);

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
       }
        return word;
    }
    // $ANTLR end word


    protected DFA8 dfa8 = new DFA8(this);
    protected DFA9 dfa9 = new DFA9(this);
    public static final String DFA8_eotS =
        "\6\uffff";
    public static final String DFA8_eofS =
        "\6\uffff";
    public static final String DFA8_minS =
        "\2\4\1\27\2\uffff\1\4";
    public static final String DFA8_maxS =
        "\1\4\1\42\1\27\2\uffff\1\42";
    public static final String DFA8_acceptS =
        "\3\uffff\1\2\1\1\1\uffff";
    public static final String DFA8_specialS =
        "\6\uffff}>";
    public static final String[] DFA8_transition = {
        "\1\1",
        "\1\4\20\uffff\1\3\1\2\10\uffff\1\4\2\uffff\1\3",
        "\1\5",
        "",
        "",
        "\1\4\20\uffff\1\3\1\2\13\uffff\1\3"
    };

    class DFA8 extends DFA {
        public DFA8(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 8;
            this.eot = DFA.unpackEncodedString(DFA8_eotS);
            this.eof = DFA.unpackEncodedString(DFA8_eofS);
            this.min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
            this.max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
            this.accept = DFA.unpackEncodedString(DFA8_acceptS);
            this.special = DFA.unpackEncodedString(DFA8_specialS);
            int numStates = DFA8_transition.length;
            this.transition = new short[numStates][];
            for (int i=0; i<numStates; i++) {
                transition[i] = DFA.unpackEncodedString(DFA8_transition[i]);
            }
        }
        public String getDescription() {
            return "373:6: (paramType= dotted_name )?";
        }
    }
    public static final String DFA9_eotS =
        "\6\uffff";
    public static final String DFA9_eofS =
        "\6\uffff";
    public static final String DFA9_minS =
        "\2\4\1\uffff\1\27\1\uffff\1\4";
    public static final String DFA9_maxS =
        "\1\4\1\42\1\uffff\1\27\1\uffff\1\42";
    public static final String DFA9_acceptS =
        "\2\uffff\1\1\1\uffff\1\2\1\uffff";
    public static final String DFA9_specialS =
        "\6\uffff}>";
    public static final String[] DFA9_transition = {
        "\1\1",
        "\1\2\20\uffff\1\4\1\3\10\uffff\1\2\2\uffff\1\4",
        "",
        "\1\5",
        "",
        "\1\2\20\uffff\1\4\1\3\13\uffff\1\4"
    };

    class DFA9 extends DFA {
        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA.unpackEncodedString(DFA9_eotS);
            this.eof = DFA.unpackEncodedString(DFA9_eofS);
            this.min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
            this.max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
            this.accept = DFA.unpackEncodedString(DFA9_acceptS);
            this.special = DFA.unpackEncodedString(DFA9_specialS);
            int numStates = DFA9_transition.length;
            this.transition = new short[numStates][];
            for (int i=0; i<numStates; i++) {
                transition[i] = DFA.unpackEncodedString(DFA9_transition[i]);
            }
        }
        public String getDescription() {
            return "377:11: (paramType= dotted_name )?";
        }
    }
 

    public static final BitSet FOLLOW_27_in_opt_semicolon39 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_prolog_in_compilation_unit51 = new BitSet(new long[]{0x0000006A60000002L});
    public static final BitSet FOLLOW_statement_in_compilation_unit58 = new BitSet(new long[]{0x0000006A60000002L});
    public static final BitSet FOLLOW_package_statement_in_prolog83 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_import_statement_in_statement107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_import_statement_in_statement118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_global_in_statement123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_statement128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_template_in_statement141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule_in_statement150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_query_in_statement160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_package_statement188 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dotted_name_in_package_statement192 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_package_statement194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_import_statement211 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_import_name_in_import_statement215 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_import_statement217 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_function_import_statement233 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_30_in_function_import_statement235 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_import_name_in_function_import_statement239 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_function_import_statement241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_import_name273 = new BitSet(new long[]{0x0000000180000002L});
    public static final BitSet FOLLOW_31_in_import_name279 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_import_name283 = new BitSet(new long[]{0x0000000180000002L});
    public static final BitSet FOLLOW_32_in_import_name293 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_global317 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dotted_name_in_global321 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_global325 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_global327 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_function354 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dotted_name_in_function359 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_function365 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_function374 = new BitSet(new long[]{0x0000000000200010L});
    public static final BitSet FOLLOW_dotted_name_in_function384 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_argument_in_function390 = new BitSet(new long[]{0x0000000400200000L});
    public static final BitSet FOLLOW_34_in_function404 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_dotted_name_in_function409 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_argument_in_function415 = new BitSet(new long[]{0x0000000400200000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_function439 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_CURLY_CHUNK_in_function445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_query476 = new BitSet(new long[]{0x00000CD820000250L,0x0000000000000400L});
    public static final BitSet FOLLOW_word_in_query480 = new BitSet(new long[]{0x0000001000100010L,0x0000000000000380L});
    public static final BitSet FOLLOW_normal_lhs_block_in_query494 = new BitSet(new long[]{0x0000001000000000L});
    public static final BitSet FOLLOW_36_in_query509 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_template535 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_template539 = new BitSet(new long[]{0x0000000008000010L});
    public static final BitSet FOLLOW_opt_semicolon_in_template541 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_template_slot_in_template556 = new BitSet(new long[]{0x0000001000000010L});
    public static final BitSet FOLLOW_36_in_template571 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_template573 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_dotted_name_in_template_slot605 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_template_slot609 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_template_slot611 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_rule642 = new BitSet(new long[]{0x00000CD820000250L,0x0000000000000400L});
    public static final BitSet FOLLOW_word_in_rule646 = new BitSet(new long[]{0x0000FE8400000040L});
    public static final BitSet FOLLOW_rule_attributes_in_rule655 = new BitSet(new long[]{0x0000008000000040L});
    public static final BitSet FOLLOW_39_in_rule664 = new BitSet(new long[]{0x0000010000100050L,0x0000000000000380L});
    public static final BitSet FOLLOW_40_in_rule666 = new BitSet(new long[]{0x0000000000100050L,0x0000000000000380L});
    public static final BitSet FOLLOW_normal_lhs_block_in_rule684 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_RHS_in_rule707 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_rule_attributes732 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_rule_attributes734 = new BitSet(new long[]{0x0000FC0400000002L});
    public static final BitSet FOLLOW_34_in_rule_attributes743 = new BitSet(new long[]{0x0000FC0000000000L});
    public static final BitSet FOLLOW_rule_attribute_in_rule_attributes748 = new BitSet(new long[]{0x0000FC0400000002L});
    public static final BitSet FOLLOW_salience_in_rule_attribute789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_no_loop_in_rule_attribute799 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_agenda_group_in_rule_attribute810 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_duration_in_rule_attribute823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_activation_group_in_rule_attribute837 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_auto_focus_in_rule_attribute848 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_salience882 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_INT_in_salience886 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_salience888 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_no_loop923 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_no_loop925 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_no_loop950 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_BOOL_in_no_loop954 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_no_loop956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_auto_focus1002 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_auto_focus1004 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_auto_focus1029 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_BOOL_in_auto_focus1033 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_auto_focus1035 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_activation_group1077 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_STRING_in_activation_group1081 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_activation_group1083 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_agenda_group1112 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_STRING_in_agenda_group1116 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_agenda_group1118 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_duration1150 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_INT_in_duration1154 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lhs_in_normal_lhs_block1180 = new BitSet(new long[]{0x0000000000100012L,0x0000000000000380L});
    public static final BitSet FOLLOW_lhs_or_in_lhs1218 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fact_binding_in_lhs_column1246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fact_in_lhs_column1255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_from_statement1283 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_from_source_in_from_statement1287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_from_source1332 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_from_source1334 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_from_source1338 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_square_chunk_in_from_source1343 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_from_source1369 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_from_source1371 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_from_source1375 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_paren_chunk_in_from_source1379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_from_source1400 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_paren_chunk_in_from_source1404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_from_source1427 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_from_source1429 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_from_source1433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_accumulate_statement1486 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_accumulate_statement1488 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_accumulate_statement1498 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_lhs_column_in_accumulate_statement1502 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_accumulate_statement1504 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_accumulate_statement1513 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_paren_chunk_in_accumulate_statement1517 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_accumulate_statement1519 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_accumulate_statement1528 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_paren_chunk_in_accumulate_statement1532 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_accumulate_statement1534 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_accumulate_statement1543 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_paren_chunk_in_accumulate_statement1547 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_accumulate_statement1549 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_collect_statement1592 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_53_in_collect_statement1594 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_collect_statement1604 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_lhs_column_in_collect_statement1608 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_collect_statement1610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_fact_binding1645 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_fact_binding1655 = new BitSet(new long[]{0x0000000000100010L});
    public static final BitSet FOLLOW_fact_expression_in_fact_binding1659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_fact_expression1691 = new BitSet(new long[]{0x0000000000100010L});
    public static final BitSet FOLLOW_fact_expression_in_paren_in_fact_expression1695 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_fact_expression1698 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fact_in_fact_expression1709 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_fact_expression_in_paren1740 = new BitSet(new long[]{0x0000000000100010L});
    public static final BitSet FOLLOW_fact_expression_in_paren_in_fact_expression_in_paren1744 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_fact_expression_in_paren1746 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fact_in_fact_expression_in_paren1757 = new BitSet(new long[]{0x00C0000000000002L});
    public static final BitSet FOLLOW_set_in_fact_expression_in_paren1770 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_fact_in_fact_expression_in_paren1787 = new BitSet(new long[]{0x00C0000000000002L});
    public static final BitSet FOLLOW_dotted_name_in_fact1826 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_fact1839 = new BitSet(new long[]{0x0000000000200010L});
    public static final BitSet FOLLOW_constraints_in_fact1847 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_fact1868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constraint_in_constraints1901 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_predicate_in_constraints1904 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_34_in_constraints1912 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_constraint_in_constraints1915 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_predicate_in_constraints1918 = new BitSet(new long[]{0x0000000400000002L});
    public static final BitSet FOLLOW_ID_in_constraint1947 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_constraint1949 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_constraint1959 = new BitSet(new long[]{0xFC00000000000002L,0x0000000000000007L});
    public static final BitSet FOLLOW_constraint_expression_in_constraint1975 = new BitSet(new long[]{0x0300000000000002L});
    public static final BitSet FOLLOW_set_in_constraint1994 = new BitSet(new long[]{0xFC00000000000000L,0x0000000000000007L});
    public static final BitSet FOLLOW_constraint_expression_in_constraint2011 = new BitSet(new long[]{0x0300000000000002L});
    public static final BitSet FOLLOW_set_in_constraint_expression2063 = new BitSet(new long[]{0x0000000000100790L,0x0000000000000008L});
    public static final BitSet FOLLOW_ID_in_constraint_expression2130 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_constraint_in_constraint_expression2146 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_constraint_in_constraint_expression2169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_retval_constraint_in_constraint_expression2183 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_literal_constraint2222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_literal_constraint2233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_literal_constraint2246 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BOOL_in_literal_constraint2257 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_literal_constraint2269 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_enum_constraint2300 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_31_in_enum_constraint2302 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_enum_constraint2306 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_predicate2328 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_40_in_predicate2330 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_predicate2334 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_68_in_predicate2336 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_paren_chunk_in_predicate2340 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_paren_chunk2374 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_paren_chunk2397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFT_SQUARE_in_square_chunk2428 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_RIGHT_SQUARE_in_square_chunk2451 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_paren_chunk_in_retval_constraint2478 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lhs_and_in_lhs_or2515 = new BitSet(new long[]{0x00C0000000000002L});
    public static final BitSet FOLLOW_set_in_lhs_or2524 = new BitSet(new long[]{0x0000000000100010L,0x0000000000000380L});
    public static final BitSet FOLLOW_lhs_and_in_lhs_or2534 = new BitSet(new long[]{0x00C0000000000002L});
    public static final BitSet FOLLOW_lhs_unary_in_lhs_and2574 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000060L});
    public static final BitSet FOLLOW_set_in_lhs_and2583 = new BitSet(new long[]{0x0000000000100010L,0x0000000000000380L});
    public static final BitSet FOLLOW_lhs_unary_in_lhs_and2593 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000060L});
    public static final BitSet FOLLOW_lhs_exist_in_lhs_unary2630 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_lhs_not_in_lhs_unary2638 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_lhs_eval_in_lhs_unary2646 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_lhs_column_in_lhs_unary2654 = new BitSet(new long[]{0x0001000008000002L});
    public static final BitSet FOLLOW_from_statement_in_lhs_unary2674 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_accumulate_statement_in_lhs_unary2696 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_collect_statement_in_lhs_unary2717 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_lhs_unary2740 = new BitSet(new long[]{0x0000000000100010L,0x0000000000000380L});
    public static final BitSet FOLLOW_lhs_in_lhs_unary2744 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_lhs_unary2746 = new BitSet(new long[]{0x0000000008000002L});
    public static final BitSet FOLLOW_opt_semicolon_in_lhs_unary2756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_71_in_lhs_exist2780 = new BitSet(new long[]{0x0000000000100010L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_lhs_exist2783 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_lhs_column_in_lhs_exist2787 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_lhs_exist2789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lhs_column_in_lhs_exist2795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_72_in_lhs_not2825 = new BitSet(new long[]{0x0000000000100010L});
    public static final BitSet FOLLOW_LEFT_PAREN_in_lhs_not2828 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_lhs_column_in_lhs_not2832 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_RIGHT_PAREN_in_lhs_not2835 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_lhs_column_in_lhs_not2841 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_lhs_eval2869 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_paren_chunk_in_lhs_eval2873 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_dotted_name2904 = new BitSet(new long[]{0x0000000080400002L});
    public static final BitSet FOLLOW_31_in_dotted_name2910 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ID_in_dotted_name2914 = new BitSet(new long[]{0x0000000080400002L});
    public static final BitSet FOLLOW_LEFT_SQUARE_in_dotted_name2923 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_RIGHT_SQUARE_in_dotted_name2925 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_ID_in_argument2955 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_LEFT_SQUARE_in_argument2961 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_RIGHT_SQUARE_in_argument2963 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_ID_in_word2991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_29_in_word3003 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_word3012 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_word3024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_word3035 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_word3045 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_word3053 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_39_in_word3061 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RHS_in_word3072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_word3083 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_word3097 = new BitSet(new long[]{0x0000000000000002L});

}