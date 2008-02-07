package org.drools.base.mvel;

import java.io.Serializable;

import org.drools.WorkingMemory;
import org.drools.common.InternalRuleBase;
import org.drools.rule.MVELDialectData;
import org.drools.rule.Package;
import org.drools.spi.Consequence;
import org.drools.spi.KnowledgeHelper;
import org.mvel.compiler.CompiledExpression;
import org.mvel.MVEL;
import org.mvel.debug.DebugTools;

public class MVELConsequence
    implements
    Consequence,
    Serializable {
    private static final long       serialVersionUID = 400L;

    private final Serializable      expr;
    private final DroolsMVELFactory prototype;

    public MVELConsequence(final Serializable expr,
                           final DroolsMVELFactory factory) {
        this.expr = expr;
        this.prototype = factory;
    }

    public void evaluate(final KnowledgeHelper knowledgeHelper,
                         final WorkingMemory workingMemory) throws Exception {
        DroolsMVELFactory factory = (DroolsMVELFactory) this.prototype.clone();
        
        factory.setContext( knowledgeHelper.getTuple(),
                            knowledgeHelper,
                            null,
                            workingMemory,
                            null );
        
        Package pkg = workingMemory.getRuleBase().getPackage( "MAIN" );
        if ( pkg != null ) {
            MVELDialectData data = ( MVELDialectData ) pkg.getDialectDatas().getDialectData( "mvel" );
            factory.setNextFactory( data.getFunctionFactory() );
        }
        
        CompiledExpression compexpr = (CompiledExpression) this.expr;

        //Receive breakpoints from debugger
        MVELDebugHandler.prepare();

        if ( MVELDebugHandler.isDebugMode() ) {
            if ( MVELDebugHandler.verbose ) {
                System.out.println( DebugTools.decompile( compexpr ) );
            }
            MVEL.executeDebugger( compexpr,
                                  null,
                                  factory );
        } else {
            MVEL.executeExpression( compexpr,
                                    null,
                                    factory );
        }

    }

    public Serializable getCompExpr() {
        return expr;
    }

}
