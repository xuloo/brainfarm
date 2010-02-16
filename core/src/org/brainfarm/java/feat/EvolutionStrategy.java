package org.brainfarm.java.feat;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.ann.NeatOrganismEvaluator;
import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.feat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.feat.api.operators.ISpeciationStrategy;
import org.brainfarm.java.feat.operators.DefaultCrossoverStrategy;
import org.brainfarm.java.feat.operators.DefaultMutationStrategy;
import org.brainfarm.java.feat.operators.DefaultPopulationInitializationStrategy;
import org.brainfarm.java.feat.operators.DefaultReproductionStrategy;
import org.brainfarm.java.feat.operators.DefaultSpeciationStrategy;

/**
 * Manages specialisations. 
 * 
 * @author dtouhy
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class EvolutionStrategy implements IEvolutionStrategy {
	
	private static Logger log = Logger.getLogger(EvolutionStrategy.class);
	
	public static Class<?> DEFAULT_EVALUATOR_CLASS = NeatOrganismEvaluator.class;
	
	private ClassLoader classLoader;
	
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	//protected static IEvolutionStrategy instance;
	
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
	
	public void reset() {
		//initialize with defaults
		crossoverStrategy = new DefaultCrossoverStrategy();
		mutationStrategy = new DefaultMutationStrategy();
		populationInitializationStrategy = new DefaultPopulationInitializationStrategy(this);
		reproductionStrategy = new DefaultReproductionStrategy(this);
		speciationStrategy = new DefaultSpeciationStrategy();
		nodeClass = Node.class;
		networkClass = Network.class;
		linkClass = Link.class;
		genomeClass = Genome.class;
		organismClass = Organism.class;
	}

	@Override
	public ICrossoverStrategy getCrossoverStrategy() {
		return crossoverStrategy;
	}
	
	@Override
	public void setCrossoverStrategy(ICrossoverStrategy crossoverStrategy) {
		this.crossoverStrategy = crossoverStrategy;
	}
	
	@Override
	public void setGenomeClass(Class<?> genomeClass) {
		this.genomeClass = genomeClass;
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
	public void setLinkClass(Class<?> linkClass) {
		this.linkClass = linkClass;
	}

	@Override
	public IMutationStrategy getMutationStrategy() {
		return mutationStrategy;
	}
	
	@Override
	public void setMutationStrategy(IMutationStrategy mutationStrategy) {
		this.mutationStrategy = mutationStrategy;
	}

	@Override
	public Class<?> getNetworkClass() {
		return networkClass;
	}
	
	@Override
	public void setNetworkClass(Class<?> networkClass) {
		this.networkClass = networkClass;
	}

	@Override
	public Class<?> getNodeClass() {
		
		try {
			this.nodeClass = classLoader.loadClass(nodeClassName);
		} catch (Exception e) {
			System.out.println("Problem creating class for string " + nodeClassName);
		}
		
		return nodeClass;
	}
	
	private String nodeClassName;
	
	@Override
	public void setNodeClassName(String nodeClassName) {
		/*try {
			this.nodeClass = Class.forName(nodeClassName);
		} catch (Exception e) {
			System.out.println("Problem creating class for string " + nodeClassName);
		}*/
		this.nodeClassName = nodeClassName;
	}

	@Override
	public Class<?> getOrganismClass() {
		return organismClass;
	}
	
	@Override 
	public void setOrganismClass(Class<?> organismClass) {
		this.organismClass = organismClass;
	}

	@Override
	public IOrganismEvaluator getOrganismEvaluator() {
		return organismEvaluator;
	}
	
	@Override
	public void setOrganismEvaluator(IOrganismEvaluator organismEvaluator) {
		this.organismEvaluator = organismEvaluator;
	}

	@Override
	public IPopulationInitializationStrategy getPopulationInitializationStrategy() {
		return populationInitializationStrategy;
	}
	
	@Override
	public void setPopulationInitializationStrategy(IPopulationInitializationStrategy populationInitializationStrategy) {
		this.populationInitializationStrategy = populationInitializationStrategy;
	}

	@Override
	public IReproductionStrategy getReproductionStrategy() {
		return reproductionStrategy;
	}
	
	@Override
	public void setReproductionStrategy(IReproductionStrategy reproductionStrategy) {
		this.reproductionStrategy = reproductionStrategy;
	}

	@Override
	public ISpeciationStrategy getSpeciationStrategy() {
		return speciationStrategy;
	}
	
	@Override
	public void setSpeciationStrategy(ISpeciationStrategy speciationStrategy) {
		this.speciationStrategy = speciationStrategy;
	}
}
