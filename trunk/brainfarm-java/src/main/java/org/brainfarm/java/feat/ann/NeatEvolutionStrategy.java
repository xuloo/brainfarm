package org.brainfarm.java.feat.ann;

import org.brainfarm.java.feat.EvolutionStrategy;

/**
 * Overrides the base EvolutionStrategy to provide NEAT-specific
 * strategies and classes as default.
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class NeatEvolutionStrategy extends EvolutionStrategy {
	
	/**
	 * Constructor. Creates a new NeatEvolutionStrategy.
	 */
	public NeatEvolutionStrategy() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public void refresh() {
		super.refresh();
		
		organismEvaluator = new NeatOrganismEvaluator();
		
		nodeClass = NeatNode.class;
		networkClass = NeatNetwork.class;
		genomeClass = NeatGenome.class;
		organismClass = NeatOrganism.class;
	}
}
