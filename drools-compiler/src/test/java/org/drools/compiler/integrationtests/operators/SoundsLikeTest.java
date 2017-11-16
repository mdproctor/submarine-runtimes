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

package org.drools.compiler.integrationtests.operators;

import java.util.stream.Stream;

import org.drools.compiler.CommonTestMethodBase;
import org.drools.compiler.Person;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

import static org.junit.Assert.assertEquals;

public class SoundsLikeTest extends CommonTestMethodBase {

    @Test
    public void testSoundsLike() {
        // JBRULES-2991: Operator soundslike is broken

        testFiredRules("package org.drools.compiler\n" +
                               "rule SoundsLike\n" +
                               "when\n" +
                               "    Person( name soundslike \"Bob\" )\n" +
                               "then\n" +
                               "end",
                       1,
                       "Mark",
                       "Bob");
    }

    @Test
    public void testSoundsLikeNegativeCase() {
        // JBRULES-2991: Operator soundslike is broken

        testFiredRules("package org.drools.compiler\n" +
                               "rule SoundsLike\n" +
                               "when\n" +
                               "    Person( name soundslike \"Bob\" )\n" +
                               "then\n" +
                               "end",
                       0,
                       "Mark");
    }

    @Test
    public void testNotSoundsLike() {
        // JBRULES-2991: Operator soundslike is broken

        testFiredRules("package org.drools.compiler\n" +
                               "rule NotSoundsLike\n" +
                               "when\n" +
                               "    Person( name not soundslike \"Bob\" )\n" +
                               "then\n" +
                               "end",
                       1,
                       "John");
    }

    @Test
    public void testNotSoundsLikeNegativeCase() {
        // JBRULES-2991: Operator soundslike is broken

        testFiredRules("package org.drools.compiler\n" +
                               "rule NotSoundsLike\n" +
                               "when\n" +
                               "    Person( name not soundslike \"Bob\" )\n" +
                               "then\n" +
                               "end",
                       0,
                       "Bob");
    }

    private void testFiredRules(final String rule,
                                final int firedRulesCount,
                                final String... persons) {
        final KieBase kbase = loadKnowledgeBaseFromString(rule);
        final KieSession ksession = createKnowledgeSession(kbase);

        Stream.of(persons).forEach(person -> ksession.insert(new Person(person)));

        final int rules = ksession.fireAllRules();
        assertEquals(firedRulesCount,
                     rules);
    }
}
