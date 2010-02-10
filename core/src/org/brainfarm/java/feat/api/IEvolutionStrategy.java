package org.brainfarm.java.feat.api;

import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.feat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.feat.api.operators.ISpeciationStrategy;
import org.brainfarm.java.feat.context.IExperiment;

public interface IEvolutionStrategy {

	public abstract void reset();
	
	public abstract IOrganismEvaluator getOrganismEvaluator();

	public abstract ICrossoverStrategy getCrossoverStrategy();

	public abstract IMutationStrategy getMutationStrategy();

	public abstract IReproductionStrategy getReproductionStrategy();

	public abstract ISpeciationStrategy getSpeciationStrategy();

	public abstract IPopulationInitializationStrategy getPopulationInitializationStrategy();

	public abstract Class<?> getNodeClass();
	public abstract void setNodeClass(Class<?> nodeClass);

	public abstract Class<?> getNetworkClass();

	public abstract Class<?> getLinkClass();

	public abstract Class<?> getGenomeClass();

	public abstract Class<?> getOrganismClass();

	public abstract Class<?> getEvaluatorClass();
	public abstract void setEvaluatorClass(Class<?> evaluatorClass);
	
	public abstract void setActiveExperiment(IExperiment experiment, INeatContext context);
}
