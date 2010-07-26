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

package org.drools.reteoo.test.parser;

/**
 * Simple enum to identify a paraphrase type. This enum is used to better format
 * error messages during parsing.
 * 
 * @author porcelli
 */
public enum DroolsParaphraseTypes {
	TESTCASE, SETUP, TEARDOWN, TEST, STEP, COMMAND;
}