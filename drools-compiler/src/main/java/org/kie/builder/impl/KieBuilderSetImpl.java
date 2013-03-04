package org.kie.builder.impl;

import org.drools.builder.impl.KnowledgeBuilderImpl;
import org.drools.compiler.PackageBuilder;
import org.drools.io.impl.BaseResource;
import org.kie.KieServices;
import org.kie.builder.CompositeKnowledgeBuilder;
import org.kie.builder.IncrementalResults;
import org.kie.builder.model.KieBaseModel;
import org.kie.builder.KieBuilderSet;
import org.kie.builder.KnowledgeBuilder;
import org.kie.builder.KnowledgeBuilderError;
import org.kie.io.Resource;
import org.kie.io.ResourceConfiguration;
import org.kie.io.ResourceType;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.kie.builder.impl.AbstractKieModule.getResourceConfiguration;
import static org.kie.builder.impl.KieBuilderImpl.filterFileInKBase;

public class KieBuilderSetImpl implements KieBuilderSet {

    private final KieBuilderImpl kieBuilder;
    private String[] files;

    private List<KnowledgeBuilderError> previousErrors;

    private final Map<String, Set<String>> resourcesWithErrors = new HashMap<String, Set<String>>();

    public KieBuilderSetImpl(KieBuilderImpl kieBuilder) {
        this.kieBuilder = kieBuilder;
        registerInitialErrors(kieBuilder);
    }

    private void registerInitialErrors(KieBuilderImpl kieBuilder) {
        previousErrors = new ArrayList<KnowledgeBuilderError>();
        InternalKieModule kieModule = (InternalKieModule) kieBuilder.getKieModuleIgnoringErrors();
        for (KieBaseModel kBaseModel : kieModule.getKieModuleModel().getKieBaseModels().values()) {
            KnowledgeBuilder kBuilder = kieModule.getKnowledgeBuilderForKieBase( kBaseModel.getName() );
            for ( KnowledgeBuilderError error : kBuilder.getErrors() ) {
                previousErrors.add(error);
            }
            resourcesWithErrors.put(kBaseModel.getName(), findResourcesWithErrors(kBuilder));
        }
    }

    KieBuilderSetImpl setFiles(String[] files) {
        this.files = files;
        return this;
    }

    @Override
    public IncrementalResults build() {
        if ( files == null ) {
            return new IncrementalResultsImpl();
        }
        for (String file : files) {
            kieBuilder.copySourceToTarget(file);
        }
        return buildChanges();
    }

    private Set<String> findResourcesWithErrors(KnowledgeBuilder kBuilder) {
        if ( kBuilder.hasErrors() ) {
            Set<String> resourcesWithErrors = new HashSet<String>();
            for ( KnowledgeBuilderError error : kBuilder.getErrors() ) {
                resourcesWithErrors.add(error.getResource().getSourcePath());
            }
            return resourcesWithErrors;
        }
        return Collections.emptySet();
    }

    private IncrementalResults buildChanges() {
        List<KnowledgeBuilderError> currentErrors = new ArrayList<KnowledgeBuilderError>();

        InternalKieModule kieModule = (InternalKieModule) kieBuilder.getKieModuleIgnoringErrors();
        for (KieBaseModel kBaseModel : kieModule.getKieModuleModel().getKieBaseModels().values()) {
            KnowledgeBuilder kBuilder = kieModule.getKnowledgeBuilderForKieBase( kBaseModel.getName() );
            CompositeKnowledgeBuilder ckbuilder = kBuilder.batch();

            PackageBuilder pkgBuilder = ((KnowledgeBuilderImpl)kBuilder).getPackageBuilder();
            Set<String> wrongResources = resourcesWithErrors.get(kBaseModel.getName());
            for ( String resourceName : wrongResources ) {
                pkgBuilder.removeObjectsGeneratedFromResource(new DummyResource(resourceName));
                addResource(ckbuilder, kBaseModel, kieModule, resourceName);
            }

            boolean modified = false;
            for (String file : files) {
                String resourceName = file.startsWith(KieBuilderImpl.RESOURCES_ROOT) ?
                        file.substring(KieBuilderImpl.RESOURCES_ROOT.length()) :
                        file;

                if ( wrongResources.contains(resourceName) ) {
                    modified = true;
                } else {
                    // remove the objects generated by the old Resource
                    modified = pkgBuilder.removeObjectsGeneratedFromResource(new DummyResource(resourceName)) || modified;
                    // add the modified Resource
                    modified = addResource(ckbuilder, kBaseModel, kieModule, resourceName) || modified;
                }
            }

            if (modified) {
                ckbuilder.build();
                resourcesWithErrors.put(kBaseModel.getName(), findResourcesWithErrors(kBuilder));
                if ( kBuilder.hasErrors() ) {
                    currentErrors.addAll(kBuilder.getErrors());
                    kBuilder.undo();
                } else {
                    KieServices.Factory.get().getRepository().addKieModule( kieModule );
                }
            }
        }
        files = null;

        IncrementalResultsImpl results = getIncrementalResults(currentErrors);
        previousErrors = currentErrors;
        return results;
    }

    private IncrementalResultsImpl getIncrementalResults(List<KnowledgeBuilderError> currentErrors) {
        IncrementalResultsImpl results = new IncrementalResultsImpl();
        for (KnowledgeBuilderError error : currentErrors) {
            if (!previousErrors.remove(error)) {
                results.addMessage(error);
            }
        }
        for (KnowledgeBuilderError error : previousErrors) {
            results.removeMessage(error);
        }
        return results;
    }

    private boolean addResource( CompositeKnowledgeBuilder ckbuilder,
                                 KieBaseModel kieBaseModel,
                                 InternalKieModule kieModule,
                                 String resourceName ) {
        byte[] bytes = kieModule.getBytes(resourceName);
        if (bytes == null || bytes.length == 0) {
            return false;
        }
        Resource resource = KieServices.Factory.get().getResources().newByteArrayResource(bytes).setSourcePath(resourceName);

        if ( filterFileInKBase(kieBaseModel, resourceName) ) {
            ResourceConfiguration conf = getResourceConfiguration(kieModule, resourceName);
            if ( conf == null ) {
                ckbuilder.add( resource, ResourceType.determineResourceType(resourceName) );
            } else {
                ckbuilder.add( resource, ResourceType.determineResourceType(resourceName), conf );
            }
            return true;
        }
        return false;
    }

    public static class DummyResource extends BaseResource {
        public DummyResource(String resourceName) {
            setSourcePath(resourceName);
        }

        @Override
        public URL getURL() throws IOException {
            throw new UnsupportedOperationException("org.kie.builder.impl.KieBuilderSetImpl.DummyResource.getURL -> TODO");
        }

        @Override
        public boolean hasURL() {
            throw new UnsupportedOperationException("org.kie.builder.impl.KieBuilderSetImpl.DummyResource.hasURL -> TODO");
        }

        @Override
        public boolean isDirectory() {
            throw new UnsupportedOperationException("org.kie.builder.impl.KieBuilderSetImpl.DummyResource.isDirectory -> TODO");
        }

        @Override
        public Collection<Resource> listResources() {
            throw new UnsupportedOperationException("org.kie.builder.impl.KieBuilderSetImpl.DummyResource.listResources -> TODO");
        }

        @Override
        public long getLastModified() {
            throw new UnsupportedOperationException("org.kie.builder.impl.KieBuilderSetImpl.DummyResource.getLastModified -> TODO");
        }

        @Override
        public long getLastRead() {
            throw new UnsupportedOperationException("org.kie.builder.impl.KieBuilderSetImpl.DummyResource.getLastRead -> TODO");
        }

        @Override
        public InputStream getInputStream() throws IOException {
            throw new UnsupportedOperationException("org.kie.builder.impl.KieBuilderSetImpl.DummyResource.getInputStream -> TODO");
        }

        @Override
        public Reader getReader() throws IOException {
            throw new UnsupportedOperationException("org.kie.builder.impl.KieBuilderSetImpl.DummyResource.getReader -> TODO");
        }
    }
}