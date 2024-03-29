package org.brainfarm.java.feat.ann;

import org.brainfarm.java.feat.Organism;
import org.brainfarm.java.feat.api.IGenome;

/**
 * Defines NEAT-specific fields and logic (e.g. error)
 * @author dtuohy
 *
 */
public class NeatOrganism extends Organism {
	
	/** Used just for reporting purposes */
	private double error = 0;

	public NeatOrganism(double xfitness, IGenome xgenome, int xgeneration) {
		super(xfitness, xgenome, xgeneration);
	}
	
	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}	
}
