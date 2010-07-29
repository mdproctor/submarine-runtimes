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

package org.drools.process.core.context.swimlane;

import java.io.Serializable;

public class Swimlane implements Serializable {

	private static final long serialVersionUID = 510l;
	
	private String name;
	private String actorId;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
	    this.name = name;
	}
	
	public void setActorId(String actorId) {
		this.actorId = actorId;
	}
	
	public String getActorId() {
		return this.actorId;
	}
	
	public String toString() {
	    return name;
	}

}
