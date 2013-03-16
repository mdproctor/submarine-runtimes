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

package org.drools.core.common;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.drools.reteoo.LeftTuple;
import org.drools.rule.ContextEntry;
import org.drools.rule.Declaration;
import org.drools.core.spi.BetaNodeFieldConstraint;

/**
 * Checks if one tuple is the start subtuple of other tuple.
 * For instance, if we have two tuples:
 *
 * T1 = [ a, b, c ]
 * T2 = [ a, b, c, d, e]
 *
 * This constraint will evaluate to true as T1 is the starting subtuple
 * of T2. On the other hand, if we have:
 *
 * T1 = [ a, c, b ]
 * T2 = [ a, b, c, d, e ]
 *
 * This constraint will evaluate to false, as T1 is not the starting subtuple
 * of T2. Besides having the same elements, the order is different.
 *
 * This constraint is used when joining subnetworks back into the main
 * network.
 */
public class TupleStartEqualsConstraint
    implements
    BetaNodeFieldConstraint {

    private static final long                       serialVersionUID = 510l;

    private Declaration[]                     declarations     = new Declaration[0];

    private static final TupleStartEqualsConstraint INSTANCE         = new TupleStartEqualsConstraint();

    // this is a stateless constraint, so we can make it a singleton
    public TupleStartEqualsConstraint() {
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        declarations  = (Declaration[])in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(declarations);
    }

    public static TupleStartEqualsConstraint getInstance() {
        return INSTANCE;
    }

    public Declaration[] getRequiredDeclarations() {
        return this.declarations;
    }

    public void replaceDeclaration(Declaration oldDecl,
                                   Declaration newDecl) {
    }

    public boolean isTemporal() {
        return false;
    }

    public ContextEntry createContextEntry() {
        return new TupleStartEqualsConstraintContextEntry();
    }

    public boolean isAllowedCachedLeft(final ContextEntry context,
                                       final InternalFactHandle handle) {
        // object MUST be a ReteTuple
        final LeftTuple tuple = ((LeftTuple) handle.getObject()).getSubTuple( ((TupleStartEqualsConstraintContextEntry) context).compareSize );
        return ((TupleStartEqualsConstraintContextEntry) context).left.equals( tuple );
    }

    public boolean isAllowedCachedRight(final LeftTuple tuple,
                                        final ContextEntry context) {
        return tuple.equals( ((TupleStartEqualsConstraintContextEntry) context).right.getSubTuple( tuple.size() ) );
    }

    public String toString() {
        return "[ TupleStartEqualsConstraint ]";
    }

    public int hashCode() {
        return 10;
    }

    public boolean equals(final Object object) {
        if ( object instanceof TupleStartEqualsConstraint ) {
            return true;
        }
        return false;
    }

    public TupleStartEqualsConstraint clone() {
        return INSTANCE;
    }

    public static class TupleStartEqualsConstraintContextEntry
        implements
        ContextEntry {

        private static final long serialVersionUID = 510l;

        public LeftTuple          left;
        public LeftTuple          right;

        // the size of the tuple to compare
        public int                compareSize;

        private ContextEntry      entry;

        public TupleStartEqualsConstraintContextEntry() {
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            left        = (LeftTuple)in.readObject();
            right       = (LeftTuple)in.readObject();
            compareSize = in.readInt();
            entry       = (ContextEntry)in.readObject();
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(left);
            out.writeObject(right);
            out.writeInt(compareSize);
            out.writeObject(entry);
        }

        public ContextEntry getNext() {
            return this.entry;
        }

        public void setNext(final ContextEntry entry) {
            this.entry = entry;
        }

        public void updateFromTuple(final InternalWorkingMemory workingMemory,
                                    final LeftTuple tuple) {
            this.left = tuple;
            this.compareSize = tuple.size();
        }

        public void updateFromFactHandle(final InternalWorkingMemory workingMemory,
                                         final InternalFactHandle handle) {
            // if it is not a rete tuple, then there is a bug in the engine...
            // it MUST be a rete tuple
            this.right = (LeftTuple) handle.getObject();
        }

        public void resetTuple() {
            this.left = null;
        }

        public void resetFactHandle() {
            this.right = null;
        }
    }

    public ConstraintType getType() {
        return ConstraintType.BETA;
    }
}
