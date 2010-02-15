package org.brainfarm.java.feat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ISpecies;
import org.brainfarm.java.feat.api.IOrganismEvaluator;
import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.evolution.IEvolutionListener;
import org.brainfarm.java.util.ThreadedCommand;

/**
 * The central class from which Evolution is run.
 * 
 * @author dtuohy
 *
 */
public class Evolution extends ThreadedCommand implements IEvolution {
	
	private static Logger logger = Logger.getLogger(Evolution.class);
	
	private int runs;
	
	private int currentRun;
	
	private int epochs;
	
	private int currentEpoch;
	
	private IPopulation population;
	
	private IOrganismEvaluator evaluator;
	
	protected List<IEvolutionListener> listeners = Collections.synchronizedList(new CopyOnWriteArrayList<IEvolutionListener>());
	
	private List<Double> maxFitnessEachEpoch = new ArrayList<Double>();
	
	public Evolution(IPopulation population, IOrganismEvaluator evaluator, int runs, int epochs) {
		this.population = population;
		this.evaluator = evaluator;
		this.runs = runs;
		this.epochs = epochs;
	}
	
	@Override
	protected void execute() {
		
		// Inform listeners we're starting a set of evolution runs.
		onEvolutionStart(population);
		
		for (int i = 0; i < runs; i++) {
			
			currentRun = i;
			
			for (int j = 0; j < epochs; j++) {
				
				currentEpoch = j;
				
				for(IOrganism org : population.getOrganisms())
					org.getGenome().validate();
				
				// Inform listeners we're starting a new Epoch.
				onEpochStart(i, j, population);
				
				// Execute the Epoch.
				epoch(population, j);
				
				// Inform the listeners the Epoch is complete.
				onEpochComplete(i, j, population);
			}
		}
		
		// Inform the listeners the evolution runs are complete.
		onEvolutionComplete(population);
	}
	
	public boolean epoch(IPopulation population, int generation) {

		boolean esito = false;
		boolean win = false;

			// Evaluate each organism if exist the winner.........
			// flag and store only the first winner

			double max_fitness_of_epoch = 0.0;

			for (IOrganism organism : population.getOrganisms()) {
				// Evaluate each organism.
				esito = evaluator.evaluate(organism);

				if (organism.getFitness() > max_fitness_of_epoch)
					max_fitness_of_epoch = organism.getFitness();
				
				// if is a winner , store a flag
				if (esito)
					win = true;
			}
			maxFitnessEachEpoch.add(max_fitness_of_epoch);
			
			// Compute average and max fitness for each species
			for (ISpecies specie : population.getSpecies()) {
				specie.computeAverageFitness();
				specie.computeMaxFitness();
			}

			// if exist a winner write to file
			if (win)
				logger.debug("in generation " + generation + " i have found at leat one WINNER");

			// wait an epoch and make a reproduction of the best species
			population.epoch(generation);
			
			return win;
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
	
	public void setPopulation(IPopulation population) {
		this.population = population;
	}
}