package org.brainfarm.java.feat.api.operators;

import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.params.IEvolutionConstants;

public interface ISpeciationStrategy extends IEvolutionConstants {

	public void speciate(IPopulation pop);

	public void addOrganismToSpecies(IPopulation pop, IOrganism organism);
	
}