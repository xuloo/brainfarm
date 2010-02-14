package org.brainfarm.java.feat.experiment;

import org.brainfarm.java.feat.Evolution;
import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.evolution.IEvolution;
import org.brainfarm.java.feat.api.experiment.IExperiment;

/**
 * 
 * @author Trevor
 *
 */
public class Experiment implements IExperiment {
	
	private IGenome genome;
	
	protected int epochs;
	
	private int runs;
	
	protected IPopulation population;
	
	protected IEvolutionStrategy evolutionStrategy;
	
	@Override
	public void refresh() {
		
	}

	@Override
	public IEvolution evolution() {
		population.init();
		return new Evolution(evolutionStrategy, population, runs, epochs);
	}

	public void setEpochs(int epochs) {
		this.epochs = epochs;
	}

	public int getEpochs() {
		return epochs;
	}

	public IPopulation getPopulation() {
		return population;
	}
	
	public void setPopulation(IPopulation population) {
		this.population = population;
	}
	
	public IEvolutionStrategy getEvolutionStrategy() {
		return evolutionStrategy;
	}
	
	public void setEvolutionStrategy(IEvolutionStrategy evolutionStrategy) {
		this.evolutionStrategy = evolutionStrategy;
	}

	public void setGenome(IGenome genome) {
		this.genome = genome;
	}

	public IGenome getGenome() {
		return genome;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getRuns() {
		return runs;
	}
}
