package org.brainfarm.java.util.writers;

import org.brainfarm.java.feat.api.IEvolution;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EvolutionResultWriter implements IEvolutionWriter {

	public static final String ROOT_NODE_NAME = "Evolution";
	public static final String RUN_NODE_NAME = "Run";
	public static final String EPOCH_NODE_NAME = "Epoch";
	
	public static final String EPOCH_HAS_WINNER_ATTRIBUTE_NAME = "hasWinner";
	public static final String MAX_FITNESS_OF_EPOCH_ATTRIBUTE_NAME = "maxFitness";
	
	@Override
	public void write(Document xml, IEvolution evolution) {
		
		for (int i = 0; i < evolution.getTotalRuns(); i++) {
			
			Element runNode = xml.createElement(RUN_NODE_NAME);
			
			xml.getDocumentElement().appendChild(runNode);
			
			for (int j = 0; j < evolution.getTotalEpochs(); j++) {
				
				Element epochNode = xml.createElement(EPOCH_NODE_NAME);

				epochNode.setAttribute(MAX_FITNESS_OF_EPOCH_ATTRIBUTE_NAME, Double.toString(evolution.getMaxFitnessEachEpoch().get(j)));
				
				runNode.appendChild(epochNode);
			}
		}
	}

}
