package org.brainfarm.java.neat;

import org.brainfarm.java.neat.api.IPopulationFactory;
import org.brainfarm.java.neat.api.types.StartFrom;
import org.brainfarm.java.neat.context.IExperiment;

public abstract class PopulationFactory implements IPopulationFactory {

	public static IPopulationFactory getFactory(IExperiment experiment) {
		if (experiment.getStartFrom() == StartFrom.GENOME) {
			return new GenomePopulationFactory();
		}
		
		return null;
	}
}
