package org.brainfarm.java.feat.api.operators;

import java.util.List;

import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ISpecies;

public interface IReproductionStrategy {

	public boolean reproduce(ISpecies species, int generation, IPopulation pop, List<ISpecies> sorted_species);

}
