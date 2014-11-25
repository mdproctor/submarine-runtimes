/*
 * Copyright 2012 JBoss Inc
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

package org.drools.workbench.models.datamodel.rule;

public class FromEntryPointFactPattern extends FromCompositeFactPattern {

    private String entryPointName;

    public String getEntryPointName() {
        return entryPointName;
    }

    public void setEntryPointName( String entryPointName ) {
        this.entryPointName = entryPointName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FromEntryPointFactPattern that = (FromEntryPointFactPattern) o;

        if (entryPointName != null ? !entryPointName.equals(that.entryPointName) : that.entryPointName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (entryPointName != null ? entryPointName.hashCode() : 0);
        return result;
    }
}
