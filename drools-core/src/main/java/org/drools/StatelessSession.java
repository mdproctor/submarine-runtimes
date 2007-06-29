package org.drools;

import java.util.List;

import org.drools.event.AgendaEventListener;
import org.drools.spi.AgendaFilter;
import org.drools.spi.GlobalResolver;

/**
 * This represents a working memory session where state is not kept between
 * invocations.
 * This is typically used for "decision services" where the rules are
 * provided all the data in one hit, and a conclusion reached by the engine.
 * (there is no accumulation of facts/knowledge - each invocation is on a fresh session).
 * 
 * Care should be used when using the async versions of the methods, consult the javadoc for 
 * the specific information.
 */
public interface StatelessSession {
    /**
     * Returns all event listeners.
     * 
     * @return listeners The listeners.
     */
    public List getWorkingMemoryEventListeners();

    /**
     * Add an event listener.
     * 
     * @param listener
     *            The listener to add.
     */
    public void addEventListener(AgendaEventListener listener);

    /**
     * Remove an event listener.
     * 
     * @param listener
     *            The listener to remove.
     */
    public void removeEventListener(AgendaEventListener listener);    
    
    
    void setAgendaFilter(AgendaFilter agendaFilter);
    
    /**
     * Delegate used to resolve any global names not found in the global map.
     * @param globalResolver
     */
    void setGlobalResolver(GlobalResolver globalResolver);
    
    void setGlobal(String identifer, Object value);        
    
    /**
     * Insert a single fact, an fire the rules, returning when finished.
     */
    void execute(Object object);

    /**
     * Insert an array of facts, an fire the rules, returning when finished.
     * This will assert the list of facts as SEPARATE facts to the engine
     * (NOT an array).
     */    
    void execute(Object[] list);

    /**
     * Insert a List of facts, an fire the rules, returning when finished.
     * This will assert the list of facts as SEPARATE facts to the engine
     * (NOT as a List).
     */        
    void execute(List list);    
    
    /**
     * This will assert the object in the background. This is
     * "send and forget" execution.
     */
    void asyncExecute(Object object);

    /**
     * This will assert the object array (as SEPARATE facts) in the background. This is
     * "send and forget" execution.
     */
    void asyncExecute(Object[] list);

    /**
     * This will assert the object List (as SEPARATE facts) in the background. This is
     * "send and forget" execution.
     */    
    void asyncExecute(List list);      
    
    
    /**
     * Similar to the normal execute method, but this will return
     * "results". 
     */
    StatelessSessionResult executeWithResults(Object object);

    /**
     * Similar to the normal execute method, but this will return
     * "results". 
     */    
    StatelessSessionResult executeWithResults(Object[] list);

    /**
     * Similar to the normal execute method, but this will return
     * "results". 
     */   
    StatelessSessionResult executeWithResults(List list);
}
