package org.brainfarm.java.util.writers;

import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.feat.api.ILink;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RaVisNetworkWriter implements INetworkWriter {

	public static final String NODE_NODE_NAME = "Node";
	public static final String EDGE_NODE_NAME = "Edge";
	
	public static final String ID_ATTRIBUTE_NAME = "id";
	public static final String NAME_ATTRIBUTE_NAME = "name";
	public static final String DESCRIPTION_ATTRIBUTE_NAME = "desc";
	public static final String DESCRIPTION_ATTRIBUTE_PREFIX = "Description of node ";
	public static final String FROMID_ATTRIBUTE_NAME = "fromID";
	public static final String TOID_ATTRIBUTE_NAME = "toID";
	
	protected List<ILink> writtenLinks = new ArrayList<ILink>();
	
	@Override
	public void write(Document xml, INetwork network) {
		
		for (INode node : network.getAllNodes()) {
			xml.getDocumentElement().appendChild(writeNode(xml, node));
			
			for (ILink inLink : node.getIncoming()) {
				if (!writtenLinks.contains(inLink)) {
					xml.getDocumentElement().appendChild(writeLink(xml, inLink));
				}
			}
			
			for (ILink outLink : node.getOutgoing()) {
				if (!writtenLinks.contains(outLink)) {
					xml.getDocumentElement().appendChild(writeLink(xml, outLink));
				}
			}
		}
	}
	
	protected Element writeNode(Document xml, INode node) {
		Element n = xml.createElement(NODE_NODE_NAME);
		
		n.setAttribute(ID_ATTRIBUTE_NAME, String.valueOf(node.getId()));
		n.setAttribute(NAME_ATTRIBUTE_NAME, String.valueOf(node.getId()));
		n.setAttribute(DESCRIPTION_ATTRIBUTE_NAME, DESCRIPTION_ATTRIBUTE_PREFIX + String.valueOf(node.getId()));
		
		return n;
	}
	
	protected Element writeLink(Document xml, ILink link) {		
		Element e = xml.createElement(EDGE_NODE_NAME);
		
		e.setAttribute(FROMID_ATTRIBUTE_NAME, String.valueOf(link.getOutputNode().getId()));
		e.setAttribute(TOID_ATTRIBUTE_NAME, String.valueOf(link.getInputNode().getId()));
		
		writtenLinks.add(link);
		
		return e;
	}
}
