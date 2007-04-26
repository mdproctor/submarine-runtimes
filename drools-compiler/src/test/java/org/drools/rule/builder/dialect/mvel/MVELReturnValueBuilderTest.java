package org.drools.rule.builder.dialect.mvel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.codehaus.jfdi.interpreter.ClassTypeResolver;
import org.drools.Cheese;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.base.ClassFieldExtractor;
import org.drools.base.ClassFieldExtractorCache;
import org.drools.base.ClassObjectType;
import org.drools.base.ValueType;
import org.drools.base.evaluators.Operator;
import org.drools.common.InternalFactHandle;
import org.drools.common.InternalWorkingMemory;
import org.drools.compiler.DialectRegistry;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.lang.descr.ReturnValueRestrictionDescr;
import org.drools.lang.descr.RuleDescr;
import org.drools.reteoo.ReteTuple;
import org.drools.rule.Pattern;
import org.drools.rule.Declaration;
import org.drools.rule.Package;
import org.drools.rule.ReturnValueRestriction;
import org.drools.rule.PredicateConstraint.PredicateContextEntry;
import org.drools.rule.builder.RuleBuildContext;
import org.drools.rule.builder.dialect.java.DeclarationTypeFixer;
import org.drools.rule.builder.dialect.java.JavaDialect;
import org.drools.rule.builder.dialect.java.JavaExprAnalyzer;
import org.drools.rule.builder.dialect.java.KnowledgeHelperFixer;
import org.drools.spi.DeclarationScopeResolver;
import org.drools.spi.FieldExtractor;

public class MVELReturnValueBuilderTest extends TestCase {

    public void setUp() {
    }

    public void testSimpleExpression() {
        final Package pkg = new Package( "pkg1" );
        final RuleDescr ruleDescr = new RuleDescr( "rule 1" );

        DialectRegistry registry = new DialectRegistry(); 
        registry.addDialect( "default",
                                  new JavaDialect( pkg,
                                                   new PackageBuilderConfiguration(),
                                                   new ClassTypeResolver(),
                                                   new ClassFieldExtractorCache() ) );           
        final InstrumentedBuildContent context = new InstrumentedBuildContent( pkg,
                                                                               ruleDescr,
                                                                               registry );
        final InstrumentedDeclarationScopeResolver declarationResolver = new InstrumentedDeclarationScopeResolver();
        final FieldExtractor extractor = new ClassFieldExtractor( Cheese.class,
                                                                  "price" );
        final Pattern patternA = new Pattern( 0,
                                              new ClassObjectType( int.class ) );

        final Pattern patternB = new Pattern( 1,
                                              new ClassObjectType( int.class ) );

        final Declaration a = new Declaration( "a",
                                               extractor,
                                               patternA );
        final Declaration b = new Declaration( "b",
                                               extractor,
                                               patternB );

        final Map map = new HashMap();
        map.put( "a",
                 a );
        map.put( "b",
                 b );
        declarationResolver.setDeclarations( map );
        context.setDeclarationResolver( declarationResolver );

        final ReturnValueRestrictionDescr returnValueDescr = new ReturnValueRestrictionDescr( "=" );
        returnValueDescr.setContent( "a + b" );

        final MVELReturnValueBuilder builder = new MVELReturnValueBuilder();

        final List[] usedIdentifiers = new ArrayList[2];
        final List list = new ArrayList();
        usedIdentifiers[1] = list;

        final Declaration[] previousDeclarations = new Declaration[]{a, b};
        final Declaration[] localDeclarations = new Declaration[]{};
        final String[] requiredGlobals = new String[]{};

        final ReturnValueRestriction returnValue = new ReturnValueRestriction( extractor,
                                                                               previousDeclarations,
                                                                               localDeclarations,
                                                                               requiredGlobals,
                                                                               ValueType.PINTEGER_TYPE.getEvaluator( Operator.EQUAL ) );

        builder.build( context,
                       usedIdentifiers,
                       previousDeclarations,
                       localDeclarations,
                       returnValue,
                       returnValueDescr );

        final RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        final InternalWorkingMemory wm = (InternalWorkingMemory) ruleBase.newStatefulSession();

        final Cheese stilton = new Cheese( "stilton",
                                           10 );

        final Cheese cheddar = new Cheese( "cheddar",
                                           10 );
        final InternalFactHandle f0 = (InternalFactHandle) wm.assertObject( cheddar );
        ReteTuple tuple = new ReteTuple( f0 );

        final InternalFactHandle f1 = (InternalFactHandle) wm.assertObject( stilton );
        tuple = new ReteTuple( tuple,
                               f1 );

        final PredicateContextEntry predicateContext = new PredicateContextEntry();
        predicateContext.leftTuple = tuple;

        final Cheese brie = new Cheese( "brie",
                                        20 );
        assertTrue( returnValue.isAllowed( extractor,
                                           brie,
                                           tuple,
                                           wm ) );

        brie.setPrice( 18 );
        assertFalse( returnValue.isAllowed( extractor,
                                            brie,
                                            tuple,
                                            wm ) );
    }
}
