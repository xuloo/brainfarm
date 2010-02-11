package org.brainfarm.java.feat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ISpecies;
import org.brainfarm.java.feat.api.evolution.IEvolution;
import org.brainfarm.java.feat.api.evolution.IEvolutionListener;
import org.brainfarm.java.feat.context.IExperiment;
import org.brainfarm.java.util.ThreadedCommand;

/**
 * The central class from which Evolution is run.
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 * @author dtuohy
 *
 */
public class Evolution extends ThreadedCommand implements IEvolution {
	
	private static Logger logger = Logger.getLogger(Evolution.class);
	
	private int currentRun;
	
	private int currentEpoch;
	
	private Neat neat;
	
	private IExperiment experiment;
	
	private IPopulation population;
	
	/**
	 * The first organism in any epoch of any run that 
	 * performed better than the error threshold.
	 */
	private IOrganism firstWinner;
	
	/**
	 * The fittest organism from any epoch of any run.
	 */
	private IOrganism superWinner;
	
	protected List<IEvolutionListener> listeners = Collections.synchronizedList(new CopyOnWriteArrayList<IEvolutionListener>());
	
	private List<Double> maxFitnessEachEpoch = new ArrayList<Double>();
	
	public Evolution() {
		
	}
	
	public Evolution(Neat neat, IExperiment experiment, IPopulation population) {
		this.neat = neat;
		this.experiment = experiment;
		this.population = population;
	}
	
	@Override
	protected void execute() {
		
		// Inform listeners we're starting a set of evolution runs.
		onEvolutionStart(population);
		
		// For each run...
		for (currentRun = 0; currentRun < neat.num_runs; currentRun++) {
			
			// Execute an Epoch.
			for (int currentEpoch = 0; currentEpoch < experiment.getEpoch(); currentEpoch++) {
				
				// Inform listeners we're starting a new Epoch.
				onEpochStart(currentRun, currentEpoch, population);
				
				// Execute the Epoch.
				epoch(population, currentEpoch);
				
				// Inform the listeners the Epoch is complete.
				onEpochComplete(currentRun, currentEpoch, population);
			}
		}
		
		// Inform the listeners the evolution runs are complete.
		onEvolutionComplete(population);
	}
	
	/**
	 * Represents a single cycle of 'evolution'.
	 * Evaluates each organism in the population and records any 'winners'.
	 * Keeps track of the maximum fitness value for the Epoch.
	 * Updates the fitness values for each species in the population.
	 * invokes the population's own 'epoch' method - functionality here depends 
	 * on the strategy implementations for the experiment.
	 * 
	 * @param population - The population to evaluate and evolve.
	 * @param generation - The current epoch we're in.
	 */
	public void epoch(IPopulation population, int generation) {

		double maxFitnessOfEpoch = 0.0;
		
		// Cache the EvolutionStrategy instance - it shouldn't 
		// change during an experiment run. But let's make sure.
		IEvolutionStrategy evolutionStrategy = EvolutionStrategy.getInstance();

		// Evaluate each organism and check for first/super winners.
		for (IOrganism organism : population.getOrganisms()) {
			
			// Evaluate each organism. 
			if (evolutionStrategy.getOrganismEvaluator().evaluate(organism)) {
				
				// If it's the first winner, store the organism.
				if (!hasWinner()) {
					
					// If there's no firstWinner then there's no superWinner either.
					firstWinner = superWinner = organism;
				
				// Otherwise, check if it's more fit than the previous superwinner.
				} else if (organism.getFitness() > superWinner.getFitness()) {
					superWinner = organism;
				}
			}

			// Keep track of the maximum fitness of the epoch.
			if (organism.getFitness() > maxFitnessOfEpoch) {
				maxFitnessOfEpoch = organism.getFitness();
			}
		}
		
		// Record the highest fitness value for this epoch.
		maxFitnessEachEpoch.add(maxFitnessOfEpoch);
		
		// Compute average and max fitness for each species
		for (ISpecies specie : population.getSpecies()) {
			specie.computeAverageFitness();
			specie.computeMaxFitness();
		}

		// Evolve the population.
		population.epoch(generation);
	}
	
	protected void onEvolutionStart(IPopulation population) {
		maxFitnessEachEpoch.clear();
		for (IEvolutionListener listener : listeners) {
			listener.onEvolutionStart(this);
		}
	}
	
	protected void onEvolutionComplete(IPopulation population) {
		for (IEvolutionListener listener : listeners) {
			listener.onEvolutionComplete(this);
		}
	}
	
	protected void onEpochStart(int run, int epoch, IPopulation population) {
		for (IEvolutionListener listener : listeners) {
			listener.onEpochStart(this);
		}
	}
	
	protected void onEpochComplete(int run, int epoch, IPopulation population) {
		for (IEvolutionListener listener : listeners) {
			listener.onEpochComplete(this);
		}
	}
	
	public void addListener(IEvolutionListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(IEvolutionListener listener) {
		listeners.remove(listener);
	}
	
	public void setListeners(List<IEvolutionListener> listeners) {
		this.listeners = listeners;
	}
	
	public int getRun() {
		return currentRun;
	}
	
	public int getEpoch() {
		return currentEpoch;
	}
	
	public IPopulation getPopulation() {
		return population;
	}
	
	public List<Double> getMaxFitnessEachEpoch(){
		return maxFitnessEachEpoch;
	}
	
	public void setNeat(Neat neat) {
		this.neat = neat;
	}
	
	public void setExperiment(IExperiment experiment) {
		this.experiment = experiment;
	}
	
	public void setPopulation(IPopulation population) {
		this.population = population;
	}

	@Override
	public IOrganism getWinner() {
		return firstWinner;
	}

	@Override
	public boolean hasWinner() {
		return firstWinner != null;
	}

	@Override
	public IOrganism getSuperWinner() {
		return superWinner;
	}
}