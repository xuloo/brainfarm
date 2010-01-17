package org.brainfarm.java.neat.api;


public interface IEvolution {

	int getNumberOfRuns();
	
	int getNumberOfEpochs();
	
	IPopulation getPopulation();
}
