/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.drools.compiler.integrationtests.facts;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private final String id;
    private final String category;

    private List<String> firings = new ArrayList<>();

    private String description = "";

    public Product(final String id, final String category ) {
        this.id = id;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public List<String> getFirings() {
        return firings;
    }

    public String getCategory() {
        return category;
    }

    public CategoryTypeEnum getCategoryAsEnum() {
        return CategoryTypeEnum.fromString(category);
    }

    public String getDescription() {
        return description;
    }

    public void appendDescription( final String append ) {
        description += append;
    }
}
