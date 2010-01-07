package org.brainfarm.java.neat.api;

public interface ITrait {

	int getId();
	
	double[] getParams();
	
	double getParam(int index);
	
	ITrait clone();
	
	void mutate();
	
	String toString();
}
