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

package org.kie.dmn.signavio.feel.runtime.functions;

import java.math.BigDecimal;
import java.util.List;

import org.kie.dmn.feel.runtime.functions.BaseFEELFunction;
import org.kie.dmn.feel.runtime.functions.BuiltInFunctions;
import org.kie.dmn.feel.runtime.functions.FEELFnResult;
import org.kie.dmn.feel.runtime.functions.MeanFunction;
import org.kie.dmn.feel.runtime.functions.ParameterName;

public class AvgFunction
        extends BaseFEELFunction {

    public AvgFunction() {
        super("avg");
    }

    public FEELFnResult<BigDecimal> invoke(@ParameterName( "list" ) List list) {
        return BuiltInFunctions.getFunction(MeanFunction.class).invoke(list);
    }

    public FEELFnResult<BigDecimal> invoke(@ParameterName("list") Number single) {
        return BuiltInFunctions.getFunction(MeanFunction.class).invoke(single);
    }

    public FEELFnResult<BigDecimal> invoke(@ParameterName("n") Object[] list) {
        return BuiltInFunctions.getFunction(MeanFunction.class).invoke(list);
    }
}
