package org.brainfarm.java.feat.api.operators;

import java.util.List;

import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ISpecies;
import org.brainfarm.java.feat.api.params.IEvolutionConstants;

public interface IReproductionStrategy extends IEvolutionConstants {

	public boolean reproduce(ISpecies species, int generation, IPopulation pop, List<ISpecies> sorted_species);

}
