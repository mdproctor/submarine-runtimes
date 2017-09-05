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

package org.drools.compiler.kie.builder.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.management.ObjectName;

import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.compiler.compiler.PackageBuilderErrors;
import org.drools.compiler.kie.util.ChangeSetBuilder;
import org.drools.compiler.kie.util.KieJarChangeSet;
import org.drools.compiler.kproject.models.KieBaseModelImpl;
import org.drools.compiler.kproject.models.KieSessionModelImpl;
import org.drools.compiler.management.KieContainerMonitor;
import org.drools.core.base.ClassObjectType;
import org.drools.core.common.ClassAwareObjectStore;
import org.drools.core.common.InternalWorkingMemory;
import org.drools.core.common.InternalWorkingMemoryEntryPoint;
import org.drools.core.common.ProjectClassLoader;
import org.drools.core.definitions.InternalKnowledgePackage;
import org.drools.core.definitions.impl.KnowledgePackageImpl;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.impl.InternalKieContainer;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.drools.core.impl.StatelessKnowledgeSessionImpl;
import org.drools.core.management.DroolsManagementAgent;
import org.drools.core.management.DroolsManagementAgent.CBSKey;
import org.drools.core.reteoo.EntryPointNode;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.FileLoggerModel;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.MBeansOption;
import org.kie.api.event.KieRuntimeEventManager;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.logger.KieLoggers;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.internal.builder.ChangeType;
import org.kie.internal.builder.CompositeKnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.builder.ResourceChange;
import org.kie.internal.builder.ResourceChangeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.drools.compiler.kie.builder.impl.KieBuilderImpl.filterFileInKBase;
import static org.drools.compiler.kie.util.InjectionHelper.wireListnersAndWIHs;
import static org.drools.core.util.ClassUtils.convertResourceToClassName;
import static org.drools.core.util.Drools.isJndiAvailable;

public class KieContainerImpl
    implements
    InternalKieContainer {

    private static final Logger        log    = LoggerFactory.getLogger( KieContainerImpl.class );

    private KieProject           kProject;

    private final Map<String, KieBase> kBases = new ConcurrentHashMap<String, KieBase>();

    private final Map<String, KieSession> kSessions = new ConcurrentHashMap<String, KieSession>();
    private final Map<String, StatelessKieSession> statelessKSessions = new ConcurrentHashMap<String, StatelessKieSession>();

    private final KieRepository        kr;

    private ReleaseId configuredReleaseId;
    private ReleaseId containerReleaseId;

	private final String containerId;

    public KieModule getMainKieModule() {
        return kr.getKieModule(getReleaseId());
    }
    
    /**
     * Please note: the recommended way of getting a KieContainer is relying on {@link org.kie.api.KieServices KieServices} API,
     * for example: {@link org.kie.api.KieServices#newKieContainer(ReleaseId) KieServices.newKieContainer(...)}.
     * The direct manual call to KieContainerImpl constructor instead would not guarantee the consistency of the supplied containerId.
     */
    public KieContainerImpl(KieProject kProject, KieRepository kr) {
        this("impl"+UUID.randomUUID(), kProject, kr);
    }
    
    /**
     * Please note: the recommended way of getting a KieContainer is relying on {@link org.kie.api.KieServices KieServices} API,
     * for example: {@link org.kie.api.KieServices#newKieContainer(ReleaseId) KieServices.newKieContainer(...)}.
     * The direct manual call to KieContainerImpl constructor instead would not guarantee the consistency of the supplied containerId.
     */    
    public KieContainerImpl(KieProject kProject, KieRepository kr, ReleaseId containerReleaseId) {
        this("impl"+UUID.randomUUID(), kProject, kr, containerReleaseId);
    }

    /**
     * Please note: the recommended way of getting a KieContainer is relying on {@link org.kie.api.KieServices KieServices} API,
     * for example: {@link org.kie.api.KieServices#newKieContainer(ReleaseId) KieServices.newKieContainer(...)}.
     * The direct manual call to KieContainerImpl constructor instead would not guarantee the consistency of the supplied containerId.
     */
    public KieContainerImpl(String containerId, KieProject kProject, KieRepository kr) {
        this.kr = kr;
        this.kProject = kProject;
        this.containerId = containerId;
        kProject.init();
        initMBeans(containerId);
    }
    
    /**
     * Please note: the recommended way of getting a KieContainer is relying on {@link org.kie.api.KieServices KieServices} API,
     * for example: {@link org.kie.api.KieServices#newKieContainer(ReleaseId) KieServices.newKieContainer(...)}.
     * The direct manual call to KieContainerImpl constructor instead would not guarantee the consistency of the supplied containerId.
     */
    public KieContainerImpl(String containerId, KieProject kProject, KieRepository kr, ReleaseId containerReleaseId) {
        this(containerId, kProject, kr);
        this.configuredReleaseId = containerReleaseId;
        this.containerReleaseId = containerReleaseId;
    }

	private void initMBeans(String containerId) {
		if ( isMBeanOptionEnabled() ) {
	        KieContainerMonitor monitor = new KieContainerMonitor(this);
	        ObjectName on = DroolsManagementAgent.createObjectNameBy(containerId);
	        DroolsManagementAgent.getInstance().registerMBean( this, monitor, on );
        }
	}
    
    @Override
    public String getContainerId() {
    	return this.containerId;
    }
    
    @Override
    public ReleaseId getConfiguredReleaseId() {
		return configuredReleaseId;
	}
    
	@Override
	public ReleaseId getResolvedReleaseId() {
		return getReleaseId();
	}

	public ReleaseId getReleaseId() {
        return kProject.getGAV();
    }

    public InputStream getPomAsStream() {
        return kProject.getPomAsStream();
    }

    public long getCreationTimestamp() {
        return kProject.getCreationTimestamp();
    }

    @Override
    public ReleaseId getContainerReleaseId() {
        return containerReleaseId != null ? containerReleaseId : getReleaseId();
    }

    public Results updateToVersion(ReleaseId newReleaseId) {
        checkNotClasspathKieProject();
        Results results = update(((KieModuleKieProject) kProject).getInternalKieModule(), newReleaseId);
        if (results != null) {
            containerReleaseId = newReleaseId;
        } else {
            results = new ResultsImpl();
            ( (ResultsImpl) results ).addMessage( Message.Level.ERROR, null, "Cannot find KieModule with ReleaseId: " + newReleaseId );
        }
        return results;
    }

    public Results updateDependencyToVersion(ReleaseId currentReleaseId, ReleaseId newReleaseId) {
        ReleaseId installedReleaseId = getReleaseId();
        if (currentReleaseId.getGroupId().equals(installedReleaseId.getGroupId()) &&
            currentReleaseId.getArtifactId().equals(installedReleaseId.getArtifactId())) {
            // upgrading the kProject itself: taking the kmodule from there
            return updateToVersion(newReleaseId);
        }

        checkNotClasspathKieProject();
        // upgrading a transitive dependency: taking the kmodule from the krepo
        // if the new and the current release are equal (a snapshot) check if there is an older version with the same releaseId
        InternalKieModule currentKM = currentReleaseId.equals(newReleaseId) ?
                                      (InternalKieModule) ((KieRepositoryImpl) kr).getOldKieModule(currentReleaseId) :
                                      (InternalKieModule) kr.getKieModule(currentReleaseId);
        return update(currentKM, newReleaseId);
    }

    private void checkNotClasspathKieProject() {
        if( kProject instanceof ClasspathKieProject) {
            throw new UnsupportedOperationException( "It is not possible to update a classpath container to a new version." );
        }
    }

    private Results update(final InternalKieModule currentKM, final ReleaseId newReleaseId) {
        final InternalKieModule newKM = (InternalKieModule) kr.getKieModule( newReleaseId );
        if (newKM == null) {
            return null;
        }
        ChangeSetBuilder csb = new ChangeSetBuilder();
        final KieJarChangeSet cs = csb.build( currentKM, newKM );

        List<String> modifiedClassNames = getModifiedClasses(cs);
        final boolean modifyingUsedClass = isModifyingUsedClass( modifiedClassNames, getClassLoader() );
        final List<Class<?>> modifiedClasses = reinitModifiedClasses( newKM, modifiedClassNames, getClassLoader() );
        final List<String> unchangedResources = getUnchangedResources( newKM, cs );

        Map<String, KieBaseModel> currentKieBaseModels = ((KieModuleKieProject) kProject).updateToModule( newKM );

        final ResultsImpl results = new ResultsImpl();

        List<String> kbasesToRemove = new ArrayList<String>();
        for ( Entry<String, KieBase> kBaseEntry : kBases.entrySet() ) {
            String kbaseName = kBaseEntry.getKey();
            final KieBaseModel newKieBaseModel = kProject.getKieBaseModel( kbaseName );
            final KieBaseModel currentKieBaseModel = currentKieBaseModels.get( kbaseName );
            // if a kbase no longer exists, just remove it from the cache
            if ( newKieBaseModel == null ) {
                // have to save for later removal to avoid iteration errors
                kbasesToRemove.add( kbaseName );
            } else {
                final InternalKnowledgeBase kBase = (InternalKnowledgeBase) kBaseEntry.getValue();
                kBase.enqueueModification( () -> updateKBase( kBase, currentKM, newReleaseId, newKM, cs, modifiedClasses, modifyingUsedClass,
                                                              unchangedResources, results, newKieBaseModel, currentKieBaseModel ) );
            }
        }

        for (String kbaseToRemove : kbasesToRemove) {
            kBases.remove(kbaseToRemove);
        }

        // remove sessions that no longer exist
        this.kSessions.entrySet().removeIf( ksession -> kProject.getKieSessionModel( ksession.getKey() ) == null );
        this.statelessKSessions.entrySet().removeIf( ksession -> kProject.getKieSessionModel( ksession.getKey() ) == null );

        return results;
    }

    private void updateKBase( InternalKnowledgeBase kBase, InternalKieModule currentKM, ReleaseId newReleaseId,
                              InternalKieModule newKM, KieJarChangeSet cs, List<Class<?>> modifiedClasses, boolean modifyingUsedClass,
                              List<String> unchangedResources, ResultsImpl results, KieBaseModel newKieBaseModel, KieBaseModel currentKieBaseModel ) {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder( kBase, newKM.getBuilderConfiguration( newKieBaseModel ) );
        KnowledgeBuilderImpl pkgbuilder = (KnowledgeBuilderImpl)kbuilder;
        CompositeKnowledgeBuilder ckbuilder = kbuilder.batch();

        boolean shouldRebuild = applyResourceChanges(currentKM, newKM, cs, modifiedClasses,
                                                     kBase, newKieBaseModel, pkgbuilder, ckbuilder, modifyingUsedClass);
        // remove resources first
        for ( ResourceChangeSet rcs : cs.getChanges().values() ) {
            if ( rcs.getChangeType() == ChangeType.REMOVED ) {
                String resourceName = rcs.getResourceName();
                if ( !resourceName.endsWith( ".properties" ) && isFileInKBase(currentKM, currentKieBaseModel, resourceName) ) {
                    pkgbuilder.removeObjectsGeneratedFromResource( currentKM.getResource( resourceName ) );
                }
            }
        }

        // remove all ObjectTypeNodes for the modified classes
        if (modifyingUsedClass) {
            for (Class<?> cls : modifiedClasses ) {
                clearInstancesOfModifiedClass( kBase, cls );
            }
        }

        if ( shouldRebuild ) {
            // readd unchanged dsl files to the kbuilder
            for (String dslFile : unchangedResources) {
                if (isFileInKBase(newKM, newKieBaseModel, dslFile)) {
                    newKM.addResourceToCompiler(ckbuilder, newKieBaseModel, dslFile);
                }
            }
            rebuildAll(newReleaseId, results, modifyingUsedClass, newKieBaseModel, pkgbuilder, ckbuilder);
        }
        
        kBase.setResolvedReleaseId(newReleaseId);

        for ( InternalWorkingMemory wm : kBase.getWorkingMemories() ) {
            wm.notifyWaitOnRest();
        }
    }

    private void clearInstancesOfModifiedClass( InternalKnowledgeBase kBase, Class<?> cls ) {
        // remove all ObjectTypeNodes for the modified classes
        ClassObjectType objectType = new ClassObjectType( cls );
        for ( EntryPointNode epn : kBase.getRete().getEntryPointNodes().values() ) {
            epn.removeObjectType( objectType );
        }

        // remove all instance of the old class from the object stores
        for (InternalWorkingMemory wm : kBase.getWorkingMemories()) {
            for (EntryPoint ep : wm.getEntryPoints()) {
                InternalWorkingMemoryEntryPoint wmEp = (InternalWorkingMemoryEntryPoint) wm.getWorkingMemoryEntryPoint( ep.getEntryPointId() );
                ClassAwareObjectStore store = ( (ClassAwareObjectStore) wmEp.getObjectStore() );
                if ( store.clearClassStore( cls ) ) {
                    log.warn( "Class " + cls.getName() + " has been modified and therfore its old instances will no longer match" );
                }
            }
        }
    }

    private List<String> getUnchangedResources( InternalKieModule newKM, KieJarChangeSet cs ) {
        List<String> dslFiles = new ArrayList<String>();
        for (String file : newKM.getFileNames()) {
            if ( includeIfUnchanged( file ) && !cs.contains( file ) ) {
                dslFiles.add(file);
            }
        }
        return dslFiles;
    }

    private static final ResourceType[] TYPES_TO_BE_INCLUDED = new ResourceType[] { ResourceType.DSL, ResourceType.GDRL };

    private boolean includeIfUnchanged( String file ) {
        for (ResourceType type : TYPES_TO_BE_INCLUDED ) {
            if (type.matchesExtension( file )) {
                return true;
            }
        }
        return false;
    }

    private boolean applyResourceChanges(InternalKieModule currentKM, InternalKieModule newKM, KieJarChangeSet cs,
                                         List<Class<?>> modifiedClasses, KieBase kBase, KieBaseModel kieBaseModel,
                                         KnowledgeBuilderImpl pkgbuilder, CompositeKnowledgeBuilder ckbuilder, boolean modifyingUsedClass) {
        boolean shouldRebuild = modifyingUsedClass;
        if (modifyingUsedClass) {
            // invalidate accessors for old class
            for (Class<?> cls : modifiedClasses) {
                InternalKnowledgePackage kpackage = ( (InternalKnowledgePackage) kBase.getKiePackage( cls.getPackage().getName() ) );
                if (kpackage != null) {
                    kpackage.getClassFieldAccessorStore().removeClass( cls );
                }
            }

            // there are modified classes used by this kbase, so it has to be completely updated
            updateAllResources(currentKM, newKM, kieBaseModel, pkgbuilder, ckbuilder);
        } else {
            // there are no modified classes used by this kbase, so update it incrementally
            shouldRebuild = updateResourcesIncrementally(currentKM, newKM, cs, modifiedClasses, kBase,
                                                         kieBaseModel, pkgbuilder, ckbuilder) > 0;
        }
        return shouldRebuild;
    }

    private boolean isModifyingUsedClass( List<String> modifiedClasses, ClassLoader classLoader ) {
        for (String modifiedClass : modifiedClasses) {
            if ( isClassInUse( classLoader, convertResourceToClassName(modifiedClass) ) ) {
                return true;
            }
        }
        return false;
    }

    private boolean isClassInUse(ClassLoader rootClassLoader, String className) {
        return !(rootClassLoader instanceof ProjectClassLoader) || ((ProjectClassLoader) rootClassLoader).isClassInUse(className);
    }

    private boolean isFileInKBase(InternalKieModule kieModule, KieBaseModel kieBase, String fileName) {
        if (filterFileInKBase(kieModule, kieBase, fileName)) {
            return true;
        }
        for (String include : kProject.getTransitiveIncludes(kieBase)) {
            InternalKieModule includeModule = kProject.getKieModuleForKBase(include);
            if (includeModule != null && filterFileInKBase(includeModule, kProject.getKieBaseModel(include), fileName)) {
                return true;
            }
        }
        return false;
    }

    private void updateAllResources(InternalKieModule currentKM, InternalKieModule newKM, KieBaseModel kieBaseModel, KnowledgeBuilderImpl kbuilder, CompositeKnowledgeBuilder ckbuilder) {
        for (String resourceName : currentKM.getFileNames()) {
            if ( !resourceName.endsWith( ".properties" ) && isFileInKBase(currentKM, kieBaseModel, resourceName) ) {
                Resource resource = currentKM.getResource(resourceName);
                kbuilder.removeObjectsGeneratedFromResource(resource);
            }
        }
        for (String resourceName : newKM.getFileNames()) {
            if ( !resourceName.endsWith( ".properties" ) && isFileInKBase(newKM, kieBaseModel, resourceName) ) {
                newKM.addResourceToCompiler(ckbuilder, kieBaseModel, resourceName);
            }
        }
    }

    private int updateResourcesIncrementally(InternalKieModule currentKM,
                                             InternalKieModule newKM,
                                             KieJarChangeSet cs,
                                             List<Class<?>> modifiedClasses,
                                             KieBase kBase,
                                             KieBaseModel kieBaseModel,
                                             KnowledgeBuilderImpl kbuilder,
                                             CompositeKnowledgeBuilder ckbuilder) {
        int fileCount = modifiedClasses.size();
        for ( ResourceChangeSet rcs : cs.getChanges().values() ) {
            if ( rcs.getChangeType() != ChangeType.REMOVED ) {
                String resourceName = rcs.getResourceName();
                if ( !resourceName.endsWith( ".properties" ) && isFileInKBase(newKM, kieBaseModel, resourceName) ) {
                    List<ResourceChange> changes = rcs.getChanges();
                    if ( ! changes.isEmpty() ) {
                        // we need to deal with individual parts of the resource
                        fileCount += AbstractKieModule.updateResource(ckbuilder,
                                                                      newKM,
                                                                      resourceName,
                                                                      rcs) ? 1 : 0;
                    } else {
                        // the whole resource has to handled
                        if( rcs.getChangeType() == ChangeType.UPDATED ) {
                            Resource resource = currentKM.getResource(resourceName);
                            kbuilder.removeObjectsGeneratedFromResource(resource);
                        }
                        fileCount += newKM.addResourceToCompiler(ckbuilder, kieBaseModel, resourceName, rcs) ? 1 : 0;
                    }
                }
            }

            for ( ResourceChangeSet.RuleLoadOrder loadOrder : rcs.getLoadOrder() ) {
            	KnowledgePackageImpl pkg = (KnowledgePackageImpl)kBase.getKiePackage( loadOrder.getPkgName() );
            	if( pkg != null ) {
	                RuleImpl rule = pkg.getRule( loadOrder.getRuleName() );
	                if ( rule != null ) {
	                    // rule can be null, if it didn't exist before
	                    rule.setLoadOrder( loadOrder.getLoadOrder() );
	                }
            	}
            }
        }
        return fileCount;
    }

    private void rebuildAll(ReleaseId newReleaseId,
                            ResultsImpl results,
                            boolean modifyingUsedClass,
                            KieBaseModel kieBaseModel,
                            KnowledgeBuilderImpl kbuilder,
                            CompositeKnowledgeBuilder ckbuilder) {
        ckbuilder.build();

        PackageBuilderErrors errors = kbuilder.getErrors();
        if ( !errors.isEmpty() ) {
            for ( KnowledgeBuilderError error : errors.getErrors() ) {
                results.addMessage(error).setKieBaseName( kieBaseModel.getName() );
            }
            log.error("Unable to update KieBase: " + kieBaseModel.getName() + " to release " + newReleaseId + "\n" + errors.toString());
        }

        if (modifyingUsedClass) {
            kbuilder.rewireAllClassObjectTypes();
        }
    }

    private List<Class<?>> reinitModifiedClasses( InternalKieModule newKM, List<String> modifiedClasses, ClassLoader classLoader ) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!modifiedClasses.isEmpty()) {
            if ( classLoader instanceof ProjectClassLoader ) {
                ProjectClassLoader projectClassLoader = (ProjectClassLoader) classLoader;
                projectClassLoader.reinitTypes();
                for (String resourceName : modifiedClasses) {
                    String className = convertResourceToClassName( resourceName );
                    byte[] bytes = newKM.getBytes(resourceName);
                    Class<?> clazz = projectClassLoader.defineClass(className, resourceName, bytes);
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }

    private List<String> getModifiedClasses(KieJarChangeSet cs) {
        List<String> modifiedClasses = new ArrayList<String>();
        for ( ResourceChangeSet rcs : cs.getChanges().values() ) {
            if ( rcs.getChangeType() != ChangeType.REMOVED ) {
                String resourceName = rcs.getResourceName();
                if ( resourceName.endsWith( ".class" ) ) {
                    modifiedClasses.add(resourceName);
                }
            }
        }
        return modifiedClasses;
    }

    public Collection<String> getKieBaseNames() {
        return kProject.getKieBaseNames();
    }

    public Collection<String> getKieSessionNamesInKieBase(String kBaseName) {
        KieBaseModel kieBaseModel = kProject.getKieBaseModel(kBaseName);
        return kieBaseModel != null ? kieBaseModel.getKieSessionModels().keySet() : Collections.<String>emptySet();
    }

    public KieBase getKieBase() {
        KieBaseModel defaultKieBaseModel = kProject.getDefaultKieBaseModel();
        if (defaultKieBaseModel == null) {
            throw new RuntimeException("Cannot find a default KieBase");
        }
        return getKieBase( defaultKieBaseModel.getName() );
    }
    
    public Results verify() {
        return this.kProject.verify();
    }

    public Results verify(String... kModelNames) {
        return this.kProject.verify( kModelNames );
    }

    public KieBase getKieBase(String kBaseName) {
        KieBase kBase = kBases.get( kBaseName );
        if ( kBase == null ) {
            KieBaseModelImpl kBaseModel = getKieBaseModelImpl(kBaseName);
            synchronized (kBaseModel) {
                kBase = kBases.get( kBaseName );
                if ( kBase == null ) {
                    ResultsImpl msgs = new ResultsImpl();
                    kBase = createKieBase(kBaseModel, kProject, msgs, null);
                    if (kBase == null) {
                        // build error, throw runtime exception
                        throw new RuntimeException("Error while creating KieBase" + msgs.filterMessages(Level.ERROR));
                    }
                    kBases.put(kBaseName, kBase);
                }
            }
        }
        return kBase;
    }

    public KieBase newKieBase(KieBaseConfiguration conf) {
        KieBaseModel defaultKieBaseModel = kProject.getDefaultKieBaseModel();
        if (defaultKieBaseModel == null) {
            throw new RuntimeException("Cannot find a default KieBase");
        }
        return newKieBase(defaultKieBaseModel.getName(), conf);
    }

    public KieBase newKieBase(String kBaseName, KieBaseConfiguration conf) {
        ResultsImpl msgs = new ResultsImpl();
        KieBase kBase = createKieBase(getKieBaseModelImpl(kBaseName), kProject, msgs, conf);
        if ( kBase == null ) {
            // build error, throw runtime exception
            throw new RuntimeException( "Error while creating KieBase" + msgs.filterMessages( Level.ERROR  ) );
        }
        return kBase;
    }

    private KieBase createKieBase(KieBaseModelImpl kBaseModel, KieProject kieProject, ResultsImpl messages, KieBaseConfiguration conf) {
        InternalKieModule kModule = kieProject.getKieModuleForKBase( kBaseModel.getName() );
        InternalKnowledgeBase kBase = kModule.createKieBase(kBaseModel, kieProject, messages, conf);
        if ( kBase == null ) {
            return null;
        }
        kBase.setResolvedReleaseId(containerReleaseId);
        kBase.setContainerId(containerId);
        kBase.setKieContainer(this);
        kBase.initMBeans();
        return kBase;
    }

    private KieBaseModelImpl getKieBaseModelImpl(String kBaseName) {
        KieBaseModelImpl kBaseModel = (KieBaseModelImpl) kProject.getKieBaseModel(kBaseName);
        if (kBaseModel == null) {
            throw new RuntimeException( "The requested KieBase \"" + kBaseName + "\" does not exist" );
        }
        return kBaseModel;
    }

    public KieSession newKieSession() {
        return newKieSession((Environment)null, null);
    }

    public KieSession getKieSession() {
        KieSessionModel defaultKieSessionModel = findKieSessionModel(false);
        return getKieSession(defaultKieSessionModel.getName());
    }

    public KieSession newKieSession(KieSessionConfiguration conf) {
        return newKieSession((Environment)null, conf);
    }

    public KieSession newKieSession(Environment environment) {
        return newKieSession(environment, null);
    }

    public KieSession newKieSession(Environment environment, KieSessionConfiguration conf) {
        KieSessionModel defaultKieSessionModel = findKieSessionModel(false);
        return newKieSession(defaultKieSessionModel.getName(), environment, conf);
    }

    private KieSessionModel findKieSessionModel(boolean stateless) {
        KieSessionModel defaultKieSessionModel = stateless ? kProject.getDefaultStatelessKieSession() : kProject.getDefaultKieSession();
        if (defaultKieSessionModel == null) {
            throw new RuntimeException(stateless ? "Cannot find a default StatelessKieSession" : "Cannot find a default KieSession");
        }
        return defaultKieSessionModel;
    }

    public StatelessKieSession newStatelessKieSession() {
        return newStatelessKieSession((KieSessionConfiguration)null);
    }

    public StatelessKieSession newStatelessKieSession(KieSessionConfiguration conf) {
        KieSessionModel defaultKieSessionModel = findKieSessionModel(true);
        return newStatelessKieSession(defaultKieSessionModel.getName(), conf);
    }

    public StatelessKieSession getStatelessKieSession() {
        KieSessionModel defaultKieSessionModel = findKieSessionModel(true);
        return getStatelessKieSession(defaultKieSessionModel.getName());
    }

    public KieSession newKieSession(String kSessionName) {
        return newKieSession(kSessionName, null, null);
    }

    public KieSession getKieSession(String kSessionName) {
        KieSession kieSession = kSessions.get(kSessionName);
        if (kieSession instanceof StatefulKnowledgeSessionImpl && !((StatefulKnowledgeSessionImpl)kieSession).isAlive()) {
            kSessions.remove(kSessionName);
            kieSession = null;
        }
        return kieSession != null ? kieSession : newKieSession(kSessionName);
    }

    public KieSession newKieSession(String kSessionName, Environment environment) {
        return newKieSession(kSessionName, environment, null);
    }

    public KieSession newKieSession(String kSessionName, KieSessionConfiguration conf) {
        return newKieSession(kSessionName, null, conf);
    }

    public KieSession newKieSession(String kSessionName, Environment environment, KieSessionConfiguration conf) {
        KieSessionModelImpl kSessionModel = kSessionName != null ?
                                            (KieSessionModelImpl) getKieSessionModel(kSessionName) :
                                            (KieSessionModelImpl) findKieSessionModel(false);

        if ( kSessionModel == null ) {
            log.error("Unknown KieSession name: " + kSessionName);
            return null;
        }
        if (kSessionModel.getType() == KieSessionModel.KieSessionType.STATELESS) {
            throw new RuntimeException("Trying to create a stateful KieSession from a stateless KieSessionModel: " + kSessionModel.getName());
        }
        KieBase kBase = getKieBase( kSessionModel.getKieBaseModel().getName() );
        if ( kBase == null ) {
            log.error("Unknown KieBase name: " + kSessionModel.getKieBaseModel().getName());
            return null;
        }

        KieSession kSession = kBase.newKieSession( conf != null ? conf : getKieSessionConfiguration( kSessionModel ), environment );
        if (isJndiAvailable()) {
            wireListnersAndWIHs( kSessionModel, kSession );
        }
        registerLoggers(kSessionModel, kSession);

        ((StatefulKnowledgeSessionImpl) kSession).initMBeans(containerId, ((InternalKnowledgeBase) kBase).getId(), kSessionModel.getName());
        
        kSessions.put(kSessionModel.getName(), kSession);
        return kSession;
    }

    private void registerLoggers(KieSessionModelImpl kSessionModel, KieRuntimeEventManager kSession) {
        KieLoggers kieLoggers = KieServices.Factory.get().getLoggers();
        if (kSessionModel.getConsoleLogger() != null) {
            kieLoggers.newConsoleLogger(kSession);
        }
        FileLoggerModel fileLogger = kSessionModel.getFileLogger();
        if (fileLogger != null) {
            if (fileLogger.isThreaded()) {
                kieLoggers.newThreadedFileLogger(kSession, fileLogger.getFile(), fileLogger.getInterval());
            } else {
                kieLoggers.newFileLogger(kSession, fileLogger.getFile());
            }
        }
    }

    public StatelessKieSession newStatelessKieSession(String kSessionName) {
        return newStatelessKieSession(kSessionName, null);
    }

    public StatelessKieSession newStatelessKieSession(String kSessionName, KieSessionConfiguration conf) {
        KieSessionModelImpl kSessionModel = kSessionName != null ?
                                            (KieSessionModelImpl) getKieSessionModel(kSessionName) :
                                            (KieSessionModelImpl) findKieSessionModel(true);

        if ( kSessionModel == null ) {
            log.error("Unknown KieSession name: " + kSessionName);
            return null;
        }
        if (kSessionModel.getType() == KieSessionModel.KieSessionType.STATEFUL) {
            throw new RuntimeException("Trying to create a stateless KieSession from a stateful KieSessionModel: " + kSessionModel.getName());
        }
        KieBase kBase = getKieBase( kSessionModel.getKieBaseModel().getName() );
        if ( kBase == null ) {
            log.error("Unknown KieBase name: " + kSessionModel.getKieBaseModel().getName());
            return null;
        }

        StatelessKieSession statelessKieSession = kBase.newStatelessKieSession( conf != null ? conf : getKieSessionConfiguration( kSessionModel ) );
        if (isJndiAvailable()) {
            wireListnersAndWIHs( kSessionModel, statelessKieSession );
        }
        registerLoggers(kSessionModel, statelessKieSession);
        
        ((StatelessKnowledgeSessionImpl) statelessKieSession).initMBeans(containerId, ((InternalKnowledgeBase) kBase).getId(), kSessionModel.getName());
        
        statelessKSessions.put(kSessionModel.getName(), statelessKieSession);
        return statelessKieSession;
    }

    public StatelessKieSession getStatelessKieSession(String kSessionName) {
        StatelessKieSession kieSession = statelessKSessions.get(kSessionName);
        return kieSession != null ? kieSession : newStatelessKieSession(kSessionName);

    }

    public KieSessionConfiguration getKieSessionConfiguration() {
        return getKieSessionConfiguration( kProject.getDefaultKieSession() );
    }

    public KieSessionConfiguration getKieSessionConfiguration( String kSessionName ) {
        KieSessionModelImpl kSessionModel = (KieSessionModelImpl) kProject.getKieSessionModel( kSessionName );
        if ( kSessionModel == null ) {
            log.error("Unknown KieSession name: " + kSessionName);
            return null;
        }
        return getKieSessionConfiguration( kSessionModel );
    }

    private KieSessionConfiguration getKieSessionConfiguration( KieSessionModel kSessionModel ) {
        KieSessionConfiguration ksConf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        ksConf.setOption( kSessionModel.getClockType() );
        ksConf.setOption( kSessionModel.getBeliefSystem() );
        return ksConf;
    }

    public void dispose() {
        kBases.values().forEach( kb -> ( (InternalKnowledgeBase) kb ).setKieContainer( null ) );

        Set<DroolsManagementAgent.CBSKey> cbskeys = new HashSet<DroolsManagementAgent.CBSKey>();
        if ( isMBeanOptionEnabled() ) {
            for (Entry<String, KieSession> kv : kSessions.entrySet()) {
                cbskeys.add(new DroolsManagementAgent.CBSKey(containerId, ((InternalKnowledgeBase) kv.getValue().getKieBase()).getId(), kv.getKey()));
            }
            for (Entry<String, StatelessKieSession> kv : statelessKSessions.entrySet()) {
                cbskeys.add(new DroolsManagementAgent.CBSKey(containerId, ((InternalKnowledgeBase) kv.getValue().getKieBase()).getId(), kv.getKey()));
            }
        }
        
        for (KieSession kieSession : kSessions.values()) {
            kieSession.dispose();
        }
        kSessions.clear();
        statelessKSessions.clear();

        if ( isMBeanOptionEnabled() ) {
            for (CBSKey c : cbskeys) {
                DroolsManagementAgent.getInstance().unregisterKnowledgeSessionBean(c);
            }
            for (KieBase kb : kBases.values()) {
                DroolsManagementAgent.getInstance().unregisterKnowledgeBase((InternalKnowledgeBase) kb);
            }
            DroolsManagementAgent.getInstance().unregisterMBeansFromOwner(this);
        }
        
        ((InternalKieServices) KieServices.Factory.get()).clearRefToContainerId(this.containerId, this);
    }

    @Override
    public void disposeSession(KieSession kieSession) {
        if (!isMBeanOptionEnabled()) {
            kSessions.values().remove( kieSession );
        }
    }

    private boolean isMBeanOptionEnabled() {
        return MBeansOption.isEnabled( System.getProperty( MBeansOption.PROPERTY_NAME, MBeansOption.DISABLED.toString() ) );
    }

    public KieProject getKieProject() {
        return kProject;
    }

    public KieModule getKieModuleForKBase(String kBaseName) {
        return kProject.getKieModuleForKBase( kBaseName );
    }

    public KieBaseModel getKieBaseModel(String kBaseName) {
        return kProject.getKieBaseModel(kBaseName);
    }

    public KieSessionModel getKieSessionModel(String kSessionName) {
        return kProject.getKieSessionModel(kSessionName);
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.kProject.getClassLoader();
    }
}
