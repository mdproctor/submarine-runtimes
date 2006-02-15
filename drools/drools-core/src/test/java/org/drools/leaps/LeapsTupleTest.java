package org.drools.leaps;

/*
 * Copyright 2006 Alexander Bagerman
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

import junit.framework.TestCase;

import org.drools.FactHandle;
import org.drools.common.AgendaItem;

public class LeapsTupleTest extends TestCase {

	LeapsTuple tuple;

	FactHandleImpl h1;

	FactHandleImpl h2;

	FactHandleImpl h3;

	FactHandleImpl h4;

	FactHandleImpl hOutsider;

	protected void setUp() throws Exception {
		super.setUp();
		this.h1 = new FactHandleImpl(1, "one");
		this.h2 = new FactHandleImpl(2, "two");
		this.h3 = new FactHandleImpl(3, "three");
		this.h4 = new FactHandleImpl(4, "four");
		FactHandleImpl arr[] = { this.h1, this.h2, this.h3, this.h4 };
		this.tuple = new LeapsTuple(arr);

		this.hOutsider = new FactHandleImpl(9876, "outsider");
	}

	/*
	 * Test method for 'org.drools.leaps.LeapsTuple.dependsOn(FactHandle)'
	 */
	public void testDependsOn() {
		assertTrue(this.tuple.dependsOn(this.h3));
		assertFalse(this.tuple.dependsOn(this.hOutsider));
	}

	/*
	 * Test method for 'org.drools.leaps.LeapsTuple.get(int)'
	 */
	public void testGetInt() {
		assertEquals(this.tuple.get(3), this.h4);

	}

	/*
	 * Test method for 'org.drools.leaps.LeapsTuple.get(Declaration)'
	 */
	public void testGetDeclaration() {

	}

	/*
	 * Test method for 'org.drools.leaps.LeapsTuple.getFactHandles()'
	 */
	public void testGetFactHandles() {
		FactHandle[] arr = this.tuple.getFactHandles();
		for (int i = 0; i < arr.length; i++) {
			assertEquals(arr[0], this.h1);
			assertEquals(arr[1], this.h2);
			assertEquals(arr[2], this.h3);
			assertEquals(arr[3], this.h4);

		}
	}

	/*
	 * Test method for 'org.drools.leaps.LeapsTuple.isActivationNull()'
	 */
	public void testIsActivationNull() {
		AgendaItem item = new AgendaItem(0L, this.tuple, null, null, null);
		assertTrue(this.tuple.isActivationNull());
		this.tuple.setActivation(item);
		assertFalse(this.tuple.isActivationNull());
	}

	/*
	 * Test method for 'org.drools.leaps.LeapsTuple.equals(Object)'
	 */
	public void testEqualsObject() {
		FactHandleImpl arr[] = { this.h1, this.h2, this.h3, this.h4 };
		LeapsTuple tupleToCompare = new LeapsTuple(arr);
		assertEquals(this.tuple, tupleToCompare);

	}

}
