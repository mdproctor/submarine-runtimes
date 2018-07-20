/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.dmn.model.v1_1;

import java.util.ArrayList;
import java.util.List;

public class DecisionService extends NamedElement {

    /**
     * This is not defined in the v1.1 XSD but used in this pojo for full backport of Decision Service onto v1.1 runtime. 
     */
    private InformationItem variable;
    private List<DMNElementReference> outputDecision;
    private List<DMNElementReference> encapsulatedDecision;
    private List<DMNElementReference> inputDecision;
    private List<DMNElementReference> inputData;

    public InformationItem getVariable() {
        return variable;
    }

    public void setVariable(InformationItem variable) {
        this.variable = variable;
    }

    public List<DMNElementReference> getOutputDecision() {
        if ( outputDecision == null ) {
            outputDecision = new ArrayList<>();
        }
        return this.outputDecision;
    }

    public List<DMNElementReference> getEncapsulatedDecision() {
        if ( encapsulatedDecision == null ) {
            encapsulatedDecision = new ArrayList<>();
        }
        return this.encapsulatedDecision;
    }

    public List<DMNElementReference> getInputDecision() {
        if ( inputDecision == null ) {
            inputDecision = new ArrayList<>();
        }
        return this.inputDecision;
    }

    public List<DMNElementReference> getInputData() {
        if ( inputData == null ) {
            inputData = new ArrayList<>();
        }
        return this.inputData;
    }

    @Override
    public String toString() {
        return getName();
    }

}
