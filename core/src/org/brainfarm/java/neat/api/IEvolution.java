package org.brainfarm.java.neat.api;

import org.brainfarm.java.neat.Population;

public interface IEvolution {

	int getNumberOfRuns();
	
	int getNumberOfEpochs();
	
	Population getPopulation();
}
