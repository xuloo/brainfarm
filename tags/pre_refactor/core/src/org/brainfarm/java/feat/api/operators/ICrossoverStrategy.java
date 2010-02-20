package org.brainfarm.java.feat.api.operators;

import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IOrganism;

/**
 * The interface that must be implemented to perform crossover
 * between the IGenomes of two IOrganisms.
 * 
 * @author dtuohy
 *
 */
public interface ICrossoverStrategy {

	public IGenome performCrossover(IOrganism mom, IOrganism dad, int count);
	
}
