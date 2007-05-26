package org.drools.rule.builder.dialect.mvel;

import java.io.Serializable;

import org.drools.base.mvel.DroolsMVELFactory;
import org.drools.base.mvel.MVELConsequence;
import org.drools.base.mvel.MVELSalienceExpression;
import org.drools.rule.builder.RuleBuildContext;
import org.drools.rule.builder.SalienceBuilder;
import org.mvel.MVEL;

public class MVELSalienceBuilder implements SalienceBuilder {

    public void build(RuleBuildContext context) {   
        // pushing consequence LHS into the stack for variable resolution
        context.getBuildStack().push( context.getRule().getLhs() );
        
        final DroolsMVELFactory factory = new DroolsMVELFactory(context.getDeclarationResolver().getDeclarations(), null, context.getPkg().getGlobals());

        final Serializable expr = MVEL.compileExpression( (String) context.getRuleDescr().getSalience() );

        MVELSalienceExpression salience = new MVELSalienceExpression(expr, factory);
        
        context.getRule().setSalience( salience );
    }

}
