package org.brainfarm.java.feat.api;

/**
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public interface IEvolutionController {

	/**
	 * Load parameters from a custom context file.
	 */
	void loadCustomParameters(String contextfile);
	
	/**
	 * Load the default parameters into the Neat class.
	 */
	void loadDefaultParameters();
	
	/**
	 * Load an experiment from the default directory.
	 */
	void loadExperiment();
	
	/**
	 * Load an experiment from the location specified.
	 * 
	 * @param location
	 */
	void loadExperiment(String location);
	
	/**
	 * Start the current experiment running.
	 */
	void startEvolution();
}
