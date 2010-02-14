package org.brainfarm.java.feat.ann;

import org.brainfarm.java.feat.EvolutionStrategy;

public class NeatEvolutionStrategy extends EvolutionStrategy {
	
	public NeatEvolutionStrategy() {
		super();
	}
	
	@Override 
	public void reset() {
		super.reset();
		
		organismEvaluator = new NeatOrganismEvaluator();
		
		nodeClass = NeatNode.class;
		networkClass = NeatNetwork.class;
		genomeClass = NeatGenome.class;
		organismClass = NeatOrganism.class;
	}
}
