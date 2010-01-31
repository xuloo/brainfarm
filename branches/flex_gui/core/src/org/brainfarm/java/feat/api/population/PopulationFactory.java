package org.brainfarm.java.feat.api.population;

import org.brainfarm.java.feat.api.enums.StartFrom;
import org.brainfarm.java.feat.context.IExperiment;
import org.brainfarm.java.feat.population.GenomePopulationFactory;

public abstract class PopulationFactory implements IPopulationFactory {

	public static IPopulationFactory getFactory(IExperiment experiment) {
		if (experiment.getStartFrom() == StartFrom.GENOME) {
			return new GenomePopulationFactory();
		}
		
		return null;
	}
}
