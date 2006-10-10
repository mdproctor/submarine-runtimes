package org.drools.reteoo;

import java.util.ArrayList;
import java.util.List;

import org.drools.base.evaluators.Operator;
import org.drools.common.InternalFactHandle;
import org.drools.common.InternalWorkingMemory;
import org.drools.rule.LiteralConstraint;
import org.drools.spi.Evaluator;
import org.drools.spi.AlphaNodeFieldConstraint;
import org.drools.spi.FieldExtractor;
import org.drools.spi.PropagationContext;
import org.drools.util.Iterator;
import org.drools.util.LinkedList;
import org.drools.util.LinkedListEntry;
import org.drools.util.LinkedListNode;
import org.drools.util.ObjectHashMap;
import org.drools.util.ObjectHashMap.ObjectEntry;

public class CompositeObjectSinkAdapter
    implements
    ObjectSinkPropagator {

    private ObjectSinkNodeList otherSinks;
    private ObjectSinkNodeList hashableSinks;

    private LinkedList         hashedFieldIndexes;

    private ObjectHashMap      hashedSinkMap;

    private HashKey            hashKey;

    public CompositeObjectSinkAdapter() {
        this.hashKey = new HashKey();
    }

    public void addObjectSink(ObjectSink sink) {
        if ( sink.getClass() == AlphaNode.class ) {
            AlphaNode alphaNode = (AlphaNode) sink;
            AlphaNodeFieldConstraint fieldConstraint = alphaNode.getConstraint();

            if ( fieldConstraint.getClass() == LiteralConstraint.class ) {
                LiteralConstraint literalConstraint = (LiteralConstraint) fieldConstraint;
                Evaluator evaluator = literalConstraint.getEvaluator();

                if ( evaluator.getOperator() == Operator.EQUAL ) {
                    int index = literalConstraint.getFieldExtractor().getIndex();
                    FieldIndex fieldIndex = registerFieldIndex( index,
                                                                literalConstraint.getFieldExtractor() );

                    if ( fieldIndex.getCount() >= 3 ) {
                        if ( !fieldIndex.isHashed() ) {
                            hashSinks( fieldIndex );
                        }
                        Object value = literalConstraint.getField();
                        // no need to check, we know  the sink  does not exist
                        hashedSinkMap.put( new HashKey( index,
                                                        value ),
                                           sink,
                                           false );
                    } else {
                        if ( this.hashableSinks == null ) {
                            this.hashableSinks = new ObjectSinkNodeList();
                        }
                        this.hashableSinks.add( (ObjectSinkNode) sink );
                    }
                    return;
                }

            }
        }

        if ( this.otherSinks == null ) {
            this.otherSinks = new ObjectSinkNodeList();
        }

        this.otherSinks.add( (ObjectSinkNode) sink );
    }

    public void removeObjectSink(ObjectSink sink) {
        if ( sink.getClass() == AlphaNode.class ) {
            AlphaNode alphaNode = (AlphaNode) sink;
            AlphaNodeFieldConstraint fieldConstraint = alphaNode.getConstraint();

            if ( fieldConstraint.getClass() == LiteralConstraint.class ) {
                LiteralConstraint literalConstraint = (LiteralConstraint) fieldConstraint;
                Evaluator evaluator = literalConstraint.getEvaluator();
                Object value = literalConstraint.getField();

                if ( evaluator.getOperator() == Operator.EQUAL ) {
                    int index = literalConstraint.getFieldExtractor().getIndex();
                    FieldIndex fieldIndex = unregisterFieldIndex( index );

                    if ( fieldIndex.isHashed() ) {
                        this.hashKey.setIndex( index );
                        this.hashKey.setValue( value );
                        hashedSinkMap.remove( this.hashKey );
                        if ( fieldIndex.getCount() <= 2 ) {
                            // we have less than three so unhash
                            unHashSinks( fieldIndex );
                        }
                    } else {
                        this.hashableSinks.remove( (ObjectSinkNode) sink );
                    }

                    if ( this.hashableSinks.isEmpty() ) {
                        this.hashableSinks = null;
                    }

                    return;
                }
            }
        }

        this.otherSinks.remove( (ObjectSinkNode) sink );

        if ( this.otherSinks.isEmpty() ) {
            this.otherSinks = null;
        }
    }

    public void hashSinks(FieldIndex fieldIndex) {
        int index = fieldIndex.getIndex();

        List list = new ArrayList();

        if ( this.hashedSinkMap == null ) {
            this.hashedSinkMap = new ObjectHashMap();
        }

        for ( ObjectSinkNode sink = this.hashableSinks.getFirst(); sink != null; sink = sink.getNextObjectSinkNode() ) {
            AlphaNode alphaNode = (AlphaNode) sink;
            AlphaNodeFieldConstraint fieldConstraint = alphaNode.getConstraint();
            LiteralConstraint literalConstraint = (LiteralConstraint) fieldConstraint;
            Evaluator evaluator = literalConstraint.getEvaluator();
            if ( evaluator.getOperator() == Operator.EQUAL && index == literalConstraint.getFieldExtractor().getIndex() ) {
                Object value = literalConstraint.getField();
                list.add( sink );
                hashedSinkMap.put( new HashKey( index,
                                                value ),
                                   sink );
            }
        }

        for ( java.util.Iterator it = list.iterator(); it.hasNext(); ) {
            ObjectSinkNode sink = (ObjectSinkNode) it.next();
            this.hashableSinks.remove( sink );
        }

        if ( this.hashableSinks.isEmpty() ) {
            this.hashableSinks = null;
        }

        fieldIndex.setHashed( true );
    }

    public void unHashSinks(FieldIndex fieldIndex) {
        int index = fieldIndex.getIndex();

        for ( ObjectSinkNode sink = this.hashableSinks.getFirst(); sink != null; sink = sink.getNextObjectSinkNode() ) {
            AlphaNode alphaNode = (AlphaNode) sink;
            AlphaNodeFieldConstraint fieldConstraint = alphaNode.getConstraint();
            LiteralConstraint literalConstraint = (LiteralConstraint) fieldConstraint;
            Evaluator evaluator = literalConstraint.getEvaluator();
            if ( evaluator.getOperator() == Operator.EQUAL && index == literalConstraint.getFieldExtractor().getIndex() ) {
                Object value = literalConstraint.getField();
                this.hashKey.setIndex( index );
                this.hashKey.setValue( value );
                this.hashableSinks.add( sink );
                hashedSinkMap.remove( this.hashKey );
            }
        }

        if ( this.hashedSinkMap.isEmpty() ) {
            this.hashedSinkMap = null;
        }

        fieldIndex.setHashed( false );
    }

    /**
     * Returns a FieldIndex which Keeps a count on how many times a particular field is used with an equality check in the sinks.
     * @param index
     * @param fieldExtractor
     * @return
     */
    private FieldIndex registerFieldIndex(int index,
                                          FieldExtractor fieldExtractor) {
        FieldIndex fieldIndex = null;

        // is linkedlist null, if so create and add
        if ( hashedFieldIndexes == null ) {
            this.hashedFieldIndexes = new LinkedList();
            fieldIndex = new FieldIndex( index,
                                         fieldExtractor );
            this.hashedFieldIndexes.add( fieldIndex );
        }

        // still null, so see if it already exists
        if ( fieldIndex == null ) {
            fieldIndex = findFieldIndex( index );
        }

        // doesn't exist so create it
        if ( fieldIndex == null ) {
            fieldIndex = new FieldIndex( index,
                                         fieldExtractor );
            this.hashedFieldIndexes.add( fieldIndex );
        }

        fieldIndex.increaseCounter();

        return fieldIndex;
    }

    private FieldIndex unregisterFieldIndex(int index) {
        FieldIndex fieldIndex = findFieldIndex( index );
        fieldIndex.decreaseCounter();

        // if the fieldcount is 0 then remove it from the linkedlist
        if ( fieldIndex.getCount() == 0 ) {
            this.hashedFieldIndexes.remove( fieldIndex );

            // if the linkedlist is empty then null it
            if ( this.hashedFieldIndexes.isEmpty() ) {
                this.hashedFieldIndexes = null;
            }
        }

        return fieldIndex;
    }

    private FieldIndex findFieldIndex(int index) {
        for ( FieldIndex node = (FieldIndex) this.hashedFieldIndexes.getFirst(); node != null; node = (FieldIndex) node.getNext() ) {
            if ( node.getIndex() == index ) {
                return node;
            }
        }

        return null;
    }

    public void propagateAssertObject(InternalFactHandle handle,
                                      PropagationContext context,
                                      InternalWorkingMemory workingMemory) {
        Object object = handle.getObject();
        if ( this.hashedFieldIndexes != null ) {
            // Iterate the FieldIndexes to see if any are hashed        
            for ( FieldIndex fieldIndex = (FieldIndex) this.hashedFieldIndexes.getFirst(); fieldIndex != null && fieldIndex.isHashed(); fieldIndex = (FieldIndex) fieldIndex.getNext() ) {
                // this field is hashed so set the existing hashKey and see if there is a sink for it
                int index = fieldIndex.getIndex();
                FieldExtractor extractor = fieldIndex.getFieldExtactor();
                this.hashKey.setIndex( index );
                this.hashKey.setValue( extractor.getValue( object ) );
                ObjectSink sink = (ObjectSink) this.hashedSinkMap.get( this.hashKey );
                if ( sink != null ) {
                    // The sink exists so propagate
                    sink.assertObject( handle,
                                       context,
                                       workingMemory );
                }
            }
        }

        // propagate unhashed
        if ( this.hashableSinks != null ) {
            for ( ObjectSinkNode sink = this.hashableSinks.getFirst(); sink != null; sink = sink.getNextObjectSinkNode() ) {
                sink.assertObject( handle,
                                   context,
                                   workingMemory );
            }
        }

        if ( this.otherSinks != null ) {
            // propagate others
            for ( ObjectSinkNode sink = this.otherSinks.getFirst(); sink != null; sink = sink.getNextObjectSinkNode() ) {
                sink.assertObject( handle,
                                   context,
                                   workingMemory );
            }
        }

    }

    public void propagateRetractObject(InternalFactHandle handle,
                                       PropagationContext context,
                                       InternalWorkingMemory workingMemory,
                                       boolean useHash) {
        if ( this.hashedFieldIndexes != null ) {
            if ( useHash && this.hashedSinkMap != null ) {
                Object object = handle.getObject();
                // Iterate the FieldIndexes to see if any are hashed        
                for ( FieldIndex fieldIndex = (FieldIndex) this.hashedFieldIndexes.getFirst(); fieldIndex != null && fieldIndex.isHashed(); fieldIndex = (FieldIndex) fieldIndex.getNext() ) {
                    // this field is hashed so set the existing hashKey and see if there is a sink for it
                    int index = fieldIndex.getIndex();
                    FieldExtractor extractor = fieldIndex.getFieldExtactor();
                    this.hashKey.setIndex( index );
                    this.hashKey.setValue( extractor.getValue( object ) );
                    ObjectSink sink = (ObjectSink) this.hashedSinkMap.get( this.hashKey );
                    if ( sink != null ) {
                        // The sink exists so propagate
                        sink.retractObject( handle,
                                            context,
                                            workingMemory );
                    }
                }
            } else if ( this.hashedSinkMap != null ) {
                Iterator it = this.hashedSinkMap.iterator();
                for ( ObjectEntry entry = (ObjectEntry) it.next(); entry != null; entry = (ObjectEntry) it.next() ) {
                    ObjectSink sink = (ObjectSink) entry.getValue();
                    sink.retractObject( handle,
                                        context,
                                        workingMemory );
                }
            }
        }

        if ( this.hashableSinks != null ) {
            // we can't retrieve hashed sinks, as the field value might have changed, so we have to iterate and propagate to all hashed sinks
            for ( ObjectSinkNode sink = this.hashableSinks.getFirst(); sink != null; sink = sink.getNextObjectSinkNode() ) {
                sink.retractObject( handle,
                                    context,
                                    workingMemory );
            }
        }

        if ( this.otherSinks != null ) {
            // propagate others
            for ( ObjectSinkNode sink = this.otherSinks.getFirst(); sink != null; sink = sink.getNextObjectSinkNode() ) {
                sink.retractObject( handle,
                                    context,
                                    workingMemory );
            }
        }
    }

    public ObjectSink[] getSinks() {
        List  list = new ArrayList();
        
        if ( this.otherSinks != null ) {
            for ( ObjectSinkNode sink = this.otherSinks.getFirst(); sink != null; sink = sink.getNextObjectSinkNode() ) {
                list.add( sink );
            }
        }
        
        if ( this.hashableSinks != null ) {
            for ( ObjectSinkNode sink = this.hashableSinks.getFirst(); sink != null; sink = sink.getNextObjectSinkNode() ) {
                list.add( sink );
            }
        } 
        
        if ( this.hashedSinkMap != null ) {
            Iterator it = this.hashedSinkMap.iterator();
            for ( ObjectEntry entry = (ObjectEntry) it.next(); entry != null; entry = (ObjectEntry) it.next() ) {
                ObjectSink sink = (ObjectSink) entry.getValue();
                list.add( sink );
            }
        }

        return ( ObjectSink[] ) list.toArray( new ObjectSink[ list.size() ] );
    }

    public int size() {
        return this.otherSinks.size();
    }

    public static class HashKey {
        private int    index;
        private Object value;

        public HashKey() {

        }

        public HashKey(int index,
                       Object value) {
            super();
            this.index = index;
            this.value = value;
        }

        public int getIndex() {
            return this.index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Object getValue() {
            return this.value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + this.index;
            result = PRIME * result + ((this.value == null) ? 0 : this.value.hashCode());
            return result;
        }

        public boolean equals(Object object) {
            final HashKey other = (HashKey) object;

            return this.index == other.index && this.value.equals( other.value );
        }

    }

    public static class FieldIndex
        implements
        LinkedListNode {
        private final int      index;
        private FieldExtractor fieldExtactor;

        private int            count;

        private boolean        hashed;

        private LinkedListNode previous;
        private LinkedListNode next;

        public FieldIndex(int index,
                          FieldExtractor fieldExtractor) {
            this.index = index;
            this.fieldExtactor = fieldExtractor;
        }

        public FieldExtractor getFieldExtractor() {
            return this.fieldExtactor;
        }

        public int getIndex() {
            return this.index;
        }

        public int getCount() {
            return this.count;
        }

        public FieldExtractor getFieldExtactor() {
            return this.fieldExtactor;
        }

        public boolean isHashed() {
            return this.hashed;
        }

        public void setHashed(boolean hashed) {
            this.hashed = hashed;
        }

        public void increaseCounter() {
            this.count++;
        }

        public void decreaseCounter() {
            this.count--;
        }

        public LinkedListNode getNext() {
            return this.next;
        }

        public LinkedListNode getPrevious() {
            return this.previous;
        }

        public void setNext(LinkedListNode next) {
            this.next = next;

        }

        public void setPrevious(LinkedListNode previous) {
            this.previous = previous;
        }
    }
}
