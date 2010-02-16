package org.brainfarm.java.feat.api;


public interface IEvolutionController {

	void loadDefaultParameters();
	
	void loadExperiment();
	
	void loadExperiment(String location);
	
	void startEvolution();
}
