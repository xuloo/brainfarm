package org.brainfarm.java.feat.api;

import java.util.List;

import org.brainfarm.java.feat.api.params.IEvolutionConstants;
import org.brainfarm.java.feat.api.params.IEvolutionParametersAware;

/**
 * TODO: Document these interface methods.
 * 
 * @author dtuohy
 *
 */
public interface IGenome extends IEvolutionParametersAware, IEvolutionConstants {
		
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
}