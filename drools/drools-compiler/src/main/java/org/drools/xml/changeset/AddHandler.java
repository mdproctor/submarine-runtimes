package org.drools.xml.changeset;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.drools.ChangeSet;
import org.drools.builder.ResourceType;
import org.drools.io.Resource;
import org.drools.io.impl.ChangeSetImpl;
import org.drools.io.impl.ClassPathResource;
import org.drools.io.impl.KnowledgeResource;
import org.drools.io.impl.UrlResource;
import org.drools.util.StringUtils;
import org.drools.xml.BaseAbstractHandler;
import org.drools.xml.ExtensibleXmlParser;
import org.drools.xml.Handler;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AddHandler extends BaseAbstractHandler
    implements
    Handler {
    
    public AddHandler() {
        if ( (this.validParents == null) && (this.validPeers == null) ) {
            this.validParents = new HashSet(1);
            this.validParents.add( ChangeSet.class );

            this.validPeers = new HashSet(2);
            this.validPeers.add( null );
            this.validPeers.add( Collection.class );

            this.allowNesting = true;
        }        
    }    
    
    public Object start(String uri,
                        String localName,
                        Attributes attrs,
                        ExtensibleXmlParser parser) throws SAXException {
        parser.startElementBuilder( localName,
                                    attrs );      
        
        final ChangeSet changeSet = (ChangeSet) parser.getParent();          
        
        return new ArrayList();
    }

    public Object end(String uri,
                      String localName,
                      ExtensibleXmlParser parser) throws SAXException {
        final Element element = parser.endElementBuilder();
        
        final ChangeSetImpl changeSet = (ChangeSetImpl) parser.getParent();
        final Collection add = ( Collection ) parser.getCurrent();
        changeSet.setResourcesAdded( add );
        return add;
    }

    
    public Class< ? > generateNodeFor() {
        return Collection.class;
    }

}
