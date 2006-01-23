package org.drools.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.drools.rule.DuplicateRuleNameException;
import org.drools.rule.Rule;
import org.drools.rule.RuleConstructionException;

public class Parser {
	
	private static Pattern PACKAGE_DECL = Pattern.compile( "\\s*package\\s*([^;]+);?\\s*" );
	private static Pattern IMPORT_STATEMENT = Pattern.compile( "\\s*import\\s*([^;]+);?\\s*" );
	private static Pattern RULE_DECL = Pattern.compile( "\\s*rule\\s*([^\\s]+)\\s*" );
	
	private static Pattern FACT_BINDING = Pattern.compile( "\\s*(\\w+)\\s*=>\\s*(.*)" );
	private static Pattern FIELD_BINDING = Pattern.compile( "\\s*(\\w+):(\\w+)\\s*" );
	
	private BufferedReader reader;
	
	private String packageDeclaration;
	private List imports;
	private List rules;

	public Parser(Reader reader) {
		this.reader  = new BufferedReader( reader );
		this.imports = new ArrayList();
		this.rules   = new ArrayList();
	}
	
	public String getPackageDeclaration() {
		return packageDeclaration;
	}
	
	public List getImports() {
		return imports;
	}
	
	public List getRules() {
		return rules;
	}
	
	public void parse() throws IOException, RuleConstructionException {
		prolog();
		rules();
	}
	
	protected void prolog() throws IOException {
		packageDeclaration();
		importStatements();
		functions();
	}
	
	protected void packageDeclaration() throws IOException {
		String line = laDiscard();
		
		if ( line == null ) { return; }
		
		Matcher matcher = PACKAGE_DECL.matcher( line );
		
		if ( matcher.matches() ) {
			consumeDiscard();
			packageDeclaration = matcher.group( 1 );
		}
	}
	
	protected void importStatements() throws IOException {
		while ( importStatement() ) {
			// just do it again
		}
	}
	
	protected boolean importStatement() throws IOException {
		String line = laDiscard();
		
		if ( line == null ) { return false; }
		
		Matcher matcher = IMPORT_STATEMENT.matcher( line );
		
		if ( matcher.matches() ) {
			consumeDiscard();
			imports.add( matcher.group( 1 ) );
			return true;
		}
		
		return false;
	}
	
	protected void functions() {
		
	}
	
	protected void rules() throws IOException, RuleConstructionException {
		while ( rule() ) {
			// do it again!
		}
	}
	
	protected boolean rule() throws IOException, RuleConstructionException {
		String line = laDiscard();
		
		if ( line == null ) { return false; }
		
		Matcher matcher = RULE_DECL.matcher( line );
		
		if ( matcher.matches() ) {
			consumeDiscard();
			String ruleName = matcher.group(1);
			
			for ( Iterator ruleIter = rules.iterator() ; ruleIter.hasNext() ; ) {
				Rule rule = (Rule) ruleIter.next();
				if ( rule.getName().equals( ruleName ) ) {
					throw new DuplicateRuleNameException( null, rule, null );
				}
			}
			rules.add( new Rule( ruleName ) );
		}
		
		ruleConditions();
		ruleConsequence();
		
		line = laDiscard();
		
		if ( ! line.trim().equals( "end" ) ) {
			throw new ParseException( "end expected" );
		}
		
		consumeDiscard();
		
		return true;
	}
	
	protected void ruleConditions() throws IOException {
		String line = laDiscard();
		
		if ( line == null ) { return; }
		
		if ( line.trim().equals( "when" ) ) {
			consumeDiscard();
		} else {
			return;
		}
		
		while ( ruleCondition() ) {
			// do it again!
		}
	}
	
	protected boolean ruleCondition() throws IOException {
		String line = laDiscard();
		
		
		if ( line == null ) {
			return false;
		}
		
		line = line.trim();
		
		if ( line.equals( "then" ) || line.equals( "end" ) ) {
			return false;
		}
		
		Matcher matcher = FACT_BINDING.matcher( line.trim() );
		
		String name = null;
		String pattern = null;
		
		consumeDiscard();
		
		if ( matcher.matches() ) {
			name    = matcher.group( 1 );
			pattern = matcher.group( 2 );
		} else {
			pattern = line;
		}
		
		if ( pattern.startsWith( ":" ) ) {
			pattern = expand( pattern );
		}
		
		pattern( pattern );
		
		return true;
	}
	
	protected String expand(String pattern) {
		// TODO: Michael, here goes the expansion bits
		return pattern;
	}
	
	protected void pattern(String pattern) {
		int leftParen = pattern.indexOf( "(" );
		
		if ( leftParen < 0 ) {
			throw new ParseException( "invalid pattern: " + pattern );
		}
		
		int rightParen = pattern.lastIndexOf( ")" );
		
		if ( rightParen < 0 ) {
			throw new ParseException( "invalid pattern: " + pattern );
		}
		
		String guts = pattern.substring( leftParen+1, rightParen ).trim();
		
		// TODO: Michael, should be also expand the guts?
		
		StringTokenizer tokens = new StringTokenizer( guts, "," );
		
		while ( tokens.hasMoreTokens() ) {
			constraint( tokens.nextToken().trim() );
			//System.err.println( "constraint [" + tokens.nextToken().trim() + "]" );
		}
	}
	
	protected void constraint(String constraint) {
		Matcher matcher = FIELD_BINDING.matcher( constraint );
		
		if ( matcher.matches() ) {
			String bindTo = matcher.group( 1 );
			String field = matcher.group( 2 );
			System.err.println( "bind [" + bindTo + "] to field [" + field + "]" );
		} else {
			// TODO: Michael, want to jack in here also?
			System.err.println( "further work required for [" + constraint + "]" );
		}
	}
	
	protected void ruleConsequence() throws IOException {
		String line = laDiscard();
		
		if ( line.trim().equals( "then" ) ) {
			consumeDiscard();
			
			StringBuffer consequence = new StringBuffer();
			
			while ( true ) {
				line = laDiscard();
				if ( line == null ) {
					throw new ParseException( "end expected" );
				}
				if ( line.trim().equals( "end" ) ) {
					break;
				}
				consumeDiscard();
				consequence.append( line + "\n" );
			}
			System.err.println( "begin consequence");
			System.err.println( consequence );
			System.err.println( "end consequence");
		}
	}
	
	protected String la() throws IOException {
		reader.mark( 1024 );
		String line = reader.readLine();
		reader.reset();
		return line;
	}
	
	protected String laDiscard() throws IOException {
		reader.mark( 1024 );
		String line = null;
		
		while ( line == null ) {
			line = reader.readLine();
			
			if ( line == null ) {
				reader.reset();
				return null;
			}
			
			String trimLine = line.trim();
			
			if ( trimLine.length() == 0 || trimLine.startsWith( "#" ) || trimLine.startsWith( "//" ) ) {
				line = null;
			}
		}
		
		reader.reset();
		return line;
	}
	
	protected String consume() throws IOException {
		return reader.readLine();
	}
	
	protected String consumeDiscard() throws IOException {
		String line = null;
		
		while ( line == null ) {
			line = reader.readLine();
			
			if ( line == null ) {
				reader.reset();
				return null;
			}
			
			String trimLine = line.trim();
			
			if ( trimLine.length() == 0 || trimLine.startsWith( "#" ) || trimLine.startsWith( "//" ) ) {
				line = null;
			}
		}
		
		return line;
	}
}
