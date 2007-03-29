package org.drools.brms.client.modeldriven.brxml;

/**
 * This is used to specify that the bound fact should be retracted
 * when the rule fires.
 * @author Michael Neale
 *
 */
public class ActionRetractFact
    implements
    IAction {
    
    public ActionRetractFact() {}
    
    public ActionRetractFact(String var) {
        this.variableName = var;
    }
    
    public String variableName;

}
