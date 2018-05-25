/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.pmml.pmml_4_2;


import org.dmg.pmml.pmml_4_2.descr.NeuralLayer;
import org.dmg.pmml.pmml_4_2.descr.NeuralNetwork;
import org.dmg.pmml.pmml_4_2.descr.PMML;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PMMLGenerationTest {

    private static int hiddenSize = 25;

    private static String[] inputfieldNames = { "PIP", "PEEP", "RATE", "IT", "Ph", "CO2", "PaO2", "FIO2" };
    private static String[] outputfieldNames = { "sPIP", "sPEEP", "sRATE", "sIT", "sFIO2" };

    private static double[] inputMeans = { 17.6315789, 4.1578947, 14.7456140, 0.3947368, 7.3273947, 42.0070175, 71.4736842, 32.4385965 };
    private static double[] inputStds = { 2.91247897, 0.71121170, 5.72652793, 0.02432125, 0.06110757, 8.60365964, 26.60059568, 15.16577136 };

    private static double[] outputMeans = { 13.0526316, 3.2192982, 11.7017544, 0.3061404, -0.2280702 };
    private static double[] outputStds = { 7.29509728, 1.83263808, 7.92546007, 0.16376926, 0.48032231 };

    private static double[] weights = {
            -4.00700156253505, 0.0637896834333224,-0.185247475091626, -3.17637952572042,1.18908693332086,-0.408949791280556,-1.58222591853641,0.0880955577324421,
            -3.24380222825235,-2.81645356461552,-1.56248954588717,2.00951419188215,-0.176679677403268,-2.03768002271404,-0.00315771856963038,-0.670310386080313,
            -1.63966251641718,-1.43340266213738,-3.04430452320322,-1.81129903450145,0.49391249566342,-4.7835283037015,-0.222626324552025,-1.54447165369249,
            -0.525943630847341,1.47492275699634,0.152578165450758,2.35986057558872,-1.05221852353660,0.237732107776662,-0.0115032349263444,0.572104953807905,
            0.231591141419976,-0.584880886387377,-0.0848817828346233,-0.552695499553553,0.882609834216733,0.134474993246233,0.0918652385138947,-2.12232942726926,
            0.1656073710734,-0.353494782823987,-0.08324042176918,-0.527195353274182,0.227600878221267,-1.45791077774567,-0.918560032308313,-0.814128534661431,
            -2.30970406253270,1.02542102858623,-0.824667894044815,-2.35246348455269,-0.163220287349661,-2.31656621337075,-0.867313569216432,1.27122182873461,
            -2.02171434437964,-0.880858370132031,0.258272947767333,0.625765721981516,0.776997702928832,0.209765368074353,-0.579408068942708,-1.28418583831291,
            -0.88153172388571,-1.01549520965570,1.33008218639820,-1.54487603328681,-0.825984769682127,-1.88201692161639,0.39432440365179,-1.21766941778641,
            -0.569332838205072,0.246747050764762,2.37797309703966,-2.41576930614572,-0.786527673741461,-1.72921574120303,0.362814422360161,1.14565461421768,
            0.127149231051080,2.73802623552525,1.18829068043564,0.702010303795689,0.510190585893527,0.384765773554306,0.955515054615039,2.08093524006632,
            0.634183561537118,-0.131593669369605,1.81181084769580,2.54733359155439,-0.809523655025315,0.199321822085396,2.37016645797045,2.15338076126612,
            1.67013039291342,0.679087323907235,-2.48319491510889,1.42495808763975,0.0650451628217988,-1.37979434908341,1.36978485491050,1.09225442605225,
            1.60989638787400,-1.62206243023003,-2.35385312772267,0.659995665743257,0.352600177513320,-2.07903249243001,-0.234305343349567,-0.621368265166557,
            -0.038355360282161,-0.301351837382778,0.205335473337566,-2.10633817322392,-2.17499755079766,-1.32436802560398,0.0106644888129735,-2.19141919870845,
            1.89544719088237,0.0501935061928684,-0.371741106064983,0.409929470054576,1.03210465644878,0.0510703603593844,-0.214425665450412,0.971490774923065,
            0.404335399006827,-2.29835695324223,-0.0885212334854117,-1.18537924967001,-0.810906985907254,3.20399084909024,-0.651875730040673,1.48426656374084,
            2.34711839836612,0.0925635370707155,-1.68400474299191,-0.774780710970975,1.76593968311233,-0.317076499517355,-2.18420285068536,-1.12312012310689,-
            0.540704801935574,-0.788718955784866,-0.962573815062097,-0.98063977825017,2.40484766510775,0.408948977187233,-2.36623623096439,-1.86722537590280,
            1.32956458976559,0.804589960818565,-0.97164799955853,-1.25511437036155,0.341870089288867,0.437894777847756,0.910338607441386,-0.875880762092548,
            1.02616836032066,-0.0552206283879658,-1.72752835362659,-3.68431628323605,0.936551886190671,-3.36297168177941,-0.104322911197235,-0.512458815211904,
            -1.12303061438752,-0.100249764265824,0.718258907355593,1.40613466151010,-1.59481682304100,-0.597208810513953,-1.28389580242927,-0.24451469899627,
            0.169056022739649,-1.79319654599864,-0.649910937079946,-0.383051477938183,1.95964353199950,-2.14410359923500,-0.065685628591909,-0.514082688415291,
            1.59764863366977,-0.690194361689913,-1.5670738742003,0.827681055771942,0.28224076748736,1.10565596028191,0.418474097964395,-2.53401256620715,
            1.29120167425601,0.232549665077627,-1.21340000021742,1.02698400561427,-1.38589465594088,1.74568671304676,-0.375835400626885,-1.16267471202408,
            -0.81638151939839,0.783376263922674,0.769928516329944,-0.116794004128527,-1.67066228983682e-05,-0.520205014653015,0.49331648350307,0.956604277738648,
            -1.93329442310389,-1.09656094871258,0.98739120867853,1.33375870819822,0.218821520728325,-0.973856194828143,-2.40353189768241,-0.719559579024822,
            1.20961180107881,-0.797646204062308,-1.38269416847062,0.430546716208746,2.01927329054716,2.52158648388836,-1.28282462258364,-1.62742221018806,
            -0.692367668949941,0.570622886983205,-2.61181349586745,-0.5493856854383,-2.12265284293479,1.30084597161969,0.791390882570713,1.40356703985260,
            -1.67304695770637,-0.383796635525781,-2.03506080384495,1.5830095908858,-0.160604790876364,-1.82916747633699,0.45320256974979,0.990823280624844,
            0.736828658198483,0.414791550114501,1.74068634516654,0.550014294887989,1.49334996826695,-0.891470086676236,-1.69508452052686,0.616461790431409,
            -1.38677991189080,0.164499085087166,-1.18074518975828,-0.682741631384268,-2.6669761579951,-0.703663382048497,-2.50853926773157,0.161209824700034,
            -0.527191615664767,0.841422450243487,-1.07934783244340,-0.0681910646170356,-1.3647365516092,2.32500244840838,-0.47653772562508,-0.94705414416777,
            0.617663615485084,-0.806779089527299,1.15372504093210,1.05458347923863,0.725834829041935,1.01142234766101,2.17475655117681,-0.662478639085692,
            -0.596418557818017,0.715999688055482,0.510372309433393,0.134840212167601,-1.47414995506487,1.50278285247751,-1.02480818229022,-0.555395756048918,
            -1.13030021471343,0.279297222671549,-1.27095923952771,0.419914031006691,-1.11581695628827,-0.576049193983673,-1.06680915692313,0.73733522336104,
            -0.578227194971188,-0.581584647247074,0.568827407850499,0.396164250072913,0.537778873533842,0.541008891070337,0.332648119979325,0.162800824870336,
            0.522578407976417,-0.293536084781949,-0.391107213750048,0.52680955852709,-0.213456795966614,-0.00737500415832278,-0.59065412084179,-0.52761164898742,
            -3.12079555879807,-1.03628155980983,-2.52580352437000,2.58284368743829,0.40202759979356,1.65785673762628,-1.83252216847724,-0.776076205560857,
            -2.30792954223526,1.82489085352601,-0.6189067140747,-1.87382927152408,0.843202129051948,1.12594744150651,0.874237125602241,0.76678752905231,
            1.47551868447129,0.544507946925621,1.90245389834316,-0.534694009799557,-1.41813427239476,0.604352950135843,-1.00372571346059,-0.0492653273232236,
            -1.37143335312230,1.44444615887514,1.09467145907631,-3.99022706711794,0.647626384969195,1.32083134900324,0.397018086266108,1.58082538813443,
            -1.02110125651510,1.41646267246847,-0.290756929310209,1.92759572861955,-3.68565037889975,-0.824471549951029,2.67156242101891,1.22901997089443,
            -2.10918122576688,2.79047975319418,-1.14580201442335,-2.27048788903191,-0.337686898709039,-2.73148570185959,-0.227748207022961,-2.21713851520726,
            2.18817128001435,-2.69291722561178,2.73923309561558 };

    private static String modelName = "SmartVent";


    @Test
    public void testNNGenration() {
        PMML net = PMMLGeneratorUtils.generateSimpleNeuralNetwork( modelName,
                                                                   inputfieldNames, outputfieldNames,
                                                                   inputMeans, inputStds,
                                                                   outputMeans, outputStds,
                                                                   hiddenSize,
                                                                   weights );
        assertNotNull( net );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assertTrue( PMMLGeneratorUtils.streamPMML( net, baos ) );
        ByteArrayInputStream bais = new ByteArrayInputStream( baos.toByteArray() );
        PMML4Compiler compiler = new PMML4Compiler();

        SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
        try {
            Schema schema = sf.newSchema( Thread.currentThread().getContextClassLoader().getResource( compiler.SCHEMA_PATH ) );
            schema.newValidator().validate( new StreamSource( bais ) );
        } catch ( SAXException e ) {
            fail( e.getMessage() );
        } catch ( IOException e ) {
            fail( e.getMessage() );
        }

        PMML net2 = null;
        try {
            bais.reset();
            JAXBContext ctx = JAXBContext.newInstance( PMML.class.getPackage().getName() );
            net2 = (PMML) ctx.createUnmarshaller().unmarshal( bais );
        } catch ( JAXBException e ) {
            e.printStackTrace();
        }

        assertNotNull( net2 );
        assertEquals( inputfieldNames.length + outputfieldNames.length, net2.getDataDictionary().getDataFields().size() );
        assertEquals( net.getDataDictionary().getDataFields().size(), net2.getDataDictionary().getDataFields().size() );

        NeuralNetwork n1 = (NeuralNetwork) net.getAssociationModelsAndBaselineModelsAndClusteringModels().get( 0 );
        NeuralNetwork n2 = (NeuralNetwork) net2.getAssociationModelsAndBaselineModelsAndClusteringModels().get( 0 );

        assertEquals( n1.getExtensionsAndNeuralLayersAndNeuralInputs().size(), n2.getExtensionsAndNeuralLayersAndNeuralInputs().size() );
        assertEquals( 6, n2.getExtensionsAndNeuralLayersAndNeuralInputs().size() );

        NeuralLayer l1 = (NeuralLayer) n1.getExtensionsAndNeuralLayersAndNeuralInputs().get( 3 );
        NeuralLayer l2 = (NeuralLayer) n2.getExtensionsAndNeuralLayersAndNeuralInputs().get( 3 );

        assertEquals( l1.getNeurons().get( 4 ).getCons().get( 2 ).getWeight(), l2.getNeurons().get( 4 ).getCons().get( 2 ).getWeight(), 1e-9 );
        assertEquals( weights[ ( inputfieldNames.length + 1 ) * 4 + 3 ], l2.getNeurons().get( 4 ).getCons().get( 2 ).getWeight(), 1e-9 );
    }

}
