package org.brainfarm.java.feat.api;

import org.brainfarm.java.feat.api.enums.InnovationType;

public interface IInnovation {

	InnovationType getInnovationType();
	
	int getInputNodeId();
	
	int getOutputNodeId();
	
	int getNewNodeId();
	
	double getNewWeight();
	
	double getInnovationNumber1();
	double getInnovationNumber2();
	
	double getOldInnovationNumber();
	
	boolean isRecurrent();
}
