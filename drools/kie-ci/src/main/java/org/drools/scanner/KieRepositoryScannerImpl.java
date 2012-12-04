package org.drools.scanner;

import org.apache.maven.project.MavenProject;
import org.drools.kproject.models.KieModuleModelImpl;
import org.drools.scanner.embedder.EmbeddedPomParser;
import org.kie.builder.GAV;
import org.kie.builder.KieContainer;
import org.kie.builder.KieModule;
import org.kie.builder.KieScanner;
import org.kie.builder.Message;
import org.kie.builder.impl.CompositeKieModule;
import org.kie.builder.impl.InternalKieScanner;
import org.kie.builder.impl.JarKieModule;
import org.kie.builder.impl.MessageImpl;
import org.kie.builder.impl.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.aether.artifact.Artifact;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.drools.scanner.embedder.MavenProjectLoader.parseMavenPom;

public class KieRepositoryScannerImpl implements InternalKieScanner {

    private Timer timer;

    private static final Logger log = LoggerFactory.getLogger(KieScanner.class);

    private KieContainer kieContainer;

    private PomParser pomParser;

    private final Set<DependencyDescriptor> usedDependencies = new HashSet<DependencyDescriptor>();

    private MavenRepository mavenRepository;

    public void setKieContainer(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
        DependencyDescriptor projectDescr = new DependencyDescriptor(kieContainer.getGAV());
        if (!projectDescr.isFixedVersion()) {
            usedDependencies.add(projectDescr);
        }

        MavenProject mavenProject = getMavenProjectForGAV(kieContainer.getGAV());
        if (mavenProject != null) {
            mavenRepository = MavenRepository.getMavenRepository(mavenProject);
            pomParser = new EmbeddedPomParser(mavenProject);
        } else {
            mavenRepository = MavenRepository.getMavenRepository();
            pomParser = new EmbeddedPomParser();
        }
        init();
    }

    private void init() {
        Collection<Artifact> artifacts = findKJarAtifacts();
        log.info("Artifacts containing a kjar: " + artifacts);
        if (artifacts.isEmpty()) {
            log.info("There's no artifacts containing a kjar: shutdown the scanner");
            return;
        }
        indexAtifacts(artifacts);
    }

    public KieModule loadArtifact(GAV gav) {
        String artifactName = gav.toString();
        Artifact artifact = MavenRepository.getMavenRepository().resolveArtifact(artifactName);
        return artifact != null ? buildArtifact(gav, artifact) : loadPomArtifact(gav);
    }

    private KieModule loadPomArtifact(GAV gav) {
        MavenProject mavenProject = getMavenProjectForGAV(gav);
        if (mavenProject == null) {
            return null;
        }

        mavenRepository = MavenRepository.getMavenRepository(mavenProject);
        PomParser pomParser = new EmbeddedPomParser(mavenProject);

        CompositeKieModule compositeKieModule = new CompositeKieModule(gav);
        for (DependencyDescriptor dep : pomParser.getPomDirectDependencies()) {
            Artifact depArtifact = mavenRepository.resolveArtifact(dep.toString());
            if (isKJar(depArtifact.getFile())) {
                compositeKieModule.addKieModule(buildArtifact(gav, depArtifact));
            }
        }
        return compositeKieModule;
    }

    private MavenProject getMavenProjectForGAV(GAV gav) {
        String artifactName = gav.getGroupId() + ":" + gav.getArtifactId() + ":pom:" + gav.getVersion();
        Artifact artifact = MavenRepository.getMavenRepository().resolveArtifact(artifactName);
        return artifact != null ? parseMavenPom(artifact.getFile()) : null;
    }

    private KieModule buildArtifact(GAV gav, Artifact artifact) {
        JarKieModule kieModule = new JarKieModule(gav, artifact.getFile());
        kieModule.build();
        return kieModule;
    }

    public void start(long pollingInterval) {
        if (pollingInterval <= 0) {
            throw new IllegalArgumentException("pollingInterval must be positive");
        }
        if (timer != null) {
            throw new IllegalStateException("The scanner is already running");
        }
        startScanTask(pollingInterval);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void startScanTask(long pollingInterval) {
        timer = new Timer(true);
        timer.schedule(new ScanTask(), pollingInterval, pollingInterval);
    }

    private class ScanTask extends TimerTask {
        public void run() {
            scanNow();
        }
    }

    public void scanNow() {
        if (usedDependencies == null) {
            return;
        }
        Collection<Artifact> updatedArtifacts = scanForUpdates(usedDependencies);
        if (updatedArtifacts.isEmpty()) {
            return;
        }
        for (Artifact artifact : updatedArtifacts) {
            DependencyDescriptor depDescr = new DependencyDescriptor(artifact);
            usedDependencies.remove(depDescr);
            usedDependencies.add(depDescr);
            updateKieModule(artifact, depDescr.getGav());
        }
        log.info("The following artifacts have been updated: " + updatedArtifacts);
    }

    private void updateKieModule(Artifact artifact, GAV gav) {
        JarKieModule kieModule = new JarKieModule(gav, artifact.getFile());
        Messages messages = kieModule.build();
        if ( MessageImpl.filterMessages(messages.getMessages(), Message.Level.ERROR).isEmpty()) {
            kieContainer.updateToVersion(gav);
        }
    }

    private Collection<Artifact> scanForUpdates(Collection<DependencyDescriptor> dependencies) {
        List<Artifact> newArtifacts = new ArrayList<Artifact>();
        for (DependencyDescriptor dependency : dependencies) {
            Artifact newArtifact = mavenRepository.resolveArtifact(dependency.toResolvableString());
            DependencyDescriptor resolvedDep = new DependencyDescriptor(newArtifact);
            if (resolvedDep.isNewerThan(dependency)) {
                newArtifacts.add(newArtifact);
            }
        }
        return newArtifacts;
    }

    private void indexAtifacts(Collection<Artifact> artifacts) {
        for (Artifact artifact : artifacts) {
            usedDependencies.add(new DependencyDescriptor(artifact));
        }
    }

    private Collection<Artifact> findKJarAtifacts() {
        Collection<DependencyDescriptor> deps = getAllDependecies();
        deps = filterNonFixedDependecies(deps);
        Collection<Artifact> artifacts = resolveArtifacts(deps);
        return filterKJarArtifacts(artifacts);
    }

    private Collection<DependencyDescriptor> getAllDependecies() {
        Set<DependencyDescriptor> dependencies = new HashSet<DependencyDescriptor>();
        for (DependencyDescriptor dep : pomParser.getPomDirectDependencies()) {
            dependencies.add(dep);
            dependencies.addAll(mavenRepository.getArtifactDependecies(dep.toString()));
        }
        return dependencies;
    }

    private Collection<DependencyDescriptor> filterNonFixedDependecies(Collection<DependencyDescriptor> dependencies) {
        List<DependencyDescriptor> nonFixedDeps = new ArrayList<DependencyDescriptor>();
        for (DependencyDescriptor dep : dependencies) {
            if (!dep.isFixedVersion()) {
                nonFixedDeps.add(dep);
            }
        }
        return nonFixedDeps;
    }

    private List<Artifact> resolveArtifacts(Collection<DependencyDescriptor> dependencies) {
        List<Artifact> artifacts = new ArrayList<Artifact>();
        for (DependencyDescriptor dep : dependencies) {
            Artifact artifact = mavenRepository.resolveArtifact(dep.toString());
            artifacts.add(artifact);
            log.debug( artifact + " resolved to  " + artifact.getFile() );
        }
        return artifacts;
    }

    private Collection<Artifact> filterKJarArtifacts(Collection<Artifact> artifacts) {
        List<Artifact> kJarArtifacts = new ArrayList<Artifact>();
        for (Artifact artifact : artifacts) {
            if (isKJar(artifact.getFile())) {
                kJarArtifacts.add(artifact);
            }
        }
        return kJarArtifacts;
    }

    private boolean isKJar(File jar) {
        ZipFile zipFile;
        try {
            zipFile = new ZipFile( jar );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ZipEntry zipEntry = zipFile.getEntry( KieModuleModelImpl.KMODULE_JAR_PATH );
        return zipEntry != null;
    }
}
