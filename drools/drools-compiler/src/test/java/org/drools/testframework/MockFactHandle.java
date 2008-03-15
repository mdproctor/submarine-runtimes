package org.drools.testframework;

import org.drools.FactHandle;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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

public class MockFactHandle
    implements
    FactHandle {
    /**
     *
     */
    private static final long serialVersionUID = 400L;
    private int               id;

    public MockFactHandle() {

    }

    public MockFactHandle(final int id) {
        this.id = id;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        id  = in.readInt();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(id);
    }


    public String toExternalForm() {
        return "[fact:" + this.id + "]";
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(final Object object) {
        if ( this == object ) {
            return true;
        }

        if ( object == null || getClass() != object.getClass() ) {
            return false;
        }

        return ((MockFactHandle) object).id == this.id;
    }

    public long getId() {
        return this.id;
    }

    public long getRecency() {
        // TODO Auto-generated method stub
        return 0;
    }
}