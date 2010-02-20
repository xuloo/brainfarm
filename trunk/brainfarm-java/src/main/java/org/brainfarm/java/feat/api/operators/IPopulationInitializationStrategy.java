package org.brainfarm.java.feat.api.operators;

import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IPopulation;

public interface IPopulationInitializationStrategy {

	public void initialize(IPopulation pop, IGenome genome, int size);
	
}
