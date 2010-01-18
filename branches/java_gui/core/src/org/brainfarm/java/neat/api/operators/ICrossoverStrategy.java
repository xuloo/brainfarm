package org.brainfarm.java.neat.api.operators;

import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.IOrganism;

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
