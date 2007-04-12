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

package org.drools.lang.descr;

import java.util.Collections;
import java.util.List;

/**
 * A descr class for accumulate node
 */
public class AccumulateDescr extends BaseDescr
    implements
    ConditionalElementDescr {

    private static final long serialVersionUID = 2831283873824863255L;

    private ColumnDescr       sourceColumn;
    private ColumnDescr       resultColumn;
    private String            initCode;
    private String            actionCode;
    private String            resultCode;
    private String[]          declarations;
    private String            classMethodName;

    AccumulateDescr() {
        super();
    }

    public int getLine() {
        return this.sourceColumn.getLine();
    }

    public void setSourceColumn(final ColumnDescr sourceColumn) {
        this.sourceColumn = sourceColumn;
    }

    public ColumnDescr getSourceColumn() {
        return this.sourceColumn;
    }

    public String getClassMethodName() {
        return this.classMethodName;
    }

    public void setClassMethodName(final String classMethodName) {
        this.classMethodName = classMethodName;
    }

    public String[] getDeclarations() {
        return this.declarations;
    }

    public void setDeclarations(final String[] declarations) {
        this.declarations = declarations;
    }

    public String getActionCode() {
        return this.actionCode;
    }

    public void setActionCode(final String actionCode) {
        this.actionCode = actionCode;
    }

    public String getInitCode() {
        return this.initCode;
    }

    public void setInitCode(final String initCode) {
        this.initCode = initCode;
    }

    public String getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(final String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultColumn(final ColumnDescr resultColumn) {
        this.resultColumn = resultColumn;
    }

    public ColumnDescr getResultColumn() {
        return this.resultColumn;
    }

    public String toString() {
        return "[Accumulate: id=" + this.resultColumn.getIdentifier() + "; objectType=" + this.resultColumn.getObjectType() + "]";
    }

    public void addDescr(final BaseDescr patternDescr) {
        // Nothing to do
    }

    public List getDescrs() {
        // nothing to do
        return Collections.EMPTY_LIST;
    }
}
