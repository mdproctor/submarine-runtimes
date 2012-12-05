package org.drools.kproject.models;

import static org.drools.core.util.IoUtils.recursiveListFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.drools.core.util.AbstractXStreamConverter;
import org.drools.core.util.Predicate;
import org.kie.builder.KieBaseModel;
import org.kie.builder.KieModuleModel;
import org.kie.builder.KieSessionModel;
import org.kie.conf.AssertBehaviorOption;
import org.kie.conf.EventProcessingOption;
import org.kie.io.ResourceType;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class KieBaseModelImpl
        implements
        KieBaseModel {

    public static final String DEFAULT_KIEBASE_NAME = "!DeFaUlT!";

    private String                       name;

    private Set<String>                  includes= new HashSet<String>();;

    private Set<String>                  packages;

    private AssertBehaviorOption         equalsBehavior = AssertBehaviorOption.IDENTITY;

    private EventProcessingOption        eventProcessingMode = EventProcessingOption.STREAM;

    private Map<String, KieSessionModel> kSessions = new HashMap<String, KieSessionModel>();

    private KieModuleModel                   kModule;

    private KieBaseModelImpl() {
    }

    public KieBaseModelImpl(KieModuleModel kModule,
                            String name) {
        this.kModule = kModule;
        this.includes = new HashSet<String>();
        this.name = name;
        this.kSessions = Collections.emptyMap();
    }

    public boolean isDefault() {
        return name.equals(DEFAULT_KIEBASE_NAME);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getPackages() {
        return (Set<String>) (packages != null ? packages : Collections.emptySet());
    }

    public KieBaseModel addPackage(String pkg) {
        if ( packages == null ) {
            packages = new HashSet<String>();
        }
        packages.add( pkg );
        return this;
    }

    public KieBaseModel removePackage(String pkg) {
        if ( packages != null ) {
            packages.remove( pkg );
        }
        return this;
    }

    public KieModuleModel getKModule() {
        return kModule;
    }

    public void setKModule(KieModuleModel kieProject) {
        this.kModule = kieProject;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieBaseModel#getKieSessionModels()
     */
    public Map<String, KieSessionModel> getKieSessionModels() {
        return Collections.unmodifiableMap( kSessions );
    }

    public Map<String, KieSessionModel> getRawKieSessionModels() {
        return kSessions;
    }    
    
    /* (non-Javadoc)
     * @see org.kie.kproject.KieBaseModel#setKSessions(java.util.Map)
     */
    private void setKSessions(Map<String, KieSessionModel> kSessions) {
        this.kSessions = kSessions;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieBaseModel#addKSession(org.kie.kproject.KieSessionModelImpl)
     */
    public KieSessionModel newKieSessionModel(String name) {
        KieSessionModel kieSessionModel = new KieSessionModelImpl( this, name );
        Map<String, KieSessionModel> newMap = new HashMap<String, KieSessionModel>();
        newMap.putAll( this.kSessions );
        newMap.put( kieSessionModel.getName(), kieSessionModel );
        setKSessions( newMap );

        return kieSessionModel;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieBaseModel#removeKieSessionModel(org.kie.kproject.KieSessionModelImpl)
     */
    public KieBaseModel removeKieSessionModel(String qName) {
        Map<String, KieSessionModel> newMap = new HashMap<String, KieSessionModel>();
        newMap.putAll( this.kSessions );
        newMap.remove( qName );
        setKSessions( newMap );
        return this;
    }

    public void moveKSession(String oldQName,
                             String newQName) {
        Map<String, KieSessionModel> newMap = new HashMap<String, KieSessionModel>();
        newMap.putAll( this.kSessions );
        KieSessionModel kieSessionModel = newMap.remove( oldQName );
        newMap.put( newQName, kieSessionModel );
        setKSessions( newMap );
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieBaseModel#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieBaseModel#setName(java.lang.String)
     */
    public KieBaseModel setName(String name) {
        this.name = name;
        return this;
    }

    public Set<String> getIncludes() {
        return Collections.unmodifiableSet( includes );
    }

    public KieBaseModel addInclude(String kBaseQName) {
        this.includes.add( kBaseQName );
        return this;
    }

    public KieBaseModel removeInclude(String kBaseQName) {
        this.includes.remove( kBaseQName );
        return this;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieBaseModel#getEqualsBehavior()
     */
    public AssertBehaviorOption getEqualsBehavior() {
        return equalsBehavior;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieBaseModel#setEqualsBehavior(org.kie.conf.AssertBehaviorOption)
     */
    public KieBaseModel setEqualsBehavior(AssertBehaviorOption equalsBehaviour) {
        this.equalsBehavior = equalsBehaviour;
        return this;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieBaseModel#getEventProcessingMode()
     */
    public EventProcessingOption getEventProcessingMode() {
        return eventProcessingMode;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieBaseModel#setEventProcessingMode(org.kie.conf.EventProcessingOption)
     */
    public KieBaseModel setEventProcessingMode(EventProcessingOption eventProcessingMode) {
        this.eventProcessingMode = eventProcessingMode;
        return this;
    }

    public static List<String> getFiles(String kBaseName,
                                        ZipFile zipFile) {
        List<String> files = new ArrayList<String>();
        Enumeration< ? extends ZipEntry> zipEntries = zipFile.entries();
        while ( zipEntries.hasMoreElements() ) {
            ZipEntry zipEntry = zipEntries.nextElement();
            String fileName = zipEntry.getName();
            if ( filterFileInKBase( kBaseName.replace( '.', '/' ), fileName ) ) {
                files.add( fileName );
            }
        }
        return files;
    }
    
    public static List<String> getFiles(java.io.File rootFolder) {
        return recursiveListFile( rootFolder, "", new Predicate<java.io.File>() {
            public boolean apply(java.io.File file) {
                String fileName = file.getName();
                return fileName.endsWith( ResourceType.DRL.getDefaultExtension() ) ||
                       fileName.endsWith( ResourceType.BPMN2.getDefaultExtension() );
            }
        } );
    }

    private static boolean filterFileInKBase(String rootPath,
                                             String fileName) {
        return fileName.startsWith( rootPath ) && (fileName.endsWith( ResourceType.DRL.getDefaultExtension() ) || fileName.endsWith( ResourceType.BPMN2.getDefaultExtension() ));
    }

    public static class KBaseConverter extends AbstractXStreamConverter {

        public KBaseConverter() {
            super( KieBaseModelImpl.class );
        }

        public void marshal(Object value,
                            HierarchicalStreamWriter writer,
                            MarshallingContext context) {
            KieBaseModelImpl kBase = (KieBaseModelImpl) value;
            writer.addAttribute( "name", kBase.getName() );
            if ( kBase.getEventProcessingMode() != null ) {
                writer.addAttribute( "eventProcessingMode", kBase.getEventProcessingMode().getMode() );
            }
            if ( kBase.getEqualsBehavior() != null ) {
                writer.addAttribute( "equalsBehavior", kBase.getEqualsBehavior().toString().toLowerCase() );
            }
            if ( ! kBase.getPackages().isEmpty() ) {
                StringBuilder buf = new StringBuilder();
                boolean first = true;
                for( String pkg : kBase.getPackages() ) {
                    if( first ) {
                        first = false;
                    } else {
                        buf.append( ", " );
                    } 
                    buf.append( pkg );
                }
                writer.addAttribute( "packages", buf.toString() );
            }
            if ( !kBase.getIncludes().isEmpty() ) {
                StringBuilder sb = new StringBuilder();
                boolean insertComma = false;
                for ( String include : kBase.getIncludes() ) {
                    if ( insertComma ) {
                        sb.append( ", " );
                    }                    
                    sb.append( include );
                    if ( !insertComma ) {
                        insertComma = true;
                    }
                }
                writer.addAttribute( "includes", sb.toString() );
            }
            
            // @TODO We only support attribte of strings for now, added nested elements with filters later 
            //writeList( writer, "includes", "include", kBase.getIncludes() );

            for ( KieSessionModel kSessionModel :  kBase.getKieSessionModels().values()) {
                writeObject( writer, context, "ksession", kSessionModel);
            }            
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                                final UnmarshallingContext context) {
            final KieBaseModelImpl kBase = new KieBaseModelImpl();
            kBase.setName( reader.getAttribute( "name" ) );

            String eventMode = reader.getAttribute( "eventProcessingMode" );
            if ( eventMode != null ) {
                kBase.setEventProcessingMode( EventProcessingOption.determineEventProcessingMode( eventMode ) );
            }
            String equalsBehavior = reader.getAttribute( "equalsBehavior" );
            if ( equalsBehavior != null ) {
                kBase.setEqualsBehavior( AssertBehaviorOption.valueOf( equalsBehavior.toUpperCase() ) );
            }
            String pkgs = reader.getAttribute( "packages" );
            if( pkgs != null ) {
                for( String pkg : pkgs.split( "," ) ) {
                    kBase.addPackage( pkg.trim() );
                }
            }
            
            String includes = reader.getAttribute( "includes" );
            if( includes != null ) {
                for( String include : includes.split( "," ) ) {
                    kBase.addInclude( include.trim() );
                }
            }            

            readNodes( reader, new AbstractXStreamConverter.NodeReader() {
                public void onNode(HierarchicalStreamReader reader,
                                   String name,
                                   String value) {
                    if ( "ksession".equals( name ) ) {
                        KieSessionModelImpl kSession = readObject( reader, context, KieSessionModelImpl.class );
                        kBase.getRawKieSessionModels().put( kSession.getName(), kSession );
                        kSession.setKBase(kBase);
                    }
                    
                   // @TODO we don't use support nested includes
//                    if ( "includes".equals( name ) ) {
//                        for ( String include : readList( reader ) ) {
//                            kBase.addInclude( include );
//                        }
//                    }
                }
            } );
            return kBase;
        }
    }

    @Override
    public String toString() {
        return "KieBaseModelImpl [name=" + name + ", includes=" + includes + ", packages=" + getPackages() + ", equalsBehavior=" + equalsBehavior + ", eventProcessingMode=" + eventProcessingMode + ", kSessions=" + kSessions + "]";
    }

}
