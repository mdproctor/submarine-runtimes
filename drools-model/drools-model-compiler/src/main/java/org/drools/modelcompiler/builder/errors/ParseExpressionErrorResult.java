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

package org.drools.modelcompiler.builder.errors;

import org.drools.compiler.compiler.DroolsError;
import com.github.javaparser.ast.expr.Expression;
import org.drools.constraint.parser.printer.PrintUtil;
import org.kie.internal.builder.ResultSeverity;

public class ParseExpressionErrorResult extends DroolsError {

    private Expression expr;

    public ParseExpressionErrorResult(Expression expr) {
        super();
        this.expr = expr;
    }

    @Override
    public ResultSeverity getSeverity() {
        return ResultSeverity.ERROR;
    }

    @Override
    public String getMessage() {
        return "Unable to Analyse Expression " + PrintUtil.printConstraint(expr) + ":";
    }

    @Override
    public int[] getLines() {
        return new int[0];
    }
}
