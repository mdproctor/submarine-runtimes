package org.drools.compiler;

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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.drools.lang.descr.ActionDescr;
import org.drools.lang.descr.ProcessDescr;
import org.drools.process.builder.ProcessNodeBuilder;
import org.drools.process.builder.ProcessNodeBuilderRegistry;
import org.drools.process.core.Context;
import org.drools.process.core.ContextContainer;
import org.drools.process.core.Process;
import org.drools.process.core.context.exception.ActionExceptionHandler;
import org.drools.process.core.context.exception.ExceptionHandler;
import org.drools.process.core.context.exception.ExceptionScope;
import org.drools.process.core.validation.ProcessValidationError;
import org.drools.process.core.validation.ProcessValidator;
import org.drools.rule.builder.ProcessBuildContext;
import org.drools.ruleflow.core.RuleFlowProcess;
import org.drools.ruleflow.core.validation.RuleFlowProcessValidator;
import org.drools.workflow.core.Connection;
import org.drools.workflow.core.Constraint;
import org.drools.workflow.core.Node;
import org.drools.workflow.core.NodeContainer;
import org.drools.workflow.core.WorkflowProcess;
import org.drools.workflow.core.impl.DroolsConsequenceAction;
import org.drools.workflow.core.impl.WorkflowProcessImpl;
import org.drools.workflow.core.node.MilestoneNode;
import org.drools.workflow.core.node.Split;
import org.drools.xml.XmlProcessReader;

/**
 * A ProcessBuilder can be used to build processes based on XML files
 * containing a process definition.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class ProcessBuilder {

    private PackageBuilder packageBuilder;
    private final List<DroolsError>     errors = new ArrayList<DroolsError>();
    private Map<String, ProcessValidator> processValidators = new HashMap<String, ProcessValidator>();;

    public ProcessBuilder(PackageBuilder packageBuilder) {
        this.packageBuilder = packageBuilder;
        this.processValidators.put(RuleFlowProcess.RULEFLOW_TYPE, RuleFlowProcessValidator.getInstance());
    }

    public List<DroolsError> getErrors() {
        return errors;
    }

    public void buildProcess(final Process process) {
        boolean hasErrors = false;
        ProcessValidator validator = processValidators.get(process.getType());
        if (validator == null) {
            System.out.println("Could not find validator for process " + process.getType() + ".");
            System.out.println("Continuing without validation of the process " + process.getName() + "[" + process.getId() + "]");
        } else {
            ProcessValidationError[] errors = validator.validateProcess( (WorkflowProcess) process );
            if ( errors.length != 0 ) {
                hasErrors = true;
                for ( int i = 0; i < errors.length; i++ ) {
                    this.errors.add( new ParserError( errors[i].toString(),
                                                      -1,
                                                      -1 ) );
                }
            }
        }
        if ( !hasErrors ) {
            // generate and add rule for process
            String rules = generateRules( process );
            try {
                packageBuilder.addPackageFromDrl( new StringReader( rules ) );
            } catch ( IOException e ) {
                // should never occur
                e.printStackTrace( System.err );
            } catch ( DroolsParserException e ) {
                // should never occur
                e.printStackTrace( System.err );
            }
            
            ProcessDescr processDescr = new ProcessDescr();
            processDescr.setName(process.getPackageName());
            PackageRegistry pkgRegistry = this.packageBuilder.getPackageRegistry( this.packageBuilder.getPackage().getName() );
            DialectCompiletimeRegistry dialectRegistry = pkgRegistry.getDialectCompiletimeRegistry();           
            Dialect dialect = dialectRegistry.getDialect( "java" );
            dialect.init(processDescr);

            ProcessBuildContext buildContext = new ProcessBuildContext(
        		this.packageBuilder,
                this.packageBuilder.getPackage(),
                process,
                processDescr,
                dialectRegistry,
                dialect);

            buildContexts( process, buildContext );
            if (process instanceof WorkflowProcess) {
            	buildNodes( (WorkflowProcess) process, buildContext );
            }
            this.packageBuilder.getPackage().addProcess( process );

            pkgRegistry.compileAll();                
            pkgRegistry.getDialectRuntimeRegistry().onBeforeExecute();
        }
    }

    public void buildContexts(ContextContainer contextContainer, ProcessBuildContext buildContext) {
    	List<Context> exceptionScopes = contextContainer.getContexts(ExceptionScope.EXCEPTION_SCOPE);
    	if (exceptionScopes != null) {
    		for (Context context: exceptionScopes) {
    			ExceptionScope exceptionScope = (ExceptionScope) context;
    			for (ExceptionHandler exceptionHandler: exceptionScope.getExceptionHandlers().values()) {
    				if (exceptionHandler instanceof ActionExceptionHandler) {
    					DroolsConsequenceAction action = (DroolsConsequenceAction) 
    						((ActionExceptionHandler) exceptionHandler).getAction();
    					ActionDescr actionDescr = new ActionDescr();
    			        actionDescr.setText( action.getConsequence() );   
    			        Dialect dialect = buildContext.getDialectRegistry().getDialect( action.getDialect() );            
    			        dialect.getActionBuilder().build( buildContext, action, actionDescr );
    				}
    			}
    		}
    	}
    }
    
    public void buildNodes(WorkflowProcess process, ProcessBuildContext context) {
    	ProcessNodeBuilderRegistry nodeBuilderRegistry = packageBuilder.getPackageBuilderConfiguration().getProcessNodeBuilderRegistry();
        processNodes(process.getNodes(), process, context.getProcessDescr(), context, nodeBuilderRegistry);
        if ( !context.getErrors().isEmpty() ) {
            this.errors.addAll( context.getErrors() );
        }
        context.getDialectRegistry().addProcess( context );
    }
    
    private void processNodes(
            Node[] nodes, Process process, ProcessDescr processDescr, 
            ProcessBuildContext context, ProcessNodeBuilderRegistry nodeBuilderRegistry) {
        for ( Node node : nodes ) {
            ProcessNodeBuilder builder = nodeBuilderRegistry.getNodeBuilder( node );
            if ( builder != null ) {
                // only build if there is a registered builder for this node type
                builder.build( process,
                               processDescr,
                               context,
                               node );
            }
            if (node instanceof NodeContainer) {
                processNodes(((NodeContainer) node).getNodes(), process, processDescr, context, nodeBuilderRegistry);
            }
            if (node instanceof ContextContainer) {
            	buildContexts((ContextContainer) node, context); 
            }
        }
    }

    public void addProcessFromFile(final Reader reader) throws Exception {
        PackageBuilderConfiguration configuration = new PackageBuilderConfiguration();
        XmlProcessReader xmlReader = new XmlProcessReader( configuration.getSemanticModules() );
        
        final ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        final ClassLoader newLoader = this.getClass().getClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( newLoader );
            Process process = xmlReader.read(reader);
            buildProcess( process );
        } finally {
            Thread.currentThread().setContextClassLoader( oldLoader );
        }
        reader.close();
    }

    private String generateRules(final Process process) {
        StringBuilder builder = new StringBuilder();

        if ( process instanceof WorkflowProcessImpl ) {
            WorkflowProcessImpl ruleFlow = (WorkflowProcessImpl) process;
            builder.append( "package " + ruleFlow.getPackageName() + "\n" );
            List<String> imports = ruleFlow.getImports();
            if ( imports != null ) {
                for ( String importString: imports ) {
                    builder.append( "import " + importString + ";\n" );
                }
            }
            Map<String, String> globals = ruleFlow.getGlobals();
            if ( globals != null ) {
                for ( Map.Entry<String, String> entry: globals.entrySet()) {
                    builder.append( "global " + entry.getValue() + " " + entry.getKey() + ";\n" );
                }
            }

            Node[] nodes = ruleFlow.getNodes();
            for ( int i = 0; i < nodes.length; i++ ) {
                if ( nodes[i] instanceof Split ) {
                    Split split = (Split) nodes[i];
                    if ( split.getType() == Split.TYPE_XOR || split.getType() == Split.TYPE_OR ) {
                        for ( Iterator iterator = split.getDefaultOutgoingConnections().iterator(); iterator.hasNext(); ) {
                            Connection connection = (Connection) iterator.next();
                            Constraint constraint = split.getConstraint( connection );
                            if ( "rule".equals( constraint.getType() ) ) {
                                builder.append( createSplitRule( process,
                                                                 connection,
                                                                 split.getConstraint( connection ).getConstraint() ) );
                            }
                        }
                    }
                } else if ( nodes[i] instanceof MilestoneNode ) {
                    MilestoneNode milestone = (MilestoneNode) nodes[i];
                    builder.append( createMilestoneRule( process,
                                                         milestone ) );
                }
            }
        }
        return builder.toString();
    }

    private String createSplitRule(Process process,
                                   Connection connection,
                                   String constraint) {
        return "rule \"RuleFlow-Split-" + process.getId() + "-" + connection.getFrom().getId() + "-" + connection.getTo().getId() + "\" \n" + "      ruleflow-group \"DROOLS_SYSTEM\" \n" + "    when \n" + "      " + constraint + "\n" + "    then \n"
               + "end \n\n";
    }

    private String createMilestoneRule(Process process,
                                       MilestoneNode milestone) {
        return "rule \"RuleFlow-Milestone-" + process.getId() + "-" + milestone.getId() + "\" \n" + "      ruleflow-group \"DROOLS_SYSTEM\" \n" + "    when \n" + "      " + milestone.getConstraint() + "\n" + "    then \n" + "end \n\n";
    }
}
