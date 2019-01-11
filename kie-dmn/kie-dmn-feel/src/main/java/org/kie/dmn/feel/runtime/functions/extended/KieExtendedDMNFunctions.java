/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.kie.dmn.feel.runtime.functions.extended;

import java.util.stream.Stream;

import org.kie.dmn.feel.runtime.FEELFunction;
import org.kie.dmn.feel.runtime.functions.twovaluelogic.*;

/**
 * additional functions not part of the spec version 1.1
 */
public class KieExtendedDMNFunctions {

    protected static final FEELFunction[] FUNCTIONS = new FEELFunction[]{
                                                                         TimeFunction.INSTANCE,
                                                                         DateFunction.INSTANCE,
                                                                         DurationFunction.INSTANCE,

                                                                         // additional functions not part of the spec version 1.1
                                                                         new NowFunction(),
                                                                         new TodayFunction(),
                                                                         new AbsFunction(),
                                                                         ModuloFunction.INSTANCE,
                                                                         new ProductFunction(),
                                                                         new CodeFunction(),
                                                                         new InvokeFunction(),
                                                                         SplitFunction.INSTANCE,
                                                                         StddevFunction.INSTANCE,
                                                                         ModeFunction.INSTANCE,
                                                                         AbsFunction.INSTANCE,
                                                                         SqrtFunction.INSTANCE,
                                                                         LogFunction.INSTANCE,
                                                                         ExpFunction.INSTANCE,
                                                                         EvenFunction.INSTANCE,
                                                                         OddFunction.INSTANCE,
                                                                         MedianFunction.INSTANCE,

                                                                         // CQL based, two value logic functions
                                                                        NNAnyFunction.INSTANCE,
                                                                        NNAllFunction.INSTANCE,
                                                                        NNCountFunction.INSTANCE,
                                                                        NNMaxFunction.INSTANCE,
                                                                        NNMeanFunction.INSTANCE,
                                                                        NNMedianFunction.INSTANCE,
                                                                        NNMinFunction.INSTANCE,
                                                                        NNModeFunction.INSTANCE,
                                                                        NNStddevFunction.INSTANCE,
                                                                        NNSumFunction.INSTANCE
    };

    public static FEELFunction[] getFunctions() {
        return FUNCTIONS;
    }

    public static <T extends FEELFunction> T getFunction(Class<T> functionClazz) {
        return (T) Stream.of(FUNCTIONS).filter(f -> functionClazz.isAssignableFrom(f.getClass())).findFirst().get();
    }
}
