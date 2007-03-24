package org.drools.clp;

public class Deffunction
    implements
    Function {
    private final String     name;
    private FunctionCaller[] functions;

    private ValueHandler[]   parameters;

    public Deffunction(String name) {
        this.name = name;
    }

    public void addParameter(ValueHandler parameter) {
        if ( parameters == null ) {
            this.parameters = new ValueHandler[]{parameter};
        } else {
            ValueHandler[] temp = new ValueHandler[parameters.length + 1];
            System.arraycopy( this.parameters,
                              0,
                              temp,
                              0,
                              this.parameters.length );
            temp[temp.length - 1] = parameter;
            this.parameters = temp;
        }
    }

    public void addFunction(FunctionCaller function) {
        if ( functions == null ) {
            this.functions = new FunctionCaller[]{function};
        } else {
            FunctionCaller[] temp = new FunctionCaller[functions.length + 1];
            System.arraycopy( this.functions,
                              0,
                              temp,
                              0,
                              this.functions.length );
            temp[temp.length - 1] = function;
            this.functions = temp;
        }
    }

    public ValueHandler[] getParameters() {
        return this.parameters;
    }

    public ValueHandler addParameterCallback(int index,
                                             ValueHandler valueHandler,
                                             ExecutionBuildContext context) {
        return valueHandler;
    }

    public void initCallback(ExecutionBuildContext context) {
    }

    public ValueHandler execute(ValueHandler[] args,
                          ExecutionContext context) {
        ExecutionContext newContext = initContext( args,
                                                   context );

        Object returnValue = null;

        for ( int i = 0, length = this.functions.length; i < length; i++ ) {
            returnValue = this.functions[i].getValue( newContext );
        }
        return new ObjectValueHandler( returnValue );
    }

    private ExecutionContext initContext(ValueHandler[] args,
                                         ExecutionContext context) {
        ExecutionContext newContext = new ExecutionContext( context.getWorkingMemory(),
                                                            context.getTuple(),
                                                            args.length );
        for ( int i = 0, length = args.length; i < length; i++ ) {
            // We know that each argument is a local variable, so we can cast and access the underlying value handler, 
            // as we don't want the variable fully resolved at this stage, just mapped.
            newContext.setLocalVariable( i,
                                         ((LocalVariableValue) args[i]).getRawValue( context ) );
        }
        return newContext;
    }

    public String getName() {
        return this.name;
    }

    public LispList createList(int index) {
        return new LispForm();
    }
}
