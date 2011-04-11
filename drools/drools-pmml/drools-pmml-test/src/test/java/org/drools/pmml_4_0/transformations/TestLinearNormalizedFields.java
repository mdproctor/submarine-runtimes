package org.drools.pmml_4_0.transformations;


import org.drools.definition.type.FactType;
import org.drools.pmml_4_0.DroolsAbstractPMMLTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by IntelliJ IDEA.
 * User: davide
 * Date: 11/12/10
 * Time: 10:11 PM
 *
 * PMML Test : Focus on the DataDictionary section
 */
public class TestLinearNormalizedFields extends DroolsAbstractPMMLTest {

    private static final boolean VERBOSE = true;
    private static final String source = "test_derived_fields_linearNorm.xml";
    private static final String packageName = "org.drools.pmml_4_0.test";

    @Before
    public void setUp() throws Exception {

        setKSession(getModelSession(source,VERBOSE));
        setKbase(getKSession().getKnowledgeBase());
    }

    @Test
    public void testDerivedTypesLinearNormMapMissing() throws Exception {

        //new PMML4Wrapper().getPmml().getTransformationDictionary().getDerivedField().get(0).getNormContinuous().getOutliers().value()


        FactType age = getKbase().getFactType(packageName,"Age");
        FactType age1 = getKbase().getFactType(packageName,"Age_norm");
        FactType age2 = getKbase().getFactType(packageName,"Age_norm2");
        FactType age3 = getKbase().getFactType(packageName,"Age_norm3");

        assertNotNull(getKSession().getWorkingMemoryEntryPoint("in_Age"));
        assertNull(getKSession().getWorkingMemoryEntryPoint("in_Age_mis"));
        assertNull(getKSession().getWorkingMemoryEntryPoint("in_Age_norm"));

        //value is "missing" for age, so should be mapped by the mapMissingTo policy
        getKSession().getWorkingMemoryEntryPoint("in_Age").insert(-1);
        getKSession().fireAllRules();


        checkFirstDataFieldOfTypeStatus(age,true,true, null,-1);

        checkFirstDataFieldOfTypeStatus(age1,true,false, null,0.0);

        checkFirstDataFieldOfTypeStatus(age2,true,false, null,-931.0);

        checkFirstDataFieldOfTypeStatus(age3,true,false, null,789.0);


    }



    @Test
    public void testDerivedTypesLinearNormOutliers() throws Exception {

        //new PMML4Wrapper().getPmml().getTransformationDictionary().getDerivedField().get(0).getNormContinuous().getOutliers().value()


        FactType age = getKbase().getFactType(packageName,"Age");
        FactType age1 = getKbase().getFactType(packageName,"Age_norm");
        FactType age2 = getKbase().getFactType(packageName,"Age_norm2");
        FactType age3 = getKbase().getFactType(packageName,"Age_norm3");

        //value is an outlier
        getKSession().getWorkingMemoryEntryPoint("in_Age").insert(-100);
        getKSession().fireAllRules();

        checkFirstDataFieldOfTypeStatus(age,true,false, null,-100);

        checkFirstDataFieldOfTypeStatus(age1,true,false, null,0.0);

        checkFirstDataFieldOfTypeStatus(age2,true,true, null,0.0);

        checkFirstDataFieldOfTypeStatus(age3,true,false, null,-1.25);


        refreshKSession();


        //value is an outlier
        getKSession().getWorkingMemoryEntryPoint("in_Age").insert(1000);
        getKSession().fireAllRules();


        checkFirstDataFieldOfTypeStatus(age,true,false, null,1000);

        checkFirstDataFieldOfTypeStatus(age1,true,false, null,2.0);

        checkFirstDataFieldOfTypeStatus(age2,true,true, null,0.0);

        checkFirstDataFieldOfTypeStatus(age3,true,false, null,12.5);

    }


    @Test
    public void testDerivedTypesLinearNormInterpolation() throws Exception {

        //new PMML4Wrapper().getPmml().getTransformationDictionary().getDerivedField().get(0).getNormContinuous().getOutliers().value()

        FactType age = getKbase().getFactType(packageName,"Age");
        FactType age1 = getKbase().getFactType(packageName,"Age_norm");

        getKSession().getWorkingMemoryEntryPoint("in_Age").insert(30);
        getKSession().fireAllRules();

        checkFirstDataFieldOfTypeStatus(age,true,false, null,30);

        checkFirstDataFieldOfTypeStatus(age1,true,false, null,0.375);



        refreshKSession();

        getKSession().getWorkingMemoryEntryPoint("in_Age").insert(90);
        getKSession().fireAllRules();


        checkFirstDataFieldOfTypeStatus(age,true,false, null,90);

        checkFirstDataFieldOfTypeStatus(age1,true,false, null,1.5);


    }








}
