package org.brainfarm.java.neat.api.operators;

import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.enums.MutationType;

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
	
	/**
	 * Adds a link somewhere in the genome, the population must be passed
	 * in so that it can be consulted for a new innovation number.
	 * 
	 * @param genome
	 * @param population
	 * @return true if a link was added
	 */
	public boolean mutateAddLink(IGenome genome, IPopulation population);
	
	/**
	 * 
	 * Mutates the weight of a link.
	 * 
	 * TODO: examine implementation in DefaultMutationStrategy to see
	 * exactly what the meanings of these parameters are.
	 * 
	 * @param genome
	 * @param power
	 * @param rate
	 * @param mutationType
	 */
	public void mutateLinkWeight(IGenome genome, double power, double rate, MutationType mutationType);

}
