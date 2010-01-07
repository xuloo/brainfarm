package org.brainfarm.java.neat.api.evaluators;

import org.brainfarm.java.neat.api.enums.DataSource;
import org.brainfarm.java.neat.context.IExperiment;
import org.brainfarm.java.neat.evaluators.ClassEvaluatorFactory;

public abstract class EvaluatorFactory implements IEvaluatorFactory {

	public static IEvaluatorFactory getFactory(IExperiment experiment) {
		DataSource dataSource = experiment.getDataSource();
		
		if (dataSource == DataSource.CLASS) {
			return new ClassEvaluatorFactory();
		}
		
		return null;
	}
	
	

}
