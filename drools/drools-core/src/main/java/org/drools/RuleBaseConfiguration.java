/*
 * Copyright 2005 JBoss Inc
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

package org.drools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.drools.common.AgendaGroupFactory;
import org.drools.common.ArrayAgendaGroupFactory;
import org.drools.common.PriorityQueueAgendaGroupFactory;
import org.drools.concurrent.ExecutorService;
import org.drools.spi.ConflictResolver;
import org.drools.util.ChainedProperties;

/**
 * RuleBaseConfiguration
 * 
 * A class to store RuleBase related configuration. It must be used at rule base instantiation time
 * or not used at all.
 * This class will automatically load default values from system properties, so if you want to set
 * a default configuration value for all your new rule bases, you can simply set the property as 
 * a System property.
 * 
 * After RuleBase is created, it makes the configuration immutable and there is no way to make it 
 * mutable again. This is to avoid inconsistent behavior inside rulebase.
 * 
 * NOTE: This API is under review and may change in the future.
 *
 * Created: 16/05/2006
 * @author <a href="mailto:tirelli@post.com">Edson Tirelli</a> 
 *
 * @version $Id$
 */

/**
 * drools.removeIdentities = <true|false>
 * drools.shareAlphaNodes  = <true|false>
 * drools.shareBetaNodes = <true|false>
 * drools.alphaMemory <true/false>
 * drools.alphaNodeHashingThreshold = <1...n>
 * drools.compositeKeyDepth  =<1..3>
 * drools.indexLeftBetaMemory = <true/false>
 * drools.indexRightBetaMemory = <true/false>
 * drools.assertBehaviour = <IDENTITY|EQUALITY>
 * drools.logicalOverride = <DISCARD|PRESERVE>
 * drools.executorService = <qualified class name>
 * drools.conflictResolver = <qualified class name>
 * 
 * drools.shadowproxy.exclude = org.domainy.* org.domainx.ClassZ
 */
public class RuleBaseConfiguration
    implements
    Serializable {
    private static final long   serialVersionUID = 320L;

    private ChainedProperties   chainedProperties;

    private boolean             immutable;
    
    private boolean             sequential;

    private boolean             maintainTms;
    private boolean             removeIdentities;
    private boolean             shareAlphaNodes;
    private boolean             shareBetaNodes;
    private boolean             alphaMemory;
    private int                 alphaNodeHashingThreshold;
    private int                 compositeKeyDepth;
    private boolean             indexLeftBetaMemory;
    private boolean             indexRightBetaMemory;
    private AssertBehaviour     assertBehaviour;
    private LogicalOverride     logicalOverride;
    private ExecutorService     executorService;

    private ConflictResolver    conflictResolver;

    private Map                 shadowProxyExcludes;
    private static final String STAR             = "*";

    public RuleBaseConfiguration(Properties properties) {
        init( properties );
    }

    public RuleBaseConfiguration() {
        init( null );
    }

    private void init(Properties properties) {
        this.immutable = false;

        this.chainedProperties = new ChainedProperties( "rulebase.conf" );

        if ( properties != null ) {
            this.chainedProperties.addProperties( properties );
        }
        
        setSequential( Boolean.valueOf( this.chainedProperties.getProperty( "drools.sequential",
                        "false" ) ).booleanValue() );        

        setMaintainTms( Boolean.valueOf( this.chainedProperties.getProperty( "drools.maintainTms",
                                                                             "true" ) ).booleanValue() );

        setRemoveIdentities( Boolean.valueOf( this.chainedProperties.getProperty( "drools.removeIdentities",
                                                                                  "false" ) ).booleanValue() );

        setAlphaMemory( Boolean.valueOf( this.chainedProperties.getProperty( "drools.alphaMemory",
                                                                             "false" ) ).booleanValue() );

        setShareAlphaNodes( Boolean.valueOf( this.chainedProperties.getProperty( "drools.shareAlphaNodes",
                                                                                 "true" ) ).booleanValue() );

        setShareBetaNodes( Boolean.valueOf( this.chainedProperties.getProperty( "drools.shareBetaNodes",
                                                                                "true" ) ).booleanValue() );

        setAlphaNodeHashingThreshold( Integer.parseInt( this.chainedProperties.getProperty( "drools.alphaNodeHashingThreshold",
                                                                                            "3" ) ) );

        setCompositeKeyDepth( Integer.parseInt( this.chainedProperties.getProperty( "drools.compositeKeyDepth",
                                                                                    "3" ) ) );

        setIndexLeftBetaMemory( Boolean.valueOf( this.chainedProperties.getProperty( "drools.indexLeftBetaMemory",
                                                                                     "true" ) ).booleanValue() );
        setIndexRightBetaMemory( Boolean.valueOf( this.chainedProperties.getProperty( "drools.indexRightBetaMemory",
                                                                                      "true" ) ).booleanValue() );

        setAssertBehaviour( AssertBehaviour.determineAssertBehaviour( this.chainedProperties.getProperty( "drools.assertBehaviour",
                                                                                                          "IDENTITY" ) ) );
        setLogicalOverride( LogicalOverride.determineLogicalOverride( this.chainedProperties.getProperty( "drools.logicalOverride",
                                                                                                          "DISCARD" ) ) );

        setExecutorService( RuleBaseConfiguration.determineExecutorService( this.chainedProperties.getProperty( "drools.executorService",
                                                                                                                "org.drools.concurrent.DefaultExecutorService" ) ) );

        setConflictResolver( RuleBaseConfiguration.determineConflictResolver( this.chainedProperties.getProperty( "drools.conflictResolver",
                                                                                                                  "org.drools.conflict.DepthConflictResolver" ) ) );

        setShadowProxyExcludes( this.chainedProperties.getProperty( "drools.shadowProxyExcludes",
                                                                    "" ) );
    }

    /**
     * Makes the configuration object immutable. Once it becomes immutable, 
     * there is no way to make it mutable again. 
     * This is done to keep consistency.
     */
    public void makeImmutable() {
        this.immutable = true;
    }

    /**
     * Returns true if this configuration object is immutable or false otherwise.
     * @return
     */
    public boolean isImmutable() {
        return this.immutable;
    }

    private void checkCanChange() {
        if ( this.immutable ) {
            throw new UnsupportedOperationException( "Can't set a property after configuration becomes immutable" );
        }
    }
    
    public void setSequential(boolean sequential) {
        this.sequential = sequential;
    }
    
    public boolean isSequential() {
        return this.sequential;
    }

    public boolean isMaintainTms() {
        return this.maintainTms;
    }

    public void setMaintainTms(final boolean maintainTms) {
        checkCanChange(); // throws an exception if a change isn't possible;
        this.maintainTms = maintainTms;
    }

    public boolean isRemoveIdentities() {
        return this.removeIdentities;
    }

    public void setRemoveIdentities(final boolean removeIdentities) {
        checkCanChange(); // throws an exception if a change isn't possible;
        this.removeIdentities = removeIdentities;
    }

    public boolean isAlphaMemory() {
        return this.alphaMemory;
    }

    public void setAlphaMemory(final boolean alphaMemory) {
        checkCanChange(); // throws an exception if a change isn't possible;
        this.alphaMemory = alphaMemory;
    }

    public boolean isShareAlphaNodes() {
        return this.shareAlphaNodes;
    }

    public void setShareAlphaNodes(final boolean shareAlphaNodes) {
        checkCanChange(); // throws an exception if a change isn't possible;
        this.shareAlphaNodes = shareAlphaNodes;
    }

    public boolean isShareBetaNodes() {
        return this.shareBetaNodes;
    }

    public void setShareBetaNodes(final boolean shareBetaNodes) {
        checkCanChange(); // throws an exception if a change isn't possible;
        this.shareBetaNodes = shareBetaNodes;
    }

    public int getAlphaNodeHashingThreshold() {
        return this.alphaNodeHashingThreshold;
    }

    public void setAlphaNodeHashingThreshold(final int alphaNodeHashingThreshold) {
        checkCanChange(); // throws an exception if a change isn't possible;        
        this.alphaNodeHashingThreshold = alphaNodeHashingThreshold;
    }

    public AssertBehaviour getAssertBehaviour() {
        return this.assertBehaviour;
    }

    public void setAssertBehaviour(final AssertBehaviour assertBehaviour) {
        checkCanChange(); // throws an exception if a change isn't possible;
        this.assertBehaviour = assertBehaviour;
    }

    public int getCompositeKeyDepth() {
        return this.compositeKeyDepth;
    }

    public void setCompositeKeyDepth(final int compositeKeyDepth) {
        if ( !this.immutable ) {
            if ( compositeKeyDepth > 3 ) {
                throw new UnsupportedOperationException( "compositeKeyDepth cannot be greater than 3" );
            }
            this.compositeKeyDepth = compositeKeyDepth;
        } else {
            throw new UnsupportedOperationException( "Can't set a property after configuration becomes immutable" );
        }
    }

    public boolean isIndexLeftBetaMemory() {
        return this.indexLeftBetaMemory;
    }

    public void setIndexLeftBetaMemory(final boolean indexLeftBetaMemory) {
        checkCanChange(); // throws an exception if a change isn't possible;
        this.indexLeftBetaMemory = indexLeftBetaMemory;
    }

    public boolean isIndexRightBetaMemory() {
        return this.indexRightBetaMemory;
    }

    public void setIndexRightBetaMemory(final boolean indexRightBetaMemory) {
        checkCanChange(); // throws an exception if a change isn't possible;
        this.indexRightBetaMemory = indexRightBetaMemory;
    }

    public LogicalOverride getLogicalOverride() {
        return this.logicalOverride;
    }

    public void setLogicalOverride(final LogicalOverride logicalOverride) {
        checkCanChange(); // throws an exception if a change isn't possible;
        this.logicalOverride = logicalOverride;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        checkCanChange(); // throws an exception if a change isn't possible;    	
        this.executorService = executorService;
    }

    private static ExecutorService determineExecutorService(String className) {
        Class clazz = null;
        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass( className );
        } catch ( ClassNotFoundException e ) {
        }

        if ( clazz == null ) {
            try {
                clazz = RuleBaseConfiguration.class.getClassLoader().loadClass( className );
            } catch ( ClassNotFoundException e ) {
            }
        }

        if ( clazz != null ) {
            try {
                return (ExecutorService) clazz.newInstance();
            } catch ( Exception e ) {
                throw new IllegalArgumentException( "Unable to instantiate ExecutorService '" + className + "'" );
            }
        } else {
            throw new IllegalArgumentException( "ExecutorService '" + className + "' not found" );
        }
    }

    public static class AssertBehaviour
        implements
        Serializable {
        private static final long           serialVersionUID = 320L;

        public static final AssertBehaviour IDENTITY         = new AssertBehaviour( 0 );
        public static final AssertBehaviour EQUALITY         = new AssertBehaviour( 1 );

        private int                         value;

        private AssertBehaviour(final int value) {
            this.value = value;
        }

        public static AssertBehaviour determineAssertBehaviour(final String value) {
            if ( value.equals( "IDENTITY" ) ) {
                return IDENTITY;
            } else if ( value.equals( "EQUALITY" ) ) {
                return EQUALITY;
            } else {
                throw new IllegalArgumentException( "Illegal enum value '" + value + "' for AssertBehaviour" );
            }
        }

        private Object readResolve() throws java.io.ObjectStreamException {
            switch ( this.value ) {
                case 0 :
                    return IDENTITY;
                case 1 :
                    return EQUALITY;
                default :
                    throw new IllegalArgumentException( "Illegal enum value '" + this.value + "' for AssertBehaviour" );
            }
        }

        public String toString() {
            return "AssertBehaviour : " + ((this.value == 0) ? "identity" : "equality");
        }
    }

    public static class LogicalOverride
        implements
        Serializable {
        private static final long           serialVersionUID = 320L;

        public static final LogicalOverride PRESERVE         = new LogicalOverride( 0 );
        public static final LogicalOverride DISCARD          = new LogicalOverride( 1 );

        private int                         value;

        private LogicalOverride(final int value) {
            this.value = value;
        }

        public static LogicalOverride determineLogicalOverride(final String value) {
            if ( value.equals( "PRESERVE" ) ) {
                return PRESERVE;
            } else if ( value.equals( "DISCARD" ) ) {
                return DISCARD;
            } else {
                throw new IllegalArgumentException( "Illegal enum value '" + value + "' for LogicalOverride" );
            }
        }

        private Object readResolve() throws java.io.ObjectStreamException {
            switch ( this.value ) {
                case 0 :
                    return PRESERVE;
                case 1 :
                    return DISCARD;
                default :
                    throw new IllegalArgumentException( "Illegal enum value '" + this.value + "' for LogicalOverride" );
            }
        }

        public String toString() {
            return "LogicalOverride : " + ((this.value == 0) ? "preserve" : "discard");
        }
    }

    public AgendaGroupFactory getAgendaGroupFactory() {
        if ( isSequential() ) {
            return ArrayAgendaGroupFactory.getInstance();
        } else {
            return PriorityQueueAgendaGroupFactory.getInstance();
        }
    }
    
    private static ConflictResolver determineConflictResolver(String className) {
        Class clazz = null;
        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass( className );
        } catch ( ClassNotFoundException e ) {
        }

        if ( clazz == null ) {
            try {
                clazz = RuleBaseConfiguration.class.getClassLoader().loadClass( className );
            } catch ( ClassNotFoundException e ) {
            }
        }

        if ( clazz != null ) {
            try {
                return (ConflictResolver) clazz.getMethod( "getInstance",
                                                           null ).invoke( null,
                                                                          null );
            } catch ( Exception e ) {
                throw new IllegalArgumentException( "Unable to Conflict Resolver '" + className + "'" );
            }
        } else {
            throw new IllegalArgumentException( "conflict Resolver '" + className + "' not found" );
        }
    }

    public void setConflictResolver(ConflictResolver conflictResolver) {
        this.conflictResolver = conflictResolver;
    }

    public ConflictResolver getConflictResolver() {
        return this.conflictResolver;
    }

    private void setShadowProxyExcludes(String excludes) {
        if ( excludes == null || "".equals( excludes.trim() ) ) {
            return;
        }

        if ( this.shadowProxyExcludes == null ) {
            this.shadowProxyExcludes = new HashMap();
        }

        String[] items = excludes.split( " " );
        for ( int i = 0; i < items.length; i++ ) {
            String qualifiedNamespace = items[i].substring( 0,
                                                            items[i].lastIndexOf( '.' ) ).trim();
            String name = items[i].substring( items[i].lastIndexOf( '.' ) + 1 ).trim();
            Object object = this.shadowProxyExcludes.get( qualifiedNamespace );
            if ( object == null ) {
                if ( STAR.equals( name ) ) {
                    this.shadowProxyExcludes.put( qualifiedNamespace,
                                                  STAR );
                } else {
                    // create a new list and add it
                    List list = new ArrayList();
                    list.add( name );
                    this.shadowProxyExcludes.put( qualifiedNamespace,
                                                  list );
                }
            } else if ( name.equals( STAR ) ) {
                // if its a STAR now add it anyway, we don't care if it was a STAR or a List before
                this.shadowProxyExcludes.put( qualifiedNamespace,
                                              STAR );
            } else {
                // its a list so add it if it doesn't already exist
                List list = (List) object;
                if ( !list.contains( object ) ) {
                    list.add( name );
                }
            }
        }
    }

    public boolean isShadowed(String className) {
        if ( this.shadowProxyExcludes == null ) {
            return true;
        }

        String qualifiedNamespace = className.substring( 0,
                                                         className.lastIndexOf( '.' ) ).trim();
        String name = className.substring( className.lastIndexOf( '.' ) + 1 ).trim();
        Object object = this.shadowProxyExcludes.get( qualifiedNamespace );
        if ( object == null ) {
            return true;
        } else if ( STAR.equals( object ) ) {
            return false;
        } else {
            List list = (List) object;
            return !list.contains( name );
        }
    }

}
