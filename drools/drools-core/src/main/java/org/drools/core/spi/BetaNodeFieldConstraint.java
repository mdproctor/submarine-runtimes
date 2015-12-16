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

package org.drools.core.spi;

import org.drools.core.common.InternalFactHandle;
import org.drools.core.rule.ContextEntry;

public interface BetaNodeFieldConstraint
    extends
    Constraint {

    boolean isAllowedCachedLeft(ContextEntry context,
                                InternalFactHandle handle);

    boolean isAllowedCachedRight(Tuple tuple,
                                 ContextEntry context);

    ContextEntry createContextEntry();

    /**
     * Clone this constraints only if it is already used by a different node, otherwise returns this
     */
    BetaNodeFieldConstraint cloneIfInUse();
}
