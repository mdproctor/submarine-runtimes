package org.drools.clp;

import org.drools.base.SimpleValueType;


public class TempTokenVariable extends BaseValueHandler implements VariableValueHandler {

    private String identifier;
    
    public TempTokenVariable(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }
    
    public int getValueType(ExecutionContext context) {
        return SimpleValueType.UNKNOWN;
    }    

    public Object getValue(ExecutionContext context) {
        throw new UnsupportedOperationException( "TempTokenVariable Variable identifer='" + getIdentifier() + " cannot be read" );
    }

    public void setValue(ExecutionContext context, Object value) {
        throw new UnsupportedOperationException( "TempTokenVariable Variable identifer='" + getIdentifier() + " cannot be set" );
    }

    public void setContext(ExecutionContext context) {

    }

}
