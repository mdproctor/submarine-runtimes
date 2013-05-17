package org.drools.core.phreak;

import org.drools.core.base.DefaultKnowledgeHelper;
import org.drools.core.common.AgendaItem;
import org.drools.core.common.DefaultAgenda;
import org.drools.core.common.InternalAgenda;
import org.drools.core.common.InternalAgendaGroup;
import org.drools.core.common.InternalFactHandle;
import org.drools.core.common.InternalRuleFlowGroup;
import org.drools.core.common.InternalWorkingMemory;
import org.drools.core.common.LeftTupleSets;
import org.drools.core.reteoo.LeftTuple;
import org.drools.core.reteoo.NodeTypeEnums;
import org.drools.core.reteoo.RuleTerminalNode;
import org.drools.core.reteoo.RuleTerminalNodeLeftTuple;
import org.drools.core.reteoo.TerminalNode;
import org.drools.core.rule.Rule;
import org.drools.core.spi.PropagationContext;
import org.drools.core.spi.Salience;

/**
* Created with IntelliJ IDEA.
* User: mdproctor
* Date: 03/05/2013
* Time: 15:42
* To change this template use File | Settings | File Templates.
*/
public class PhreakRuleTerminalNode {
    public void doNode(TerminalNode rtnNode,
                       InternalWorkingMemory wm,
                       LeftTupleSets srcLeftTuples,
                       RuleExecutor executor) {
        if (srcLeftTuples.getDeleteFirst() != null) {
            doLeftDeletes(rtnNode, wm, srcLeftTuples, executor);
        }

        if (srcLeftTuples.getUpdateFirst() != null) {
            doLeftUpdates(rtnNode, wm, srcLeftTuples, executor);
        }

        if (srcLeftTuples.getInsertFirst() != null) {
            doLeftInserts(rtnNode, wm, srcLeftTuples, executor);
        }

        srcLeftTuples.resetAll();
    }

    public void doLeftInserts(TerminalNode rtnNode,
                              InternalWorkingMemory wm,
                              LeftTupleSets srcLeftTuples,
                              RuleExecutor executor) {
        InternalAgenda agenda = ( InternalAgenda ) wm.getAgenda();
        RuleAgendaItem ruleAgendaItem = executor.getRuleAgendaItem();

        int salienceInt = 0;
        Salience salience = ruleAgendaItem.getRule().getSalience();
        if ( !salience.isDynamic() ) {
            salienceInt = ruleAgendaItem.getRule().getSalience().getValue();
            salience = null;
        }

        if ( rtnNode.getRule().getAutoFocus() && !ruleAgendaItem.getAgendaGroup().isActive() ) {
            ((DefaultAgenda)wm.getAgenda()).setFocus(ruleAgendaItem.getAgendaGroup());
        }

        for (LeftTuple leftTuple = srcLeftTuples.getInsertFirst(); leftTuple != null; ) {
            LeftTuple next = leftTuple.getStagedNext();

            doLeftTupleInsert(rtnNode, executor, agenda, ruleAgendaItem, salienceInt, salience, leftTuple, wm);

            leftTuple.clearStaged();
            leftTuple = next;
        }
    }

    public static void doLeftTupleInsert(TerminalNode rtnNode, RuleExecutor executor,
                                         InternalAgenda agenda, RuleAgendaItem ruleAgendaItem, int salienceInt,
                                         Salience salience, LeftTuple leftTuple, InternalWorkingMemory wm) {
        PropagationContext pctx = leftTuple.getPropagationContext();
        pctx = RuleTerminalNode.findMostRecentPropagationContext(leftTuple,
                                                                 pctx);

        if ( salience != null ) {
                salienceInt = salience.getValue(new DefaultKnowledgeHelper((AgendaItem) leftTuple, wm),
                                                rtnNode.getRule(), wm);
        }

        RuleTerminalNodeLeftTuple rtnLeftTuple = (RuleTerminalNodeLeftTuple) leftTuple;
        rtnLeftTuple.init(agenda.getNextActivationCounter(), salienceInt, pctx, ruleAgendaItem, ruleAgendaItem.getAgendaGroup(), ruleAgendaItem.getRuleFlowGroup() );
        rtnLeftTuple.setObject( rtnLeftTuple );

        if (  rtnNode.getRule().isLockOnActive() &&
              leftTuple.getPropagationContext().getType() != org.kie.api.runtime.rule.PropagationContext.RULE_ADDITION ) {
            long handleRecency = ((InternalFactHandle) pctx.getFactHandle()).getRecency();
            InternalAgendaGroup agendaGroup = executor.getRuleAgendaItem().getAgendaGroup();
            if (blockedByLockOnActive(rtnNode.getRule(), agenda, pctx, handleRecency, agendaGroup)) {
                return;
            }
        }

        ((DefaultAgenda)wm.getAgenda()).addItemToActivationGroup( rtnLeftTuple );

        executor.addLeftTuple(leftTuple);
        leftTuple.increaseActivationCountForEvents(); // increased here, decreased in Agenda's cancelActivation and fireActivation
        if( !rtnNode.isFireDirect() && executor.isDeclarativeAgendaEnabled() ) {
            agenda.insertAndStageActivation(rtnLeftTuple);
        }
    }

    public void doLeftUpdates(TerminalNode rtnNode,
                              InternalWorkingMemory wm,
                              LeftTupleSets srcLeftTuples,
                              RuleExecutor executor) {
        InternalAgenda agenda = ( InternalAgenda ) wm.getAgenda();

        RuleAgendaItem ruleAgendaItem = executor.getRuleAgendaItem();
        if ( rtnNode.getRule().getAutoFocus() && !ruleAgendaItem.getAgendaGroup().isActive() ) {
            ((DefaultAgenda)wm.getAgenda()).setFocus(ruleAgendaItem.getAgendaGroup());
        }

        int salienceInt = 0;
        Salience salience = ruleAgendaItem.getRule().getSalience();
        if ( !salience.isDynamic() ) {
            salienceInt = ruleAgendaItem.getRule().getSalience().getValue();
        } else {
            salience = null;
        }

        //Salience salienceInt = ruleAgendaItem.getRule().getSalience();
        for (LeftTuple leftTuple = srcLeftTuples.getUpdateFirst(); leftTuple != null; ) {
            LeftTuple next = leftTuple.getStagedNext();

            doLeftTupleUpdate(rtnNode, executor, agenda, salienceInt, salience, leftTuple, wm);

            leftTuple.clearStaged();
            leftTuple = next;
        }
    }

    public static void doLeftTupleUpdate(TerminalNode rtnNode, RuleExecutor executor,
                                         InternalAgenda agenda, int salienceInt, Salience salience,
                                         LeftTuple leftTuple, InternalWorkingMemory wm) {
        PropagationContext pctx = leftTuple.getPropagationContext();
        pctx = RuleTerminalNode.findMostRecentPropagationContext(leftTuple,
                                                                 pctx);

        boolean blocked = false;
        RuleTerminalNodeLeftTuple rtnLeftTuple = (RuleTerminalNodeLeftTuple) leftTuple;
        if( executor.isDeclarativeAgendaEnabled() ) {
           if ( rtnLeftTuple.getBlockers() != null && !rtnLeftTuple.getBlockers().isEmpty() ) {
               blocked = true; // declarativeAgenda still blocking LeftTuple, so don't add back ot list
           }
        }

        if ( salience != null ) {
            salienceInt = salience.getValue( new DefaultKnowledgeHelper(rtnLeftTuple, wm),
                                             rtnNode.getRule(), wm);
        }
        if ( !blocked ) {
            boolean addToExector = true;
            if (  rtnNode.getRule().isLockOnActive() &&
                  pctx.getType() != org.kie.api.runtime.rule.PropagationContext.RULE_ADDITION ) {

                long handleRecency = ((InternalFactHandle) pctx.getFactHandle()).getRecency();
                InternalAgendaGroup agendaGroup = executor.getRuleAgendaItem().getAgendaGroup();
                if (blockedByLockOnActive(rtnNode.getRule(), agenda, pctx, handleRecency, agendaGroup)) {
                    addToExector = false;
                }
            }
            if ( addToExector ) {
                if (rtnLeftTuple.isQueued() ) {
                    executor.updateLeftTuple(rtnLeftTuple, salienceInt, pctx);
                } else {
                    rtnLeftTuple.update(salienceInt, pctx);
                    executor.addLeftTuple(leftTuple);
                }
            }

        } else {
            // LeftTuple is blocked, and thus not queued, so just update it's values
            rtnLeftTuple.update(salienceInt, pctx);
        }

        if( !rtnNode.isFireDirect() && executor.isDeclarativeAgendaEnabled()) {
            agenda.modifyActivation(rtnLeftTuple, rtnLeftTuple.isQueued());
        }
    }

    public void doLeftDeletes(TerminalNode rtnNode,
                              InternalWorkingMemory wm,
                              LeftTupleSets srcLeftTuples,
                              RuleExecutor executor) {

        for (LeftTuple leftTuple = srcLeftTuples.getDeleteFirst(); leftTuple != null; ) {
            LeftTuple next = leftTuple.getStagedNext();
            PropagationContext pctx = leftTuple.getPropagationContext();
            if ( leftTuple.getMemory() != null && !(pctx.getType() == PropagationContext.EXPIRATION && pctx.getFactHandleOrigin() != null ) ) {
                // Expiration propagations should not be removed from the list, as they still need to fire
                executor.removeLeftTuple(leftTuple);
            }
            rtnNode.retractLeftTuple(leftTuple, leftTuple.getPropagationContext(), wm);
            leftTuple.clearStaged();
            leftTuple.setObject(null);
            leftTuple = next;
        }
    }

    private static boolean blockedByLockOnActive(Rule rule,
                                          InternalAgenda agenda,
                                          PropagationContext pctx,
                                          long handleRecency,
                                          InternalAgendaGroup agendaGroup) {
        if ( rule.isLockOnActive() ) {
            boolean isActive = false;
            long activatedForRecency = 0;
            long clearedForRecency = 0;

            if ( rule.getRuleFlowGroup() == null ) {
                isActive = agendaGroup.isActive();
                activatedForRecency = agendaGroup.getActivatedForRecency();
                clearedForRecency = agendaGroup.getClearedForRecency();
            } else {
                InternalRuleFlowGroup rfg = (InternalRuleFlowGroup) agenda.getRuleFlowGroup( rule.getRuleFlowGroup() );
                isActive = rfg.isActive();
                activatedForRecency = rfg.getActivatedForRecency();
                clearedForRecency = rfg.getClearedForRecency();
            }

            if ( isActive && activatedForRecency < handleRecency &&
                 agendaGroup.getAutoFocusActivator() != pctx ) {
                return true;
            } else if ( clearedForRecency != -1 && clearedForRecency >= handleRecency ) {
                return true;
            }

        }
        return false;
    }
}
