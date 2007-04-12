/*
 * Copyright 2006 JBoss Inc
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

package org.drools.lang.descr;

/**
 * @author etirelli
 *
 */
public class ImportDescr extends BaseDescr {

    private static final long serialVersionUID = -5578158193651898438L;
    private String            target;

    public ImportDescr() {
    }

    public ImportDescr(final String clazzName) {
        this.target = clazzName;
    }

    public String getTarget() {
        return this.target;
    }

    public void setTarget(final String clazzName) {
        this.target = clazzName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((this.target == null) ? 0 : this.target.hashCode());
        result = PRIME * result + this.getStartCharacter();
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final ImportDescr other = (ImportDescr) obj;
        if ( this.target == null ) {
            if ( other.target != null ) {
                return false;
            }
        } else if ( !this.target.equals( other.target ) ) {
            return false;
        }
        return this.getStartCharacter() == other.getStartCharacter();
    }

    public String toString() {
        return "import " + this.target;
    }

}
