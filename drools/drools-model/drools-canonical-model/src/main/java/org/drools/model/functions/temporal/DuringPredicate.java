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

package org.drools.model.functions.temporal;

public class DuringPredicate extends AbstractTemporalPredicate {

    private final long startMinDev, startMaxDev;
    private final long endMinDev, endMaxDev;

    public DuringPredicate() {
        super(new Interval(1, Interval.MAX));
        this.startMinDev = 1;
        this.startMaxDev = Long.MAX_VALUE;
        this.endMinDev = 1;
        this.endMaxDev = Long.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "during" + interval;
    }

    @Override
    public boolean evaluate(long start1, long duration1, long end1, long start2, long duration2, long end2) {
        long distStart = start1 - start2;
        long distEnd = end2 - end1;
        return (distStart >= this.startMinDev && distStart <= this.startMaxDev && distEnd >= this.endMinDev && distEnd <= this.endMaxDev);
    }
}
