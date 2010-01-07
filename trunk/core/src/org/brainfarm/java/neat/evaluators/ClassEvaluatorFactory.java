package org.brainfarm.java.neat.evaluators;

import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evaluators.EvaluatorFactory;
import org.brainfarm.java.neat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.neat.evaluators.ClassOrganismEvaluator;

public class ClassEvaluatorFactory extends EvaluatorFactory {

	@Override
	public IOrganismEvaluator getEvaluator(INeatContext context) {
		return new ClassOrganismEvaluator(context.getNeat(), 
										  context.getFitnessImpl(), 
										  context.getInputImpl(), 
										  context.getOutputImpl());
	}

}
