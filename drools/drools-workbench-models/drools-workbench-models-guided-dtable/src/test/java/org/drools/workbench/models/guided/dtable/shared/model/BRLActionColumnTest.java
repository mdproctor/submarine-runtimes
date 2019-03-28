/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.drools.workbench.models.guided.dtable.shared.model;

import static org.drools.workbench.models.guided.dtable.shared.model.BRLActionColumn.FIELD_DEFINITION;
import static org.drools.workbench.models.guided.dtable.shared.model.BRLActionVariableColumn.FIELD_VAR_NAME;
import static org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52.FIELD_FIELD_TYPE;
import static org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52.FIELD_HEADER;
import static org.drools.workbench.models.guided.dtable.shared.model.DTColumnConfig52.FIELD_HIDE_COLUMN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.drools.workbench.models.datamodel.rule.ActionRetractFact;
import org.drools.workbench.models.datamodel.rule.IAction;
import org.junit.Before;
import org.junit.Test;

public class BRLActionColumnTest extends ColumnTestBase {

    private BRLActionColumn column1;
    private BRLActionColumn column2;

    @Before
    public void setup() {
        column1 = new BRLActionColumn();
        List<IAction> definition1 = Arrays.asList(new ActionRetractFact("var"));
        column1.setDefinition(definition1);
        List<BRLActionVariableColumn> childColumns1 = Arrays.asList(new BRLActionVariableColumn("var", "FieldType"));
        column1.setChildColumns(childColumns1);
        column1.setHeader("header");
        column1.setHideColumn(false);

        column2 = new BRLActionColumn();
        List<IAction> definition2 = Arrays.asList(new ActionRetractFact("var"));
        column2.setDefinition(definition2);
        List<BRLActionVariableColumn> childColumns2 = Arrays.asList(new BRLActionVariableColumn("var", "FieldType"));
        column2.setChildColumns(childColumns2);
        column2.setHeader("header");
        column2.setHideColumn(false);
    }

    @Test
    public void testDiffEmpty() {
        checkDiffEmpty(column1, column2);
    }

    @Test
    public void testDiffDefinitions() {
        List<IAction> definition1 = Arrays.asList(new ActionRetractFact("var1"));
        column1.setDefinition(definition1);
        List<IAction> definition2 = Arrays.asList(new ActionRetractFact("var2"));
        column2.setDefinition(definition2);

        checkSingleDiff(FIELD_DEFINITION, definition1, definition2, column1, column2);
    }

    @Test
    public void testDiffChildColumns() {
        List<BRLActionVariableColumn> childColumns1 = Arrays.asList(new BRLActionVariableColumn("var1", "FieldType1"));
        column1.setChildColumns(childColumns1);
        List<BRLActionVariableColumn> childColumns2 = Arrays.asList(new BRLActionVariableColumn("var2", "FieldType2"));
        column2.setChildColumns(childColumns2);

        List<BaseColumnFieldDiff> diff = column1.diff(column2);
        assertNotNull(diff);
        assertEquals(2, diff.size());
        assertEquals(FIELD_VAR_NAME, diff.get(0).getFieldName());
        assertEquals("var1", diff.get(0).getOldValue());
        assertEquals("var2", diff.get(0).getValue());
        assertEquals(FIELD_FIELD_TYPE, diff.get(1).getFieldName());
        assertEquals("FieldType1", diff.get(1).getOldValue());
        assertEquals("FieldType2", diff.get(1).getValue());
    }

    @Test
    public void testDiffAll() {
        List<IAction> definition1 = Arrays.asList(new ActionRetractFact("var1"));
        column1.setDefinition(definition1);
        List<BRLActionVariableColumn> childColumns1 = Arrays.asList(new BRLActionVariableColumn("var1", "FieldType1"));
        column1.setChildColumns(childColumns1);
        column1.setHeader("header1");
        column1.setHideColumn(false);

        List<IAction> definition2 = Arrays.asList(new ActionRetractFact("var2"));
        column2.setDefinition(definition2);
        List<BRLActionVariableColumn> childColumns2 = Arrays.asList(new BRLActionVariableColumn("var2", "FieldType2"));
        column2.setChildColumns(childColumns2);
        column2.setHeader("header2");
        column2.setHideColumn(true);

        List<BaseColumnFieldDiff> diff = column1.diff(column2);
        assertNotNull(diff);
        assertEquals(5, diff.size());
        assertEquals(FIELD_HIDE_COLUMN, diff.get(0).getFieldName());
        assertEquals(false, diff.get(0).getOldValue());
        assertEquals(true, diff.get(0).getValue());
        assertEquals(FIELD_HEADER, diff.get(1).getFieldName());
        assertEquals("header1", diff.get(1).getOldValue());
        assertEquals("header2", diff.get(1).getValue());
        assertEquals(FIELD_DEFINITION, diff.get(2).getFieldName());
        assertEquals(definition1, diff.get(2).getOldValue());
        assertEquals(definition2, diff.get(2).getValue());
        assertEquals(FIELD_VAR_NAME, diff.get(3).getFieldName());
        assertEquals("var1", diff.get(3).getOldValue());
        assertEquals("var2", diff.get(3).getValue());
        assertEquals(FIELD_FIELD_TYPE, diff.get(4).getFieldName());
        assertEquals("FieldType1", diff.get(4).getOldValue());
        assertEquals("FieldType2", diff.get(4).getValue());
    }
}