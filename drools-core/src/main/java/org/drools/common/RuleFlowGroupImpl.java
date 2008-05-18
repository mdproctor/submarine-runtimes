package org.drools.common;

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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.drools.marshalling.PersisterEnums;
import org.drools.marshalling.MarshallerReaderContext;
import org.drools.marshalling.MarshallerWriteContext;
import org.drools.spi.Activation;
import org.drools.util.Iterator;
import org.drools.util.LinkedList;

/**
 * Implementation of a <code>RuleFlowGroup</code> that collects activations
 * of rules of this ruleflow-group.
 * If this group is activated, all its activations are added to the agenda.
 * As long as this group is active, its activations are added to the agenda.
 * Deactivating the group removes all its activations from the agenda and
 * collects them until it is activated again.
 * By default, <code>RuleFlowGroups</code> are automatically deactivated when there are no more
 * activations in the <code>RuleFlowGroup</code>.  However, this can be configured.
 *
 * @author <a href="mailto:mark.proctor@jboss.com">Mark Proctor</a>
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 *
 */
public class RuleFlowGroupImpl
    implements
    InternalRuleFlowGroup {

    private static final long           serialVersionUID = 400L;

    private InternalWorkingMemory       workingMemory;
    private String                      name;
    private boolean                     active           = false;
    private boolean                     autoDeactivate   = true;    
    private LinkedList                  list;
    private List<RuleFlowGroupListener> listeners;

    public RuleFlowGroupImpl() {

    }

    /**
     * Construct a <code>RuleFlowGroupImpl</code> with the given name.
     *
     * @param name
     *      The RuleFlowGroup name.
     */
    public RuleFlowGroupImpl(final String name) {
        this.name = name;
        this.list = new LinkedList();
    }
    
    public RuleFlowGroupImpl(final String name, final boolean active, final boolean autoDeactivate) {
        this.name = name;
        this.active = active;
        this.autoDeactivate = autoDeactivate;
        this.list = new LinkedList();
    }    

    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        workingMemory = (InternalWorkingMemory) in.readObject();
        name = (String) in.readObject();
        active = in.readBoolean();
        list = (LinkedList) in.readObject();
        autoDeactivate = in.readBoolean();
        listeners = (List<RuleFlowGroupListener>) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject( workingMemory );
        out.writeObject( name );
        out.writeBoolean( active );
        out.writeObject( list );
        out.writeBoolean( autoDeactivate );
        out.writeObject( listeners );
    }

    public String getName() {
        return this.name;
    }

    public void setWorkingMemory(InternalWorkingMemory workingMemory) {
        this.workingMemory = workingMemory;
    }

    public InternalWorkingMemory getWorkingMemory() {
        return this.workingMemory;
    }

    public void setActive(final boolean active) {
        if ( this.active == active ) {
            return;
        }
        this.active = active;
        if ( active ) {
            ((EventSupport) this.workingMemory).getRuleFlowEventSupport().fireBeforeRuleFlowGroupActivated( this,
                                                                                                            this.workingMemory );
            if ( this.list.isEmpty() ) {
                if ( this.autoDeactivate ) {
                    // if the list of activations is empty and
                    // auto-deactivate is on, deactivate this group
                    WorkingMemoryAction action = new DeactivateCallback( this );
                    this.workingMemory.queueWorkingMemoryAction( action );
                }
            } else {
                triggerActivations();
            }
            ((EventSupport) this.workingMemory).getRuleFlowEventSupport().fireAfterRuleFlowGroupActivated( this,
                                                                                                           this.workingMemory );
        } else {
            ((EventSupport) this.workingMemory).getRuleFlowEventSupport().fireBeforeRuleFlowGroupDeactivated( this,
                                                                                                              this.workingMemory );
            final Iterator it = this.list.iterator();
            for ( RuleFlowGroupNode node = (RuleFlowGroupNode) it.next(); node != null; node = (RuleFlowGroupNode) it.next() ) {
                final Activation activation = node.getActivation();
                activation.remove();
                if ( activation.getActivationGroupNode() != null ) {
                    activation.getActivationGroupNode().getActivationGroup().removeActivation( activation );
                }
            }
            notifyRuleFlowGroupListeners();
            ((EventSupport) this.workingMemory).getRuleFlowEventSupport().fireAfterRuleFlowGroupDeactivated( this,
                                                                                                             this.workingMemory );
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isAutoDeactivate() {
        return this.autoDeactivate;
    }

    public void setAutoDeactivate(final boolean autoDeactivate) {
        this.autoDeactivate = autoDeactivate;
        if ( autoDeactivate && this.active && this.list.isEmpty() ) {
            this.active = false;
        }
    }

    private void triggerActivations() {
        // iterate all activations adding them to their AgendaGroups
        final Iterator it = this.list.iterator();
        for ( RuleFlowGroupNode node = (RuleFlowGroupNode) it.next(); node != null; node = (RuleFlowGroupNode) it.next() ) {
            final Activation activation = node.getActivation();
            ((BinaryHeapQueueAgendaGroup) activation.getAgendaGroup()).add( activation );
        }
    }

    public void clear() {
        this.list.clear();
    }

    public int size() {
        return this.list.size();
    }

    public void addActivation(final Activation activation) {
        final RuleFlowGroupNode node = new RuleFlowGroupNode( activation,
                                                              this );
        activation.setRuleFlowGroupNode( node );
        this.list.add( node );

        if ( this.active ) {
            ((InternalAgendaGroup) activation.getAgendaGroup()).add( activation );
        }
    }

    public void removeActivation(final Activation activation) {
        final RuleFlowGroupNode node = activation.getRuleFlowGroupNode();
        this.list.remove( node );
        activation.setActivationGroupNode( null );
        if ( this.active && this.autoDeactivate ) {
            if ( this.list.isEmpty() ) {
                // deactivate callback
                WorkingMemoryAction action = new DeactivateCallback( this );
                this.workingMemory.queueWorkingMemoryAction( action );
            }
        }
    }

    public void addRuleFlowGroupListener(RuleFlowGroupListener listener) {
        if ( listeners == null ) {
            listeners = new CopyOnWriteArrayList<RuleFlowGroupListener>();
        }
        listeners.add( listener );
    }

    public void removeRuleFlowGroupListener(RuleFlowGroupListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    public void notifyRuleFlowGroupListeners() {
        if ( listeners != null ) {
            for ( java.util.Iterator<RuleFlowGroupListener> iterator = listeners.iterator(); iterator.hasNext(); ) {
                iterator.next().ruleFlowGroupDeactivated();
            }
        }
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public java.util.Iterator iterator() {
        return this.list.javaUtilIterator();
    }

    public String toString() {
        return "RuleFlowGroup '" + this.name + "'";
    }

    public boolean equal(final Object object) {
        if ( (object == null) || !(object instanceof RuleFlowGroupImpl) ) {
            return false;
        }

        if ( ((RuleFlowGroupImpl) object).name.equals( this.name ) ) {
            return true;
        }

        return false;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public static class DeactivateCallback
        implements
        WorkingMemoryAction {
        private static final long     serialVersionUID = 400L;
        private InternalRuleFlowGroup ruleFlowGroup;

        public DeactivateCallback() {
        }

        public DeactivateCallback(InternalRuleFlowGroup ruleFlowGroup) {
            this.ruleFlowGroup = ruleFlowGroup;
        }

        public DeactivateCallback(MarshallerReaderContext context) throws IOException {

        }

        public void write(MarshallerWriteContext context) throws IOException {
        }

        public void readExternal(ObjectInput in) throws IOException,
                                                ClassNotFoundException {
            ruleFlowGroup = (InternalRuleFlowGroup) in.readObject();
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject( ruleFlowGroup );
        }

        public void execute(InternalWorkingMemory workingMemory) {
            // check whether ruleflow group is still empty first
            if ( this.ruleFlowGroup.isEmpty() ) {
                // deactivate ruleflow group
                this.ruleFlowGroup.setActive( false );
            }
        }
    }
}
