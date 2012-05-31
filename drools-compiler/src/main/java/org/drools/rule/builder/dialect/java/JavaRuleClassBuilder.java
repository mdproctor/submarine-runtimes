/*
 * Copyright 2006 JBoss Inc
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

package org.drools.rule.builder.dialect.java;

import org.drools.core.util.StringUtils;
import org.drools.lang.descr.RuleDescr;
import org.drools.rule.builder.RuleBuildContext;
import org.drools.rule.builder.RuleClassBuilder;

public class JavaRuleClassBuilder
    implements
    RuleClassBuilder {

    /* (non-Javadoc)
     * @see org.drools.rule.builder.dialect.java.RuleClassBuilder#buildRule(org.drools.rule.builder.BuildContext, org.drools.rule.builder.dialect.java.BuildUtils, org.drools.lang.descr.RuleDescr)
     */
    public String  buildRule(final RuleBuildContext context) {
        // If there is no compiled code, return
        if ( context.getMethods().isEmpty() ) {
            return null;
        }
        
        final String lineSeparator = System.getProperty( "line.separator" );

        final StringBuilder buffer = new StringBuilder();
        buffer.append("package ").append(context.getPkg().getName()).append(";").append(lineSeparator);

        for (String s : context.getPkg().getImports().keySet()) {
            buffer.append("import ").append(s).append(";");
        }

        for (String s : context.getPkg().getStaticImports()) {
            buffer.append("import static ").append(s).append(";");
        }
        
        buffer.append( lineSeparator );

        final RuleDescr ruleDescr = context.getRuleDescr();
        
        buffer.append("public class ").append(StringUtils.ucFirst(ruleDescr.getClassName())).append(" {").append(lineSeparator);
        buffer.append("    private static final long serialVersionUID = 510l;").append(lineSeparator);

        for ( int i = 0, size = context.getMethods().size() - 1; i < size; i++ ) {
            buffer.append(context.getMethods().get(i)).append(lineSeparator);
        }

        final String[] lines = buffer.toString().split( lineSeparator, -1 );

        ruleDescr.setConsequenceOffset( lines.length );

        buffer.append(context.getMethods().get(context.getMethods().size() - 1)).append(lineSeparator);
        buffer.append( "}" );

        return buffer.toString();
    }
}
