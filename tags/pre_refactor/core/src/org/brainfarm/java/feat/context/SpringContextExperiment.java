package org.brainfarm.java.feat.context;

import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.population.PopulationFactory;

public class SpringContextExperiment extends AbstractExperiment {

	@Override
	public IPopulation getPopulation(INeatContext context) {
		return PopulationFactory.getFactory(this).getPopulation(context);
	}

}
