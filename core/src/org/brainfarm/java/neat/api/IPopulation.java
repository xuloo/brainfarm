package org.brainfarm.java.neat.api;

import java.util.List;

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

}