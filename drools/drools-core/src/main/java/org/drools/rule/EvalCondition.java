package org.drools.rule;

import org.drools.WorkingMemory;
import org.drools.spi.Constraint;
import org.drools.spi.EvalExpression;
import org.drools.spi.Tuple;

public class EvalCondition extends ConditionalElement {    
    private EvalExpression           expression;    
   
    private final Declaration[]            requiredDeclarations;

    private static final Declaration[]     EMPTY_DECLARATIONS = new Declaration[0];

    public EvalCondition(Declaration[] requiredDeclarations) {
       this( null, requiredDeclarations );
   }
    
    public EvalCondition(EvalExpression eval,
                          Declaration[] requiredDeclarations) {
        
        this.expression = eval;
        

        if ( requiredDeclarations == null ) {
            this.requiredDeclarations = EvalCondition.EMPTY_DECLARATIONS;
        } else {
            this.requiredDeclarations = requiredDeclarations;
        }
    }
    
    public void setEvalExpression(EvalExpression expression) {
        this.expression = expression;
    }

    public Declaration[] getRequiredDeclarations() {
        return this.requiredDeclarations;
    }

    public boolean isAllowed(Tuple tuple,
                             WorkingMemory workingMemory) {

        return expression.evaluate( tuple, 
                              this.requiredDeclarations, 
                             workingMemory);

    }

};
