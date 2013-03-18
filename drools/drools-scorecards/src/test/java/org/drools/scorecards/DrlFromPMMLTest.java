package org.drools.scorecards;

import org.dmg.pmml.pmml_4_1.descr.PMML;
import org.junit.Before;
import org.junit.Test;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.definition.type.FactType;
import org.kie.internal.io.ResourceFactory;
import org.kie.io.ResourceType;
import org.kie.runtime.StatefulKnowledgeSession;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.drools.scorecards.ScorecardCompiler.DrlType.INTERNAL_DECLARED_TYPES;

public class DrlFromPMMLTest {

    private static String drl;

    @Before
    public void setUp() throws Exception {
        ScorecardCompiler scorecardCompiler = new ScorecardCompiler(INTERNAL_DECLARED_TYPES);
        if (scorecardCompiler.compileFromExcel(PMMLDocumentTest.class.getResourceAsStream("/scoremodel_c.xls")) ) {
            PMML pmmlDocument = scorecardCompiler.getPMMLDocument();
            assertNotNull(pmmlDocument);
            drl = scorecardCompiler.getDRL();
        } else {
            fail("failed to parse scoremodel Excel.");
        }
    }

    @Test
    public void testDrlNoNull() throws Exception {
        assertNotNull(drl);
        assertTrue(drl.length() > 0);
        //System.out.println(drl);
    }

    @Test
    public void testPackage() throws Exception {
        assertTrue(drl.contains("package org.drools.scorecards.example"));
    }

    @Test
    public void testRuleCount() throws Exception {
        assertEquals(13, StringUtil.countMatches(drl, "rule \""));
    }

    @Test
    public void testImports() throws Exception {
        assertEquals(5, StringUtil.countMatches(drl, "import "));
    }

    @Test
    public void testDRLExecution() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        kbuilder.add( ResourceFactory.newByteArrayResource(drl.getBytes()), ResourceType.DRL);
        for (KnowledgeBuilderError error : kbuilder.getErrors()){
            System.out.println(error.getMessage());
        }
        assertFalse( kbuilder.hasErrors() );

        //BUILD RULEBASE
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );

        //NEW WORKING MEMORY
        StatefulKnowledgeSession session = kbase.newStatefulKnowledgeSession();
        FactType scorecardType = kbase.getFactType( "org.drools.scorecards.example","SampleScore" );
        DroolsScorecard scorecard = (DroolsScorecard) scorecardType.newInstance();
        scorecardType.set(scorecard, "age", 10);
        session.insert( scorecard );
        session.fireAllRules();
        session.dispose();
        //occupation = 5, age = 25, validLicence -1
        assertEquals(29.0,scorecard.getCalculatedScore());

        session = kbase.newStatefulKnowledgeSession();
        scorecard = (DroolsScorecard) scorecardType.newInstance();
        scorecardType.set(scorecard, "occupation", "SKYDIVER");
        scorecardType.set(scorecard, "age", 0);
        session.insert( scorecard );
        session.fireAllRules();
        session.dispose();
        //occupation = -10, age = +10, validLicense = -1;
        assertTrue(-1 == scorecard.getCalculatedScore());

        session = kbase.newStatefulKnowledgeSession();
        scorecard = (DroolsScorecard) scorecardType.newInstance();
        scorecardType.set(scorecard, "residenceState", "AP");
        scorecardType.set(scorecard, "occupation", "TEACHER");
        scorecardType.set(scorecard, "age", 20);
        scorecardType.set(scorecard, "validLicense", true);
        session.insert( scorecard );
        session.fireAllRules();
        session.dispose();
        //occupation = +10, age = +40, state = -10, validLicense = 1
        assertEquals(41.0,scorecard.getCalculatedScore());
    }


}
