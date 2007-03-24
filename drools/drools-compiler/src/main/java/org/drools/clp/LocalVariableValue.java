package org.drools.clp;


public class LocalVariableValue extends BaseValueHandler implements VariableValueHandler {
    private static final long serialVersionUID = 320L;    

    /** The identifier for the variable. */
    private final String      identifier;    
    private final int         index;
    
    /**
     * Construct.
     * 
     * @param identifier
     *            The name of the variable.
     */
    public LocalVariableValue(final String identifier, final int index) {
        this.identifier = identifier;
        this.index = index;
    }
    
    /**
     * Retrieve the variable's identifier.
     * 
     * @return The variable's identifier.
     */
    public String getIdentifier() {
        return this.identifier;
    }
    
    public int getValueType(ExecutionContext context) {
        return context.getLocalVariable( this.index ).getValueType( context );
    }
    
    public Object getValue(ExecutionContext context) {
        Object object = context.getLocalVariable( this.index ).getValue( context );
        //if ( object instanceof )
        return object;
    }        
    
    public ValueHandler getRawValue(ExecutionContext context) {
        // this is a hack as some parts of the system need the ValueHandler and not the Value
        return context.getLocalVariable( this.index );
    }

    public void setValue(ExecutionContext context,
                         Object object) {
        context.setLocalVariable( this.index, (ValueHandler) object );        
    }        
    
    public String toString() {
        String name = getClass().getName();
        name = name.substring( name.lastIndexOf( "." ) + 1 );
        return "[" + name + " identifier = '" + getIdentifier()  + "']";
    }    
        
}

