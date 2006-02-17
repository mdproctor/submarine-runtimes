package org.drools.lang.descr;

public class PredicateDescr extends PatternDescr {
    private final String fieldName;
    private final String text;
    
    private final String declaration;
    private String[] declarations;
        
    public PredicateDescr(String fieldName,
                          String declaration,
                          String text) {
        this.fieldName = fieldName;
        this.declaration = declaration;
        this.text = text;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public String getText() {
        return this.text;
    } 
    
    public String getDeclaration() {
        return this.declaration;
    }
    
    public void setDeclarations( String[] declarations) {
        this.declarations = declarations;
    }
    
    public String[] getDeclarations() {
        return this.declarations;
    }
}
