package org.brainfarm.java.neat.context;

import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.population.PopulationFactory;

public class SpringContextExperiment extends AbstractExperiment {

	@Override
	public IPopulation getPopulation(INeatContext context) {
		return PopulationFactory.getFactory(this).getPopulation(context);
	}

}
