package org.brainfarm.java.neat.evaluators;

import java.lang.reflect.Constructor;

import org.brainfarm.java.neat.EvolutionStrategy;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evaluators.EvaluatorFactory;
import org.brainfarm.java.neat.api.evaluators.IOrganismEvaluator;

public class ClassEvaluatorFactory extends EvaluatorFactory {

	@Override
	public IOrganismEvaluator getEvaluator(INeatContext context) {
		Class<?> eClass = EvolutionStrategy.getInstance().getEvaluatorClass();
		try{
			Constructor<?> c = eClass.getConstructor(INeatContext.class);
			return (IOrganismEvaluator)c.newInstance(context);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}