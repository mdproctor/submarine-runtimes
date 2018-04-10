/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.kie.dmn.core.classloader;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNModel;
import org.kie.dmn.api.core.DMNResult;
import org.kie.dmn.api.core.DMNRuntime;
import org.kie.dmn.core.api.DMNFactory;
import org.kie.dmn.core.util.DMNRuntimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DMNClassloaderTest {
    public static final Logger LOG = LoggerFactory.getLogger(DMNClassloaderTest.class);

    @Test
    public void testClassloaderFunctionInvocation() {
        final String javaSource = "package com.acme.functions;\n" +
                                  "\n" +
                                  "import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;\n" +
                                  "import java.math.BigDecimal;\n" +
                                  "import java.util.List;\n" +
                                  "\n" +
                                  "public class StandardDeviation {\n" +
                                  "\n" +
                                  "    public static BigDecimal std( List<Number> values ) {\n" +
                                  "        DescriptiveStatistics stats = new DescriptiveStatistics();\n" +
                                  "        for( Number value : values ) {\n" +
                                  "            stats.addValue( value.doubleValue() );\n" +
                                  "        }\n" +
                                  "        return new BigDecimal( stats.getStandardDeviation() );\n" +
                                  "    }\n" +
                                  "}";

        final KieServices ks = KieServices.Factory.get();
        
        ReleaseId kjarReleaseId = ks.newReleaseId("org.kie.dmn.core.classloader", "testClassloaderFunctionInvocation", UUID.randomUUID().toString());
        ReleaseId commonsMathGAV = ks.newReleaseId("org.apache.commons", "commons-math3", "3.6.1");

        final KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write("src/main/java/com/acme/functions/StandardDeviation.java", javaSource);
        kfs.write(ks.getResources().newClassPathResource("Standard Deviation.dmn", this.getClass()));
        kfs.writePomXML(getPom(kjarReleaseId, commonsMathGAV));

        KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
        assertTrue(kieBuilder.getResults().getMessages().toString(), kieBuilder.getResults().getMessages().isEmpty());

        final KieContainer container = ks.newKieContainer(kjarReleaseId);
        DMNRuntime runtime = container.newKieSession().getKieRuntime(DMNRuntime.class);
        DMNModel dmnModel = runtime.getModel("http://www.trisotech.com/dmn/definitions/_48c4b6e2-25da-44bc-97b2-1e842ff28c71", "Standard Deviation");
        assertThat(dmnModel, notNullValue());
        assertThat(DMNRuntimeUtil.formatMessages(dmnModel.getMessages()), dmnModel.hasErrors(), is(false));

        DMNContext context = DMNFactory.newContext();
        context.set("Values", Arrays.asList(new BigDecimal(1), new BigDecimal(2), new BigDecimal(3)));

        DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);
        LOG.info("{}", dmnResult);
        assertThat(DMNRuntimeUtil.formatMessages(dmnResult.getMessages()), dmnResult.hasErrors(), is(false));

        DMNContext result = dmnResult.getContext();
        assertThat(result.get("Standard Deviation"), is(new BigDecimal(1)));
    }

    protected String getPom(ReleaseId releaseId, ReleaseId... dependencies) {
        String pom =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                     "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                     "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" +
                     "  <modelVersion>4.0.0</modelVersion>\n" +
                     "\n" +
                     "  <groupId>" + releaseId.getGroupId() + "</groupId>\n" +
                     "  <artifactId>" + releaseId.getArtifactId() + "</artifactId>\n" +
                     "  <version>" + releaseId.getVersion() + "</version>\n" +
                     "\n";
        if (dependencies != null && dependencies.length > 0) {
            pom += "<dependencies>\n";
            for (ReleaseId dep : dependencies) {
                pom += "<dependency>\n";
                pom += "  <groupId>" + dep.getGroupId() + "</groupId>\n";
                pom += "  <artifactId>" + dep.getArtifactId() + "</artifactId>\n";
                pom += "  <version>" + dep.getVersion() + "</version>\n";
                pom += "</dependency>\n";
            }
            pom += "</dependencies>\n";
        }
        pom += "</project>";
        return pom;
    }
}
