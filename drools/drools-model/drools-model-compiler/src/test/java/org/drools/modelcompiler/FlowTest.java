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

package org.drools.modelcompiler;

import org.assertj.core.api.Assertions;
import org.drools.core.reteoo.AlphaNode;
import org.drools.model.*;
import org.drools.model.Index.ConstraintType;
import org.drools.model.impl.ModelImpl;
import org.drools.modelcompiler.builder.KieBaseBuilder;
import org.drools.modelcompiler.domain.*;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.QueryResults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.drools.model.DSL.*;
import static org.drools.modelcompiler.CompilerTest.getObjects;
import static org.junit.Assert.*;

public class FlowTest {

    @Test
    public void testBeta() {
        Result result = new Result();
        Variable<Person> markV = declarationOf( type( Person.class ) );
        Variable<Person> olderV = declarationOf( type( Person.class ) );

        Rule rule = rule( "beta" )
                .build(
                        expr("exprA", markV, p -> p.getName().equals("Mark"))
                                .indexedBy( String.class, ConstraintType.EQUAL, Person::getName, "Mark" )
                                .reactOn( "name", "age" ), // also react on age, see RuleDescr.lookAheadFieldsOfIdentifier
                        expr("exprB", olderV, p -> !p.getName().equals("Mark"))
                                .indexedBy( String.class, ConstraintType.NOT_EQUAL, Person::getName, "Mark" )
                                .reactOn( "name" ),
                        expr("exprC", olderV, markV, (p1, p2) -> p1.getAge() > p2.getAge())
                                .indexedBy( int.class, ConstraintType.GREATER_THAN, Person::getAge, Person::getAge )
                                .reactOn( "age" ),
                        on(olderV, markV).execute((p1, p2) -> result.setValue( p1.getName() + " is older than " + p2.getName()))
                     );

        Model model = new ModelImpl().addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        Person mark = new Person("Mark", 37);
        Person edson = new Person("Edson", 35);
        Person mario = new Person("Mario", 40);

        FactHandle markFH = ksession.insert(mark);
        FactHandle edsonFH = ksession.insert(edson);
        FactHandle marioFH = ksession.insert(mario);

        ksession.fireAllRules();
        assertEquals("Mario is older than Mark", result.getValue());

        result.setValue( null );
        ksession.delete( marioFH );
        ksession.fireAllRules();
        assertNull(result.getValue());

        mark.setAge( 34 );
        ksession.update( markFH, mark, "age" );

        ksession.fireAllRules();
        assertEquals("Edson is older than Mark", result.getValue());
    }

    @Test
    public void testBetaWithResult() {
        Variable<Person> markV = declarationOf( type( Person.class ) );
        Variable<Person> olderV = declarationOf( type( Person.class ) );
        Variable<Result> resultV = declarationOf( type( Result.class ) );

        Rule rule = rule( "beta" )
                .build(
                        expr("exprA", markV, p -> p.getName().equals("Mark"))
                                .indexedBy( String.class, ConstraintType.EQUAL, Person::getName, "Mark" )
                                .reactOn( "name", "age" ), // also react on age, see RuleDescr.lookAheadFieldsOfIdentifier
                        expr("exprB", olderV, p -> !p.getName().equals("Mark"))
                                .indexedBy( String.class, ConstraintType.NOT_EQUAL, Person::getName, "Mark" )
                                .reactOn( "name" ),
                        expr("exprC", olderV, markV, (p1, p2) -> p1.getAge() > p2.getAge())
                                .indexedBy( int.class, ConstraintType.GREATER_THAN, Person::getAge, Person::getAge )
                                .reactOn( "age" ),
                        on(olderV, markV, resultV)
                            .execute((p1, p2, r) -> r.setValue( p1.getName() + " is older than " + p2.getName()) )
                      );

        Model model = new ModelImpl().addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        Result result = new Result();
        ksession.insert(result);

        Person mark = new Person("Mark", 37);
        Person edson = new Person("Edson", 35);
        Person mario = new Person("Mario", 40);

        FactHandle markFH = ksession.insert(mark);
        FactHandle edsonFH = ksession.insert(edson);
        FactHandle marioFH = ksession.insert(mario);

        ksession.fireAllRules();
        assertEquals( "Mario is older than Mark", result.getValue() );
    }

    @Test
    public void test3Patterns() {
        Result result = new Result();
        Variable<Person> personV = declarationOf( type( Person.class ) );
        Variable<Person> markV = declarationOf( type( Person.class ) );
        Variable<String> nameV = declarationOf( type( String.class ) );

        Rule rule = rule( "myrule" )
                .build(
                        expr("exprA", markV, p -> p.getName().equals("Mark")),
                        expr("exprB", personV, markV, (p1, p2) -> p1.getAge() > p2.getAge()),
                        expr("exprC", nameV, personV, (s, p) -> s.equals( p.getName() )),
                        on(nameV).execute(s -> result.setValue( s ))
                      );

        Model model = new ModelImpl().addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        ksession.insert( "Mario" );
        ksession.insert(new Person("Mark", 37));
        ksession.insert(new Person("Edson", 35));
        ksession.insert(new Person("Mario", 40));
        ksession.fireAllRules();

        assertEquals("Mario", result.getValue());
    }

    @Test
    public void testOr() {
        Result result = new Result();
        Variable<Person> personV = declarationOf( type( Person.class ) );
        Variable<Person> markV = declarationOf( type( Person.class ) );
        Variable<String> nameV = declarationOf( type( String.class ) );

        Rule rule = rule( "or" )
                .build(
                        or(
                            expr("exprA", personV, p -> p.getName().equals("Mark")),
                            and(
                                    expr("exprA", markV, p -> p.getName().equals("Mark")),
                                    expr("exprB", personV, markV, (p1, p2) -> p1.getAge() > p2.getAge())
                               )
                          ),
                        expr("exprC", nameV, personV, (s, p) -> s.equals( p.getName() )),
                        on(nameV).execute(s -> result.setValue( s ))
                      );

        Model model = new ModelImpl().addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        ksession.insert( "Mario" );
        ksession.insert(new Person("Mark", 37));
        ksession.insert(new Person("Edson", 35));
        ksession.insert(new Person("Mario", 40));
        ksession.fireAllRules();

        assertEquals("Mario", result.getValue());
    }

    @Test
    public void testNot() {
        Result result = new Result();
        Variable<Person> oldestV = declarationOf( type( Person.class ) );
        Variable<Person> otherV = declarationOf( type( Person.class ) );

        Rule rule = rule("not")
                .build(
                        not(otherV, oldestV, (p1, p2) -> p1.getAge() > p2.getAge()),
                        on(oldestV).execute(p -> result.setValue( "Oldest person is " + p.getName()))
                      );

        Model model = new ModelImpl().addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        ksession.insert(new Person("Mark", 37));
        ksession.insert(new Person("Edson", 35));
        ksession.insert(new Person("Mario", 40));

        ksession.fireAllRules();
        assertEquals("Oldest person is Mario", result.getValue());
    }

    @Test
    public void testAccumulate1() {
        Result result = new Result();
        Variable<Person> person = declarationOf( type( Person.class ) );
        Variable<Integer> resultSum = declarationOf( type( Integer.class ) );

        Rule rule = rule("accumulate")
                .build(
                        accumulate(expr(person, p -> p.getName().startsWith("M")),
                                   sum((Person p) -> p.getAge()).as(resultSum)),
                        on(resultSum).execute(sum -> result.setValue( "total = " + sum) )
                      );

        Model model = new ModelImpl().addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        ksession.insert(new Person("Mark", 37));
        ksession.insert(new Person("Edson", 35));
        ksession.insert(new Person("Mario", 40));

        ksession.fireAllRules();
        assertEquals("total = 77", result.getValue());
    }

    @Test
    public void testAccumulate2() {
        Result result = new Result();
        Variable<Person> person = declarationOf( type( Person.class ) );
        Variable<Integer> resultSum = declarationOf( type( Integer.class ) );
        Variable<Double> resultAvg = declarationOf( type( Double.class ) );

        Rule rule = rule("accumulate")
                .build(
                        accumulate(expr(person, p -> p.getName().startsWith("M")),
                                   sum(Person::getAge).as(resultSum),
                                   average(Person::getAge).as(resultAvg)),
                        on(resultSum, resultAvg)
                                .execute((sum, avg) -> result.setValue( "total = " + sum + "; average = " + avg ))
                     );

        Model model = new ModelImpl().addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        ksession.insert(new Person("Mark", 37));
        ksession.insert(new Person("Edson", 35));
        ksession.insert(new Person("Mario", 40));

        ksession.fireAllRules();
        assertEquals("total = 77; average = 38.5", result.getValue());
    }

    @Test
    public void testGlobalInConsequence() {
        Variable<Person> markV = declarationOf( type( Person.class ) );
        Global<Result> resultG = globalOf( type( Result.class ), "org.mypkg" );

        Rule rule = rule( "org.mypkg", "global" )
                .build(
                        expr("exprA", markV, p -> p.getName().equals("Mark"))
                                .indexedBy( String.class, ConstraintType.EQUAL, Person::getName, "Mark" )
                                .reactOn( "name" ),
                        on(markV, resultG)
                              .execute((p, r) -> r.setValue( p.getName() + " is " + p.getAge() ) )
                      );

        Model model = new ModelImpl().addRule( rule ).addGlobal( resultG );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        Result result = new Result();
        ksession.setGlobal( resultG.getName(), result );

        Person mark = new Person("Mark", 37);
        Person edson = new Person("Edson", 35);
        Person mario = new Person("Mario", 40);

        ksession.insert(mark);
        ksession.insert(edson);
        ksession.insert(mario);

        ksession.fireAllRules();
        assertEquals("Mark is 37", result.getValue());
    }

    @Test
    public void testGlobalInConstraint() {
        Variable<Person> markV = declarationOf( type( Person.class ) );
        Global<Result> resultG = globalOf( type( Result.class ), "org.mypkg" );
        Global<String> nameG = globalOf( type( String.class ), "org.mypkg" );

        Rule rule = rule( "org.mypkg", "global" )
                .build(
                        expr("exprA", markV, nameG, (p, n) -> p.getName().equals(n)).reactOn( "name" ),
                        on(markV, resultG).execute((p, r) -> r.setValue( p.getName() + " is " + p.getAge() ) )
                      );

        Model model = new ModelImpl().addRule( rule ).addGlobal( nameG ).addGlobal( resultG );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        ksession.setGlobal( nameG.getName(), "Mark" );

        Result result = new Result();
        ksession.setGlobal( resultG.getName(), result );

        Person mark = new Person("Mark", 37);
        Person edson = new Person("Edson", 35);
        Person mario = new Person("Mario", 40);

        ksession.insert(mark);
        ksession.insert(edson);
        ksession.insert(mario);

        ksession.fireAllRules();
        assertEquals("Mark is 37", result.getValue());
    }

    @Test
    public void testNotEmptyPredicate() {
        Rule rule = rule("R")
                .build(
                        not(input(declarationOf(type(Person.class)))),
                        execute((drools) -> drools.insert(new Result("ok")) )
                      );

        Model model = new ModelImpl().addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        ReteDumper.checkRete(ksession, node -> !(node instanceof AlphaNode) );

        Person mario = new Person("Mario", 40);

        ksession.insert(mario);
        ksession.fireAllRules();

        assertTrue( ksession.getObjects(new ClassObjectFilter( Result.class ) ).isEmpty() );
    }

    @Test
    public void testQuery() {
        Variable<Person> personV = declarationOf( type( Person.class ), "$p" );
        Variable<Integer> ageV = declarationOf( type( Integer.class ) );

        Query1<Integer> query = query( "olderThan", ageV )
                .build( expr("exprA", personV, ageV, (p, a) -> p.getAge() > a) );

        Model model = new ModelImpl().addQuery( query );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        ksession.insert( new Person( "Mark", 39 ) );
        ksession.insert( new Person( "Mario", 41 ) );

        QueryResults results = ksession.getQueryResults( "olderThan", 40 );

        assertEquals( 1, results.size() );
        Person p = (Person) results.iterator().next().get( "$p" );
        assertEquals( "Mario", p.getName() );
    }

    @Test
    public void testQueryInRule() {
        Variable<Person> personV = declarationOf( type( Person.class ) );
        Variable<Integer> ageV = declarationOf( type( Integer.class ) );

        Query2<Person, Integer> query = query( "olderThan", personV, ageV )
                .build( expr("exprA", personV, ageV, (p, a) -> p.getAge() > a) );

        Variable<Person> personVRule = declarationOf( type( Person.class ) );
        Rule rule = rule("R")
                .build(
                        query.call(personVRule, valueOf(40)),
                        on(personVRule).execute((drools, p) -> drools.insert(new Result(p.getName())) )
                      );

        Model model = new ModelImpl().addQuery( query ).addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        ksession.insert( new Person( "Mark", 39 ) );
        ksession.insert( new Person( "Mario", 41 ) );

        ksession.fireAllRules();

        Collection<Result> results = (Collection<Result>) ksession.getObjects( new ClassObjectFilter( Result.class ) );
        assertEquals( 1, results.size() );
        assertEquals( "Mario", results.iterator().next().getValue() );
    }

    @Test
    public void testQueryInRuleWithDeclaration() {
        Variable<Person> personV = declarationOf( type( Person.class ) );
        Variable<Integer> ageV = declarationOf( type( Integer.class ) );

        Query2<Person, Integer> query = query( "olderThan", personV, ageV )
                .build( expr("exprA", personV, ageV, (p, a) -> p.getAge() > a) );

        Rule rule = rule("R")
                .build(
                        expr( "exprB", personV, p -> p.getName().startsWith( "M" ) ),
                        query.call(personV, valueOf(40)),
                        on(personV).execute((drools, p) -> drools.insert(new Result(p.getName())) )
                     );

        Model model = new ModelImpl().addQuery( query ).addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        ksession.insert( new Person( "Mark", 39 ) );
        ksession.insert( new Person( "Mario", 41 ) );
        ksession.insert( new Person( "Edson", 41 ) );

        ksession.fireAllRules();

        Collection<Result> results = (Collection<Result>) ksession.getObjects( new ClassObjectFilter( Result.class ) );
        assertEquals( 1, results.size() );
        assertEquals( "Mario", results.iterator().next().getValue() );
    }

    @Test
    public void testQueryInvokedWithGlobal() {
        Global<Integer> ageG = globalOf(type(Integer.class), "defaultpkg", "ageG");
        Variable<Person> personV = declarationOf( type( Person.class ) );
        Variable<Integer> ageV = declarationOf( type( Integer.class ) );

        Query2<Person, Integer> query = query("olderThan", personV, ageV)
                .build( expr("exprA", personV, ageV, (_this, $age) -> _this.getAge() > $age) );

        Rule rule = rule("R")
                .build(
                        expr( "exprB", personV, p -> p.getName().startsWith( "M" ) ),
                        query.call(personV, ageG),
                        on(personV).execute((drools, p) -> drools.insert(new Result(p.getName())) )
                     );

        Model model = new ModelImpl().addGlobal( ageG ).addQuery( query ).addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        ksession.setGlobal("ageG", 40);

        ksession.insert( new Person( "Mark", 39 ) );
        ksession.insert( new Person( "Mario", 41 ) );
        ksession.insert( new Person( "Edson", 41 ) );

        ksession.fireAllRules();

        Collection<Result> results = getObjects( ksession, Result.class );
        assertEquals( 1, results.size() );
        assertEquals( "Mario", results.iterator().next().getValue() );
    }

    @Test
    public void testNamedConsequence() {
        Variable<Result> resultV = declarationOf( type( Result.class ) );
        Variable<Person> markV = declarationOf( type( Person.class ) );
        Variable<Person> olderV = declarationOf( type( Person.class ) );

        Rule rule = rule( "beta" )
                .build(
                        expr("exprA", markV, p -> p.getName().equals("Mark"))
                                .indexedBy( String.class, ConstraintType.EQUAL, Person::getName, "Mark" )
                                .reactOn( "name", "age" ), // also react on age, see RuleDescr.lookAheadFieldsOfIdentifier
                        on(markV, resultV).execute((p, r) -> r.addValue( "Found " + p.getName())),
                        expr("exprB", olderV, p -> !p.getName().equals("Mark"))
                                .indexedBy( String.class, ConstraintType.NOT_EQUAL, Person::getName, "Mark" )
                                .reactOn( "name" ),
                        expr("exprC", olderV, markV, (p1, p2) -> p1.getAge() > p2.getAge())
                                .indexedBy( int.class, ConstraintType.GREATER_THAN, Person::getAge, Person::getAge )
                                .reactOn( "age" ),
                        on(olderV, markV, resultV).execute((p1, p2, r) -> r.addValue( p1.getName() + " is older than " + p2.getName()))
                     );

        Model model = new ModelImpl().addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );

        KieSession ksession = kieBase.newKieSession();

        Result result = new Result();
        ksession.insert( result );

        ksession.insert( new Person( "Mark", 37 ) );
        ksession.insert( new Person( "Edson", 35 ) );
        ksession.insert( new Person( "Mario", 40 ) );
        ksession.fireAllRules();

        Collection<String> results = (Collection<String>)result.getValue();
        assertEquals(2, results.size());

        assertTrue( results.containsAll( asList("Found Mark", "Mario is older than Mark") ) );
    }

    @Test
    public void testBreakingNamedConsequence() {
        Variable<Result> resultV = declarationOf( type( Result.class ) );
        Variable<Person> markV = declarationOf( type( Person.class ) );
        Variable<Person> olderV = declarationOf( type( Person.class ) );

        Rule rule = rule( "beta" )
                .build(
                        expr("exprA", markV, p -> p.getName().equals("Mark"))
                                .indexedBy( String.class, ConstraintType.EQUAL, Person::getName, "Mark" )
                                .reactOn( "name", "age" ),
                        when("cond1", markV, p -> p.getAge() < 30).then(
                            on(markV, resultV).breaking().execute((p, r) -> r.addValue( "Found young " + p.getName()))
                        ).elseWhen("cond2", markV, p -> p.getAge() > 50).then(
                            on(markV, resultV).breaking().execute((p, r) -> r.addValue( "Found old " + p.getName()))
                        ).elseWhen().then(
                            on(markV, resultV).breaking().execute((p, r) -> r.addValue( "Found " + p.getName()))
                        ),
                        expr("exprB", olderV, p -> !p.getName().equals("Mark"))
                                .indexedBy( String.class, ConstraintType.NOT_EQUAL, Person::getName, "Mark" )
                                .reactOn( "name" ),
                        expr("exprC", olderV, markV, (p1, p2) -> p1.getAge() > p2.getAge())
                                .indexedBy( int.class, ConstraintType.GREATER_THAN, Person::getAge, Person::getAge )
                                .reactOn( "age" ),
                        on(olderV, markV, resultV).execute((p1, p2, r) -> r.addValue( p1.getName() + " is older than " + p2.getName()))
                     );

        Model model = new ModelImpl().addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );
        KieSession ksession = kieBase.newKieSession();

        Result result = new Result();
        ksession.insert( result );

        ksession.insert( new Person( "Mark", 37 ) );
        ksession.insert( new Person( "Edson", 35 ) );
        ksession.insert( new Person( "Mario", 40 ) );
        ksession.fireAllRules();

        Collection<String> results = (Collection<String>)result.getValue();
        assertEquals(1, results.size());

        assertEquals( "Found Mark", results.iterator().next() );
    }

    @Test
    public void testFrom() throws Exception {
        Variable<Result> resultV = declarationOf( type( Result.class ) );
        Variable<Adult> dadV = declarationOf( type( Adult.class ) );
        Variable<Child> childV = declarationOf( type( Child.class ), from( dadV, Adult::getChildren ) );

        Rule rule = rule( "from" )
                .build(
                        expr("exprA", childV, c -> c.getAge() > 8),
                        on(childV, resultV).execute( (c,r) -> r.setValue( c.getName() ) )
                );

        Model model = new ModelImpl().addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );
        KieSession ksession = kieBase.newKieSession();

        Result result = new Result();
        ksession.insert( result );

        Adult dad = new Adult( "dad", 40 );
        dad.addChild( new Child( "Alan", 10 ) );
        dad.addChild( new Child( "Betty", 7 ) );
        ksession.insert( dad );
        ksession.fireAllRules();

        assertEquals("Alan", result.getValue());
   }

    @Test
    public void testConcatenatedFrom() {
        Global<List> listG = globalOf(type(List.class), "defaultpkg", "list");
        Variable<Man> manV = declarationOf( type( Man.class ) );
        Variable<Woman> wifeV = declarationOf( type( Woman.class ), from( manV, Man::getWife ) );
        Variable<Child> childV = declarationOf( type( Child.class ), from( wifeV, Woman::getChildren ) );
        Variable<Toy> toyV = declarationOf( type( Toy.class ), from( childV, Child::getToys ) );

        Rule rule = rule( "froms" )
                .build(
                        expr("exprA", childV, c -> c.getAge() > 10),
                        on(toyV, listG).execute( (t,l) -> l.add(t.getName()) )
                );

        Model model = new ModelImpl().addGlobal( listG ).addRule( rule );
        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel( model );
        KieSession ksession = kieBase.newKieSession();

        final List<String> list = new ArrayList<>();
        ksession.setGlobal( "list", list );

        final Woman alice = new Woman( "Alice", 38 );
        final Man bob = new Man( "Bob", 40 );
        bob.setWife( alice );

        final Child charlie = new Child( "Charles", 12 );
        final Child debbie = new Child( "Debbie", 10 );
        alice.addChild( charlie );
        alice.addChild( debbie );

        charlie.addToy( new Toy( "car" ) );
        charlie.addToy( new Toy( "ball" ) );
        debbie.addToy( new Toy( "doll" ) );

        ksession.insert( bob );
        ksession.fireAllRules();

        Assertions.assertThat(list).containsExactlyInAnyOrder("car", "ball");
    }
}
