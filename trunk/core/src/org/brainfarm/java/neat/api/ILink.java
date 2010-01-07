package org.brainfarm.java.neat.api;

public interface ILink {

	ITrait getTrait();
	void setTrait(ITrait trait);
	
	INode getInputNode();
	void setInputNode(INode node);
	
	INode getOutputNode();
	void setOutputNode(INode node);
	
	double getWeight();
	void setWeight(double weight);
	
	boolean isRecurrent();
	void setRecurrent(boolean recurrent);
	
	void deriveTrait(ITrait trait);
	
	boolean isTimeDelayed();
	
	void setAddedWeight(double addedWeight);
	
	void setTraversed(boolean traversed);
}
