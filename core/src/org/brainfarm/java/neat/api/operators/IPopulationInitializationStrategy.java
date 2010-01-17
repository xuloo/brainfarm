package org.brainfarm.java.neat.api.operators;

import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.IPopulation;

public interface IPopulationInitializationStrategy {

	public void initialize(IPopulation pop, IGenome genome, int size);
	
}
