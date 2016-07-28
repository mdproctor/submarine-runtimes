/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.core.concurrent;

import org.kie.api.concurrent.KieExecutors;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorProviderImpl implements KieExecutors {

    public static final String EXECUTOR_SERVICE_PROPERTY = "drools.executorService";
    public static final String DEFAULT_JEE_EXECUTOR_SERVICE_NAME = "java:comp/env/concurrent/ThreadPool";

    public static final String THREAD_FACTORY_PROPERTY = "drools.threadFactory";

    private static class ExecutorHolder {
        private static final java.util.concurrent.ExecutorService executor;

        static {
            java.util.concurrent.ExecutorService newExecutor = null;

            String executorServiceName = System.getProperty( EXECUTOR_SERVICE_PROPERTY );
            try {
                InitialContext ctx = new InitialContext();
                newExecutor = (java.util.concurrent.ExecutorService) ctx.lookup(executorServiceName != null ? executorServiceName : DEFAULT_JEE_EXECUTOR_SERVICE_NAME);
            } catch (NamingException e) {
                // not in a JEE container, throws an Exception only if user explicitly defined
                // a JNDI name for the ExecutorService, otherwise ignore
                if (executorServiceName != null) {
                    throw new RuntimeException( "Unable to find an ExecutorService with JNDI name: " + executorServiceName, e );
                }
            }

            if (newExecutor == null) {
                String threadFactoryClass = System.getProperty( THREAD_FACTORY_PROPERTY );
                ThreadFactory threadFactory;

                if ( threadFactoryClass == null ) {
                    threadFactory = new DaemonThreadFactory();
                } else {
                    try {
                        threadFactory = (ThreadFactory) Class.forName( threadFactoryClass ).newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException( "Unable to instance a ThreadFactory of class " + threadFactoryClass, e );
                    }
                }

                newExecutor = new ThreadPoolExecutor( Pool.SIZE, Pool.SIZE,
                                                      60L, TimeUnit.SECONDS,
                                                      new LinkedBlockingQueue<Runnable>(),
                                                      threadFactory );
            }

            executor = newExecutor;
        }
    }

    public Executor getExecutor() {
        return ExecutorHolder.executor;
    }

    public Executor newSingleThreadExecutor() {
        return Executors.newSingleThreadExecutor(new DaemonThreadFactory());
    }

    public <T> CompletionService<T> getCompletionService() {
        return new ExecutorCompletionService<T>(getExecutor());
    }

    private static class DaemonThreadFactory implements ThreadFactory {
        private static final AtomicInteger threadCount = new AtomicInteger();

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("drools-worker-" + threadCount.incrementAndGet());
            t.setDaemon(true);
            return t;
        }
    }
}
