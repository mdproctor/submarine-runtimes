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

package org.drools.decisiontable.parser;

/**
 * This is for building up LHS and RHS code for a rule row.
 */
public interface SourceBuilder {
    ActionType.Code getActionTypeCode();
    String getResult();
    void addTemplate(int row, int col, String content);

    void addCellValue(int row, int col, String value);

    default void addCellValue(int row, int col, String value, boolean trim) {
        addCellValue( row, col, value );
    }

    void clearValues();
    boolean hasValues();
    int getColumn();
}
