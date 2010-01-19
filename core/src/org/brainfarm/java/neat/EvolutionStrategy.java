package org.brainfarm.java.neat;

import org.brainfarm.java.neat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.neat.api.operators.IMutationStrategy;
import org.brainfarm.java.neat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.neat.api.operators.IReproductionStrategy;
import org.brainfarm.java.neat.api.operators.ISpeciationStrategy;
import org.brainfarm.java.neat.operators.DefaultCrossoverStrategy;
import org.brainfarm.java.neat.operators.DefaultMutationStrategy;
import org.brainfarm.java.neat.operators.DefaultPopulationInitializationStrategy;
import org.brainfarm.java.neat.operators.DefaultReproductionStrategy;
import org.brainfarm.java.neat.operators.DefaultSpeciationStrategy;

/**
 * Singleton providing access to methods for executing various
 * pieces of the NEAT algorithm.
 * 
 * @author dtuohy
 *
 */
public class EvolutionStrategy {

	public static EvolutionStrategy _instance;
	
	ICrossoverStrategy crossoverStrategy;
	IMutationStrategy mutationStrategy;
	IPopulationInitializationStrategy populationInitializationStrategy;
	IReproductionStrategy reproductionStrategy;
	ISpeciationStrategy speciationStrategy;
	
	private EvolutionStrategy(){
		crossoverStrategy = new DefaultCrossoverStrategy();
		mutationStrategy = new DefaultMutationStrategy();
		populationInitializationStrategy = new DefaultPopulationInitializationStrategy();
		reproductionStrategy = new DefaultReproductionStrategy();
		speciationStrategy = new DefaultSpeciationStrategy();
	}
	
	public static EvolutionStrategy getInstance(){
		if(_instance == null)
			_instance = new EvolutionStrategy();
		return _instance;
	}
	
	public ICrossoverStrategy getCrossoverStrategy() {
		return crossoverStrategy;
	}
	
	public IMutationStrategy getMutationStrategy(){
		return mutationStrategy;
	}
	
	public IReproductionStrategy getReproductionStrategy(){
		return reproductionStrategy;
	}
	
	public ISpeciationStrategy getSpeciationStrategy(){
		return speciationStrategy;
	}
	
	public IPopulationInitializationStrategy getPopulationInitializationStrategy(){
		return populationInitializationStrategy;
	}
}