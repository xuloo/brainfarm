package org.brainfarm.java.neat.api.operators;

import java.util.List;

import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.ISpecies;

public interface IReproductionStrategy {

	public boolean reproduce(ISpecies species, int generation, IPopulation pop, List<ISpecies> sorted_species);

}
