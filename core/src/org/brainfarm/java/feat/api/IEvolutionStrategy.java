package org.brainfarm.java.feat.api;

import org.brainfarm.java.feat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.feat.api.operators.ISpeciationStrategy;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;

/**
 * 
 * @author dtuohy
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public interface IEvolutionStrategy {

	public abstract void setClassLoader(ClassLoader classLoader);
	
	public abstract IEvolutionParameters getEvolutionParameters();
	public abstract void setEvolutionParameters(IEvolutionParameters evolutionParameters);
	
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
	public abstract void setNodeClassName(String nodeClassName);

	public abstract Class<?> getNetworkClass();
	public abstract void setNetworkClassName(String networkClassName);

	public abstract Class<?> getLinkClass();
	public abstract void setLinkClassName(String networkClassName);

	public abstract Class<?> getGenomeClass();
	public abstract void setGenomeClassName(String networkClassName);

	public abstract Class<?> getOrganismClass();
	public abstract void setOrganismClassName(String networkClassName);
	
	public abstract Class<?> getSpeciesClass();
	public abstract void setSpeciesClassName(String speciesClassName);
	
	public abstract Class<?> getGeneClass();
	public abstract void setGeneClassName(String geneClassName);
	
	public abstract Class<?> getInnovationClass();
	public abstract void setInnovationClassName(String innovationClassName);
}
