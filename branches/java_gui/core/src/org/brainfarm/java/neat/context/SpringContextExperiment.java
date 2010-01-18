package org.brainfarm.java.neat.context;

import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evaluators.EvaluatorFactory;
import org.brainfarm.java.neat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.neat.api.population.PopulationFactory;

public class SpringContextExperiment extends AbstractExperiment {

	@Override
	public IPopulation getPopulation(INeatContext context) {
		return PopulationFactory.getFactory(this).getPopulation(context);
	}
	
	@Override 
	public IOrganismEvaluator getEvaluator(INeatContext context) {
		return EvaluatorFactory.getFactory(this).getEvaluator(context);
	}

}
