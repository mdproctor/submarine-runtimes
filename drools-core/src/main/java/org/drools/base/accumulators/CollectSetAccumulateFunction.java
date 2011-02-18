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

package org.drools.base.accumulators;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>An implementation of an accumulator capable of collecting sets of values.
 * This is similar to the "collect" CE, but allows us to collect any value, not
 * only facts.</p>
 * 
 * <p>Example:</p>
 * <pre>
 * rule "Set of unique employee names"
 * when
 *     $names : Set() from accumulate(
 *             Employee( $n : firstName, $l : lastName ),
 *             collectSet( $n + " " + $l ) )
 * then
 *     // do something
 * end
 * </pre>
 * 
 * <p>The set obviously does not computes duplications and the order of the elements in the set is not
 * guaranteed.</p>
 */
public class CollectSetAccumulateFunction
    implements
    AccumulateFunction {

    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        // functions are stateless, so nothing to serialize
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        // functions are stateless, so nothing to serialize
    }

    public static class CollectListData
        implements
        Externalizable {
        public Map< Object, MutableInt > map = new HashMap<Object, MutableInt>();

        public CollectListData() {
        }

        @SuppressWarnings("unchecked")
        public void readExternal(ObjectInput in) throws IOException,
                                                ClassNotFoundException {
            map = (Map< Object, MutableInt >) in.readObject();
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject( map );
        }
        
        public static class MutableInt implements Serializable {
            private static final long serialVersionUID = 510l;
            public int value = 0;
        }

    }

    /* (non-Javadoc)
     * @see org.drools.base.accumulators.AccumulateFunction#createContext()
     */
    public Serializable createContext() {
        return new CollectListData();
    }

    /* (non-Javadoc)
     * @see org.drools.base.accumulators.AccumulateFunction#init(java.lang.Object)
     */
    public void init(Serializable context) throws Exception {
        CollectListData data = (CollectListData) context;
        data.map.clear();
    }

    /* (non-Javadoc)
     * @see org.drools.base.accumulators.AccumulateFunction#accumulate(java.lang.Object, java.lang.Object)
     */
    public void accumulate(Serializable context,
                           Object value) {
        CollectListData data = (CollectListData) context;
        CollectListData.MutableInt counter = data.map.get( value );
        if( counter == null ) {
            counter = new CollectListData.MutableInt();
            data.map.put( value, counter );
        }
        counter.value++;
    }

    /* (non-Javadoc)
     * @see org.drools.base.accumulators.AccumulateFunction#reverse(java.lang.Object, java.lang.Object)
     */
    public void reverse(Serializable context,
                        Object value) throws Exception {
        CollectListData data = (CollectListData) context;
        CollectListData.MutableInt counter = data.map.get( value );
        if( (--counter.value) == 0 ) {
            data.map.remove( value );
        }
    }

    /* (non-Javadoc)
     * @see org.drools.base.accumulators.AccumulateFunction#getResult(java.lang.Object)
     */
    public Object getResult(Serializable context) throws Exception {
        CollectListData data = (CollectListData) context;
        return Collections.unmodifiableSet( data.map.keySet() );
    }

    /* (non-Javadoc)
     * @see org.drools.base.accumulators.AccumulateFunction#supportsReverse()
     */
    public boolean supportsReverse() {
        return true;
    }

}
