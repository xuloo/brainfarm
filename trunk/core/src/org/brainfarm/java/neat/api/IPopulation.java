package org.brainfarm.java.neat.api;

import java.util.List;


public interface IPopulation {
	
	void epoch(int generation);
	
	List<IOrganism> getOrganisms();
	
	List<ISpecies> getSpecies();
	
	List<IInnovation> getInnovations();
	
	int getCurrentNodeIdAndIncrement();
	
	double getCurrentInnovationNumberAndIncrement();
	
	int getLastSpecies();
	void setLastSpecies(int lastSpecies);
	void incrementLastSpecies();
}
