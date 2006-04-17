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

package org.drools.reteoo.beta;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.drools.WorkingMemory;
import org.drools.reteoo.FactHandleImpl;
import org.drools.reteoo.ReteTuple;
import org.drools.rule.Declaration;
import org.drools.spi.Evaluator;
import org.drools.spi.FieldExtractor;
import org.drools.util.MultiLinkedList;
import org.drools.util.MultiLinkedListNode;
import org.drools.util.MultiLinkedListNodeWrapper;

/**
 * BooleanConstrainedLeftMemory
 * A boolean constrained implementation for the left memory
 *
 * @author <a href="mailto:tirelli@post.com">Edson Tirelli</a>
 *
 * Created: 12/02/2006
 */
public class BooleanConstrainedLeftMemory
    implements
    BetaLeftMemory {

    private BetaLeftMemory  childMemory  = null;

    private MultiLinkedList trueList     = null;
    private MultiLinkedList falseList    = null;
    private MultiLinkedList selectedList = null;

    private FieldExtractor  extractor    = null;
    private Declaration     declaration  = null;
    private Evaluator       evaluator    = null;

    public BooleanConstrainedLeftMemory(FieldExtractor extractor,
                                        Declaration declaration,
                                        Evaluator evaluator) {
        this( extractor,
              declaration,
              evaluator,
              null );
    }

    public BooleanConstrainedLeftMemory(FieldExtractor extractor,
                                        Declaration declaration,
                                        Evaluator evaluator,
                                        BetaLeftMemory childMemory) {
        this.extractor = extractor;
        this.declaration = declaration;
        this.evaluator = evaluator;
        this.childMemory = childMemory;
        this.trueList = new MultiLinkedList();
        this.falseList = new MultiLinkedList();
    }

    /**
     * @inheritDoc 
     *
     * @see org.drools.reteoo.beta.BetaLeftMemory#add(org.drools.WorkingMemory, org.drools.reteoo.ReteTuple)
     */
    public void add(WorkingMemory workingMemory,
                    ReteTuple tuple) {
        boolean select = ((Boolean) declaration.getValue( workingMemory.getObject( tuple.get( this.declaration ) ) )).booleanValue();
        if ( select == true ) {
            trueList.add( tuple );
        } else {
            falseList.add( tuple );
        }
        if ( this.childMemory != null ) {
            tuple.setChild( new MultiLinkedListNodeWrapper( tuple ) );
            this.childMemory.add( workingMemory,
                                  ((MultiLinkedListNodeWrapper) tuple.getChild()) );
        }
    }

    /**
     * @inheritDoc 
     *
     * @see org.drools.reteoo.beta.BetaLeftMemory#remove(org.drools.reteoo.ReteTuple)
     */
    public void remove(WorkingMemory workingMemory,
                       ReteTuple tuple) {
        if ( this.childMemory != null ) {
            this.childMemory.remove( workingMemory,
                                     (MultiLinkedListNodeWrapper) tuple.getChild() );
        }
        tuple.getLinkedList().remove( tuple );
    }

    /**
     * @inheritDoc 
     *
     * @see org.drools.reteoo.beta.BetaLeftMemory#add(org.drools.reteoo.ReteTuple)
     */
    public void add(WorkingMemory workingMemory,
                    MultiLinkedListNodeWrapper tuple) {
        boolean partition = ((Boolean) declaration.getValue( workingMemory.getObject( ((ReteTuple) tuple.getNode()).get( this.declaration ) ) )).booleanValue();
        if ( partition == true ) {
            trueList.add( tuple );
        } else {
            falseList.add( tuple );
        }
        if ( this.childMemory != null ) {
            tuple.setChild( new MultiLinkedListNodeWrapper( tuple.getNode() ) );
            this.childMemory.add( workingMemory,
                                  ((MultiLinkedListNodeWrapper) tuple.getChild()) );
        }
    }

    /**
     * @inheritDoc 
     *
     * @see org.drools.reteoo.beta.BetaLeftMemory#remove(org.drools.reteoo.ReteTuple)
     */
    public void remove(WorkingMemory workingMemory,
                       MultiLinkedListNodeWrapper tuple) {
        if ( this.childMemory != null ) {
            this.childMemory.remove( workingMemory,
                                     (MultiLinkedListNodeWrapper) tuple.getChild() );
        }
        tuple.getLinkedList().remove( tuple );
    }

    /**
     * @inheritDoc 
     *
     * @see org.drools.reteoo.beta.BetaLeftMemory#isEmpty()
     */
    public boolean isEmpty() {
        return (trueList.isEmpty()) && (falseList.isEmpty());
    }

    /**
     * @inheritDoc 
     *
     * @see org.drools.reteoo.beta.BetaLeftMemory#iterator(org.drools.WorkingMemory, org.drools.reteoo.FactHandleImpl)
     */
    public Iterator iterator(final WorkingMemory workingMemory,
                             final FactHandleImpl handle) {
        this.selectPossibleMatches( workingMemory,
                                    handle );
        Iterator iterator = new Iterator() {
            Iterator            it      = selectedList.iterator();
            MultiLinkedListNode current = null;
            MultiLinkedListNode next    = null;

            public boolean hasNext() {
                boolean hasnext = false;
                if ( next == null ) {
                    while ( it.hasNext() ) {
                        next = (MultiLinkedListNode) it.next();
                        if ( (childMemory == null) || (childMemory.isPossibleMatch( (MultiLinkedListNodeWrapper) next.getChild() )) ) {
                            hasnext = true;
                            break;
                        }
                    }
                } else {
                    hasnext = true;
                }
                return hasnext;
            }

            public Object next() {
                if ( this.next == null ) {
                    this.hasNext();
                }
                this.current = this.next;
                this.next = null;
                if ( this.current == null ) {
                    throw new NoSuchElementException( "No more elements to return" );
                }
                return this.current;
            }

            public void remove() {
                if ( this.current != null ) {
                    BooleanConstrainedLeftMemory.this.remove( workingMemory,
                                                              (ReteTuple) current );
                } else {
                    throw new IllegalStateException( "No item to remove. Call next() before calling remove()." );
                }
            }

        };
        return iterator;
    }

    /**
     * @inheritDoc
     */
    public Iterator iterator() {
        return new Iterator() {
            Iterator  trueIt       = trueList.iterator();
            Iterator  falseIt      = falseList.iterator();
            ReteTuple currentTrue  = null;
            ReteTuple currentFalse = null;
            ReteTuple current      = null;
            ReteTuple next         = null;

            public boolean hasNext() {
                boolean hasnext = false;
                if ( next == null ) {
                    if ( (currentTrue == null) && (trueIt.hasNext()) ) {
                        currentTrue = (ReteTuple) trueIt.next();
                    }
                    if ( (currentFalse == null) && (falseIt.hasNext()) ) {
                        currentFalse = (ReteTuple) falseIt.next();
                    }
                    if ( (currentTrue != null) && (currentFalse != null) ) {
                        if ( currentTrue.getRecency() <= currentFalse.getRecency() ) {
                            next = currentTrue;
                            currentTrue = null;
                        } else {
                            next = currentFalse;
                            currentFalse = null;
                        }
                        hasnext = true;
                    } else if ( currentTrue != null ) {
                        next = currentTrue;
                        currentTrue = null;
                        hasnext = true;
                    } else if ( currentFalse != null ) {
                        next = currentFalse;
                        currentFalse = null;
                        hasnext = true;
                    }
                    // if no previous condition evaluates to true, 
                    // than next will be null and hasnext will be false
                } else {
                    hasnext = true;
                }
                return hasnext;
            }

            public Object next() {
                if ( this.next == null ) {
                    this.hasNext();
                }
                this.current = this.next;
                this.next = null;
                if ( this.current == null ) {
                    throw new NoSuchElementException( "No more elements to return" );
                }
                return this.current;
            }

            public void remove() {
                throw new UnsupportedOperationException( "Not possible to call remove when iterating over all elements" );
            }
        };
    }

    /**
     * @inheritDoc 
     *
     * @see org.drools.reteoo.beta.BetaLeftMemory#selectPossibleMatches(org.drools.WorkingMemory, org.drools.reteoo.FactHandleImpl)
     */
    public void selectPossibleMatches(WorkingMemory workingMemory,
                                      FactHandleImpl handle) {
        boolean select = ((Boolean) this.extractor.getValue( workingMemory.getObject( handle ) )).booleanValue();
        select = (evaluator.getOperator()) == Evaluator.EQUAL ? select : !select;
        if ( select == true ) {
            this.selectedList = trueList;
        } else {
            this.selectedList = falseList;
        }
        if ( this.childMemory != null ) {
            this.childMemory.selectPossibleMatches( workingMemory,
                                                    handle );
        }
    }

    /**
     * @inheritDoc 
     *
     * @see org.drools.reteoo.beta.BetaLeftMemory#isPossibleMatch(org.drools.util.MultiLinkedListNodeWrapper)
     */
    public boolean isPossibleMatch(MultiLinkedListNodeWrapper tuple) {
        boolean isPossible = ((this.selectedList != null) && (tuple.getLinkedList() == this.selectedList));
        if ( (isPossible) && (this.childMemory != null) ) {
            isPossible = this.childMemory.isPossibleMatch( (MultiLinkedListNodeWrapper) tuple.getChild() );
        }
        return isPossible;
    }

    public int size() {
        return this.trueList.size() + this.falseList.size();
    }

}
