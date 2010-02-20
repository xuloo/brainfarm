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
import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.IEvolutionListener;
import org.brainfarm.java.util.ThreadedCommand;

/**
 * The central class from which Evolution is run.
 * 
 * An Evolution instance is created by an Experiment by calling Experiment.evolution();
 * 
 * Evolution is executed in its own thread.
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 * @author dtuohy
 *
 */
public class Evolution extends ThreadedCommand implements IEvolution {
	
	/**
	 * Logger instance.
	 */
	private static Logger logger = Logger.getLogger(Evolution.class);
	
	/**
	 * The number of times this Evolution run will be executed before it completes.
	 */
	private int runs;
	
	/**
	 * The current run this Evolution instance is in.
	 */
	private int currentRun;
	
	/**
	 * The number of epochs to push the population through during each run.
	 */
	private int epochs;
	
	/**
	 * The current epoch this Evolution run is in.
	 */
	private int currentEpoch;
	
	/**
	 * The IPopulation instance that contains the organisms evolved during the Evolution.
	 */
	private IPopulation population;
	
	/**
	 * The strategy factory used to execute the evolutionary algorithm on the organisms in the population.
	 */
	private IEvolutionStrategy evolutionStrategy;
	
	/**
	 * The first organism in any epoch of any run that 
	 * performed better than the error threshold.
	 */
	private IOrganism firstWinner;
	
	/**
	 * The fittest organism from any epoch of any run.
	 */
	private IOrganism superWinner;
	
	/**
	 * List containing the highest fitness values from each epoch of the last run.
	 */
	private List<Double> maxFitnessEachEpoch = new ArrayList<Double>();
	
	/**
	 * Objects listening for events from this evolutionary run.
	 */
	protected List<IEvolutionListener> listeners = Collections.synchronizedList(new CopyOnWriteArrayList<IEvolutionListener>());
	
	/**
	 * Constructor. Creates a new Evolution instance that contains all the properties required for a complete
	 * Evolution run. A 'snapshot' of the Experiment settings at the point Experiment#evolution() is called.
	 * 
	 * @param evolutionStrategy
	 * @param population
	 * @param runs
	 * @param epochs
	 */
	public Evolution(IEvolutionStrategy evolutionStrategy, IPopulation population, int runs, int epochs) {
		this.evolutionStrategy = evolutionStrategy;
		this.population = population;
		this.runs = runs;
		this.epochs = epochs;
	}
	
	/**
	 * Starts the Evolution running.
	 */
	@Override
	protected void execute() {
		
		logger.debug("Executing Evolution");
		
		// Inform listeners we're starting a set of evolution runs.
		onEvolutionStart();
		
		// For each run...
		for (currentRun = 0; currentRun < runs; currentRun++) {
			
			// Execute an Epoch.
			for (int currentEpoch = 0; currentEpoch < epochs; currentEpoch++) {
				
				// Inform listeners we're starting a new Epoch.
				onEpochStart();
				
				// Execute the Epoch.
				epoch(population, currentEpoch);
				
				// Inform the listeners the Epoch is complete.
				onEpochComplete();
			}
		}
		
		// Inform the listeners the evolution runs are complete.
		onEvolutionComplete();
		
		logger.debug("Evolution Complete");
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
	
	/**
	 * Inform each listener that Evolution has started.
	 * 
	 * @param population
	 */
	protected void onEvolutionStart() {
		maxFitnessEachEpoch.clear();
		for (IEvolutionListener listener : listeners) {
			listener.onEvolutionStart(this);
		}
	}
	
	/**
	 * Inform each listener that Evolution has completed.
	 * 
	 * @param population
	 */
	protected void onEvolutionComplete() {
		for (IEvolutionListener listener : listeners) {
			listener.onEvolutionComplete(this);
		}
	}
	
	/**
	 * Inform each listener that a new Epoch is about to begin.
	 * 
	 * @param run
	 * @param epoch
	 * @param population
	 */
	protected void onEpochStart() {
		for (IEvolutionListener listener : listeners) {
			listener.onEpochStart(this);
		}
	}
	
	/**
	 * Inform each listener that an Epoch has just completed.
	 * 
	 * @param run
	 * @param epoch
	 * @param population
	 */
	protected void onEpochComplete() {
		for (IEvolutionListener listener : listeners) {
			listener.onEpochComplete(this);
		}
	}
	
	/**
	 * Add a listener for events from this Evolution.
	 */
	public void addListener(IEvolutionListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Remove a listener for events from this Evolution.
	 */
	public void removeListener(IEvolutionListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * Returns the current 'run' this Evolution is in.
	 */
	public int getRun() {
		return currentRun;
	}
	
	/**
	 * Returns the current Epoch this Evolution is in.
	 */
	public int getEpoch() {
		return currentEpoch;
	}
	
	/**
	 * Returns the population that is being evolved.
	 */
	public IPopulation getPopulation() {
		return population;
	}
	
	/**
	 * Returns the list of fitnesses from each epoch of the last run.
	 */
	public List<Double> getMaxFitnessEachEpoch(){
		return maxFitnessEachEpoch;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IOrganism getWinner() {
		return firstWinner;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasWinner() {
		return firstWinner != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IOrganism getSuperWinner() {
		return superWinner;
	}
}