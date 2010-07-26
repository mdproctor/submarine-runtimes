/**
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

package org.drools.base;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.drools.WorkingMemory;
import org.drools.rule.Rule;
import org.drools.spi.Enabled;
import org.drools.spi.Tuple;

public class EnabledBoolean
    implements
    Enabled,
    Externalizable {

    /**
     *
     */
    private static final long   serialVersionUID = 400L;

    public static final Enabled ENABLED_TRUE  = new EnabledBoolean( true );
    public static final Enabled ENABLED_FALSE  = new EnabledBoolean( false );

    private boolean             value;

    public EnabledBoolean() {
    }

    public EnabledBoolean(boolean value) {
        this.value = value;
    }

    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        value = in.readBoolean();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeBoolean( value );
    }

    public boolean getValue(final Tuple tuple,
                            final Rule rule,
                            final WorkingMemory workingMemory) {
        return this.value;
    }

    public String toString() {
        return String.valueOf( this.value );
    }

}
