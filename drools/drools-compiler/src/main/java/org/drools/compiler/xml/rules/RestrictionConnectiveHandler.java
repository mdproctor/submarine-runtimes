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

package org.drools.compiler.xml.rules;

import java.util.HashSet;

import org.drools.lang.descr.FieldConstraintDescr;
import org.drools.lang.descr.LiteralRestrictionDescr;
import org.drools.lang.descr.QualifiedIdentifierRestrictionDescr;
import org.drools.lang.descr.RestrictionConnectiveDescr;
import org.drools.lang.descr.ReturnValueRestrictionDescr;
import org.drools.lang.descr.VariableRestrictionDescr;
import org.drools.xml.BaseAbstractHandler;
import org.drools.xml.ExtensibleXmlParser;
import org.drools.xml.Handler;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class RestrictionConnectiveHandler extends BaseAbstractHandler
    implements
    Handler {

    public final static String AND = "and-restriction-connective";
    public final static String OR  = "or-restriction-connective";

   public RestrictionConnectiveHandler() {
        if ( (this.validParents == null) && (this.validPeers == null) ) {
            this.validParents = new HashSet();
            this.validParents.add( FieldConstraintDescr.class );

            this.validPeers = new HashSet();
            this.validPeers.add( null );

            this.validPeers.add( LiteralRestrictionDescr.class );
            this.validPeers.add( ReturnValueRestrictionDescr.class );
            this.validPeers.add( VariableRestrictionDescr.class );
            this.validPeers.add( RestrictionConnectiveDescr.class );
            this.validPeers.add( QualifiedIdentifierRestrictionDescr.class );

            this.allowNesting = true;
        }
    }

    public Object start(final String uri,
                        final String localName,
                        final Attributes attrs,
                        final ExtensibleXmlParser parser) throws SAXException {
        parser.startElementBuilder( localName,
                                    attrs );

        RestrictionConnectiveDescr connectiveDescr = null;
        if ( localName.equals( RestrictionConnectiveHandler.OR ) ) {
            connectiveDescr = new RestrictionConnectiveDescr( RestrictionConnectiveDescr.OR );
        } else if ( localName.equals( RestrictionConnectiveHandler.AND ) ) {
            connectiveDescr = new RestrictionConnectiveDescr( RestrictionConnectiveDescr.AND );
        }

        return connectiveDescr;
    }

    public Object end(final String uri,
                      final String localName,
                      final ExtensibleXmlParser parser) throws SAXException {
        final Element element = parser.endElementBuilder();

        final RestrictionConnectiveDescr connectiveDescr = (RestrictionConnectiveDescr) parser.getCurrent();

        final Object obj = parser.getParent();

        if ( obj instanceof FieldConstraintDescr ) {
            final FieldConstraintDescr fieldConstriantDescr = (FieldConstraintDescr) obj;
            fieldConstriantDescr.addRestriction( connectiveDescr );
        } else if ( obj instanceof RestrictionConnectiveDescr ) {
            final RestrictionConnectiveDescr restconective = (RestrictionConnectiveDescr) obj;
            restconective.addRestriction( connectiveDescr );
        }

        return connectiveDescr;
    }

    public Class generateNodeFor() {
        return RestrictionConnectiveDescr.class;
    }
}
