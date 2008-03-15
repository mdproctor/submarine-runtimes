package org.drools.base.mvel;

import org.drools.WorkingMemory;
import org.drools.rule.MVELDialectData;
import org.drools.rule.Package;
import org.drools.spi.Consequence;
import org.drools.spi.KnowledgeHelper;
import org.mvel.MVEL;
import org.mvel.compiler.CompiledExpression;
import org.mvel.debug.DebugTools;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

public class MVELConsequence
    implements
    Consequence,
    Externalizable {
    private static final long       serialVersionUID = 400L;

    private Serializable      expr;
    private DroolsMVELFactory prototype;

    public MVELConsequence() {
    }

    public MVELConsequence(final Serializable expr,
                           final DroolsMVELFactory factory) {
        this.expr = expr;
        this.prototype = factory;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        expr    = (Serializable)in.readObject();
        prototype    = (DroolsMVELFactory)in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(expr);
        out.writeObject(prototype);
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
