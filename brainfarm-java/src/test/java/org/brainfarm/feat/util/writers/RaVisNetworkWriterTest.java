package org.brainfarm.feat.util.writers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.feat.Link;
import org.brainfarm.java.feat.Network;
import org.brainfarm.java.feat.Node;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;
import org.brainfarm.java.util.XMLUtils;
import org.brainfarm.java.util.writers.RaVisNetworkWriter;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class RaVisNetworkWriterTest {

	@Test
	public void testWriteNetwork() {
		
		// Build a simple network with 2 nodes and single connection between them.
		INode node1 = new Node(1);
		INode node2 = new Node(2);
		
		new Link(0, node1, node2, false);
		
		List<INode> nodes = new ArrayList<INode>();
		
		nodes.add(node1);
		nodes.add(node2);
		
		INetwork network = new Network(nodes, 0);
	
		Document xml = XMLUtils.createNewDocument("network");
		
		// Write the network into an XML document.
		new RaVisNetworkWriter().write(xml, network);
		
		NodeList nodeList = xml.getDocumentElement().getChildNodes();
		
		// Check we have the correct number of nodes.
		assertEquals("Should be 3 nodes", 3, nodeList.getLength());
		
		// Check we have the correct number of nodes of each type.
		assertEquals("Should be 2 'Node' nodes", 2, xml.getDocumentElement().getElementsByTagName(RaVisNetworkWriter.NODE_NODE_NAME).getLength());
		assertEquals("Should be 1 'Edge' node", 1, xml.getDocumentElement().getElementsByTagName(RaVisNetworkWriter.EDGE_NODE_NAME).getLength());
		
		// Grab the first 'Node' node and make sure it's got the correct attributes.		
		org.w3c.dom.Node n1 = nodeList.item(0);
		String idAsString = String.valueOf(node1.getId());
		
		assertEquals(RaVisNetworkWriter.NODE_NODE_NAME, n1.getNodeName());
		assertEquals(idAsString, n1.getAttributes().getNamedItem(RaVisNetworkWriter.ID_ATTRIBUTE_NAME).getNodeValue());
		assertEquals(idAsString, n1.getAttributes().getNamedItem(RaVisNetworkWriter.NAME_ATTRIBUTE_NAME).getNodeValue());
		assertEquals(RaVisNetworkWriter.DESCRIPTION_ATTRIBUTE_PREFIX + idAsString, n1.getAttributes().getNamedItem(RaVisNetworkWriter.DESCRIPTION_ATTRIBUTE_NAME).getNodeValue());
		
		// Grab the 'Edge' node and make sure it's got the correct attributes.
		org.w3c.dom.Node edgeNode = nodeList.item(1);

		assertNotNull("Weird, there are definately 3 nodes but the 3rd is null!", edgeNode);
		assertEquals("The 3rd Node should be an 'Edge' node", RaVisNetworkWriter.EDGE_NODE_NAME, edgeNode.getNodeName());
		assertNotNull("There's no 'fromID' attribute on the Edge node", edgeNode.getAttributes().getNamedItem(RaVisNetworkWriter.FROMID_ATTRIBUTE_NAME));
		assertEquals("2", edgeNode.getAttributes().getNamedItem(RaVisNetworkWriter.FROMID_ATTRIBUTE_NAME).getNodeValue());
		assertEquals("1", edgeNode.getAttributes().getNamedItem(RaVisNetworkWriter.TOID_ATTRIBUTE_NAME).getNodeValue());
	}
}
