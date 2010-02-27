package org.brainfarm.java.feat;

import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IOrganismEvaluator;
import org.brainfarm.java.feat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.feat.api.operators.ISpeciationStrategy;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.brainfarm.java.feat.operators.DefaultCrossoverStrategy;
import org.brainfarm.java.feat.operators.DefaultMutationStrategy;
import org.brainfarm.java.feat.operators.DefaultPopulationInitializationStrategy;
import org.brainfarm.java.feat.operators.DefaultReproductionStrategy;
import org.brainfarm.java.feat.operators.DefaultSpeciationStrategy;

/**
 * Manages specializations. The EvolutionStrategy holds references to the various strategies used
 * during evolution of a population. It also holds references to the concrete types used by the 
 * strategies.  
 * 
 * @author dtuohy
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class EvolutionStrategy implements IEvolutionStrategy {

	/** Data Class Default Class Names */
	public static String DEFAULT_NODE_CLASS_NAME = Node.class.getName();
	public static String DEFAULT_NETWORK_CLASS_NAME = Network.class.getName();
	public static String DEFAULT_LINK_CLASS_NAME = Link.class.getName();
	public static String DEFAULT_GENOME_CLASS_NAME = Genome.class.getName();
	public static String DEFAULT_ORGANISM_CLASS_NAME = Organism.class.getName();
	public static String DEFAULT_SPECIES_CLASS_NAME = Species.class.getName();
	public static String DEFAULT_GENE_CLASS_NAME = Gene.class.getName();
	public static String DEFAULT_INNOVATION_CLASS_NAME = Innovation.class.getName();
	/** Logic Class Default Class Names */
	public static String DEFAULT_CROSSOVER_CLASS_NAME = DefaultCrossoverStrategy.class.getName();
	public static String DEFAULT_MUTATION_CLASS_NAME = DefaultMutationStrategy.class.getName();
	public static String DEFAULT_POPULATION_INITIALIZATION_CLASS_NAME = DefaultPopulationInitializationStrategy.class.getName();
	public static String DEFAULT_REPRODUCTION_CLASS_NAME = DefaultReproductionStrategy.class.getName();
	public static String DEFAULT_SPECIATION_CLASS_NAME = DefaultSpeciationStrategy.class.getName();

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

	/**
	 * Evolution Parameters that will be used for all the objects
	 * retrieved from this factory.
	 */
	protected IEvolutionParameters evolutionParameters;

	/** Data Classes - the data manipulated by the FEAT algorithm*/
	protected Class<?> nodeClass;
	protected Class<?> networkClass;
	protected Class<?> linkClass;
	protected Class<?> genomeClass;
	protected Class<?> organismClass;
	protected Class<?> speciesClass;
	protected Class<?> geneClass;
	protected Class<?> innovationClass;
	protected Class<?> evaluatorClass;

	protected String nodeClassName;
	protected String networkClassName;
	protected String linkClassName;
	protected String genomeClassName;
	protected String organismClassName;
	protected String speciesClassName;
	protected String geneClassName;
	protected String innovationClassName;

	/** Logic Classes - encapsulate various parts of the FEAT algorithm */
	protected ICrossoverStrategy crossoverStrategy;
	protected IMutationStrategy mutationStrategy;
	protected IPopulationInitializationStrategy populationInitializationStrategy;
	protected IReproductionStrategy reproductionStrategy;
	protected ISpeciationStrategy speciationStrategy;

	protected String crossoverClassName;
	protected String mutationClassName;
	protected String populationInitializationClassName;
	protected String reproductionClassName;
	protected String speciationClassName;

	/**
	 * Constructor. Creates a new EvolutionStrategy.
	 * EvolutionStrategy is not usually called directly from code but is loaded as a Bean 
	 * from the Experiment's Spring Context file when the experiment is loaded.
	 */
	public EvolutionStrategy() {

	}

	/**
	 * Initialises the default properties for this factory.
	 */
	public void refresh() {

		log.debug("Resetting EvolutionStrategy Defaults");
		
		/** Refresh Logic Class specifications */
		crossoverClassName 					= (crossoverClassName == null) 					? DEFAULT_CROSSOVER_CLASS_NAME 		: crossoverClassName;
		mutationClassName 					= (mutationClassName == null) 					? DEFAULT_MUTATION_CLASS_NAME 		: mutationClassName;
		populationInitializationClassName 	= (populationInitializationClassName == null)	? DEFAULT_POPULATION_INITIALIZATION_CLASS_NAME : populationInitializationClassName;
		reproductionClassName 				= (reproductionClassName == null) 				? DEFAULT_REPRODUCTION_CLASS_NAME 	: reproductionClassName;
		speciationClassName 				= (speciationClassName == null) 				? DEFAULT_SPECIATION_CLASS_NAME 		: speciationClassName;
		
		crossoverStrategy 					= (ICrossoverStrategy)instantiate(crossoverClassName,IEvolutionParameters.class,evolutionParameters);
		mutationStrategy 					= (IMutationStrategy)instantiate(mutationClassName,IEvolutionParameters.class,evolutionParameters);
		populationInitializationStrategy 	= (IPopulationInitializationStrategy)instantiate(populationInitializationClassName,IEvolutionStrategy.class,this);
		reproductionStrategy 				= (IReproductionStrategy)instantiate(reproductionClassName,IEvolutionStrategy.class,this);
		speciationStrategy 					= (ISpeciationStrategy)instantiate(speciationClassName,IEvolutionParameters.class,evolutionParameters);

		/** Refresh Data Class specifications */
		nodeClassName 			= (nodeClassName == null) 			? DEFAULT_NODE_CLASS_NAME 		: nodeClassName;
		networkClassName 		= (networkClassName == null) 		? DEFAULT_NETWORK_CLASS_NAME 	: networkClassName;
		linkClassName 			= (linkClassName == null) 			? DEFAULT_LINK_CLASS_NAME 		: linkClassName;
		genomeClassName 		= (genomeClassName == null) 		? DEFAULT_GENOME_CLASS_NAME 	: genomeClassName;
		organismClassName		= (organismClassName == null) 		? DEFAULT_ORGANISM_CLASS_NAME 	: organismClassName;
		speciesClassName		= (speciesClassName == null) 		? DEFAULT_SPECIES_CLASS_NAME 	: speciesClassName;
		geneClassName			= (geneClassName == null) 			? DEFAULT_GENE_CLASS_NAME 		: geneClassName;
		innovationClassName		= (innovationClassName == null) 	? DEFAULT_INNOVATION_CLASS_NAME	: innovationClassName;

		nodeClass 		= null;
		networkClass 	= null;
		linkClass 		= null;
		genomeClass 	= null;
		organismClass 	= null;
		speciesClass	= null;
		geneClass		= null;
		innovationClass = null;
	}

	@SuppressWarnings("unchecked")
	private Object instantiate(String className, Class parameterClass, Object parameter) {
		try {
			Class c = classLoader.loadClass(className);
			Constructor<?> constructor = c.getConstructor(parameterClass);
			return constructor.newInstance(parameter);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problem creating class for string " + className);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCrossoverClassName(String crossoverClassName) {
		this.crossoverClassName = crossoverClassName;
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

	@Override
	public void setEvolutionParameters(IEvolutionParameters evolutionParameters) {
		this.evolutionParameters = evolutionParameters;

		refresh();
	}

	@Override
	public IEvolutionParameters getEvolutionParameters() {
		return evolutionParameters;
	}

	@Override
	public Class<?> getSpeciesClass() {

		if (speciesClass == null) {
			try {
				return speciesClass = classLoader.loadClass(speciesClassName);
			} catch (Exception e) {
				System.out.println("Problem creating class for string " + speciesClassName);
			}
		}

		return speciesClass;
	}

	@Override
	public void setSpeciesClassName(String speciesClassName) {
		this.speciesClassName = speciesClassName;
	}

	@Override
	public Class<?> getGeneClass() {

		if (geneClass == null) {
			try {
				return geneClass = classLoader.loadClass(geneClassName);
			} catch (Exception e) {
				System.out.println("Problem creating class for string " + geneClassName);
			}
		}

		return geneClass;
	}

	@Override
	public void setGeneClassName(String geneClassName) {
		this.geneClassName = geneClassName;
	}

	@Override
	public Class<?> getInnovationClass() {
		if (innovationClass == null) {
			try {
				return innovationClass = classLoader.loadClass(innovationClassName);
			} catch (Exception e) {
				System.out.println("Problem creating class for string " + innovationClassName);
			}
		}

		return innovationClass;
	}

	@Override
	public void setInnovationClassName(String innovationClassName) {
		this.innovationClassName = innovationClassName;
	}

	@Override
	public void setCrossoverStrategy(ICrossoverStrategy crossoverStrategy) {
		this.crossoverStrategy = crossoverStrategy;
	}
	
	@Override
	public void setMutationClassName(String mutationClassName) {
		this.mutationClassName = mutationClassName;
	}

	@Override
	public void setPopulationInitializationClassName(
			String populationInitializationClassName) {
		this.populationInitializationClassName = populationInitializationClassName;
	}

	@Override
	public void setReproductionClassName(String reproductionClassName) {
		this.reproductionClassName = reproductionClassName;
	}

	@Override
	public void setSpeciationClassName(String speciationClassName) {
		this.speciationClassName = speciationClassName;
	}
}
