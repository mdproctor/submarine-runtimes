package com.sample;

import java.io.InputStreamReader;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.audit.WorkingMemoryFileLogger;
import org.drools.compiler.PackageBuilder;

public class FibonacciExample {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

        PackageBuilder builder = new PackageBuilder();
        builder.addPackageFromDrl( new InputStreamReader( FibonacciExample.class.getResourceAsStream( "/Fibonacci.drl" ) ) );

        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage( builder.getPackage() );

        WorkingMemory workingMemory = ruleBase.newWorkingMemory();

        WorkingMemoryFileLogger logger = new WorkingMemoryFileLogger( workingMemory );
        logger.setFileName( "log/fibonacci" );

        // By setting dynamic to TRUE, Drools will use JavaBean
        // PropertyChangeListeners so you don't have to call modifyObject().
        boolean dynamic = false;

        workingMemory.assertObject( new Fibonacci( 50 ),
                                    dynamic );

        workingMemory.fireAllRules();

        logger.writeToDisk();
    }

    public static class Fibonacci {
        private int  sequence;

        private long value;

        public Fibonacci(int sequence) {
            this.sequence = sequence;
            this.value = -1;
        }

        public int getSequence() {
            return this.sequence;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public long getValue() {
            return this.value;
        }

        public String toString() {
            return "Fibonacci(" + this.sequence + "/" + this.value + ")";
        }
    }

}
