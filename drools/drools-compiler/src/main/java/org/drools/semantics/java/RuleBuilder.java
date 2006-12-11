package org.drools.semantics.java;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenStream;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.AngleBracketTemplateLexer;
import org.codehaus.jfdi.interpreter.TypeResolver;
import org.codehaus.jfdi.parser.JFDILexer;
import org.codehaus.jfdi.parser.JFDIParser;
import org.drools.RuntimeDroolsException;
import org.drools.base.ClassFieldExtractorCache;
import org.drools.base.ClassObjectType;
import org.drools.base.DroolsJFDIFactory;
import org.drools.base.FieldFactory;
import org.drools.base.ShadowProxyFactory;
import org.drools.base.ValueType;
import org.drools.base.dataproviders.JFDIDataProvider;
import org.drools.base.evaluators.Operator;
import org.drools.compiler.RuleError;
import org.drools.facttemplates.FactTemplate;
import org.drools.facttemplates.FactTemplateFieldExtractor;
import org.drools.facttemplates.FactTemplateObjectType;
import org.drools.lang.descr.AccessorDescr;
import org.drools.lang.descr.AccumulateDescr;
import org.drools.lang.descr.AndDescr;
import org.drools.lang.descr.AttributeDescr;
import org.drools.lang.descr.BaseDescr;
import org.drools.lang.descr.CollectDescr;
import org.drools.lang.descr.ColumnDescr;
import org.drools.lang.descr.ConditionalElementDescr;
import org.drools.lang.descr.EvalDescr;
import org.drools.lang.descr.ExistsDescr;
import org.drools.lang.descr.FieldBindingDescr;
import org.drools.lang.descr.FieldConstraintDescr;
import org.drools.lang.descr.FromDescr;
import org.drools.lang.descr.LiteralRestrictionDescr;
import org.drools.lang.descr.NotDescr;
import org.drools.lang.descr.OrDescr;
import org.drools.lang.descr.PredicateDescr;
import org.drools.lang.descr.QueryDescr;
import org.drools.lang.descr.RestrictionConnectiveDescr;
import org.drools.lang.descr.RestrictionDescr;
import org.drools.lang.descr.ReturnValueRestrictionDescr;
import org.drools.lang.descr.RuleDescr;
import org.drools.lang.descr.VariableRestrictionDescr;
import org.drools.rule.Accumulate;
import org.drools.rule.AndCompositeRestriction;
import org.drools.rule.Collect;
import org.drools.rule.Column;
import org.drools.rule.Declaration;
import org.drools.rule.EvalCondition;
import org.drools.rule.From;
import org.drools.rule.GroupElement;
import org.drools.rule.GroupElementFactory;
import org.drools.rule.LiteralConstraint;
import org.drools.rule.LiteralRestriction;
import org.drools.rule.MultiRestrictionFieldConstraint;
import org.drools.rule.OrCompositeRestriction;
import org.drools.rule.Package;
import org.drools.rule.PredicateConstraint;
import org.drools.rule.Query;
import org.drools.rule.ReturnValueConstraint;
import org.drools.rule.ReturnValueRestriction;
import org.drools.rule.Rule;
import org.drools.rule.VariableConstraint;
import org.drools.rule.VariableRestriction;
import org.drools.spi.AvailableVariables;
import org.drools.spi.DataProvider;
import org.drools.spi.Evaluator;
import org.drools.spi.FieldExtractor;
import org.drools.spi.FieldValue;
import org.drools.spi.ObjectType;
import org.drools.spi.Restriction;

/**
 * This builds the rule structure from an AST.
 * Generates semantic code where necessary if semantics are used.
 * This is an internal API.
 */
public class RuleBuilder {
    private Package                           pkg;
    private Rule                              rule;
    private RuleDescr                         ruleDescr;

    public String                             ruleClass;
    public List                               methods;
    public Map                                invokers;

    private Map                               invokerLookups;

    private Map                               descrLookups;

    private Map                               declarations;

    private int                               counter;

    private ColumnCounter                     columnCounter;

    private int                               columnOffset;

    private List                              errors;

    private final TypeResolver                typeResolver;

    private Map                               innerDeclarations;

    private static final StringTemplateGroup  ruleGroup            = new StringTemplateGroup( new InputStreamReader( RuleBuilder.class.getResourceAsStream( "javaRule.stg" ) ),
                                                                                              AngleBracketTemplateLexer.class );

    private static final StringTemplateGroup  invokerGroup         = new StringTemplateGroup( new InputStreamReader( RuleBuilder.class.getResourceAsStream( "javaInvokers.stg" ) ),
                                                                                              AngleBracketTemplateLexer.class );

    private static final KnowledgeHelperFixer knowledgeHelperFixer = new KnowledgeHelperFixer();

    private final FunctionFixer               functionFixer;
    
    private AvailableVariables          variables;

    // @todo move to an interface so it can work as a decorator
    private final JavaExprAnalyzer            analyzer             = new JavaExprAnalyzer();
    private ClassFieldExtractorCache          classFieldExtractorCache;

    public RuleBuilder(final TypeResolver typeResolver,
                       final FunctionFixer functionFixer,
                       final ClassFieldExtractorCache cache) {
        this.classFieldExtractorCache = cache;
        this.typeResolver = typeResolver;
        this.functionFixer = functionFixer;
        this.errors = new ArrayList();
    }

    public Map getInvokers() {
        return this.invokers;
    }

    public Map getDescrLookups() {
        return this.descrLookups;
    }

    public String getRuleClass() {
        return this.ruleClass;
    }

    public Map getInvokerLookups() {
        return this.invokerLookups;
    }

    public List getErrors() {
        return this.errors;
    }

    public Rule getRule() {
        if ( !this.errors.isEmpty() ) {
            this.rule.setSemanticallyValid( false );
        }
        return this.rule;
    }

    public Package getPackage() {
        return this.pkg;
    }

    public synchronized Rule build(final Package pkg,
                                   final RuleDescr ruleDescr) {
        this.pkg = pkg;
        this.methods = new ArrayList();
        this.invokers = new HashMap();
        this.invokerLookups = new HashMap();
        this.declarations = new HashMap();
        this.descrLookups = new HashMap();
        this.columnCounter = new ColumnCounter();
        this.variables = new AvailableVariables( new Map[] { this.declarations, this.pkg.getGlobals() } );

        this.ruleDescr = ruleDescr;

        if ( ruleDescr instanceof QueryDescr ) {
            this.rule = new Query( ruleDescr.getName() );
        } else {
            this.rule = new Rule( ruleDescr.getName() );
        }

        // Assign attributes
        setAttributes( this.rule,
                       ruleDescr.getAttributes() );

        // Build the left hand side
        // generate invoker, s
        build( ruleDescr );

        return this.rule;
    }

    private void setAttributes(final Rule rule,
                               final List attributes) {
        for ( final Iterator it = attributes.iterator(); it.hasNext(); ) {
            final AttributeDescr attributeDescr = (AttributeDescr) it.next();
            final String name = attributeDescr.getName();
            if ( name.equals( "salience" ) ) {
                rule.setSalience( Integer.parseInt( attributeDescr.getValue() ) );
            } else if ( name.equals( "no-loop" ) ) {
                if ( attributeDescr.getValue() == null ) {
                    rule.setNoLoop( true );
                } else {
                    rule.setNoLoop( Boolean.valueOf( attributeDescr.getValue() ).booleanValue() );
                }
            } else if ( name.equals( "auto-focus" ) ) {
                if ( attributeDescr.getValue() == null ) {
                    rule.setAutoFocus( true );
                } else {
                    rule.setAutoFocus( Boolean.valueOf( attributeDescr.getValue() ).booleanValue() );
                }
            } else if ( name.equals( "agenda-group" ) ) {
                rule.setAgendaGroup( attributeDescr.getValue() );
            } else if ( name.equals( "activation-group" ) ) {
                rule.setXorGroup( attributeDescr.getValue() );
            } else if ( name.equals( "duration" ) ) {
                rule.setDuration( Long.parseLong( attributeDescr.getValue() ) );
                rule.setAgendaGroup( "" );
            } else if ( name.equals( "language" ) ) {
                //@todo: we don't currently  support multiple languages
            }
        }
    }

    private void build(final RuleDescr ruleDescr) {

        for ( final Iterator it = ruleDescr.getLhs().getDescrs().iterator(); it.hasNext(); ) {
            final Object object = it.next();
            if ( object instanceof ConditionalElementDescr ) {
                if ( object.getClass() == AndDescr.class ) {
                    final GroupElement and = GroupElementFactory.newAndInstance();
                    build( this.rule,
                           (ConditionalElementDescr) object,
                           and,
                           false, // do not decrement offset
                           false ); // do not decrement first offset
                    this.rule.addPattern( and );
                } else if ( object.getClass() == OrDescr.class ) {
                    final GroupElement or = GroupElementFactory.newOrInstance();
                    build( this.rule,
                           (ConditionalElementDescr) object,
                           or,
                           true, // when OR is used, offset MUST be decremented
                           false ); // do not decrement first offset
                    this.rule.addPattern( or );
                } else if ( object.getClass() == NotDescr.class ) {
                    // We cannot have declarations created inside a not visible outside it, so track no declarations so they can be removed
                    this.innerDeclarations = new HashMap();
                    final GroupElement not = GroupElementFactory.newNotInstance();
                    build( this.rule,
                           (ConditionalElementDescr) object,
                           not,
                           true, // when NOT is used, offset MUST be decremented
                           true ); // when NOT is used, offset MUST be decremented for first column
                    this.rule.addPattern( not );

                    // remove declarations bound inside not node
                    for ( final Iterator notIt = this.innerDeclarations.keySet().iterator(); notIt.hasNext(); ) {
                        this.declarations.remove( notIt.next() );
                    }

                    this.innerDeclarations = null;
                } else if ( object.getClass() == ExistsDescr.class ) {
                    // We cannot have declarations created inside exists visible outside it, 
                    // so track declarations in a way they can be removed
                    this.innerDeclarations = new HashMap();
                    final GroupElement exists = GroupElementFactory.newExistsInstance();
                    build( this.rule,
                           (ConditionalElementDescr) object,
                           exists,
                           true, // when EXIST is used, offset MUST be decremented
                           true ); // when EXIST is used, offset MUST be decremented for first column
                    // remove declarations bound inside not node
                    for ( final Iterator notIt = this.innerDeclarations.keySet().iterator(); notIt.hasNext(); ) {
                        this.declarations.remove( notIt.next() );
                    }

                    this.innerDeclarations = null;
                    this.rule.addPattern( exists );
                } else if ( object.getClass() == EvalDescr.class ) {
                    final EvalCondition eval = build( (EvalDescr) object );
                    if ( eval != null ) {
                        this.rule.addPattern( eval );
                    }
                } else if ( object.getClass() == FromDescr.class ) {
                    final From from = build( (FromDescr) object );
                    this.rule.addPattern( from );
                } else if ( object.getClass() == AccumulateDescr.class ) {
                    final Accumulate accumulate = build( (AccumulateDescr) object );
                    this.rule.addPattern( accumulate );
                } else if ( object.getClass() == CollectDescr.class ) {
                    final Collect collect = build( (CollectDescr) object );
                    this.rule.addPattern( collect );
                }
            } else if ( object.getClass() == ColumnDescr.class ) {
                final Column column = build( (ColumnDescr) object );
                if ( column != null ) {
                    this.rule.addPattern( column );
                }
            }
        }

        // Build the consequence and generate it's invoker/s
        // generate the main rule from the previously generated s.
        if ( !(ruleDescr instanceof QueryDescr) ) {
            // do not build the consequence if we have a query
            buildConsequence( ruleDescr );
        }
        buildRule( ruleDescr );
    }

    private void build(final Rule rule,
                       final ConditionalElementDescr descr,
                       final GroupElement ce,
                       final boolean decrementOffset,
                       boolean decrementFirst) {
        for ( final Iterator it = descr.getDescrs().iterator(); it.hasNext(); ) {
            final Object object = it.next();
            if ( object instanceof ConditionalElementDescr ) {
                if ( object.getClass() == AndDescr.class ) {
                    final GroupElement and = GroupElementFactory.newAndInstance();
                    build( rule,
                           (ConditionalElementDescr) object,
                           and,
                           false, // do not decrement offset
                           false ); // do not decrement first offset
                    ce.addChild( and );
                } else if ( object.getClass() == OrDescr.class ) {
                    final GroupElement or = GroupElementFactory.newOrInstance();
                    build( rule,
                           (ConditionalElementDescr) object,
                           or,
                           true, // when OR is used, offset MUST be decremented
                           false ); // do not decrement first offset
                    ce.addChild( or );
                } else if ( object.getClass() == NotDescr.class ) {
                    final GroupElement not = GroupElementFactory.newNotInstance();
                    build( rule,
                           (ConditionalElementDescr) object,
                           not,
                           true, // when NOT is used, offset MUST be decremented
                           true ); // when NOT is used, offset MUST be decremented for first column
                    ce.addChild( not );
                } else if ( object.getClass() == ExistsDescr.class ) {
                    final GroupElement exists = GroupElementFactory.newExistsInstance();
                    build( rule,
                           (ConditionalElementDescr) object,
                           exists,
                           true, // when EXIST is used, offset MUST be decremented
                           true ); // when EXIST is used, offset MUST be decremented for first column
                    ce.addChild( exists );
                } else if ( object.getClass() == EvalDescr.class ) {
                    final EvalCondition eval = build( (EvalDescr) object );
                    if ( eval != null ) {
                        ce.addChild( eval );
                    }
                } else if ( object.getClass() == FromDescr.class ) {
                    final From from = build( (FromDescr) object );
                    this.rule.addPattern( from );
                } else if ( object.getClass() == AccumulateDescr.class ) {
                    final Accumulate accumulate = build( (AccumulateDescr) object );
                    this.rule.addPattern( accumulate );
                } else if ( object.getClass() == CollectDescr.class ) {
                    final Collect collect = build( (CollectDescr) object );
                    this.rule.addPattern( collect );
                }
            } else if ( object.getClass() == ColumnDescr.class ) {
                if ( decrementOffset && decrementFirst ) {
                    this.columnOffset--;
                } else {
                    decrementFirst = true;
                }
                final Column column = build( (ColumnDescr) object );
                if ( column != null ) {
                    ce.addChild( column );
                }
            }
        }
    }

    private Column build(final ColumnDescr columnDescr) {
        if ( columnDescr.getObjectType() == null || columnDescr.getObjectType().equals( "" ) ) {
            this.errors.add( new RuleError( this.rule,
                                            columnDescr,
                                            null,
                                            "ObjectType not correctly defined" ) );
            return null;
        }

        ObjectType objectType = null;

        final FactTemplate factTemplate = this.pkg.getFactTemplate( columnDescr.getObjectType() );

        if ( factTemplate != null ) {
            objectType = new FactTemplateObjectType( factTemplate );
        } else {
            try {
                Class userProvidedClass = this.typeResolver.resolveType( columnDescr.getObjectType() );
                String shadowProxyName = ShadowProxyFactory.getProxyClassNameForClass( userProvidedClass );
                Class shadowClass = null;
                try {
                    // if already loaded
                    shadowClass = this.pkg.getPackageCompilationData().getClassLoader().loadClass( shadowProxyName );                    
                } catch( ClassNotFoundException cnfe ) {
                    // otherwise, create and load
                    byte[] proxyBytes = ShadowProxyFactory.getProxyBytes( userProvidedClass );
                    if( proxyBytes != null ) {
                        this.pkg.getPackageCompilationData().write( shadowProxyName, 
                                                                    proxyBytes );
                        shadowClass = this.pkg.getPackageCompilationData().getClassLoader().loadClass( shadowProxyName );
                    }
                    
                }
                objectType = new ClassObjectType( userProvidedClass, shadowClass );
            } catch ( final ClassNotFoundException e ) {
                this.errors.add( new RuleError( this.rule,
                                                columnDescr,
                                                null,
                                                "Unable to resolve ObjectType '" + columnDescr.getObjectType() + "'" ) );
                return null;
            }
        }

        Column column;
        if ( columnDescr.getIdentifier() != null && !columnDescr.getIdentifier().equals( "" ) ) {
            column = new Column( this.columnCounter.getNext(),
                                 this.columnOffset,
                                 objectType,
                                 columnDescr.getIdentifier() );
            this.declarations.put( column.getDeclaration().getIdentifier(),
                                   column.getDeclaration() );

            if ( this.innerDeclarations != null ) {
                this.innerDeclarations.put( column.getDeclaration().getIdentifier(),
                                            column.getDeclaration() );
            }
        } else {
            column = new Column( this.columnCounter.getNext(),
                                 this.columnOffset,
                                 objectType,
                                 null );
        }

        for ( final Iterator it = columnDescr.getDescrs().iterator(); it.hasNext(); ) {
            final Object object = it.next();
            if ( object instanceof FieldBindingDescr ) {
                build( column,
                       (FieldBindingDescr) object );
            } else if ( object instanceof FieldConstraintDescr ) {
                build( column,
                       (FieldConstraintDescr) object );
            } else if ( object instanceof PredicateDescr ) {
                build( column,
                       (PredicateDescr) object );
            }
        }
        return column;
    }

    private void build(final Column column,
                       final FieldConstraintDescr fieldConstraintDescr) {

        final FieldExtractor extractor = getFieldExtractor( fieldConstraintDescr,
                                                            column.getObjectType(),
                                                            fieldConstraintDescr.getFieldName() );
        if ( extractor == null ) {
            // @todo log error
            return;
        }

        if ( fieldConstraintDescr.getRestrictions().size() == 1 ) {
            final Object object = fieldConstraintDescr.getRestrictions().get( 0 );

            final Restriction restriction = buildRestriction( column, 
                                                              extractor,
                                                              fieldConstraintDescr,
                                                              (RestrictionDescr) object );
            if ( restriction == null ) {
                // @todo log errors
                return;
            }

            if ( object instanceof LiteralRestrictionDescr ) {
                column.addConstraint( new LiteralConstraint( extractor,
                                                             (LiteralRestriction) restriction ) );
            } else if ( object instanceof VariableRestrictionDescr ) {
                column.addConstraint( new VariableConstraint( extractor,
                                                              (VariableRestriction) restriction ) );
            } else if ( object instanceof ReturnValueRestrictionDescr ) {
                column.addConstraint( new ReturnValueConstraint( extractor,
                                                                 (ReturnValueRestriction) restriction ) );
            }

            return;
        }

        final List orList = new ArrayList();
        List andList = null;

        RestrictionDescr currentRestriction = null;
        RestrictionDescr previousRestriction = null;

        List currentList = null;
        List previousList = null;

        for ( final Iterator it = fieldConstraintDescr.getRestrictions().iterator(); it.hasNext(); ) {
            final Object object = it.next();

            // Process an and/or connective 
            if ( object instanceof RestrictionConnectiveDescr ) {

                // is the connective an 'and'?
                if ( ((RestrictionConnectiveDescr) object).getConnective() == RestrictionConnectiveDescr.AND ) {
                    // if andList is null, then we know its the first
                    if ( andList == null ) {
                        andList = new ArrayList();
                    }
                    previousList = currentList;
                    currentList = andList;
                } else {
                    previousList = currentList;
                    currentList = orList;
                }
            } else {
                Restriction restriction = null;
                if ( currentList != null ) {
                    // Are we are at the first operator? if so treat differently
                    if ( previousList == null ) {
                        restriction = buildRestriction( column,
                                                        extractor,
                                                        fieldConstraintDescr,
                                                        previousRestriction );
                        if ( currentList == andList ) {
                            andList.add( restriction );
                        } else {
                            orList.add( restriction );
                        }
                    } else {
                        restriction = buildRestriction( column,
                                                        extractor,
                                                        fieldConstraintDescr,
                                                        previousRestriction );

                        if ( previousList == andList && currentList == orList ) {
                            andList.add( restriction );
                            if ( andList.size() == 1 ) {
                                // Can't have an 'and' connective with one child, so add directly to the or list
                                orList.add( andList.get( 0 ) );
                            } else {
                                final Restriction restrictions = new AndCompositeRestriction( (Restriction[]) andList.toArray( new Restriction[andList.size()] ) );
                                orList.add( restrictions );
                            }
                            andList = null;
                        } else if ( previousList == andList && currentList == andList ) {
                            andList.add( restriction );
                        } else if ( previousList == orList && currentList == andList ) {
                            andList.add( restriction );
                        } else if ( previousList == orList && currentList == orList ) {
                            orList.add( restriction );
                        }
                    }
                }
            }
            previousRestriction = currentRestriction;
            currentRestriction = (RestrictionDescr) object;
        }

        final Restriction restriction = buildRestriction( column, 
                                                          extractor,
                                                          fieldConstraintDescr,
                                                          currentRestriction );
        currentList.add( restriction );

        Restriction restrictions = null;
        if ( currentList == andList && !orList.isEmpty() ) {
            // Check if it finished with an and, and process it
            if ( andList != null ) {
                if ( andList.size() == 1 ) {
                    // Can't have an 'and' connective with one child, so add directly to the or list
                    orList.add( andList.get( 0 ) );
                } else {
                    orList.add( new AndCompositeRestriction( (Restriction[]) andList.toArray( new Restriction[andList.size()] ) ) );
                }
                andList = null;
            }
        }

        if ( !orList.isEmpty() ) {
            restrictions = new OrCompositeRestriction( (Restriction[]) orList.toArray( new Restriction[orList.size()] ) );
        } else if ( andList != null && !andList.isEmpty() ) {
            restrictions = new AndCompositeRestriction( (Restriction[]) andList.toArray( new Restriction[andList.size()] ) );
        } else {
            // @todo throw error
        }

        column.addConstraint( new MultiRestrictionFieldConstraint( extractor,
                                                                   restrictions ) );
    }

    private Restriction buildRestriction(final Column column,
                                         final FieldExtractor extractor,
                                         final FieldConstraintDescr fieldConstraintDescr,
                                         final RestrictionDescr restrictionDescr) {
        Restriction restriction = null;
        if ( restrictionDescr instanceof LiteralRestrictionDescr ) {
            restriction = buildRestriction( extractor,
                                            fieldConstraintDescr,
                                            (LiteralRestrictionDescr) restrictionDescr );
        } else if ( restrictionDescr instanceof VariableRestrictionDescr ) {
            restriction = buildRestriction( extractor,
                                            fieldConstraintDescr,
                                            (VariableRestrictionDescr) restrictionDescr );
        } else if ( restrictionDescr instanceof ReturnValueRestrictionDescr ) {
            restriction = buildRestriction( column,
                                            extractor,
                                            fieldConstraintDescr,
                                            (ReturnValueRestrictionDescr) restrictionDescr );

        }

        return restriction;
    }

    private void build(final Column column,
                       final FieldBindingDescr fieldBindingDescr) {
        Declaration declaration = (Declaration) this.declarations.get( fieldBindingDescr.getIdentifier() );
        if ( declaration != null ) {
            // This declaration already  exists, so throw an Exception
            this.errors.add( new RuleError( this.rule,
                                            fieldBindingDescr,
                                            null,
                                            "Duplicate declaration for variable '" + fieldBindingDescr.getIdentifier() + "' in the rule '" + this.rule.getName() + "'" ) );
            return;
        }

        final FieldExtractor extractor = getFieldExtractor( fieldBindingDescr,
                                                            column.getObjectType(),
                                                            fieldBindingDescr.getFieldName() );
        if ( extractor == null ) {
            return;
        }

        declaration = column.addDeclaration( fieldBindingDescr.getIdentifier(),
                                             extractor );

        this.declarations.put( declaration.getIdentifier(),
                               declaration );

        if ( this.innerDeclarations != null ) {
            this.innerDeclarations.put( declaration.getIdentifier(),
                                        declaration );
        }
    }

    private VariableRestriction buildRestriction(final FieldExtractor extractor,
                                                 final FieldConstraintDescr fieldConstraintDescr,
                                                 final VariableRestrictionDescr variableRestrictionDescr) {
        if ( variableRestrictionDescr.getIdentifier() == null || variableRestrictionDescr.getIdentifier().equals( "" ) ) {
            this.errors.add( new RuleError( this.rule,
                                            variableRestrictionDescr,
                                            null,
                                            "Identifier not defined for binding field '" + fieldConstraintDescr.getFieldName() + "'" ) );
            return null;
        }

        final Declaration declaration = (Declaration) this.declarations.get( variableRestrictionDescr.getIdentifier() );

        if ( declaration == null ) {
            this.errors.add( new RuleError( this.rule,
                                            variableRestrictionDescr,
                                            null,
                                            "Unable to return Declaration for identifier '" + variableRestrictionDescr.getIdentifier() + "'" ) );
            return null;
        }

        final Evaluator evaluator = getEvaluator( variableRestrictionDescr,
                                                  extractor.getValueType(),
                                                  variableRestrictionDescr.getEvaluator() );
        if ( evaluator == null ) {
            return null;
        }

        return new VariableRestriction( extractor, 
                                        declaration,
                                        evaluator );
    }

    private LiteralRestriction buildRestriction(final FieldExtractor extractor,
                                                final FieldConstraintDescr fieldConstraintDescr,
                                                final LiteralRestrictionDescr literalRestrictionDescr) {
        FieldValue field = null;
        if ( literalRestrictionDescr.isStaticFieldValue() ) {
            final int lastDot = literalRestrictionDescr.getText().lastIndexOf( '.' );
            final String className = literalRestrictionDescr.getText().substring( 0,
                                                                                  lastDot );
            final String fieldName = literalRestrictionDescr.getText().substring( lastDot + 1 );
            try {
                final Class staticClass = this.typeResolver.resolveType( className );
                field = FieldFactory.getFieldValue( staticClass.getField( fieldName ).get( null ).toString(), extractor.getValueType() );
            } catch ( final ClassNotFoundException e ) {
                this.errors.add( new RuleError( this.rule,
                                                literalRestrictionDescr,
                                                e,
                                                e.getMessage() ) );
            } catch ( final Exception e ) {
                this.errors.add( new RuleError( this.rule,
                                                literalRestrictionDescr,
                                                e,
                                                "Unable to create a Field value of type  '" + extractor.getValueType() + "' and value '" + literalRestrictionDescr.getText() + "'" ) );
            }

        } else {
            try {
                field = FieldFactory.getFieldValue( literalRestrictionDescr.getText(),
                                                    extractor.getValueType() );
            } catch ( final Exception e ) {
                this.errors.add( new RuleError( this.rule,
                                                literalRestrictionDescr,
                                                e,
                                                "Unable to create a Field value of type  '" + extractor.getValueType() + "' and value '" + literalRestrictionDescr.getText() + "'" ) );
            }
        }

        final Evaluator evaluator = getEvaluator( literalRestrictionDescr,
                                                  extractor.getValueType(),
                                                  literalRestrictionDescr.getEvaluator() );
        if ( evaluator == null ) {
            return null;
        }

        return new LiteralRestriction( field,
                                       evaluator,
                                       extractor );
    }

    private ReturnValueRestriction buildRestriction(final Column column,
                                                    final FieldExtractor extractor,
                                                    final FieldConstraintDescr fieldConstraintDescr,
                                                    final ReturnValueRestrictionDescr returnValueRestrictionDescr) {
        final String className = "returnValue" + this.counter++;
        returnValueRestrictionDescr.setClassMethodName( className );

        final List[] usedIdentifiers = getUsedIdentifiers( returnValueRestrictionDescr,
                                                           returnValueRestrictionDescr.getText() );

        final List tupleDeclarations = new ArrayList();
        final List factDeclarations = new ArrayList();
        for ( int i = 0, size = usedIdentifiers[0].size(); i < size; i++ ) {
            Declaration declaration = (Declaration) this.declarations.get( (String) usedIdentifiers[0].get( i ) );
            if( declaration.getColumn() == column ) {
                factDeclarations.add( declaration );
            } else {
                tupleDeclarations.add( declaration );
            }
        }

        final Evaluator evaluator = getEvaluator( returnValueRestrictionDescr,
                                                  extractor.getValueType(),
                                                  returnValueRestrictionDescr.getEvaluator() );
        if ( evaluator == null ) {
            return null;
        }

        Declaration[] previousDeclarations = (Declaration[]) tupleDeclarations.toArray( new Declaration[tupleDeclarations.size()] );
        Declaration[] localDeclarations = (Declaration[]) factDeclarations.toArray( new Declaration[factDeclarations.size()] );
        final ReturnValueRestriction returnValueRestriction = new ReturnValueRestriction( extractor,
                                                                                          previousDeclarations,
                                                                                          localDeclarations,
                                                                                          evaluator );

        StringTemplate st = RuleBuilder.ruleGroup.getInstanceOf( "returnValueMethod" );

        setStringTemplateAttributes( st,
                                     previousDeclarations,
                                     (String[]) usedIdentifiers[1].toArray( new String[usedIdentifiers[1].size()] ),
                                     returnValueRestrictionDescr.getText() );

        final String[] localDeclarationTypes = new String[localDeclarations.length];
        for ( int i = 0, size = localDeclarations.length; i < size; i++ ) {
            localDeclarationTypes[i] = localDeclarations[i].getExtractor().getExtractToClass().getName().replace( '$',
                                                                                                        '.' );
        }

        st.setAttribute( "localDeclarations",
                         localDeclarations );
        st.setAttribute( "localDeclarationTypes",
                         localDeclarationTypes );

        st.setAttribute( "methodName",
                         className );

        final String returnValueText = this.functionFixer.fix( returnValueRestrictionDescr.getText(), variables );
        st.setAttribute( "text",
                         returnValueText );

        this.methods.add( st.toString() );

        st = RuleBuilder.invokerGroup.getInstanceOf( "returnValueInvoker" );

        st.setAttribute( "package",
                         this.pkg.getName() );
        st.setAttribute( "ruleClassName",
                         ucFirst( this.ruleDescr.getClassName() ) );
        st.setAttribute( "invokerClassName",
                         this.ruleDescr.getClassName() + ucFirst( className ) + "Invoker" );
        st.setAttribute( "methodName",
                         className );

        setStringTemplateAttributes( st,
                                     previousDeclarations,
                                     (String[]) usedIdentifiers[1].toArray( new String[usedIdentifiers[1].size()] ),
                                     returnValueRestrictionDescr.getText() );

        st.setAttribute( "localDeclarations",
                         localDeclarations );
        st.setAttribute( "localDeclarationTypes",
                         localDeclarationTypes );

        st.setAttribute( "hashCode",
                         returnValueText.hashCode() );

        final String invokerClassName = this.pkg.getName() + "." + this.ruleDescr.getClassName() + ucFirst( className ) + "Invoker";
        this.invokers.put( invokerClassName,
                           st.toString() );
        this.invokerLookups.put( invokerClassName,
                                 returnValueRestriction );
        this.descrLookups.put( invokerClassName,
                               returnValueRestrictionDescr );

        return returnValueRestriction;
    }

    private void build(final Column column,
                       final PredicateDescr predicateDescr) {
        // generate 
        // generate Invoker
        final String className = "predicate" + this.counter++;
        predicateDescr.setClassMethodName( className );

        final FieldExtractor extractor = getFieldExtractor( predicateDescr,
                                                            column.getObjectType(),
                                                            predicateDescr.getFieldName() );
        if ( extractor == null ) {
            return;
        }

        final Declaration declaration = column.addDeclaration( predicateDescr.getDeclaration(),
                                                               extractor );

        this.declarations.put( declaration.getIdentifier(),
                               declaration );

        if ( this.innerDeclarations != null ) {
            this.innerDeclarations.put( declaration.getIdentifier(),
                                        declaration );
        }

        final List[] usedIdentifiers = getUsedIdentifiers( predicateDescr,
                                                           predicateDescr.getText() );
        // Don't include the focus declaration, that hasn't been merged into the tuple yet.
        usedIdentifiers[0].remove( predicateDescr.getDeclaration() );

        final List tupleDeclarations = new ArrayList();
        final List factDeclarations = new ArrayList();
        for ( int i = 0, size = usedIdentifiers[0].size(); i < size; i++ ) {
            Declaration decl = (Declaration) this.declarations.get( (String) usedIdentifiers[0].get( i ) );
            if( decl.getColumn() == column ) {
                factDeclarations.add( decl );
            } else {
                tupleDeclarations.add( decl );
            }
        }
        Declaration[] previousDeclarations = (Declaration[]) tupleDeclarations.toArray( new Declaration[tupleDeclarations.size()] );
        Declaration[] localDeclarations = (Declaration[]) factDeclarations.toArray( new Declaration[factDeclarations.size()] );

        final PredicateConstraint predicateConstraint = new PredicateConstraint( declaration,
                                                                                 previousDeclarations,
                                                                                 localDeclarations );
        column.addConstraint( predicateConstraint );

        StringTemplate st = RuleBuilder.ruleGroup.getInstanceOf( "predicateMethod" );

        st.setAttribute( "declaration",
                         declaration );

        st.setAttribute( "declarationType",
                         declaration.getExtractor().getExtractToClass().getName().replace( '$',
                                                                                           '.' ) );

        setStringTemplateAttributes( st,
                                     previousDeclarations,
                                     (String[]) usedIdentifiers[1].toArray( new String[usedIdentifiers[1].size()] ),
                                     predicateDescr.getText() );

        final String[] localDeclarationTypes = new String[localDeclarations.length];
        for ( int i = 0, size = localDeclarations.length; i < size; i++ ) {
            localDeclarationTypes[i] = localDeclarations[i].getExtractor().getExtractToClass().getName().replace( '$',
                                                                                                        '.' );
        }

        st.setAttribute( "localDeclarations",
                         localDeclarations );
        st.setAttribute( "localDeclarationTypes",
                         localDeclarationTypes );

        st.setAttribute( "methodName",
                         className );

        final String predicateText = this.functionFixer.fix( predicateDescr.getText(), variables );
        st.setAttribute( "text",
                         predicateText );

        this.methods.add( st.toString() );

        st = RuleBuilder.invokerGroup.getInstanceOf( "predicateInvoker" );

        st.setAttribute( "package",
                         this.pkg.getName() );
        st.setAttribute( "ruleClassName",
                         ucFirst( this.ruleDescr.getClassName() ) );
        st.setAttribute( "invokerClassName",
                         this.ruleDescr.getClassName() + ucFirst( className ) + "Invoker" );
        st.setAttribute( "methodName",
                         className );

        st.setAttribute( "declaration",
                         declaration );
        st.setAttribute( "declarationType",
                         declaration.getExtractor().getExtractToClass().getName().replace( '$',
                                                                                           '.' ) );

        setStringTemplateAttributes( st,
                                     previousDeclarations,
                                     (String[]) usedIdentifiers[1].toArray( new String[usedIdentifiers[1].size()] ),
                                     predicateDescr.getText() );

        st.setAttribute( "localDeclarations",
                         localDeclarations );
        st.setAttribute( "localDeclarationTypes",
                         localDeclarationTypes );

        st.setAttribute( "hashCode",
                         predicateText.hashCode() );

        final String invokerClassName = this.pkg.getName() + "." + this.ruleDescr.getClassName() + ucFirst( className ) + "Invoker";
        this.invokers.put( invokerClassName,
                           st.toString() );
        this.invokerLookups.put( invokerClassName,
                                 predicateConstraint );
        this.descrLookups.put( invokerClassName,
                               predicateDescr );
    }

    private From build(final FromDescr fromDescr) {
      final Column column = build( fromDescr.getReturnedColumn() );
      AccessorDescr accessor = (AccessorDescr) fromDescr.getDataSource();      
      DataProvider dataProvider = null;      
      try {                    
          JFDIParser parser = createParser(  accessor.toString() );
          DroolsJFDIFactory factory = new DroolsJFDIFactory( this.typeResolver );
          factory.setDeclarationMap( this.declarations );
          factory.setGlobalsMap( this.pkg.getGlobals() );          
          parser.setValueHandlerFactory( factory );
          
          dataProvider = new JFDIDataProvider(parser.expr(), factory);
      } catch ( final Exception e ) {
          this.errors.add( new RuleError( this.rule,
                                          fromDescr,
                                          null,
                                          "Unable to build expression for 'from' node '" + accessor.toString() + "'" ) );
          return null;
      }
      
      
      return new From( column,
                       dataProvider );		        
    }
    
//    private From build(final FromDescr fromDescr) {
//    	return null;
    	// @todo: waiting for JFDI so we can impl this properly
//        final Column column = build( fromDescr.getReturnedColumn() );
//
//        final DeclarativeInvokerDescr invokerDescr = fromDescr.getDataSource();
//
//        DataProvider dataProvider = null;
//
//        if ( invokerDescr.getClass() == MethodAccessDescr.class ) {
//            final MethodAccessDescr methodAccessor = (MethodAccessDescr) invokerDescr;
//
//            ValueHandler instanceValueHandler = null;
//            final String variableName = methodAccessor.getVariableName();
//            if ( this.declarations.containsKey( variableName ) ) {
//                instanceValueHandler = new DeclarationVariable( (Declaration) this.declarations.get( variableName ) );
//            } else if ( this.pkg.getGlobals().containsKey( variableName ) ) {
//                instanceValueHandler = new GlobalVariable( variableName,
//                                                           (Class) this.pkg.getGlobals().get( variableName ) );
//            } else {
//                throw new IllegalArgumentException( "The variable name [" + variableName + "] was not a global or declaration." );
//            }
//
//            final List arguments = ((MethodAccessDescr) invokerDescr).getArguments();
//            final List valueHandlers = new ArrayList();
//
//            for ( final Iterator iter = arguments.iterator(); iter.hasNext(); ) {
//                valueHandlers.add( buildValueHandler( (ArgumentValueDescr) iter.next() ) );
//            }
//
//            final MethodInvoker invoker = new MethodInvoker( methodAccessor.getMethodName(),
//                                                             instanceValueHandler,
//                                                             (ValueHandler[]) valueHandlers.toArray( new ValueHandler[valueHandlers.size()] ) );
//            dataProvider = new MethodDataProvider( invoker );
//        } else if ( invokerDescr.getClass() == FieldAccessDescr.class ) {
//            final FieldAccessDescr fieldAccessor = (FieldAccessDescr) invokerDescr;
//
//            ValueHandler instanceValueHandler = null;
//            final String variableName = fieldAccessor.getVariableName();
//            if ( this.declarations.containsKey( variableName ) ) {
//                instanceValueHandler = new DeclarationVariable( (Declaration) this.declarations.get( variableName ) );
//            } else if ( this.pkg.getGlobals().containsKey( variableName ) ) {
//                instanceValueHandler = new GlobalVariable( variableName,
//                                                           (Class) this.pkg.getGlobals().get( variableName ) );
//            } else {
//                throw new IllegalArgumentException( "The variable name [" + variableName + "] was not a global or declaration." );
//            }
//            ArgumentValueDescr arg = ((FieldAccessDescr) invokerDescr).getArgument();
//            ValueHandler valueHandler = null;
//            if ( arg != null ) {
//                valueHandler = buildValueHandler( arg );
//            }
//
//            final FieldGetter getter = new FieldGetter( fieldAccessor.getFieldName(),
//                                                        instanceValueHandler,
//                                                        valueHandler );
//            dataProvider = new MethodDataProvider( getter );
//        }
//
//        return new From( column,
//                         dataProvider );

//    }

//    private ValueHandler buildValueHandler(final ArgumentValueDescr descr) {
//        ValueHandler valueHandler = null;
//        if ( descr.getType() == ArgumentValueDescr.VARIABLE ) {
//            if ( this.declarations.containsKey( descr.getValue() ) ) {
//                valueHandler = new DeclarationVariable( (Declaration) this.declarations.get( descr.getValue() ) );
//            } else if ( this.pkg.getGlobals().containsKey( descr.getValue() ) ) {
//                valueHandler = new GlobalVariable( (String) descr.getValue(),
//                                                   (Class) this.pkg.getGlobals().get( descr.getValue() ) );
//            } else {
//                throw new IllegalArgumentException( "Uknown variable: " + descr.getValue() );
//            }
//        } else if ( descr.getType() == ArgumentValueDescr.MAP ) {
//            final ArgumentValueDescr.KeyValuePairDescr[] pairs = (ArgumentValueDescr.KeyValuePairDescr[]) descr.getValue();
//            final List list = new ArrayList( pairs.length );
//            for ( int i = 0, length = pairs.length; i < length; i++ ) {
//                list.add( new MapValue.KeyValuePair( buildValueHandler( pairs[i].getKey() ),
//                                                     buildValueHandler( pairs[i].getValue() ) ) );
//            }
//
//            valueHandler = new MapValue( (MapValue.KeyValuePair[]) list.toArray( new MapValue.KeyValuePair[pairs.length] ) );
//        } else if ( descr.getType() == ArgumentValueDescr.LIST ) {
//            final List args = (List) descr.getValue();
//            final List handlers = new ArrayList( args.size() );
//            for ( final Iterator iter = args.iterator(); iter.hasNext(); ) {
//                final ArgumentValueDescr arg = (ArgumentValueDescr) iter.next();
//                handlers.add( buildValueHandler( arg ) );
//            }
//            valueHandler = new ListValue( handlers );
//        } else if ( descr.getType() == ArgumentValueDescr.BOOLEAN ) {
//            // handling a literal
//            valueHandler = new LiteralValue( new Boolean ( (String) descr.getValue() ) );
//        } else if ( descr.getType() == ArgumentValueDescr.INTEGRAL ) {
//            String text = (String) descr.getValue();
//            char c = text.charAt( text.length() - 1 );
//            if ( Character.getType( c ) != Character.DECIMAL_DIGIT_NUMBER ) {
//                switch ( c ) {
//                    case 'l' :
//                    case 'L' :
//                        valueHandler = new LiteralValue( new Long( (String) descr.getValue() ) );                        
//                        break;
//                    case 'f' :
//                    case 'F' :
//                        valueHandler = new LiteralValue( new Float( (String) descr.getValue() ) );                          
//                        break;
//                    case 'd' :
//                    case 'D' :
//                        valueHandler = new LiteralValue( new Double( (String) descr.getValue() ) );                        
//                        break;
//                    default :
//                        throw new IllegalArgumentException( "invalid type identifier '" + c + "' used with number [" + text + "]" );
//                }
//            } else {
//                valueHandler = new LiteralValue( new Integer( (String) descr.getValue() ) );                
//            }
//            
//        } else if ( descr.getType() == ArgumentValueDescr.DECIMAL ) {
//            String text = (String) descr.getValue();
//            char c = text.charAt( text.length() - 1 );
//            if ( Character.getType( c ) != Character.DECIMAL_DIGIT_NUMBER ) {
//                switch ( c ) {
//                    case 'l' :
//                    case 'L' :
//                        throw new IllegalArgumentException( "invalid type identifier '" + c + "' used with number [" + text + "]" );                      
//                    case 'f' :
//                    case 'F' :
//                        valueHandler = new LiteralValue( new Float( (String) descr.getValue() ) );                          
//                        break;
//                    case 'd' :
//                    case 'D' :
//                        valueHandler = new LiteralValue( new Double( (String) descr.getValue() ) );                        
//                        break;
//                    default :
//                        throw new IllegalArgumentException( "invalid type identifier '" + c + "' used with number [" + text + "]" );
//                }
//            } else {
//                valueHandler = new LiteralValue( new Float( (String) descr.getValue() ) );                
//            }
//        } else if ( descr.getType() == ArgumentValueDescr.STRING ) {
//            // handling a literal
//            valueHandler = new LiteralValue( (String) descr.getValue() );
//        } else {
//            // This should never happen
//            throw new IllegalArgumentException( "Unable to determine type for argument [" + descr.getType() + "]" );
//        }
//        return valueHandler;
//    }

	protected JFDIParser createParser(String text) throws IOException {
		JFDIParser parser = new JFDIParser( createTokenStream( text ) );
        DroolsJFDIFactory factory = new DroolsJFDIFactory( this.typeResolver );
        parser.setValueHandlerFactory( factory );
		return parser;
	}
	
	private TokenStream createTokenStream(String text) throws IOException {
		return new CommonTokenStream( createLexer( text ) );
	}
	
	private JFDILexer createLexer(String text) throws IOException {
		JFDILexer lexer = new JFDILexer(  new ANTLRStringStream( text ) );
		return lexer;
	}
	
	private Reader createReader(String text) {
		InputStream in = getClass().getResourceAsStream( text );
		return new InputStreamReader( in );
	}    
    
    private EvalCondition build(final EvalDescr evalDescr) {

        final String className = "eval" + this.counter++;
        evalDescr.setClassMethodName( className );

        final List[] usedIdentifiers = getUsedIdentifiers( evalDescr,
                                                           evalDescr.getText() );

        final Declaration[] declarations = new Declaration[usedIdentifiers[0].size()];
        for ( int i = 0, size = usedIdentifiers[0].size(); i < size; i++ ) {
            declarations[i] = (Declaration) this.declarations.get( (String) usedIdentifiers[0].get( i ) );
        }

        final EvalCondition eval = new EvalCondition( declarations );

        StringTemplate st = RuleBuilder.ruleGroup.getInstanceOf( "evalMethod" );

        setStringTemplateAttributes( st,
                                     declarations,
                                     (String[]) usedIdentifiers[1].toArray( new String[usedIdentifiers[1].size()] ),
                                     evalDescr.getText() );

        st.setAttribute( "methodName",
                         className );

        final String evalText = this.functionFixer.fix( evalDescr.getText(), variables );
        st.setAttribute( "text",
                         evalText );

        this.methods.add( st.toString() );

        st = RuleBuilder.invokerGroup.getInstanceOf( "evalInvoker" );

        st.setAttribute( "package",
                         this.pkg.getName() );
        st.setAttribute( "ruleClassName",
                         ucFirst( this.ruleDescr.getClassName() ) );
        st.setAttribute( "invokerClassName",
                         this.ruleDescr.getClassName() + ucFirst( className ) + "Invoker" );
        st.setAttribute( "methodName",
                         className );

        setStringTemplateAttributes( st,
                                     declarations,
                                     (String[]) usedIdentifiers[1].toArray( new String[usedIdentifiers[1].size()] ),
                                     evalDescr.getText() );

        st.setAttribute( "hashCode",
                         evalText.hashCode() );

        final String invokerClassName = this.pkg.getName() + "." + this.ruleDescr.getClassName() + ucFirst( className ) + "Invoker";
        this.invokers.put( invokerClassName,
                           st.toString() );
        this.invokerLookups.put( invokerClassName,
                                 eval );
        this.descrLookups.put( invokerClassName,
                               evalDescr );
        return eval;
    }

    private Accumulate build(final AccumulateDescr accumDescr) {
        this.innerDeclarations = new HashMap();

        Column sourceColumn = build( accumDescr.getSourceColumn() );
        // remove declarations bound inside source column
        this.declarations.keySet().removeAll( this.innerDeclarations.keySet() );
        Map sourceDeclarations = this.innerDeclarations;
        this.innerDeclarations = null;

        // decrementing offset as accumulate fills only one column
        this.columnOffset--;
        Column resultColumn = build( accumDescr.getResultColumn() );

        final String className = "accumulate" + this.counter++;
        accumDescr.setClassMethodName( className );

        final List[] usedIdentifiers1 = getUsedIdentifiers( accumDescr,
                                                            accumDescr.getInitCode() );
        final List[] usedIdentifiers2 = getUsedIdentifiers( accumDescr,
                                                            accumDescr.getActionCode() );
        final List[] usedIdentifiers3 = getUsedIdentifiers( accumDescr,
                                                            accumDescr.getResultCode() );

        final List requiredDeclarations = new ArrayList( usedIdentifiers1[0] );
        requiredDeclarations.addAll( usedIdentifiers2[0] );
        requiredDeclarations.addAll( usedIdentifiers3[0] );

        final List requiredGlobals = new ArrayList( usedIdentifiers1[1] );
        requiredGlobals.addAll( usedIdentifiers2[1] );
        requiredGlobals.addAll( usedIdentifiers3[1] );

        final Declaration[] declarations = new Declaration[requiredDeclarations.size()];
        for ( int i = 0, size = requiredDeclarations.size(); i < size; i++ ) {
            declarations[i] = (Declaration) this.declarations.get( (String) requiredDeclarations.get( i ) );
        }
        final Declaration[] sourceDeclArr = (Declaration[]) sourceDeclarations.values().toArray( new Declaration[sourceDeclarations.size()] );

        final String[] globals = (String[]) requiredGlobals.toArray( new String[requiredGlobals.size()] );

        StringTemplate st = RuleBuilder.ruleGroup.getInstanceOf( "accumulateMethod" );

        setStringTemplateAttributes( st,
                                     declarations,
                                     globals,
                                     null );

        st.setAttribute( "innerDeclarations",
                         sourceDeclArr );
        st.setAttribute( "methodName",
                         className );

        final String initCode = this.functionFixer.fix( accumDescr.getInitCode() );
        final String actionCode = this.functionFixer.fix( accumDescr.getActionCode() );
        final String resultCode = this.functionFixer.fix( accumDescr.getResultCode() );
        st.setAttribute( "initCode",
                         initCode );
        st.setAttribute( "actionCode",
                         actionCode );
        st.setAttribute( "resultCode",
                         resultCode );

        String resultType = null;
        // TODO: Need to change this... 
        if ( resultColumn.getObjectType() instanceof ClassObjectType ) {
            resultType = ((ClassObjectType) resultColumn.getObjectType()).getClassType().getName();
        } else {
            resultType = resultColumn.getObjectType().getValueType().getClassType().getName();
        }

        st.setAttribute( "resultType",
                         resultType );

        this.methods.add( st.toString() );

        st = RuleBuilder.invokerGroup.getInstanceOf( "accumulateInvoker" );

        st.setAttribute( "package",
                         this.pkg.getName() );
        st.setAttribute( "ruleClassName",
                         ucFirst( this.ruleDescr.getClassName() ) );
        st.setAttribute( "invokerClassName",
                         this.ruleDescr.getClassName() + ucFirst( className ) + "Invoker" );
        st.setAttribute( "methodName",
                         className );

        setStringTemplateAttributes( st,
                                     declarations,
                                     (String[]) requiredGlobals.toArray( new String[requiredGlobals.size()] ),
                                     null );

        st.setAttribute( "hashCode",
                         actionCode.hashCode() );

        Accumulate accumulate = new Accumulate( sourceColumn,
                                                resultColumn,
                                                declarations,
                                                sourceDeclArr );
        final String invokerClassName = this.pkg.getName() + "." + this.ruleDescr.getClassName() + ucFirst( className ) + "Invoker";
        this.invokers.put( invokerClassName,
                           st.toString() );
        this.invokerLookups.put( invokerClassName,
                                 accumulate );
        this.descrLookups.put( invokerClassName,
                               accumDescr );
        return accumulate;
    }

    private Collect build(final CollectDescr collectDescr) {
        this.innerDeclarations = new HashMap();
        Column sourceColumn = build( collectDescr.getSourceColumn() );
        // remove declarations bound inside source column
        this.declarations.keySet().removeAll( this.innerDeclarations.keySet() );
        this.innerDeclarations = null;

        // decrementing offset as collect fills only one column
        this.columnOffset--;
        Column resultColumn = build( collectDescr.getResultColumn() );

        final String className = "collect" + this.counter++;
        collectDescr.setClassMethodName( className );

        Collect collect = new Collect( sourceColumn,
                                       resultColumn );
        return collect;
    }

    private void buildConsequence(final RuleDescr ruleDescr) {
        // generate 
        // generate Invoker
        final String className = "consequence";

        StringTemplate st = RuleBuilder.ruleGroup.getInstanceOf( "consequenceMethod" );

        st.setAttribute( "methodName",
                         className );

        final List[] usedIdentifiers = getUsedCIdentifiers( ruleDescr,
                                                            ruleDescr.getConsequence() );

        final Declaration[] declarations = new Declaration[usedIdentifiers[0].size()];
        for ( int i = 0, size = usedIdentifiers[0].size(); i < size; i++ ) {
            declarations[i] = (Declaration) this.declarations.get( (String) usedIdentifiers[0].get( i ) );
        }

        setStringTemplateAttributes( st,
                                     declarations,
                                     (String[]) usedIdentifiers[1].toArray( new String[usedIdentifiers[1].size()] ),
                                     ruleDescr.getConsequence() );
        st.setAttribute( "text",
                         this.functionFixer.fix( RuleBuilder.knowledgeHelperFixer.fix( ruleDescr.getConsequence() ), variables ) );

        this.methods.add( st.toString() );

        st = RuleBuilder.invokerGroup.getInstanceOf( "consequenceInvoker" );

        st.setAttribute( "package",
                         this.pkg.getName() );
        st.setAttribute( "ruleClassName",
                         ucFirst( this.ruleDescr.getClassName() ) );
        st.setAttribute( "invokerClassName",
                         ruleDescr.getClassName() + ucFirst( className ) + "Invoker" );
        st.setAttribute( "methodName",
                         className );

        setStringTemplateAttributes( st,
                                     declarations,
                                     (String[]) usedIdentifiers[1].toArray( new String[usedIdentifiers[1].size()] ),
                                     ruleDescr.getConsequence() );

        // Must use the rule declarations, so we use the same order as used in the generated invoker
        final List list = Arrays.asList( this.rule.getDeclarations() );

        final int[] indexes = new int[declarations.length];
        for ( int i = 0, length = declarations.length; i < length; i++ ) {
            indexes[i] = list.indexOf( declarations[i] );
            if ( indexes[i] == -1 ) {
                // some defensive code, this should never happen
                throw new RuntimeDroolsException( "Unable to find declaration in list while generating the consequence invoker" );
            }
        }

        st.setAttribute( "indexes",
                         indexes );

        st.setAttribute( "text",
                         ruleDescr.getConsequence() );

        final String invokerClassName = this.pkg.getName() + "." + ruleDescr.getClassName() + ucFirst( className ) + "Invoker";
        this.invokers.put( invokerClassName,
                           st.toString() );
        this.invokerLookups.put( invokerClassName,
                                 this.rule );
        this.descrLookups.put( invokerClassName,
                               ruleDescr );
    }

    private void buildRule(final RuleDescr ruleDescr) {
        // If there is no compiled code, return
        if ( this.methods.isEmpty() ) {
            this.ruleClass = null;
            return;
        }
        final String lineSeparator = System.getProperty( "line.separator" );

        final StringBuffer buffer = new StringBuffer();
        buffer.append( "package " + this.pkg.getName() + ";" + lineSeparator );

        for ( final Iterator it = this.pkg.getImports().iterator(); it.hasNext(); ) {
            buffer.append( "import " + it.next() + ";" + lineSeparator );
        }

        buffer.append( "public class " + ucFirst( this.ruleDescr.getClassName() ) + " {" + lineSeparator );
        buffer.append( "    private static final long serialVersionUID  = 320L;" + lineSeparator );

        for ( int i = 0, size = this.methods.size() - 1; i < size; i++ ) {
            buffer.append( this.methods.get( i ) + lineSeparator );
        }

        final String[] lines = buffer.toString().split( lineSeparator );

        this.ruleDescr.setConsequenceOffset( lines.length + 1 );

        buffer.append( this.methods.get( this.methods.size() - 1 ) + lineSeparator );
        buffer.append( "}" );

        this.ruleClass = buffer.toString();
    }

    private void setStringTemplateAttributes(final StringTemplate st,
                                             final Declaration[] declarations,
                                             final String[] globals,
                                             final String text) {
        final String[] declarationTypes = new String[declarations.length];
        for ( int i = 0, size = declarations.length; i < size; i++ ) {
            declarationTypes[i] = declarations[i].getExtractor().getExtractToClass().getName().replace( '$',
                                                                                                        '.' );
        }

        final List globalTypes = new ArrayList( globals.length );
        for ( int i = 0, length = globals.length; i < length; i++ ) {
            globalTypes.add( ((Class) this.pkg.getGlobals().get( globals[i] )).getName().replace( '$',
                                                                                                  '.' ) );
        }

        st.setAttribute( "declarations",
                         declarations );
        st.setAttribute( "declarationTypes",
                         declarationTypes );

        st.setAttribute( "globals",
                         globals );
        st.setAttribute( "globalTypes",
                         globalTypes );
    }

    private String ucFirst(final String name) {
        return name.toUpperCase().charAt( 0 ) + name.substring( 1 );
    }

    private FieldExtractor getFieldExtractor(final BaseDescr descr,
                                             final ObjectType objectType,
                                             final String fieldName) {
        FieldExtractor extractor = null;

        if ( objectType.getValueType() == ValueType.FACTTEMPLATE_TYPE ) {
            //@todo use extractor cache            
            final FactTemplate factTemplate = ((FactTemplateObjectType) objectType).getFactTemplate();
            extractor = new FactTemplateFieldExtractor( factTemplate,
                                                        factTemplate.getFieldTemplateIndex( fieldName ) );
        } else {
            try {
                extractor = this.classFieldExtractorCache.getExtractor( ((ClassObjectType) objectType).getClassType(),
                                                                        fieldName );
            } catch ( final RuntimeDroolsException e ) {
                this.errors.add( new RuleError( this.rule,
                                                descr,
                                                e,
                                                "Unable to create Field Extractor for '" + fieldName + "'" ) );
            }
        }

        return extractor;
    }

    private Evaluator getEvaluator(final BaseDescr descr,
                                   final ValueType valueType,
                                   final String evaluatorString) {

        final Evaluator evaluator = valueType.getEvaluator( Operator.determineOperator( evaluatorString ) );

        if ( evaluator == null ) {
            this.errors.add( new RuleError( this.rule,
                                            descr,
                                            null,
                                            "Unable to determine the Evaluator for  '" + valueType + "' and '" + evaluatorString + "'" ) );
        }

        return evaluator;
    }

    private List[] getUsedIdentifiers(final BaseDescr descr,
                                      final String text) {
        List[] usedIdentifiers = null;
        try {
            usedIdentifiers = this.analyzer.analyzeExpression( text,
                                                               new Set[]{this.declarations.keySet(), this.pkg.getGlobals().keySet()} );
        } catch ( final Exception e ) {
            this.errors.add( new RuleError( this.rule,
                                            descr,
                                            null,
                                            "Unable to determine the used declarations" ) );
        }
        return usedIdentifiers;
    }

    private List[] getUsedCIdentifiers(final BaseDescr descr,
                                       final String text) {
        List[] usedIdentifiers = null;
        try {
            usedIdentifiers = this.analyzer.analyzeBlock( text,
                                                          new Set[]{this.declarations.keySet(), this.pkg.getGlobals().keySet()} );
        } catch ( final Exception e ) {
            this.errors.add( new RuleError( this.rule,
                                            descr,
                                            null,
                                            "Unable to determine the used declarations" ) );
        }
        return usedIdentifiers;
    }

    static class ColumnCounter {
        // we start with -1 so that we can ++this.value - otherwise the first element has a lower value than the second in an 'or'
        private int value = -1;

        public int getNext() {
            return ++this.value;
        }
    }
}