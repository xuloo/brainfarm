package org.brainfarm.java.neat.api;

import java.util.List;

import org.brainfarm.java.neat.api.enums.MutationType;

public interface IGenome {
	
	boolean mutateAddLink(IPopulation population, int attempts);
	
	boolean mutateAddNode(IPopulation population);
	
	void mutateGeneReenable();
	
	void mutateLinkTrait(int repeats);
	
	void mutateLinkWeight(double power, double rate, MutationType mutationType);
	
	void mutateNodeTrait(int repeats);
	
	void mutateRandomTrait();
	
	void mutateToggleEnable(int repeats);
	
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