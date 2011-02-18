/**
 * Copyright 2010 JBoss Inc
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

package org.drools.verifier.report.components;

/**
 * Partial redundancy between left and right. Redundancy stores the connection
 * between left and right.
 * 
 * @author Toni Rikkola
 */
public class PartialRedundancy {

	private final Cause left;
	private final Cause right;
	private final Redundancy redundancy;

	/**
	 * 
	 * @param left
	 *            Left side parent.
	 * @param right
	 *            Right side parent.
	 * @param redundancy
	 *            Connection between left and right.
	 */
	public PartialRedundancy(Cause left, Cause right, Redundancy redundancy) {
		this.left = left;
		this.right = right;
		this.redundancy = redundancy;
	}

	public Cause getLeft() {
		return left;
	}

	public Redundancy getRedundancy() {
		return redundancy;
	}

	public Cause getRight() {
		return right;
	}

}
