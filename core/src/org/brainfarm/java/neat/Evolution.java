package org.brainfarm.java.neat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.brainfarm.java.neat.api.IOrganism;
import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.ISpecies;
import org.brainfarm.java.neat.api.evolution.IEvolution;
import org.brainfarm.java.neat.api.evolution.IEvolutionListener;
import org.brainfarm.java.neat.context.IExperiment;
import org.brainfarm.java.util.ThreadedCommand;

public class Evolution extends ThreadedCommand implements IEvolution {
	
	private static Logger logger = Logger.getLogger(Evolution.class);
	
	private int currentRun;
	
	private int currentEpoch;
	
	private Neat neat;
	
	private IExperiment experiment;
	
	private IPopulation population;
	
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
		
		for (int i = 0; i < neat.num_runs; i++) {
			
			currentRun = i;
			
			for (int j = 0; j < experiment.getEpoch(); j++) {
				
				currentEpoch = j;
				
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
				esito = EvolutionStrategy.getInstance().getOrganismEvaluator().evaluate(organism);

				if (organism.getFitness() > max_fitness_of_epoch)
					max_fitness_of_epoch = organism.getFitness();
				
				// if is a winner , store a flag
				if (esito) {
					win = true;
					// store only first organism
					/*if (EnvConstant.FIRST_ORGANISM_WINNER == null) {
						EnvConstant.FIRST_ORGANISM_WINNER = _organism;
					}*/

				}
			}
			maxFitnessEachEpoch.add(max_fitness_of_epoch);
			
			// Compute average and max fitness for each species
			for (ISpecies specie : population.getSpecies()) {
				specie.computeAverageFitness();
				specie.computeMaxFitness();
			}
			
			// Only print to file every print_every generations
			/*String cause1 = " ";
			String cause2 = " ";*/
			/*if (((generation % _neat.print_every) == 0) || (win)) {

				if ((generation % _neat.print_every) == 0)
					cause1 = " request";
				if (win)
					cause2 = " winner";
			}*/

			// if exist a winner write to file
			if (win) {
				logger.debug("in generation " + generation + " i have found at leat one WINNER");
				/*int conta = 0;
				itr_organism = pop.getOrganisms().iterator();
				while (itr_organism.hasNext()) {
					Organism _organism = ((Organism) itr_organism.next());
					if (_organism.winner) {
						conta++;
					}
					if (EnvConstant.SUPER_WINNER_) {
						logger
								.debug(" generation:      in this generation "
										+ generation
										+ " i have found a SUPER WINNER ");
						EnvConstant.SUPER_WINNER_ = false;

					}

				}

				logger.debug(" generation:      number of winner's is "
						+ conta);
*/
			}

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
	
	public void setNeat(Neat neat) {
		this.neat = neat;
	}
	
	public void setExperiment(IExperiment experiment) {
		this.experiment = experiment;
	}
	
	public void setPopulation(IPopulation population) {
		this.population = population;
	}
}
