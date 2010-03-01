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
	public static final String RUN_NODE_LABEL_PREFIX = "Run #";
	public static final String EPOCH_NODE_LABEL_PREFIX = "Epoch #";
	public static final String LABEL_ATTRIBUTE_NAME = "label";
	
	@Override
	public void write(Document xml, IEvolution evolution) {
		
		for (int i = 0; i < evolution.getTotalRuns(); i++) {
			
			Element runNode = writeRun(xml, evolution, i);
			
			for (int j = 0; j < evolution.getTotalEpochs(); j++) {
				
				runNode.appendChild(writeEpoch(xml, evolution, i, j));
			}
			
			xml.getDocumentElement().appendChild(runNode);
		}
	}
	
	protected Element writeRun(Document xml, IEvolution evolution, int run) {
		Element runNode = xml.createElement(RUN_NODE_NAME);
		
		runNode.setAttribute(LABEL_ATTRIBUTE_NAME, RUN_NODE_LABEL_PREFIX + String.valueOf(run + 1));
		
		return runNode;
	}
	
	protected Element writeEpoch(Document xml, IEvolution evolution, int run, int epoch) {
		Element epochNode = xml.createElement(EPOCH_NODE_NAME);

		epochNode.setAttribute(MAX_FITNESS_OF_EPOCH_ATTRIBUTE_NAME, Double.toString(evolution.getMaxFitnessEachEpoch().get(epoch)));
		epochNode.setAttribute(LABEL_ATTRIBUTE_NAME, EPOCH_NODE_LABEL_PREFIX + String.valueOf(epoch + 1));

		return epochNode;
	}
}
