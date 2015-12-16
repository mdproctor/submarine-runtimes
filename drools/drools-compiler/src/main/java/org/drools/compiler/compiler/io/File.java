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

package org.drools.compiler.compiler.io;

import java.io.IOException;
import java.io.InputStream;


public interface File extends Resource {      
    String getName();
    
    boolean exists();    

    InputStream getContents() throws IOException;
    
    void setContents(InputStream is) throws IOException;

    void create(InputStream is) throws IOException;
    
    Path getPath();
}
