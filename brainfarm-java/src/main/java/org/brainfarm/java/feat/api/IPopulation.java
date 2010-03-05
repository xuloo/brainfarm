package org.brainfarm.java.feat.api;

import java.util.List;

import org.brainfarm.java.feat.api.enums.InnovationType;
import org.brainfarm.java.feat.api.params.IEvolutionParametersAware;

public interface IPopulation extends IEvolutionParametersAware {
	
	void init();
	
	void epoch(int generation);
	
	List<IOrganism> getOrganisms();

	void setOrganisms(List<IOrganism> arrayList);
	
	List<ISpecies> getSpecies();
	
	void setSpecies(List<ISpecies> species);
	
	List<IInnovation> getInnovations();
	
	int getCurrentNodeIdAndIncrement();
	
	double getCurrentInnovationNumberAndIncrement();
	
	int getLastSpecies();
	
	void setLastSpecies(int lastSpecies);
	
	void incrementLastSpecies();

	public void setCur_node_id(int cur_node_id);

	public void setCur_innov_num(double cur_innov_num);

	IInnovation getExistingNodeInnovation(int inNodeId, int outNodeId, double innovationNumber);

	IInnovation getExistingLinkInnovation(int inNodeId, int outNodeId, boolean isRecurrent);

}