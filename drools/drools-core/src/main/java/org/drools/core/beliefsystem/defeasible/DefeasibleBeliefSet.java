package org.drools.core.beliefsystem.defeasible;

import org.drools.core.beliefsystem.BeliefSet;
import org.drools.core.beliefsystem.BeliefSystem;
import org.drools.core.beliefsystem.jtms.JTMSBeliefSet.MODE;
import org.drools.core.beliefsystem.simple.SimpleLogicalDependency;
import org.drools.core.common.InternalFactHandle;
import org.drools.core.common.LogicalDependency;
import org.drools.core.common.WorkingMemoryAction;
import org.drools.core.rule.Rule;
import org.drools.core.spi.PropagationContext;
import org.drools.core.util.Entry;
import org.drools.core.util.FastIterator;
import org.drools.core.util.LinkedListEntry;
import org.drools.core.util.LinkedListNode;

import java.util.Arrays;

public class DefeasibleBeliefSet implements BeliefSet {
    private BeliefSystem beliefSystem;

    public static final String DEFEATS = Defeats.class.getSimpleName();

    private static final FastIterator iterator = new IteratorImpl();

    private InternalFactHandle rootHandle;
    private InternalFactHandle positiveFactHandle;
    private InternalFactHandle negativeFactHandle;

    private LinkedListEntry<DefeasibleLogicalDependency> rootUndefeated;
    private LinkedListEntry<DefeasibleLogicalDependency> tailUndefeated;

    private int definitelyPosCount;
    private int definitelyNegCount;
    private int defeasiblyPosCount;
    private int defeasiblyNegCount;
    private int defeatedlyPosCount;
    private int defeatedlyNegCount;

    private static final int DEFINITELY_POS_BIT = 1;
    private static final int DEFINITELY_NEG_BIT = 2;
    private static final int DEFEASIBLY_POS_BIT = 4;
    private static final int DEFEASIBLY_NEG_BIT = 6;
    private static final int DEFEATEDLY_POS_BIT = 8;
    private static final int DEFEATEDLY_NEG_BIT = 10;

    private static final int POS_MASK = DEFINITELY_POS_BIT & DEFEASIBLY_POS_BIT & DEFEATEDLY_POS_BIT;
    private static final int NEG_MASK = DEFINITELY_NEG_BIT & DEFEASIBLY_NEG_BIT & DEFEATEDLY_NEG_BIT;

    private int statusMask;

    public DefeasibleBeliefSet(BeliefSystem beliefSystem, InternalFactHandle rootHandle) {
        this.beliefSystem = beliefSystem;
        this.rootHandle = rootHandle;
    }

    public BeliefSystem getBeliefSystem() {
        return null;
    }

    public InternalFactHandle getFactHandle() {
        return rootHandle;
    }

    public LinkedListEntry<DefeasibleLogicalDependency>  getFirst() {
        return rootUndefeated;
    }

    public InternalFactHandle getPositiveFactHandle() {
        return positiveFactHandle;
    }

    public void setPositiveFactHandle(InternalFactHandle positiveFactHandle) {
        this.positiveFactHandle = positiveFactHandle;
    }

    public InternalFactHandle getNegativeFactHandle() {
        return negativeFactHandle;
    }

    public void setNegativeFactHandle(InternalFactHandle negativeFactHandle) {
        this.negativeFactHandle = negativeFactHandle;
    }

    public void add(LinkedListNode node) {
        DefeasibleLogicalDependency newDep = (DefeasibleLogicalDependency) ((LinkedListEntry) node).getObject();

        Rule rule = newDep.getJustifier().getRule();

        // first iterate to see if this new dep is defeated. If it's defeated, it can no longer impacts any deps
        // if we checked what it defeats, and later this was defeated, we would have undo action. So we do the cheaper work first.
        boolean wasDefeated = false;
        for (LinkedListEntry<DefeasibleLogicalDependency> existingNode = rootUndefeated; existingNode != null; existingNode = existingNode.getNext()) {
            DefeasibleLogicalDependency existingDep = existingNode.getObject();
            wasDefeated = checkAndApplyIsDefeated(newDep, rule, existingDep);
            if (wasDefeated) {
                break;
            }
        }

        if (!wasDefeated) {
            DefeasibleLogicalDependency stagedDeps = null;
            //for (DefeasibleLogicalDependency existingDep = rootUndefeated; existingDep != null; ) {
            for (LinkedListEntry<DefeasibleLogicalDependency> existingNode = rootUndefeated; existingNode != null; existingNode = existingNode.getNext()) {
                LinkedListEntry<DefeasibleLogicalDependency> next = existingNode.getNext();
                DefeasibleLogicalDependency existingDep = existingNode.getObject();

                if (checkAndApplyIsDefeated(existingDep, existingDep.getJustifier().getRule(), newDep)) {
                    // fist remove it from the undefeated list
                    removeUndefeated(existingDep, existingNode);
                    if (existingDep.getRootDefeated() != null) {
                        // build up the list of staged deps, that will need to be reprocessed
                        if (stagedDeps == null) {
                            stagedDeps = existingDep.getRootDefeated();
                        } else {
                            stagedDeps.setPrevious(existingDep.getTailDefeated());
                            stagedDeps = existingDep.getRootDefeated();
                        }
                    }
                }
                existingNode = next;
            }
            addUndefeated(newDep, (LinkedListEntry<DefeasibleLogicalDependency>) node);
            // now process the staged
            reprocessDefeated(stagedDeps);
        }
    }

    private void reprocessDefeated(DefeasibleLogicalDependency deps) {
        for (DefeasibleLogicalDependency dep = deps; dep != null; ) {
            DefeasibleLogicalDependency next = (DefeasibleLogicalDependency) dep.getNext();
            dep.nullPrevNext(); // it needs to be removed, before it can be processed
            add(dep); // adding back in, effectively reprocesses the dep
            dep = next;
        }
    }

    public void remove(LinkedListNode node) {
        DefeasibleLogicalDependency dep =  ((LinkedListEntry<DefeasibleLogicalDependency>)node).getObject();

        if (dep.getDefeatedBy() != null) {
            // Defeated deps do not have defeated defeated lists of their own, so just remove.
            DefeasibleLogicalDependency defeater = dep.getDefeatedBy();
            defeater.removeDefeated(dep);
        } else {
            // ins undefeated, process it's defeated list if they exist
            removeUndefeated(dep, (LinkedListEntry<DefeasibleLogicalDependency>) node);
            if (dep.getRootDefeated() != null) {
                reprocessDefeated(dep.getRootDefeated());
            }
        }
    }

    private boolean checkAndApplyIsDefeated(DefeasibleLogicalDependency potentialInferior, Rule rule, DefeasibleLogicalDependency potentialSuperior) {
        // adds the references that defeat the current node
        if (Arrays.binarySearch(potentialSuperior.getDefeats(), rule.getName()) >= 0 ||
            Arrays.binarySearch(potentialSuperior.getDefeats(), rule.getPackage() + "." + rule.getName()) >= 0) {
            potentialSuperior.addDefeated(potentialInferior);
            return true;
        }
        return false;
    }

    public void addUndefeated(DefeasibleLogicalDependency dep, LinkedListEntry<DefeasibleLogicalDependency> node) {
        boolean pos = dep.getValue() != null && MODE.POSITIVE.getId().equals( dep.getValue().toString() );
        switch( dep.getStatus() ) {
            case DEFINITELY:
                if ( pos ) {
                    definitelyPosCount++;
                    statusMask = statusMask & DEFINITELY_POS_BIT;
                } else {
                    definitelyNegCount++;
                    statusMask = statusMask & DEFINITELY_NEG_BIT;
                }
                break;
            case DEFEASIBLY:
                if ( pos ) {
                    defeasiblyPosCount++;
                    statusMask = statusMask & DEFEASIBLY_POS_BIT;
                } else {
                    defeasiblyNegCount++;
                    statusMask = statusMask & DEFEASIBLY_NEG_BIT;
                }
                break;
            case DEFEATEDLY:
                if ( pos ) {
                    defeatedlyPosCount++;
                    statusMask = statusMask & DEFEATEDLY_POS_BIT;
                } else {
                    defeatedlyNegCount++;
                    statusMask = statusMask & DEFEATEDLY_NEG_BIT;
                }
                break;
            case UNDECIDABLY:
                throw new IllegalStateException("Individual logical dependencies cannot be undecidably");
        }

        if (rootUndefeated == null) {
            rootUndefeated = node;
            tailUndefeated = node;
        } else {
            if ( dep.getStatus() == DefeasibilityStatus.DEFINITELY ) {
                // Strict dependencies at to the front
                rootUndefeated.setPrevious(node);
                node.setNext(rootUndefeated);
                rootUndefeated = node;
            } else {
                // add to end
                tailUndefeated.setNext(node);
                node.setPrevious(rootUndefeated);
                tailUndefeated = node;
            }
        }
    }

    public void removeUndefeated(DefeasibleLogicalDependency dep, LinkedListEntry<DefeasibleLogicalDependency> node) {
        boolean pos = dep.getValue() != null && MODE.POSITIVE.getId().equals( dep.getValue().toString() );
        switch( dep.getStatus() ) {
            case DEFINITELY:
                if ( pos ) {
                    definitelyPosCount--;
                    if ( definitelyPosCount == 0 ) {
                        statusMask = statusMask ^ DEFINITELY_POS_BIT;
                    }
                } else {
                    definitelyNegCount--;
                    if ( definitelyNegCount == 0 ) {
                        statusMask = statusMask ^ DEFINITELY_NEG_BIT;
                    }
                }
                break;
            case DEFEASIBLY:
                if ( pos ) {
                    defeasiblyPosCount--;
                    if ( defeasiblyPosCount == 0 ) {
                        statusMask = statusMask ^ DEFEASIBLY_POS_BIT;
                    }
                } else {
                    defeasiblyNegCount--;
                    if ( defeasiblyNegCount == 0 ) {
                        statusMask = statusMask ^ DEFEASIBLY_NEG_BIT;
                    }
                }
                break;
            case DEFEATEDLY:
                if ( pos ) {
                    defeatedlyPosCount--;
                    if ( defeatedlyPosCount == 0 ) {
                        statusMask = statusMask ^ DEFEATEDLY_POS_BIT;
                    }
                } else {
                    defeatedlyNegCount--;
                    if ( defeatedlyNegCount == 0 ) {
                        statusMask = statusMask ^ DEFEATEDLY_NEG_BIT;
                    }
                }
                break;
            case UNDECIDABLY:
                throw new IllegalStateException("Individual logical dependencies cannot be undecidably");
        }

        if (this.rootUndefeated == node) {
            removeFirst();
        } else if (this.tailUndefeated == node) {
            removeLast();
        } else {
            node.getPrevious().setNext(node.getNext());
            ((LinkedListNode)node.getNext()).setPrevious(node.getPrevious());
            node.nullPrevNext();
        }
    }

    public LinkedListNode removeFirst() {
        if (this.rootUndefeated == null) {
            return null;
        }
        final LinkedListEntry<DefeasibleLogicalDependency> node = this.rootUndefeated;
        this.rootUndefeated = node.getNext();
        node.setNext(null);
        if (this.rootUndefeated != null) {
            this.rootUndefeated.setPrevious(null);
        } else {
            this.tailUndefeated = null;
        }
        return node;
    }

    public LinkedListNode removeLast() {
        if (this.tailUndefeated == null) {
            return null;
        }
        final LinkedListEntry<DefeasibleLogicalDependency> node = this.tailUndefeated;
        this.tailUndefeated = node.getPrevious();
        node.setPrevious(null);
        if (this.tailUndefeated != null) {
            this.tailUndefeated.setNext(null);
        } else {
            this.rootUndefeated = this.tailUndefeated;
        }
        return node;
    }

    public LinkedListNode getRootUndefeated() {
        return this.rootUndefeated;
    }

    public LinkedListNode getTailUnDefeated() {
        return this.tailUndefeated;
    }

    public boolean isEmpty() {
        return rootUndefeated == null;
    }

    public int size() {
        return definitelyPosCount + definitelyNegCount + defeasiblyPosCount + defeasiblyNegCount + defeatedlyPosCount + defeatedlyNegCount;
    }

    public void cancel(PropagationContext propagationContext) {
        // get all but last, as that we'll do via the BeliefSystem, for cleanup
        // note we don't update negative, conflict counters. It's needed for the last cleanup operation
        FastIterator it = iterator();
        for ( LinkedListEntry<DefeasibleLogicalDependency>  node =  getFirst(); node != tailUndefeated;  ) {
            LinkedListEntry<DefeasibleLogicalDependency>  temp = (LinkedListEntry<DefeasibleLogicalDependency>) it.next(node); // get next, as we are about to remove it
            final DefeasibleLogicalDependency dep =  node.getObject();
            dep.getJustifier().getLogicalDependencies().remove( dep );
            remove( dep );
            node = temp;
        }

        LinkedListEntry last = (LinkedListEntry) getFirst();
        final LogicalDependency node = (LogicalDependency) last.getObject();
        node.getJustifier().getLogicalDependencies().remove( node );
        //beliefSystem.delete( node, this, context );
        positiveFactHandle = null;
        negativeFactHandle = null;
    }

    public void clear(PropagationContext propagationContext) {
    }

    public void setWorkingMemoryAction(WorkingMemoryAction wmAction) {
    }

    public boolean isDefinitelyPosProveable() {
        return  (statusMask &  DEFINITELY_POS_BIT) != 0;
    }

    public boolean isDefinitelyNegProveable() {
        return  (statusMask &  DEFINITELY_NEG_BIT) != 0;
    }

    public boolean isDefeasiblyPosProveable() {
        return  (statusMask &  DEFEASIBLY_POS_BIT) != 0;
    }

    public boolean isDefeasiblyNegProveable() {
        return  (statusMask &  DEFEASIBLY_NEG_BIT) != 0;
    }

    public boolean isDefeatedlyPosProveable() {
        return  (statusMask &  DEFEATEDLY_POS_BIT) != 0;
    }

    public boolean isDefeatedlyNegProveable() {
        return  (statusMask &  DEFEATEDLY_NEG_BIT) != 0;
    }

    public DefeasibilityStatus getStatus() {
        if ( isDefinitelyPosProveable() && !isDefinitelyNegProveable() ) {
            return DefeasibilityStatus.DEFINITELY;
        } else if ( !isDefinitelyPosProveable() && isDefinitelyNegProveable() ) {
            return DefeasibilityStatus.DEFINITELY;
        }  else if ( isDefinitelyPosProveable() && isDefinitelyNegProveable() ) {
            return DefeasibilityStatus.UNDECIDABLY;
        }

        if ( isDefeasiblyPosProveable() && !isDefeasiblyNegProveable() && !isDefeatedlyNegProveable() ) {
            return DefeasibilityStatus.DEFEASIBLY;
        } else if ( !isDefeasiblyPosProveable() && !isDefeatedlyPosProveable() && isDefeasiblyNegProveable() ) {
            return DefeasibilityStatus.DEFEASIBLY;
        }

//        else if ( isDefinitelyPosProveable() && isDefinitelyNegProveable() ) {
//            return DefeasibilityStatus.UNDECIDABLY;
//        }

        return DefeasibilityStatus.UNDECIDABLY;
    }

    public boolean isNegated() {
        return ((statusMask & POS_MASK ) == 0 ) &&  ((statusMask & NEG_MASK ) != 0 );
    }

    public boolean isPositive() {
        return ((statusMask & POS_MASK ) != 0 ) &&  ((statusMask & NEG_MASK ) == 0 );
    }

    public boolean isConflicting() {
        return getStatus() == DefeasibilityStatus.UNDECIDABLY;
    }

    public FastIterator iterator() {
        return iterator();
    }

    private static class IteratorImpl implements FastIterator {

        public Entry next(Entry object) {
            DefeasibleLogicalDependency dep = ( DefeasibleLogicalDependency ) object;
            if ( dep.getRootDefeated() != null ) {
                // try going down the list of defeated first
                return dep.getRootDefeated();
            }

            if ( dep.getNext() != null ) {
                return dep.getNext();
            }

            if ( dep.getDefeatedBy() != null ) {
                // go back up to the parent undefeated, and try the next undefeated
                return dep.getDefeatedBy().getNext();
            }

            return null; // nothing more to iterate too.

        }

        public boolean isFullIterator() {
            return true;
        }
    }

}
