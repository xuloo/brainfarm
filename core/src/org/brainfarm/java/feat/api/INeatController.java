package org.brainfarm.java.feat.api;


public interface INeatController {

	void loadExperiment();
	
	void loadExperiment(String location);
	
	void startEvolution();
}
