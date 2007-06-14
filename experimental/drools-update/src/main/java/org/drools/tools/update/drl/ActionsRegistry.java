/*
 * Copyright 2006 JBoss Inc
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
 *
 * Created on Jun 13, 2007
 */
package org.drools.tools.update.drl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A registry class for all actions
 * 
 * @author etirelli
 */
public class ActionsRegistry {
    
    private Map<Class, List> actionMap;
    
    public ActionsRegistry() {
        this.actionMap = new HashMap<Class, List>();
    }
    
    public void addAction( Class target, UpdateAction action ) {
        List actions = this.actionMap.get( target );
        if( actions == null ) {
            actions = new LinkedList<UpdateAction>();
            this.actionMap.put( target, actions );
        }
        actions.add( action );
    }
    
    public List<UpdateAction> getActionsForClass( Class target ) {
        List actions = this.actionMap.get( target );
        if( actions == null ) {
            actions = Collections.EMPTY_LIST;
        }
        return actions;
    }

}
