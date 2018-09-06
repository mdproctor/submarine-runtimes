/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.scorecards;

import org.drools.compiler.compiler.ScoreCardFactory;
import org.drools.compiler.compiler.ScoreCardProvider;
import org.drools.core.definitions.InternalKnowledgePackage;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.core.ruleunit.RuleUnitDescr;
import org.drools.core.ruleunit.RuleUnitRegistry;
import org.drools.scorecards.example.Applicant;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.pmml.PMML4Data;
import org.kie.api.pmml.PMML4Result;
import org.kie.api.pmml.PMMLRequestData;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.DataSource;
import org.kie.api.runtime.rule.RuleUnit;
import org.kie.api.runtime.rule.RuleUnitExecutor;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.builder.ScoreCardConfiguration;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.kie.internal.utils.KieHelper;
import org.kie.pmml.pmml_4_2.PMML4ExecutionHelper;
import org.kie.pmml.pmml_4_2.PMML4ExecutionHelper.PMML4ExecutionHelperFactory;
import org.kie.pmml.pmml_4_2.model.PMML4UnitImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

@Ignore
public class ScorecardProviderTest {

    private static String drl;
    private ScoreCardProvider scoreCardProvider;

    @Before
    public void setUp() {
        scoreCardProvider = ScoreCardFactory.getScoreCardProvider();
        assertNotNull(scoreCardProvider);
    }

    @Test
    public void testDrlWithoutSheetName() {
        InputStream is = ScorecardProviderTest.class.getResourceAsStream("/scoremodel_c.xls");
        assertNotNull(is);

        ScoreCardConfiguration scconf = KnowledgeBuilderFactory.newScoreCardConfiguration();
        KieBase kbase = scoreCardProvider.getKieBaseFromInputStream(is, scconf);
        assertNotNull(kbase);
    }

    @Test
    public void testDrlWithSheetName() {
        InputStream is = ScorecardProviderTest.class.getResourceAsStream("/scoremodel_c.xls");
        assertNotNull(is);

        ScoreCardConfiguration scconf = KnowledgeBuilderFactory.newScoreCardConfiguration();
        scconf.setWorksheetName("scorecards");
        KieBase kbase = scoreCardProvider.getKieBaseFromInputStream(is, scconf);
        assertNotNull(kbase);
    }

    @Test
    public void testKnowledgeBaseWithExection() {
        InputStream is = ScorecardProviderTest.class.getResourceAsStream("/scoremodel_c.xls");
        ScoreCardConfiguration scconf = KnowledgeBuilderFactory.newScoreCardConfiguration();
        scconf.setWorksheetName("scorecards");

        KieBase kbase = scoreCardProvider.getKieBaseFromInputStream(is, scconf);
        assertNotNull(kbase);
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind(kbase);
        assertNotNull(executor);

        DataSource<PMMLRequestData> data = executor.newDataSource("request");
        DataSource<PMML4Result> resultData = executor.newDataSource("results");
        DataSource<PMML4Data> pmmlData = executor.newDataSource("pmmlData");

        PMMLRequestData request = new PMMLRequestData("123", "SampleScore");
        request.addRequestParam("age", 33.0);
        request.addRequestParam("occupation", "PROGRAMMER");
        request.addRequestParam("residenceState", "KN");
        request.addRequestParam("validLicense", true);
        data.insert(request);

        PMML4Result resultHolder = new PMML4Result("123");
        resultData.insert(resultHolder);

        List<String> possiblePackages = calculatePossiblePackageNames("Sample Score", "org.drools.scorecards.example");
        Class<? extends RuleUnit> ruleUnitClass = getStartingRuleUnit("RuleUnitIndicator", (InternalKnowledgeBase) kbase, possiblePackages);
        int executions = executor.run(ruleUnitClass);
        assertTrue(executions > 0);

        Double calculatedScore = resultHolder.getResultValue("CalculatedScore", "value", Double.class).orElse(null);
        assertEquals(56.0, calculatedScore, 1e-6);
    }

    @Test
    public void testDrlGenerationWithExternalTypes() {

        Resource resource = ResourceFactory.newClassPathResource("scoremodel_externalmodel.xls");
        ScoreCardConfiguration scconf = KnowledgeBuilderFactory.newScoreCardConfiguration();
        scconf.setWorksheetName("scorecards");
        scconf.setUsingExternalTypes(true);
        resource.setConfiguration(scconf);
        resource.setResourceType(ResourceType.SCARD);
        KieBase kbase = new KieHelper().addResource(resource, ResourceType.SCARD).build();
        assertNotNull(kbase);
        RuleUnitExecutor executor = RuleUnitExecutor.create().bind(kbase);

        DataSource<PMMLRequestData> data = executor.newDataSource("request");
        DataSource<PMML4Result> resultData = executor.newDataSource("results");
        DataSource<PMML4Data> pmmlData = executor.newDataSource("pmmlData");
        DataSource<Applicant> applicantData = executor.newDataSource("externalBeanApplicant");

        PMMLRequestData request = new PMMLRequestData("123", "Sample Score");
        Applicant applicant = new Applicant();
        applicant.setAge(33.0);
        applicant.setOccupation("PROGRAMMER");
        applicant.setResidenceState("AP");
        applicant.setValidLicense(true);

        PMML4Result resultHolder = new PMML4Result("123");
        List<String> possiblePackages = calculatePossiblePackageNames("Sample Score", "org.drools.scorecards.example");
        Class<? extends RuleUnit> ruleUnitClass = getStartingRuleUnit("RuleUnitIndicator", (InternalKnowledgeBase) kbase, possiblePackages);
        assertNotNull(ruleUnitClass);

        resultData.insert(resultHolder);
        data.insert(request);
        applicantData.insert(applicant);

        int count = executor.run(ruleUnitClass);
        assertTrue(count > 0);

        Double calculatedScore = resultHolder.getResultValue("Scorecard__calculatedScore", "value", Double.class).orElse(null);
        assertEquals(36.0, calculatedScore, 1e-6);
    }

    protected Class<? extends RuleUnit> getStartingRuleUnit(String startingRule, InternalKnowledgeBase ikb, List<String> possiblePackages) {
        RuleUnitRegistry unitRegistry = ikb.getRuleUnitRegistry();
        Map<String, InternalKnowledgePackage> pkgs = ikb.getPackagesMap();
        RuleImpl ruleImpl = null;
        for (String pkgName : possiblePackages) {
            if (pkgs.containsKey(pkgName)) {
                InternalKnowledgePackage pkg = pkgs.get(pkgName);
                ruleImpl = pkg.getRule(startingRule);
                if (ruleImpl != null) {
                    RuleUnitDescr descr = unitRegistry.getRuleUnitFor(ruleImpl).orElse(null);
                    if (descr != null) {
                        return descr.getRuleUnitClass();
                    }
                }
            }
        }
        return null;
    }

    protected List<String> calculatePossiblePackageNames(String modelId, String... knownPackageNames) {
        List<String> packageNames = new ArrayList<>();
        String javaModelId = modelId.replaceAll("\\s", "");
        if (knownPackageNames != null && knownPackageNames.length > 0) {
            for (String knownPkgName : knownPackageNames) {
                packageNames.add(knownPkgName + "." + javaModelId);
            }
        }
        String basePkgName = PMML4UnitImpl.DEFAULT_ROOT_PACKAGE + "." + javaModelId;
        packageNames.add(basePkgName);
        return packageNames;
    }
}