package org.brainfarm.java.feat.api.operators;

import java.util.List;

import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ISpecies;
import org.brainfarm.java.feat.api.params.IEvolutionConstants;

public interface IReproductionStrategy extends IEvolutionConstants {

	public void reproduce(int generation, IPopulation pop, List<ISpecies> sorted_species);

}
