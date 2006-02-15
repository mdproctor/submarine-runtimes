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

import org.drools.spi.Activation;

/**
 * This is wrapper for drools activation to keep track of activations
 * that were revoked from the agenda due to the fact retraction
 * 
 * @author Alexander Bagerman
 * 
 */
class PostedActivation {
	boolean removed;

	Activation activation;

	public PostedActivation(Activation activation) {
		this.activation = activation;
		this.removed = false;
	}

	protected boolean isRemoved() {
		return this.removed;
	}

	protected void setRemoved() {
		this.removed = true;
	}

	protected Activation getActivation() {
		return this.activation;
	}

}
