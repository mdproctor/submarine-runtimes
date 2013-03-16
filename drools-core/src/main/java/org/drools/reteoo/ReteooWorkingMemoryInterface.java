package org.drools.reteoo;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;

import org.drools.core.common.EndOperationListener;
import org.drools.core.common.EventSupport;
import org.drools.core.common.InternalWorkingMemoryActions;
import org.drools.core.common.WorkingMemoryAction;
import org.drools.core.spi.AgendaFilter;
import org.kie.event.process.ProcessEventManager;
import org.kie.runtime.Channel;
import org.kie.runtime.process.ProcessInstance;
import org.kie.runtime.rule.LiveQuery;
import org.kie.runtime.rule.ViewChangedEventListener;
import org.kie.runtime.rule.SessionEntryPoint;

/**
 * This is an interface for ReteooWorkingMemory implementations
 * 
 * @author etirelli
 */
public interface ReteooWorkingMemoryInterface extends InternalWorkingMemoryActions,
                                             EventSupport,
                                             ProcessEventManager {

    Collection<? extends SessionEntryPoint> getWorkingMemoryEntryPoints();

    void fireUntilHalt();

    void fireUntilHalt( AgendaFilter agendaFilterWrapper );

    ProcessInstance createProcessInstance( String processId, Map<String, Object> parameters );

    ProcessInstance startProcessInstance( long processInstanceId );

    void registerChannel( String name, Channel channel );

    void unregisterChannel( String name );

    Queue<WorkingMemoryAction> getActionQueue();

    LiveQuery openLiveQuery( String query, Object[] arguments, ViewChangedEventListener listener );

    void setEndOperationListener( EndOperationListener listener );

    long getLastIdleTimestamp();

}
