package org.brainfarm.java.neat.api.operators;

import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.IPopulation;

public interface IMutationStrategy {

	public void mutate(IGenome genome, IPopulation pop);
	
}
