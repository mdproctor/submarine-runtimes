package org.drools.workbench.models.guided.template.backend;

import org.drools.compiler.kie.builder.impl.FormatConverter;
import org.junit.Test;

import static org.junit.Assert.*;

public class GuidedRuleTemplateConverterTest {

    @Test
    public void testZeroParameterConstructor() {
        //A zero parameter is essential for programmatic instantiation from FormatsManager
        final FormatConverter converter = new GuidedRuleTemplateConverter();
        assertNotNull( converter );
    }

}
