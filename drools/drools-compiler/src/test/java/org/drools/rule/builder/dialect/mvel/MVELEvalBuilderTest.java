package org.drools.rule.builder.dialect.mvel;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.drools.Cheese;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.base.ClassFieldExtractor;
import org.drools.base.ClassObjectType;
import org.drools.common.InternalFactHandle;
import org.drools.lang.descr.EvalDescr;
import org.drools.lang.descr.RuleDescr;
import org.drools.reteoo.ReteTuple;
import org.drools.rule.Pattern;
import org.drools.rule.Declaration;
import org.drools.rule.EvalCondition;
import org.drools.rule.Package;
import org.drools.rule.builder.BuildContext;
import org.drools.rule.builder.dialect.mvel.MVELEvalBuilder;
import org.drools.spi.DeclarationScopeResolver;
import org.drools.spi.FieldExtractor;

public class MVELEvalBuilderTest extends TestCase {

    public void setUp() {
    }

    public void testSimpleExpression() {
        final Package pkg = new Package( "pkg1" );
        final RuleDescr ruleDescr = new RuleDescr( "rule 1" );

        final InstrumentedBuildContent context = new InstrumentedBuildContent( pkg,
                                                                         ruleDescr );
        final InstrumentedDeclarationScopeResolver declarationResolver = new InstrumentedDeclarationScopeResolver();
        final FieldExtractor extractor = new ClassFieldExtractor( Cheese.class,
                                                                  "price" );
        final Pattern pattern = new Pattern( 0,
                                    new ClassObjectType( int.class ) );
        final Declaration declaration = new Declaration( "a",
                                                   extractor,
                                                   pattern );
        final Map map = new HashMap();
        map.put( "a",
                 declaration );
        declarationResolver.setDeclarations( map );
        context.setDeclarationResolver( declarationResolver );

        final EvalDescr evalDescr = new EvalDescr();
        evalDescr.setContent( "a == 10" );

        final MVELEvalBuilder builder = new MVELEvalBuilder();
        final EvalCondition eval = (EvalCondition) builder.build( context,
                                                            null,
                                                            null,
                                                            evalDescr );

        final RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        final WorkingMemory wm = ruleBase.newStatefulSession();

        final Cheese cheddar = new Cheese( "cheddar",
                                     10 );
        final InternalFactHandle f0 = (InternalFactHandle) wm.assertObject( cheddar );
        final ReteTuple tuple = new ReteTuple( f0 );

        assertTrue( eval.isAllowed( tuple,
                                    wm ) );

        cheddar.setPrice( 9 );
        wm.modifyObject( f0,
                         cheddar );
        assertFalse( eval.isAllowed( tuple,
                                     wm ) );
    }

    public static class InstrumentedDeclarationScopeResolver extends DeclarationScopeResolver {
        private Map declarations;

        public InstrumentedDeclarationScopeResolver() {
            super( null );
        }

        public void setDeclarations(final Map map) {
            this.declarations = map;
        }

        public Map getDeclarations() {
            return this.declarations;
        }
    }

    public static class InstrumentedBuildContent extends BuildContext {
        private DeclarationScopeResolver declarationScopeResolver;

        public InstrumentedBuildContent(final Package pkg,
                                        final RuleDescr ruleDescr) {
            super( pkg,
                   ruleDescr );
        }

        public void setDeclarationResolver(final DeclarationScopeResolver declarationScopeResolver) {
            this.declarationScopeResolver = declarationScopeResolver;
        }

        public DeclarationScopeResolver getDeclarationResolver() {
            return this.declarationScopeResolver;
        }

    }

}
