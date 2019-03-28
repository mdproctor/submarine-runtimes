/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.core.factmodel.traits;

import org.drools.core.factmodel.MapCore;

import java.util.Map;

@Traitable( logical=true )
public class LogicalMapCore<K> extends MapCore {


    public LogicalMapCore( Map core ) {
        super( core );

        TraitFieldTMS tms = _getFieldTMS();
        Map<String,Object> props = _getDynamicProperties();
        for ( Entry<String,Object> propEntry : props.entrySet() ) {
            tms.registerField( Map.class, propEntry.getKey(), Object.class, propEntry.getValue(), null );
        }
    }
}
