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
	
	private EvolutionStrategy(){}
	
	public static EvolutionStrategy getInstance(){
		if(_instance == null)
			_instance = new EvolutionStrategy();
		return _instance;
	}
	
	public ICrossoverStrategy getCrossoverStrategy() {
		return new DefaultCrossoverStrategy();
	}
	
	public IMutationStrategy getMutationStrategy(){
		return new DefaultMutationStrategy();
	}
	
	public IReproductionStrategy getReproductionStrategy(){
		return new DefaultReproductionStrategy();
	}
	
	public ISpeciationStrategy getSpeciationStrategy(){
		return new DefaultSpeciationStrategy();
	}
	
	public IPopulationInitializationStrategy getPopulationInitializationStrategy(){
		return new DefaultPopulationInitializationStrategy();
	}
}