/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.drools.compiler.compiler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.kie.api.Service;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.internal.builder.KnowledgeBuilderResult;

public interface PMMLCompiler extends Service {

    String compile( InputStream stream, ClassLoader classLoader );
    
    Map<String,String> getJavaClasses(InputStream stream);
    
    Map<String,String> getJavaClasses(String fileName);
    
    List<PMMLResource> precompile( InputStream stream, ClassLoader classLoader, KieBaseModel rootModel);
    
    List<PMMLResource> precompile( String fileName, ClassLoader classLoader, KieBaseModel rootModel);

    List<KnowledgeBuilderResult> getResults();
    
    String getCompilerVersion();
    

    void clearResults();
}
