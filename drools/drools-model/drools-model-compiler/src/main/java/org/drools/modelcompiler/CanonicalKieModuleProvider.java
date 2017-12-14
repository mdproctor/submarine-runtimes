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

package org.drools.modelcompiler;

import java.io.File;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.InternalKieModuleProvider;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieModuleModel;

public class CanonicalKieModuleProvider extends InternalKieModuleProvider.DrlBasedKieModuleProvider implements InternalKieModuleProvider {

    @Override
    public InternalKieModule createKieModule( ReleaseId releaseId, KieModuleModel kieProject, File file ) {
        InternalKieModule internalKieModule = super.createKieModule( releaseId, kieProject, file );
        return internalKieModule.hasResource( CanonicalKieModule.MODEL_FILE ) ? new CanonicalKieModule( internalKieModule ) : internalKieModule;
    }
}
