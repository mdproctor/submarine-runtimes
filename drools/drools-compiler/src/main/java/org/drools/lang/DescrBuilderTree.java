// $ANTLR 3.0.1 src/main/resources/org/drools/lang/DescrBuilderTree.g 2008-10-17 12:12:29

	package org.drools.lang;

	import java.util.HashMap;
	import java.util.Map;
	import java.util.LinkedList;
	import org.drools.lang.descr.AccessorDescr;
	import org.drools.lang.descr.AccumulateDescr;
	import org.drools.lang.descr.AndDescr;
	import org.drools.lang.descr.AttributeDescr;
	import org.drools.lang.descr.BaseDescr;
	import org.drools.lang.descr.BehaviorDescr;
	import org.drools.lang.descr.DeclarativeInvokerDescr;
	import org.drools.lang.descr.DescrFactory;
	import org.drools.lang.descr.FactTemplateDescr;
	import org.drools.lang.descr.FieldConstraintDescr;
	import org.drools.lang.descr.FieldTemplateDescr;
	import org.drools.lang.descr.FromDescr;
	import org.drools.lang.descr.FunctionDescr;
	import org.drools.lang.descr.FunctionImportDescr;
	import org.drools.lang.descr.GlobalDescr;
	import org.drools.lang.descr.ImportDescr;
	import org.drools.lang.descr.PackageDescr;
	import org.drools.lang.descr.PatternSourceDescr;
	import org.drools.lang.descr.QueryDescr;
	import org.drools.lang.descr.RuleDescr;
	import org.drools.lang.descr.TypeDeclarationDescr;
	import org.drools.lang.descr.TypeFieldDescr;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class DescrBuilderTree extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "VT_COMPILATION_UNIT", "VT_FUNCTION_IMPORT", "VT_FACT", "VT_CONSTRAINTS", "VT_LABEL", "VT_QUERY_ID", "VT_TEMPLATE_ID", "VT_TYPE_DECLARE_ID", "VT_RULE_ID", "VT_ENTRYPOINT_ID", "VT_SLOT_ID", "VT_SLOT", "VT_RULE_ATTRIBUTES", "VT_RHS_CHUNK", "VT_CURLY_CHUNK", "VT_SQUARE_CHUNK", "VT_PAREN_CHUNK", "VT_BEHAVIOR", "VT_AND_IMPLICIT", "VT_AND_PREFIX", "VT_OR_PREFIX", "VT_AND_INFIX", "VT_OR_INFIX", "VT_ACCUMULATE_INIT_CLAUSE", "VT_ACCUMULATE_ID_CLAUSE", "VT_FROM_SOURCE", "VT_EXPRESSION_CHAIN", "VT_PATTERN", "VT_FACT_BINDING", "VT_FACT_OR", "VT_BIND_FIELD", "VT_FIELD", "VT_ACCESSOR_PATH", "VT_ACCESSOR_ELEMENT", "VT_DATA_TYPE", "VT_PATTERN_TYPE", "VT_PACKAGE_ID", "VT_IMPORT_ID", "VT_GLOBAL_ID", "VT_FUNCTION_ID", "VT_PARAM_LIST", "VK_DATE_EFFECTIVE", "VK_DATE_EXPIRES", "VK_LOCK_ON_ACTIVE", "VK_NO_LOOP", "VK_AUTO_FOCUS", "VK_ACTIVATION_GROUP", "VK_AGENDA_GROUP", "VK_RULEFLOW_GROUP", "VK_DURATION", "VK_DIALECT", "VK_SALIENCE", "VK_ENABLED", "VK_ATTRIBUTES", "VK_RULE", "VK_IMPORT", "VK_PACKAGE", "VK_TEMPLATE", "VK_QUERY", "VK_DECLARE", "VK_FUNCTION", "VK_GLOBAL", "VK_EVAL", "VK_CONTAINS", "VK_MATCHES", "VK_EXCLUDES", "VK_SOUNDSLIKE", "VK_MEMBEROF", "VK_ENTRY_POINT", "VK_NOT", "VK_IN", "VK_OR", "VK_AND", "VK_EXISTS", "VK_FORALL", "VK_ACTION", "VK_REVERSE", "VK_RESULT", "SEMICOLON", "ID", "DOT", "DOT_STAR", "END", "STRING", "LEFT_PAREN", "COMMA", "RIGHT_PAREN", "AT", "COLON", "EQUALS", "WHEN", "BOOL", "INT", "DOUBLE_PIPE", "DOUBLE_AMPER", "FROM", "OVER", "ACCUMULATE", "INIT", "COLLECT", "ARROW", "EQUAL", "GREATER", "GREATER_EQUAL", "LESS", "LESS_EQUAL", "NOT_EQUAL", "GRAVE_ACCENT", "FLOAT", "NULL", "LEFT_SQUARE", "RIGHT_SQUARE", "THEN", "LEFT_CURLY", "RIGHT_CURLY", "MISC", "EOL", "WS", "EscapeSequence", "HexDigit", "UnicodeEscape", "OctalEscape", "SH_STYLE_SINGLE_LINE_COMMENT", "C_STYLE_SINGLE_LINE_COMMENT", "MULTI_LINE_COMMENT"
    };
    public static final int COMMA=89;
    public static final int VT_PATTERN_TYPE=39;
    public static final int VT_ACCUMULATE_ID_CLAUSE=28;
    public static final int VK_DIALECT=54;
    public static final int VK_FUNCTION=64;
    public static final int END=86;
    public static final int HexDigit=123;
    public static final int VK_ATTRIBUTES=57;
    public static final int VT_EXPRESSION_CHAIN=30;
    public static final int MISC=119;
    public static final int VT_AND_PREFIX=23;
    public static final int VK_QUERY=62;
    public static final int THEN=116;
    public static final int VK_AUTO_FOCUS=49;
    public static final int DOT=84;
    public static final int VK_IMPORT=59;
    public static final int VT_SLOT=15;
    public static final int VT_PACKAGE_ID=40;
    public static final int LEFT_SQUARE=114;
    public static final int SH_STYLE_SINGLE_LINE_COMMENT=126;
    public static final int VT_DATA_TYPE=38;
    public static final int VK_MATCHES=68;
    public static final int VT_FACT=6;
    public static final int LEFT_CURLY=117;
    public static final int AT=91;
    public static final int LEFT_PAREN=88;
    public static final int DOUBLE_AMPER=98;
    public static final int VT_QUERY_ID=9;
    public static final int VT_ACCESSOR_PATH=36;
    public static final int VT_LABEL=8;
    public static final int WHEN=94;
    public static final int VT_ENTRYPOINT_ID=13;
    public static final int VK_SOUNDSLIKE=70;
    public static final int VK_SALIENCE=55;
    public static final int VT_FIELD=35;
    public static final int WS=121;
    public static final int OVER=100;
    public static final int STRING=87;
    public static final int VK_AND=76;
    public static final int VT_ACCESSOR_ELEMENT=37;
    public static final int VT_ACCUMULATE_INIT_CLAUSE=27;
    public static final int VK_GLOBAL=65;
    public static final int VK_REVERSE=80;
    public static final int VT_BEHAVIOR=21;
    public static final int GRAVE_ACCENT=111;
    public static final int VK_DURATION=53;
    public static final int VT_SQUARE_CHUNK=19;
    public static final int VK_FORALL=78;
    public static final int VT_PAREN_CHUNK=20;
    public static final int VT_COMPILATION_UNIT=4;
    public static final int COLLECT=103;
    public static final int VK_ENABLED=56;
    public static final int EQUALS=93;
    public static final int VK_RESULT=81;
    public static final int UnicodeEscape=124;
    public static final int VK_PACKAGE=60;
    public static final int VT_RULE_ID=12;
    public static final int EQUAL=105;
    public static final int VK_NO_LOOP=48;
    public static final int SEMICOLON=82;
    public static final int VK_TEMPLATE=61;
    public static final int VT_AND_IMPLICIT=22;
    public static final int NULL=113;
    public static final int COLON=92;
    public static final int MULTI_LINE_COMMENT=128;
    public static final int VT_RULE_ATTRIBUTES=16;
    public static final int RIGHT_SQUARE=115;
    public static final int VK_AGENDA_GROUP=51;
    public static final int VT_FACT_OR=33;
    public static final int VK_NOT=73;
    public static final int VK_DATE_EXPIRES=46;
    public static final int ARROW=104;
    public static final int FLOAT=112;
    public static final int INIT=102;
    public static final int VT_SLOT_ID=14;
    public static final int VT_CURLY_CHUNK=18;
    public static final int VT_OR_PREFIX=24;
    public static final int DOUBLE_PIPE=97;
    public static final int LESS=108;
    public static final int VT_TYPE_DECLARE_ID=11;
    public static final int VT_PATTERN=31;
    public static final int VK_DATE_EFFECTIVE=45;
    public static final int EscapeSequence=122;
    public static final int VK_EXISTS=77;
    public static final int INT=96;
    public static final int VT_BIND_FIELD=34;
    public static final int VK_RULE=58;
    public static final int VK_EVAL=66;
    public static final int GREATER=106;
    public static final int VT_FACT_BINDING=32;
    public static final int ID=83;
    public static final int FROM=99;
    public static final int NOT_EQUAL=110;
    public static final int RIGHT_CURLY=118;
    public static final int VK_ENTRY_POINT=72;
    public static final int VT_PARAM_LIST=44;
    public static final int VT_AND_INFIX=25;
    public static final int BOOL=95;
    public static final int VT_FROM_SOURCE=29;
    public static final int VK_CONTAINS=67;
    public static final int VK_LOCK_ON_ACTIVE=47;
    public static final int VT_FUNCTION_IMPORT=5;
    public static final int VK_IN=74;
    public static final int VT_RHS_CHUNK=17;
    public static final int VK_MEMBEROF=71;
    public static final int GREATER_EQUAL=107;
    public static final int VT_OR_INFIX=26;
    public static final int DOT_STAR=85;
    public static final int VK_OR=75;
    public static final int VT_GLOBAL_ID=42;
    public static final int LESS_EQUAL=109;
    public static final int ACCUMULATE=101;
    public static final int VK_RULEFLOW_GROUP=52;
    public static final int VT_FUNCTION_ID=43;
    public static final int EOF=-1;
    public static final int VT_CONSTRAINTS=7;
    public static final int VT_IMPORT_ID=41;
    public static final int EOL=120;
    public static final int VK_ACTIVATION_GROUP=50;
    public static final int OctalEscape=125;
    public static final int VK_ACTION=79;
    public static final int VK_EXCLUDES=69;
    public static final int RIGHT_PAREN=90;
    public static final int VT_TEMPLATE_ID=10;
    public static final int VK_DECLARE=63;
    public static final int C_STYLE_SINGLE_LINE_COMMENT=127;

        public DescrBuilderTree(TreeNodeStream input) {
            super(input);
        }
        

    public String[] getTokenNames() { return tokenNames; }
    public String getGrammarFileName() { return "src/main/resources/org/drools/lang/DescrBuilderTree.g"; }


    	DescrFactory factory = new DescrFactory();
    	PackageDescr packageDescr = null;
    	
    	public PackageDescr getPackageDescr() {
    		return packageDescr;
    	}



    // $ANTLR start compilation_unit
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:48:1: compilation_unit : ^( VT_COMPILATION_UNIT package_statement ( statement )* ) ;
    public final void compilation_unit() throws RecognitionException {
        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:49:2: ( ^( VT_COMPILATION_UNIT package_statement ( statement )* ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:49:4: ^( VT_COMPILATION_UNIT package_statement ( statement )* )
            {
            match(input,VT_COMPILATION_UNIT,FOLLOW_VT_COMPILATION_UNIT_in_compilation_unit49); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                pushFollow(FOLLOW_package_statement_in_compilation_unit51);
                package_statement();
                _fsp--;

                // src/main/resources/org/drools/lang/DescrBuilderTree.g:49:44: ( statement )*
                loop1:
                do {
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==VT_FUNCTION_IMPORT||(LA1_0>=VK_DATE_EFFECTIVE && LA1_0<=VK_ENABLED)||(LA1_0>=VK_RULE && LA1_0<=VK_IMPORT)||(LA1_0>=VK_TEMPLATE && LA1_0<=VK_GLOBAL)) ) {
                        alt1=1;
                    }


                    switch (alt1) {
                	case 1 :
                	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:49:44: statement
                	    {
                	    pushFollow(FOLLOW_statement_in_compilation_unit53);
                	    statement();
                	    _fsp--;


                	    }
                	    break;

                	default :
                	    break loop1;
                    }
                } while (true);


                match(input, Token.UP, null); 
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
    // $ANTLR end compilation_unit


    // $ANTLR start package_statement
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:52:1: package_statement returns [String packageName] : ( ^( VK_PACKAGE packageId= package_id ) | );
    public final String package_statement() throws RecognitionException {
        String packageName = null;

        List packageId = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:53:2: ( ^( VK_PACKAGE packageId= package_id ) | )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==VK_PACKAGE) ) {
                alt2=1;
            }
            else if ( (LA2_0==UP||LA2_0==VT_FUNCTION_IMPORT||(LA2_0>=VK_DATE_EFFECTIVE && LA2_0<=VK_ENABLED)||(LA2_0>=VK_RULE && LA2_0<=VK_IMPORT)||(LA2_0>=VK_TEMPLATE && LA2_0<=VK_GLOBAL)) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("52:1: package_statement returns [String packageName] : ( ^( VK_PACKAGE packageId= package_id ) | );", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:53:4: ^( VK_PACKAGE packageId= package_id )
                    {
                    match(input,VK_PACKAGE,FOLLOW_VK_PACKAGE_in_package_statement71); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_package_id_in_package_statement75);
                    packageId=package_id();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	this.packageDescr = factory.createPackage(packageId);	
                    		packageName = packageDescr.getName();	

                    }
                    break;
                case 2 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:57:2: 
                    {
                    	this.packageDescr = factory.createPackage(null);	
                    		packageName = "";	

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
        return packageName;
    }
    // $ANTLR end package_statement


    // $ANTLR start package_id
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:61:1: package_id returns [List idList] : ^( VT_PACKAGE_ID (tempList+= ID )+ ) ;
    public final List package_id() throws RecognitionException {
        List idList = null;

        DroolsTree tempList=null;
        List list_tempList=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:62:2: ( ^( VT_PACKAGE_ID (tempList+= ID )+ ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:62:4: ^( VT_PACKAGE_ID (tempList+= ID )+ )
            {
            match(input,VT_PACKAGE_ID,FOLLOW_VT_PACKAGE_ID_in_package_id102); 

            match(input, Token.DOWN, null); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:62:28: (tempList+= ID )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==ID) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:62:28: tempList+= ID
            	    {
            	    tempList=(DroolsTree)input.LT(1);
            	    match(input,ID,FOLLOW_ID_in_package_id106); 
            	    if (list_tempList==null) list_tempList=new ArrayList();
            	    list_tempList.add(tempList);


            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            match(input, Token.UP, null); 
            	idList = list_tempList;	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return idList;
    }
    // $ANTLR end package_id


    // $ANTLR start statement
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:66:1: statement : (a= rule_attribute | fi= function_import_statement | is= import_statement | gl= global | fn= function | tp= template | rl= rule | qr= query | td= type_declaration );
    public final void statement() throws RecognitionException {
        AttributeDescr a = null;

        FunctionImportDescr fi = null;

        ImportDescr is = null;

        global_return gl = null;

        function_return fn = null;

        template_return tp = null;

        rule_return rl = null;

        query_return qr = null;

        TypeDeclarationDescr td = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:67:2: (a= rule_attribute | fi= function_import_statement | is= import_statement | gl= global | fn= function | tp= template | rl= rule | qr= query | td= type_declaration )
            int alt4=9;
            switch ( input.LA(1) ) {
            case VK_DATE_EFFECTIVE:
            case VK_DATE_EXPIRES:
            case VK_LOCK_ON_ACTIVE:
            case VK_NO_LOOP:
            case VK_AUTO_FOCUS:
            case VK_ACTIVATION_GROUP:
            case VK_AGENDA_GROUP:
            case VK_RULEFLOW_GROUP:
            case VK_DURATION:
            case VK_DIALECT:
            case VK_SALIENCE:
            case VK_ENABLED:
                {
                alt4=1;
                }
                break;
            case VT_FUNCTION_IMPORT:
                {
                alt4=2;
                }
                break;
            case VK_IMPORT:
                {
                alt4=3;
                }
                break;
            case VK_GLOBAL:
                {
                alt4=4;
                }
                break;
            case VK_FUNCTION:
                {
                alt4=5;
                }
                break;
            case VK_TEMPLATE:
                {
                alt4=6;
                }
                break;
            case VK_RULE:
                {
                alt4=7;
                }
                break;
            case VK_QUERY:
                {
                alt4=8;
                }
                break;
            case VK_DECLARE:
                {
                alt4=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("66:1: statement : (a= rule_attribute | fi= function_import_statement | is= import_statement | gl= global | fn= function | tp= template | rl= rule | qr= query | td= type_declaration );", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:67:4: a= rule_attribute
                    {
                    pushFollow(FOLLOW_rule_attribute_in_statement124);
                    a=rule_attribute();
                    _fsp--;

                    	this.packageDescr.addAttribute(a);	

                    }
                    break;
                case 2 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:69:4: fi= function_import_statement
                    {
                    pushFollow(FOLLOW_function_import_statement_in_statement134);
                    fi=function_import_statement();
                    _fsp--;

                    	this.packageDescr.addFunctionImport(fi);	

                    }
                    break;
                case 3 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:71:4: is= import_statement
                    {
                    pushFollow(FOLLOW_import_statement_in_statement144);
                    is=import_statement();
                    _fsp--;

                    	this.packageDescr.addImport(is);	

                    }
                    break;
                case 4 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:73:4: gl= global
                    {
                    pushFollow(FOLLOW_global_in_statement155);
                    gl=global();
                    _fsp--;

                    	this.packageDescr.addGlobal(gl.globalDescr);	

                    }
                    break;
                case 5 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:75:4: fn= function
                    {
                    pushFollow(FOLLOW_function_in_statement165);
                    fn=function();
                    _fsp--;

                    	this.packageDescr.addFunction(fn.functionDescr);	

                    }
                    break;
                case 6 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:77:4: tp= template
                    {
                    pushFollow(FOLLOW_template_in_statement175);
                    tp=template();
                    _fsp--;

                    	this.packageDescr.addFactTemplate(tp.factTemplateDescr);	

                    }
                    break;
                case 7 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:79:4: rl= rule
                    {
                    pushFollow(FOLLOW_rule_in_statement185);
                    rl=rule();
                    _fsp--;

                    	this.packageDescr.addRule(rl.ruleDescr);	

                    }
                    break;
                case 8 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:81:4: qr= query
                    {
                    pushFollow(FOLLOW_query_in_statement195);
                    qr=query();
                    _fsp--;

                    	this.packageDescr.addRule(qr.queryDescr);	

                    }
                    break;
                case 9 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:83:4: td= type_declaration
                    {
                    pushFollow(FOLLOW_type_declaration_in_statement205);
                    td=type_declaration();
                    _fsp--;

                    	this.packageDescr.addTypeDeclaration(td);	

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
        return ;
    }
    // $ANTLR end statement


    // $ANTLR start import_statement
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:87:1: import_statement returns [ImportDescr importDescr] : ^(importStart= VK_IMPORT importId= import_name ) ;
    public final ImportDescr import_statement() throws RecognitionException {
        ImportDescr importDescr = null;

        DroolsTree importStart=null;
        import_name_return importId = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:88:2: ( ^(importStart= VK_IMPORT importId= import_name ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:88:4: ^(importStart= VK_IMPORT importId= import_name )
            {
            importStart=(DroolsTree)input.LT(1);
            match(input,VK_IMPORT,FOLLOW_VK_IMPORT_in_import_statement226); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_import_name_in_import_statement230);
            importId=import_name();
            _fsp--;


            match(input, Token.UP, null); 
            	importDescr = factory.createImport(importStart, importId.idList, importId.dotStar);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return importDescr;
    }
    // $ANTLR end import_statement


    // $ANTLR start function_import_statement
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:92:1: function_import_statement returns [FunctionImportDescr functionImportDescr] : ^(importStart= VT_FUNCTION_IMPORT VK_FUNCTION importId= import_name ) ;
    public final FunctionImportDescr function_import_statement() throws RecognitionException {
        FunctionImportDescr functionImportDescr = null;

        DroolsTree importStart=null;
        import_name_return importId = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:93:2: ( ^(importStart= VT_FUNCTION_IMPORT VK_FUNCTION importId= import_name ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:93:4: ^(importStart= VT_FUNCTION_IMPORT VK_FUNCTION importId= import_name )
            {
            importStart=(DroolsTree)input.LT(1);
            match(input,VT_FUNCTION_IMPORT,FOLLOW_VT_FUNCTION_IMPORT_in_function_import_statement252); 

            match(input, Token.DOWN, null); 
            match(input,VK_FUNCTION,FOLLOW_VK_FUNCTION_in_function_import_statement254); 
            pushFollow(FOLLOW_import_name_in_function_import_statement258);
            importId=import_name();
            _fsp--;


            match(input, Token.UP, null); 
            	functionImportDescr = factory.createFunctionImport(importStart, importId.idList, importId.dotStar);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return functionImportDescr;
    }
    // $ANTLR end function_import_statement

    public static class import_name_return extends TreeRuleReturnScope {
        public List idList;
        public DroolsTree dotStar;
    };

    // $ANTLR start import_name
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:97:1: import_name returns [List idList, DroolsTree dotStar] : ^( VT_IMPORT_ID (tempList+= ID )+ (tempDotStar= DOT_STAR )? ) ;
    public final import_name_return import_name() throws RecognitionException {
        import_name_return retval = new import_name_return();
        retval.start = input.LT(1);

        DroolsTree tempDotStar=null;
        DroolsTree tempList=null;
        List list_tempList=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:98:2: ( ^( VT_IMPORT_ID (tempList+= ID )+ (tempDotStar= DOT_STAR )? ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:98:4: ^( VT_IMPORT_ID (tempList+= ID )+ (tempDotStar= DOT_STAR )? )
            {
            match(input,VT_IMPORT_ID,FOLLOW_VT_IMPORT_ID_in_import_name277); 

            match(input, Token.DOWN, null); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:98:27: (tempList+= ID )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==ID) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:98:27: tempList+= ID
            	    {
            	    tempList=(DroolsTree)input.LT(1);
            	    match(input,ID,FOLLOW_ID_in_import_name281); 
            	    if (list_tempList==null) list_tempList=new ArrayList();
            	    list_tempList.add(tempList);


            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);

            // src/main/resources/org/drools/lang/DescrBuilderTree.g:98:44: (tempDotStar= DOT_STAR )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==DOT_STAR) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:98:44: tempDotStar= DOT_STAR
                    {
                    tempDotStar=(DroolsTree)input.LT(1);
                    match(input,DOT_STAR,FOLLOW_DOT_STAR_in_import_name286); 

                    }
                    break;

            }


            match(input, Token.UP, null); 
            	retval.idList = list_tempList;
            		retval.dotStar = tempDotStar;	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end import_name

    public static class global_return extends TreeRuleReturnScope {
        public GlobalDescr globalDescr;
    };

    // $ANTLR start global
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:103:1: global returns [GlobalDescr globalDescr] : ^(start= VK_GLOBAL dt= data_type globalId= VT_GLOBAL_ID ) ;
    public final global_return global() throws RecognitionException {
        global_return retval = new global_return();
        retval.start = input.LT(1);

        DroolsTree start=null;
        DroolsTree globalId=null;
        BaseDescr dt = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:104:2: ( ^(start= VK_GLOBAL dt= data_type globalId= VT_GLOBAL_ID ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:104:4: ^(start= VK_GLOBAL dt= data_type globalId= VT_GLOBAL_ID )
            {
            start=(DroolsTree)input.LT(1);
            match(input,VK_GLOBAL,FOLLOW_VK_GLOBAL_in_global309); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_data_type_in_global313);
            dt=data_type();
            _fsp--;

            globalId=(DroolsTree)input.LT(1);
            match(input,VT_GLOBAL_ID,FOLLOW_VT_GLOBAL_ID_in_global317); 

            match(input, Token.UP, null); 
            	retval.globalDescr = factory.createGlobal(start,dt, globalId);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end global

    public static class function_return extends TreeRuleReturnScope {
        public FunctionDescr functionDescr;
    };

    // $ANTLR start function
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:108:1: function returns [FunctionDescr functionDescr] : ^(start= VK_FUNCTION (dt= data_type )? functionId= VT_FUNCTION_ID params= parameters content= VT_CURLY_CHUNK ) ;
    public final function_return function() throws RecognitionException {
        function_return retval = new function_return();
        retval.start = input.LT(1);

        DroolsTree start=null;
        DroolsTree functionId=null;
        DroolsTree content=null;
        BaseDescr dt = null;

        List params = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:109:2: ( ^(start= VK_FUNCTION (dt= data_type )? functionId= VT_FUNCTION_ID params= parameters content= VT_CURLY_CHUNK ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:109:4: ^(start= VK_FUNCTION (dt= data_type )? functionId= VT_FUNCTION_ID params= parameters content= VT_CURLY_CHUNK )
            {
            start=(DroolsTree)input.LT(1);
            match(input,VK_FUNCTION,FOLLOW_VK_FUNCTION_in_function339); 

            match(input, Token.DOWN, null); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:109:26: (dt= data_type )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==VT_DATA_TYPE) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:109:26: dt= data_type
                    {
                    pushFollow(FOLLOW_data_type_in_function343);
                    dt=data_type();
                    _fsp--;


                    }
                    break;

            }

            functionId=(DroolsTree)input.LT(1);
            match(input,VT_FUNCTION_ID,FOLLOW_VT_FUNCTION_ID_in_function348); 
            pushFollow(FOLLOW_parameters_in_function352);
            params=parameters();
            _fsp--;

            content=(DroolsTree)input.LT(1);
            match(input,VT_CURLY_CHUNK,FOLLOW_VT_CURLY_CHUNK_in_function356); 

            match(input, Token.UP, null); 
            	retval.functionDescr = factory.createFunction(start, dt, functionId, params, content);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end function

    public static class template_return extends TreeRuleReturnScope {
        public FactTemplateDescr factTemplateDescr;
    };

    // $ANTLR start template
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:113:1: template returns [FactTemplateDescr factTemplateDescr] : ^(start= VK_TEMPLATE id= VT_TEMPLATE_ID (ts= template_slot )+ end= END ) ;
    public final template_return template() throws RecognitionException {
        template_return retval = new template_return();
        retval.start = input.LT(1);

        DroolsTree start=null;
        DroolsTree id=null;
        DroolsTree end=null;
        FieldTemplateDescr ts = null;



        	List slotList = new LinkedList<FieldTemplateDescr>();

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:116:3: ( ^(start= VK_TEMPLATE id= VT_TEMPLATE_ID (ts= template_slot )+ end= END ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:116:5: ^(start= VK_TEMPLATE id= VT_TEMPLATE_ID (ts= template_slot )+ end= END )
            {
            start=(DroolsTree)input.LT(1);
            match(input,VK_TEMPLATE,FOLLOW_VK_TEMPLATE_in_template381); 

            match(input, Token.DOWN, null); 
            id=(DroolsTree)input.LT(1);
            match(input,VT_TEMPLATE_ID,FOLLOW_VT_TEMPLATE_ID_in_template385); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:117:4: (ts= template_slot )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0==VT_SLOT) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:117:6: ts= template_slot
            	    {
            	    pushFollow(FOLLOW_template_slot_in_template394);
            	    ts=template_slot();
            	    _fsp--;

            	    slotList.add(ts);

            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);

            end=(DroolsTree)input.LT(1);
            match(input,END,FOLLOW_END_in_template402); 

            match(input, Token.UP, null); 
            	retval.factTemplateDescr = factory.createFactTemplate(start, id, slotList, end);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end template


    // $ANTLR start template_slot
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:121:1: template_slot returns [FieldTemplateDescr fieldTemplateDescr] : ^( VT_SLOT dt= data_type id= VT_SLOT_ID ) ;
    public final FieldTemplateDescr template_slot() throws RecognitionException {
        FieldTemplateDescr fieldTemplateDescr = null;

        DroolsTree id=null;
        BaseDescr dt = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:122:2: ( ^( VT_SLOT dt= data_type id= VT_SLOT_ID ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:122:4: ^( VT_SLOT dt= data_type id= VT_SLOT_ID )
            {
            match(input,VT_SLOT,FOLLOW_VT_SLOT_in_template_slot422); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_data_type_in_template_slot426);
            dt=data_type();
            _fsp--;

            id=(DroolsTree)input.LT(1);
            match(input,VT_SLOT_ID,FOLLOW_VT_SLOT_ID_in_template_slot430); 

            match(input, Token.UP, null); 
            	fieldTemplateDescr = factory.createFieldTemplate(dt, id);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return fieldTemplateDescr;
    }
    // $ANTLR end template_slot

    public static class query_return extends TreeRuleReturnScope {
        public QueryDescr queryDescr;
    };

    // $ANTLR start query
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:126:1: query returns [QueryDescr queryDescr] : ^(start= VK_QUERY id= VT_QUERY_ID (params= parameters )? lb= lhs_block end= END ) ;
    public final query_return query() throws RecognitionException {
        query_return retval = new query_return();
        retval.start = input.LT(1);

        DroolsTree start=null;
        DroolsTree id=null;
        DroolsTree end=null;
        List params = null;

        AndDescr lb = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:127:2: ( ^(start= VK_QUERY id= VT_QUERY_ID (params= parameters )? lb= lhs_block end= END ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:127:4: ^(start= VK_QUERY id= VT_QUERY_ID (params= parameters )? lb= lhs_block end= END )
            {
            start=(DroolsTree)input.LT(1);
            match(input,VK_QUERY,FOLLOW_VK_QUERY_in_query452); 

            match(input, Token.DOWN, null); 
            id=(DroolsTree)input.LT(1);
            match(input,VT_QUERY_ID,FOLLOW_VT_QUERY_ID_in_query456); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:127:42: (params= parameters )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==VT_PARAM_LIST) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:127:42: params= parameters
                    {
                    pushFollow(FOLLOW_parameters_in_query460);
                    params=parameters();
                    _fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_lhs_block_in_query465);
            lb=lhs_block();
            _fsp--;

            end=(DroolsTree)input.LT(1);
            match(input,END,FOLLOW_END_in_query469); 

            match(input, Token.UP, null); 
            	retval.queryDescr = factory.createQuery(start, id, params, lb, end);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end query

    public static class rule_return extends TreeRuleReturnScope {
        public RuleDescr ruleDescr;
    };

    // $ANTLR start rule
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:131:1: rule returns [RuleDescr ruleDescr] : ^(start= VK_RULE id= VT_RULE_ID (ra= rule_attributes )? (dm= decl_metadata )* (wn= when_part )? content= VT_RHS_CHUNK ) ;
    public final rule_return rule() throws RecognitionException {
        rule_return retval = new rule_return();
        retval.start = input.LT(1);

        DroolsTree start=null;
        DroolsTree id=null;
        DroolsTree content=null;
        List ra = null;

        Map dm = null;

        AndDescr wn = null;


        	List<Map> declMetadaList = new LinkedList<Map>();
        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:133:2: ( ^(start= VK_RULE id= VT_RULE_ID (ra= rule_attributes )? (dm= decl_metadata )* (wn= when_part )? content= VT_RHS_CHUNK ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:133:4: ^(start= VK_RULE id= VT_RULE_ID (ra= rule_attributes )? (dm= decl_metadata )* (wn= when_part )? content= VT_RHS_CHUNK )
            {
            start=(DroolsTree)input.LT(1);
            match(input,VK_RULE,FOLLOW_VK_RULE_in_rule496); 

            match(input, Token.DOWN, null); 
            id=(DroolsTree)input.LT(1);
            match(input,VT_RULE_ID,FOLLOW_VT_RULE_ID_in_rule500); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:133:36: (ra= rule_attributes )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==VT_RULE_ATTRIBUTES) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:133:36: ra= rule_attributes
                    {
                    pushFollow(FOLLOW_rule_attributes_in_rule504);
                    ra=rule_attributes();
                    _fsp--;


                    }
                    break;

            }

            // src/main/resources/org/drools/lang/DescrBuilderTree.g:134:3: (dm= decl_metadata )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==AT) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:134:4: dm= decl_metadata
            	    {
            	    pushFollow(FOLLOW_decl_metadata_in_rule513);
            	    dm=decl_metadata();
            	    _fsp--;

            	    declMetadaList.add(dm);	

            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);

            // src/main/resources/org/drools/lang/DescrBuilderTree.g:135:6: (wn= when_part )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==WHEN) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:135:6: wn= when_part
                    {
                    pushFollow(FOLLOW_when_part_in_rule525);
                    wn=when_part();
                    _fsp--;


                    }
                    break;

            }

            content=(DroolsTree)input.LT(1);
            match(input,VT_RHS_CHUNK,FOLLOW_VT_RHS_CHUNK_in_rule530); 

            match(input, Token.UP, null); 
            	retval.ruleDescr = factory.createRule(start, id, ra, wn, content, declMetadaList);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end rule


    // $ANTLR start when_part
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:139:1: when_part returns [AndDescr andDescr] : WHEN lh= lhs_block ;
    public final AndDescr when_part() throws RecognitionException {
        AndDescr andDescr = null;

        AndDescr lh = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:140:2: ( WHEN lh= lhs_block )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:140:4: WHEN lh= lhs_block
            {
            match(input,WHEN,FOLLOW_WHEN_in_when_part549); 
            pushFollow(FOLLOW_lhs_block_in_when_part553);
            lh=lhs_block();
            _fsp--;

            	andDescr = lh;	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return andDescr;
    }
    // $ANTLR end when_part


    // $ANTLR start rule_attributes
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:144:1: rule_attributes returns [List attrList] : ^( VT_RULE_ATTRIBUTES ( VK_ATTRIBUTES )? (rl= rule_attribute )+ ) ;
    public final List rule_attributes() throws RecognitionException {
        List attrList = null;

        AttributeDescr rl = null;



        	attrList = new LinkedList<AttributeDescr>();

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:147:3: ( ^( VT_RULE_ATTRIBUTES ( VK_ATTRIBUTES )? (rl= rule_attribute )+ ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:147:5: ^( VT_RULE_ATTRIBUTES ( VK_ATTRIBUTES )? (rl= rule_attribute )+ )
            {
            match(input,VT_RULE_ATTRIBUTES,FOLLOW_VT_RULE_ATTRIBUTES_in_rule_attributes575); 

            match(input, Token.DOWN, null); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:147:26: ( VK_ATTRIBUTES )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==VK_ATTRIBUTES) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:147:26: VK_ATTRIBUTES
                    {
                    match(input,VK_ATTRIBUTES,FOLLOW_VK_ATTRIBUTES_in_rule_attributes577); 

                    }
                    break;

            }

            // src/main/resources/org/drools/lang/DescrBuilderTree.g:147:41: (rl= rule_attribute )+
            int cnt14=0;
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( ((LA14_0>=VK_DATE_EFFECTIVE && LA14_0<=VK_ENABLED)) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:147:42: rl= rule_attribute
            	    {
            	    pushFollow(FOLLOW_rule_attribute_in_rule_attributes583);
            	    rl=rule_attribute();
            	    _fsp--;

            	    attrList.add(rl);

            	    }
            	    break;

            	default :
            	    if ( cnt14 >= 1 ) break loop14;
                        EarlyExitException eee =
                            new EarlyExitException(14, input);
                        throw eee;
                }
                cnt14++;
            } while (true);


            match(input, Token.UP, null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return attrList;
    }
    // $ANTLR end rule_attributes


    // $ANTLR start parameters
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:150:1: parameters returns [List paramList] : ^( VT_PARAM_LIST (p= param_definition )* ) ;
    public final List parameters() throws RecognitionException {
        List paramList = null;

        Map p = null;



        	paramList = new LinkedList<Map<BaseDescr, BaseDescr>>();

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:153:3: ( ^( VT_PARAM_LIST (p= param_definition )* ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:153:5: ^( VT_PARAM_LIST (p= param_definition )* )
            {
            match(input,VT_PARAM_LIST,FOLLOW_VT_PARAM_LIST_in_parameters607); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // src/main/resources/org/drools/lang/DescrBuilderTree.g:153:21: (p= param_definition )*
                loop15:
                do {
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==VT_DATA_TYPE||LA15_0==ID) ) {
                        alt15=1;
                    }


                    switch (alt15) {
                	case 1 :
                	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:153:22: p= param_definition
                	    {
                	    pushFollow(FOLLOW_param_definition_in_parameters612);
                	    p=param_definition();
                	    _fsp--;

                	    paramList.add(p);

                	    }
                	    break;

                	default :
                	    break loop15;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return paramList;
    }
    // $ANTLR end parameters


    // $ANTLR start param_definition
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:156:1: param_definition returns [Map param] : (dt= data_type )? a= argument ;
    public final Map param_definition() throws RecognitionException {
        Map param = null;

        BaseDescr dt = null;

        BaseDescr a = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:157:2: ( (dt= data_type )? a= argument )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:157:4: (dt= data_type )? a= argument
            {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:157:6: (dt= data_type )?
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==VT_DATA_TYPE) ) {
                alt16=1;
            }
            switch (alt16) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:157:6: dt= data_type
                    {
                    pushFollow(FOLLOW_data_type_in_param_definition634);
                    dt=data_type();
                    _fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_argument_in_param_definition639);
            a=argument();
            _fsp--;

            	param = new HashMap<BaseDescr, BaseDescr>();
            		param.put(a, dt);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return param;
    }
    // $ANTLR end param_definition


    // $ANTLR start argument
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:162:1: argument returns [BaseDescr arg] : id= ID ( LEFT_SQUARE rightList+= RIGHT_SQUARE )* ;
    public final BaseDescr argument() throws RecognitionException {
        BaseDescr arg = null;

        DroolsTree id=null;
        DroolsTree rightList=null;
        List list_rightList=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:163:2: (id= ID ( LEFT_SQUARE rightList+= RIGHT_SQUARE )* )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:163:4: id= ID ( LEFT_SQUARE rightList+= RIGHT_SQUARE )*
            {
            id=(DroolsTree)input.LT(1);
            match(input,ID,FOLLOW_ID_in_argument659); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:163:10: ( LEFT_SQUARE rightList+= RIGHT_SQUARE )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==LEFT_SQUARE) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:163:11: LEFT_SQUARE rightList+= RIGHT_SQUARE
            	    {
            	    match(input,LEFT_SQUARE,FOLLOW_LEFT_SQUARE_in_argument662); 
            	    rightList=(DroolsTree)input.LT(1);
            	    match(input,RIGHT_SQUARE,FOLLOW_RIGHT_SQUARE_in_argument666); 
            	    if (list_rightList==null) list_rightList=new ArrayList();
            	    list_rightList.add(rightList);


            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);

            	arg = factory.createArgument(id, list_rightList);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return arg;
    }
    // $ANTLR end argument


    // $ANTLR start type_declaration
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:167:1: type_declaration returns [TypeDeclarationDescr declaration] : ^( VK_DECLARE id= VT_TYPE_DECLARE_ID (dm= decl_metadata )* (df= decl_field )* END ) ;
    public final TypeDeclarationDescr type_declaration() throws RecognitionException {
        TypeDeclarationDescr declaration = null;

        DroolsTree id=null;
        Map dm = null;

        TypeFieldDescr df = null;


        	List<Map> declMetadaList = new LinkedList<Map>();
        		List<TypeFieldDescr> declFieldList = new LinkedList<TypeFieldDescr>(); 
        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:170:2: ( ^( VK_DECLARE id= VT_TYPE_DECLARE_ID (dm= decl_metadata )* (df= decl_field )* END ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:170:4: ^( VK_DECLARE id= VT_TYPE_DECLARE_ID (dm= decl_metadata )* (df= decl_field )* END )
            {
            match(input,VK_DECLARE,FOLLOW_VK_DECLARE_in_type_declaration692); 

            match(input, Token.DOWN, null); 
            id=(DroolsTree)input.LT(1);
            match(input,VT_TYPE_DECLARE_ID,FOLLOW_VT_TYPE_DECLARE_ID_in_type_declaration696); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:171:4: (dm= decl_metadata )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==AT) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:171:5: dm= decl_metadata
            	    {
            	    pushFollow(FOLLOW_decl_metadata_in_type_declaration705);
            	    dm=decl_metadata();
            	    _fsp--;

            	    declMetadaList.add(dm);	

            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);

            // src/main/resources/org/drools/lang/DescrBuilderTree.g:172:4: (df= decl_field )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==ID) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:172:5: df= decl_field
            	    {
            	    pushFollow(FOLLOW_decl_field_in_type_declaration718);
            	    df=decl_field();
            	    _fsp--;

            	    declFieldList.add(df);	

            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

            match(input,END,FOLLOW_END_in_type_declaration724); 

            match(input, Token.UP, null); 
            	declaration = factory.createTypeDeclr(id, declMetadaList, declFieldList);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return declaration;
    }
    // $ANTLR end type_declaration


    // $ANTLR start decl_metadata
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:176:1: decl_metadata returns [Map attData] : ^( AT att= ID pc= VT_PAREN_CHUNK ) ;
    public final Map decl_metadata() throws RecognitionException {
        Map attData = null;

        DroolsTree att=null;
        DroolsTree pc=null;

        attData = new HashMap();
        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:178:2: ( ^( AT att= ID pc= VT_PAREN_CHUNK ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:178:4: ^( AT att= ID pc= VT_PAREN_CHUNK )
            {
            match(input,AT,FOLLOW_AT_in_decl_metadata749); 

            match(input, Token.DOWN, null); 
            att=(DroolsTree)input.LT(1);
            match(input,ID,FOLLOW_ID_in_decl_metadata753); 
            pc=(DroolsTree)input.LT(1);
            match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_decl_metadata757); 

            match(input, Token.UP, null); 
            	attData.put(att, pc);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return attData;
    }
    // $ANTLR end decl_metadata


    // $ANTLR start decl_field
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:182:1: decl_field returns [TypeFieldDescr fieldDescr] : ^(id= ID (init= decl_field_initialization )? dt= data_type (dm= decl_metadata )* ) ;
    public final TypeFieldDescr decl_field() throws RecognitionException {
        TypeFieldDescr fieldDescr = null;

        DroolsTree id=null;
        String init = null;

        BaseDescr dt = null;

        Map dm = null;


        List<Map> declMetadaList = new LinkedList<Map>(); 
        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:184:2: ( ^(id= ID (init= decl_field_initialization )? dt= data_type (dm= decl_metadata )* ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:184:4: ^(id= ID (init= decl_field_initialization )? dt= data_type (dm= decl_metadata )* )
            {
            id=(DroolsTree)input.LT(1);
            match(input,ID,FOLLOW_ID_in_decl_field784); 

            match(input, Token.DOWN, null); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:184:16: (init= decl_field_initialization )?
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==EQUALS) ) {
                alt20=1;
            }
            switch (alt20) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:184:16: init= decl_field_initialization
                    {
                    pushFollow(FOLLOW_decl_field_initialization_in_decl_field788);
                    init=decl_field_initialization();
                    _fsp--;


                    }
                    break;

            }

            pushFollow(FOLLOW_data_type_in_decl_field793);
            dt=data_type();
            _fsp--;

            // src/main/resources/org/drools/lang/DescrBuilderTree.g:184:57: (dm= decl_metadata )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( (LA21_0==AT) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:184:58: dm= decl_metadata
            	    {
            	    pushFollow(FOLLOW_decl_metadata_in_decl_field798);
            	    dm=decl_metadata();
            	    _fsp--;

            	    declMetadaList.add(dm);

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            match(input, Token.UP, null); 
            	fieldDescr = factory.createTypeField(id, init, dt, declMetadaList);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return fieldDescr;
    }
    // $ANTLR end decl_field


    // $ANTLR start decl_field_initialization
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:188:1: decl_field_initialization returns [String expr] : ^( EQUALS pc= VT_PAREN_CHUNK ) ;
    public final String decl_field_initialization() throws RecognitionException {
        String expr = null;

        DroolsTree pc=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:189:2: ( ^( EQUALS pc= VT_PAREN_CHUNK ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:189:4: ^( EQUALS pc= VT_PAREN_CHUNK )
            {
            match(input,EQUALS,FOLLOW_EQUALS_in_decl_field_initialization825); 

            match(input, Token.DOWN, null); 
            pc=(DroolsTree)input.LT(1);
            match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_decl_field_initialization829); 

            match(input, Token.UP, null); 
            	expr = pc.getText().substring(1, pc.getText().length() -1 ).trim();	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return expr;
    }
    // $ANTLR end decl_field_initialization


    // $ANTLR start rule_attribute
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:193:1: rule_attribute returns [AttributeDescr attributeDescr] : ( ^(attrName= VK_SALIENCE (value= INT | value= VT_PAREN_CHUNK ) ) | ^(attrName= VK_NO_LOOP (value= BOOL )? ) | ^(attrName= VK_AGENDA_GROUP value= STRING ) | ^(attrName= VK_DURATION value= INT ) | ^(attrName= VK_ACTIVATION_GROUP value= STRING ) | ^(attrName= VK_AUTO_FOCUS (value= BOOL )? ) | ^(attrName= VK_DATE_EFFECTIVE value= STRING ) | ^(attrName= VK_DATE_EXPIRES value= STRING ) | ^(attrName= VK_ENABLED (value= BOOL | value= VT_PAREN_CHUNK ) ) | ^(attrName= VK_RULEFLOW_GROUP value= STRING ) | ^(attrName= VK_LOCK_ON_ACTIVE (value= BOOL )? ) | ^(attrName= VK_DIALECT value= STRING ) ) ;
    public final AttributeDescr rule_attribute() throws RecognitionException {
        AttributeDescr attributeDescr = null;

        DroolsTree attrName=null;
        DroolsTree value=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:194:2: ( ( ^(attrName= VK_SALIENCE (value= INT | value= VT_PAREN_CHUNK ) ) | ^(attrName= VK_NO_LOOP (value= BOOL )? ) | ^(attrName= VK_AGENDA_GROUP value= STRING ) | ^(attrName= VK_DURATION value= INT ) | ^(attrName= VK_ACTIVATION_GROUP value= STRING ) | ^(attrName= VK_AUTO_FOCUS (value= BOOL )? ) | ^(attrName= VK_DATE_EFFECTIVE value= STRING ) | ^(attrName= VK_DATE_EXPIRES value= STRING ) | ^(attrName= VK_ENABLED (value= BOOL | value= VT_PAREN_CHUNK ) ) | ^(attrName= VK_RULEFLOW_GROUP value= STRING ) | ^(attrName= VK_LOCK_ON_ACTIVE (value= BOOL )? ) | ^(attrName= VK_DIALECT value= STRING ) ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:194:4: ( ^(attrName= VK_SALIENCE (value= INT | value= VT_PAREN_CHUNK ) ) | ^(attrName= VK_NO_LOOP (value= BOOL )? ) | ^(attrName= VK_AGENDA_GROUP value= STRING ) | ^(attrName= VK_DURATION value= INT ) | ^(attrName= VK_ACTIVATION_GROUP value= STRING ) | ^(attrName= VK_AUTO_FOCUS (value= BOOL )? ) | ^(attrName= VK_DATE_EFFECTIVE value= STRING ) | ^(attrName= VK_DATE_EXPIRES value= STRING ) | ^(attrName= VK_ENABLED (value= BOOL | value= VT_PAREN_CHUNK ) ) | ^(attrName= VK_RULEFLOW_GROUP value= STRING ) | ^(attrName= VK_LOCK_ON_ACTIVE (value= BOOL )? ) | ^(attrName= VK_DIALECT value= STRING ) )
            {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:194:4: ( ^(attrName= VK_SALIENCE (value= INT | value= VT_PAREN_CHUNK ) ) | ^(attrName= VK_NO_LOOP (value= BOOL )? ) | ^(attrName= VK_AGENDA_GROUP value= STRING ) | ^(attrName= VK_DURATION value= INT ) | ^(attrName= VK_ACTIVATION_GROUP value= STRING ) | ^(attrName= VK_AUTO_FOCUS (value= BOOL )? ) | ^(attrName= VK_DATE_EFFECTIVE value= STRING ) | ^(attrName= VK_DATE_EXPIRES value= STRING ) | ^(attrName= VK_ENABLED (value= BOOL | value= VT_PAREN_CHUNK ) ) | ^(attrName= VK_RULEFLOW_GROUP value= STRING ) | ^(attrName= VK_LOCK_ON_ACTIVE (value= BOOL )? ) | ^(attrName= VK_DIALECT value= STRING ) )
            int alt27=12;
            switch ( input.LA(1) ) {
            case VK_SALIENCE:
                {
                alt27=1;
                }
                break;
            case VK_NO_LOOP:
                {
                alt27=2;
                }
                break;
            case VK_AGENDA_GROUP:
                {
                alt27=3;
                }
                break;
            case VK_DURATION:
                {
                alt27=4;
                }
                break;
            case VK_ACTIVATION_GROUP:
                {
                alt27=5;
                }
                break;
            case VK_AUTO_FOCUS:
                {
                alt27=6;
                }
                break;
            case VK_DATE_EFFECTIVE:
                {
                alt27=7;
                }
                break;
            case VK_DATE_EXPIRES:
                {
                alt27=8;
                }
                break;
            case VK_ENABLED:
                {
                alt27=9;
                }
                break;
            case VK_RULEFLOW_GROUP:
                {
                alt27=10;
                }
                break;
            case VK_LOCK_ON_ACTIVE:
                {
                alt27=11;
                }
                break;
            case VK_DIALECT:
                {
                alt27=12;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("194:4: ( ^(attrName= VK_SALIENCE (value= INT | value= VT_PAREN_CHUNK ) ) | ^(attrName= VK_NO_LOOP (value= BOOL )? ) | ^(attrName= VK_AGENDA_GROUP value= STRING ) | ^(attrName= VK_DURATION value= INT ) | ^(attrName= VK_ACTIVATION_GROUP value= STRING ) | ^(attrName= VK_AUTO_FOCUS (value= BOOL )? ) | ^(attrName= VK_DATE_EFFECTIVE value= STRING ) | ^(attrName= VK_DATE_EXPIRES value= STRING ) | ^(attrName= VK_ENABLED (value= BOOL | value= VT_PAREN_CHUNK ) ) | ^(attrName= VK_RULEFLOW_GROUP value= STRING ) | ^(attrName= VK_LOCK_ON_ACTIVE (value= BOOL )? ) | ^(attrName= VK_DIALECT value= STRING ) )", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:194:5: ^(attrName= VK_SALIENCE (value= INT | value= VT_PAREN_CHUNK ) )
                    {
                    attrName=(DroolsTree)input.LT(1);
                    match(input,VK_SALIENCE,FOLLOW_VK_SALIENCE_in_rule_attribute852); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:194:28: (value= INT | value= VT_PAREN_CHUNK )
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==INT) ) {
                        alt22=1;
                    }
                    else if ( (LA22_0==VT_PAREN_CHUNK) ) {
                        alt22=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("194:28: (value= INT | value= VT_PAREN_CHUNK )", 22, 0, input);

                        throw nvae;
                    }
                    switch (alt22) {
                        case 1 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:194:29: value= INT
                            {
                            value=(DroolsTree)input.LT(1);
                            match(input,INT,FOLLOW_INT_in_rule_attribute857); 

                            }
                            break;
                        case 2 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:194:39: value= VT_PAREN_CHUNK
                            {
                            value=(DroolsTree)input.LT(1);
                            match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_rule_attribute861); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                    }
                    break;
                case 2 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:195:4: ^(attrName= VK_NO_LOOP (value= BOOL )? )
                    {
                    attrName=(DroolsTree)input.LT(1);
                    match(input,VK_NO_LOOP,FOLLOW_VK_NO_LOOP_in_rule_attribute872); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // src/main/resources/org/drools/lang/DescrBuilderTree.g:195:31: (value= BOOL )?
                        int alt23=2;
                        int LA23_0 = input.LA(1);

                        if ( (LA23_0==BOOL) ) {
                            alt23=1;
                        }
                        switch (alt23) {
                            case 1 :
                                // src/main/resources/org/drools/lang/DescrBuilderTree.g:195:31: value= BOOL
                                {
                                value=(DroolsTree)input.LT(1);
                                match(input,BOOL,FOLLOW_BOOL_in_rule_attribute876); 

                                }
                                break;

                        }


                        match(input, Token.UP, null); 
                    }

                    }
                    break;
                case 3 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:196:4: ^(attrName= VK_AGENDA_GROUP value= STRING )
                    {
                    attrName=(DroolsTree)input.LT(1);
                    match(input,VK_AGENDA_GROUP,FOLLOW_VK_AGENDA_GROUP_in_rule_attribute888); 

                    match(input, Token.DOWN, null); 
                    value=(DroolsTree)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_rule_attribute892); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 4 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:197:4: ^(attrName= VK_DURATION value= INT )
                    {
                    attrName=(DroolsTree)input.LT(1);
                    match(input,VK_DURATION,FOLLOW_VK_DURATION_in_rule_attribute903); 

                    match(input, Token.DOWN, null); 
                    value=(DroolsTree)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_rule_attribute907); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 5 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:198:4: ^(attrName= VK_ACTIVATION_GROUP value= STRING )
                    {
                    attrName=(DroolsTree)input.LT(1);
                    match(input,VK_ACTIVATION_GROUP,FOLLOW_VK_ACTIVATION_GROUP_in_rule_attribute919); 

                    match(input, Token.DOWN, null); 
                    value=(DroolsTree)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_rule_attribute923); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 6 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:199:4: ^(attrName= VK_AUTO_FOCUS (value= BOOL )? )
                    {
                    attrName=(DroolsTree)input.LT(1);
                    match(input,VK_AUTO_FOCUS,FOLLOW_VK_AUTO_FOCUS_in_rule_attribute933); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // src/main/resources/org/drools/lang/DescrBuilderTree.g:199:34: (value= BOOL )?
                        int alt24=2;
                        int LA24_0 = input.LA(1);

                        if ( (LA24_0==BOOL) ) {
                            alt24=1;
                        }
                        switch (alt24) {
                            case 1 :
                                // src/main/resources/org/drools/lang/DescrBuilderTree.g:199:34: value= BOOL
                                {
                                value=(DroolsTree)input.LT(1);
                                match(input,BOOL,FOLLOW_BOOL_in_rule_attribute937); 

                                }
                                break;

                        }


                        match(input, Token.UP, null); 
                    }

                    }
                    break;
                case 7 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:200:4: ^(attrName= VK_DATE_EFFECTIVE value= STRING )
                    {
                    attrName=(DroolsTree)input.LT(1);
                    match(input,VK_DATE_EFFECTIVE,FOLLOW_VK_DATE_EFFECTIVE_in_rule_attribute948); 

                    match(input, Token.DOWN, null); 
                    value=(DroolsTree)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_rule_attribute952); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 8 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:201:4: ^(attrName= VK_DATE_EXPIRES value= STRING )
                    {
                    attrName=(DroolsTree)input.LT(1);
                    match(input,VK_DATE_EXPIRES,FOLLOW_VK_DATE_EXPIRES_in_rule_attribute962); 

                    match(input, Token.DOWN, null); 
                    value=(DroolsTree)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_rule_attribute966); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 9 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:202:4: ^(attrName= VK_ENABLED (value= BOOL | value= VT_PAREN_CHUNK ) )
                    {
                    attrName=(DroolsTree)input.LT(1);
                    match(input,VK_ENABLED,FOLLOW_VK_ENABLED_in_rule_attribute976); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:202:26: (value= BOOL | value= VT_PAREN_CHUNK )
                    int alt25=2;
                    int LA25_0 = input.LA(1);

                    if ( (LA25_0==BOOL) ) {
                        alt25=1;
                    }
                    else if ( (LA25_0==VT_PAREN_CHUNK) ) {
                        alt25=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("202:26: (value= BOOL | value= VT_PAREN_CHUNK )", 25, 0, input);

                        throw nvae;
                    }
                    switch (alt25) {
                        case 1 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:202:27: value= BOOL
                            {
                            value=(DroolsTree)input.LT(1);
                            match(input,BOOL,FOLLOW_BOOL_in_rule_attribute981); 

                            }
                            break;
                        case 2 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:202:38: value= VT_PAREN_CHUNK
                            {
                            value=(DroolsTree)input.LT(1);
                            match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_rule_attribute985); 

                            }
                            break;

                    }


                    match(input, Token.UP, null); 

                    }
                    break;
                case 10 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:203:4: ^(attrName= VK_RULEFLOW_GROUP value= STRING )
                    {
                    attrName=(DroolsTree)input.LT(1);
                    match(input,VK_RULEFLOW_GROUP,FOLLOW_VK_RULEFLOW_GROUP_in_rule_attribute996); 

                    match(input, Token.DOWN, null); 
                    value=(DroolsTree)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_rule_attribute1000); 

                    match(input, Token.UP, null); 

                    }
                    break;
                case 11 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:204:4: ^(attrName= VK_LOCK_ON_ACTIVE (value= BOOL )? )
                    {
                    attrName=(DroolsTree)input.LT(1);
                    match(input,VK_LOCK_ON_ACTIVE,FOLLOW_VK_LOCK_ON_ACTIVE_in_rule_attribute1010); 

                    if ( input.LA(1)==Token.DOWN ) {
                        match(input, Token.DOWN, null); 
                        // src/main/resources/org/drools/lang/DescrBuilderTree.g:204:38: (value= BOOL )?
                        int alt26=2;
                        int LA26_0 = input.LA(1);

                        if ( (LA26_0==BOOL) ) {
                            alt26=1;
                        }
                        switch (alt26) {
                            case 1 :
                                // src/main/resources/org/drools/lang/DescrBuilderTree.g:204:38: value= BOOL
                                {
                                value=(DroolsTree)input.LT(1);
                                match(input,BOOL,FOLLOW_BOOL_in_rule_attribute1014); 

                                }
                                break;

                        }


                        match(input, Token.UP, null); 
                    }

                    }
                    break;
                case 12 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:205:4: ^(attrName= VK_DIALECT value= STRING )
                    {
                    attrName=(DroolsTree)input.LT(1);
                    match(input,VK_DIALECT,FOLLOW_VK_DIALECT_in_rule_attribute1024); 

                    match(input, Token.DOWN, null); 
                    value=(DroolsTree)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_rule_attribute1028); 

                    match(input, Token.UP, null); 

                    }
                    break;

            }

            	attributeDescr = factory.createAttribute(attrName, value);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return attributeDescr;
    }
    // $ANTLR end rule_attribute


    // $ANTLR start lhs_block
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:209:1: lhs_block returns [AndDescr andDescr] : ^( VT_AND_IMPLICIT (dt= lhs )* ) ;
    public final AndDescr lhs_block() throws RecognitionException {
        AndDescr andDescr = null;

        lhs_return dt = null;



        	andDescr = new AndDescr();

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:212:3: ( ^( VT_AND_IMPLICIT (dt= lhs )* ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:212:5: ^( VT_AND_IMPLICIT (dt= lhs )* )
            {
            match(input,VT_AND_IMPLICIT,FOLLOW_VT_AND_IMPLICIT_in_lhs_block1053); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // src/main/resources/org/drools/lang/DescrBuilderTree.g:212:23: (dt= lhs )*
                loop28:
                do {
                    int alt28=2;
                    int LA28_0 = input.LA(1);

                    if ( ((LA28_0>=VT_AND_PREFIX && LA28_0<=VT_OR_INFIX)||LA28_0==VT_PATTERN||LA28_0==VK_EVAL||LA28_0==VK_NOT||(LA28_0>=VK_EXISTS && LA28_0<=VK_FORALL)||LA28_0==FROM) ) {
                        alt28=1;
                    }


                    switch (alt28) {
                	case 1 :
                	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:212:24: dt= lhs
                	    {
                	    pushFollow(FOLLOW_lhs_in_lhs_block1058);
                	    dt=lhs();
                	    _fsp--;

                	    andDescr.addDescr(dt.baseDescr);

                	    }
                	    break;

                	default :
                	    break loop28;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return andDescr;
    }
    // $ANTLR end lhs_block

    public static class lhs_return extends TreeRuleReturnScope {
        public BaseDescr baseDescr;
    };

    // $ANTLR start lhs
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:215:1: lhs returns [BaseDescr baseDescr] : ( ^(start= VT_OR_PREFIX (dt= lhs )+ ) | ^(start= VT_OR_INFIX dt1= lhs dt2= lhs ) | ^(start= VT_AND_PREFIX (dt= lhs )+ ) | ^(start= VT_AND_INFIX dt1= lhs dt2= lhs ) | ^(start= VK_EXISTS dt= lhs ) | ^(start= VK_NOT dt= lhs ) | ^(start= VK_EVAL pc= VT_PAREN_CHUNK ) | ^(start= VK_FORALL (dt= lhs )+ ) | ^( FROM pn= lhs_pattern fe= from_elements ) | pn= lhs_pattern );
    public final lhs_return lhs() throws RecognitionException {
        lhs_return retval = new lhs_return();
        retval.start = input.LT(1);

        DroolsTree start=null;
        DroolsTree pc=null;
        lhs_return dt = null;

        lhs_return dt1 = null;

        lhs_return dt2 = null;

        BaseDescr pn = null;

        from_elements_return fe = null;



        	List<BaseDescr> lhsList = new LinkedList<BaseDescr>();

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:218:3: ( ^(start= VT_OR_PREFIX (dt= lhs )+ ) | ^(start= VT_OR_INFIX dt1= lhs dt2= lhs ) | ^(start= VT_AND_PREFIX (dt= lhs )+ ) | ^(start= VT_AND_INFIX dt1= lhs dt2= lhs ) | ^(start= VK_EXISTS dt= lhs ) | ^(start= VK_NOT dt= lhs ) | ^(start= VK_EVAL pc= VT_PAREN_CHUNK ) | ^(start= VK_FORALL (dt= lhs )+ ) | ^( FROM pn= lhs_pattern fe= from_elements ) | pn= lhs_pattern )
            int alt32=10;
            switch ( input.LA(1) ) {
            case VT_OR_PREFIX:
                {
                alt32=1;
                }
                break;
            case VT_OR_INFIX:
                {
                alt32=2;
                }
                break;
            case VT_AND_PREFIX:
                {
                alt32=3;
                }
                break;
            case VT_AND_INFIX:
                {
                alt32=4;
                }
                break;
            case VK_EXISTS:
                {
                alt32=5;
                }
                break;
            case VK_NOT:
                {
                alt32=6;
                }
                break;
            case VK_EVAL:
                {
                alt32=7;
                }
                break;
            case VK_FORALL:
                {
                alt32=8;
                }
                break;
            case FROM:
                {
                alt32=9;
                }
                break;
            case VT_PATTERN:
                {
                alt32=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("215:1: lhs returns [BaseDescr baseDescr] : ( ^(start= VT_OR_PREFIX (dt= lhs )+ ) | ^(start= VT_OR_INFIX dt1= lhs dt2= lhs ) | ^(start= VT_AND_PREFIX (dt= lhs )+ ) | ^(start= VT_AND_INFIX dt1= lhs dt2= lhs ) | ^(start= VK_EXISTS dt= lhs ) | ^(start= VK_NOT dt= lhs ) | ^(start= VK_EVAL pc= VT_PAREN_CHUNK ) | ^(start= VK_FORALL (dt= lhs )+ ) | ^( FROM pn= lhs_pattern fe= from_elements ) | pn= lhs_pattern );", 32, 0, input);

                throw nvae;
            }

            switch (alt32) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:218:5: ^(start= VT_OR_PREFIX (dt= lhs )+ )
                    {
                    start=(DroolsTree)input.LT(1);
                    match(input,VT_OR_PREFIX,FOLLOW_VT_OR_PREFIX_in_lhs1084); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:218:26: (dt= lhs )+
                    int cnt29=0;
                    loop29:
                    do {
                        int alt29=2;
                        int LA29_0 = input.LA(1);

                        if ( ((LA29_0>=VT_AND_PREFIX && LA29_0<=VT_OR_INFIX)||LA29_0==VT_PATTERN||LA29_0==VK_EVAL||LA29_0==VK_NOT||(LA29_0>=VK_EXISTS && LA29_0<=VK_FORALL)||LA29_0==FROM) ) {
                            alt29=1;
                        }


                        switch (alt29) {
                    	case 1 :
                    	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:218:27: dt= lhs
                    	    {
                    	    pushFollow(FOLLOW_lhs_in_lhs1089);
                    	    dt=lhs();
                    	    _fsp--;

                    	    	lhsList.add(dt.baseDescr);	

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt29 >= 1 ) break loop29;
                                EarlyExitException eee =
                                    new EarlyExitException(29, input);
                                throw eee;
                        }
                        cnt29++;
                    } while (true);


                    match(input, Token.UP, null); 
                    	retval.baseDescr = factory.createOr(start, lhsList);	

                    }
                    break;
                case 2 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:220:4: ^(start= VT_OR_INFIX dt1= lhs dt2= lhs )
                    {
                    start=(DroolsTree)input.LT(1);
                    match(input,VT_OR_INFIX,FOLLOW_VT_OR_INFIX_in_lhs1105); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_lhs_in_lhs1109);
                    dt1=lhs();
                    _fsp--;

                    pushFollow(FOLLOW_lhs_in_lhs1113);
                    dt2=lhs();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	lhsList.add(dt1.baseDescr);
                    		lhsList.add(dt2.baseDescr);
                    		retval.baseDescr = factory.createOr(start, lhsList);	

                    }
                    break;
                case 3 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:224:4: ^(start= VT_AND_PREFIX (dt= lhs )+ )
                    {
                    start=(DroolsTree)input.LT(1);
                    match(input,VT_AND_PREFIX,FOLLOW_VT_AND_PREFIX_in_lhs1125); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:224:26: (dt= lhs )+
                    int cnt30=0;
                    loop30:
                    do {
                        int alt30=2;
                        int LA30_0 = input.LA(1);

                        if ( ((LA30_0>=VT_AND_PREFIX && LA30_0<=VT_OR_INFIX)||LA30_0==VT_PATTERN||LA30_0==VK_EVAL||LA30_0==VK_NOT||(LA30_0>=VK_EXISTS && LA30_0<=VK_FORALL)||LA30_0==FROM) ) {
                            alt30=1;
                        }


                        switch (alt30) {
                    	case 1 :
                    	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:224:27: dt= lhs
                    	    {
                    	    pushFollow(FOLLOW_lhs_in_lhs1130);
                    	    dt=lhs();
                    	    _fsp--;

                    	    	lhsList.add(dt.baseDescr);	

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt30 >= 1 ) break loop30;
                                EarlyExitException eee =
                                    new EarlyExitException(30, input);
                                throw eee;
                        }
                        cnt30++;
                    } while (true);


                    match(input, Token.UP, null); 
                    	retval.baseDescr = factory.createAnd(start, lhsList);	

                    }
                    break;
                case 4 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:226:4: ^(start= VT_AND_INFIX dt1= lhs dt2= lhs )
                    {
                    start=(DroolsTree)input.LT(1);
                    match(input,VT_AND_INFIX,FOLLOW_VT_AND_INFIX_in_lhs1146); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_lhs_in_lhs1150);
                    dt1=lhs();
                    _fsp--;

                    pushFollow(FOLLOW_lhs_in_lhs1154);
                    dt2=lhs();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	lhsList.add(dt1.baseDescr);
                    		lhsList.add(dt2.baseDescr);
                    		retval.baseDescr = factory.createAnd(start, lhsList);	

                    }
                    break;
                case 5 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:230:4: ^(start= VK_EXISTS dt= lhs )
                    {
                    start=(DroolsTree)input.LT(1);
                    match(input,VK_EXISTS,FOLLOW_VK_EXISTS_in_lhs1166); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_lhs_in_lhs1170);
                    dt=lhs();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.baseDescr = factory.createExists(start, dt.baseDescr);	

                    }
                    break;
                case 6 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:232:4: ^(start= VK_NOT dt= lhs )
                    {
                    start=(DroolsTree)input.LT(1);
                    match(input,VK_NOT,FOLLOW_VK_NOT_in_lhs1182); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_lhs_in_lhs1186);
                    dt=lhs();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.baseDescr = factory.createNot(start, dt.baseDescr);	

                    }
                    break;
                case 7 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:234:4: ^(start= VK_EVAL pc= VT_PAREN_CHUNK )
                    {
                    start=(DroolsTree)input.LT(1);
                    match(input,VK_EVAL,FOLLOW_VK_EVAL_in_lhs1198); 

                    match(input, Token.DOWN, null); 
                    pc=(DroolsTree)input.LT(1);
                    match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_lhs1202); 

                    match(input, Token.UP, null); 
                    	retval.baseDescr = factory.createEval(start, pc);	

                    }
                    break;
                case 8 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:236:4: ^(start= VK_FORALL (dt= lhs )+ )
                    {
                    start=(DroolsTree)input.LT(1);
                    match(input,VK_FORALL,FOLLOW_VK_FORALL_in_lhs1214); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:236:22: (dt= lhs )+
                    int cnt31=0;
                    loop31:
                    do {
                        int alt31=2;
                        int LA31_0 = input.LA(1);

                        if ( ((LA31_0>=VT_AND_PREFIX && LA31_0<=VT_OR_INFIX)||LA31_0==VT_PATTERN||LA31_0==VK_EVAL||LA31_0==VK_NOT||(LA31_0>=VK_EXISTS && LA31_0<=VK_FORALL)||LA31_0==FROM) ) {
                            alt31=1;
                        }


                        switch (alt31) {
                    	case 1 :
                    	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:236:23: dt= lhs
                    	    {
                    	    pushFollow(FOLLOW_lhs_in_lhs1219);
                    	    dt=lhs();
                    	    _fsp--;

                    	    	lhsList.add(dt.baseDescr);	

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt31 >= 1 ) break loop31;
                                EarlyExitException eee =
                                    new EarlyExitException(31, input);
                                throw eee;
                        }
                        cnt31++;
                    } while (true);


                    match(input, Token.UP, null); 
                    	retval.baseDescr = factory.createForAll(start, lhsList);	

                    }
                    break;
                case 9 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:238:4: ^( FROM pn= lhs_pattern fe= from_elements )
                    {
                    match(input,FROM,FOLLOW_FROM_in_lhs1233); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_lhs_pattern_in_lhs1237);
                    pn=lhs_pattern();
                    _fsp--;

                    pushFollow(FOLLOW_from_elements_in_lhs1241);
                    fe=from_elements();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.baseDescr = factory.setupFrom(pn, fe.patternSourceDescr);	

                    }
                    break;
                case 10 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:240:4: pn= lhs_pattern
                    {
                    pushFollow(FOLLOW_lhs_pattern_in_lhs1252);
                    pn=lhs_pattern();
                    _fsp--;

                    	retval.baseDescr = pn;	

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
        return retval;
    }
    // $ANTLR end lhs

    public static class from_elements_return extends TreeRuleReturnScope {
        public PatternSourceDescr patternSourceDescr;
    };

    // $ANTLR start from_elements
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:244:1: from_elements returns [PatternSourceDescr patternSourceDescr] : ( ^(start= ACCUMULATE dt= lhs ret= accumulate_parts[$patternSourceDescr] ) | ^(start= COLLECT dt= lhs ) | ^(start= VK_ENTRY_POINT entryId= VT_ENTRYPOINT_ID ) | fs= from_source_clause );
    public final from_elements_return from_elements() throws RecognitionException {
        from_elements_return retval = new from_elements_return();
        retval.start = input.LT(1);

        DroolsTree start=null;
        DroolsTree entryId=null;
        lhs_return dt = null;

        AccumulateDescr ret = null;

        from_source_clause_return fs = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:245:2: ( ^(start= ACCUMULATE dt= lhs ret= accumulate_parts[$patternSourceDescr] ) | ^(start= COLLECT dt= lhs ) | ^(start= VK_ENTRY_POINT entryId= VT_ENTRYPOINT_ID ) | fs= from_source_clause )
            int alt33=4;
            switch ( input.LA(1) ) {
            case ACCUMULATE:
                {
                alt33=1;
                }
                break;
            case COLLECT:
                {
                alt33=2;
                }
                break;
            case VK_ENTRY_POINT:
                {
                alt33=3;
                }
                break;
            case VT_FROM_SOURCE:
                {
                alt33=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("244:1: from_elements returns [PatternSourceDescr patternSourceDescr] : ( ^(start= ACCUMULATE dt= lhs ret= accumulate_parts[$patternSourceDescr] ) | ^(start= COLLECT dt= lhs ) | ^(start= VK_ENTRY_POINT entryId= VT_ENTRYPOINT_ID ) | fs= from_source_clause );", 33, 0, input);

                throw nvae;
            }

            switch (alt33) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:245:4: ^(start= ACCUMULATE dt= lhs ret= accumulate_parts[$patternSourceDescr] )
                    {
                    start=(DroolsTree)input.LT(1);
                    match(input,ACCUMULATE,FOLLOW_ACCUMULATE_in_from_elements1273); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_lhs_in_from_elements1277);
                    dt=lhs();
                    _fsp--;

                    	retval.patternSourceDescr = factory.createAccumulate(start, dt.baseDescr);	
                    pushFollow(FOLLOW_accumulate_parts_in_from_elements1287);
                    ret=accumulate_parts(retval.patternSourceDescr);
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.patternSourceDescr = ret;	

                    }
                    break;
                case 2 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:249:4: ^(start= COLLECT dt= lhs )
                    {
                    start=(DroolsTree)input.LT(1);
                    match(input,COLLECT,FOLLOW_COLLECT_in_from_elements1300); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_lhs_in_from_elements1304);
                    dt=lhs();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.patternSourceDescr = factory.createCollect(start, dt.baseDescr);	

                    }
                    break;
                case 3 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:251:4: ^(start= VK_ENTRY_POINT entryId= VT_ENTRYPOINT_ID )
                    {
                    start=(DroolsTree)input.LT(1);
                    match(input,VK_ENTRY_POINT,FOLLOW_VK_ENTRY_POINT_in_from_elements1316); 

                    match(input, Token.DOWN, null); 
                    entryId=(DroolsTree)input.LT(1);
                    match(input,VT_ENTRYPOINT_ID,FOLLOW_VT_ENTRYPOINT_ID_in_from_elements1320); 

                    match(input, Token.UP, null); 
                    	retval.patternSourceDescr = factory.createEntryPoint(start, entryId);	

                    }
                    break;
                case 4 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:253:4: fs= from_source_clause
                    {
                    pushFollow(FOLLOW_from_source_clause_in_from_elements1331);
                    fs=from_source_clause();
                    _fsp--;

                    	retval.patternSourceDescr = fs.fromDescr;	

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
        return retval;
    }
    // $ANTLR end from_elements


    // $ANTLR start accumulate_parts
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:257:1: accumulate_parts[PatternSourceDescr patternSourceDescr] returns [AccumulateDescr accumulateDescr] : (ac1= accumulate_init_clause[$patternSourceDescr] | ac2= accumulate_id_clause[$patternSourceDescr] );
    public final AccumulateDescr accumulate_parts(PatternSourceDescr patternSourceDescr) throws RecognitionException {
        AccumulateDescr accumulateDescr = null;

        accumulate_init_clause_return ac1 = null;

        AccumulateDescr ac2 = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:258:2: (ac1= accumulate_init_clause[$patternSourceDescr] | ac2= accumulate_id_clause[$patternSourceDescr] )
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==VT_ACCUMULATE_INIT_CLAUSE) ) {
                alt34=1;
            }
            else if ( (LA34_0==VT_ACCUMULATE_ID_CLAUSE) ) {
                alt34=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("257:1: accumulate_parts[PatternSourceDescr patternSourceDescr] returns [AccumulateDescr accumulateDescr] : (ac1= accumulate_init_clause[$patternSourceDescr] | ac2= accumulate_id_clause[$patternSourceDescr] );", 34, 0, input);

                throw nvae;
            }
            switch (alt34) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:258:4: ac1= accumulate_init_clause[$patternSourceDescr]
                    {
                    pushFollow(FOLLOW_accumulate_init_clause_in_accumulate_parts1352);
                    ac1=accumulate_init_clause(patternSourceDescr);
                    _fsp--;

                    	accumulateDescr = ac1.accumulateDescr;	

                    }
                    break;
                case 2 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:260:4: ac2= accumulate_id_clause[$patternSourceDescr]
                    {
                    pushFollow(FOLLOW_accumulate_id_clause_in_accumulate_parts1363);
                    ac2=accumulate_id_clause(patternSourceDescr);
                    _fsp--;

                    	accumulateDescr = ac2;	

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
        return accumulateDescr;
    }
    // $ANTLR end accumulate_parts

    public static class accumulate_init_clause_return extends TreeRuleReturnScope {
        public AccumulateDescr accumulateDescr;
    };

    // $ANTLR start accumulate_init_clause
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:264:1: accumulate_init_clause[PatternSourceDescr accumulateParam] returns [AccumulateDescr accumulateDescr] : ^( VT_ACCUMULATE_INIT_CLAUSE ^(start= INIT pc1= VT_PAREN_CHUNK ) ^( VK_ACTION pc2= VT_PAREN_CHUNK ) (rev= accumulate_init_reverse_clause )? ^( VK_RESULT pc3= VT_PAREN_CHUNK ) ) ;
    public final accumulate_init_clause_return accumulate_init_clause(PatternSourceDescr accumulateParam) throws RecognitionException {
        accumulate_init_clause_return retval = new accumulate_init_clause_return();
        retval.start = input.LT(1);

        DroolsTree start=null;
        DroolsTree pc1=null;
        DroolsTree pc2=null;
        DroolsTree pc3=null;
        accumulate_init_reverse_clause_return rev = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:265:2: ( ^( VT_ACCUMULATE_INIT_CLAUSE ^(start= INIT pc1= VT_PAREN_CHUNK ) ^( VK_ACTION pc2= VT_PAREN_CHUNK ) (rev= accumulate_init_reverse_clause )? ^( VK_RESULT pc3= VT_PAREN_CHUNK ) ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:265:4: ^( VT_ACCUMULATE_INIT_CLAUSE ^(start= INIT pc1= VT_PAREN_CHUNK ) ^( VK_ACTION pc2= VT_PAREN_CHUNK ) (rev= accumulate_init_reverse_clause )? ^( VK_RESULT pc3= VT_PAREN_CHUNK ) )
            {
            match(input,VT_ACCUMULATE_INIT_CLAUSE,FOLLOW_VT_ACCUMULATE_INIT_CLAUSE_in_accumulate_init_clause1386); 

            match(input, Token.DOWN, null); 
            start=(DroolsTree)input.LT(1);
            match(input,INIT,FOLLOW_INIT_in_accumulate_init_clause1395); 

            match(input, Token.DOWN, null); 
            pc1=(DroolsTree)input.LT(1);
            match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_accumulate_init_clause1399); 

            match(input, Token.UP, null); 
            match(input,VK_ACTION,FOLLOW_VK_ACTION_in_accumulate_init_clause1407); 

            match(input, Token.DOWN, null); 
            pc2=(DroolsTree)input.LT(1);
            match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_accumulate_init_clause1411); 

            match(input, Token.UP, null); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:268:7: (rev= accumulate_init_reverse_clause )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==VK_REVERSE) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:268:7: rev= accumulate_init_reverse_clause
                    {
                    pushFollow(FOLLOW_accumulate_init_reverse_clause_in_accumulate_init_clause1420);
                    rev=accumulate_init_reverse_clause();
                    _fsp--;


                    }
                    break;

            }

            match(input,VK_RESULT,FOLLOW_VK_RESULT_in_accumulate_init_clause1427); 

            match(input, Token.DOWN, null); 
            pc3=(DroolsTree)input.LT(1);
            match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_accumulate_init_clause1431); 

            match(input, Token.UP, null); 

            match(input, Token.UP, null); 
            	if (null == rev){
            			retval.accumulateDescr = factory.setupAccumulateInit(accumulateParam, start, pc1, pc2, pc3, null);
            		} else {
            			retval.accumulateDescr = factory.setupAccumulateInit(accumulateParam, start, pc1, pc2, pc3, rev.vkReverseChunk);
            		}	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end accumulate_init_clause

    public static class accumulate_init_reverse_clause_return extends TreeRuleReturnScope {
        public DroolsTree vkReverse;
        public DroolsTree vkReverseChunk;
    };

    // $ANTLR start accumulate_init_reverse_clause
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:277:1: accumulate_init_reverse_clause returns [DroolsTree vkReverse, DroolsTree vkReverseChunk] : ^(vk= VK_REVERSE pc= VT_PAREN_CHUNK ) ;
    public final accumulate_init_reverse_clause_return accumulate_init_reverse_clause() throws RecognitionException {
        accumulate_init_reverse_clause_return retval = new accumulate_init_reverse_clause_return();
        retval.start = input.LT(1);

        DroolsTree vk=null;
        DroolsTree pc=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:278:2: ( ^(vk= VK_REVERSE pc= VT_PAREN_CHUNK ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:278:4: ^(vk= VK_REVERSE pc= VT_PAREN_CHUNK )
            {
            vk=(DroolsTree)input.LT(1);
            match(input,VK_REVERSE,FOLLOW_VK_REVERSE_in_accumulate_init_reverse_clause1454); 

            match(input, Token.DOWN, null); 
            pc=(DroolsTree)input.LT(1);
            match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_accumulate_init_reverse_clause1458); 

            match(input, Token.UP, null); 
            	retval.vkReverse = vk;
            		retval.vkReverseChunk = pc;	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end accumulate_init_reverse_clause


    // $ANTLR start accumulate_id_clause
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:283:1: accumulate_id_clause[PatternSourceDescr accumulateParam] returns [AccumulateDescr accumulateDescr] : ^( VT_ACCUMULATE_ID_CLAUSE id= ID pc= VT_PAREN_CHUNK ) ;
    public final AccumulateDescr accumulate_id_clause(PatternSourceDescr accumulateParam) throws RecognitionException {
        AccumulateDescr accumulateDescr = null;

        DroolsTree id=null;
        DroolsTree pc=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:284:2: ( ^( VT_ACCUMULATE_ID_CLAUSE id= ID pc= VT_PAREN_CHUNK ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:284:4: ^( VT_ACCUMULATE_ID_CLAUSE id= ID pc= VT_PAREN_CHUNK )
            {
            match(input,VT_ACCUMULATE_ID_CLAUSE,FOLLOW_VT_ACCUMULATE_ID_CLAUSE_in_accumulate_id_clause1480); 

            match(input, Token.DOWN, null); 
            id=(DroolsTree)input.LT(1);
            match(input,ID,FOLLOW_ID_in_accumulate_id_clause1484); 
            pc=(DroolsTree)input.LT(1);
            match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_accumulate_id_clause1488); 

            match(input, Token.UP, null); 
            	accumulateDescr = factory.setupAccumulateId(accumulateParam, id, pc);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return accumulateDescr;
    }
    // $ANTLR end accumulate_id_clause

    protected static class from_source_clause_scope {
        AccessorDescr accessorDescr;
    }
    protected Stack from_source_clause_stack = new Stack();

    public static class from_source_clause_return extends TreeRuleReturnScope {
        public FromDescr fromDescr;
        public AccessorDescr retAccessorDescr;
    };

    // $ANTLR start from_source_clause
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:288:1: from_source_clause returns [FromDescr fromDescr, AccessorDescr retAccessorDescr] : ^( VT_FROM_SOURCE id= ID (pc= VT_PAREN_CHUNK )? ( expression_chain )? ) ;
    public final from_source_clause_return from_source_clause() throws RecognitionException {
        from_source_clause_stack.push(new from_source_clause_scope());
        from_source_clause_return retval = new from_source_clause_return();
        retval.start = input.LT(1);

        DroolsTree id=null;
        DroolsTree pc=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:291:3: ( ^( VT_FROM_SOURCE id= ID (pc= VT_PAREN_CHUNK )? ( expression_chain )? ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:291:5: ^( VT_FROM_SOURCE id= ID (pc= VT_PAREN_CHUNK )? ( expression_chain )? )
            {
            match(input,VT_FROM_SOURCE,FOLLOW_VT_FROM_SOURCE_in_from_source_clause1510); 

            match(input, Token.DOWN, null); 
            id=(DroolsTree)input.LT(1);
            match(input,ID,FOLLOW_ID_in_from_source_clause1514); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:291:30: (pc= VT_PAREN_CHUNK )?
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==VT_PAREN_CHUNK) ) {
                alt36=1;
            }
            switch (alt36) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:291:30: pc= VT_PAREN_CHUNK
                    {
                    pc=(DroolsTree)input.LT(1);
                    match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_from_source_clause1518); 

                    }
                    break;

            }

            	((from_source_clause_scope)from_source_clause_stack.peek()).accessorDescr = factory.createAccessor(id, pc);	
            		retval.retAccessorDescr = ((from_source_clause_scope)from_source_clause_stack.peek()).accessorDescr;	
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:294:3: ( expression_chain )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==VT_EXPRESSION_CHAIN) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:294:3: expression_chain
                    {
                    pushFollow(FOLLOW_expression_chain_in_from_source_clause1527);
                    expression_chain();
                    _fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 
            	retval.fromDescr = factory.createFromSource(factory.setupAccessorOffset(((from_source_clause_scope)from_source_clause_stack.peek()).accessorDescr)); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            from_source_clause_stack.pop();
        }
        return retval;
    }
    // $ANTLR end from_source_clause

    public static class expression_chain_return extends TreeRuleReturnScope {
    };

    // $ANTLR start expression_chain
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:298:1: expression_chain : ^(start= VT_EXPRESSION_CHAIN id= ID (sc= VT_SQUARE_CHUNK )? (pc= VT_PAREN_CHUNK )? ( expression_chain )? ) ;
    public final expression_chain_return expression_chain() throws RecognitionException {
        expression_chain_return retval = new expression_chain_return();
        retval.start = input.LT(1);

        DroolsTree start=null;
        DroolsTree id=null;
        DroolsTree sc=null;
        DroolsTree pc=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:299:2: ( ^(start= VT_EXPRESSION_CHAIN id= ID (sc= VT_SQUARE_CHUNK )? (pc= VT_PAREN_CHUNK )? ( expression_chain )? ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:299:4: ^(start= VT_EXPRESSION_CHAIN id= ID (sc= VT_SQUARE_CHUNK )? (pc= VT_PAREN_CHUNK )? ( expression_chain )? )
            {
            start=(DroolsTree)input.LT(1);
            match(input,VT_EXPRESSION_CHAIN,FOLLOW_VT_EXPRESSION_CHAIN_in_expression_chain1546); 

            match(input, Token.DOWN, null); 
            id=(DroolsTree)input.LT(1);
            match(input,ID,FOLLOW_ID_in_expression_chain1550); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:299:40: (sc= VT_SQUARE_CHUNK )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==VT_SQUARE_CHUNK) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:299:40: sc= VT_SQUARE_CHUNK
                    {
                    sc=(DroolsTree)input.LT(1);
                    match(input,VT_SQUARE_CHUNK,FOLLOW_VT_SQUARE_CHUNK_in_expression_chain1554); 

                    }
                    break;

            }

            // src/main/resources/org/drools/lang/DescrBuilderTree.g:299:60: (pc= VT_PAREN_CHUNK )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==VT_PAREN_CHUNK) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:299:60: pc= VT_PAREN_CHUNK
                    {
                    pc=(DroolsTree)input.LT(1);
                    match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_expression_chain1559); 

                    }
                    break;

            }

            	DeclarativeInvokerDescr declarativeInvokerResult = factory.createExpressionChain(start, id, sc, pc);	
            		((from_source_clause_scope)from_source_clause_stack.peek()).accessorDescr.addInvoker(declarativeInvokerResult);	
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:302:3: ( expression_chain )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==VT_EXPRESSION_CHAIN) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:302:3: expression_chain
                    {
                    pushFollow(FOLLOW_expression_chain_in_expression_chain1567);
                    expression_chain();
                    _fsp--;


                    }
                    break;

            }


            match(input, Token.UP, null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end expression_chain


    // $ANTLR start lhs_pattern
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:305:1: lhs_pattern returns [BaseDescr baseDescr] : ^( VT_PATTERN fe= fact_expression ) (oc= over_clause )? ;
    public final BaseDescr lhs_pattern() throws RecognitionException {
        BaseDescr baseDescr = null;

        fact_expression_return fe = null;

        List oc = null;


        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:306:2: ( ^( VT_PATTERN fe= fact_expression ) (oc= over_clause )? )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:306:4: ^( VT_PATTERN fe= fact_expression ) (oc= over_clause )?
            {
            match(input,VT_PATTERN,FOLLOW_VT_PATTERN_in_lhs_pattern1585); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_fact_expression_in_lhs_pattern1589);
            fe=fact_expression();
            _fsp--;


            match(input, Token.UP, null); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:306:39: (oc= over_clause )?
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==OVER) ) {
                alt41=1;
            }
            switch (alt41) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:306:39: oc= over_clause
                    {
                    pushFollow(FOLLOW_over_clause_in_lhs_pattern1594);
                    oc=over_clause();
                    _fsp--;


                    }
                    break;

            }

            	baseDescr = factory.setupBehavior(fe.descr, oc);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return baseDescr;
    }
    // $ANTLR end lhs_pattern


    // $ANTLR start over_clause
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:310:1: over_clause returns [List behaviorList] : ^( OVER (oe= over_element )+ ) ;
    public final List over_clause() throws RecognitionException {
        List behaviorList = null;

        BehaviorDescr oe = null;


        behaviorList = new LinkedList();
        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:312:2: ( ^( OVER (oe= over_element )+ ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:312:4: ^( OVER (oe= over_element )+ )
            {
            match(input,OVER,FOLLOW_OVER_in_over_clause1619); 

            match(input, Token.DOWN, null); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:312:11: (oe= over_element )+
            int cnt42=0;
            loop42:
            do {
                int alt42=2;
                int LA42_0 = input.LA(1);

                if ( (LA42_0==VT_BEHAVIOR) ) {
                    alt42=1;
                }


                switch (alt42) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:312:12: oe= over_element
            	    {
            	    pushFollow(FOLLOW_over_element_in_over_clause1624);
            	    oe=over_element();
            	    _fsp--;

            	    behaviorList.add(oe);

            	    }
            	    break;

            	default :
            	    if ( cnt42 >= 1 ) break loop42;
                        EarlyExitException eee =
                            new EarlyExitException(42, input);
                        throw eee;
                }
                cnt42++;
            } while (true);


            match(input, Token.UP, null); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return behaviorList;
    }
    // $ANTLR end over_clause


    // $ANTLR start over_element
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:315:1: over_element returns [BehaviorDescr behavior] : ^( VT_BEHAVIOR ID id2= ID pc= VT_PAREN_CHUNK ) ;
    public final BehaviorDescr over_element() throws RecognitionException {
        BehaviorDescr behavior = null;

        DroolsTree id2=null;
        DroolsTree pc=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:316:2: ( ^( VT_BEHAVIOR ID id2= ID pc= VT_PAREN_CHUNK ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:316:4: ^( VT_BEHAVIOR ID id2= ID pc= VT_PAREN_CHUNK )
            {
            match(input,VT_BEHAVIOR,FOLLOW_VT_BEHAVIOR_in_over_element1645); 

            match(input, Token.DOWN, null); 
            match(input,ID,FOLLOW_ID_in_over_element1647); 
            id2=(DroolsTree)input.LT(1);
            match(input,ID,FOLLOW_ID_in_over_element1651); 
            pc=(DroolsTree)input.LT(1);
            match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_over_element1655); 

            match(input, Token.UP, null); 
            	behavior = factory.createBehavior(id2,pc);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return behavior;
    }
    // $ANTLR end over_element

    public static class fact_expression_return extends TreeRuleReturnScope {
        public BaseDescr descr;
    };

    // $ANTLR start fact_expression
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:320:1: fact_expression returns [BaseDescr descr] : ( ^( VT_FACT pt= pattern_type (fe= fact_expression )* ) | ^( VT_FACT_BINDING label= VT_LABEL fact= fact_expression ) | ^(start= VT_FACT_OR left= fact_expression right= fact_expression ) | ^( VT_FIELD field= field_element (fe= fact_expression )? ) | ^( VT_BIND_FIELD label= VT_LABEL fe= fact_expression ) | ^( VK_EVAL pc= VT_PAREN_CHUNK ) | ^(op= EQUAL fe= fact_expression ) | ^(op= NOT_EQUAL fe= fact_expression ) | ^(op= GREATER fe= fact_expression ) | ^(op= GREATER_EQUAL fe= fact_expression ) | ^(op= LESS fe= fact_expression ) | ^(op= LESS_EQUAL fe= fact_expression ) | ^(op= VK_CONTAINS (not= VK_NOT )? fe= fact_expression ) | ^(op= VK_EXCLUDES (not= VK_NOT )? fe= fact_expression ) | ^(op= VK_MATCHES (not= VK_NOT )? fe= fact_expression ) | ^(op= VK_SOUNDSLIKE (not= VK_NOT )? fe= fact_expression ) | ^(op= VK_MEMBEROF (not= VK_NOT )? fe= fact_expression ) | ^(op= ID (not= VK_NOT )? (param= VT_SQUARE_CHUNK )? fe= fact_expression ) | ^( VK_IN (not= VK_NOT )? (fe= fact_expression )+ ) | ^( DOUBLE_PIPE left= fact_expression right= fact_expression ) | ^( DOUBLE_AMPER left= fact_expression right= fact_expression ) | ^( VT_ACCESSOR_PATH (ae= accessor_element )+ ) | s= STRING | i= INT | f= FLOAT | b= BOOL | n= NULL | pc= VT_PAREN_CHUNK );
    public final fact_expression_return fact_expression() throws RecognitionException {
        fact_expression_return retval = new fact_expression_return();
        retval.start = input.LT(1);

        DroolsTree label=null;
        DroolsTree start=null;
        DroolsTree pc=null;
        DroolsTree op=null;
        DroolsTree not=null;
        DroolsTree param=null;
        DroolsTree s=null;
        DroolsTree i=null;
        DroolsTree f=null;
        DroolsTree b=null;
        DroolsTree n=null;
        BaseDescr pt = null;

        fact_expression_return fe = null;

        fact_expression_return fact = null;

        fact_expression_return left = null;

        fact_expression_return right = null;

        FieldConstraintDescr field = null;

        BaseDescr ae = null;



        	List<BaseDescr> exprList = new LinkedList<BaseDescr>();

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:323:3: ( ^( VT_FACT pt= pattern_type (fe= fact_expression )* ) | ^( VT_FACT_BINDING label= VT_LABEL fact= fact_expression ) | ^(start= VT_FACT_OR left= fact_expression right= fact_expression ) | ^( VT_FIELD field= field_element (fe= fact_expression )? ) | ^( VT_BIND_FIELD label= VT_LABEL fe= fact_expression ) | ^( VK_EVAL pc= VT_PAREN_CHUNK ) | ^(op= EQUAL fe= fact_expression ) | ^(op= NOT_EQUAL fe= fact_expression ) | ^(op= GREATER fe= fact_expression ) | ^(op= GREATER_EQUAL fe= fact_expression ) | ^(op= LESS fe= fact_expression ) | ^(op= LESS_EQUAL fe= fact_expression ) | ^(op= VK_CONTAINS (not= VK_NOT )? fe= fact_expression ) | ^(op= VK_EXCLUDES (not= VK_NOT )? fe= fact_expression ) | ^(op= VK_MATCHES (not= VK_NOT )? fe= fact_expression ) | ^(op= VK_SOUNDSLIKE (not= VK_NOT )? fe= fact_expression ) | ^(op= VK_MEMBEROF (not= VK_NOT )? fe= fact_expression ) | ^(op= ID (not= VK_NOT )? (param= VT_SQUARE_CHUNK )? fe= fact_expression ) | ^( VK_IN (not= VK_NOT )? (fe= fact_expression )+ ) | ^( DOUBLE_PIPE left= fact_expression right= fact_expression ) | ^( DOUBLE_AMPER left= fact_expression right= fact_expression ) | ^( VT_ACCESSOR_PATH (ae= accessor_element )+ ) | s= STRING | i= INT | f= FLOAT | b= BOOL | n= NULL | pc= VT_PAREN_CHUNK )
            int alt55=28;
            switch ( input.LA(1) ) {
            case VT_FACT:
                {
                alt55=1;
                }
                break;
            case VT_FACT_BINDING:
                {
                alt55=2;
                }
                break;
            case VT_FACT_OR:
                {
                alt55=3;
                }
                break;
            case VT_FIELD:
                {
                alt55=4;
                }
                break;
            case VT_BIND_FIELD:
                {
                alt55=5;
                }
                break;
            case VK_EVAL:
                {
                alt55=6;
                }
                break;
            case EQUAL:
                {
                alt55=7;
                }
                break;
            case NOT_EQUAL:
                {
                alt55=8;
                }
                break;
            case GREATER:
                {
                alt55=9;
                }
                break;
            case GREATER_EQUAL:
                {
                alt55=10;
                }
                break;
            case LESS:
                {
                alt55=11;
                }
                break;
            case LESS_EQUAL:
                {
                alt55=12;
                }
                break;
            case VK_CONTAINS:
                {
                alt55=13;
                }
                break;
            case VK_EXCLUDES:
                {
                alt55=14;
                }
                break;
            case VK_MATCHES:
                {
                alt55=15;
                }
                break;
            case VK_SOUNDSLIKE:
                {
                alt55=16;
                }
                break;
            case VK_MEMBEROF:
                {
                alt55=17;
                }
                break;
            case ID:
                {
                alt55=18;
                }
                break;
            case VK_IN:
                {
                alt55=19;
                }
                break;
            case DOUBLE_PIPE:
                {
                alt55=20;
                }
                break;
            case DOUBLE_AMPER:
                {
                alt55=21;
                }
                break;
            case VT_ACCESSOR_PATH:
                {
                alt55=22;
                }
                break;
            case STRING:
                {
                alt55=23;
                }
                break;
            case INT:
                {
                alt55=24;
                }
                break;
            case FLOAT:
                {
                alt55=25;
                }
                break;
            case BOOL:
                {
                alt55=26;
                }
                break;
            case NULL:
                {
                alt55=27;
                }
                break;
            case VT_PAREN_CHUNK:
                {
                alt55=28;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("320:1: fact_expression returns [BaseDescr descr] : ( ^( VT_FACT pt= pattern_type (fe= fact_expression )* ) | ^( VT_FACT_BINDING label= VT_LABEL fact= fact_expression ) | ^(start= VT_FACT_OR left= fact_expression right= fact_expression ) | ^( VT_FIELD field= field_element (fe= fact_expression )? ) | ^( VT_BIND_FIELD label= VT_LABEL fe= fact_expression ) | ^( VK_EVAL pc= VT_PAREN_CHUNK ) | ^(op= EQUAL fe= fact_expression ) | ^(op= NOT_EQUAL fe= fact_expression ) | ^(op= GREATER fe= fact_expression ) | ^(op= GREATER_EQUAL fe= fact_expression ) | ^(op= LESS fe= fact_expression ) | ^(op= LESS_EQUAL fe= fact_expression ) | ^(op= VK_CONTAINS (not= VK_NOT )? fe= fact_expression ) | ^(op= VK_EXCLUDES (not= VK_NOT )? fe= fact_expression ) | ^(op= VK_MATCHES (not= VK_NOT )? fe= fact_expression ) | ^(op= VK_SOUNDSLIKE (not= VK_NOT )? fe= fact_expression ) | ^(op= VK_MEMBEROF (not= VK_NOT )? fe= fact_expression ) | ^(op= ID (not= VK_NOT )? (param= VT_SQUARE_CHUNK )? fe= fact_expression ) | ^( VK_IN (not= VK_NOT )? (fe= fact_expression )+ ) | ^( DOUBLE_PIPE left= fact_expression right= fact_expression ) | ^( DOUBLE_AMPER left= fact_expression right= fact_expression ) | ^( VT_ACCESSOR_PATH (ae= accessor_element )+ ) | s= STRING | i= INT | f= FLOAT | b= BOOL | n= NULL | pc= VT_PAREN_CHUNK );", 55, 0, input);

                throw nvae;
            }

            switch (alt55) {
                case 1 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:323:5: ^( VT_FACT pt= pattern_type (fe= fact_expression )* )
                    {
                    match(input,VT_FACT,FOLLOW_VT_FACT_in_fact_expression1678); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_pattern_type_in_fact_expression1682);
                    pt=pattern_type();
                    _fsp--;

                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:323:31: (fe= fact_expression )*
                    loop43:
                    do {
                        int alt43=2;
                        int LA43_0 = input.LA(1);

                        if ( (LA43_0==VT_FACT||LA43_0==VT_PAREN_CHUNK||(LA43_0>=VT_FACT_BINDING && LA43_0<=VT_ACCESSOR_PATH)||(LA43_0>=VK_EVAL && LA43_0<=VK_MEMBEROF)||LA43_0==VK_IN||LA43_0==ID||LA43_0==STRING||(LA43_0>=BOOL && LA43_0<=DOUBLE_AMPER)||(LA43_0>=EQUAL && LA43_0<=NOT_EQUAL)||(LA43_0>=FLOAT && LA43_0<=NULL)) ) {
                            alt43=1;
                        }


                        switch (alt43) {
                    	case 1 :
                    	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:323:32: fe= fact_expression
                    	    {
                    	    pushFollow(FOLLOW_fact_expression_in_fact_expression1687);
                    	    fe=fact_expression();
                    	    _fsp--;

                    	    exprList.add(fe.descr);

                    	    }
                    	    break;

                    	default :
                    	    break loop43;
                        }
                    } while (true);


                    match(input, Token.UP, null); 
                    	retval.descr = factory.createPattern(pt, exprList);	

                    }
                    break;
                case 2 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:325:4: ^( VT_FACT_BINDING label= VT_LABEL fact= fact_expression )
                    {
                    match(input,VT_FACT_BINDING,FOLLOW_VT_FACT_BINDING_in_fact_expression1701); 

                    match(input, Token.DOWN, null); 
                    label=(DroolsTree)input.LT(1);
                    match(input,VT_LABEL,FOLLOW_VT_LABEL_in_fact_expression1705); 
                    pushFollow(FOLLOW_fact_expression_in_fact_expression1709);
                    fact=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupPatternBiding(label, fact.descr);	

                    }
                    break;
                case 3 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:327:4: ^(start= VT_FACT_OR left= fact_expression right= fact_expression )
                    {
                    start=(DroolsTree)input.LT(1);
                    match(input,VT_FACT_OR,FOLLOW_VT_FACT_OR_in_fact_expression1721); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_fact_expression_in_fact_expression1725);
                    left=fact_expression();
                    _fsp--;

                    pushFollow(FOLLOW_fact_expression_in_fact_expression1729);
                    right=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.createFactOr(start, left.descr, right.descr);	

                    }
                    break;
                case 4 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:330:4: ^( VT_FIELD field= field_element (fe= fact_expression )? )
                    {
                    match(input,VT_FIELD,FOLLOW_VT_FIELD_in_fact_expression1740); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_field_element_in_fact_expression1744);
                    field=field_element();
                    _fsp--;

                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:330:37: (fe= fact_expression )?
                    int alt44=2;
                    int LA44_0 = input.LA(1);

                    if ( (LA44_0==VT_FACT||LA44_0==VT_PAREN_CHUNK||(LA44_0>=VT_FACT_BINDING && LA44_0<=VT_ACCESSOR_PATH)||(LA44_0>=VK_EVAL && LA44_0<=VK_MEMBEROF)||LA44_0==VK_IN||LA44_0==ID||LA44_0==STRING||(LA44_0>=BOOL && LA44_0<=DOUBLE_AMPER)||(LA44_0>=EQUAL && LA44_0<=NOT_EQUAL)||(LA44_0>=FLOAT && LA44_0<=NULL)) ) {
                        alt44=1;
                    }
                    switch (alt44) {
                        case 1 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:330:37: fe= fact_expression
                            {
                            pushFollow(FOLLOW_fact_expression_in_fact_expression1748);
                            fe=fact_expression();
                            _fsp--;


                            }
                            break;

                    }


                    match(input, Token.UP, null); 
                    	if (null != fe){
                    			retval.descr = factory.setupFieldConstraint(field, fe.descr);
                    		} else {
                    			retval.descr = factory.setupFieldConstraint(field, null);
                    		}	

                    }
                    break;
                case 5 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:336:4: ^( VT_BIND_FIELD label= VT_LABEL fe= fact_expression )
                    {
                    match(input,VT_BIND_FIELD,FOLLOW_VT_BIND_FIELD_in_fact_expression1759); 

                    match(input, Token.DOWN, null); 
                    label=(DroolsTree)input.LT(1);
                    match(input,VT_LABEL,FOLLOW_VT_LABEL_in_fact_expression1763); 
                    pushFollow(FOLLOW_fact_expression_in_fact_expression1767);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.createFieldBinding(label, fe.descr);	

                    }
                    break;
                case 6 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:339:4: ^( VK_EVAL pc= VT_PAREN_CHUNK )
                    {
                    match(input,VK_EVAL,FOLLOW_VK_EVAL_in_fact_expression1778); 

                    match(input, Token.DOWN, null); 
                    pc=(DroolsTree)input.LT(1);
                    match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_fact_expression1782); 

                    match(input, Token.UP, null); 
                    	retval.descr = factory.createPredicate(pc);	

                    }
                    break;
                case 7 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:342:4: ^(op= EQUAL fe= fact_expression )
                    {
                    op=(DroolsTree)input.LT(1);
                    match(input,EQUAL,FOLLOW_EQUAL_in_fact_expression1795); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_fact_expression_in_fact_expression1799);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupRestriction(op, null, fe.descr);	

                    }
                    break;
                case 8 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:344:4: ^(op= NOT_EQUAL fe= fact_expression )
                    {
                    op=(DroolsTree)input.LT(1);
                    match(input,NOT_EQUAL,FOLLOW_NOT_EQUAL_in_fact_expression1811); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_fact_expression_in_fact_expression1815);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupRestriction(op, null, fe.descr);	

                    }
                    break;
                case 9 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:346:4: ^(op= GREATER fe= fact_expression )
                    {
                    op=(DroolsTree)input.LT(1);
                    match(input,GREATER,FOLLOW_GREATER_in_fact_expression1827); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_fact_expression_in_fact_expression1831);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupRestriction(op, null, fe.descr);	

                    }
                    break;
                case 10 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:348:4: ^(op= GREATER_EQUAL fe= fact_expression )
                    {
                    op=(DroolsTree)input.LT(1);
                    match(input,GREATER_EQUAL,FOLLOW_GREATER_EQUAL_in_fact_expression1843); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_fact_expression_in_fact_expression1847);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupRestriction(op, null, fe.descr);	

                    }
                    break;
                case 11 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:350:4: ^(op= LESS fe= fact_expression )
                    {
                    op=(DroolsTree)input.LT(1);
                    match(input,LESS,FOLLOW_LESS_in_fact_expression1859); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_fact_expression_in_fact_expression1863);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupRestriction(op, null, fe.descr);	

                    }
                    break;
                case 12 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:352:4: ^(op= LESS_EQUAL fe= fact_expression )
                    {
                    op=(DroolsTree)input.LT(1);
                    match(input,LESS_EQUAL,FOLLOW_LESS_EQUAL_in_fact_expression1875); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_fact_expression_in_fact_expression1879);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupRestriction(op, null, fe.descr);	

                    }
                    break;
                case 13 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:354:4: ^(op= VK_CONTAINS (not= VK_NOT )? fe= fact_expression )
                    {
                    op=(DroolsTree)input.LT(1);
                    match(input,VK_CONTAINS,FOLLOW_VK_CONTAINS_in_fact_expression1891); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:354:24: (not= VK_NOT )?
                    int alt45=2;
                    int LA45_0 = input.LA(1);

                    if ( (LA45_0==VK_NOT) ) {
                        alt45=1;
                    }
                    switch (alt45) {
                        case 1 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:354:24: not= VK_NOT
                            {
                            not=(DroolsTree)input.LT(1);
                            match(input,VK_NOT,FOLLOW_VK_NOT_in_fact_expression1895); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_fact_expression_in_fact_expression1900);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupRestriction(op, not, fe.descr);	

                    }
                    break;
                case 14 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:356:4: ^(op= VK_EXCLUDES (not= VK_NOT )? fe= fact_expression )
                    {
                    op=(DroolsTree)input.LT(1);
                    match(input,VK_EXCLUDES,FOLLOW_VK_EXCLUDES_in_fact_expression1912); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:356:24: (not= VK_NOT )?
                    int alt46=2;
                    int LA46_0 = input.LA(1);

                    if ( (LA46_0==VK_NOT) ) {
                        alt46=1;
                    }
                    switch (alt46) {
                        case 1 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:356:24: not= VK_NOT
                            {
                            not=(DroolsTree)input.LT(1);
                            match(input,VK_NOT,FOLLOW_VK_NOT_in_fact_expression1916); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_fact_expression_in_fact_expression1921);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupRestriction(op, not, fe.descr);	

                    }
                    break;
                case 15 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:358:4: ^(op= VK_MATCHES (not= VK_NOT )? fe= fact_expression )
                    {
                    op=(DroolsTree)input.LT(1);
                    match(input,VK_MATCHES,FOLLOW_VK_MATCHES_in_fact_expression1933); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:358:23: (not= VK_NOT )?
                    int alt47=2;
                    int LA47_0 = input.LA(1);

                    if ( (LA47_0==VK_NOT) ) {
                        alt47=1;
                    }
                    switch (alt47) {
                        case 1 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:358:23: not= VK_NOT
                            {
                            not=(DroolsTree)input.LT(1);
                            match(input,VK_NOT,FOLLOW_VK_NOT_in_fact_expression1937); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_fact_expression_in_fact_expression1942);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupRestriction(op, not, fe.descr);	

                    }
                    break;
                case 16 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:360:4: ^(op= VK_SOUNDSLIKE (not= VK_NOT )? fe= fact_expression )
                    {
                    op=(DroolsTree)input.LT(1);
                    match(input,VK_SOUNDSLIKE,FOLLOW_VK_SOUNDSLIKE_in_fact_expression1954); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:360:26: (not= VK_NOT )?
                    int alt48=2;
                    int LA48_0 = input.LA(1);

                    if ( (LA48_0==VK_NOT) ) {
                        alt48=1;
                    }
                    switch (alt48) {
                        case 1 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:360:26: not= VK_NOT
                            {
                            not=(DroolsTree)input.LT(1);
                            match(input,VK_NOT,FOLLOW_VK_NOT_in_fact_expression1958); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_fact_expression_in_fact_expression1963);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupRestriction(op, not, fe.descr);	

                    }
                    break;
                case 17 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:362:4: ^(op= VK_MEMBEROF (not= VK_NOT )? fe= fact_expression )
                    {
                    op=(DroolsTree)input.LT(1);
                    match(input,VK_MEMBEROF,FOLLOW_VK_MEMBEROF_in_fact_expression1975); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:362:24: (not= VK_NOT )?
                    int alt49=2;
                    int LA49_0 = input.LA(1);

                    if ( (LA49_0==VK_NOT) ) {
                        alt49=1;
                    }
                    switch (alt49) {
                        case 1 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:362:24: not= VK_NOT
                            {
                            not=(DroolsTree)input.LT(1);
                            match(input,VK_NOT,FOLLOW_VK_NOT_in_fact_expression1979); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_fact_expression_in_fact_expression1984);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupRestriction(op, not, fe.descr);	

                    }
                    break;
                case 18 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:364:4: ^(op= ID (not= VK_NOT )? (param= VT_SQUARE_CHUNK )? fe= fact_expression )
                    {
                    op=(DroolsTree)input.LT(1);
                    match(input,ID,FOLLOW_ID_in_fact_expression1996); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:364:15: (not= VK_NOT )?
                    int alt50=2;
                    int LA50_0 = input.LA(1);

                    if ( (LA50_0==VK_NOT) ) {
                        alt50=1;
                    }
                    switch (alt50) {
                        case 1 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:364:15: not= VK_NOT
                            {
                            not=(DroolsTree)input.LT(1);
                            match(input,VK_NOT,FOLLOW_VK_NOT_in_fact_expression2000); 

                            }
                            break;

                    }

                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:364:29: (param= VT_SQUARE_CHUNK )?
                    int alt51=2;
                    int LA51_0 = input.LA(1);

                    if ( (LA51_0==VT_SQUARE_CHUNK) ) {
                        alt51=1;
                    }
                    switch (alt51) {
                        case 1 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:364:29: param= VT_SQUARE_CHUNK
                            {
                            param=(DroolsTree)input.LT(1);
                            match(input,VT_SQUARE_CHUNK,FOLLOW_VT_SQUARE_CHUNK_in_fact_expression2005); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_fact_expression_in_fact_expression2010);
                    fe=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.setupRestriction(op, not, fe.descr, param);	

                    }
                    break;
                case 19 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:367:4: ^( VK_IN (not= VK_NOT )? (fe= fact_expression )+ )
                    {
                    match(input,VK_IN,FOLLOW_VK_IN_in_fact_expression2021); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:367:15: (not= VK_NOT )?
                    int alt52=2;
                    int LA52_0 = input.LA(1);

                    if ( (LA52_0==VK_NOT) ) {
                        alt52=1;
                    }
                    switch (alt52) {
                        case 1 :
                            // src/main/resources/org/drools/lang/DescrBuilderTree.g:367:15: not= VK_NOT
                            {
                            not=(DroolsTree)input.LT(1);
                            match(input,VK_NOT,FOLLOW_VK_NOT_in_fact_expression2025); 

                            }
                            break;

                    }

                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:367:24: (fe= fact_expression )+
                    int cnt53=0;
                    loop53:
                    do {
                        int alt53=2;
                        int LA53_0 = input.LA(1);

                        if ( (LA53_0==VT_FACT||LA53_0==VT_PAREN_CHUNK||(LA53_0>=VT_FACT_BINDING && LA53_0<=VT_ACCESSOR_PATH)||(LA53_0>=VK_EVAL && LA53_0<=VK_MEMBEROF)||LA53_0==VK_IN||LA53_0==ID||LA53_0==STRING||(LA53_0>=BOOL && LA53_0<=DOUBLE_AMPER)||(LA53_0>=EQUAL && LA53_0<=NOT_EQUAL)||(LA53_0>=FLOAT && LA53_0<=NULL)) ) {
                            alt53=1;
                        }


                        switch (alt53) {
                    	case 1 :
                    	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:367:25: fe= fact_expression
                    	    {
                    	    pushFollow(FOLLOW_fact_expression_in_fact_expression2031);
                    	    fe=fact_expression();
                    	    _fsp--;

                    	    exprList.add(fe.descr);

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt53 >= 1 ) break loop53;
                                EarlyExitException eee =
                                    new EarlyExitException(53, input);
                                throw eee;
                        }
                        cnt53++;
                    } while (true);


                    match(input, Token.UP, null); 
                    	retval.descr = factory.createRestrictionConnective(not, exprList);	

                    }
                    break;
                case 20 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:370:4: ^( DOUBLE_PIPE left= fact_expression right= fact_expression )
                    {
                    match(input,DOUBLE_PIPE,FOLLOW_DOUBLE_PIPE_in_fact_expression2046); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_fact_expression_in_fact_expression2050);
                    left=fact_expression();
                    _fsp--;

                    pushFollow(FOLLOW_fact_expression_in_fact_expression2054);
                    right=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.createOrRestrictionConnective(left.descr, right.descr);	

                    }
                    break;
                case 21 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:372:4: ^( DOUBLE_AMPER left= fact_expression right= fact_expression )
                    {
                    match(input,DOUBLE_AMPER,FOLLOW_DOUBLE_AMPER_in_fact_expression2064); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_fact_expression_in_fact_expression2068);
                    left=fact_expression();
                    _fsp--;

                    pushFollow(FOLLOW_fact_expression_in_fact_expression2072);
                    right=fact_expression();
                    _fsp--;


                    match(input, Token.UP, null); 
                    	retval.descr = factory.createAndRestrictionConnective(left.descr, right.descr);	

                    }
                    break;
                case 22 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:375:4: ^( VT_ACCESSOR_PATH (ae= accessor_element )+ )
                    {
                    match(input,VT_ACCESSOR_PATH,FOLLOW_VT_ACCESSOR_PATH_in_fact_expression2083); 

                    match(input, Token.DOWN, null); 
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:375:23: (ae= accessor_element )+
                    int cnt54=0;
                    loop54:
                    do {
                        int alt54=2;
                        int LA54_0 = input.LA(1);

                        if ( (LA54_0==VT_ACCESSOR_ELEMENT) ) {
                            alt54=1;
                        }


                        switch (alt54) {
                    	case 1 :
                    	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:375:24: ae= accessor_element
                    	    {
                    	    pushFollow(FOLLOW_accessor_element_in_fact_expression2088);
                    	    ae=accessor_element();
                    	    _fsp--;

                    	    exprList.add(ae);

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt54 >= 1 ) break loop54;
                                EarlyExitException eee =
                                    new EarlyExitException(54, input);
                                throw eee;
                        }
                        cnt54++;
                    } while (true);


                    match(input, Token.UP, null); 
                    	retval.descr = factory.createAccessorPath(exprList);	

                    }
                    break;
                case 23 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:377:4: s= STRING
                    {
                    s=(DroolsTree)input.LT(1);
                    match(input,STRING,FOLLOW_STRING_in_fact_expression2103); 
                    	retval.descr = factory.createStringLiteralRestriction(s);	

                    }
                    break;
                case 24 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:379:4: i= INT
                    {
                    i=(DroolsTree)input.LT(1);
                    match(input,INT,FOLLOW_INT_in_fact_expression2113); 
                    	retval.descr = factory.createIntLiteralRestriction(i);	

                    }
                    break;
                case 25 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:381:4: f= FLOAT
                    {
                    f=(DroolsTree)input.LT(1);
                    match(input,FLOAT,FOLLOW_FLOAT_in_fact_expression2123); 
                    	retval.descr = factory.createFloatLiteralRestriction(f);	

                    }
                    break;
                case 26 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:383:4: b= BOOL
                    {
                    b=(DroolsTree)input.LT(1);
                    match(input,BOOL,FOLLOW_BOOL_in_fact_expression2133); 
                    	retval.descr = factory.createBoolLiteralRestriction(b);	

                    }
                    break;
                case 27 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:385:4: n= NULL
                    {
                    n=(DroolsTree)input.LT(1);
                    match(input,NULL,FOLLOW_NULL_in_fact_expression2143); 
                    	retval.descr = factory.createNullLiteralRestriction(n);	

                    }
                    break;
                case 28 :
                    // src/main/resources/org/drools/lang/DescrBuilderTree.g:387:4: pc= VT_PAREN_CHUNK
                    {
                    pc=(DroolsTree)input.LT(1);
                    match(input,VT_PAREN_CHUNK,FOLLOW_VT_PAREN_CHUNK_in_fact_expression2153); 
                    	retval.descr = factory.createReturnValue(pc);	

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
        return retval;
    }
    // $ANTLR end fact_expression


    // $ANTLR start field_element
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:391:1: field_element returns [FieldConstraintDescr element] : ^( VT_ACCESSOR_PATH (ae= accessor_element )+ ) ;
    public final FieldConstraintDescr field_element() throws RecognitionException {
        FieldConstraintDescr element = null;

        BaseDescr ae = null;



        	List<BaseDescr> aeList = new LinkedList<BaseDescr>();

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:394:3: ( ^( VT_ACCESSOR_PATH (ae= accessor_element )+ ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:394:5: ^( VT_ACCESSOR_PATH (ae= accessor_element )+ )
            {
            match(input,VT_ACCESSOR_PATH,FOLLOW_VT_ACCESSOR_PATH_in_field_element2175); 

            match(input, Token.DOWN, null); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:394:24: (ae= accessor_element )+
            int cnt56=0;
            loop56:
            do {
                int alt56=2;
                int LA56_0 = input.LA(1);

                if ( (LA56_0==VT_ACCESSOR_ELEMENT) ) {
                    alt56=1;
                }


                switch (alt56) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:394:25: ae= accessor_element
            	    {
            	    pushFollow(FOLLOW_accessor_element_in_field_element2180);
            	    ae=accessor_element();
            	    _fsp--;

            	    aeList.add(ae);

            	    }
            	    break;

            	default :
            	    if ( cnt56 >= 1 ) break loop56;
                        EarlyExitException eee =
                            new EarlyExitException(56, input);
                        throw eee;
                }
                cnt56++;
            } while (true);


            match(input, Token.UP, null); 
            	element = factory.createFieldConstraint(aeList);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return element;
    }
    // $ANTLR end field_element


    // $ANTLR start accessor_element
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:398:1: accessor_element returns [BaseDescr element] : ^( VT_ACCESSOR_ELEMENT id= ID (sc+= VT_SQUARE_CHUNK )* ) ;
    public final BaseDescr accessor_element() throws RecognitionException {
        BaseDescr element = null;

        DroolsTree id=null;
        DroolsTree sc=null;
        List list_sc=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:399:2: ( ^( VT_ACCESSOR_ELEMENT id= ID (sc+= VT_SQUARE_CHUNK )* ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:399:4: ^( VT_ACCESSOR_ELEMENT id= ID (sc+= VT_SQUARE_CHUNK )* )
            {
            match(input,VT_ACCESSOR_ELEMENT,FOLLOW_VT_ACCESSOR_ELEMENT_in_accessor_element2204); 

            match(input, Token.DOWN, null); 
            id=(DroolsTree)input.LT(1);
            match(input,ID,FOLLOW_ID_in_accessor_element2208); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:399:34: (sc+= VT_SQUARE_CHUNK )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==VT_SQUARE_CHUNK) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:399:34: sc+= VT_SQUARE_CHUNK
            	    {
            	    sc=(DroolsTree)input.LT(1);
            	    match(input,VT_SQUARE_CHUNK,FOLLOW_VT_SQUARE_CHUNK_in_accessor_element2212); 
            	    if (list_sc==null) list_sc=new ArrayList();
            	    list_sc.add(sc);


            	    }
            	    break;

            	default :
            	    break loop57;
                }
            } while (true);


            match(input, Token.UP, null); 
            	element = factory.createAccessorElement(id, list_sc);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return element;
    }
    // $ANTLR end accessor_element


    // $ANTLR start pattern_type
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:403:1: pattern_type returns [BaseDescr dataType] : ^( VT_PATTERN_TYPE (idList+= ID )+ ( LEFT_SQUARE rightList+= RIGHT_SQUARE )* ) ;
    public final BaseDescr pattern_type() throws RecognitionException {
        BaseDescr dataType = null;

        DroolsTree idList=null;
        DroolsTree rightList=null;
        List list_idList=null;
        List list_rightList=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:404:2: ( ^( VT_PATTERN_TYPE (idList+= ID )+ ( LEFT_SQUARE rightList+= RIGHT_SQUARE )* ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:404:4: ^( VT_PATTERN_TYPE (idList+= ID )+ ( LEFT_SQUARE rightList+= RIGHT_SQUARE )* )
            {
            match(input,VT_PATTERN_TYPE,FOLLOW_VT_PATTERN_TYPE_in_pattern_type2233); 

            match(input, Token.DOWN, null); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:404:28: (idList+= ID )+
            int cnt58=0;
            loop58:
            do {
                int alt58=2;
                int LA58_0 = input.LA(1);

                if ( (LA58_0==ID) ) {
                    alt58=1;
                }


                switch (alt58) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:404:28: idList+= ID
            	    {
            	    idList=(DroolsTree)input.LT(1);
            	    match(input,ID,FOLLOW_ID_in_pattern_type2237); 
            	    if (list_idList==null) list_idList=new ArrayList();
            	    list_idList.add(idList);


            	    }
            	    break;

            	default :
            	    if ( cnt58 >= 1 ) break loop58;
                        EarlyExitException eee =
                            new EarlyExitException(58, input);
                        throw eee;
                }
                cnt58++;
            } while (true);

            // src/main/resources/org/drools/lang/DescrBuilderTree.g:404:34: ( LEFT_SQUARE rightList+= RIGHT_SQUARE )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==LEFT_SQUARE) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:404:35: LEFT_SQUARE rightList+= RIGHT_SQUARE
            	    {
            	    match(input,LEFT_SQUARE,FOLLOW_LEFT_SQUARE_in_pattern_type2241); 
            	    rightList=(DroolsTree)input.LT(1);
            	    match(input,RIGHT_SQUARE,FOLLOW_RIGHT_SQUARE_in_pattern_type2245); 
            	    if (list_rightList==null) list_rightList=new ArrayList();
            	    list_rightList.add(rightList);


            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);


            match(input, Token.UP, null); 
            	dataType = factory.createDataType(list_idList, list_rightList);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return dataType;
    }
    // $ANTLR end pattern_type


    // $ANTLR start data_type
    // src/main/resources/org/drools/lang/DescrBuilderTree.g:408:1: data_type returns [BaseDescr dataType] : ^( VT_DATA_TYPE (idList+= ID )+ ( LEFT_SQUARE rightList+= RIGHT_SQUARE )* ) ;
    public final BaseDescr data_type() throws RecognitionException {
        BaseDescr dataType = null;

        DroolsTree idList=null;
        DroolsTree rightList=null;
        List list_idList=null;
        List list_rightList=null;

        try {
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:409:2: ( ^( VT_DATA_TYPE (idList+= ID )+ ( LEFT_SQUARE rightList+= RIGHT_SQUARE )* ) )
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:409:4: ^( VT_DATA_TYPE (idList+= ID )+ ( LEFT_SQUARE rightList+= RIGHT_SQUARE )* )
            {
            match(input,VT_DATA_TYPE,FOLLOW_VT_DATA_TYPE_in_data_type2267); 

            match(input, Token.DOWN, null); 
            // src/main/resources/org/drools/lang/DescrBuilderTree.g:409:25: (idList+= ID )+
            int cnt60=0;
            loop60:
            do {
                int alt60=2;
                int LA60_0 = input.LA(1);

                if ( (LA60_0==ID) ) {
                    alt60=1;
                }


                switch (alt60) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:409:25: idList+= ID
            	    {
            	    idList=(DroolsTree)input.LT(1);
            	    match(input,ID,FOLLOW_ID_in_data_type2271); 
            	    if (list_idList==null) list_idList=new ArrayList();
            	    list_idList.add(idList);


            	    }
            	    break;

            	default :
            	    if ( cnt60 >= 1 ) break loop60;
                        EarlyExitException eee =
                            new EarlyExitException(60, input);
                        throw eee;
                }
                cnt60++;
            } while (true);

            // src/main/resources/org/drools/lang/DescrBuilderTree.g:409:31: ( LEFT_SQUARE rightList+= RIGHT_SQUARE )*
            loop61:
            do {
                int alt61=2;
                int LA61_0 = input.LA(1);

                if ( (LA61_0==LEFT_SQUARE) ) {
                    alt61=1;
                }


                switch (alt61) {
            	case 1 :
            	    // src/main/resources/org/drools/lang/DescrBuilderTree.g:409:32: LEFT_SQUARE rightList+= RIGHT_SQUARE
            	    {
            	    match(input,LEFT_SQUARE,FOLLOW_LEFT_SQUARE_in_data_type2275); 
            	    rightList=(DroolsTree)input.LT(1);
            	    match(input,RIGHT_SQUARE,FOLLOW_RIGHT_SQUARE_in_data_type2279); 
            	    if (list_rightList==null) list_rightList=new ArrayList();
            	    list_rightList.add(rightList);


            	    }
            	    break;

            	default :
            	    break loop61;
                }
            } while (true);


            match(input, Token.UP, null); 
            	dataType = factory.createDataType(list_idList, list_rightList);	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return dataType;
    }
    // $ANTLR end data_type


 

    public static final BitSet FOLLOW_VT_COMPILATION_UNIT_in_compilation_unit49 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_package_statement_in_compilation_unit51 = new BitSet(new long[]{0xEDFFE00000000028L,0x0000000000000003L});
    public static final BitSet FOLLOW_statement_in_compilation_unit53 = new BitSet(new long[]{0xEDFFE00000000028L,0x0000000000000003L});
    public static final BitSet FOLLOW_VK_PACKAGE_in_package_statement71 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_package_id_in_package_statement75 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_PACKAGE_ID_in_package_id102 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_package_id106 = new BitSet(new long[]{0x0000000000000008L,0x0000000000080000L});
    public static final BitSet FOLLOW_rule_attribute_in_statement124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_import_statement_in_statement134 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_import_statement_in_statement144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_global_in_statement155 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_statement165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_template_in_statement175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule_in_statement185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_query_in_statement195 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_declaration_in_statement205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VK_IMPORT_in_import_statement226 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_import_name_in_import_statement230 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_FUNCTION_IMPORT_in_function_import_statement252 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VK_FUNCTION_in_function_import_statement254 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_import_name_in_function_import_statement258 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_IMPORT_ID_in_import_name277 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_import_name281 = new BitSet(new long[]{0x0000000000000008L,0x0000000000280000L});
    public static final BitSet FOLLOW_DOT_STAR_in_import_name286 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_GLOBAL_in_global309 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_data_type_in_global313 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_VT_GLOBAL_ID_in_global317 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_FUNCTION_in_function339 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_data_type_in_function343 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_VT_FUNCTION_ID_in_function348 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_parameters_in_function352 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_VT_CURLY_CHUNK_in_function356 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_TEMPLATE_in_template381 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_TEMPLATE_ID_in_template385 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_template_slot_in_template394 = new BitSet(new long[]{0x0000000000008000L,0x0000000000400000L});
    public static final BitSet FOLLOW_END_in_template402 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_SLOT_in_template_slot422 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_data_type_in_template_slot426 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_VT_SLOT_ID_in_template_slot430 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_QUERY_in_query452 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_QUERY_ID_in_query456 = new BitSet(new long[]{0x0000100000400000L});
    public static final BitSet FOLLOW_parameters_in_query460 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_lhs_block_in_query465 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_END_in_query469 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_RULE_in_rule496 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_RULE_ID_in_rule500 = new BitSet(new long[]{0x0000000000030000L,0x0000000048000000L});
    public static final BitSet FOLLOW_rule_attributes_in_rule504 = new BitSet(new long[]{0x0000000000020000L,0x0000000048000000L});
    public static final BitSet FOLLOW_decl_metadata_in_rule513 = new BitSet(new long[]{0x0000000000020000L,0x0000000048000000L});
    public static final BitSet FOLLOW_when_part_in_rule525 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_VT_RHS_CHUNK_in_rule530 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHEN_in_when_part549 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_lhs_block_in_when_part553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VT_RULE_ATTRIBUTES_in_rule_attributes575 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VK_ATTRIBUTES_in_rule_attributes577 = new BitSet(new long[]{0x01FFE00000000000L});
    public static final BitSet FOLLOW_rule_attribute_in_rule_attributes583 = new BitSet(new long[]{0x01FFE00000000008L});
    public static final BitSet FOLLOW_VT_PARAM_LIST_in_parameters607 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_param_definition_in_parameters612 = new BitSet(new long[]{0x0000004000000008L,0x0000000000080000L});
    public static final BitSet FOLLOW_data_type_in_param_definition634 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_argument_in_param_definition639 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_argument659 = new BitSet(new long[]{0x0000000000000002L,0x0004000000000000L});
    public static final BitSet FOLLOW_LEFT_SQUARE_in_argument662 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RIGHT_SQUARE_in_argument666 = new BitSet(new long[]{0x0000000000000002L,0x0004000000000000L});
    public static final BitSet FOLLOW_VK_DECLARE_in_type_declaration692 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_TYPE_DECLARE_ID_in_type_declaration696 = new BitSet(new long[]{0x0000000000000000L,0x0000000008480000L});
    public static final BitSet FOLLOW_decl_metadata_in_type_declaration705 = new BitSet(new long[]{0x0000000000000000L,0x0000000008480000L});
    public static final BitSet FOLLOW_decl_field_in_type_declaration718 = new BitSet(new long[]{0x0000000000000000L,0x0000000000480000L});
    public static final BitSet FOLLOW_END_in_type_declaration724 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AT_in_decl_metadata749 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_decl_metadata753 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_decl_metadata757 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ID_in_decl_field784 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_decl_field_initialization_in_decl_field788 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_data_type_in_decl_field793 = new BitSet(new long[]{0x0000000000000008L,0x0000000008000000L});
    public static final BitSet FOLLOW_decl_metadata_in_decl_field798 = new BitSet(new long[]{0x0000000000000008L,0x0000000008000000L});
    public static final BitSet FOLLOW_EQUALS_in_decl_field_initialization825 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_decl_field_initialization829 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_SALIENCE_in_rule_attribute852 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_rule_attribute857 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_rule_attribute861 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_NO_LOOP_in_rule_attribute872 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_BOOL_in_rule_attribute876 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_AGENDA_GROUP_in_rule_attribute888 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_STRING_in_rule_attribute892 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_DURATION_in_rule_attribute903 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INT_in_rule_attribute907 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_ACTIVATION_GROUP_in_rule_attribute919 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_STRING_in_rule_attribute923 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_AUTO_FOCUS_in_rule_attribute933 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_BOOL_in_rule_attribute937 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_DATE_EFFECTIVE_in_rule_attribute948 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_STRING_in_rule_attribute952 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_DATE_EXPIRES_in_rule_attribute962 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_STRING_in_rule_attribute966 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_ENABLED_in_rule_attribute976 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_BOOL_in_rule_attribute981 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_rule_attribute985 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_RULEFLOW_GROUP_in_rule_attribute996 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_STRING_in_rule_attribute1000 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_LOCK_ON_ACTIVE_in_rule_attribute1010 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_BOOL_in_rule_attribute1014 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_DIALECT_in_rule_attribute1024 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_STRING_in_rule_attribute1028 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_AND_IMPLICIT_in_lhs_block1053 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_lhs_in_lhs_block1058 = new BitSet(new long[]{0x0000000087800008L,0x0000000800006204L});
    public static final BitSet FOLLOW_VT_OR_PREFIX_in_lhs1084 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_lhs_in_lhs1089 = new BitSet(new long[]{0x0000000087800008L,0x0000000800006204L});
    public static final BitSet FOLLOW_VT_OR_INFIX_in_lhs1105 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_lhs_in_lhs1109 = new BitSet(new long[]{0x0000000087800000L,0x0000000800006204L});
    public static final BitSet FOLLOW_lhs_in_lhs1113 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_AND_PREFIX_in_lhs1125 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_lhs_in_lhs1130 = new BitSet(new long[]{0x0000000087800008L,0x0000000800006204L});
    public static final BitSet FOLLOW_VT_AND_INFIX_in_lhs1146 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_lhs_in_lhs1150 = new BitSet(new long[]{0x0000000087800000L,0x0000000800006204L});
    public static final BitSet FOLLOW_lhs_in_lhs1154 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_EXISTS_in_lhs1166 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_lhs_in_lhs1170 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_NOT_in_lhs1182 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_lhs_in_lhs1186 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_EVAL_in_lhs1198 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_lhs1202 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_FORALL_in_lhs1214 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_lhs_in_lhs1219 = new BitSet(new long[]{0x0000000087800008L,0x0000000800006204L});
    public static final BitSet FOLLOW_FROM_in_lhs1233 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_lhs_pattern_in_lhs1237 = new BitSet(new long[]{0x0000000020000000L,0x000000A000000100L});
    public static final BitSet FOLLOW_from_elements_in_lhs1241 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_lhs_pattern_in_lhs1252 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ACCUMULATE_in_from_elements1273 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_lhs_in_from_elements1277 = new BitSet(new long[]{0x0000000018000000L});
    public static final BitSet FOLLOW_accumulate_parts_in_from_elements1287 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_COLLECT_in_from_elements1300 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_lhs_in_from_elements1304 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_ENTRY_POINT_in_from_elements1316 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_ENTRYPOINT_ID_in_from_elements1320 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_from_source_clause_in_from_elements1331 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_accumulate_init_clause_in_accumulate_parts1352 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_accumulate_id_clause_in_accumulate_parts1363 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VT_ACCUMULATE_INIT_CLAUSE_in_accumulate_init_clause1386 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_INIT_in_accumulate_init_clause1395 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_accumulate_init_clause1399 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_ACTION_in_accumulate_init_clause1407 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_accumulate_init_clause1411 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_accumulate_init_reverse_clause_in_accumulate_init_clause1420 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_VK_RESULT_in_accumulate_init_clause1427 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_accumulate_init_clause1431 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_REVERSE_in_accumulate_init_reverse_clause1454 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_accumulate_init_reverse_clause1458 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_ACCUMULATE_ID_CLAUSE_in_accumulate_id_clause1480 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_accumulate_id_clause1484 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_accumulate_id_clause1488 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_FROM_SOURCE_in_from_source_clause1510 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_from_source_clause1514 = new BitSet(new long[]{0x0000000040100008L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_from_source_clause1518 = new BitSet(new long[]{0x0000000040000008L});
    public static final BitSet FOLLOW_expression_chain_in_from_source_clause1527 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_EXPRESSION_CHAIN_in_expression_chain1546 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_expression_chain1550 = new BitSet(new long[]{0x0000000040180008L});
    public static final BitSet FOLLOW_VT_SQUARE_CHUNK_in_expression_chain1554 = new BitSet(new long[]{0x0000000040100008L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_expression_chain1559 = new BitSet(new long[]{0x0000000040000008L});
    public static final BitSet FOLLOW_expression_chain_in_expression_chain1567 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_PATTERN_in_lhs_pattern1585 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_fact_expression_in_lhs_pattern1589 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_over_clause_in_lhs_pattern1594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OVER_in_over_clause1619 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_over_element_in_over_clause1624 = new BitSet(new long[]{0x0000000000200008L});
    public static final BitSet FOLLOW_VT_BEHAVIOR_in_over_element1645 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_over_element1647 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_ID_in_over_element1651 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_over_element1655 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_FACT_in_fact_expression1678 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_pattern_type_in_fact_expression1682 = new BitSet(new long[]{0x0000001F00100048L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1687 = new BitSet(new long[]{0x0000001F00100048L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_VT_FACT_BINDING_in_fact_expression1701 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_LABEL_in_fact_expression1705 = new BitSet(new long[]{0x0000001F00100040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1709 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_FACT_OR_in_fact_expression1721 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1725 = new BitSet(new long[]{0x0000001F00100040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1729 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_FIELD_in_fact_expression1740 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_field_element_in_fact_expression1744 = new BitSet(new long[]{0x0000001F00100048L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1748 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_BIND_FIELD_in_fact_expression1759 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_LABEL_in_fact_expression1763 = new BitSet(new long[]{0x0000001F00100040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1767 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_EVAL_in_fact_expression1778 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_fact_expression1782 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EQUAL_in_fact_expression1795 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1799 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_EQUAL_in_fact_expression1811 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1815 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GREATER_in_fact_expression1827 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1831 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GREATER_EQUAL_in_fact_expression1843 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1847 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LESS_in_fact_expression1859 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1863 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LESS_EQUAL_in_fact_expression1875 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1879 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_CONTAINS_in_fact_expression1891 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VK_NOT_in_fact_expression1895 = new BitSet(new long[]{0x0000001F00100040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1900 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_EXCLUDES_in_fact_expression1912 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VK_NOT_in_fact_expression1916 = new BitSet(new long[]{0x0000001F00100040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1921 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_MATCHES_in_fact_expression1933 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VK_NOT_in_fact_expression1937 = new BitSet(new long[]{0x0000001F00100040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1942 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_SOUNDSLIKE_in_fact_expression1954 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VK_NOT_in_fact_expression1958 = new BitSet(new long[]{0x0000001F00100040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1963 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_MEMBEROF_in_fact_expression1975 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VK_NOT_in_fact_expression1979 = new BitSet(new long[]{0x0000001F00100040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression1984 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ID_in_fact_expression1996 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VK_NOT_in_fact_expression2000 = new BitSet(new long[]{0x0000001F00180040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_VT_SQUARE_CHUNK_in_fact_expression2005 = new BitSet(new long[]{0x0000001F00100040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression2010 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VK_IN_in_fact_expression2021 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VK_NOT_in_fact_expression2025 = new BitSet(new long[]{0x0000001F00100040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression2031 = new BitSet(new long[]{0x0000001F00100048L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_DOUBLE_PIPE_in_fact_expression2046 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression2050 = new BitSet(new long[]{0x0000001F00100040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression2054 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DOUBLE_AMPER_in_fact_expression2064 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression2068 = new BitSet(new long[]{0x0000001F00100040L,0x00037E07808804FCL});
    public static final BitSet FOLLOW_fact_expression_in_fact_expression2072 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VT_ACCESSOR_PATH_in_fact_expression2083 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_accessor_element_in_fact_expression2088 = new BitSet(new long[]{0x0000002000000008L});
    public static final BitSet FOLLOW_STRING_in_fact_expression2103 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_fact_expression2113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_fact_expression2123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BOOL_in_fact_expression2133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NULL_in_fact_expression2143 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VT_PAREN_CHUNK_in_fact_expression2153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VT_ACCESSOR_PATH_in_field_element2175 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_accessor_element_in_field_element2180 = new BitSet(new long[]{0x0000002000000008L});
    public static final BitSet FOLLOW_VT_ACCESSOR_ELEMENT_in_accessor_element2204 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_accessor_element2208 = new BitSet(new long[]{0x0000000000080008L});
    public static final BitSet FOLLOW_VT_SQUARE_CHUNK_in_accessor_element2212 = new BitSet(new long[]{0x0000000000080008L});
    public static final BitSet FOLLOW_VT_PATTERN_TYPE_in_pattern_type2233 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_pattern_type2237 = new BitSet(new long[]{0x0000000000000008L,0x0004000000080000L});
    public static final BitSet FOLLOW_LEFT_SQUARE_in_pattern_type2241 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RIGHT_SQUARE_in_pattern_type2245 = new BitSet(new long[]{0x0000000000000008L,0x0004000000000000L});
    public static final BitSet FOLLOW_VT_DATA_TYPE_in_data_type2267 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_data_type2271 = new BitSet(new long[]{0x0000000000000008L,0x0004000000080000L});
    public static final BitSet FOLLOW_LEFT_SQUARE_in_data_type2275 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_RIGHT_SQUARE_in_data_type2279 = new BitSet(new long[]{0x0000000000000008L,0x0004000000000000L});

}