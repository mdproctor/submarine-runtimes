package org.drools.process.command.impl;

import java.util.Collection;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.command.Command;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.impl.StatefulKnowledgeSessionImpl;
import org.drools.process.command.AbortWorkItemCommand;
import org.drools.process.command.AddEventListenerCommand;
import org.drools.process.command.AgendaGroupSetFocusCommand;
import org.drools.process.command.ClearActivationGroupCommand;
import org.drools.process.command.ClearAgendaCommand;
import org.drools.process.command.ClearAgendaGroupCommand;
import org.drools.process.command.ClearRuleFlowGroupCommand;
import org.drools.process.command.CommandService;
import org.drools.process.command.CompleteWorkItemCommand;
import org.drools.process.command.DisposeCommand;
import org.drools.process.command.ExecuteCommand;
import org.drools.process.command.FireAllRulesCommand;
import org.drools.process.command.FireUntilHaltCommand;
import org.drools.process.command.GetAgendaEventListenersCommand;
import org.drools.process.command.GetEnvironmentCommand;
import org.drools.process.command.GetFactHandleCommand;
import org.drools.process.command.GetFactHandlesCommand;
import org.drools.process.command.GetGlobalCommand;
import org.drools.process.command.GetGlobalsCommand;
import org.drools.process.command.GetKnowledgeBaseCommand;
import org.drools.process.command.GetObjectCommand;
import org.drools.process.command.GetObjectsCommand;
import org.drools.process.command.GetProcessEventListenersCommand;
import org.drools.process.command.GetProcessInstanceCommand;
import org.drools.process.command.GetProcessInstancesCommand;
import org.drools.process.command.GetSessionClockCommand;
import org.drools.process.command.GetWorkingMemoryEntryPointCommand;
import org.drools.process.command.GetWorkingMemoryEntryPointsCommand;
import org.drools.process.command.GetWorkingMemoryEventListenersCommand;
import org.drools.process.command.HaltCommand;
import org.drools.process.command.InsertObjectCommand;
import org.drools.process.command.QueryCommand;
import org.drools.process.command.RegisterExitPointCommand;
import org.drools.process.command.RegisterWorkItemHandlerCommand;
import org.drools.process.command.RemoveEventListenerCommand;
import org.drools.process.command.RetractCommand;
import org.drools.process.command.SetGlobalCommand;
import org.drools.process.command.SetProcessInstanceStateCommand;
import org.drools.process.command.SignalEventCommand;
import org.drools.process.command.StartProcessCommand;
import org.drools.process.command.UnregisterExitPointCommand;
import org.drools.process.command.UpdateCommand;
import org.drools.runtime.Environment;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.ExitPoint;
import org.drools.runtime.Globals;
import org.drools.runtime.ObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.drools.runtime.rule.ActivationGroup;
import org.drools.runtime.rule.Agenda;
import org.drools.runtime.rule.AgendaFilter;
import org.drools.runtime.rule.AgendaGroup;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.RuleFlowGroup;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.drools.time.SessionClock;

public class CommandBasedStatefulKnowledgeSession
    implements
    StatefulKnowledgeSession {

    private CommandService            commandService;
    private transient WorkItemManager workItemManager;
    private transient Agenda          agenda;

    public CommandBasedStatefulKnowledgeSession(CommandService commandService) {
        this.commandService = commandService;
    }

    public int getId() {
        return ((StatefulKnowledgeSessionImpl) ((KnowledgeCommandContext) this.commandService.getContext()).getStatefulKnowledgesession()).getId();
    }

    public ProcessInstance getProcessInstance(long id) {
        GetProcessInstanceCommand command = new GetProcessInstanceCommand();
        command.setProcessInstanceId( id );
        return commandService.execute( command );
    }

    public void abortProcessInstance(long id) {
        SetProcessInstanceStateCommand command = new SetProcessInstanceStateCommand();
        command.setProcessInstanceId( id );
        command.setState( ProcessInstance.STATE_ABORTED );
        commandService.execute( command );
    }

    public CommandService getCommandService() {
        return commandService;
    }

    public Collection<ProcessInstance> getProcessInstances() {
        return this.commandService.execute( new GetProcessInstancesCommand() );
    }

    public WorkItemManager getWorkItemManager() {
        if ( workItemManager == null ) {
            workItemManager = new WorkItemManager() {
                public void completeWorkItem(long id,
                                             Map<String, Object> results) {
                    CompleteWorkItemCommand command = new CompleteWorkItemCommand();
                    command.setWorkItemId( id );
                    command.setResults( results );
                    commandService.execute( command );
                }

                public void abortWorkItem(long id) {
                    AbortWorkItemCommand command = new AbortWorkItemCommand();
                    command.setWorkItemId( id );
                    commandService.execute( command );
                }

                public void registerWorkItemHandler(String workItemName,
                                                    WorkItemHandler handler) {
                    RegisterWorkItemHandlerCommand command = new RegisterWorkItemHandlerCommand();
                    command.setWorkItemName( workItemName );
                    command.setHandler( handler );
                    commandService.execute( command );
                }
            };
        }
        return workItemManager;
    }

    public void signalEvent(String type,
                            Object event) {
        SignalEventCommand command = new SignalEventCommand( type,
                                                             event );
        commandService.execute( command );
    }

    public ProcessInstance startProcess(String processId) {
        return startProcess( processId,
                             null );
    }

    public ProcessInstance startProcess(String processId,
                                        Map<String, Object> parameters) {
        StartProcessCommand command = new StartProcessCommand();
        command.setProcessId( processId );
        command.setParameters( parameters );
        return commandService.execute( command );
    }

    public void dispose() {
        commandService.execute( new DisposeCommand() );
    }

    public int fireAllRules() {
        return this.commandService.execute( new FireAllRulesCommand() );
    }

    public int fireAllRules(int max) {
        return this.commandService.execute( new FireAllRulesCommand( max ) );
    }

    public int fireAllRules(AgendaFilter agendaFilter) {
        return this.commandService.execute( new FireAllRulesCommand( agendaFilter ) );
    }

    public void fireUntilHalt() {
        this.commandService.execute( new FireUntilHaltCommand() );
    }

    public void fireUntilHalt(AgendaFilter agendaFilter) {
        this.commandService.execute( new FireUntilHaltCommand( agendaFilter ) );
    }

    public KnowledgeBase getKnowledgeBase() {
        return this.commandService.execute( new GetKnowledgeBaseCommand() );
    }

    public void registerExitPoint(String name,
                                  ExitPoint exitPoint) {
        this.commandService.execute( new RegisterExitPointCommand( name,
                                                                   exitPoint ) );
    }

    public void unregisterExitPoint(String name) {
        this.commandService.execute( new UnregisterExitPointCommand( name ) );

    }

    public Agenda getAgenda() {
        if ( agenda == null ) {
            agenda = new Agenda() {
                public void clear() {
                    ClearAgendaCommand command = new ClearAgendaCommand();
                    commandService.execute( command );
                }

                public ActivationGroup getActivationGroup(final String name) {
                    return new ActivationGroup() {
                        public void clear() {
                            ClearActivationGroupCommand command = new ClearActivationGroupCommand();
                            command.setName( name );
                            commandService.execute( command );
                        }

                        public String getName() {
                            return name;
                        }
                    };
                }

                public AgendaGroup getAgendaGroup(final String name) {
                    return new AgendaGroup() {
                        public void clear() {
                            ClearAgendaGroupCommand command = new ClearAgendaGroupCommand();
                            command.setName( name );
                            commandService.execute( command );
                        }

                        public String getName() {
                            return name;
                        }

                        public void setFocus() {
                            AgendaGroupSetFocusCommand command = new AgendaGroupSetFocusCommand();
                            command.setName( name );
                            commandService.execute( command );
                        }
                    };
                }

                public RuleFlowGroup getRuleFlowGroup(final String name) {
                    return new RuleFlowGroup() {
                        public void clear() {
                            ClearRuleFlowGroupCommand command = new ClearRuleFlowGroupCommand();
                            command.setName( name );
                            commandService.execute( command );
                        }

                        public String getName() {
                            return name;
                        }
                    };
                }
            };
        }
        return agenda;
    }

    public FactHandle getFactHandle(Object object) {
        return this.commandService.execute( new GetFactHandleCommand( object ) );
    }

    public <T extends org.drools.runtime.rule.FactHandle> Collection<T> getFactHandles() {
        return (Collection<T>) this.commandService.execute( new GetFactHandlesCommand() );

    }

    public <T extends org.drools.runtime.rule.FactHandle> Collection<T> getFactHandles(ObjectFilter filter) {
        return (Collection<T>) this.commandService.execute( new GetFactHandlesCommand( filter ) );
    }

    public Collection<Object> getObjects() {
        return getObjects( null );
    }

    public Collection<Object> getObjects(ObjectFilter filter) {
        Collection result = commandService.execute( new GetObjectsCommand( filter ) );
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T extends SessionClock> T getSessionClock() {
        return (T) this.commandService.execute( new GetSessionClockCommand() );
    }

    public WorkingMemoryEntryPoint getWorkingMemoryEntryPoint(String name) {
        return this.commandService.execute( new GetWorkingMemoryEntryPointCommand( name ) );
    }

    public Collection< ? extends WorkingMemoryEntryPoint> getWorkingMemoryEntryPoints() {
        return this.commandService.execute( new GetWorkingMemoryEntryPointsCommand() );
    }

    public void halt() {
        this.commandService.execute( new HaltCommand() );
    }

    public FactHandle insert(Object object) {
        return commandService.execute( new InsertObjectCommand( object ) );
    }

    public void retract(FactHandle handle) {
        commandService.execute( new RetractCommand( handle ) );
    }

    public void update(FactHandle handle,
                       Object object) {
        commandService.execute( new UpdateCommand( handle,
                                                   object ) );
    }

    public void addEventListener(WorkingMemoryEventListener listener) {
        commandService.execute( new AddEventListenerCommand( listener ) );
    }

    public void addEventListener(AgendaEventListener listener) {
        commandService.execute( new AddEventListenerCommand( listener ) );
    }

    public Collection<AgendaEventListener> getAgendaEventListeners() {
        return commandService.execute( new GetAgendaEventListenersCommand() );
    }

    public Collection<WorkingMemoryEventListener> getWorkingMemoryEventListeners() {
        return commandService.execute( new GetWorkingMemoryEventListenersCommand() );
    }

    public void removeEventListener(WorkingMemoryEventListener listener) {
        commandService.execute( new RemoveEventListenerCommand( listener ) );
    }

    public void removeEventListener(AgendaEventListener listener) {
        commandService.execute( new RemoveEventListenerCommand( listener ) );
    }

    public void addEventListener(ProcessEventListener listener) {
        commandService.execute( new AddEventListenerCommand( listener ) );
    }

    public Collection<ProcessEventListener> getProcessEventListeners() {
        return commandService.execute( new GetProcessEventListenersCommand() );
    }

    public void removeEventListener(ProcessEventListener listener) {
        commandService.execute( new RemoveEventListenerCommand( listener ) );
    }

    public Object getGlobal(String identifier) {
        return commandService.execute( new GetGlobalCommand( identifier ) );
    }

    public void setGlobal(String identifier,
                          Object object) {
        this.commandService.execute( new SetGlobalCommand( identifier,
                                                           object ) );
    }

    public Globals getGlobals() {
        return commandService.execute( new GetGlobalsCommand() );
    }

    public Object getObject(FactHandle factHandle) {
        return commandService.execute( new GetObjectCommand( factHandle ) );
    }

    public Environment getEnvironment() {
        return commandService.execute( new GetEnvironmentCommand() );
    }

    public ExecutionResults execute(Command command) {
        return this.commandService.execute( new ExecuteCommand( command ) );
    }

    public QueryResults getQueryResults(String query) {
        QueryCommand cmd = new QueryCommand( null,
                                             query,
                                             null );
        return this.commandService.execute( cmd );
    }

    public QueryResults getQueryResults(String query,
                                        Object[] arguments) {
        QueryCommand cmd = new QueryCommand( null,
                                             query,
                                             arguments );
        return this.commandService.execute( cmd );
    }

}