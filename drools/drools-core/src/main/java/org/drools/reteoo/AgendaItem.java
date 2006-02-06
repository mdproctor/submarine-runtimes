package org.drools.reteoo;
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


import java.io.Serializable;

import org.drools.FactHandle;
import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.PropagationContext;
import org.drools.spi.Tuple;
import org.drools.util.AbstractBaseLinkedListNode;

/**
 * Item entry in the <code>Agenda</code>.
 * 
 * @author <a href="mailto:mark.proctor@jboss.com">Mark Proctor</a>
 * @author <a href="mailto:bob@werken.com">Bob McWhirter</a>
 */
class AgendaItem extends AbstractBaseLinkedListNode
    implements
    Activation,
    Serializable {
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** The tuple. */
    private final ReteTuple          tuple;

    /** The rule. */
    private final Rule               rule;

    /** The propagation context */
    private final PropagationContext context;

    /** The activation number */
    private final long               activationNumber;    

    /** A reference to the ActivatinQeue the item is on */
    private final ActivationQueue          queue;
    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     * 
     * @param tuple
     *            The tuple.
     * @param rule
     *            The rule.
     */
    AgendaItem(long activationNumber,
               ReteTuple tuple,
               PropagationContext context,
               Rule rule,
               ActivationQueue queue) {
        this.tuple = tuple;
        this.context = context;
        this.rule = rule;
        this.activationNumber = activationNumber;
        this.queue = queue;
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------
    public PropagationContext getPropagationContext() {
        return this.context;
    }

    /**
     * Retrieve the rule.
     * 
     * @return The rule.
     */
    public Rule getRule() {
        return this.rule;
    }

    /**
     * Determine if this <code>Actiation</code>'s tuple depends on the given FactHandle
     * 
     * @param handle
     *            The root object handle.
     * 
     * @return <code>true<code> if this agenda item depends
     *          upon the item, otherwise <code>false</code>.
     */
    boolean dependsOn(FactHandle handle) {
        return this.tuple.dependsOn( handle );
    }

    /**
     * Retrieve the tuple.
     * 
     * @return The tuple.
     */
    public Tuple getTuple() {
        return this.tuple;
    }

    /**
     * Retrieve the <code>TupleKey</code>.
     * 
     * @return The key to the tuple in this item.
     */
    TupleKey getKey() {
        return this.tuple.getKey();
    }

    /* (non-Javadoc)
     * @see org.drools.spi.Activation#getActivationNumber()
     */
    public long getActivationNumber() {
        return this.activationNumber;
    }
    
    /* (non-Javadoc)
     * @see org.drools.spi.Activation#remove()
     */
    public void remove() {
        this.tuple.setActivation( null );        
        this.queue.remove( this );
    }    

    public String toString() {
        return "[Activation rule=" + this.rule.getName() + ", tuple=" + this.tuple + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object object) {
        if ( object == this ) {
            return true;
        }

        if ( (object == null) || !(object instanceof AgendaItem) ) {
            return false;
        }

        AgendaItem otherItem = (AgendaItem) object;

        return (this.rule.equals( otherItem.getRule() ) && this.tuple.getKey().equals( otherItem.getKey() ));
    }


    /**
     * Return the hashcode of the <code>TupleKey<code> as the hashcode of the AgendaItem
     * @return
     */
    public int hashcode() {
        return this.getKey().hashCode();
    }    
}
