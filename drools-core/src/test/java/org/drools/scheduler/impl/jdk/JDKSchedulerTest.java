package org.drools.scheduler.impl.jdk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import org.drools.scheduler.Job;
import org.drools.scheduler.JobContext;
import org.drools.scheduler.JobHandle;
import org.drools.scheduler.Scheduler;
import org.drools.scheduler.SchedulerFactory;
import org.drools.scheduler.Trigger;

import junit.framework.TestCase;

public class JDKSchedulerTest extends TestCase {
    
    public void test1() throws Exception {
        Scheduler scheduler = SchedulerFactory.getScheduler(); 
        Trigger trigger = new DelayedTrigger( 100 );
        HelloWorldJobContext ctx = new HelloWorldJobContext( "hello world", scheduler);
        scheduler.scheduleJob( new HelloWorldJob(), ctx,  trigger);        
        Thread.sleep( 500 );
        assertEquals( 1, ctx.getList().size() ); 
    }    
    
    public void test2() throws Exception {
        Scheduler scheduler = SchedulerFactory.getScheduler(); 
        Trigger trigger = new DelayedTrigger(  new long[] { 100, 100, 100} );
        HelloWorldJobContext ctx = new HelloWorldJobContext( "hello world", scheduler);
        scheduler.scheduleJob( new HelloWorldJob(), ctx,  trigger);        
        Thread.sleep( 500 );
        
        assertEquals( 3, ctx.getList().size() );
    }    
        
    
	public void test3() throws Exception {
        Scheduler scheduler = SchedulerFactory.getScheduler();
		Trigger trigger = new DelayedTrigger( new long[] { 100, 100, 100, 100, 100 } );
		HelloWorldJobContext ctx = new HelloWorldJobContext( "hello world", scheduler);
		ctx.setLimit( 3 );
		scheduler.scheduleJob( new HelloWorldJob(), ctx,  trigger);		
		Thread.sleep( 1000 );
		
		assertEquals( 4, ctx.getList().size() );
	}
	
	public static class HelloWorldJob implements Job {
        public void execute(JobContext c) {
            HelloWorldJobContext ctx = (HelloWorldJobContext) c;
            int counter = ctx.increaseCounter();
            if ( counter > 3 ) {
                ctx.scheduler.removeJob( ctx.getJobHandle() );
            }
            ctx.getList().add( ((HelloWorldJobContext)ctx).getMessage() + " : " + counter);
        }	    
	}
	
	public static class HelloWorldJobContext implements JobContext {
	    private String message;
	    private  Scheduler scheduler;
	    private JobHandle jobHandle;
	    
	    private List list;
	    
	    private int counter;	    
	    private int limit;
	    
	    public HelloWorldJobContext(String message, Scheduler scheduler) {
	        this.message = message;
	        this.scheduler = scheduler;
	        this.list = new ArrayList();
	    }
	    
	    public String getMessage() {
	        return this.message;
	    }
	    
	    public int increaseCounter() {
	        return this.counter++;
	    }

        public JobHandle getJobHandle() {
            return this.jobHandle;
        }

        public void setJobHandle(JobHandle jobHandle) {
            this.jobHandle = jobHandle;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public List getList() {
            return list;
        }
	    
	    
	}
	
	public static class DelayedTrigger implements Trigger {
	    public Stack<Long> stack;
	    
	    public DelayedTrigger(long delay) {
	        this( new long[] { delay } );
	    }
	    
        public DelayedTrigger(long[] delay) {
            this.stack = new Stack<Long>();
            for( int i = delay.length-1; i >= 0; i-- ) {
                this.stack.push( delay[i] );
            }
        }	    

        public Date getNextFireTime() {
            if ( !this.stack.isEmpty() ) {
                return new Date( this.stack.pop() );    
            } else {
                return null;
            }
        }
	    
	}
}
