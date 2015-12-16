/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.drools.workbench.models.guided.dtable.shared.auditlog;

import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;

/**
 * Details of a Condition column
 */
public class ConditionColumnDetails extends ColumnDetails {

    private static final long serialVersionUID = -4296771936396335786L;

    private String factField;
    private String operator;

    public ConditionColumnDetails() {
    }

    public ConditionColumnDetails( final ConditionCol52 column ) {
        super( column );
        this.factField = column.getFactField();
        this.operator = column.getOperator();
    }

    public String getFactField() {
        return factField;
    }

    public String getOperator() {
        return operator;
    }

}
