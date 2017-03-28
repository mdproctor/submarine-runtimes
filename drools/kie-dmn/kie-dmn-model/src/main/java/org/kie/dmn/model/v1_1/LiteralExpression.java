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

public class LiteralExpression extends Expression {

    private String text;
    private ImportedValues importedValues;
    private String expressionLanguage;

    public String getText() {
        return text;
    }

    public void setText( final String value ) {
        this.text = value;
    }

    public ImportedValues getImportedValues() {
        return importedValues;
    }

    public void setImportedValues( final ImportedValues value ) {
        this.importedValues = value;
    }

    public String getExpressionLanguage() {
        return expressionLanguage;
    }

    public void setExpressionLanguage( final String value ) {
        this.expressionLanguage = value;
    }

}
