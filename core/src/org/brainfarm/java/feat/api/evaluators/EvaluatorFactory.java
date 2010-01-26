package org.brainfarm.java.feat.api.evaluators;

import org.brainfarm.java.feat.api.enums.DataSource;
import org.brainfarm.java.feat.context.IExperiment;
import org.brainfarm.java.feat.evaluators.ClassEvaluatorFactory;

public abstract class EvaluatorFactory implements IEvaluatorFactory {

	public static IEvaluatorFactory getFactory(IExperiment experiment) {
		DataSource dataSource = experiment.getDataSource();
		
		if (dataSource == DataSource.CLASS) {
			return new ClassEvaluatorFactory();
		}
		
		return null;
	}
	
	

}
