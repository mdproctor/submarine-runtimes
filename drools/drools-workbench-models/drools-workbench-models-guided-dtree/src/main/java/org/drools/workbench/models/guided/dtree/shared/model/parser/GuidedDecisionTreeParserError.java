/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
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
package org.drools.workbench.models.guided.dtree.shared.model.parser;

import java.util.ArrayList;
import java.util.List;

import org.drools.workbench.models.guided.dtree.shared.model.parser.messages.ParserMessage;
import org.kie.soup.commons.validation.PortablePreconditions;

public class GuidedDecisionTreeParserError {

    protected String originalRuleName;
    protected String originalDrl;
    protected List<ParserMessage> messages = new ArrayList<ParserMessage>();

    public GuidedDecisionTreeParserError() {
        //Errai marshalling
    }

    public GuidedDecisionTreeParserError(final String originalRuleName,
                                         final String originalDrl,
                                         final List<ParserMessage> messages) {
        this.originalRuleName = PortablePreconditions.checkNotNull("originalRuleName",
                                                                   originalRuleName);
        this.originalDrl = PortablePreconditions.checkNotNull("originalDrl",
                                                              originalDrl);
        this.messages = PortablePreconditions.checkNotNull("messages",
                                                           messages);
    }

    public String getOriginalDrl() {
        return originalDrl;
    }

    public String getOriginalRuleName() {
        return originalRuleName;
    }

    public List<ParserMessage> getMessages() {
        return messages;
    }
}
