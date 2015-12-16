/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
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

package org.drools.template.model;

/**
 * Wrapper for declarative types. Declared Types must be written in the
 * appropriate style, no formatting is contributed here.
 */
public class DeclaredType
        implements
        DRLJavaEmitter {

    private String declaredTypeListing;

    public void setDeclaredTypeListing(final String declaredTypeListing) {
        this.declaredTypeListing = declaredTypeListing;
    }

    public void renderDRL(final DRLOutput out) {
        if (this.declaredTypeListing != null) {
            out.writeLine(this.declaredTypeListing);
        }
    }

}
