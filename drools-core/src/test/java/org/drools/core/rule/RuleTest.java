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

package org.drools.core.rule;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.drools.core.SessionConfiguration;
import org.drools.core.WorkingMemory;
import org.junit.Test;
import static org.junit.Assert.*;

import org.drools.core.ClockType;
import org.drools.core.base.EnabledBoolean;
import org.drools.core.reteoo.ReteooRuleBase;
import org.drools.core.reteoo.RuleTerminalNode;
import org.drools.core.time.impl.PseudoClockScheduler;

public class RuleTest {

    @Test
    public void testDateEffective() {
        WorkingMemory wm = new ReteooRuleBase("x", null).newStatefulSession();

        final Rule rule = new Rule( "myrule" );

        assertTrue( rule.isEffective(null, new RuleTerminalNode(), wm ) );

        final Calendar earlier = Calendar.getInstance();
        earlier.setTimeInMillis( 10 );

        rule.setDateEffective( earlier );

        assertTrue( rule.isEffective(null, new RuleTerminalNode(), wm ) );

        final Calendar later = Calendar.getInstance();
        later.setTimeInMillis( later.getTimeInMillis() + 100000000 );

        assertTrue( later.after( Calendar.getInstance() ) );

        rule.setDateEffective( later );
        assertFalse( rule.isEffective(null, new RuleTerminalNode(), wm ) );

    }

    @Test
    public void testDateExpires() throws Exception {
        WorkingMemory wm = new ReteooRuleBase("x", null).newStatefulSession();
        
        final Rule rule = new Rule( "myrule" );

        assertTrue( rule.isEffective(null, new RuleTerminalNode(), wm ) );

        final Calendar earlier = Calendar.getInstance();
        earlier.setTimeInMillis( 10 );

        rule.setDateExpires( earlier );

        assertFalse( rule.isEffective(null, new RuleTerminalNode(), wm ) );

        final Calendar later = Calendar.getInstance();
        later.setTimeInMillis( later.getTimeInMillis() + 100000000 );

        rule.setDateExpires( later );
        assertTrue( rule.isEffective(null, new RuleTerminalNode(), wm ) );

    }

    @Test
    public void testDateEffectiveExpires() {
        WorkingMemory wm = new ReteooRuleBase("x",null).newStatefulSession();
        
        final Rule rule = new Rule( "myrule" );

        final Calendar past = Calendar.getInstance();
        past.setTimeInMillis( 10 );

        final Calendar future = Calendar.getInstance();
        future.setTimeInMillis( future.getTimeInMillis() + 100000000 );

        rule.setDateEffective( past );
        rule.setDateExpires( future );

        assertTrue( rule.isEffective(null, new RuleTerminalNode(), wm ) );

        rule.setDateExpires( past );
        assertFalse( rule.isEffective(null, new RuleTerminalNode(), wm ) );

        rule.setDateExpires( future );
        rule.setDateEffective( future );



        assertFalse( rule.isEffective(null, new RuleTerminalNode(), wm ) );

    }

    @Test
    public void testRuleEnabled() {
        WorkingMemory wm = new ReteooRuleBase("x", null).newStatefulSession();
        
        final Rule rule = new Rule( "myrule" );
        rule.setEnabled( EnabledBoolean.ENABLED_FALSE );
        assertFalse( rule.isEffective( null, new RuleTerminalNode(), wm ) );

        final Calendar past = Calendar.getInstance();
        past.setTimeInMillis( 10 );

        rule.setDateEffective( past );
        assertFalse( rule.isEffective( null, new RuleTerminalNode(), wm ) );
        rule.setEnabled( EnabledBoolean.ENABLED_TRUE );

        assertTrue( rule.isEffective( null, new RuleTerminalNode(), wm ) );
    }

    @Test
    public void testTimeMachine() {
        SessionConfiguration conf = new SessionConfiguration();
        conf.setClockType( ClockType.PSEUDO_CLOCK );
        WorkingMemory wm = new ReteooRuleBase("x", null).newStatefulSession(conf, null);
        
        final Calendar future = Calendar.getInstance();
        ((PseudoClockScheduler)wm.getSessionClock()).setStartupTime( future.getTimeInMillis() );
        
        final Rule rule = new Rule( "myrule" );
        rule.setEnabled( EnabledBoolean.ENABLED_TRUE );
        assertTrue( rule.isEffective(null, new RuleTerminalNode(), wm ) );

        
        future.setTimeInMillis( future.getTimeInMillis() + 100000000 );
        rule.setDateEffective(future);
        assertFalse( rule.isEffective(null, new RuleTerminalNode(), wm ) );

        ((PseudoClockScheduler)wm.getSessionClock()).advanceTime( 1000000000000L, TimeUnit.MILLISECONDS );
        
        assertTrue(rule.isEffective(null, new RuleTerminalNode(), wm ));
    }

}
