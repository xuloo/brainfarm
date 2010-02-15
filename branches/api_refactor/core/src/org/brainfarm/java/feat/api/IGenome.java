package org.brainfarm.java.feat.api;

import java.util.List;

/**
 * TODO: Document these interface methods.
 * 
 * @author dtuohy
 *
 */
public interface IGenome {
		
	INetwork generatePhenotype(int genomeId);
	
	int getId();
	void setId(int id);
	
	List<IGene> getGenes();
	
	List<INode> getNodes();
	
	IGenome duplicate(int new_id);
	
	int getLastNodeId();
	
	double getLastGeneInnovationId();
	
	INetwork getPhenotype();
	
	double compatibility(IGenome genome);
	
	boolean verify();

	boolean validate();
}