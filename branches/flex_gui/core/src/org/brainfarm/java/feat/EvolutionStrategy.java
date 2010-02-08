package org.brainfarm.java.feat;

import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.feat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.feat.api.operators.IFeatFactory;
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.feat.api.operators.ISpeciationStrategy;

public class EvolutionStrategy implements IEvolutionStrategy {
	
	public static final Class<? extends EvolutionStrategy> DEFAULT_STRATEGY_CLASS = FeatEvolutionStrategy.class;
	
	public static Class<? extends EvolutionStrategy> STRATEGY_CLASS;
	
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
	//TODO: Eliminate this field - make FeatFactory statically invokable itself
	protected IFeatFactory modelObjectFactory;
	
	public static IEvolutionStrategy getInstance() {
		if (instance == null) {
			try {
				instance = (IEvolutionStrategy)DEFAULT_STRATEGY_CLASS.newInstance();
			} catch (Exception e) {
				System.out.println("Problem instantiating the default strategy \n" + e.getMessage());
			}
		}
		
		return instance;
	}
	
	public static void setEvalutionStrategyClass(Class<? extends EvolutionStrategy> strategyClass) {
		STRATEGY_CLASS = strategyClass;
	}

	@Override
	public ICrossoverStrategy getCrossoverStrategy() {
		return null;
	}

	@Override
	public Class<?> getEvaluatorClass() {
		return null;
	}

	@Override
	public Class<?> getGenomeClass() {
		return null;
	}

	@Override
	public Class<?> getLinkClass() {
		return null;
	}

	@Override
	public IFeatFactory getModelObjectFactory() {
		return null;
	}

	@Override
	public IMutationStrategy getMutationStrategy() {
		return null;
	}

	@Override
	public Class<?> getNetworkClass() {
		return null;
	}

	@Override
	public Class<?> getNodeClass() {
		return null;
	}

	@Override
	public Class<?> getOrganismClass() {
		return null;
	}

	@Override
	public IOrganismEvaluator getOrganismEvaluator() {
		return null;
	}

	@Override
	public IPopulationInitializationStrategy getPopulationInitializationStrategy() {
		return null;
	}

	@Override
	public IReproductionStrategy getReproductionStrategy() {
		return null;
	}

	@Override
	public ISpeciationStrategy getSpeciationStrategy() {
		return null;
	}
}
