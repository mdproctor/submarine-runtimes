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

package org.drools.compiler.compiler.xml.rules;

import org.drools.compiler.lang.descr.ConnectiveDescr;
import org.drools.compiler.lang.descr.QualifiedIdentifierRestrictionDescr;
import org.drools.core.xml.BaseAbstractHandler;
import org.drools.core.xml.ExtensibleXmlParser;
import org.drools.core.xml.Handler;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


public class QualifiedIdentifierRestrictionHandler extends BaseAbstractHandler
    implements
    Handler {
    public QualifiedIdentifierRestrictionHandler() {
    }

    public Object start(final String uri,
                        final String localName,
                        final Attributes attrs,
                        final ExtensibleXmlParser parser) throws SAXException {
        parser.startElementBuilder( localName,
                                    attrs );

        String evaluator = attrs.getValue( "evaluator" );
        emptyAttributeCheck( localName, "evaluator", evaluator, parser );

        return evaluator.trim() + " ";
    }

    public Object end(final String uri,
                      final String localName,
                      final ExtensibleXmlParser parser) throws SAXException {
        final Element element = parser.endElementBuilder();
        final String expression =((org.w3c.dom.Text)element.getChildNodes().item( 0 )).getWholeText();

        emptyContentCheck( localName, expression, parser );

        ConnectiveDescr c = (ConnectiveDescr) parser.getParent();
        String s = ( (String) parser.getCurrent()) + expression;

        c.add( s );
        return null;      
    }

    public Class generateNodeFor() {
        return QualifiedIdentifierRestrictionDescr.class;
    }
}
