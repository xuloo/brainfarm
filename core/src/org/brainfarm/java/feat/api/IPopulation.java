package org.brainfarm.java.feat.api;

import java.util.List;

import org.brainfarm.java.feat.api.operators.IReproductionStrategy;

public interface IPopulation {
	
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
	
	public void setReproductionStrategy(IReproductionStrategy reproductionStrategy);
}