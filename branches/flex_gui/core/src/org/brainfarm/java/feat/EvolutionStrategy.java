package org.brainfarm.java.feat;

import org.apache.log4j.Logger;
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
 * Manages specialisations. The EvolutionStrategy holds references to the various strategies used
 * during evolution of a population. It also holds references to the concrete types used by the 
 * strategies.  
 * 
 * @author dtouhy
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class EvolutionStrategy implements IEvolutionStrategy {
	
	/**
	 * Logger instance.
	 */
	private static Logger log = Logger.getLogger(EvolutionStrategy.class);
	
	/**
	 * The ClassLoader used to load classes.
	 * This value is set by the ExperimentLoader when a new Experiment is loaded.
	 */
	private ClassLoader classLoader;
	
	/**
	 * Sets the ClassLoader that will be used to load classes for the strategies.
	 */
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/**
	 * Evaluator for IOrganisms in the current experiment
	 */
	protected IOrganismEvaluator organismEvaluator;

	/** Data Classes - the data manipulated by the FEAT algorithm*/
	protected Class<?> nodeClass;
	protected Class<?> networkClass;
	protected Class<?> linkClass;
	protected Class<?> genomeClass;
	protected Class<?> organismClass;
	protected Class<?> evaluatorClass;
	
	protected String nodeClassName;
	protected String networkClassName;
	protected String linkClassName;
	protected String genomeClassName;
	protected String organismClassName;

	/** Logic Classes - encapsulate various parts of the FEAT algorithm */
	protected ICrossoverStrategy crossoverStrategy;
	protected IMutationStrategy mutationStrategy;
	protected IPopulationInitializationStrategy populationInitializationStrategy;
	protected IReproductionStrategy reproductionStrategy;
	protected ISpeciationStrategy speciationStrategy;
	
	/**
	 * Constructor. Creates a new EvolutionStrategy.
	 * EvolutionStrategy is not usually called directly from code but is loaded as a Bean 
	 * from the Experiment's Spring Context file when the experiment is loaded.
	 */
	public EvolutionStrategy() {
		reset();
	}
	
	/**
	 * Initialises the default properties for this factory.
	 */
	public void reset() {
		crossoverStrategy = new DefaultCrossoverStrategy();
		mutationStrategy = new DefaultMutationStrategy();
		populationInitializationStrategy = new DefaultPopulationInitializationStrategy(this);
		reproductionStrategy = new DefaultReproductionStrategy(this);
		speciationStrategy = new DefaultSpeciationStrategy();
		
		nodeClassName = Node.class.getName();
		nodeClass = null;
		
		networkClassName = Network.class.getName();
		networkClass = null;
		
		linkClassName = Link.class.getName();
		linkClass = null;
		
		genomeClassName = Genome.class.getName();
		genomeClass = null;
		
		organismClassName = Organism.class.getName();
		organismClass = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICrossoverStrategy getCrossoverStrategy() {
		return crossoverStrategy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCrossoverStrategy(ICrossoverStrategy crossoverStrategy) {
		this.crossoverStrategy = crossoverStrategy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGenomeClassName(String genomeClassName) {
		this.genomeClassName = genomeClassName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getGenomeClass() {
		
		if (genomeClass == null) {
			try {
				return genomeClass = classLoader.loadClass(genomeClassName);
			} catch (Exception e) {
				System.out.println("Problem creating class for string " + genomeClassName);
			}
		}
		
		return genomeClass;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getLinkClass() {
		
		if (linkClass == null) {
			try {
				return linkClass = classLoader.loadClass(linkClassName);
			} catch (Exception e) {
				System.out.println("Problem creating class for string " + linkClassName);
			}
		}
		
		return linkClass;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public void setLinkClassName(String linkClassName) {
		this.linkClassName = linkClassName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMutationStrategy getMutationStrategy() {
		return mutationStrategy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMutationStrategy(IMutationStrategy mutationStrategy) {
		this.mutationStrategy = mutationStrategy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getNetworkClass() {
		
		if (networkClass == null) {
			try {
				return networkClass = classLoader.loadClass(networkClassName);
			} catch (Exception e) {
				System.out.println("Problem creating class for string " + networkClassName);
			}
		}
		
		return networkClass;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNetworkClassName(String networkClassName) {
		this.networkClassName = networkClassName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getNodeClass() {
		
		if (nodeClass == null) {
			try {
				return nodeClass = classLoader.loadClass(nodeClassName);
			} catch (Exception e) {
				System.out.println("Problem creating class for string " + nodeClassName);
			}
		}
		
		return nodeClass;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNodeClassName(String nodeClassName) {
		this.nodeClassName = nodeClassName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getOrganismClass() {
		
		if (organismClass == null) {
			try {
				return organismClass = classLoader.loadClass(organismClassName);
			} catch (Exception e) {
				System.out.println("Problem creating class for string " + organismClassName);
			}
		}
		
		return organismClass;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public void setOrganismClassName(String organismClassName) {
		this.organismClassName = organismClassName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IOrganismEvaluator getOrganismEvaluator() {
		return organismEvaluator;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOrganismEvaluator(IOrganismEvaluator organismEvaluator) {
		this.organismEvaluator = organismEvaluator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPopulationInitializationStrategy getPopulationInitializationStrategy() {
		return populationInitializationStrategy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPopulationInitializationStrategy(IPopulationInitializationStrategy populationInitializationStrategy) {
		this.populationInitializationStrategy = populationInitializationStrategy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IReproductionStrategy getReproductionStrategy() {
		return reproductionStrategy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setReproductionStrategy(IReproductionStrategy reproductionStrategy) {
		this.reproductionStrategy = reproductionStrategy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISpeciationStrategy getSpeciationStrategy() {
		return speciationStrategy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSpeciationStrategy(ISpeciationStrategy speciationStrategy) {
		this.speciationStrategy = speciationStrategy;
	}
}
