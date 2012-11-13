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

package org.drools.event.knowledgebase;

import org.drools.Cheese;
import org.drools.WorkingMemory;
import org.drools.base.ClassFieldAccessorCache;
import org.drools.base.ClassFieldAccessorStore;
import org.drools.base.ClassFieldReader;
import org.drools.base.ClassObjectType;
import org.drools.base.FieldFactory;
import org.drools.definitions.impl.KnowledgePackageImp;
import org.drools.rule.MvelConstraintTestUtil;
import org.drools.rule.Package;
import org.drools.rule.Pattern;
import org.drools.rule.Rule;
import org.drools.rule.constraint.MvelConstraint;
import org.drools.spi.Consequence;
import org.drools.spi.FieldValue;
import org.drools.spi.KnowledgeHelper;
import org.junit.Before;
import org.junit.Test;
import org.kie.KnowledgeBase;
import org.kie.KnowledgeBaseFactory;
import org.kie.definition.KnowledgePackage;
import org.kie.event.knowledgebase.AfterFunctionRemovedEvent;
import org.kie.event.knowledgebase.AfterKnowledgeBaseLockedEvent;
import org.kie.event.knowledgebase.AfterKnowledgeBaseUnlockedEvent;
import org.kie.event.knowledgebase.AfterKnowledgePackageAddedEvent;
import org.kie.event.knowledgebase.AfterKnowledgePackageRemovedEvent;
import org.kie.event.knowledgebase.AfterProcessAddedEvent;
import org.kie.event.knowledgebase.AfterProcessRemovedEvent;
import org.kie.event.knowledgebase.AfterRuleAddedEvent;
import org.kie.event.knowledgebase.AfterRuleRemovedEvent;
import org.kie.event.knowledgebase.BeforeFunctionRemovedEvent;
import org.kie.event.knowledgebase.BeforeKnowledgeBaseLockedEvent;
import org.kie.event.knowledgebase.BeforeKnowledgeBaseUnlockedEvent;
import org.kie.event.knowledgebase.BeforeKnowledgePackageAddedEvent;
import org.kie.event.knowledgebase.BeforeKnowledgePackageRemovedEvent;
import org.kie.event.knowledgebase.BeforeProcessAddedEvent;
import org.kie.event.knowledgebase.BeforeProcessRemovedEvent;
import org.kie.event.knowledgebase.BeforeRuleAddedEvent;
import org.kie.event.knowledgebase.BeforeRuleRemovedEvent;
import org.kie.event.knowledgebase.KnowledgeBaseEventListener;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class KnowledgeBaseEventSupportTest {

    private KnowledgeBase        kbase;
    private TestRuleBaseListener listener1;
    private TestRuleBaseListener listener2;
    private KnowledgePackageImp  pkg;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        listener1 = new TestRuleBaseListener( "(listener-1) " );
        listener2 = new TestRuleBaseListener( "(listener-2) " );
        kbase.addEventListener( listener1 );
        kbase.addEventListener( listener2 );

        final Rule rule1 = new Rule( "test1" );
        final ClassObjectType cheeseObjectType = new ClassObjectType( Cheese.class );
        final Pattern pattern = new Pattern( 0,
                                             cheeseObjectType );

        ClassFieldAccessorStore store = new ClassFieldAccessorStore();
        store.setClassFieldAccessorCache( new ClassFieldAccessorCache( Thread.currentThread().getContextClassLoader() ) );
        store.setEagerWire( true );

        final ClassFieldReader extractor = store.getReader(Cheese.class,
                "type",
                getClass().getClassLoader());

        final FieldValue field = FieldFactory.getInstance().getFieldValue( "cheddar" );

        final MvelConstraint constraint = new MvelConstraintTestUtil("type == \"cheddar\"", field, extractor);

        pattern.addConstraint( constraint );
        rule1.addPattern( pattern );

        rule1.setConsequence( new Consequence() {
            private static final long serialVersionUID = 510l;

            public void evaluate(final KnowledgeHelper knowledgeHelper,
                                 final WorkingMemory workingMemory) throws Exception {
            }

            public void readExternal(ObjectInput in) throws IOException,
                                                    ClassNotFoundException {

            }

            public void writeExternal(ObjectOutput out) throws IOException {

            }
            
            public String getName() {
                return "default";
            }
        } );

        final Rule rule2 = new Rule( "test2" );
        final ClassObjectType cheeseObjectType2 = new ClassObjectType( Cheese.class );
        final Pattern pattern2 = new Pattern( 0,
                                              cheeseObjectType2 );

        final FieldValue field2 = FieldFactory.getInstance().getFieldValue( "stilton" );

        final MvelConstraint constraint2 = new MvelConstraintTestUtil("type == \"stilton\"", field, extractor);

        pattern2.addConstraint( constraint2 );
        rule2.addPattern( pattern2 );

        rule2.setConsequence( new Consequence() {
            private static final long serialVersionUID = 510l;

            public void evaluate(final KnowledgeHelper knowledgeHelper,
                                 final WorkingMemory workingMemory) throws Exception {
            }

            public void readExternal(ObjectInput in) throws IOException,
                                                    ClassNotFoundException {

            }

            public void writeExternal(ObjectOutput out) throws IOException {

            }
            
            public String getName() {
                return "default";
            }
        } );

        pkg = new KnowledgePackageImp( new Package( "org.drools.test1" ) );
        pkg.pkg.addRule( rule1 );
        pkg.pkg.addRule( rule2 );
    }

    @Test
    public void testAddPackageEvents() throws Exception {
        assertEquals( 0,
                      listener1.getBeforePackageAdded() );
        assertEquals( 0,
                      listener1.getAfterPackageAdded() );
        assertEquals( 0,
                      listener2.getBeforePackageAdded() );
        assertEquals( 0,
                      listener2.getAfterPackageAdded() );
        assertEquals( 0,
                      listener1.getBeforeRuleAdded() );
        assertEquals( 0,
                      listener1.getAfterRuleAdded() );
        assertEquals( 0,
                      listener2.getBeforeRuleAdded() );
        assertEquals( 0,
                      listener2.getAfterRuleAdded() );

        List<KnowledgePackage> pkgs = new ArrayList<KnowledgePackage>();
        pkgs.add( pkg );
        this.kbase.addKnowledgePackages( pkgs );

        assertEquals( 1,
                      listener1.getBeforePackageAdded() );
        assertEquals( 1,
                      listener1.getAfterPackageAdded() );
        assertEquals( 1,
                      listener2.getBeforePackageAdded() );
        assertEquals( 1,
                      listener2.getAfterPackageAdded() );
        assertEquals( 2,
                      listener1.getBeforeRuleAdded() );
        assertEquals( 2,
                      listener1.getAfterRuleAdded() );
        assertEquals( 2,
                      listener2.getBeforeRuleAdded() );
        assertEquals( 2,
                      listener2.getAfterRuleAdded() );
    }

    @Test
    public void testRemovePackageEvents() throws Exception {
        List<KnowledgePackage> pkgs = new ArrayList<KnowledgePackage>();
        pkgs.add( pkg );
        this.kbase.addKnowledgePackages( pkgs );

        assertEquals( 0,
                      listener1.getBeforeKnowledgePackageRemoved() );
        assertEquals( 0,
                      listener1.getAfterKnowledgePackageRemoved() );
        assertEquals( 0,
                      listener2.getBeforeKnowledgePackageRemoved() );
        assertEquals( 0,
                      listener2.getAfterKnowledgePackageRemoved() );

        assertEquals( 0,
                      listener1.getBeforeRuleRemoved() );
        assertEquals( 0,
                      listener1.getAfterRuleRemoved() );
        assertEquals( 0,
                      listener2.getBeforeRuleRemoved() );
        assertEquals( 0,
                      listener2.getAfterRuleRemoved() );

        this.kbase.removeKnowledgePackage( "org.drools.test1" );

        assertEquals( 1,
                      listener1.getBeforeKnowledgePackageRemoved() );
        assertEquals( 1,
                      listener1.getAfterKnowledgePackageRemoved() );
        assertEquals( 1,
                      listener2.getBeforeKnowledgePackageRemoved() );
        assertEquals( 1,
                      listener2.getAfterKnowledgePackageRemoved() );
        assertEquals( 2,
                      listener1.getBeforeRuleRemoved() );
        assertEquals( 2,
                      listener1.getAfterRuleRemoved() );
        assertEquals( 2,
                      listener2.getBeforeRuleRemoved() );
        assertEquals( 2,
                      listener2.getAfterRuleRemoved() );

    }

    public static class TestRuleBaseListener
        implements
        KnowledgeBaseEventListener {
        private String id;
        private int    beforePackageAdded   = 0;
        private int    afterPackageAdded    = 0;
        private int    beforePackageRemoved = 0;
        private int    afterPackageRemoved  = 0;
        private int    beforeRuleAdded      = 0;
        private int    afterRuleAdded       = 0;
        private int    beforeRuleRemoved    = 0;
        private int    afterRuleRemoved     = 0;

        public TestRuleBaseListener() {
        }

        public TestRuleBaseListener(String id) {
            super();
            this.id = id;
        }

        public void readExternal(ObjectInput in) throws IOException,
                                                ClassNotFoundException {
            id = (String) in.readObject();
            beforePackageAdded = in.readInt();
            afterPackageAdded = in.readInt();
            beforePackageRemoved = in.readInt();
            afterPackageRemoved = in.readInt();
            beforeRuleAdded = in.readInt();
            afterRuleAdded = in.readInt();
            beforeRuleRemoved = in.readInt();
            afterRuleRemoved = in.readInt();
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject( id );
            out.writeInt( beforePackageAdded );
            out.writeInt( afterPackageAdded );
            out.writeInt( beforePackageRemoved );
            out.writeInt( afterPackageRemoved );
            out.writeInt( beforeRuleAdded );
            out.writeInt( afterRuleAdded );
            out.writeInt( beforeRuleRemoved );
            out.writeInt( afterRuleRemoved );
        }

        public void afterKnowledgePackageAdded(AfterKnowledgePackageAddedEvent event) {
            //            System.out.println( this.id + event );
            this.afterPackageAdded++;
        }

        public void beforeKnowledgePackageAdded(BeforeKnowledgePackageAddedEvent event) {
            //            System.out.println( this.id + event );
            this.beforePackageAdded++;
        }

        protected int getAfterPackageAdded() {
            return afterPackageAdded;
        }

        protected int getBeforePackageAdded() {
            return beforePackageAdded;
        }

        protected String getId() {
            return id;
        }

        public void afterKnowledgePackageRemoved(AfterKnowledgePackageRemovedEvent event) {
            //            System.out.println( this.id + event );
            this.afterPackageRemoved++;
        }

        public void beforeKnowledgePackageRemoved(BeforeKnowledgePackageRemovedEvent event) {
            //            System.out.println( this.id + event );
            this.beforePackageRemoved++;
        }

        protected int getAfterKnowledgePackageRemoved() {
            return afterPackageRemoved;
        }

        protected int getBeforeKnowledgePackageRemoved() {
            return beforePackageRemoved;
        }

        public int getAfterRuleAdded() {
            return afterRuleAdded;
        }

        public int getBeforeRuleAdded() {
            return beforeRuleAdded;
        }

        public void afterRuleAdded(AfterRuleAddedEvent event) {
            //            System.out.println( this.id + event );
            this.afterRuleAdded++;
        }

        public void beforeRuleAdded(BeforeRuleAddedEvent event) {
            //            System.out.println( this.id + event );
            this.beforeRuleAdded++;
        }

        public int getAfterRuleRemoved() {
            return afterRuleRemoved;
        }

        public int getBeforeRuleRemoved() {
            return beforeRuleRemoved;
        }

        public void afterRuleRemoved(AfterRuleRemovedEvent event) {
            //            System.out.println( this.id + event );
            this.afterRuleRemoved++;
        }

        public void beforeRuleRemoved(BeforeRuleRemovedEvent event) {
            //            System.out.println( this.id + event );
            this.beforeRuleRemoved++;
        }

        public void afterFunctionRemoved(AfterFunctionRemovedEvent event) {
            // TODO Auto-generated method stub

        }

        public void afterKnowledgeBaseLocked(AfterKnowledgeBaseLockedEvent event) {
            // TODO Auto-generated method stub

        }

        public void afterKnowledgeBaseUnlocked(AfterKnowledgeBaseUnlockedEvent event) {
            // TODO Auto-generated method stub

        }

        public void beforeFunctionRemoved(BeforeFunctionRemovedEvent event) {
            // TODO Auto-generated method stub

        }

        public void beforeKnowledgeBaseLocked(BeforeKnowledgeBaseLockedEvent event) {
            // TODO Auto-generated method stub

        }

        public void beforeKnowledgeBaseUnlocked(BeforeKnowledgeBaseUnlockedEvent event) {
            // TODO Auto-generated method stub

        }

		public void afterProcessAdded(AfterProcessAddedEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void afterProcessRemoved(AfterProcessRemovedEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void beforeProcessAdded(BeforeProcessAddedEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void beforeProcessRemoved(BeforeProcessRemovedEvent arg0) {
			// TODO Auto-generated method stub
			
		}

    }

}
