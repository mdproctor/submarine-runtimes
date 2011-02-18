/*
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

package org.drools.rule;

import java.util.List;
import java.util.Map;

/**
 * FactType declarations are fact definitions (like classes) that
 * are managed alongside the rules.
 * You then communicate with the rulebase/knowledge base by using instances created by this.
 * There are utility set and get methods on this, as well as in the FieldAccessors.
 *
 * The Object that is used is a javabean (which was generated by the rules). You can also use reflection on it as normal.
 *
 */
public interface FactType
    extends
    java.io.Externalizable {

    public String getName();

    public List<FactField> getFields();

    public FactField getField(String name);

    public Class< ? > getFactClass();

    /**
     * Create a new fact based on the declared fact type.
     * This object will normally be a javabean.
     */
    public Object newInstance() throws InstantiationException,
                               IllegalAccessException;

    /**
     * Set the value of the field on a dynamic fact.
     */
    public void set(Object bean, String field, Object value);

    /**
     * get the value of the specified field on the dynamic fact.
     */
    public Object get(Object bean, String field);

    /**
     * Get a map of the fields and their values for the bean.
     */
    public Map<String, Object> getAsMap(Object bean);

    /**
     * Set the values of the bean from a map.
     */
    public void setFromMap(Object bean, Map<String, Object> values);
}
