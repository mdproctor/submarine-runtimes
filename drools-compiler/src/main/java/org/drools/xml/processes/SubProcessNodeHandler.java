package org.drools.xml.processes;

import java.util.Map;

import org.drools.workflow.core.Node;
import org.drools.workflow.core.node.SubProcessNode;
import org.drools.xml.Configuration;
import org.drools.xml.ExtensibleXmlParser;
import org.xml.sax.SAXException;

public class SubProcessNodeHandler extends AbstractNodeHandler {

    protected Node createNode() {
        return new SubProcessNode();
    }

    public void handleNode(final Node node, final Configuration config, final String uri,
            final String localName, final ExtensibleXmlParser parser)
            throws SAXException {
        super.handleNode(node, config, uri, localName, parser);
        SubProcessNode subProcessNode = (SubProcessNode) node;
        String processId = config.getAttribute("processId");
        subProcessNode.setProcessId(processId);
        String waitForCompletion = config.getAttribute("waitForCompletion");
        subProcessNode.setWaitForCompletion(!"false".equals(waitForCompletion));
        String independent = config.getAttribute("independent");
        subProcessNode.setIndependent(!"false".equals(independent));
    }

    public Class generateNodeFor() {
        return SubProcessNode.class;
    }

	public void writeNode(Node node, StringBuffer xmlDump, boolean includeMeta) {
		SubProcessNode subProcessNode = (SubProcessNode) node;
		writeNode("subProcess", subProcessNode, xmlDump, includeMeta);
        String processId = subProcessNode.getProcessId();
        if (processId != null) {
            xmlDump.append("processId=\"" + processId + "\" ");
        }
        if (!subProcessNode.isWaitForCompletion()) {
            xmlDump.append("waitForCompletion=\"false\" ");
        }
        if (!subProcessNode.isIndependent()) {
            xmlDump.append("independent=\"false\" ");
        }
        xmlDump.append(">" + EOL);
        Map<String, String> inMappings = subProcessNode.getInMappings();
        for (Map.Entry<String, String> inMapping: inMappings.entrySet()) {
            xmlDump.append(
                "      <mapping type=\"in\" "
                             + "from=\"" + inMapping.getValue() + "\" "
                             + "to=\"" + inMapping.getKey() + "\" />" + EOL);
        }
        Map<String, String> outMappings = subProcessNode.getOutMappings();
        for (Map.Entry<String, String> outMapping: outMappings.entrySet()) {
            xmlDump.append(
                "      <mapping type=\"out\" "
                             + "from=\"" + outMapping.getKey() + "\" "
                             + "to=\"" + outMapping.getValue() + "\" />" + EOL);
        }
        endNode("subProcess", xmlDump);
	}

}
