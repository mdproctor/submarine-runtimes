package org.drools;

/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This is a utility to create rule bases based on the type of engine you wish to use.
 * 
 * @author Michael Neale
 */
public class RuleBaseFactory {
      
    private static RuleBaseFactory INSTANCE = new RuleBaseFactory();
    
    private RuleBaseFactory() {
    }
    
    public static RuleBaseFactory getInstance() {
        return INSTANCE;
    }
    
    /** Create a new default rule base (RETEOO type engine) */
    public RuleBase newRuleBase() {
        return newRuleBase(RuleBase.RETEOO);
    }

    /** Create a new RuleBase of the appropriate type */
    public RuleBase newRuleBase(int type) {
        switch ( type ) {
            case RuleBase.RETEOO :
                return new org.drools.reteoo.RuleBaseImpl();
            case RuleBase.LEAPS :
                try {
                    return new org.drools.leaps.RuleBaseImpl();
                } catch ( PackageIntegrationException e ) {
                    throw new IllegalStateException("Unable to create Leaps engine. Error: " + e.getMessage());
                }
            default :
                throw new IllegalArgumentException("Unknown engine type: " + type);
                
        }
    }
    
    
    
    
    
}
