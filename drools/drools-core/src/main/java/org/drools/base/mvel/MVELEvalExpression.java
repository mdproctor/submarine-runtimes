package org.drools.base.mvel;

import java.io.Serializable;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.IOException;
import java.io.ObjectInput;

import org.drools.rule.Declaration;
import org.drools.rule.MVELDialectRuntimeData;
import org.drools.rule.Package;
import org.drools.WorkingMemory;
import org.drools.spi.EvalExpression;
import org.drools.spi.Tuple;
import org.mvel.MVEL;

public class MVELEvalExpression
    implements
    EvalExpression,
    MVELCompileable,
    Externalizable  {


    private static final long       serialVersionUID = 400L;

    private MVELCompilationUnit unit;
    private String id;
    
    private Serializable      expr;
    private DroolsMVELFactory prototype;


    public MVELEvalExpression() {
    }

    public MVELEvalExpression(final MVELCompilationUnit unit,
                              final String id) {
        this.unit = unit;
        this.id = id;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        id = in.readUTF();
        unit = ( MVELCompilationUnit ) in.readObject();
//        expr    = (Serializable)in.readObject();
//        prototype   = (DroolsMVELFactory)in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF( id );
        out.writeObject( unit );
//        out.writeObject(expr);
//        out.writeObject(prototype);
    }
    
    public void compile(ClassLoader classLoader) {
        expr = unit.getCompiledExpression( classLoader );
        prototype = unit.getFactory( );
    }    

    public Object createContext() {
        return this.prototype.clone();
    }

    public boolean evaluate(final Tuple tuple,
                            final Declaration[] requiredDeclarations,
                            final WorkingMemory workingMemory,
                            final Object context) throws Exception {
        DroolsMVELFactory factory = (DroolsMVELFactory) context;
        factory.setContext( tuple,
                                 null,
                                 null,
                                 workingMemory,
                                 null );

        // do we have any functions for this namespace?
        Package pkg = workingMemory.getRuleBase().getPackage( "MAIN" );
        if ( pkg != null ) {
            MVELDialectRuntimeData data = ( MVELDialectRuntimeData ) pkg.getDialectRuntimeRegistry().getDialectData( this.id );
            factory.setNextFactory( data.getFunctionFactory() );
        }

        final Boolean result = (Boolean) MVEL.executeExpression( this.expr,
                                                                 new Object(),
                                                                 factory );
        return result.booleanValue();
    }

}
