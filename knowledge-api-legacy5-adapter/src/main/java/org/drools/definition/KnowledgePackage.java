/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
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

package org.drools.definition;

import java.util.Collection;

import org.drools.definition.process.Process;
import org.drools.definition.rule.Global;
import org.drools.definition.rule.Query;
import org.drools.definition.rule.Rule;
import org.drools.definition.type.FactType;

/**
 * This provides a collection of knowledge definitions that can be given to a KnowledgeBase.
 * The name is used to provide "namespace" separation of those definitions.
 */
public interface KnowledgePackage {

    /**
     * The namespace for this package
     * @return
     */
    String getName();

    /**
     * Return the rule definitions for this package.
     * The collection is immutable.
     * 
     * @return
     */
    Collection<Rule> getRules();

    /**
     * Return the process definitions for this package.
     * The collection is immutable.
     * 
     * @return a Collection of Processes for this package.
     */
    Collection<Process> getProcesses();

    /**
     * Return the fact types declared in this package
     * The collection is immutable.
     *
     * @return a Collection of FactType for this package
     */
    Collection<FactType> getFactTypes();

    /**
     * Return the query definitions for this package.
     * The collection is immutable.
     *
     * @return a Collection of Query for this package
     */
    Collection<Query> getQueries();

    /**
     * Return the names of the functions defined in this package.
     * The collection is immutable.
     *
     * @return a Collection of Function names for this package
     */
    Collection<String> getFunctionNames();

    /**
     * Return the names of the globals defined in this package.
     * The collection is immutable.
     *
     * @return a Collection of Global names for this package
     */
    Collection<Global> getGlobalVariables();

}
