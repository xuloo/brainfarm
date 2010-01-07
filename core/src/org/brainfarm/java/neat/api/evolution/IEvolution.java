package org.brainfarm.java.neat.api.evolution;

import org.brainfarm.java.neat.api.IPopulation;


public interface IEvolution {

	int getRun();
	
	int getEpoch();
	
	IPopulation getPopulation();
}
