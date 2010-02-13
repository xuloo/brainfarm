package org.brainfarm.java.feat.api.population;

import org.brainfarm.java.feat.api.experiment.IExperiment;

public abstract class PopulationFactory implements IPopulationFactory {

	public static IPopulationFactory getFactory(IExperiment experiment) {
		
		return null;
	}
}
