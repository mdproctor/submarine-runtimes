/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.core.beliefsystem.defeasible;

import org.drools.core.beliefsystem.simple.SimpleLogicalDependency;
import org.drools.core.common.LogicalDependency;
import org.drools.core.spi.Activation;
import org.drools.core.util.LinkedListEntry;
import org.kie.internal.runtime.beliefs.Mode;

public class DefeasibleLogicalDependency<M extends DefeasibleMode<M>> extends SimpleLogicalDependency<M> {

    public DefeasibleLogicalDependency(Activation<M> justifier, Object justified, Object object, M mode) {
        super(justifier, justified, object, mode);
    }

}
