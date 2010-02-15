package org.brainfarm.java.feat.context;

import org.brainfarm.java.feat.Evolution;
import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.operators.FeatFactory;

public class Experiment implements IExperiment {
	
	private int runs;
	
	private int epochs;
	
	private int populationSize;
	
	private IGenome genome;
	
	private IPopulation population;
	
	private IEvolutionStrategy evolutionStrategy;
	
	public Experiment() {
		
	}
	
	@Override
	public IEvolution evolution() {
		FeatFactory.setEvolutionStrategy(evolutionStrategy);
		
		// Initialise population.
		population.setReproductionStrategy(evolutionStrategy.getReproductionStrategy());
		evolutionStrategy.getPopulationInitializationStrategy().initialize(population, genome, populationSize);
		
		// Now create a new IEvolution instance.
		return new Evolution(population, evolutionStrategy.getOrganismEvaluator(), runs, epochs);
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public void setEpochs(int epochs) {
		this.epochs = epochs;
	}

	public void setPopulation(IPopulation population) {
		this.population = population;
	}

	public void setEvolutionStrategy(IEvolutionStrategy evolutionStrategy) {
		this.evolutionStrategy = evolutionStrategy;
	}

	public void setGenome(IGenome genome) {
		this.genome = genome;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}	
}
