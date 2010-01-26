package org.brainfarm.java.feat.api;


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
	
	int getSuperChampOffspring();
	void setSuperChampOffspring(int superChampOffspring);

	void incrementSuperChampOffspring();
	void incrementSuperChampOffspring(int value);
	
	boolean getMutStructBaby();
	void setMutStructBaby(boolean mutStructBaby);
	
	boolean getMateBaby();
	void setMateBaby(boolean mateBaby);
	
	boolean isPopulationChampion();
	void setPopulationChampion(boolean populationChampion);
	
	double getHighestFitness();
	void setHighestFitness(double highFitness);
	
	boolean isPopulationChampionChild();
	void setPopulationChampionChild(boolean populationChampionChild);
	
	boolean getChampion();
	void setChampion(boolean champion);
	
	boolean isWinner();
	void setWinner(boolean winner);
}
