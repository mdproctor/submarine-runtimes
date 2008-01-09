package org.drools.process.core.impl;/* * Copyright 2005 JBoss Inc *  * Licensed under the Apache License, Version 2.0 (the "License"); * you may not use this file except in compliance with the License. * You may obtain a copy of the License at *  *      http://www.apache.org/licenses/LICENSE-2.0 *  * Unless required by applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the License. */import java.io.Serializable;import java.util.List;import org.drools.process.core.Process;import org.drools.process.core.Variable;import org.drools.process.core.VariableScope;/** * Default implementation of a Process *  * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a> */public class ProcessImpl implements Process, Serializable {        private static final long serialVersionUID = 400L;    private String            id;    private String            name;    private String            version;    private String            type;    private String			  packageName;    private VariableScope     variableScope = new VariableScopeImpl();    public void setId(final String id) {        this.id = id;    }    public String getId() {        return this.id;    }    public void setName(final String name) {        this.name = name;    }    public String getName() {        return this.name;    }    public void setVersion(final String version) {        this.version = version;    }    public String getVersion() {        return this.version;    }    public String getType() {        return this.type;    }    public void setType(final String type) {        this.type = type;    }	public String getPackageName() {		return packageName;	}	public void setPackageName(String packageName) {		this.packageName = packageName;	}    public List<Variable> getVariables() {        return variableScope.getVariables();    }    public void setVariables(final List<Variable> variables) {        variableScope.setVariables(variables);    }    public String[] getVariableNames() {        return variableScope.getVariableNames();    }    public boolean equals(final Object o) {        if ( o instanceof ProcessImpl ) {            return ((ProcessImpl) o).getName().equals( this.name ) && ((ProcessImpl) o).getVersion().equals( this.version );        }        return false;    }    public int hashCode() {        return this.name.hashCode() + 3 * this.version.hashCode();    }}