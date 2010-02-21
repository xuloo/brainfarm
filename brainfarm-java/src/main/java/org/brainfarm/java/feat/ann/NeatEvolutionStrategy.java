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
				
		nodeClassName 			= (nodeClassName == null) 			? DEFAULT_NODE_CLASS_NAME 		: NeatNode.class.getName();
		networkClassName 		= (networkClassName == null) 		? DEFAULT_NETWORK_CLASS_NAME 	: NeatNetwork.class.getName();
		genomeClassName 		= (genomeClassName == null) 		? DEFAULT_GENOME_CLASS_NAME 	: NeatGenome.class.getName();
		organismClassName		= (organismClassName == null) 		? DEFAULT_ORGANISM_CLASS_NAME 	: NeatOrganism.class.getName();

	}
}
