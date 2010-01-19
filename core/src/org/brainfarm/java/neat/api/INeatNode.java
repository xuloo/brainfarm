package org.brainfarm.java.neat.api;

import org.brainfarm.java.neat.api.enums.ActivationFunction;

public interface INeatNode extends INode {

	void flushbackOLD();
	
	double getActivation();
	void setActivation(double activation);
	
	ActivationFunction getActivationFunction();
	void setActivationFunction(ActivationFunction activationFunction);
	
	double getLastActivation();
	void setLastActivation(double lastActivation);
	
	double getLastActivation2();
	void setLastActivation2(double lastActivation2);
	
	double getActivationCount();
	void setActivationCount(double activationCount); 
	
	boolean sensorLoad(double value);
	
	void incrementActivationCount();
}
