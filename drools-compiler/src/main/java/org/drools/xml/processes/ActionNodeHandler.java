package org.drools.xml.processes;

import org.drools.workflow.core.Node;
import org.drools.workflow.core.impl.DroolsConsequenceAction;
import org.drools.workflow.core.node.ActionNode;
import org.drools.xml.ExtensibleXmlParser;
import org.drools.xml.XmlDumper;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class ActionNodeHandler extends AbstractNodeHandler {

    protected Node createNode() {
        return new ActionNode();
    }

    public void handleNode(final Node node, final Element element, final String uri,
            final String localName, final ExtensibleXmlParser parser)
            throws SAXException {
        super.handleNode(node, element, uri, localName, parser);
        ActionNode actionNode = (ActionNode) node;
        String text = ((Text)element.getChildNodes().item( 0 )).getWholeText();
        if (text != null) {
            text.trim();
            if ("".equals(text)) {
                text = null;
            }
        }
        final String dialect = element.getAttribute("dialect");
        DroolsConsequenceAction actionText = new DroolsConsequenceAction(dialect, text);
        actionNode.setAction(actionText);
    }

    public Class generateNodeFor() {
        return ActionNode.class;
    }

	public void writeNode(Node node, StringBuffer xmlDump, boolean includeMeta) {
		ActionNode actionNode = (ActionNode) node;
		writeNode("action", actionNode, xmlDump, includeMeta);
        DroolsConsequenceAction action = (DroolsConsequenceAction) actionNode.getAction();
        if (action != null) {
            String dialect = action.getDialect();
            if (dialect != null) {
                xmlDump.append("dialect=\"" + action.getDialect() + "\" ");
            }
            String consequence = action.getConsequence();
            if (consequence == null) {
                endNode(xmlDump);
            } else {
                xmlDump.append(">" + XmlDumper.replaceIllegalChars(consequence.trim()) + "</action>" + EOL);
            }
        } else {
        	endNode(xmlDump);
        }
    }

}
