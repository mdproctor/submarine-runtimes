grammar RuleParser; 

@parser::header {
	package org.drools.lang;
	import java.util.List;
	import java.util.ArrayList;
	import java.util.Iterator;
	import java.util.StringTokenizer;
	import org.drools.lang.descr.*;
}

@parser::members {
	private ExpanderResolver expanderResolver;
	private Expander expander;

	private PackageDescr packageDescr;
	
	public PackageDescr getPackageDescr() {
		return packageDescr;
	}
	
	public void setExpanderResolver(ExpanderResolver expanderResolver) {
		this.expanderResolver = expanderResolver;
	}
	
	public ExpanderResolver getExpanderResolver() {
		return expanderResolver;
	}
	
	private void runWhenExpander(String text, AndDescr descrs) throws RecognitionException {
		String expanded = text.trim();
		if (expanded.startsWith(">")) {
			expanded = expanded.substring(1);  //escape !!
		} else {
			expanded = expander.expand( "when", text );			
		}
		reparseLhs( expanded, descrs );
	}

	private void reparseLhs(String text, AndDescr descrs) throws RecognitionException {
		CharStream charStream = new ANTLRStringStream( text );
		RuleParserLexer lexer = new RuleParserLexer( charStream );
		TokenStream tokenStream = new CommonTokenStream( lexer );
		RuleParser parser = new RuleParser( tokenStream );
		
		parser.normal_lhs_block(descrs);
	}
	
	private String runThenExpander(String text) {
		//System.err.println( "expand THEN [" + text + "]" );
		StringTokenizer lines = new StringTokenizer( text, "\n\r" );

		StringBuffer expanded = new StringBuffer();
		
		String eol = System.getProperty( "line.separator" );
				
		while ( lines.hasMoreTokens() ) {
			String line = lines.nextToken();
			line = line.trim();
			if ( line.length() > 0 ) {
				if ( line.startsWith( ">" ) ) {
					expanded.append( line.substring( 1 ) );
					expanded.append( eol );
				} else {
					expanded.append( expander.expand( "then", line ) );
					expanded.append( eol );
				}
			}
		}
		
		return expanded.toString();
	}
	

	
	private String getString(Token token) {
		String orig = token.getText();
		return orig.substring( 1, orig.length() -1 );
	}
}

@lexer::header {
	package org.drools.lang;
}

opt_eol	:
		EOL*	
	;

compilation_unit
	:	opt_eol
		prolog 
		(	r=rule 	{this.packageDescr.addRule( r ); } 
		| 	q=query	{this.packageDescr.addRule( q ); }
		)*
	;
	
prolog
	@init {
		String packageName = "";
	}
	:	opt_eol
		( name=package_statement { packageName = name; } )?
		{ 
			this.packageDescr = new PackageDescr( name ); 
		}
		import_statement*
		expander? 
		global*
		function*
		opt_eol
	;
	
package_statement returns [String packageName]
	@init{
		packageName = null;
	}
	:	
		'package' opt_eol name=dotted_name ';'? opt_eol
		{
			packageName = name;
		}
	;
	
import_statement
	:	'import' opt_eol name=dotted_name ';'? opt_eol
		{
			packageDescr.addImport( name );
		}	
	;

expander
	@init {
		String config=null;
	}
	:	'expander' (name=dotted_name)? ';'? opt_eol
		{
			if (expanderResolver == null) 
				throw new IllegalArgumentException("Unable to use expander. Make sure a expander or dsl config is being passed to the parser. [ExpanderResolver was not set].");
			expander = expanderResolver.get( name, config );
		}
	;
	
global
	@init {
	}
	:
		'global' type=dotted_name id=ID ';'? opt_eol
		{
			packageDescr.addGlobal( id.getText(), type );
		}
	;
	
function
	@init {
		FunctionDescr f = null;
	}
	:
		'function' opt_eol (retType=dotted_name)? opt_eol name=ID opt_eol
		{
			//System.err.println( "function :: " + name.getText() );
			f = new FunctionDescr( name.getText(), retType );
		} 
		'(' opt_eol
			(	(paramType=dotted_name)? opt_eol paramName=ID opt_eol
				{
					f.addParameter( paramType, paramName.getText() );
				}
				(	',' opt_eol (paramType=dotted_name)? opt_eol paramName=ID opt_eol 
					{
						f.addParameter( paramType, paramName.getText() );
					}
				)*
			)?
		')'
		opt_eol
		'{'
			body=curly_chunk
			{
				f.setText( body );
			}
		'}'
		{
			packageDescr.addFunction( f );
		}
		opt_eol
	;


query returns [QueryDescr query]
	@init {
		query = null;
	}
	:
		opt_eol
		loc='query' queryName=word opt_eol 
		{ 
			query = new QueryDescr( queryName, null ); 
			query.setLocation( loc.getLine(), loc.getCharPositionInLine() );
			AndDescr lhs = new AndDescr(); query.setLhs( lhs ); 
			lhs.setLocation( loc.getLine(), loc.getCharPositionInLine() );
		}
		(
			{ expander != null }? expander_lhs_block[lhs]
			| normal_lhs_block[lhs]
		)
					
		'end' opt_eol
	;

rule returns [RuleDescr rule]
	@init {
		rule = null;
		String consequence = "";
	}
	:
		opt_eol
		loc='rule' ruleName=word opt_eol 
		{ 
			rule = new RuleDescr( ruleName, null ); 
			rule.setLocation( loc.getLine(), loc.getCharPositionInLine() );
		}
		(	rule_attributes[rule]
		)?
		opt_eol
		(	loc='when' ':'? opt_eol
			{ 
				AndDescr lhs = new AndDescr(); rule.setLhs( lhs ); 
				lhs.setLocation( loc.getLine(), loc.getCharPositionInLine() );
			}
			(
				{ expander != null }? expander_lhs_block[lhs]
				| normal_lhs_block[lhs]
			)
					
		)?
		'then' ':'?  opt_eol
		( options{greedy=false;} : any=.
			{
				consequence = consequence + " " + any.getText();
			}
		)*
		{
			if ( expander != null ) {
				String expanded = runThenExpander( consequence );
				rule.setConsequence( expanded );
			} else { 
				rule.setConsequence( consequence ); 
			}
		}
		'end' opt_eol
	;
	

rule_attributes[RuleDescr rule]
	: 
			'attributes'? ':'? opt_eol
			(	','? a=rule_attribute opt_eol
				{
					rule.addAttribute( a );
				}
			)*
	;
	
rule_attribute returns [AttributeDescr d]
	@init {
		d = null;
	}
	:
			a=salience { d = a; }
		|	a=no_loop  { d = a; }
		|	a=agenda_group  { d = a; }		
		|	a=duration  { d = a; }			
		|	a=xor_group { d = a; }	
		|	a=auto_focus { d = a; }	
		
	;
	
salience returns [AttributeDescr d ]
	@init {
		d = null;
	}
	:	
		loc='salience' opt_eol i=INT ';'? opt_eol
		{
			d = new AttributeDescr( "salience", i.getText() );
			d.setLocation( loc.getLine(), loc.getCharPositionInLine() );
		}
	;
	
no_loop returns [AttributeDescr d]
	@init {
		d = null;
	}
	:
		(
			loc='no-loop' opt_eol ';'? opt_eol
			{
				d = new AttributeDescr( "no-loop", "true" );
				d.setLocation( loc.getLine(), loc.getCharPositionInLine() );
			}
		) 
		|
		(
			loc='no-loop' t=BOOL opt_eol ';'? opt_eol
			{
				d = new AttributeDescr( "no-loop", t.getText() );
				d.setLocation( loc.getLine(), loc.getCharPositionInLine() );
			}
		
		)
		
	;
	
auto_focus returns [AttributeDescr d]
	@init {
		d = null;
	}
	:
		(
			loc='auto-focus' opt_eol ';'? opt_eol
			{
				d = new AttributeDescr( "auto-focus", "true" );
				d.setLocation( loc.getLine(), loc.getCharPositionInLine() );
			}
		) 
		|
		(
			loc='auto-focus' t=BOOL opt_eol ';'? opt_eol
			{
				d = new AttributeDescr( "auto-focus", t.getText() );
				d.setLocation( loc.getLine(), loc.getCharPositionInLine() );
			}
		
		)
		
	;	
	
xor_group returns [AttributeDescr d]
	@init {
		d = null;
	}
	:
		loc='xor-group' opt_eol name=STRING ';'? opt_eol
		{
			d = new AttributeDescr( "xor-group", getString( name ) );
			d.setLocation( loc.getLine(), loc.getCharPositionInLine() );
		}
	;

agenda_group returns [AttributeDescr d]
	@init {
		d = null;
	}
	:
		loc='agenda-group' opt_eol name=STRING ';'? opt_eol
		{
			d = new AttributeDescr( "agenda-group", getString( name ) );
			d.setLocation( loc.getLine(), loc.getCharPositionInLine() );
		}
	;		


duration returns [AttributeDescr d]
	@init {
		d = null;
	}
	:
		loc='duration' opt_eol i=INT ';'? opt_eol
		{
			d = new AttributeDescr( "duration", i.getText() );
			d.setLocation( loc.getLine(), loc.getCharPositionInLine() );
		}
	;		
	

normal_lhs_block[AndDescr descrs]
	:
		(	d=lhs
			{ descrs.addDescr( d ); }
		)*
	;

	

	
expander_lhs_block[AndDescr descrs]

	:
		(options{greedy=false;} : 
			text=paren_chunk EOL 
			{
				//only expand non null
				if (text != null) {
					runWhenExpander( text, descrs);
					text = null;
				}
			}
			
		)*

	;
	
	
	
lhs returns [PatternDescr d]
	@init {
		d=null;
	}
	:	l=lhs_or { d = l; }
	;

	
lhs_column returns [PatternDescr d]
	@init {
		d=null;
	}
	:	f=fact_binding	{ d = f; }
	|	f=fact		{ d = f; }
	;
 	
fact_binding returns [PatternDescr d]
	@init {
		d=null;
		boolean multi=false;
	}
 	:
 		id=ID 
 		
 		opt_eol ':' opt_eol 
 		f=fact opt_eol
 		{
 			((ColumnDescr)f).setIdentifier( id.getText() );
 			d = f;
 		}
 		(	'or'
 			{	if ( ! multi ) {
 					PatternDescr first = d;
 					d = new OrDescr();
 					((OrDescr)d).addDescr( first );
 					multi=true;
 				}
 			}
 			f=fact
 			{
 				((ColumnDescr)f).setIdentifier( id.getText() );
 				((OrDescr)d).addDescr( f );
 			}
 		)*	
 	;
 
fact returns [PatternDescr d] 
	@init {
		d=null;
	}
 	:	id=ID 
 		{ 
 			d = new ColumnDescr( id.getText() ); 
 			d.setLocation( id.getLine(), id.getCharPositionInLine() );
 		} opt_eol 
 		'(' opt_eol (	c=constraints
 				{
		 			for ( Iterator cIter = c.iterator() ; cIter.hasNext() ; ) {
 						((ColumnDescr)d).addDescr( (PatternDescr) cIter.next() );
 					}
 				}
 
 				)? opt_eol ')' opt_eol
 	;
	
	
constraints returns [List constraints]
	@init {
		constraints = new ArrayList();
	}
	:	opt_eol
		(constraint[constraints]|predicate[constraints])
		( opt_eol ',' opt_eol (constraint[constraints]|predicate[constraints]))*
		opt_eol
	;
	
constraint[List constraints]
	@init {
		PatternDescr d = null;
	}
	:	opt_eol
		( fb=ID opt_eol ':' opt_eol )? 
		f=ID	
		{
			if ( fb != null ) {
				//System.err.println( "fb: " + fb.getText() );
				//System.err.println( " f: " + f.getText() );
				d = new FieldBindingDescr( f.getText(), fb.getText() );
				//System.err.println( "fbd: " + d );
				
				d.setLocation( f.getLine(), f.getCharPositionInLine() );
				constraints.add( d );
			} 
		}
			opt_eol (	op=(	'=='
					|	'>'
					|	'>='
					|	'<'
					|	'<='
					|	'!='
					|	'contains'
					|	'matches'
					) opt_eol	
					
					(	bvc=ID
						{
							d = new BoundVariableDescr( f.getText(), op.getText(), bvc.getText() );
							d.setLocation( f.getLine(), f.getCharPositionInLine() );
							constraints.add( d );
						}
					|
						lc=enum_constraint 
						{ 
							d = new LiteralDescr( f.getText(), op.getText(), lc, true ); 
							d.setLocation( f.getLine(), f.getCharPositionInLine() );
							constraints.add( d );
						}						
					|
						lc=literal_constraint 
						{ 
							d = new LiteralDescr( f.getText(), op.getText(), lc ); 
							d.setLocation( f.getLine(), f.getCharPositionInLine() );
							constraints.add( d );
						}
					|	rvc=retval_constraint 
						{ 
							d = new ReturnValueDescr( f.getText(), op.getText(), rvc ); 
							d.setLocation( f.getLine(), f.getCharPositionInLine() );
							constraints.add( d );
						} 
					)
				)?					
		opt_eol
	;
		
literal_constraint returns [String text]
	@init {
		text = null;
	}
	:	(	t=STRING { text = getString( t ); } //t.getText(); text=text.substring( 1, text.length() - 1 ); }
		|	t=INT    { text = t.getText(); }
		|	t=FLOAT	 { text = t.getText(); }
		|	t=BOOL 	 { text = t.getText(); }
		)
	;
	
enum_constraint returns [String text]
	@init {
		text = null;
	}
	:	(cls=ID '.' enum=ID) { text = cls.getText() + "." + enum.getText(); }
	;	
	
retval_constraint returns [String text]
	@init {
		text = null;
	}
	:	
		'(' c=paren_chunk ')' { text = c; }
	;

predicate[List constraints]
	:
		decl=ID ':' field=ID '->' '(' text=paren_chunk ')'
		{
			PredicateDescr d = new PredicateDescr(field.getText(), decl.getText(), text );
			constraints.add( d );
		}
	;
	
paren_chunk returns [String text]
	@init {
		text = null;
	}
	
	:
		(	options{greedy=false;} : 
			'(' c=paren_chunk ')' 	
			{
				//System.err.println( "chunk [" + c + "]" );
				if ( c == null ) {
					c = "";
				}
				if ( text == null ) {
					text = "( " + c + " )";
				} else {
					text = text + " ( " + c + " )";
				}
			} 
		| any=. 
			{
				//System.err.println( "any [" + any.getText() + "]" );
				if ( text == null ) {
					text = any.getText();
				} else {
					text = text + " " + any.getText(); 
				} 
			}
		)*
	;
	
curly_chunk returns [String text]
	@init {
		text = null;
	}
	
	:
		(	options{greedy=false;} : 
			'{' c=curly_chunk '}' 	
			{
				//System.err.println( "chunk [" + c + "]" );
				if ( c == null ) {
					c = "";
				}
				if ( text == null ) {
					text = "{ " + c + " }";
				} else {
					text = text + " { " + c + " }";
				}
			} 
		| any=. 
			{
				//System.err.println( "any [" + any.getText() + "]" );
				if ( text == null ) {
					text = any.getText();
				} else {
					text = text + " " + any.getText(); 
				} 
			}
		)*
	;	
	
lhs_or returns [PatternDescr d]
	@init{
		d = null;
	}
	:	
		{ OrDescr or = null; }
		left=lhs_and {d = left; }
		( 	('or'|'||') 
			right=lhs_and 
			{
				if ( or == null ) {
					or = new OrDescr();
					or.addDescr( left );
					d = or;
				}
				
				or.addDescr( right );
			}
		)*
	;
	
lhs_and returns [PatternDescr d]
	@init{
		d = null;
	}
	:
		{ AndDescr and = null; }
		left=lhs_unary { d = left; }
		(	('and'|'&&') 
			right=lhs_unary 
			{
				if ( and == null ) {
					and = new AndDescr();
					and.addDescr( left );
					d = and;
				}
				
				and.addDescr( right );
			}
		)* 
	;
	
lhs_unary returns [PatternDescr d]
	@init {
		d = null;
	}
	:	(	u=lhs_exist
		|	u=lhs_not
		|	u=lhs_eval
		|	u=lhs_column
		|	'(' u=lhs ')'
		) { d = u; }
	;
	
lhs_exist returns [PatternDescr d]
	@init {
		d = null;
	}
	:	loc='exists' column=lhs_column 
		{ 
			d = new ExistsDescr( (ColumnDescr) column ); 
			d.setLocation( loc.getLine(), loc.getCharPositionInLine() );
		}	
	;
	
lhs_not	returns [NotDescr d]
	@init {
		d = null;
	}
	:	loc='not' column=lhs_column 
		{
			d = new NotDescr( (ColumnDescr) column ); 
			d.setLocation( loc.getLine(), loc.getCharPositionInLine() );
		}
	;

lhs_eval returns [PatternDescr d]
	@init {
		d = null;
		String text = "";
	}
	:	'eval' '(' c=paren_chunk ')' 
		{ d = new EvalDescr( c ); }
	;
	
dotted_name returns [String name]
	@init {
		name = null;
	}
	:	
		id=ID { name=id.getText(); } ( '.' id=ID { name = name + "." + id.getText(); } )*
	;
	
	
word returns [String word]
	@init{
		word = null;
	}
	:	id=ID      { word=id.getText(); }
	|	'import'   { word="import"; }
	|	'use'      { word="use"; }
	|	'rule'     { word="rule"; }
	|	'query'    { word="query"; }
	|	'salience' { word="salience"; }
 	|	'no-loop'  { word="no-loop"; }
	|	'when'     { word="when"; }
	|	'then'     { word="then"; }
	|	'end'      { word="end"; }
	|	str=STRING { word=getString(str);} //str.getText(); word=word.substring( 1, word.length()-1 ); }
	;


MISC 	:
		'!' | '@' | '$' | '%' | '^' | '&' | '*' | '_' | '-' | '+' | '|' | ',' | '{' | '}' | '[' | ']' | ';'  | '=' | '/' | '(' | ')' | '\'' | '\\'
	;

WS      :       (	' '
                |	'\t'
                |	'\f'
                )
                { channel=99; }
        ;
        
EOL 	:	     
   		(       '\r\n'  // Evil DOS
                |       '\r'    // Macintosh
                |       '\n'    // Unix (the right way)
                )
        ;  
        
INT	
	:	('-')?('0'..'9')+
	;

FLOAT
	:	('0'..'9')+ '.' ('0'..'9')+
	;
	
STRING
	:	'"' ( options{greedy=false;} : .)* '"' 
	;
	
BOOL
	:	('true'|'false') 
	;	
	
ID	
	:	('a'..'z'|'A'..'Z'|'_'|'$')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* 
	;
		

SH_STYLE_SINGLE_LINE_COMMENT	
	:	'#' ( options{greedy=false;} : .)* EOL /* ('\r')? '\n'  */
                { channel=99; }
	;
        
        
C_STYLE_SINGLE_LINE_COMMENT	
	:	'//' ( options{greedy=false;} : .)* EOL // ('\r')? '\n' 
                { channel=99; }
	;

MULTI_LINE_COMMENT
	:	'/*' (options{greedy=false;} : .)* '*/'
                { channel=99; }
	;
