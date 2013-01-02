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

package org.drools.core.time.impl;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.drools.core.common.DroolsObjectInputStream;
import org.drools.core.common.InternalWorkingMemory;
import org.drools.core.time.AcceptsTimerJobFactoryManager;
import org.drools.core.time.InternalSchedulerService;
import org.drools.core.time.Job;
import org.drools.core.time.JobContext;
import org.drools.core.time.JobHandle;
import org.drools.core.time.SessionPseudoClock;
import org.drools.core.time.TimerService;
import org.drools.core.time.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A PseudoClockScheduler is a scheduler based on a user controlled clock 
 * that allows the user to explicitly control current time.
 */
public class PseudoClockScheduler
    implements
    TimerService,
    SessionPseudoClock,
    Externalizable,
    InternalSchedulerService,
    AcceptsTimerJobFactoryManager {
    
    private Logger logger = LoggerFactory.getLogger( PseudoClockScheduler.class ); 

    private AtomicLong                      timer;
    private PriorityQueue<Callable<Void>>   queue;
    private transient InternalWorkingMemory session;

    private TimerJobFactoryManager          jobFactoryManager = DefaultTimerJobFactoryManager.instance;

    private AtomicLong                      idCounter         = new AtomicLong();

    public PseudoClockScheduler() {
        this( null );
    }

    public PseudoClockScheduler(InternalWorkingMemory session) {
        this.timer = new AtomicLong(0);
        this.queue = new PriorityQueue<Callable<Void>>();
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        timer = new AtomicLong( in.readLong() );
        PriorityQueue<Callable<Void>> tmp = (PriorityQueue<Callable<Void>>) in.readObject();
        if ( tmp != null ) {
            queue = tmp;
        }
        session = ((DroolsObjectInputStream) in).getWorkingMemory();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong( timer.get() );
        // this is a work around to a bug in the object stream code, where it raises exceptions
        // when trying to de-serialize an empty priority queue.
        out.writeObject( queue.isEmpty() ? null : queue );
    }

    public void setTimerJobFactoryManager(TimerJobFactoryManager timerJobFactoryManager) {
        this.jobFactoryManager = timerJobFactoryManager;
    }
    
    public TimerJobFactoryManager getTimerJobFactoryManager() {
        return this.jobFactoryManager;
    }    

    /**
     * @inheritDoc
     * 
     * @see org.kie.temporal.SessionClock#getCurrentTime()
     */
    public long getCurrentTime() {
        return this.timer.get();
    }

    /**
     * @inheritDoc
     *
     * @see org.kie.api.time.TimerService#scheduleJob(org.kie.api.time.Job, org.kie.api.time.JobContext, org.kie.api.time.Trigger)
     */
    public JobHandle scheduleJob(Job job,
                                 JobContext ctx,
                                 Trigger trigger) {

        Date date = trigger.hasNextFireTime();

        if ( date != null ) {
            DefaultJobHandle jobHandle = new DefaultJobHandle( idCounter.getAndIncrement() );
            TimerJobInstance jobInstance = jobFactoryManager.createTimerJobInstance( job,
                                                                                   ctx,
                                                                                   trigger,
                                                                                   jobHandle,
                                                                                   this );
            jobHandle.setTimerJobInstance( (TimerJobInstance) jobInstance );
            internalSchedule( (TimerJobInstance) jobInstance );

            return jobHandle;
        }

        return null;
    }

    public void internalSchedule(TimerJobInstance timerJobInstance) {
        jobFactoryManager.addTimerJobInstance( timerJobInstance );
        synchronized(queue) {
            queue.add( ( Callable<Void> ) timerJobInstance );
        }
    }

    /**
     * @inheritDoc
     *
     * @see org.kie.api.time.TimerService#removeJob(org.kie.api.time.JobHandle)
     */
    public boolean removeJob(JobHandle jobHandle) {
        jobHandle.setCancel( true );
        jobFactoryManager.removeTimerJobInstance( ((DefaultJobHandle) jobHandle).getTimerJobInstance() );
        synchronized( queue ) {
            return this.queue.remove( (Callable<Void>) ((DefaultJobHandle) jobHandle).getTimerJobInstance() );
        }
    }

    /**
     * @inheritDoc
     */
    public long advanceTime(long amount,
                            TimeUnit unit) {
        return this.runCallBacksAndIncreaseTimer( unit.toMillis( amount ) );
    }

    public void setStartupTime(long i) {
        this.timer.set( i );
    }

    /**
     * @return the session
     */
    public synchronized InternalWorkingMemory getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public synchronized void setSession(InternalWorkingMemory session) {
        this.session = session;
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        // nothing to do
    }

    @SuppressWarnings("unchecked")
    private synchronized long runCallBacksAndIncreaseTimer( long increase ) {
        long endTime = this.timer.get() + increase;
        TimerJobInstance item = (TimerJobInstance) queue.peek();
        long fireTime;
        while ( item != null && ((item.getTrigger().hasNextFireTime() != null && ( ( fireTime = item.getTrigger().hasNextFireTime().getTime()) <= endTime ) ) )  ) {
            // remove the head
            synchronized( queue ) {
                queue.remove(item);
            }

            if ( item.getJobHandle().isCancel() ) {
                // do not call it, do not reschedule it
                continue;
            }
            
            try {
                // set the clock back to the trigger's fire time
                this.timer.getAndSet( fireTime );
                // execute the call
                ((Callable<Void>) item).call();
            } catch ( Exception e ) {
                logger.error( "Exception running callbacks: ", e );
            }
            // get next head
            synchronized( queue ) {
                item = (TimerJobInstance) queue.peek();
            }
        }
        this.timer.set( endTime );
        return this.timer.get(); 
    }

    public long getTimeToNextJob() {
        synchronized( queue ) {
            TimerJobInstance item = (TimerJobInstance) queue.peek();
            return (item != null) ? item.getTrigger().hasNextFireTime().getTime() - this.timer.get() : -1;
        }
    }

    public Collection<TimerJobInstance> getTimerJobInstances(int id) {
        return jobFactoryManager.getTimerJobInstances();
    }

}
