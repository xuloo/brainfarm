package org.brainfarm.java.feat.api;

/**
 * Describes an Organism.
 * 
 * @author dtuohy
 *
 */
public interface IOrganism {

	double getFitness();
	void setFitness(double fitness);
	
	IGenome getGenome();
	
	INetwork getPhenotype();
	
	boolean isEliminated();
	void setEliminated(boolean eliminate);
	
	double getExpectedOffspring();
	void setExpectedOffspring(double expectedOffspring);
	
	ISpecies getSpecies();
	void setSpecies(ISpecies species);
	
	double getOriginalFitness();
	void setOriginalFitness(double originalFitness);
	
	/** TODO: Should these fields be on the IOrganism itself? */
	
	int getSuperChampOffspring();
	void setSuperChampOffspring(int superChampOffspring);

	void incrementSuperChampOffspring();
	void incrementSuperChampOffspring(int value);	
}
