package org.brainfarm.java.feat.api;

public interface ILink {
	
	INode getInputNode();
	void setInputNode(INode node);
	
	INode getOutputNode();
	void setOutputNode(INode node);
	
	double getWeight();
	void setWeight(double weight);
	
	boolean isRecurrent();
	void setRecurrent(boolean recurrent);
	
	boolean isTimeDelayed();
	
	void setAddedWeight(double addedWeight);
	
	void setTraversed(boolean traversed);
	
	IGene getGene();
	void setGene(IGene gene);
}
