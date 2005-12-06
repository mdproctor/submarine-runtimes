package org.drools.spi;

import org.drools.FactHandle;
import org.drools.rule.Declaration;

public interface BooleanExpressionConstraint
{
    
    public boolean isAllowed(Object object,
                             FactHandle handle,
                             Declaration declaration,
                             Declaration[] declarations,
                             Tuple tuple);
}
