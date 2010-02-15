package org.brainfarm.java.feat.ann;

import org.brainfarm.java.feat.Gene;
import org.brainfarm.java.feat.api.IGene;

/**
 * Custom functionality for NEAT IGenes, which
 * represent artificial neural synapses.
 * 
 * @author dtuohy
 *
 */
public class NeatGene extends Gene {

	public boolean sameAs(IGene other) {
		if (other.getLink().getInputNode().getId() == getLink().getInputNode().getId()
				&& other.getLink().getOutputNode().getId() == getLink().getOutputNode().getId()
				&& other.getLink().isRecurrent() == getLink().isRecurrent()) {
			return true;
		}
		if (other.getLink().getInputNode().getId() == getLink().getOutputNode().getId()
				&& other.getLink().getOutputNode().getId() == getLink().getInputNode().getId()
				&& !other.getLink().isRecurrent()
				&& !getLink().isRecurrent()) {
			return true;
		}
		return false;
	}
	
}
