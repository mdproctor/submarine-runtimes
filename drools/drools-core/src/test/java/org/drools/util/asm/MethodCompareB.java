package org.drools.util.asm;

public class MethodCompareB {

    public boolean evaluate(String foox) {
        if (foox == null || foox.startsWith( "42" )) {
            
            
            return true;
        } else {
            return false;
        }
    }
    
}
