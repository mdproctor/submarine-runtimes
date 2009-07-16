package org.drools.command.impl;

import java.util.List;
import java.util.Map;

import org.drools.command.AbortWorkItemCommand;
import org.drools.command.Command;
import org.drools.command.CommandFactoryProvider;
import org.drools.command.CompleteWorkItemCommand;
import org.drools.command.FireAllRulesCommand;
import org.drools.command.GetGlobalCommand;
import org.drools.command.GetObjectCommand;
import org.drools.command.GetObjectsCommand;
import org.drools.command.InsertElementsCommand;
import org.drools.command.InsertObjectCommand;
import org.drools.command.ModifyCommand;
import org.drools.command.QueryCommand;
import org.drools.command.RetractCommand;
import org.drools.command.SetGlobalCommand;
import org.drools.command.Setter;
import org.drools.command.SignalEventCommand;
import org.drools.command.StartProcessCommand;
import org.drools.command.ModifyCommand.SetterImpl;
import org.drools.runtime.ObjectFilter;
import org.drools.runtime.impl.BatchExecutionImpl;
import org.drools.runtime.rule.FactHandle;

public class CommandFactoryProviderImpl implements CommandFactoryProvider {

	public Command newGetGlobal(String identifier) {
		return new GetGlobalCommand(identifier);
	}

	public Command newGetGlobal(String identifier, String outIdentifier) {
		GetGlobalCommand cmd = new GetGlobalCommand(identifier);
		cmd.setOutIdentifier(outIdentifier);
		return cmd;
	}

	public Command newInsertElements(Iterable objects) {
		return new InsertElementsCommand(objects);
	}

	public Command newInsert(Object object) {
		return new InsertObjectCommand(object);
	}

	public Command newInsert(Object object, String outIdentifier) {
		InsertObjectCommand cmd = new InsertObjectCommand(object);
		cmd.setOutIdentifier(outIdentifier);
		return cmd;
	}
	
    public Command newRetract(FactHandle factHandle) {
        return new RetractCommand( factHandle );
    }
    
    public Setter newSetter(String accessor,
                             String value) {
        return new SetterImpl(accessor, value);
    }    
    
    public Command newModify(FactHandle factHandle,
                             List<Setter> setters) {
        return new ModifyCommand(factHandle, setters);
    }    
	
    public Command newGetObject(FactHandle factHandle) {
        return new GetObjectCommand(factHandle);
    }	

	public Command newGetObjects() {
		return newGetObjects(null);
	}

	public Command newGetObjects(ObjectFilter filter) {
		return new GetObjectsCommand(filter);
	}

	public Command newSetGlobal(String identifier, Object object) {
		return new SetGlobalCommand(identifier, object);
	}

	public Command newSetGlobal(String identifier, Object object, boolean out) {
		SetGlobalCommand cmd = new SetGlobalCommand(identifier, object);
		cmd.setOut(out);
		return cmd;
	}

	public Command newSetGlobal(String identifier, Object object,
			String outIdentifier) {
		SetGlobalCommand cmd = new SetGlobalCommand(identifier, object);
		cmd.setOutIdentifier(outIdentifier);
		return cmd;
	}
	
	public Command newFireAllRules() {
	    return new FireAllRulesCommand();
	}
	
	public Command newFireAllRules(int max) {
	    return new FireAllRulesCommand(max);
	}

	public Command newStartProcess(String processId) {
		StartProcessCommand startProcess = new StartProcessCommand();
		startProcess.setProcessId(processId);
		return startProcess;
	}

	public Command newStartProcess(String processId,
			Map<String, Object> parameters) {
		StartProcessCommand startProcess = new StartProcessCommand();
		startProcess.setProcessId(processId);
		startProcess.setParameters(parameters);
		return startProcess;
	}

    public Command newSignalEvent(String type,
                               Object event) {
        return new SignalEventCommand( type, event );
    }
    
    public Command newSignalEvent(long processInstanceId,
                               String type,
                               Object event) {
        return new SignalEventCommand( processInstanceId, type, event );
    }    
    
    public Command newCompleteWorkItem(long workItemId,
                                       Map<String, Object> results) {
        return new CompleteWorkItemCommand(workItemId, results);
    }    
    
    public Command newAbortWorkItem(long workItemId) {
        return new AbortWorkItemCommand( workItemId);
    }    
	
	public Command newQuery(String identifier, String name) {
		return new QueryCommand(identifier, name, null);
	}

	public Command newQuery(String identifier, String name, Object[] arguments) {
		return new QueryCommand(identifier, name, arguments);
	}

	public Command newBatchExecution(List<? extends Command> commands) {
		return new BatchExecutionImpl(
				(List<GenericCommand>) commands);
	}
}
