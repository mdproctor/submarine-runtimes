/*
 * Copyright 2010 JBoss Inc
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

package org.drools.benchmark.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class SubTransaction extends Security {

    private static final long serialVersionUID = 510l;

    protected String[]  transactionSet = null;
    protected ArrayList<PropertyChangeListener> listeners      = new ArrayList<PropertyChangeListener>();

    public SubTransaction() {
        super();
    }

    public void setTransactionSet(String[] ids) {
        if ( ids != this.transactionSet ) {
            String[] old = this.transactionSet;
            this.transactionSet = ids;
            this.notifyListener( "transactionSet",
                                 old,
                                 this.transactionSet );
        }
    }

    public String[] getTransactionSet() {
        return this.transactionSet;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.add( listener );
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.listeners.remove( listener );
    }

    protected void notifyListener(String field,
                                  Object oldValue,
                                  Object newValue) {
        if ( listeners == null || listeners.size() == 0 ) {
            return;
        } else {
            PropertyChangeEvent event = new PropertyChangeEvent( this,
                                                                 field,
                                                                 oldValue,
                                                                 newValue );

            for ( int i = 0; i < listeners.size(); i++ ) {
                ((java.beans.PropertyChangeListener) listeners.get( i )).propertyChange( event );
            }
        }

    }
}
