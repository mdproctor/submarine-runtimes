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

package org.drools.compiler.integrationtests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.drools.compiler.LongAddress;
import org.drools.compiler.Person;
import org.drools.compiler.util.debug.DebugList;
import org.drools.core.impl.InternalRuleUnitExecutor;
import org.drools.core.ruleunit.RuleUnitFactory;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Results;
import org.kie.api.definition.rule.UnitVar;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.rule.DataSource;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.RuleUnit;
import org.kie.api.runtime.rule.RuleUnitExecutor;
import org.kie.internal.builder.conf.PropertySpecificOption;
import org.kie.internal.utils.KieHelper;

import static java.util.Arrays.asList;
import static org.drools.core.ruleunit.RuleUnitUtil.getUnitName;
import static org.drools.core.util.ClassUtils.getCanonicalSimpleName;
import static org.junit.Assert.*;

public class RuleUnitTest {
	

    @Test
    public void testWithDataSource() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "import " + NotAdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( AdultUnit.class ) when\n" +
                "    Person(age >= 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");\n" +
                "end\n" +
                "rule NotAdult @Unit( NotAdultUnit.class ) when\n" +
                "    Person(age < 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is NOT adult\");\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "persons",
                                                             new Person( "Mario", 42 ),
                                                             new Person( "Marilena", 44 ),
                                                             new Person( "Sofia", 4 ) );

        // explicitly create unit
        assertEquals(2, executor.run( new AdultUnit(persons) ) );

        // let RuleUnitExecutor internally create and wire the unit instance
        assertEquals(1, executor.run( NotAdultUnit.class ) );
    }

    @Test
    public void testBindDataSource() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "import " + NotAdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( AdultUnit.class ) when\n" +
                "    Person(age >= 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");\n" +
                "end\n" +
                "rule NotAdult @Unit( NotAdultUnit.class ) when\n" +
                "    Person(age < 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is NOT adult\");\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = DataSource.create( new Person( "Mario", 42 ),
                                                        new Person( "Marilena", 44 ),
                                                        new Person( "Sofia", 4 ) );

        executor.bindVariable( "persons", persons );

        // explicitly create unit
        assertEquals(2, executor.run( AdultUnit.class ) );

        // let RuleUnitExecutor internally create and wire the unit instance
        assertEquals(1, executor.run( NotAdultUnit.class ) );
    }

    @Test
    public void testUnboundDataSource() throws Exception {
        // DROOLS-1533
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "import " + NotAdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( AdultUnit.class ) when\n" +
                "    Person(age >= 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");\n" +
                "end\n" +
                "rule NotAdult @Unit( NotAdultUnit.class ) when\n" +
                "    Person(age < 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is NOT adult\");\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = DataSource.create( new Person( "Mario", 42 ),
                                                        new Person( "Marilena", 44 ),
                                                        new Person( "Sofia", 4 ) );

        // explicitly create unit
        assertEquals(2, executor.run( new AdultUnit(persons) ) );

        // let RuleUnitExecutor internally create and wire the unit instance
        assertEquals(1, executor.run( new NotAdultUnit(persons) ) );
    }

    @Test
    public void testRuleWithoutUnitsIsNotExecutor() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "import " + NotAdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult when\n" +
                "    Person(age >= 18, $name : name)\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");\n" +
                "end\n" +
                "rule NotAdult when\n" +
                "    Person(age < 18, $name : name)\n" +
                "then\n" +
                "    System.out.println($name + \" is NOT adult\");\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();

        try {
            RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );
            fail("It should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testRunUnexistingUnit() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "import " + NotAdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( AdultUnit.class ) when\n" +
                "    Person(age >= 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        try {
            executor.run( NotAdultUnit.class );
            fail("It should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testDisallowToMixRulesWithAndWithoutUnit() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "import " + NotAdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( AdultUnit.class ) when\n" +
                "    Person(age >= 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");\n" +
                "end\n" +
                "rule NotAdult when\n" +
                "    Person(age < 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is NOT adult\");\n" +
                "end";

        try {
            KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
            fail("It should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testRuleUnitInvocationFromConsequence() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "import " + NotAdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( AdultUnit.class ) when\n" +
                "    Person(age >= 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");\n" +
                "end\n" +
                "rule NotAdult @Unit( NotAdultUnit.class ) when\n" +
                "    $p : Person(age < 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is NOT adult\");\n" +
                "    modify($p) { setAge(18); }\n" +
                "    drools.run( AdultUnit.class );" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "persons",
                                                             new Person( "Mario", 42 ),
                                                             new Person( "Marilena", 44 ),
                                                             new Person( "Sofia", 4 ) );
        List<String> log = new ArrayList<>();
        executor.bindVariable( "log", log );

        assertEquals(4, executor.run( NotAdultUnit.class ) );

        List<String> expectedLogs = asList("org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit started.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit yielded to org.drools.compiler.integrationtests.RuleUnitTest$AdultUnit",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$AdultUnit started.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$AdultUnit ended.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit started.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit ended.");
        assertEquals( expectedLogs, log );
    }

    @Test
    public void testStackedRuleUnitInvocationFromConsequence() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "import " + NotAdultUnit.class.getCanonicalName() + "\n" +
                "rule NotAdult @Unit( NotAdultUnit.class ) when\n" +
                "    $p : Person(age < 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is NOT adult\");\n" +
                "    modify($p) { setAge(18); }\n" +
                "    drools.run( AdultUnit.class );" +
                "end\n" +
                "rule Adult @Unit( AdultUnit.class ) when\n" +
                "    Person(age >= 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");\n" +
                "end\n";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "persons",
                                                             new Person( "Mario", 2 ),
                                                             new Person( "Sofia", 6 ) );
        List<String> log = new ArrayList<>();
        executor.bindVariable( "log", log );

        assertEquals(4, executor.run( NotAdultUnit.class ) );

        List<String> expectedLogs = asList("org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit started.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit yielded to org.drools.compiler.integrationtests.RuleUnitTest$AdultUnit",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$AdultUnit started.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$AdultUnit ended.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit started.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit yielded to org.drools.compiler.integrationtests.RuleUnitTest$AdultUnit",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$AdultUnit started.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$AdultUnit ended.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit started.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit ended.");
        assertEquals( expectedLogs, log );
    }

    @Test
    public void testModifyOnDataSource() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( AdultUnit.class ) when\n" +
                "    Person(age >= 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");\n" +
                "    results.add($name);\n" +
                "end\n" +
                "rule NotAdult @Unit( AdultUnit.class ) when\n" +
                "    $p : Person(age < 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is NOT adult\");\n" +
                "    modify($p) { setAge(18); }\n" +
                "    drools.run( AdultUnit.class );" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "persons",
                                                             new Person( "Mario", 42 ),
                                                             new Person( "Marilena", 44 ),
                                                             new Person( "Sofia", 4 ) );
        List<String> results = new ArrayList<>();
        executor.bindVariable( "results", results );

        assertEquals(4, executor.run( AdultUnit.class ) );
        assertEquals(3, results.size());
        assertTrue(results.containsAll( asList("Mario", "Marilena", "Sofia") ));
    }

    public static class AdultUnit implements RuleUnit {
        private int adultAge = 0;
        private DataSource<Person> persons;
        private List<String> log;
        private List<String> results;

        public AdultUnit( ) { }

        public AdultUnit( DataSource<Person> persons ) {
            this.persons = persons;
        }

        public DataSource<Person> getPersons() {
            return persons;
        }

        public int getAdultAge() {
            return adultAge;
        }

        public List<String> getResults() {
            return results;
        }

        @Override
        public void onStart() {
            if (log != null) {
                log.add( getUnitName(this) + " started." );
            } else {
                System.out.println( getUnitName(this) + " started." );
            }
        }

        @Override
        public void onEnd() {
            if (log != null) {
                log.add( getUnitName(this) + " ended." );
            } else {
                System.out.println( getUnitName(this) + " ended." );
            }
        }

        @Override
        public void onYield( RuleUnit other ) {
            if (log != null) {
                log.add( getUnitName(this) + " yielded to " + getUnitName(other) );
            } else {
                System.out.println( getUnitName(this) + " yielded to " + getUnitName(other) );
            }
        }
    }

    public static class NotAdultUnit implements RuleUnit {
        private DataSource<Person> persons;
        private List<String> log;

        public NotAdultUnit( ) { }

        public NotAdultUnit( DataSource<Person> persons ) {
            this.persons = persons;
        }

        public DataSource<Person> getPersons() {
            return persons;
        }

        @Override
        public void onStart() {
            if (log != null) {
                log.add( getUnitName(this) + " started." );
            } else {
                System.out.println( getUnitName(this) + " started." );
            }
        }

        @Override
        public void onEnd() {
            if (log != null) {
                log.add( getUnitName(this) + " ended." );
            } else {
                System.out.println( getUnitName(this) + " ended." );
            }
        }

        @Override
        public void onYield( RuleUnit other ) {
            if (log != null) {
                log.add( getUnitName(this) + " yielded to " + getUnitName(other) );
            } else {
                System.out.println( getUnitName(this) + " yielded to " + getUnitName(other) );
            }
        }
    }

    @Test
    public void testNotExistingDataSource() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( AdultUnit.class ) when\n" +
                "    Person(age >= 18, $name : name) from adults\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");\n" +
                "end";

        KieServices ks = KieServices.get();
        KieFileSystem kfs = ks.newKieFileSystem().write( "src/main/resources/r1.drl", drl1 );
        Results results = ks.newKieBuilder( kfs ).buildAll().getResults();
        assertFalse( results.getMessages().isEmpty() );
    }

    @Test
    public void testReactiveDataSource() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + ReactiveAdultUnit.class.getCanonicalName() + "\n" +
                "import " + ReactiveNotAdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( ReactiveAdultUnit.class) when\n" +
                "    Person(age >= 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");\n" +
                "end\n" +
                "rule NotAdult @Unit( ReactiveNotAdultUnit.class ) when\n" +
                "    Person(age < 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is NOT adult\");\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "persons", new Person( "Mario", 42 ) );

        ReactiveAdultUnit adultUnit = new ReactiveAdultUnit(persons, null);
        assertEquals(1, executor.run( adultUnit ) );

        ReactiveNotAdultUnit notAdultUnit = new ReactiveNotAdultUnit(persons);
        assertEquals(0, executor.run( notAdultUnit ) );

        persons.insert( new Person( "Sofia", 4 ) );
        assertEquals(0, executor.run( adultUnit ) );
        assertEquals(1, executor.run( notAdultUnit ) );

        persons.insert( new Person( "Marilena", 44 ) );
        assertEquals(1, executor.run( adultUnit ) );
        assertEquals(0, executor.run( notAdultUnit ) );
    }

    public static class ReactiveAdultUnit implements RuleUnit {
        private final DataSource<Person> persons;
        private final List<String> list;

        public ReactiveAdultUnit( DataSource<Person> persons, List<String> list ) {
            this.persons = persons;
            this.list = list;
        }

        public DataSource<Person> getPersons() {
            return persons;
        }

        public List<String> getList() {
            return list;
        }

        @Override
        public void onStart() {
            System.out.println(getUnitName(this) + " started.");
        }

        @Override
        public void onEnd() {
            System.out.println(getUnitName(this) + " ended.");
        }

        @Override
        public void onSuspend() {
            System.out.println(getUnitName(this) + " suspended.");
        }

        @Override
        public void onResume() {
            System.out.println(getUnitName(this) + " resumed.");
        }
    }

    public static class ReactiveNotAdultUnit implements RuleUnit {
        private final DataSource<Person> persons;

        public ReactiveNotAdultUnit( DataSource<Person> persons ) {
            this.persons = persons;
        }

        public DataSource<Person> getPersons() {
            return persons;
        }
    }

    @Test(timeout = 10000L)
    public void testReactiveDataSourceWithRunUntilHalt() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + ReactiveAdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( ReactiveAdultUnit.class ) when\n" +
                "    Person(age >= 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");" +
                "    list.add($name);\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DebugList<String> list = new DebugList<>();
        executor.bindVariable( "list", list );

        DataSource<Person> persons = executor.newDataSource( "persons", new Person( "Mario", 42 ) );
        ReactiveAdultUnit adultUnit = new ReactiveAdultUnit(persons, list);

        Semaphore ready = new Semaphore( 0, true);
        list.onItemAdded = ( l -> ready.release() );

        new Thread( () -> executor.runUntilHalt( adultUnit ) ).start();

        ready.acquire();

        assertEquals( 1, list.size() );
        assertEquals( "Mario", list.get(0) );
        list.clear();

        list.onItemAdded = ( l -> ready.release() );

        persons.insert( new Person( "Sofia", 4 ) );
        persons.insert( new Person( "Marilena", 44 ) );

        ready.acquire();

        assertEquals( 1, list.size() );
        assertEquals( "Marilena", list.get(0) );

        executor.halt();
    }

    @Test
    public void testNamingConventionOnDrlFile() throws Exception {
        String drl1 =
                "package org.kie.test;\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "rule Adult when\n" +
                "    $p : /persons[age >= 18]\n" +
                "then\n" +
                "    System.out.println($p.getName() + \" is adult\");\n" +
                "end";

        String javaRuleUnit =
                "package org.kie.test;\n" +
                "\n" +
                "import " + Person.class.getCanonicalName() + ";\n" +
                "import " + RuleUnit.class.getCanonicalName() + ";\n" +
                "import " + DataSource.class.getCanonicalName() + ";\n" +
                "\n" +
                "public class MyRuleUnit implements RuleUnit {\n" +
                "    private DataSource<Person> persons;\n" +
                "\n" +
                "    public DataSource<Person> getPersons() {\n" +
                "        return persons;\n" +
                "    }\n" +
                "}\n";

        String path = "org/kie/test/MyRuleUnit";

        KieServices ks = KieServices.get();
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.writeKModuleXML(ks.newKieModuleModel().toXML())
           .write("src/main/resources/" + path + ".drl", drl1)
           .write("src/main/java/" + path + ".java", javaRuleUnit);

        ks.newKieBuilder( kfs ).buildAll();
        KieContainer kcontainer = ks.newKieContainer( ks.getRepository().getDefaultReleaseId() );
        KieBase kbase = kcontainer.getKieBase();

        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "persons",
                                                             new Person( "Mario", 42 ) );

        RuleUnit ruleUnit = new RuleUnitFactory().bindVariable( "persons", persons )
                                                 .getOrCreateRuleUnit( ( (InternalRuleUnitExecutor) executor ), "org.kie.test.MyRuleUnit", kcontainer.getClassLoader() );

        assertEquals(1, executor.run( ruleUnit ) );

        persons.insert( new Person( "Sofia", 4 ) );
        assertEquals(0, executor.run( ruleUnit ) );

        persons.insert( new Person( "Marilena", 44 ) );
        assertEquals(1, executor.run( ruleUnit ) );
    }

    @Test
    public void testWithOOPath() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( AdultUnit.class ) when\n" +
                "    $p : /persons[age >= 18]\n" +
                "then\n" +
                "    System.out.println($p.getName() + \" is adult\");\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "persons",
                                                             new Person( "Mario", 42 ),
                                                             new Person( "Marilena", 44 ),
                                                             new Person( "Sofia", 4 ) );

        RuleUnit adultUnit = new AdultUnit(persons);
        assertEquals(2, executor.run( adultUnit ) );
    }

    @Test
    public void testWithOOPathAndNot() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                        "import " + AdultUnit.class.getCanonicalName() + "\n" +
                        "rule Adult @Unit( AdultUnit.class ) when\n" +
                        "    not /persons[age >= 18]\n" +
                        "then\n" +
                        "    System.out.println(\"No adults\");\n" +
                        "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "persons",
                                                             new Person( "Mario", 4 ),
                                                             new Person( "Marilena", 17 ),
                                                             new Person( "Sofia", 4 ) );

        RuleUnit adultUnit = new AdultUnit(persons);
        assertEquals(1, executor.run( adultUnit ) );
    }

    @Test
    public void testWithOOPathAndNotNoMatch() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                        "import " + AdultUnit.class.getCanonicalName() + "\n" +
                        "rule Adult @Unit( AdultUnit.class ) when\n" +
                        "    not /persons[age >= 18]\n" +
                        "then\n" +
                        "    System.out.println(\"No adults\");\n" +
                        "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "persons",
                                                             new Person( "Mario", 44 ),
                                                             new Person( "Marilena", 170 ),
                                                             new Person( "Sofia", 18 ) );

        RuleUnit adultUnit = new AdultUnit(persons);
        assertEquals(0, executor.run( adultUnit ) );
    }

    @Test
    public void testVarResolution() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( AdultUnit.class ) when\n" +
                "    $p : /persons[age >= adultAge]\n" +
                "then\n" +
                "    System.out.println($p.getName() + \" is adult and greater than \" + adultAge);\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "persons",
                                                             new Person( "Mario", 42 ),
                                                             new Person( "Marilena", 44 ),
                                                             new Person( "Sofia", 4 ) );
        executor.bindVariable( "adultAge", 18 );

        assertEquals(2, executor.run( AdultUnit.class ) );
    }

    @Test
    public void testUnitDeclaration() throws Exception {
        String drl1 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName(AdultUnit.class) + "\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "rule Adult when\n" +
                "    Person(age >= adultAge, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");\n" +
                "end";

        String drl2 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName(NotAdultUnit.class) + "\n" +
                "import " + AdultUnit.class.getCanonicalName() + "\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "rule NotAdult when\n" +
                "    $p : Person(age < 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is NOT adult\");\n" +
                "    modify($p) { setAge(18); }\n" +
                "    drools.run( AdultUnit.class );\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL )
                                       .addContent( drl2, ResourceType.DRL )
                                       .build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "persons",
                                                             new Person( "Mario", 42 ),
                                                             new Person( "Marilena", 44 ),
                                                             new Person( "Sofia", 4 ) );
        List<String> log = new ArrayList<>();
        executor.bindVariable( "log", log )
                .bindVariable( "adultAge", 18 );

        assertEquals(4, executor.run( NotAdultUnit.class ) );

        List<String> expectedLogs = asList("org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit started.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit yielded to org.drools.compiler.integrationtests.RuleUnitTest$AdultUnit",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$AdultUnit started.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$AdultUnit ended.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit started.",
                                           "org.drools.compiler.integrationtests.RuleUnitTest$NotAdultUnit ended.");
        assertEquals( expectedLogs, log );
    }

    @Test
    public void testBindingWithNamedVars() throws Exception {
        String drl1 =
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + NamedVarsUnit.class.getCanonicalName() + "\n" +
                "rule Adult @Unit( NamedVarsUnit.class ) when\n" +
                "    $p : /persons[age >= adultAge]\n" +
                "then\n" +
                "    System.out.println($p.getName() + \" is adult and greater than \" + adultAge);\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "data",
                                                             new Person( "Mario", 42 ),
                                                             new Person( "Marilena", 44 ),
                                                             new Person( "Sofia", 4 ) );
        executor.bindVariable( "minAge", 18 );

        assertEquals(2, executor.run( NamedVarsUnit.class ) );
    }

    public static class NamedVarsUnit implements RuleUnit {
        @UnitVar("minAge") private int adultAge = 0;
        @UnitVar("data") private DataSource<Person> persons;

        public NamedVarsUnit( ) { }

        public DataSource<Person> getPersons() {
            return persons;
        }

        public int getAdultAge() {
            return adultAge;
        }
    }
    
    @Test
    public void testCoordination() throws Exception {
    	String drl1 = 
    			"package org.drools.compiler.integrationtests\n" +
    	        "unit " + getCanonicalSimpleName( MasterModelUnit.class ) + ";\n" +
    		    "import " + MasterModel.class.getCanonicalName() + "\n" +
    	        "import " + ApplicableModel.class.getCanonicalName() + "\n" +
    	        "import " + ApplyMathModel.class.getCanonicalName() + "\n" +
    	        "import " + ApplyStringModel.class.getCanonicalName() + "\n" +
    		    "import " + ScheduledModelApplicationUnit.class.getCanonicalName() + "\n" +
    		    "\n" +
    	        "rule FindModelToApply \n" +
    		    "when\n" +
    	        "   $mm: MasterModel( subModels != null ) from models\n" +
    		    "   $am: ApplicableModel( applied == false, $idx: index ) from $mm.subModels\n" +
    	        "   not ApplicableModel( applied == false, index < $idx ) from $mm.subModels\n" +
    	        "then\n" +
    	        "   applicableModels.insert($am);\n" +
    	        "   drools.run(new ScheduledModelApplicationUnit(models,applicableModels));\n" +
    		    "end\n" +
    		    "";
    	String drl2 = 
    			"package org.drools.compiler.integrationtests\n" +
    			"unit " + getCanonicalSimpleName( ScheduledModelApplicationUnit.class ) + ";\n" +
    	    	"import " + MasterModel.class.getCanonicalName() + "\n" +
    	    	"import " + ApplicableModel.class.getCanonicalName() + "\n" +
    	    	"import " + ApplyMathModel.class.getCanonicalName() + "\n" +
    	    	"import " + ApplyStringModel.class.getCanonicalName() + "\n" +
    	    	"\n" +
    			"rule Apply_ApplyMathModel_Addition \n" +
    	    	"when\n" +
    			"    $amm: ApplyMathModel( applied == false, inputValue1 != null, "+
    	    	"                          inputValue2 != null, operation == \"add\" ) from applicableModels\n" +
    	    	"    $v1: Integer() from $amm.inputValue1 \n" +
    			"    $v2: Integer() from $amm.inputValue2 \n" +
    			"then\n" +
    			"    modify($amm) { \n" +
    			"       setResult($v1.intValue() + $v2.intValue()), \n" +
    			"       setApplied(true) \n" +
    			"    };\n" +
    			"    System.out.println(\"Result = \"+$amm.getResult());\n" +
    	    	"end\n" +
    			"\n" +
    	    	"rule Apply_ApplyStringModel_Concat \n" +
    			"when\n" +
    	    	"    $asm: ApplyStringModel( applied == false, inputString1 != null, " +
    			"                            inputString2 != null, operation == \"concat\" ) from applicableModels \n" +
    	    	"    $v1: String() from $asm.inputString1 \n" +
    			"    $v2: String() from $asm.inputString2 \n" +
    	    	"then\n" +
    			"    String result = $v1+\" \"+$v2; \n" +
    	    	"    modify($asm) {\n" +
    			"       setResult(result)\n" +
    	    	"    };\n" +
    			"end\n" +
    	        "";
    	
    	MasterModel master = new MasterModel("TestMaster");
    	ApplyMathModel mathModel = new ApplyMathModel(1, "Math1", "add", 10, 10);
    	ApplyStringModel stringModel = new ApplyStringModel(2, "String1", "concat", "hello", "world");
    	master.addSubModel(mathModel);
    	master.addSubModel(stringModel);
        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL )
        		.addContent(drl2, ResourceType.DRL)
                .build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

    	DataSource<MasterModel> masterModels = executor.newDataSource("models", master);
    	DataSource<ApplicableModel> applicableModels = executor.newDataSource("applicableModel");
    	int x = executor.run(new MasterModelUnit(masterModels,applicableModels));
    	System.out.println(x);
    	for (Iterator<ApplicableModel> iter = applicableModels.iterator(); iter.hasNext();) {
    		ApplicableModel model = iter.next();
    		System.out.println(model.getName()+" : "+model.getResult());
    	}
    }
    
    public static class BasicModel {
    	private int index;
    	private String name;
    	private String operation;
    	
		public BasicModel(int index, String name, String operation) {
			this.index = index;
			this.name = name;
			this.operation = operation;
		}

		public int getIndex() {
			return index;
		}

		public String getName() {
			return name;
		}

		public String getOperation() {
			return operation;
		}
    	
    }
    
    public static class MasterModel extends BasicModel {
    	private List<ApplicableModel> subModels;
    	
    	public MasterModel(String name) {
    		super(0,name,"master");
    		subModels = new ArrayList<>();
    	}
    	
    	public List<ApplicableModel> getSubModels() {
    		return subModels;
    	}
    	
    	public boolean addSubModel(ApplicableModel subModel) {
    		return subModels.add(subModel);
    	}
    }
    
    public abstract static class ApplicableModel extends BasicModel {
    	private boolean applied;
    	
    	public ApplicableModel(int index, String name, String operation) {
    		super(index,name,operation);
    		this.applied = false;
    	}
    	
    	public ApplicableModel(int index, String name, String operation, boolean applied) {
    		super(index,name,operation);
    		this.applied = applied; 
    	}
    	
    	public boolean isApplied() {
    		return applied;
    	}
    	
    	public void setApplied(boolean applied) {
    		this.applied = applied;
    	}
    	
    	public abstract Object getResult();
    }
    
    public static class ApplyMathModel extends ApplicableModel {
		private Integer inputValue1;
    	private Integer inputValue2;
    	private Integer result;
    	
    	public ApplyMathModel(int index, String name, String operation, Integer inputValue1, Integer inputValue2) {
			super(index, name, operation);
			this.inputValue1 = inputValue1;
			this.inputValue2 = inputValue2;
		}

		public Integer getInputValue1() {
			return inputValue1;
		}

		public void setInputValue1(Integer inputValue1) {
			this.inputValue1 = inputValue1;
		}

		public Integer getInputValue2() {
			return inputValue2;
		}

		public void setInputValue2(Integer inputValue2) {
			this.inputValue2 = inputValue2;
		}

		public Integer getResult() {
			return result;
		}

		public void setResult(Integer result) {
			this.result = result;
		}
    }
    
    public static class ApplyStringModel extends ApplicableModel {
    	private String inputString1;
    	private String inputString2;
    	private String result;
    	
    	public ApplyStringModel(int index, String name, String operation, String inputString1, String inputString2) {
    		super(index,name,operation);
    		this.inputString1 = inputString1;
    		this.inputString2 = inputString2;
    	}

		public String getInputString1() {
			return inputString1;
		}

		public void setInputString1(String inputString1) {
			this.inputString1 = inputString1;
		}

		public String getInputString2() {
			return inputString2;
		}

		public void setInputString2(String inputString2) {
			this.inputString2 = inputString2;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}
		
    }
    
    public static class MasterModelUnit implements RuleUnit {
    	private DataSource<MasterModel> models;
    	private DataSource<ApplicableModel> applicableModels;
    	
    	public MasterModelUnit(DataSource<MasterModel> models, DataSource<ApplicableModel> applicableModels) {
    		this.models = models;
    		this.applicableModels = applicableModels;
    	}
    	
    	public DataSource<MasterModel> getModels() {
    		return models;
    	}
    	
    	public DataSource<ApplicableModel> getApplicableModels() {
    		return applicableModels;
    	}
    }
    
    public static class ScheduledModelApplicationUnit implements RuleUnit {
    	private DataSource<MasterModel> models;
    	private DataSource<ApplicableModel> applicableModels;
    	
    	public ScheduledModelApplicationUnit(DataSource<MasterModel> models, DataSource<ApplicableModel> applicableModels) {
    		this.models = models;
    		this.applicableModels = applicableModels;
    	}
    	
    	public DataSource<MasterModel> getModels() {
    		return models;
    	}
    	
    	public DataSource<ApplicableModel> getApplicableModels() {
    		return applicableModels;
    	}
    }

    @Test
    public void testGuardedUnit() throws Exception {
        String drl1 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( BoxOfficeUnit.class ) + ";\n" +
                "import " + BoxOffice.class.getCanonicalName() + "\n" +
                "import " + AdultTicketIssuerUnit.class.getCanonicalName() + "\n" +
                "import " + ChildTicketIssuerUnit.class.getCanonicalName() + "\n" +
                "\n" +
                "rule BoxOfficeIsOpen when\n" +
                "    $box: /boxOffices[ open ]\n" +
                "then\n" +
                "    drools.guard( AdultTicketIssuerUnit.class ); \n" +
                "end \n" +
                "\n" +
                "rule ChildBoxOfficeIsOpen \n" +
                "when \n" +
                "    $box: BoxOffice( open == true, serveMinors == true ) from boxOffices \n" +
                "then \n" +
                "    drools.guard( AdultTicketIssuerUnit.class ); \n" +
                "    drools.guard( ChildTicketIssuerUnit.class ); \n" +
                "end \n";

        String drl2 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( AdultTicketIssuerUnit.class ) + ";\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + AdultTicket.class.getCanonicalName() + "\n" +
                "rule IssueAdultTicket when\n" +
                "    $p: /persons[ age >= 18 ]\n" +
                "then\n" +
                "    adultTickets.insert(new AdultTicket($p));\n" +
                "    ticketsIssued += 1; \n" +
                "end\n" +
                "rule RegisterAdultTicket when\n" +
                "    $t: /adultTickets\n" +
                "then\n" +
                "    results.add( $t.getPerson().getName() );\n" +
                "end";
        String drl3 = 
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( ChildTicketIssuerUnit.class ) + ";\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + ChildTicket.class.getCanonicalName() + "\n" +
                "rule IssueChildTicket when\n" +
                "    $p: /persons[ age < 18 ]\n" +
                "then\n" +
                "    childTickets.insert(new ChildTicket($p));\n" +
                "    ticketsIssued += 1; \n" +
                "end\n" +
                "rule RegisterChildTicket when\n" +
                "    $t: /childTickets\n" +
                "then\n" +
                "    results.add( $t.getPerson().getName() );\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL )
                                       .addContent( drl2, ResourceType.DRL )
                                       .addContent( drl3, ResourceType.DRL )
                                       .build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Person> persons = executor.newDataSource( "persons" );
        DataSource<BoxOffice> boxOffices = executor.newDataSource( "boxOffices" );
        DataSource<AdultTicket> adultTickets = executor.newDataSource( "adultTickets" );
        DataSource<ChildTicket> childTickets = executor.newDataSource( "childTickets" );

        List<String> list = new ArrayList<>();
        Integer adultTicketsIssued = new Integer(0);
        executor.bindVariable( "results", list );
        executor.bindVariable( "ticketsIssued", adultTicketsIssued);

        // two open box offices
        BoxOffice office1 = new BoxOffice(true,true);
        FactHandle officeFH1 = boxOffices.insert( office1 );
        BoxOffice office2 = new BoxOffice(true);
        FactHandle officeFH2 = boxOffices.insert( office2 );

        persons.insert(new Person("Mario", 40));
        persons.insert(new Person("Mary", 16));
        executor.run(BoxOfficeUnit.class); // fire BoxOfficeIsOpen -> run TicketIssuerUnit -> fire RegisterAdultTicket

        assertEquals( 2, list.size() );
        assertTrue( list.contains("Mario") );
        assertTrue( list.contains("Mary") );
        list.clear();

        persons.insert(new Person("Matteo", 30));
        executor.run(BoxOfficeUnit.class); // fire RegisterAdultTicket

        assertEquals( 1, list.size() );
        assertEquals( "Matteo", list.get(0) );
        list.clear();

        // close one box office, the other is still open
        office1.setOpen(false);
        boxOffices.update(officeFH1, office1);
        persons.insert(new Person("Mark", 35));
        persons.insert(new Person("Sue", 12));
        executor.run(BoxOfficeUnit.class);

        assertEquals( 1, list.size() ); // closed boxOffice1 is the only box office to serve persons < 18
        assertEquals( "Mark", list.get(0) );
        list.clear();

        // all box offices, are now closed
        office2.setOpen(false);
        boxOffices.update(officeFH2, office2); // guarding rule no longer true
        persons.insert(new Person("Edson", 35));
        executor.run(BoxOfficeUnit.class); // no fire
        
        assertEquals( 0, list.size() );
        
        // reopen all box offices
        office1.setOpen(true);
        office2.setOpen(true);
        boxOffices.update(officeFH1, office1);
        boxOffices.update(officeFH2, office2);
        
        persons.insert(new Person("Alec", 15));
        executor.run(BoxOfficeUnit.class);

        assertEquals( 3, list.size() ); // box office1 issues previously registered tickets as well as the new ticket
        assertTrue( list.contains("Edson") );
        assertTrue( list.contains("Sue") );
        assertTrue( list.contains("Alec") );

        adultTickets.forEach(t -> {
        	System.out.printf("Adult ticket: %s (%d)%n",t.getPerson().getName(),t.getPerson().getAge());
        });
        childTickets.forEach(t -> {
        	System.out.printf("Child ticket: %s (%d)%n",t.getPerson().getName(),t.getPerson().getAge());
        });
    }
    

    public static class BoxOffice {
        private boolean open;
        private boolean serveMinors;

        public BoxOffice( boolean open ) {
            this.open = open;
            this.serveMinors = false;
        }
        
        public BoxOffice( boolean open, boolean serveMinors) {
        	this.open = open;
        	this.serveMinors = serveMinors;
        }

        public boolean isOpen() {
            return open;
        }

        public void setOpen( boolean open ) {
            this.open = open;
        }

		public boolean isServeMinors() {
			return serveMinors;
		}

		public void setServeMinors(boolean serveMinors) {
			this.serveMinors = serveMinors;
		}
        
    }

    public static class AdultTicket {
        private final Person person;

        public AdultTicket( Person person ) {
            this.person = person;
        }

        public Person getPerson() {
            return person;
        }
    }
    
    public static class ChildTicket {
    	private final Person person;
    	
    	public ChildTicket( Person person ) {
    		this.person = person;
    	}
    	
    	public Person getPerson() {
    		return person;
    	}
    }

    public static class BoxOfficeUnit implements RuleUnit {
        private DataSource<BoxOffice> boxOffices;

        public DataSource<BoxOffice> getBoxOffices() {
            return boxOffices;
        }
    }

    public static class AdultTicketIssuerUnit implements RuleUnit {
        private DataSource<Person> persons;
        private DataSource<AdultTicket> adultTickets;
        private int ticketsIssued;

        private List<String> results;

        public AdultTicketIssuerUnit() { }

        public AdultTicketIssuerUnit( DataSource<Person> persons, DataSource<AdultTicket> tickets ) {
            this.persons = persons;
            this.adultTickets = tickets;
            this.ticketsIssued = 0;
        }

        public DataSource<Person> getPersons() {
            return persons;
        }

        public DataSource<AdultTicket> getAdultTickets() {
            return adultTickets;
        }

        public List<String> getResults() {
            return results;
        }
        
        public int getTicketsIssued() {
        	return ticketsIssued;
        }
    }
    
    public static class ChildTicketIssuerUnit implements RuleUnit {
    	private DataSource<Person> persons;
    	private DataSource<ChildTicket> childTickets;
    	private int ticketsIssued;
    	
    	private List<String> results;
    	
    	public ChildTicketIssuerUnit() { }
    	
    	public ChildTicketIssuerUnit( DataSource<Person> persons, DataSource<ChildTicket> tickets ) {
    		this.persons = persons;
    		this.childTickets = tickets;
    		this.ticketsIssued = 0;
    	}
    	
    	public DataSource<Person> getPersons() {
    		return persons;
    	}
    	
    	public DataSource<ChildTicket> getChildTickets() {
    		return childTickets;
    	}
    	
    	public List<String> getResults() {
    		return results;
    	}
    	
    	public int getTicketsIssued() {
    		return ticketsIssued;
    	}
    }
    

    @Test
    public void testMultiLevelGuards() throws Exception {
        String drl1 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( Unit0.class ) + "\n" +
                "import " + UnitA.class.getCanonicalName() + "\n" +
                "rule X when\n" +
                "    $b: /ds#Boolean\n" +
                "then\n" +
                "    Boolean b = $b;\n" +
                "    drools.guard( UnitA.class );\n" +
                "end";

        String drl2 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( UnitA.class ) + "\n" +
                "import " + UnitB.class.getCanonicalName() + "\n" +
                "rule A when\n" +
                "    $s: /ds#String\n" +
                "then\n" +
                "    drools.guard( UnitB.class );" +
                "end";

        String drl3 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( UnitB.class ) + "\n" +
                "import " + UnitB.class.getCanonicalName() + "\n" +
                "rule B when\n" +
                "    $i: /ds#Integer\n" +
                "then\n" +
                "    list.add($i);" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL )
                                       .addContent( drl2, ResourceType.DRL )
                                       .addContent( drl3, ResourceType.DRL )
                                       .build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Object> ds = executor.newDataSource( "ds" );

        List<Integer> list = new ArrayList<>();
        executor.bindVariable( "list", list );

        ds.insert( 1 );
        executor.run(Unit0.class);
        assertEquals( 0, list.size() ); // all units are inactive

        FactHandle guardA = ds.insert( true );
        executor.run(Unit0.class);
        assertEquals( 0, list.size() ); // UnitB still inactive

        FactHandle guardB = ds.insert( "test" );
        executor.run(Unit0.class);
        assertEquals( 1, list.size() ); // all units are active
        assertEquals( 1, (int)list.get(0) ); // all units are active
        list.clear();

        ds.insert( 2 );
        executor.run(Unit0.class);
        assertEquals( 1, list.size() ); // all units are inactive
        assertEquals( 2, (int)list.get(0) ); // all units are active
        list.clear();

        ds.delete( guardA ); // retracting guard A deactivate unitA and in cascade unit B
        ds.insert( 3 );
        executor.run(Unit0.class);
        assertEquals( 0, list.size() ); // all units are inactive

        guardA = ds.insert( true ); // activating guard A reactivate unitA and in cascade unit B
        executor.run(Unit0.class);
        assertEquals( 1, list.size() ); // all units are active
        list.clear();
    }

    public static class Unit0 implements RuleUnit {
        private DataSource<Object> ds;

        public DataSource<Object> getDs() {
            return ds;
        }
    }
    public static class UnitA implements RuleUnit {
        private DataSource<Object> ds;

        public DataSource<Object> getDs() {
            return ds;
        }
    }
    public static class UnitB implements RuleUnit {
        private DataSource<Object> ds;
        private List<Integer> list;

        public DataSource<Object> getDs() {
            return ds;
        }

        public List<Integer> getList() {
            return list;
        }
    }

    @Test
    public void testRuleUnitIdentity() throws Exception {
        String drl1 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( Unit0.class ) + "\n" +
                "import " + AgeCheckUnit.class.getCanonicalName() + "\n" +
                "\n" +
                "rule R1 when\n" +
                "    $i: /ds#Integer\n" +
                "then\n" +
                "    drools.guard( new AgeCheckUnit($i) );" +
                "end\n" +
                "rule RegisterAdultTicket when\n" +
                "    $s: /ds#String\n" +
                "then\n" +
                "    drools.guard( new AgeCheckUnit($s.length()) );" +
                "end";

        String drl2 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( AgeCheckUnit.class ) + ";\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "rule CheckAge when\n" +
                "    $p : /persons[ age > minAge ]\n" +
                "then\n" +
                "    list.add($p.getName() + \">\" + minAge);\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL )
                                       .addContent( drl2, ResourceType.DRL )
                                       .build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DataSource<Object> ds = executor.newDataSource( "ds" );
        DataSource<Person> persons = executor.newDataSource( "persons",
                                                             new Person( "Mario", 42 ),
                                                             new Person( "Sofia", 4 ) );

        List<String> list = new ArrayList<>();
        executor.bindVariable( "list", list );

        ds.insert("test");
        ds.insert(3);
        ds.insert(4);
        executor.run(Unit0.class);

        System.out.println(list);
        assertEquals(3, list.size());
        assertTrue( list.containsAll( asList("Mario>4", "Mario>3", "Sofia>3") ) );

        list.clear();
        ds.insert("xxx");
        ds.insert("yyyy");
        executor.run(Unit0.class);
        assertEquals(0, list.size());
    }

    public static class AgeCheckUnit implements RuleUnit {
        private final int minAge;
        private DataSource<Person> persons;
        private List<String> list;

        public AgeCheckUnit( int minAge ) {
            this.minAge = minAge;
        }

        public DataSource<Person> getPersons() {
            return persons;
        }

        public int getMinAge() {
            return minAge;
        }

        public List<String> getList() {
            return list;
        }

        @Override
        public Identity getUnitIdentity() {
            return new Identity(getClass(), minAge);
        }

        @Override
        public String toString() {
            return "AgeCheckUnit(" + minAge + ")";
        }
    }

    @Test(timeout = 10000L)
    public void testPropertyReactiveModify() throws Exception {
        String drl1 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( AdultUnit.class ) + "\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "rule Adult when\n" +
                "    $p: /persons[ age < 18 ]\n" +
                "then\n" +
                "    System.out.println($p.getName() + \" is NOT adult\");\n" +
                "    modify($p) { setHappy(true); }\n" +
                "end";

        KieBase kbase = new KieHelper( PropertySpecificOption.ALWAYS ).addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        Person mario = new Person( "Mario", 42 );
        Person sofia = new Person( "Sofia", 4 );

        DataSource<Person> persons = executor.newDataSource( "persons" );
        FactHandle marioFh = persons.insert( mario );
        FactHandle sofiaFh = persons.insert( sofia );

        executor.run( AdultUnit.class );

        assertTrue( sofia.isHappy() );
        assertFalse( mario.isHappy() );

        sofia.setAge( 5 );
        persons.update( sofiaFh, sofia, "age" );
        assertEquals( 1, executor.run( AdultUnit.class ) );

        sofia.setSex( 'F' );
        persons.update( sofiaFh, sofia, "sex" );
        assertEquals( 0, executor.run( AdultUnit.class ) );
    }

    @Test
    public void testTwoPartsOOPath() throws Exception {
        // DROOLS-1539
        String drl1 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( AdultUnit.class ) + "\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + LongAddress.class.getCanonicalName() + "\n" +
                "rule Adult when\n" +
                "    $a: /persons[ age > 18 ]/addresses#LongAddress[ country == \"it\" ]\n" +
                "then\n" +
                "    System.out.println($a.getCountry());\n" +
                "end";

        KieBase kbase = new KieHelper( PropertySpecificOption.ALWAYS ).addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        Person mario = new Person( "Mario", 43 );
        mario.setAddresses(asList( new LongAddress( "street", "suburb", "zipCode", "it") ) );
        Person mark = new Person( "Mark", 40 );
        mark.setAddresses(asList( new LongAddress( "street", "suburb", "zipCode", "uk") ) );

        DataSource<Person> persons = executor.newDataSource( "persons", mario, mark );

        assertEquals( 1, executor.run( AdultUnit.class ) );
    }

    @Test
    public void testNestedOOPath() throws Exception {
        // DROOLS-1539
        String drl1 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( AdultUnit.class ) + "\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "import " + LongAddress.class.getCanonicalName() + "\n" +
                "rule Adult when\n" +
                "    $p: /persons[ age > 18, $a: /addresses#LongAddress[ country == \"it\" ] ]\n" +
                "then\n" +
                "    System.out.println($p.getName() + \" is in \" + $a.getCountry());\n" +
                "end";

        KieBase kbase = new KieHelper( PropertySpecificOption.ALWAYS ).addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        Person mario = new Person( "Mario", 43 );
        mario.setAddresses(asList( new LongAddress( "street", "suburb", "zipCode", "it") ) );
        Person mark = new Person( "Mark", 40 );
        mark.setAddresses(asList( new LongAddress( "street", "suburb", "zipCode", "uk") ) );

        DataSource<Person> persons = executor.newDataSource( "persons", mario, mark );

        assertEquals( 1, executor.run( AdultUnit.class ) );
    }

    @Test
    public void testWithOOPathOnList() throws Exception {
        // DROOLS-1646
        String drl1 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( AdultUnitWithList.class ) + "\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "rule Adult when\n" +
                "    $p : /persons[age >= adultAge]\n" +
                "then\n" +
                "    System.out.println($p.getName() + \" is adult\");\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        List<Person> persons = asList( new Person( "Mario", 42 ),
                                       new Person( "Marilena", 44 ),
                                       new Person( "Sofia", 4 ) );

        RuleUnit adultUnit = new AdultUnitWithList(persons);
        assertEquals(2, executor.run( adultUnit ) );
    }

    public static class AdultUnitWithList implements RuleUnit {
        private int adultAge = 18;
        private List<Person> persons;

        public AdultUnitWithList( ) { }

        public AdultUnitWithList( List<Person> persons ) {
            this.persons = persons;
        }

        public List<Person> getPersons() {
            return persons;
        }

        public int getAdultAge() {
            return adultAge;
        }
    }

    @Test
    public void testWithOOPathOnArray() throws Exception {
        // DROOLS-1646
        String drl1 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( AdultUnitWithArray.class ) + "\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "rule Adult when\n" +
                "    $p : /persons[age >= adultAge]\n" +
                "then\n" +
                "    System.out.println($p.getName() + \" is adult\");\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        RuleUnit adultUnit = new AdultUnitWithArray( new Person( "Mario", 42 ),
                                                     new Person( "Marilena", 44 ),
                                                     new Person( "Sofia", 4 ) );
        assertEquals(2, executor.run( adultUnit ) );
    }

    public static class AdultUnitWithArray implements RuleUnit {
        private int adultAge = 18;
        private Person[] persons;

        public AdultUnitWithArray( ) { }

        public AdultUnitWithArray( Person... persons ) {
            this.persons = persons;
        }

        public Person[] getPersons() {
            return persons;
        }

        public int getAdultAge() {
            return adultAge;
        }
    }

    @Test
    public void testWithOOPathOnSingleItem() throws Exception {
        // DROOLS-1646
        String drl1 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( AdultUnitWithSingleItem.class ) + "\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "rule Adult when\n" +
                "    $p : /person[age >= adultAge]\n" +
                "then\n" +
                "    System.out.println($p.getName() + \" is adult\");\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        RuleUnit adultUnit = new AdultUnitWithSingleItem(new Person( "Mario", 42 ));
        assertEquals(1, executor.run( adultUnit ) );
    }

    public static class AdultUnitWithSingleItem implements RuleUnit {
        private int adultAge = 18;
        private Person person;

        public AdultUnitWithSingleItem( ) { }

        public AdultUnitWithSingleItem( Person person ) {
            this.person = person;
        }

        public Person getPerson() {
            return person;
        }

        public int getAdultAge() {
            return adultAge;
        }
    }

    @Test(timeout = 10000L)
    public void testReactiveOnUnitCreatingDataSource() throws Exception {
        // DROOLS-1647
        String drl1 =
                "package org.drools.compiler.integrationtests\n" +
                "unit " + getCanonicalSimpleName( AdultUnitCreatingDataSource.class ) + "\n" +
                "import " + Person.class.getCanonicalName() + "\n" +
                "rule Adult when\n" +
                "    Person(age >= 18, $name : name) from persons\n" +
                "then\n" +
                "    System.out.println($name + \" is adult\");" +
                "    list.add($name);\n" +
                "end";

        KieBase kbase = new KieHelper().addContent( drl1, ResourceType.DRL ).build();
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind( kbase );

        DebugList<String> list = new DebugList<>();
        executor.bindVariable( "list", list );

        AdultUnitCreatingDataSource adultUnit = new AdultUnitCreatingDataSource(list);
        adultUnit.insertPerson( new Person( "Mario", 42 ) );

        Semaphore ready = new Semaphore( 0, true);
        list.onItemAdded = ( l -> ready.release() );

        new Thread( () -> executor.runUntilHalt( adultUnit ) ).start();

        ready.acquire();

        assertEquals( 1, list.size() );
        assertEquals( "Mario", list.get(0) );
        list.clear();

        list.onItemAdded = ( l -> ready.release() );

        adultUnit.insertPerson( new Person( "Sofia", 4 ) );
        adultUnit.insertPerson( new Person( "Marilena", 44 ) );

        ready.acquire();

        assertEquals( 1, list.size() );
        assertEquals( "Marilena", list.get(0) );

        executor.halt();
    }

    public static class AdultUnitCreatingDataSource implements RuleUnit {
        private final DataSource<Person> persons;
        private final List<String> list;

        public AdultUnitCreatingDataSource( List<String> list ) {
            this.persons = DataSource.create();
            this.list = list;
        }

        public DataSource<Person> getPersons() {
            return persons;
        }

        public List<String> getList() {
            return list;
        }

        public void insertPerson(Person person) {
            persons.insert( person );
        }
    }
}
