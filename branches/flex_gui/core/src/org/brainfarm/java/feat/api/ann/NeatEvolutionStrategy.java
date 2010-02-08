package org.brainfarm.java.feat.api.ann;

import org.brainfarm.java.feat.EvolutionStrategy;
import org.brainfarm.java.feat.Link;
import org.brainfarm.java.feat.ann.NeatGenome;
import org.brainfarm.java.feat.ann.NeatMutationStrategy;
import org.brainfarm.java.feat.ann.NeatNetwork;
import org.brainfarm.java.feat.ann.NeatNode;
import org.brainfarm.java.feat.ann.NeatOrganism;
import org.brainfarm.java.feat.ann.NeatOrganismEvaluator;
import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.feat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.feat.api.operators.IFeatFactory;
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.feat.api.operators.ISpeciationStrategy;

public class NeatEvolutionStrategy extends EvolutionStrategy {

	public IOrganismEvaluator getOrganismEvaluator(){
		return organismEvaluator = (organismEvaluator == null) ? new NeatOrganismEvaluator() : organismEvaluator;
	}

	public ICrossoverStrategy getCrossoverStrategy() {
		return crossoverStrategy;
	}

	public IMutationStrategy getMutationStrategy(){
		return mutationStrategy = (mutationStrategy == null) ? new NeatMutationStrategy() : mutationStrategy;
	}

	public IReproductionStrategy getReproductionStrategy(){
		return reproductionStrategy;
	}

	public ISpeciationStrategy getSpeciationStrategy(){
		return speciationStrategy;
	}

	public IPopulationInitializationStrategy getPopulationInitializationStrategy(){
		return populationInitializationStrategy;
	}

	public IFeatFactory getModelObjectFactory(){
		return modelObjectFactory;
	}

	public Class<?> getNodeClass(){
		return nodeClass = (nodeClass == null) ? NeatNode.class : nodeClass;
	}

	public Class<?> getNetworkClass(){
		return networkClass = (networkClass == null) ? NeatNetwork.class : networkClass;
	}

	public Class<?> getLinkClass(){
		return linkClass = (linkClass == null) ? Link.class : linkClass;
	}

	public Class<?> getGenomeClass(){
		return genomeClass = (genomeClass == null) ? NeatGenome.class : genomeClass;
	}

	public Class<?> getOrganismClass() {
		return organismClass = (organismClass == null) ? NeatOrganism.class : organismClass;
	}

	public Class<?> getEvaluatorClass(){
		return evaluatorClass = (evaluatorClass == null) ? NeatOrganismEvaluator.class : evaluatorClass;
	}
}
