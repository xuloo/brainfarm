package org.brainfarm.java.neat.api;

import java.util.List;

public interface ISpecies {

	int getId();
	
	double getAverageFitness();
	
	List<IOrganism> getOrganisms();
	
	int getAge();
	
	void addOrganism(IOrganism organism);
	
	void adjustFitness();
	
	double countOffspring(double skim);
	
	int getExpectedOffspring();
	void setExpectedOffspring(int expectedOffspring);
	
	int getAgeOfLastImprovement();
	void setAgeOfLastImprovement(int ageOfLastImprovement);
	
	void incrementExpectedOffspring();
	void incrementExpectedOffspring(double value);
	void decrementExpectedOffspring(double value);
	
	int lastImproved();
	
	void removeOrganism(IOrganism organism);
	
	boolean isNovel();
	void setNovel(boolean novel);
		
	void incrementAge();
	
	void computeAverageFitness();
	
	void computeMaxFitness();
}
