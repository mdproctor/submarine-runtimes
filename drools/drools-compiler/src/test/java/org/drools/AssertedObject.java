package org.drools;

public class AssertedObject {
    private String value;

    public String getValue() {
        return this.value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public AssertedObject(final String value) {
        this.value = value;
    }

    public AssertedObject() {
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }
    
    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        final AssertedObject other = (AssertedObject) obj;
        if ( value == null ) {
            if ( other.value != null ) return false;
        } else if ( !value.equals( other.value ) ) return false;
        return true;
    }
    
    

}