package org.brainfarm.java.feat.api;

import org.brainfarm.java.feat.api.IOrganismEvaluator;
import org.brainfarm.java.feat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.feat.api.operators.ISpeciationStrategy;

public interface IEvolutionStrategy {

	public abstract void reset();

	public abstract IOrganismEvaluator getOrganismEvaluator();

	public abstract void setOrganismEvaluator(
			IOrganismEvaluator organismEvaluator);

	public abstract ICrossoverStrategy getCrossoverStrategy();

	public abstract IMutationStrategy getMutationStrategy();

	public abstract IReproductionStrategy getReproductionStrategy();

	public abstract ISpeciationStrategy getSpeciationStrategy();

	public abstract IPopulationInitializationStrategy getPopulationInitializationStrategy();

	public abstract Class<?> getNodeClass();

	public abstract Class<?> getNetworkClass();

	public abstract Class<?> getLinkClass();

	public abstract Class<?> getGenomeClass();

	public abstract Class<?> getOrganismClass();

	public abstract void setNodeClass(Class<?> nodeClass);

	public abstract void setNetworkClass(Class<?> networkClass);

	public abstract void setLinkClass(Class<?> linkClass);

	public abstract void setGenomeClass(Class<?> genomeClass);

	public abstract void setOrganismClass(Class<?> organismClass);

	public abstract void setCrossoverStrategy(
			ICrossoverStrategy crossoverStrategy);

	public abstract void setMutationStrategy(IMutationStrategy mutationStrategy);

	public abstract void setPopulationInitializationStrategy(
			IPopulationInitializationStrategy populationInitializationStrategy);

	public abstract void setReproductionStrategy(
			IReproductionStrategy reproductionStrategy);

	public abstract void setSpeciationStrategy(
			ISpeciationStrategy speciationStrategy);

}