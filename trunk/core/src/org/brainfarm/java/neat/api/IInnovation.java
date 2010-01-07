package org.brainfarm.java.neat.api;

import org.brainfarm.java.neat.api.types.InnovationType;

public interface IInnovation {

	InnovationType getInnovationType();
	
	int getInputNodeId();
	
	int getOutputNodeId();
	
	int getNewNodeId();
	int getNewTraitId();
	
	double getNewWeight();
	
	double getInnovationNumber1();
	double getInnovationNumber2();
	
	double getOldInnovationNumber();
	
	boolean isRecurrent();
}
