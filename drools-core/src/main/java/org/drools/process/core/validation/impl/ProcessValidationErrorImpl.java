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

package org.drools.process.core.validation.impl;

import org.drools.definition.process.Process;
import org.drools.process.core.validation.ProcessValidationError;

public class ProcessValidationErrorImpl implements ProcessValidationError {

    private Process process;
    private String message;
    
    public ProcessValidationErrorImpl(Process process, String message) {
        this.process = process;
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }

    public Process getProcess() {
        return process;
    }
    
    public String toString() {
        return "Process '" + process.getName() + "' [" + process.getId() + "]: " + getMessage();
    }

}
