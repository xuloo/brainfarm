package org.brainfarm.java.feat.api;


public interface IEvolution {

	int getNumberOfRuns();
	
	int getNumberOfEpochs();
	
	IPopulation getPopulation();
}
