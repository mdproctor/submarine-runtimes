package org.drools.games.adventures;

import org.apache.commons.io.IOUtils;
import org.drools.core.common.InternalRuleBase;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KnowledgeBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.kie.api.runtime.rule.FactHandle;
import org.mvel2.MVEL;
import org.mvel2.integration.impl.MapVariableResolverFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.kie.internal.io.ResourceFactory.newClassPathResource;
import static org.kie.api.io.ResourceType.DRL;

public class GameEngine {

    StatefulKnowledgeSession ksession;

    ClassLoader              classLoader;

    public void createGame() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        kbuilder.batch().add( newClassPathResource( "Model.drl", getClass()  ), DRL )
                        .add( newClassPathResource( "Queries.drl",  getClass()  ), DRL )
                        .add( newClassPathResource( "General.drl",  getClass()  ), DRL )
                        .add( newClassPathResource( "Response.drl",  getClass()  ), DRL )
                        .add( newClassPathResource( "Events.drl",  getClass()  ), DRL )
                        .add( newClassPathResource( "UiView.drl", getClass() ), DRL )
                        .add( newClassPathResource( "Commands.drl", getClass() ), DRL ).build();

        if ( kbuilder.hasErrors() ) {
            System.out.println( kbuilder.getErrors().toString() );
            System.exit( 1 );
        }

        KieBaseConfiguration kbaseConf = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        kbaseConf.setOption( EqualityBehaviorOption.EQUALITY );

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase( kbaseConf );
        kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );

        Counter c = new Counter();
        ksession = kbase.newStatefulKnowledgeSession();

        ksession.setGlobal( "counter",
                            c );

        classLoader = (((InternalRuleBase) ((KnowledgeBaseImpl) kbase).ruleBase).getRootClassLoader());
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            Map vars = new HashMap();
            vars.put( "c",
                      c );
            Map<String, Map> map;
            try {
                map = (Map<String, Map>) MVEL.executeExpression( MVEL.compileExpression( new String( IOUtils.toByteArray(getClass().getResource("data.mvel").openStream()) ) ),
                                                                 vars );
            } catch ( IOException e ) {
                throw new RuntimeException( e );
            }

            for ( Object o : map.get( "rooms" ).values() ) {
                ksession.insert( o );
            }

            for ( Object o : map.get( "doors" ).values() ) {
                ksession.insert( o );
            }

            for ( Object o : map.get( "characters" ).values() ) {
                ksession.insert( o );
            }

            for ( Object o : map.get( "items" ).values() ) {
                ksession.insert( o );
            }

            for ( Object o : map.get( "locations" ).values() ) {
                ksession.insert( o );
            }

            MapVariableResolverFactory f = new MapVariableResolverFactory( map );

            String baseStr = "import  org.drools.games.adventures.*;  import org.drools.games.adventures.commands.*;\n";
            FactHandle fh = ksession.insert( MVEL.eval( baseStr + "new EnterEvent( characters['hero'], rooms['first floor hallway'] )",
                                                        f ) );
            ksession.fireAllRules();
        } finally {
            Thread.currentThread().setContextClassLoader( currentClassLoader );
        }
    }

    public void receiveMessage(UserSession session,
                               List cmd) {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        String baseStr = "import  org.drools.games.adventures.*;  import org.drools.games.adventures.commands.*;\n";
        try {
            Thread.currentThread().setContextClassLoader( classLoader );
            Map vars = new HashMap();
            vars.put( "args",
                      cmd );
            MapVariableResolverFactory f = new MapVariableResolverFactory( vars );
            Action c = (Action) cmd.get( 0 );
            switch ( c ) {
                case MOVE : {
                    ksession.insert( new Request( session,
                                                  MVEL.eval( baseStr + "new MoveCommand(args[1], args[2])",
                                                             f ) ) );
                    ksession.fireAllRules();
                    break;
                }
                case PICKUP : {
                    ksession.insert( new Request( session,
                                                  MVEL.eval( baseStr + "new PickupCommand(args[1], args[2])",
                                                             f ) ) );
                    ksession.fireAllRules();
                    break;
                }
                case DROP : {
                    ksession.insert( new Request( session,
                                                  MVEL.eval( baseStr + "new DropCommand(args[1], args[2])",
                                                             f ) ) );
                    ksession.fireAllRules();
                    break;
                }
                case GIVE : {
                    ksession.insert( new Request( session,
                                                  MVEL.eval( baseStr + "new GiveCommand(args[1], args[2], args[3])",
                                                             f ) ) );
                    ksession.fireAllRules();
                    break;
                }
                case LOOK : {
                    ksession.insert( new Request( session,
                                                  MVEL.eval( baseStr + "new LookCommand(args[1])",
                                                             f ) ) );
                    ksession.fireAllRules();
                    break;
                }
                case SELECT_CHARACTER : {
                    ksession.insert( new Request( session,
                                                  MVEL.eval( baseStr + "new SetSessionCharacterCommand(args[1], args[2])",
                                                             f ) ) );
                    ksession.fireAllRules();
                    break;
                }
            }
        } catch ( Exception e ) {
            session.getChannels().get( "output" ).send( "Unable to Execute Command: " + cmd );
        } finally {
            Thread.currentThread().setContextClassLoader( currentClassLoader );
        }

    }

}
