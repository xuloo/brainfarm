package org.brainfarm.java.feat.api.evaluators;

import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;

public interface IEvaluatorFactory {

	public abstract IOrganismEvaluator getEvaluator(INeatContext context);
}
