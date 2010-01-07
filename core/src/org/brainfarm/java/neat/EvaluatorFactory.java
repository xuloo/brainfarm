package org.brainfarm.java.neat;

import org.brainfarm.java.neat.api.IEvaluatorFactory;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.neat.api.types.DataSource;
import org.brainfarm.java.neat.context.IExperiment;

public abstract class EvaluatorFactory implements IEvaluatorFactory {

	public static IEvaluatorFactory getFactory(IExperiment experiment) {
		DataSource dataSource = experiment.getDataSource();
		
		if (dataSource == DataSource.CLASS) {
			return new ClassEvaluatorFactory();
		}
		
		return null;
	}
	
	

}
