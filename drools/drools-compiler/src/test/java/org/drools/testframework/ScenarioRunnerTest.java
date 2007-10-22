package org.drools.testframework;

import java.util.HashSet;

import org.drools.Cheese;
import org.drools.Person;
import org.drools.base.ClassTypeResolver;
import org.drools.base.TypeResolver;
import org.drools.brms.client.modeldriven.testing.FactData;
import org.drools.brms.client.modeldriven.testing.FieldData;
import org.drools.brms.client.modeldriven.testing.Scenario;
import org.drools.brms.client.modeldriven.testing.VerifyFact;
import org.drools.brms.client.modeldriven.testing.VerifyField;

import junit.framework.TestCase;

public class ScenarioRunnerTest extends TestCase {

	public void testPopulateFacts() throws Exception {
		Scenario sc = new Scenario();
		sc.facts = new FactData[] { new FactData("Cheese", "c1", new FieldData[] {
																			new FieldData("type", "cheddar", false),
																			new FieldData("price", "42", false)
																		}),
									new FactData("Person", "p1", new FieldData[] {
																			new FieldData("name", "mic", false),
																			new FieldData("age", "30 + 3", true)
									})};

		TypeResolver resolver = new ClassTypeResolver(new HashSet(), Thread.currentThread().getContextClassLoader());
		resolver.addImport("org.drools.Cheese");
		resolver.addImport("org.drools.Person");
		ScenarioRunner runner = new ScenarioRunner(sc, resolver);

		assertTrue(runner.populatedData.containsKey("c1"));
		assertTrue(runner.populatedData.containsKey("p1"));

		Cheese c = (Cheese) runner.populatedData.get("c1");
		assertEquals("cheddar", c.getType());
		assertEquals(42, c.getPrice());

		Person p = (Person) runner.populatedData.get("p1");
		assertEquals("mic", p.getName());
		assertEquals(33, p.getAge());

	}

	public void testVerifyFacts() throws Exception {

		ScenarioRunner runner = new ScenarioRunner(new Scenario(), null);
		Cheese f1 = new Cheese("cheddar", 42);
		runner.populatedData.put("f1", f1);

		Person f2 = new Person("michael", 33);
		runner.populatedData.put("f2", f2);

		//test all true
		VerifyFact vf = new VerifyFact();
		vf.factName = "f1";
		vf.fieldValues = new VerifyField[] { new VerifyField("type", "cheddar"), new VerifyField("price", "42") } ;

		runner.verify(vf);
		for (int i = 0; i < vf.fieldValues.length; i++) {
			assertTrue(vf.fieldValues[i].success);
		}

		vf = new VerifyFact();
		vf.factName = "f2";
		vf.fieldValues = new VerifyField[] { new VerifyField("name", "michael"), new VerifyField("age", "33") } ;

		runner.verify(vf);
		for (int i = 0; i < vf.fieldValues.length; i++) {
			assertTrue(vf.fieldValues[i].success);
		}

		//test one false
		vf = new VerifyFact();
		vf.factName = "f2";
		vf.fieldValues = new VerifyField[] { new VerifyField("name", "mark"), new VerifyField("age", "33") } ;

		runner.verify(vf);
		assertFalse(vf.fieldValues[0].success);
		assertTrue(vf.fieldValues[1].success);

		assertEquals("michael", vf.fieldValues[0].actual);
		assertEquals("mark", vf.fieldValues[0].expected);


		//test 2 false
		vf = new VerifyFact();
		vf.factName = "f2";
		vf.fieldValues = new VerifyField[] { new VerifyField("name", "mark"), new VerifyField("age", "32") } ;

		runner.verify(vf);
		assertFalse(vf.fieldValues[0].success);
		assertFalse(vf.fieldValues[1].success);

		assertEquals("michael", vf.fieldValues[0].actual);
		assertEquals("mark", vf.fieldValues[0].expected);

		assertEquals("33", vf.fieldValues[1].actual);
		assertEquals("32", vf.fieldValues[1].expected);

	}



}
