package org.drools.clp;

import org.drools.clp.valuehandlers.FunctionCaller;

public interface ExecutionEngine {
    public void addFunction(FunctionCaller function);

    public int getNextIndex();

    public FunctionCaller[] getFunctions();
}
