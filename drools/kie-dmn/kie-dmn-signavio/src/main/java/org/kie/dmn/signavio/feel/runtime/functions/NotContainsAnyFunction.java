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

import java.util.List;

import org.kie.dmn.api.feel.runtime.events.FEELEvent.Severity;
import org.kie.dmn.feel.runtime.events.InvalidParametersEvent;
import org.kie.dmn.feel.runtime.functions.BaseFEELFunction;
import org.kie.dmn.feel.runtime.functions.FEELFnResult;
import org.kie.dmn.feel.runtime.functions.ParameterName;

public class NotContainsAnyFunction
        extends BaseFEELFunction {

    public NotContainsAnyFunction() {
        super("notContainsAny");
    }

    public FEELFnResult<Boolean> invoke(@ParameterName("list1") List list1, @ParameterName("list2") List list2) {
        if (list1 == null) {
            return FEELFnResult.ofError(new InvalidParametersEvent(Severity.ERROR, "list1", "cannot be null"));
        }

        boolean result = true;
        for (Object element : list2) {
            result = (!list1.contains(element)) && result;

            // optimization: terminate early if any element is actually contained.
            if (!result) {
                return FEELFnResult.ofResult(result);
            }
        }

        return FEELFnResult.ofResult( result );
    }
}
