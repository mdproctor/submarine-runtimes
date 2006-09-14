package org.drools.semantics.java;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.AngleBracketTemplateLexer;
import org.drools.RuntimeDroolsException;
import org.drools.lang.descr.FunctionDescr;
import org.drools.rule.LineMappings;
import org.drools.rule.Package;
import org.drools.spi.AvailableVariables;
import org.drools.spi.TypeResolver;

public class FunctionBuilder {
    private static final StringTemplateGroup functionGroup = new StringTemplateGroup( new InputStreamReader( FunctionBuilder.class.getResourceAsStream( "javaFunction.stg" ) ),
                                                                                      AngleBracketTemplateLexer.class );

    public FunctionBuilder() {

    }

    public String build(final Package pkg,
                        final FunctionDescr functionDescr,
                        final FunctionFixer fixer,
                        final TypeResolver typeResolver,
                        final Map lineMappings) {
        final StringTemplate st = FunctionBuilder.functionGroup.getInstanceOf( "function" );

        st.setAttribute( "package",
                         pkg.getName() );

        st.setAttribute( "imports",
                         pkg.getImports() );

        st.setAttribute( "className",
                         ucFirst( functionDescr.getName() ) );
        st.setAttribute( "methodName",
                         functionDescr.getName() );

        st.setAttribute( "returnType",
                         functionDescr.getReturnType() );

        st.setAttribute( "parameterTypes",
                         functionDescr.getParameterTypes() );

        st.setAttribute( "parameterNames",
                         functionDescr.getParameterNames() );

        Map params = new HashMap();
        List names = functionDescr.getParameterNames();
        List types = functionDescr.getParameterTypes();
        try {
            for ( int i = 0, size = names.size(); i < size; i++ ) {
                params.put( names.get( i ), typeResolver.resolveType( (String) types.get( i ) ) );
            }
        } catch ( ClassNotFoundException e ) {
            // todo : must be a better way so we don't have to try/catch each resolveType call
            throw new RuntimeDroolsException( e );                
        }                   
        
        st.setAttribute( "text",
                         fixer.fix( functionDescr.getText(), new AvailableVariables( new Map[] { params } ) ) );
        
        String text = st.toString();
        
        BufferedReader reader = new BufferedReader( new StringReader ( text ) );
        String line = null;
        String lineStartsWith = "    public static " + functionDescr.getReturnType( ) + " " + functionDescr.getName();
        int offset = 0;
        try {
            while ( ( line = reader.readLine() ) != null ) {
                offset++;
                if ( line.startsWith( lineStartsWith ) ) {
                    break;
                }
            }
            functionDescr.setOffset( offset );
        } catch ( IOException e ) {
            // won't ever happen, it's just reading over a string.
            throw new RuntimeDroolsException( "Error determining start offset with function" );
        }
        
        String name = pkg.getName() + "." + ucFirst( functionDescr.getName() );
        LineMappings mapping = new LineMappings( name );
        mapping.setStartLine( functionDescr.getLine() );
        mapping.setOffset( functionDescr.getOffset() );      
        lineMappings.put( name, lineMappings );

        return text;

    }

    private String ucFirst(final String name) {
        return name.toUpperCase().charAt( 0 ) + name.substring( 1 );
    }
}