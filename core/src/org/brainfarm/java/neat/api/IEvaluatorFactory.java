package org.brainfarm.java.neat.api;

import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evaluators.IOrganismEvaluator;

public interface IEvaluatorFactory {

	public abstract IOrganismEvaluator getEvaluator(INeatContext context);
}
