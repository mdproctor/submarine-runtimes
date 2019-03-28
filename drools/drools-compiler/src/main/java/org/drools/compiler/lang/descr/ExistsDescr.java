/*
 * Copyright 2005 Red Hat, Inc. and/or its affiliates.
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

package org.drools.compiler.lang.descr;

import java.util.ArrayList;
import java.util.List;

public class ExistsDescr extends AnnotatedBaseDescr
    implements
    ConditionalElementDescr {

    private static final long serialVersionUID = 510l;
    private final List        descrs           = new ArrayList( 1 );

    public ExistsDescr() { }

    public ExistsDescr(final BaseDescr baseDescr) {
        addDescr( baseDescr );
    }

    public void addDescr(final BaseDescr baseDescr) {
        this.descrs.add( baseDescr );
    }
    
    public void insertBeforeLast(final Class clazz ,final BaseDescr baseDescr ) {
        throw new UnsupportedOperationException( "Can't add descriptors to " + this.getClass().getName() );
    }

    public List getDescrs() {
        return this.descrs;
    }

    public boolean removeDescr(BaseDescr baseDescr) {
        return baseDescr == null ? false : descrs.remove(baseDescr);
    }

    public void addOrMerge(BaseDescr baseDescr) {
        if( baseDescr instanceof ExistsDescr ) {
            this.descrs.addAll( ((ExistsDescr)baseDescr).getDescrs() );
        } else {
            this.descrs.add( baseDescr );
        }
    }

    @Override
    public void accept(DescrVisitor visitor) {
        visitor.visit(this);
    }
}
