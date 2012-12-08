package org.drools.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.util.AnnotationLiteral;

import org.drools.kproject.models.KieSessionModelImpl;
import org.kie.KieBase;
import org.kie.builder.KieBaseModel;
import org.kie.builder.KieContainer;
import org.kie.builder.KieSessionModel;
import org.kie.builder.KieSessionModel.KieSessionType;
import org.kie.builder.impl.ClasspathKieProject;
import org.kie.builder.impl.InternalKieModule;
import org.kie.builder.impl.KieContainerImpl;
import org.kie.cdi.KBase;
import org.kie.cdi.KSession;
import org.kie.runtime.KieSession;
import org.kie.runtime.StatefulKnowledgeSession;
import org.kie.runtime.StatelessKieSession;
import org.kie.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KieCDIExtension
    implements
    Extension {

    private static final Logger log = LoggerFactory.getLogger( KieCDIExtension.class );

    private Set<KieCDIEntry>         kBaseNames;
    private Set<KieCDIEntry>         kSessionNames;

    ClasspathKieProject         kProject;

    public KieCDIExtension() {
    }

    public void init() {
        kProject = new ClasspathKieProject();
    }

    <Object> void processInjectionTarget(@Observes ProcessInjectionTarget<Object> pit, BeanManager beanManager) {
        if ( kProject == null ) {
            init();
        }

        // Find all uses of KieBaseModel and KieSessionModel and add to Set index
        if ( !pit.getInjectionTarget().getInjectionPoints().isEmpty() ) {
            for ( InjectionPoint ip : pit.getInjectionTarget().getInjectionPoints() ) {
                KBase kBase = ip.getAnnotated().getAnnotation( KBase.class );
                if ( kBase != null ) {                    
                    Class< ? extends Annotation> scope = ApplicationScoped.class;                    
                    if ( kBaseNames == null ) {
                        kBaseNames = new HashSet<KieCDIEntry>();
                    }
                    kBaseNames.add( new KieCDIEntry(kBase.value(), scope) );
                }

                KSession kSession = ip.getAnnotated().getAnnotation( KSession.class );
                if ( kSession != null ) {
                    Class< ? extends Annotation> scope = ApplicationScoped.class;
                    
                    if ( kSessionNames == null ) {
                        kSessionNames = new HashSet<KieCDIEntry>();
                    }
                    kSessionNames.add( new KieCDIEntry(kSession.value(), scope) );
                }
            }
        }

    }    

    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd,
                            BeanManager bm) {
        if ( kProject != null ) {
            // if kProjects null, processInjectionTarget was not called, so beans to create
            
            KieContainerImpl kContainer = new KieContainerImpl( kProject, null );
            
            if ( kBaseNames != null ) {
                for ( KieCDIEntry entry : kBaseNames ) {
                    String kBaseQName = entry.getName();
                    KieBaseModel kBaseModel = kProject.getKieBaseModel( kBaseQName );
                    if ( kBaseModel == null ) {
                        log.error( "Annotation @KBase({}) found, but no KieBaseModel exist.\nEither the required kproject.xml does not exist, was corrupted, or mising the KieBase entry",
                                   kBaseQName );
                        continue;
                    }
                    if ( !kBaseModel.getScope().trim().equals( entry.getScope().getClass().getName()  ) ) {
                        try {
                            if (kBaseModel.getScope().indexOf( '.' ) >= 0 ) {
                                entry.setScope( (Class< ? extends Annotation>) Class.forName( kBaseModel.getScope() ) );
                            } else {
                                entry.setScope( (Class< ? extends Annotation>) Class.forName( "javax.enterprise.context." + kBaseModel.getScope() ) );                                
                            }
                        } catch ( ClassNotFoundException e ) {
                            log.error( "KieBaseModule {} overrides default annotation, but it was not able to find it {}\n{}", new String[] { kBaseQName, kBaseModel.getScope(), e.getMessage() } );
                        }
                    }
                    KBaseBean bean = new KBaseBean( kBaseModel,
                                                    kContainer,
                                                    entry.getScope() );
                    if ( log.isDebugEnabled() ) {
                        InternalKieModule kModule = (InternalKieModule) kProject.getKieModuleForKBase( kBaseQName );
                        log.debug( "Added Bean for @KBase({})",
                                   kBaseQName,
                                   kModule.getFile() );
                    }
                    abd.addBean( bean );
                }
            }
            kBaseNames = null;
            
            if ( kSessionNames != null ) {
                for ( KieCDIEntry entry : kSessionNames ) {
                    String kSessionName = entry.getName();
                    KieSessionModel kSessionModel = kProject.getKieSessionModel( kSessionName );
                    if ( kSessionModel == null ) {
                        log.error( "Annotation @KSession({}) found, but no KieSessioneModel exist.\nEither the required kproject.xml does not exist, was corrupted, or mising the KieBase entry",
                                   kSessionName );
                        continue;
                    }

                    if ( !kSessionModel.getScope().trim().equals( entry.getScope().getClass().getName()  ) ) {
                        try {
                            if (kSessionModel.getScope().indexOf( '.' ) >= 0 ) {
                                entry.setScope( (Class< ? extends Annotation>) Class.forName( kSessionModel.getScope() ) );
                            } else {
                                entry.setScope( (Class< ? extends Annotation>) Class.forName( "javax.enterprise.context." + kSessionModel.getScope() ) );                                
                            }
                        } catch ( ClassNotFoundException e ) {
                            log.error( "KieBaseModule {} overrides default annotation, but it was not able to find it {}\n{}", new String[] { kSessionName, kSessionModel.getScope(), e.getMessage() } );
                        }
                    }
                    
                    if ( KieSessionType.STATELESS.equals( kSessionModel.getType() ) ) {
                        if ( log.isDebugEnabled() ) {
                            InternalKieModule kModule = (InternalKieModule) kProject.getKieModuleForKBase( ((KieSessionModelImpl) kSessionModel).getKieBaseModel().getName() );
                            log.debug( "Added Bean for Stateless @KSession({}) from: {}",
                                       kSessionName,
                                       kModule.getFile() );
                        }
                        abd.addBean( new StatelessKSessionBean( kSessionModel,
                                                                kContainer,
                                                                entry.getScope() ) );
                    } else {
                        InternalKieModule kModule = (InternalKieModule) kProject.getKieModuleForKBase( ((KieSessionModelImpl) kSessionModel).getKieBaseModel().getName() );
                        log.debug( "Added Bean for Stateful @KSession({})  from: {}",
                                   kSessionName,
                                   kModule.getFile() );
                        abd.addBean( new StatefulKSessionBean( kSessionModel,
                                                               kContainer,
                                                               entry.getScope() ) );
                    }
                }
            }
            kSessionNames = null;
        }
    }

    public static class KBaseBean
        implements
        Bean<KieBase> {
        static final Set<Type>        types = Collections.unmodifiableSet( new HashSet<Type>( Arrays.asList( KieBase.class,
                                                                                                             Object.class ) ) );

        private Set<Annotation>              qualifiers;

        private KieContainer                 kieContainer;

        private KieBaseModel                 kBaseModel;

        private Class< ? extends Annotation> scope;

        public KBaseBean(final KieBaseModel kBaseModel,
                         KieContainer kieContainer,
                         Class< ? extends Annotation> scope) {
            this.kBaseModel = kBaseModel;
            this.kieContainer = kieContainer;

            this.scope = scope;
            
            this.qualifiers = Collections.unmodifiableSet( new HashSet<Annotation>( Arrays.asList( new AnnotationLiteral<Default>() {
                                                                                                   },
                                                                                                   new AnnotationLiteral<Any>() {
                                                                                                   },
                                                                                                   new KBase() {
                                                                                                       public Class< ? extends Annotation> annotationType() {
                                                                                                           return KBase.class;
                                                                                                       }

                                                                                                       public String value() {
                                                                                                           return kBaseModel.getName();
                                                                                                       }
                                                                                                   }
                    ) ) );
        }

        public KieBase create(CreationalContext ctx) {
            KieBase kieBase = kieContainer.getKieBase( kBaseModel.getName() );
            return kieBase;
        }

        public void destroy(KieBase kBase,
                            CreationalContext ctx) {
            this.kieContainer = null;
            ctx.release();
        }

        public Class getBeanClass() {
            return KieBase.class;
        }

        public Set<InjectionPoint> getInjectionPoints() {
            return Collections.emptySet();
        }

        public String getName() {
            return kBaseModel.getName();
        }

        public Set<Annotation> getQualifiers() {
            return qualifiers;
        }

        public Class< ? extends Annotation> getScope() {
            return this.scope;
        }

        public Set<Class< ? extends Annotation>> getStereotypes() {
            return Collections.emptySet();
        }

        public Set<Type> getTypes() {
            return types;
        }

        public boolean isAlternative() {
            return false;
        }

        public boolean isNullable() {
            return false;
        }
    }

    public static class StatelessKSessionBean
        implements
        Bean<StatelessKieSession> {
        static final Set<Type>  types = Collections.unmodifiableSet( new HashSet<Type>( Arrays.asList( StatelessKieSession.class,
                                                                                                       Object.class ) ) );

        private Set<Annotation> qualifiers;

        private KieSessionModel kSessionModel;

        private KieContainer    kieContainer;
        
        private Class< ? extends Annotation> scope;

        public StatelessKSessionBean(final KieSessionModel kieSessionModelModel,
                                     KieContainer kieContainer,
                                     Class< ? extends Annotation> scope) {
            this.kSessionModel = kieSessionModelModel;
            this.kieContainer = kieContainer;
            this.scope = scope;

            this.qualifiers = Collections.unmodifiableSet( new HashSet<Annotation>( Arrays.asList( new AnnotationLiteral<Default>() {
                                                                                                   },
                                                                                                   new AnnotationLiteral<Any>() {
                                                                                                   },
                                                                                                   new KSession() {
                                                                                                       public Class< ? extends Annotation> annotationType() {
                                                                                                           return KSession.class;
                                                                                                       }

                                                                                                       public String value() {
                                                                                                           return kieSessionModelModel.getName();
                                                                                                       }
                                                                                                   }
                    ) ) );
        }

        public StatelessKieSession create(CreationalContext ctx) {
            return kieContainer.getKieStatelessSession( kSessionModel.getName() );
        }

        public void destroy(StatelessKieSession kSession,
                            CreationalContext ctx) {
            ctx.release();
        }

        public Class getBeanClass() {
            return StatelessKnowledgeSession.class;
        }

        public Set<InjectionPoint> getInjectionPoints() {
            return Collections.emptySet();
        }

        public String getName() {
            return kSessionModel.getName();
        }

        public Set<Annotation> getQualifiers() {
            return qualifiers;
        }

        public Class< ? extends Annotation> getScope() {
            return scope;
        }

        public Set<Class< ? extends Annotation>> getStereotypes() {
            return Collections.emptySet();
        }

        public Set<Type> getTypes() {
            return types;
        }

        public boolean isAlternative() {
            return false;
        }

        public boolean isNullable() {
            return false;
        }
    }

    public static class StatefulKSessionBean
        implements
        Bean<KieSession> {
        static final Set<Type>        types = Collections.unmodifiableSet( new HashSet<Type>( Arrays.asList( KieSession.class,
                                                                                                             Object.class ) ) );

        private Set<Annotation>       qualifiers;

        private KieSessionModel       kSessionModel;

        private KieContainer          kContainer;
        
        private Class< ? extends Annotation> scope;

        public StatefulKSessionBean(final KieSessionModel kieSessionModelModel,
                                    KieContainer kContainer,
                                    Class< ? extends Annotation> scope) {
            this.kSessionModel = kieSessionModelModel;
            this.kContainer = kContainer;
            this.scope = scope;

            this.qualifiers = Collections.unmodifiableSet( new HashSet<Annotation>( Arrays.asList( new AnnotationLiteral<Default>() {
                                                                                                   },
                                                                                                   new AnnotationLiteral<Any>() {
                                                                                                   },
                                                                                                   new KSession() {
                                                                                                       public Class< ? extends Annotation> annotationType() {
                                                                                                           return KSession.class;
                                                                                                       }

                                                                                                       public String value() {
                                                                                                           return kieSessionModelModel.getName();
                                                                                                       }
                                                                                                   }
                    ) ) );
        }

        public KieSession create(CreationalContext ctx) {
            return kContainer.getKieSession( kSessionModel.getName() );
        }

        public void destroy(KieSession kBase,
                            CreationalContext ctx) {
            ctx.release();
        }

        public Class getBeanClass() {
            return StatefulKnowledgeSession.class;
        }

        public Set<InjectionPoint> getInjectionPoints() {
            return Collections.emptySet();
        }

        public String getName() {
            return null;
        }

        public Set<Annotation> getQualifiers() {
            return qualifiers;
        }

        public Class< ? extends Annotation> getScope() {
            return this.scope;
        }

        public Set<Class< ? extends Annotation>> getStereotypes() {
            return Collections.emptySet();
        }

        public Set<Type> getTypes() {
            return types;
        }

        public boolean isAlternative() {
            return false;
        }

        public boolean isNullable() {
            return false;
        }
    }
    
    public static class KieCDIEntry {
        private String name;
        private Class< ? extends Annotation>  scope;
        
        public KieCDIEntry(String name,
                           Class< ? extends Annotation>  scope) {
            super();
            this.name = name;
            this.scope = scope;
        }

        public KieCDIEntry(String name) {
            super();
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }

        public void setScope(Class< ? extends Annotation> scope) {
            this.scope = scope;
        }

        public Class< ? extends Annotation>  getScope() {
            return scope;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((scope == null) ? 0 : scope.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(java.lang.Object obj) {
            if ( this == obj ) return true;
            if ( obj == null ) return false;
            if ( getClass() != obj.getClass() ) return false;
            KieCDIEntry other = (KieCDIEntry) obj;
            if ( name == null ) {
                if ( other.name != null ) return false;
            } else if ( !name.equals( other.name ) ) return false;
            if ( scope == null ) {
                if ( other.scope != null ) return false;
            } else if ( !scope.equals( other.scope ) ) return false;
            return true;
        }
        
        @Override
        public String toString() {
            return "KSessionEntry [name=" + name + ", scope=" + scope + "]";
        }
        
        
    }    
}
