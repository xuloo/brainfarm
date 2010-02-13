package org.brainfarm.java.feat.api;

import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.feat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.feat.api.operators.ISpeciationStrategy;

public interface IEvolutionStrategy {

	public abstract IOrganismEvaluator getOrganismEvaluator();
	public abstract void setOrganismEvaluator(IOrganismEvaluator organismEvaluator);

	public abstract ICrossoverStrategy getCrossoverStrategy();
	public abstract void setCrossoverStrategy(ICrossoverStrategy crossoverStrategy);

	public abstract IMutationStrategy getMutationStrategy();
	public abstract void setMutationStrategy(IMutationStrategy mutationStrategy);

	public abstract IReproductionStrategy getReproductionStrategy();
	public abstract void setReproductionStrategy(IReproductionStrategy reproductionStrategy);

	public abstract ISpeciationStrategy getSpeciationStrategy();
	public abstract void setSpeciationStrategy(ISpeciationStrategy speciationStrategy);

	public abstract IPopulationInitializationStrategy getPopulationInitializationStrategy();
	public abstract void setPopulationInitializationStrategy(IPopulationInitializationStrategy populationInitializationStrategy);

	public abstract Class<?> getNodeClass();
	public abstract void setNodeClass(Class<?> nodeClass);

	public abstract Class<?> getNetworkClass();
	public abstract void setNetworkClass(Class<?> networkClass);

	public abstract Class<?> getLinkClass();
	public abstract void setLinkClass(Class<?> networkClass);

	public abstract Class<?> getGenomeClass();
	public abstract void setGenomeClass(Class<?> networkClass);

	public abstract Class<?> getOrganismClass();
	public abstract void setOrganismClass(Class<?> networkClass);
}
