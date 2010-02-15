package org.brainfarm.java.feat.evaluators;

import java.lang.reflect.Constructor;

import org.brainfarm.java.feat.EvolutionStrategy;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.evaluators.EvaluatorFactory;
import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;

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