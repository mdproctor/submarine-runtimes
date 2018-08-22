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

package org.kie.dmn.backend.marshalling.v1_2.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.kie.dmn.model.api.DMNModelInstrumentedBase;
import org.kie.dmn.model.api.Expression;
import org.kie.dmn.model.api.FunctionDefinition;
import org.kie.dmn.model.api.FunctionKind;
import org.kie.dmn.model.api.InformationItem;
import org.kie.dmn.model.v1_2.TFunctionDefinition;

public class FunctionDefinitionConverter extends ExpressionConverter {

    private static final String KIND = "kind";
    public static final String EXPRESSION = "expression";
    public static final String FORMAL_PARAMETER = "formalParameter";


    @Override
    protected void assignChildElement(Object parent, String nodeName, Object child) {
        FunctionDefinition fd = (FunctionDefinition) parent;
        
        if (FORMAL_PARAMETER.equals(nodeName)) {
            fd.getFormalParameter().add((InformationItem) child);
        } else if (child instanceof Expression) {
            fd.setExpression((Expression) child);
        } else {
            super.assignChildElement(parent, nodeName, child);
        }
    }

    @Override
    protected void assignAttributes(HierarchicalStreamReader reader, Object parent) {
        super.assignAttributes(reader, parent);

        FunctionDefinition i = (FunctionDefinition) parent;

        String kind = reader.getAttribute(KIND);
        if (kind != null) {
            i.setKind(FunctionKind.fromValue(kind));
        }

    }

    @Override
    protected void writeChildren(HierarchicalStreamWriter writer, MarshallingContext context, Object parent) {
        super.writeChildren(writer, context, parent);
        FunctionDefinition fd = (FunctionDefinition) parent;
        
        for (InformationItem fparam : fd.getFormalParameter()) {
            writeChildrenNode(writer, context, fparam, FORMAL_PARAMETER);
        }
        if (fd.getExpression() != null) writeChildrenNode(writer, context, fd.getExpression(), MarshallingUtils.defineExpressionNodeName(fd.getExpression()));
    }

    @Override
    protected void writeAttributes(HierarchicalStreamWriter writer, Object parent) {
        super.writeAttributes(writer, parent);
        
        FunctionDefinition fd = (FunctionDefinition) parent;
        writer.addAttribute(KIND, fd.getKind().toString());
    }

    public FunctionDefinitionConverter(XStream xstream) {
        super(xstream);
    }

    @Override
    protected DMNModelInstrumentedBase createModelObject() {
        return new TFunctionDefinition();
    }

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(TFunctionDefinition.class);
    }

}
