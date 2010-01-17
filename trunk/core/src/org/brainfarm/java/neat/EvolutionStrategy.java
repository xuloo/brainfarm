package org.brainfarm.java.neat;

import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.IOrganism;
import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.neat.api.operators.IMutationStrategy;
import org.brainfarm.java.neat.operators.DefaultCrossoverStrategy;
import org.brainfarm.java.neat.operators.DefaultMutationStrategy;

/**
 * Singleton providing access to methods for executing the various
 * operations required by Evolution.
 * 
 * @author dtuohy
 *
 */
public class EvolutionStrategy {

	public static EvolutionStrategy _instance;
	
	private EvolutionStrategy(){
		
	}
	
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
	
}
