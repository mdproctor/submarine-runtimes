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

package org.drools.examples.manners;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class LastSeat
    implements
    Externalizable {
    /**
     *
     */
    private static final long serialVersionUID = 510l;
    private int               seat;

    public LastSeat() {
    }

    public LastSeat(final int seat) {
        this.seat = seat;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        seat    = in.readInt();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(seat);
    }

    public int getSeat() {
        return this.seat;
    }

    public String toString() {
        return "[LastSeat seat=" + this.seat + "]";
    }
}
