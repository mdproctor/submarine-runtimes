package org.drools.core.common;

import org.drools.core.FactException;
import org.drools.core.SessionConfiguration;
import org.drools.core.base.DroolsQuery;
import org.drools.core.event.AgendaEventSupport;
import org.drools.core.event.RuleEventListenerSupport;
import org.drools.core.event.WorkingMemoryEventSupport;
import org.drools.core.phreak.RuleAgendaItem;
import org.drools.core.phreak.SegmentUtilities;
import org.drools.core.reteoo.LIANodePropagation;
import org.drools.core.reteoo.LeftInputAdapterNode;
import org.drools.core.reteoo.LeftInputAdapterNode.LiaNodeMemory;
import org.drools.core.reteoo.LeftTuple;
import org.drools.core.reteoo.LeftTupleSource;
import org.drools.core.reteoo.NodeTypeEnums;
import org.drools.core.reteoo.PathMemory;
import org.drools.core.reteoo.QueryTerminalNode;
import org.drools.core.reteoo.SegmentMemory;
import org.drools.core.reteoo.TerminalNode;
import org.drools.core.spi.AgendaFilter;
import org.drools.core.spi.FactHandleFactory;
import org.drools.core.spi.PropagationContext;
import org.kie.api.runtime.Environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReteWorkingMemory extends AbstractWorkingMemory {

    private List<LIANodePropagation> liaPropagations;

    public ReteWorkingMemory() {
    }

    public ReteWorkingMemory(int id, InternalRuleBase ruleBase) {
        super(id, ruleBase);
    }

    public ReteWorkingMemory(int id, InternalRuleBase ruleBase, SessionConfiguration config, Environment environment) {
        super(id, ruleBase, config, environment);
    }

    public ReteWorkingMemory(int id, InternalRuleBase ruleBase, FactHandleFactory handleFactory, InternalFactHandle initialFactHandle, long propagationContext, SessionConfiguration config, InternalAgenda agenda, Environment environment) {
        super(id, ruleBase, handleFactory, initialFactHandle, propagationContext, config, agenda, environment);
    }

    public ReteWorkingMemory(int id, InternalRuleBase ruleBase, FactHandleFactory handleFactory, InternalFactHandle initialFactHandle, long propagationContext, SessionConfiguration config, Environment environment, WorkingMemoryEventSupport workingMemoryEventSupport, AgendaEventSupport agendaEventSupport, RuleEventListenerSupport ruleEventListenerSupport, InternalAgenda agenda) {
        super(id, ruleBase, handleFactory, initialFactHandle, propagationContext, config, environment, workingMemoryEventSupport, agendaEventSupport, ruleEventListenerSupport, agenda);
    }


    public void reset(int handleId,
                      long handleCounter,
                      long propagationCounter) {
        super.reset(handleId, handleCounter, propagationCounter );
        if (liaPropagations != null) liaPropagations.clear();
    }

    public void addLIANodePropagation(LIANodePropagation liaNodePropagation) {
        if (liaPropagations == null) liaPropagations = new ArrayList();
        liaPropagations.add( liaNodePropagation );
    }

    public int fireAllRules(final AgendaFilter agendaFilter,
                            int fireLimit) throws FactException {
        if ( this.firing.compareAndSet( false,
                                        true ) ) {
            try {
                startOperation();
                ruleBase.readLock();

                // If we're already firing a rule, then it'll pick up the firing for any other assertObject(..) that get
                // nested inside, avoiding concurrent-modification exceptions, depending on code paths of the actions.
                if ( liaPropagations != null && isSequential() ) {
                    for ( Iterator it = liaPropagations.iterator(); it.hasNext(); ) {
                        ((LIANodePropagation) it.next()).doPropagation( this );
                    }
                }

                // do we need to call this in advance?
                executeQueuedActions();

                int fireCount = 0;
                fireCount = this.agenda.fireAllRules( agendaFilter,
                                                      fireLimit );
                return fireCount;
            } finally {
                ruleBase.readUnlock();
                endOperation();
                this.firing.set( false );
            }
        }
        return 0;
    }

    public void closeLiveQuery(final InternalFactHandle factHandle) {

        try {
            startOperation();
            this.ruleBase.readLock();
            this.lock.lock();

            final PropagationContext pCtx = pctxFactory.createPropagationContext(getNextPropagationIdCounter(), PropagationContext.INSERTION,
                                                                                 null, null, factHandle, getEntryPoint());

            getEntryPointNode().retractQuery( factHandle,
                                              pCtx,
                                              this );

            pCtx.evaluateActionQueue( this );

            getFactHandleFactory().destroyFactHandle( factHandle );

        } finally {
            this.lock.unlock();
            this.ruleBase.readUnlock();
            endOperation();
        }
    }

    private BaseNode[] evalQuery(String queryName, DroolsQuery queryObject, InternalFactHandle handle, PropagationContext pCtx) {
        BaseNode[] tnodes = ( BaseNode[] ) ruleBase.getReteooBuilder().getTerminalNodes(queryName);
        // no need to call retract, as no leftmemory used.
        getEntryPointNode().assertQuery( handle,
                                         pCtx,
                                         this );

        pCtx.evaluateActionQueue( this );
        return tnodes;
    }
}
