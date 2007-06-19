package org.drools.rule.builder.dialect.mvel;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.antlr.runtime.RecognitionException;
import org.mvel.ExpressionCompiler;

/**
 * Expression analyzer.
 * 
 */
public class MVELExprAnalyzer {
    
    public MVELExprAnalyzer() {
        // intentionally left blank.
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Analyze an expression.
     * 
     * @param expr
     *            The expression to analyze.
     * @param availDecls
     *            Total set of declarations available.
     * 
     * @return The <code>Set</code> of declarations used by the expression.
     * @throws RecognitionException 
     *             If an error occurs in the parser.
     */
    public MVELAnalysisResult analyzeExpression(final String expr,
                                    final Set[] availableIdentifiers) throws RecognitionException {
        ExpressionCompiler compiler = new ExpressionCompiler( expr);
        compiler.compile();  
        
        return analyze( compiler.getInputs(),
                        availableIdentifiers );
    }

    /**
     * Analyze an expression.
     * 
     * @param availDecls
     *            Total set of declarations available.
     * @param ast
     *            The AST for the expression.
     * 
     * @return The <code>Set</code> of declarations used by the expression.
     * 
     * @throws RecognitionException
     *             If an error occurs in the parser.
     */
    private MVELAnalysisResult analyze(final Set identifiers,
                           final Set[] availableIdentifiers) throws RecognitionException {

        MVELAnalysisResult result = new MVELAnalysisResult();
        result.setIdentifiers( new ArrayList( identifiers ) );

        final Set notBound = new HashSet( identifiers );
        final List[] used = new List[availableIdentifiers.length];
        for ( int i = 0, length = used.length; i < length; i++ ) {
            used[i] = new ArrayList();
        }

        for ( int i = 0, length = availableIdentifiers.length; i < length; i++ ) {
            final Set set = availableIdentifiers[i];
            for ( final Iterator it = set.iterator(); it.hasNext(); ) {
                final String eachDecl = (String) it.next();
                if ( identifiers.contains( eachDecl ) ) {
                    used[i].add( eachDecl );
                    notBound.remove( eachDecl );
                }
            }
        }
        result.setBoundIdentifiers( used );
        result.setNotBoundedIdentifiers( new ArrayList( notBound ) );

        return result;      
    }
}