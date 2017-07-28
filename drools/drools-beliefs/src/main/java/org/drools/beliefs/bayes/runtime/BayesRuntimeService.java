/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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

package org.drools.beliefs.bayes.runtime;

import org.drools.core.common.InternalKnowledgeRuntime;
import org.kie.api.internal.runtime.KieRuntimeService;
import org.kie.api.runtime.KieRuntime;

public class BayesRuntimeService implements KieRuntimeService<BayesRuntime> {
    @Override
    public BayesRuntime newKieRuntime(KieRuntime session ) {
        return new BayesRuntimeImpl( (InternalKnowledgeRuntime) session );
    }

    @Override
    public Class getServiceInterface() {
        return BayesRuntime.class;
    }
}
