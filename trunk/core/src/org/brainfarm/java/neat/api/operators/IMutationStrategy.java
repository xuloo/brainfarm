package org.brainfarm.java.neat.api.operators;

import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.IPopulation;

/**
 * This interface must be implemented to perform
 * the various mutations applicable to an IGenome.
 * 
 * @author dtuohy
 *
 */
public interface IMutationStrategy {

	/**
	 * Applies the full suite of possible mutations to a genome according
	 * to their probabilities.
	 * 
	 * @param genome - the genome to be mutated
	 * @param pop - the population of the genome
	 * @param generation - the current epoch
	 * @return 'true' iff a mutation introduced a structural change to the network
	 * 			encoded by the genome.
	 */
	public boolean mutate(IGenome genome, IPopulation pop, int generation);
}
