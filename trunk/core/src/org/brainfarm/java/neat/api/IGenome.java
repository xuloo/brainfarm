package org.brainfarm.java.neat.api;

import java.util.List;

public interface IGenome {
		
	INetwork genesis(int genomeId);
	
	int getId();
	void setId(int id);
	
	List<IGene> getGenes();
	
	List<INode> getNodes();
	
	List<ITrait> getTraits();
	
	IGenome duplicate(int count);
	
	int getLastNodeId();
	
	double getLastGeneInnovationId();
	
	INetwork getPhenotype();
	
	double compatibility(IGenome genome);
	
	boolean verify();
}