package org.brainfarm.feat.util.writers;

import static org.junit.Assert.*;

import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.util.XMLUtils;
import org.brainfarm.java.util.writers.EvolutionResultWriter;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EvolutionResultWriterTest {

	@Test
	public void testWrite() {
		
		Document xml = XMLUtils.createNewDocument(EvolutionResultWriter.ROOT_NODE_NAME);
		
		IEvolution evolution = new DummyEvolution();
		
		new EvolutionResultWriter().write(xml, evolution);
		
		assertEquals(EvolutionResultWriter.ROOT_NODE_NAME, xml.getDocumentElement().getNodeName());
		
		// Get the 'Run' nodes.
		NodeList runNodes = xml.getElementsByTagName(EvolutionResultWriter.RUN_NODE_NAME);
		
		assertEquals("There should be " + evolution.getTotalRuns() + " " + EvolutionResultWriter.RUN_NODE_NAME + " nodes", evolution.getTotalRuns(), runNodes.getLength());
	
		for (int i = 0; i < runNodes.getLength(); i++) {
			Node runNode = runNodes.item(i);
			
			assertEquals("run label should be " + (i + 1), EvolutionResultWriter.RUN_NODE_LABEL_PREFIX + String.valueOf(i + 1), runNode.getAttributes().getNamedItem(EvolutionResultWriter.LABEL_ATTRIBUTE_NAME).getNodeValue());
			
			// Get the 'Epoch' nodes within this 'Run' node.
			NodeList epochNodes = runNode.getChildNodes();
			
			assertEquals("There should be " + evolution.getTotalEpochs() + " " + EvolutionResultWriter.EPOCH_NODE_NAME + " nodes within the #" + i + " node", evolution.getTotalEpochs(), epochNodes.getLength());
		
			for (int j = 1; j <= evolution.getTotalEpochs(); j++) {
				Node epochNode = epochNodes.item(j - 1);
				
				assertNotNull("Epoch node " + (i + 1) + ":" + j + " should have an " + EvolutionResultWriter.MAX_FITNESS_OF_EPOCH_ATTRIBUTE_NAME + " attribute", epochNode.getAttributes().getNamedItem(EvolutionResultWriter.MAX_FITNESS_OF_EPOCH_ATTRIBUTE_NAME));
				
				assertEquals("epoch label should be " + j, EvolutionResultWriter.EPOCH_NODE_LABEL_PREFIX + String.valueOf(j), epochNode.getAttributes().getNamedItem(EvolutionResultWriter.LABEL_ATTRIBUTE_NAME).getNodeValue());

				Double maxFitness = 1.0 * j;
				String fitnessAttribute = epochNode.getAttributes().getNamedItem(EvolutionResultWriter.MAX_FITNESS_OF_EPOCH_ATTRIBUTE_NAME).getNodeValue();

				assertEquals("The maxFitness of epoch " + j + " should be " + maxFitness, maxFitness, Double.valueOf(fitnessAttribute));
			}
		}
	}

}
