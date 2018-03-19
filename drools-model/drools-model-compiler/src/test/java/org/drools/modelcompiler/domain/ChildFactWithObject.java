/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.modelcompiler.domain;

import java.util.Date;

public class ChildFactWithObject {

    private final int id;
    private final int parentId;

    private final Object objectValue;

    private int VAr;

    public ChildFactWithObject(final int id, final int parentId, final Object objectValue) {
        this.id = id;
        this.parentId = parentId;
        this.objectValue = objectValue;
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public Object getObjectValue() {
        return objectValue;
    }

    public Short getIdAsShort() {
        return (short) id;
    }

    public Boolean getIdIsEven() {
        return id % 2 == 0;
    }

    public Date getDate() {
        return new Date(id);
    }

    public int getVAr() {
        return VAr;
    }

    public void setVAr( int VAr ) {
        this.VAr = VAr;
    }
}
