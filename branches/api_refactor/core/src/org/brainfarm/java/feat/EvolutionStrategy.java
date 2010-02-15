package org.brainfarm.java.feat;

import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IOrganismEvaluator;
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
 * Singleton providing access to customization classes
 * for customizing various pieces of the FEAT algorithm.
 * 
 * @author dtuohy
 *
 */
public class EvolutionStrategy implements IEvolutionStrategy {

	//evaluator for IOrganisms in the current experiment
	private IOrganismEvaluator organismEvaluator;

	/** Data Classes - the data manipulated by the FEAT algorithm*/
	private Class<?> nodeClass;
	private Class<?> networkClass;
	private Class<?> linkClass;
	private Class<?> genomeClass;
	private Class<?> organismClass;

	/** Logic Classes - encapsulate various parts of the FEAT algorithm */
	private ICrossoverStrategy crossoverStrategy;
	private IMutationStrategy mutationStrategy;
	private IPopulationInitializationStrategy populationInitializationStrategy;
	private IReproductionStrategy reproductionStrategy;
	private ISpeciationStrategy speciationStrategy;

	public EvolutionStrategy(){
		reset();
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#reset()
	 */
	public void reset() {

		//initialize with defaults
		setCrossoverStrategy(new DefaultCrossoverStrategy());
		setMutationStrategy(new DefaultMutationStrategy());
		setPopulationInitializationStrategy(new DefaultPopulationInitializationStrategy(this));
		setReproductionStrategy(new DefaultReproductionStrategy(this));
		setSpeciationStrategy(new DefaultSpeciationStrategy());

		setNodeClass(Node.class);
		setNetworkClass(Network.class);
		setLinkClass(Link.class);
		setGenomeClass(Genome.class);
		setOrganismClass(Organism.class);
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#getOrganismEvaluator()
	 */
	public IOrganismEvaluator getOrganismEvaluator(){
		return organismEvaluator;
	}
	
	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#setOrganismEvaluator(org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator)
	 */
	public void setOrganismEvaluator(IOrganismEvaluator organismEvaluator) {
		this.organismEvaluator = organismEvaluator;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#getCrossoverStrategy()
	 */
	public ICrossoverStrategy getCrossoverStrategy() {
		return crossoverStrategy;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#getMutationStrategy()
	 */
	public IMutationStrategy getMutationStrategy(){
		return mutationStrategy;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#getReproductionStrategy()
	 */
	public IReproductionStrategy getReproductionStrategy(){
		return reproductionStrategy;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#getSpeciationStrategy()
	 */
	public ISpeciationStrategy getSpeciationStrategy(){
		return speciationStrategy;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#getPopulationInitializationStrategy()
	 */
	public IPopulationInitializationStrategy getPopulationInitializationStrategy(){
		return populationInitializationStrategy;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#getNodeClass()
	 */
	public Class<?> getNodeClass(){
		return nodeClass;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#getNetworkClass()
	 */
	public Class<?> getNetworkClass(){
		return networkClass;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#getLinkClass()
	 */
	public Class<?> getLinkClass(){
		return linkClass;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#getGenomeClass()
	 */
	public Class<?> getGenomeClass(){
		return genomeClass;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#getOrganismClass()
	 */
	public Class<?> getOrganismClass() {
		return organismClass;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#setNodeClass(java.lang.Class)
	 */
	public void setNodeClass(Class<?> nodeClass) {
		this.nodeClass = nodeClass;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#setNetworkClass(java.lang.Class)
	 */
	public void setNetworkClass(Class<?> networkClass) {
		this.networkClass = networkClass;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#setLinkClass(java.lang.Class)
	 */
	public void setLinkClass(Class<?> linkClass) {
		this.linkClass = linkClass;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#setGenomeClass(java.lang.Class)
	 */
	public void setGenomeClass(Class<?> genomeClass) {
		this.genomeClass = genomeClass;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#setOrganismClass(java.lang.Class)
	 */
	public void setOrganismClass(Class<?> organismClass) {
		this.organismClass = organismClass;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#setCrossoverStrategy(org.brainfarm.java.feat.api.operators.ICrossoverStrategy)
	 */
	public void setCrossoverStrategy(ICrossoverStrategy crossoverStrategy) {
		this.crossoverStrategy = crossoverStrategy;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#setMutationStrategy(org.brainfarm.java.feat.api.operators.IMutationStrategy)
	 */
	public void setMutationStrategy(IMutationStrategy mutationStrategy) {
		this.mutationStrategy = mutationStrategy;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#setPopulationInitializationStrategy(org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy)
	 */
	public void setPopulationInitializationStrategy(
			IPopulationInitializationStrategy populationInitializationStrategy) {
		this.populationInitializationStrategy = populationInitializationStrategy;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#setReproductionStrategy(org.brainfarm.java.feat.api.operators.IReproductionStrategy)
	 */
	public void setReproductionStrategy(IReproductionStrategy reproductionStrategy) {
		this.reproductionStrategy = reproductionStrategy;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.feat.IEvolutionStrategy#setSpeciationStrategy(org.brainfarm.java.feat.api.operators.ISpeciationStrategy)
	 */
	public void setSpeciationStrategy(ISpeciationStrategy speciationStrategy) {
		this.speciationStrategy = speciationStrategy;
	}
}