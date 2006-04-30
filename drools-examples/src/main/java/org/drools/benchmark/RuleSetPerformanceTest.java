package org.drools.benchmark;
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
import junit.framework.TestCase;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

import org.drools.PackageIntegrationException;
import org.drools.RuleBase;
import org.drools.RuleIntegrationException;
import org.drools.WorkingMemory;
import org.drools.compiler.DrlParser;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.lang.descr.PackageDescr;
import org.drools.reteoo.RuleBaseImpl;
import org.drools.rule.InvalidPatternException;
import org.drools.rule.Package;

import org.drools.benchmark.models.Account;

/**
 * @author Peter Lin
 *
 */
public class RuleSetPerformanceTest {
    
    public static final void main(final String[] args) {
        RuleSetPerformanceTest test = new RuleSetPerformanceTest();
        test.test2KRuleFire3Conditions();
        test.test2KRuleFire2Conditions();
        test.test2KRuleFire();
        test.testFiveHundredRuleFire();
        test.testFiftyRuleFire();
        test.testFiveRuleFire();        
    }    
    
    private static RuleBase readRule(String file) throws IOException,
                                                 DroolsParserException,
                                                 RuleIntegrationException,
                                                 PackageIntegrationException,
                                                 InvalidPatternException {
        //read in the source
        Reader reader = new InputStreamReader( RuleSetPerformanceTest.class.getResourceAsStream( file ) );
        DrlParser parser = new DrlParser();
        PackageDescr packageDescr = parser.parse( reader );

        //pre build the package
        PackageBuilder builder = new PackageBuilder();
        builder.addPackage( packageDescr );
        Package pkg = builder.getPackage();

        //add the package to a rulebase
        RuleBaseImpl ruleBase = new RuleBaseImpl();
        ruleBase.addPackage( pkg );
        return ruleBase;
    }

    /**
     * test the performance with 50 facts and 5 rules. The test measures
     * the load, assert and fire time.
     */
    public void testFiveRuleFire() {
        try {
            int factCount = 50000;
            String file = "5_rules.drl";
            int loop = 5;
            long totalload = 0;
            long totalassert = 0;
            long totalfire = 0;
            for ( int c = 0; c < loop; c++ ) {
                long loadStart = System.currentTimeMillis();
                RuleBase ruleBase = readRule( file );
                long loadEnd = System.currentTimeMillis();
                long loadet = loadEnd - loadStart;
                totalload += loadet;
                System.out.println( "time to load " + file + " " + loadet + "ms" );
                WorkingMemory workingMemory = ruleBase.newWorkingMemory();
                ArrayList objects = new ArrayList();
                // create the objects
                for ( int idx = 0; idx < factCount; idx++ ) {
                    Account acc = new Account();
                    acc.setAccountId( "acc" + idx );
                    acc.setTitle( "mr" );
                    acc.setStatus( "standard" );
                    objects.add( acc );
                }
                Iterator itr = objects.iterator();
                long assertStart = System.currentTimeMillis();
                while ( itr.hasNext() ) {
                    workingMemory.assertObject( itr.next() );
                }
                long assertEnd = System.currentTimeMillis();
                long assertet = assertEnd - assertStart;
                totalassert += assertet;
                System.out.println( "time to assert " + assertet + " ms" );
                long fireStart = System.currentTimeMillis();
                workingMemory.fireAllRules();
                long fireEnd = System.currentTimeMillis();
                long fireet = fireEnd - fireStart;
                totalfire += fireet;
                System.out.println( "time to fireAllRules " + fireet + " ms" );
                workingMemory.dispose();
            }
            System.out.println( file );
            System.out.println( "number of objects asserted " + factCount );
            System.out.println( "average load " + (totalload / loop) + " ms" );
            System.out.println( "average assert " + (totalassert / loop) + " ms" );
            System.out.println( "average fire " + (totalfire / loop) + " ms" );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * 
     *
     */
    public void testFiftyRuleFire() {
        try {
            int factCount = 50000;
            String file = "50_rules.drl";
            int loop = 5;
            long totalload = 0;
            long totalassert = 0;
            long totalfire = 0;
            for ( int c = 0; c < loop; c++ ) {
                long loadStart = System.currentTimeMillis();
                RuleBase ruleBase = readRule( file );
                long loadEnd = System.currentTimeMillis();
                long loadet = loadEnd - loadStart;
                totalload += loadet;
                System.out.println( "time to load " + file + " " + loadet + "ms" );
                WorkingMemory workingMemory = ruleBase.newWorkingMemory();
                ArrayList objects = new ArrayList();
                // create the objects
                for ( int idx = 0; idx < factCount; idx++ ) {
                    Account acc = new Account();
                    acc.setAccountId( "acc" + idx );
                    acc.setTitle( "mr" );
                    acc.setStatus( "standard" );
                    objects.add( acc );
                }
                Iterator itr = objects.iterator();
                long assertStart = System.currentTimeMillis();
                while ( itr.hasNext() ) {
                    workingMemory.assertObject( itr.next() );
                }
                long assertEnd = System.currentTimeMillis();
                long assertet = assertEnd - assertStart;
                totalassert += assertet;
                System.out.println( "time to assert " + assertet + " ms" );
                long fireStart = System.currentTimeMillis();
                workingMemory.fireAllRules();
                long fireEnd = System.currentTimeMillis();
                long fireet = fireEnd - fireStart;
                totalfire += fireet;
                System.out.println( "time to fireAllRules " + fireet + " ms" );
                workingMemory.dispose();
            }
            System.out.println( file );
            System.out.println( "number of objects asserted " + factCount );
            System.out.println( "average load " + (totalload / loop) + " ms" );
            System.out.println( "average assert " + (totalassert / loop) + " ms" );
            System.out.println( "average fire " + (totalfire / loop) + " ms" );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * 
     *
     */
    public void testFiveHundredRuleFire() {
        try {
            int factCount = 50000;
            String file = "500_rules.drl";
            int loop = 5;
            long totalload = 0;
            long totalassert = 0;
            long totalfire = 0;
            for ( int c = 0; c < loop; c++ ) {
                long loadStart = System.currentTimeMillis();
                RuleBase ruleBase = readRule( file );
                long loadEnd = System.currentTimeMillis();
                long loadet = loadEnd - loadStart;
                totalload += loadet;
                System.out.println( "time to load " + file + " " + loadet + "ms" );
                WorkingMemory workingMemory = ruleBase.newWorkingMemory();
                ArrayList objects = new ArrayList();
                // create the objects
                for ( int idx = 0; idx < factCount; idx++ ) {
                    Account acc = new Account();
                    acc.setAccountId( "acc" + idx );
                    acc.setTitle( "mr" );
                    acc.setStatus( "standard" );
                    objects.add( acc );
                }
                Iterator itr = objects.iterator();
                long assertStart = System.currentTimeMillis();
                while ( itr.hasNext() ) {
                    workingMemory.assertObject( itr.next() );
                }
                long assertEnd = System.currentTimeMillis();
                long assertet = assertEnd - assertStart;
                totalassert += assertet;
                System.out.println( "time to assert " + assertet + " ms" );
                long fireStart = System.currentTimeMillis();
                workingMemory.fireAllRules();
                long fireEnd = System.currentTimeMillis();
                long fireet = fireEnd - fireStart;
                totalfire += fireet;
                System.out.println( "time to fireAllRules " + fireet + " ms" );
                workingMemory.dispose();
            }
            System.out.println( file );
            System.out.println( "number of objects asserted " + factCount );
            System.out.println( "average load " + (totalload / loop) + " ms" );
            System.out.println( "average assert " + (totalassert / loop) + " ms" );
            System.out.println( "average fire " + (totalfire / loop) + " ms" );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * 
     *
     */
    public void test2KRuleFire() {
        try {
            int factCount = 50000;
            String file = "2000_rules_1condition.rule";
            int loop = 5;
            long totalload = 0;
            long totalassert = 0;
            long totalfire = 0;
            for ( int c = 0; c < loop; c++ ) {
                long loadStart = System.currentTimeMillis();
                RuleBase ruleBase = readRule( file );
                long loadEnd = System.currentTimeMillis();
                long loadet = loadEnd - loadStart;
                totalload += loadet;
                System.out.println( "time to load " + file + " " + loadet + "ms" );
                WorkingMemory workingMemory = ruleBase.newWorkingMemory();
                ArrayList objects = new ArrayList();
                // create the objects
                for ( int idx = 0; idx < factCount; idx++ ) {
                    Account acc = new Account();
                    acc.setAccountId( "acc" + idx );
                    acc.setTitle( "mr" );
                    acc.setStatus( "standard" );
                    objects.add( acc );
                }
                Iterator itr = objects.iterator();
                long assertStart = System.currentTimeMillis();
                while ( itr.hasNext() ) {
                    workingMemory.assertObject( itr.next() );
                }
                long assertEnd = System.currentTimeMillis();
                long assertet = assertEnd - assertStart;
                totalassert += assertet;
                System.out.println( "time to assert " + assertet + " ms" );
                long fireStart = System.currentTimeMillis();
                workingMemory.fireAllRules();
                long fireEnd = System.currentTimeMillis();
                long fireet = fireEnd - fireStart;
                totalfire += fireet;
                System.out.println( "time to fireAllRules " + fireet + " ms" );
                workingMemory.dispose();
            }
            System.out.println( file );
            System.out.println( "number of objects asserted " + factCount );
            System.out.println( "average load " + (totalload / loop) + " ms" );
            System.out.println( "average assert " + (totalassert / loop) + " ms" );
            System.out.println( "average fire " + (totalfire / loop) + " ms" );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void test2KRuleFire2Conditions() {
        try {
            int factCount = 50000;
            String file = "2000_rules_2condition.rule";
            int loop = 5;
            long totalload = 0;
            long totalassert = 0;
            long totalfire = 0;
            for ( int c = 0; c < loop; c++ ) {
                long loadStart = System.currentTimeMillis();
                RuleBase ruleBase = readRule( file );
                long loadEnd = System.currentTimeMillis();
                long loadet = loadEnd - loadStart;
                totalload += loadet;
                System.out.println( "time to load " + file + " " + loadet + "ms" );
                WorkingMemory workingMemory = ruleBase.newWorkingMemory();
                ArrayList objects = new ArrayList();
                // create the objects
                for ( int idx = 0; idx < factCount; idx++ ) {
                    Account acc = new Account();
                    acc.setAccountId( "acc" + idx );
                    acc.setTitle( "mr" );
                    acc.setStatus( "standard" );
                    objects.add( acc );
                }
                Iterator itr = objects.iterator();
                long assertStart = System.currentTimeMillis();
                while ( itr.hasNext() ) {
                    workingMemory.assertObject( itr.next() );
                }
                long assertEnd = System.currentTimeMillis();
                long assertet = assertEnd - assertStart;
                totalassert += assertet;
                System.out.println( "time to assert " + assertet + " ms" );
                long fireStart = System.currentTimeMillis();
                workingMemory.fireAllRules();
                long fireEnd = System.currentTimeMillis();
                long fireet = fireEnd - fireStart;
                totalfire += fireet;
                System.out.println( "time to fireAllRules " + fireet + " ms" );
                workingMemory.dispose();
            }
            System.out.println( file );
            System.out.println( "number of objects asserted " + factCount );
            System.out.println( "average load " + (totalload / loop) + " ms" );
            System.out.println( "average assert " + (totalassert / loop) + " ms" );
            System.out.println( "average fire " + (totalfire / loop) + " ms" );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public void test2KRuleFire3Conditions() {
        try {
            int factCount = 50000;
            String file = "2000_rules_3condition.rule";
            int loop = 5;
            long totalload = 0;
            long totalassert = 0;
            long totalfire = 0;
            for ( int c = 0; c < loop; c++ ) {
                long loadStart = System.currentTimeMillis();
                RuleBase ruleBase = readRule( file );
                long loadEnd = System.currentTimeMillis();
                long loadet = loadEnd - loadStart;
                totalload += loadet;
                System.out.println( "time to load " + file + " " + loadet + "ms" );
                WorkingMemory workingMemory = ruleBase.newWorkingMemory();
                ArrayList objects = new ArrayList();
                // create the objects
                for ( int idx = 0; idx < factCount; idx++ ) {
                    Account acc = new Account();
                    acc.setAccountId( "acc" + idx );
                    acc.setTitle( "mr" );
                    acc.setStatus( "standard" );
                    objects.add( acc );
                }
                Iterator itr = objects.iterator();
                long assertStart = System.currentTimeMillis();
                while ( itr.hasNext() ) {
                    workingMemory.assertObject( itr.next() );
                }
                long assertEnd = System.currentTimeMillis();
                long assertet = assertEnd - assertStart;
                totalassert += assertet;
                System.out.println( "time to assert " + assertet + " ms" );
                long fireStart = System.currentTimeMillis();
                workingMemory.fireAllRules();
                long fireEnd = System.currentTimeMillis();
                long fireet = fireEnd - fireStart;
                totalfire += fireet;
                System.out.println( "time to fireAllRules " + fireet + " ms" );
                workingMemory.dispose();
            }
            System.out.println( file );
            System.out.println( "number of objects asserted " + factCount );
            System.out.println( "average load " + (totalload / loop) + " ms" );
            System.out.println( "average assert " + (totalassert / loop) + " ms" );
            System.out.println( "average fire " + (totalfire / loop) + " ms" );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
