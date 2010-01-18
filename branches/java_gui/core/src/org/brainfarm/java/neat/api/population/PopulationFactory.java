package org.brainfarm.java.neat.api.population;

import org.brainfarm.java.neat.api.enums.StartFrom;
import org.brainfarm.java.neat.context.IExperiment;
import org.brainfarm.java.neat.population.GenomePopulationFactory;

public abstract class PopulationFactory implements IPopulationFactory {

	public static IPopulationFactory getFactory(IExperiment experiment) {
		if (experiment.getStartFrom() == StartFrom.GENOME) {
			return new GenomePopulationFactory();
		}
		
		return null;
	}
}
