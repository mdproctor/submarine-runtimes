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

package org.drools.compiler.cdi.example;

import org.kie.internal.KnowledgeBase;


public class TestClassImpl implements TestClass {
    
    public TestClassImpl() {
        
    }

    private //@Inject @KBase("org.droosl.test1")  
            KnowledgeBase kBase1;
    
    public KnowledgeBase getKBase1() {
        return kBase1;
    }
}
