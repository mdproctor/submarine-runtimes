package org.drools.runtime.pipeline.impl;

import java.util.Collection;
import java.util.List;

import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.pipeline.KnowledgeRuntimeCommand;
import org.drools.runtime.pipeline.PipelineContext;

public class ExecutorStage extends BaseEmitter
    implements
    KnowledgeRuntimeCommand {
    private ExecutionResults result = null;
    public void receive(Object object,
                        PipelineContext context) {
        BasePipelineContext kContext = (BasePipelineContext) context;
        this.result = execute(object, kContext);
        emit( result,
              kContext );
    }
    public ExecutionResults execute(Object object, PipelineContext kContext){
        
        if ( object instanceof Collection ) {
            object = CommandFactory.newBatchExecution( (List<Command>) object );
        }
         
        return kContext.getCommandExecutor().execute( (Command) object );
    }

}
