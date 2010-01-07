package org.brainfarm.java.neat.context;

import org.brainfarm.java.neat.EvaluatorFactory;
import org.brainfarm.java.neat.PopulationFactory;
import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evaluators.IOrganismEvaluator;

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
