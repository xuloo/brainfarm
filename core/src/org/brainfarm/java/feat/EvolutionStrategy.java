package org.brainfarm.java.feat;

import java.lang.reflect.Constructor;

import org.brainfarm.java.feat.ann.NeatOrganismEvaluator;
import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.feat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.feat.api.operators.ISpeciationStrategy;
import org.brainfarm.java.feat.context.IExperiment;
import org.brainfarm.java.feat.operators.DefaultCrossoverStrategy;
import org.brainfarm.java.feat.operators.DefaultMutationStrategy;
import org.brainfarm.java.feat.operators.DefaultPopulationInitializationStrategy;
import org.brainfarm.java.feat.operators.DefaultReproductionStrategy;
import org.brainfarm.java.feat.operators.DefaultSpeciationStrategy;

public class EvolutionStrategy implements IEvolutionStrategy {
	
	public static Class<?> DEFAULT_EVALUATOR_CLASS = NeatOrganismEvaluator.class;
	
	protected static IEvolutionStrategy instance;
	
	//evaluator for IOrganisms in the current experiment
	protected IOrganismEvaluator organismEvaluator;

	/** Data Classes - the data manipulated by the FEAT algorithm*/
	protected Class<?> nodeClass;
	protected Class<?> networkClass;
	protected Class<?> linkClass;
	protected Class<?> genomeClass;
	protected Class<?> organismClass;
	protected Class<?> evaluatorClass;

	/** Logic Classes - encapsulate various parts of the FEAT algorithm */
	protected ICrossoverStrategy crossoverStrategy;
	protected IMutationStrategy mutationStrategy;
	protected IPopulationInitializationStrategy populationInitializationStrategy;
	protected IReproductionStrategy reproductionStrategy;
	protected ISpeciationStrategy speciationStrategy;
	
	public EvolutionStrategy() {
		reset();
	}
	
	public static IEvolutionStrategy getInstance() {
		if (instance == null) {
			try {
				instance = new EvolutionStrategy();
			} catch (Exception e) {
				System.out.println("Problem instantiating the default strategy factory \n" + e.getMessage());
			}
		}
		
		return instance;
	}
	
	public void reset() {
		//initialize with defaults
		crossoverStrategy = new DefaultCrossoverStrategy();
		mutationStrategy = new DefaultMutationStrategy();
		populationInitializationStrategy = new DefaultPopulationInitializationStrategy();
		reproductionStrategy = new DefaultReproductionStrategy();
		speciationStrategy = new DefaultSpeciationStrategy();
		nodeClass = Node.class;
		networkClass = Network.class;
		linkClass = Link.class;
		genomeClass = Genome.class;
		organismClass = Organism.class;
	}
	
	public void setActiveExperiment(IExperiment experiment, INeatContext context) {
		
		if (evaluatorClass == null) {
			evaluatorClass = DEFAULT_EVALUATOR_CLASS;
		}
		
		try {
			Constructor<?> c = evaluatorClass.getConstructor(INeatContext.class);
			organismEvaluator = (IOrganismEvaluator)c.newInstance(context);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		System.out.println("organism evaluator: " + evaluatorClass + " " + organismEvaluator);
	}
	
	public static void setStrategyFactory(IEvolutionStrategy strategyFactory) {
		instance = strategyFactory;
	}

	@Override
	public ICrossoverStrategy getCrossoverStrategy() {
		return crossoverStrategy;
	}

	@Override
	public Class<?> getEvaluatorClass() {
		return evaluatorClass;
	}
	
	@Override
	public void setEvaluatorClass(Class<?> evaluatorClass) {
		this.evaluatorClass = evaluatorClass;
	}

	@Override
	public Class<?> getGenomeClass() {
		return genomeClass;
	}

	@Override
	public Class<?> getLinkClass() {
		return linkClass;
	}

	@Override
	public IMutationStrategy getMutationStrategy() {
		return mutationStrategy;
	}

	@Override
	public Class<?> getNetworkClass() {
		return networkClass;
	}

	@Override
	public Class<?> getNodeClass() {
		return nodeClass;
	}
	
	@Override
	public void setNodeClass(Class<?> nodeClass) {
		this.nodeClass = nodeClass;
		System.out.println("node class set to " + nodeClass);
	}

	@Override
	public Class<?> getOrganismClass() {
		return organismClass;
	}

	@Override
	public IOrganismEvaluator getOrganismEvaluator() {
		return organismEvaluator;
	}

	@Override
	public IPopulationInitializationStrategy getPopulationInitializationStrategy() {
		System.out.println("Getting population initialisation strategy " + populationInitializationStrategy);
		return populationInitializationStrategy;
	}

	@Override
	public IReproductionStrategy getReproductionStrategy() {
		return reproductionStrategy;
	}

	@Override
	public ISpeciationStrategy getSpeciationStrategy() {
		return speciationStrategy;
	}
}
