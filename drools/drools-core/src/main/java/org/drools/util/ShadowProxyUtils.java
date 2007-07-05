/*
 * Copyright 2007 JBoss Inc
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
 *
 * Created on Jul 5, 2007
 */
package org.drools.util;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * A class with simple utility methods for shadowing
 * 
 * @author etirelli
 */
public class ShadowProxyUtils {

    private ShadowProxyUtils() {
    }

    public static Object cloneObject(Object original) {
        Object clone = null;
        if ( original instanceof Cloneable ) {
            try {
                Method cloneMethod = original.getClass().getMethod( "clone",
                                                                    new Class[0] );
                clone = cloneMethod.invoke( original,
                                            new Object[0] );
            } catch ( Exception e ) {
                // Nothing to do
            }
        }

        if ( clone == null ) {
            try {
                if ( original instanceof Map ) {
                    clone = original.getClass().newInstance();
                    ((Map) clone).putAll( (Map) original );
                } else if ( original instanceof Collection ) {
                    clone = original.getClass().newInstance();
                    ((Collection) clone).addAll( (Collection) original );
                } else if ( original.getClass().isArray() ) {
                    clone = cloneArray( original );
                }
            } catch ( Exception e ) {
                e.printStackTrace();
                // nothing to do
            }
        }

        if ( clone == null ) {
            clone = original;
        }

        return clone;
    }

    public static Object cloneArray(Object original) {
        Object result = null;

        if ( original.getClass().isArray() ) {
            final int arrayLength = Array.getLength( original );

            if ( arrayLength == 0 ) {
                // empty arrays are immutable
                result = original;
            } else {
                final Class componentType = original.getClass().getComponentType();

                // Even though arrays implicitly have a public clone(), it
                // cannot be invoked reflectively, so need to do copy construction:
                result = Array.newInstance( componentType,
                                            arrayLength );
                
                if( componentType.isArray() ) {
                    for( int i = 0; i < arrayLength; i++ ) {
                        Array.set( result, i, cloneArray( Array.get( original, i ) ) );
                    }
                } else {
                    System.arraycopy( original,
                                      0,
                                      result,
                                      0,
                                      arrayLength );
                }
            }
        }
        return result;
    }

}
