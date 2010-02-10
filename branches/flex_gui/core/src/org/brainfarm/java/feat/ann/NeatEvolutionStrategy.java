package org.brainfarm.java.feat.ann;

import org.brainfarm.java.feat.EvolutionStrategy;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.feat.context.IExperiment;

public class NeatEvolutionStrategy extends EvolutionStrategy {
	
	public NeatEvolutionStrategy() {
		reset();
	}
	
	@Override 
	public void reset() {
		super.reset();
		
		nodeClass = NeatNode.class;
		networkClass = NeatNetwork.class;
		genomeClass = NeatGenome.class;
		organismClass = NeatOrganism.class;
	}

	public void setActiveExperiment(IExperiment experiment, INeatContext context) {
		
		organismEvaluator = (organismEvaluator == null) ? new NeatOrganismEvaluator(context) : organismEvaluator;
	}
	
	public IOrganismEvaluator getOrganismEvaluator(){
		return organismEvaluator;
	}

	public Class<?> getEvaluatorClass(){
		return evaluatorClass = (evaluatorClass == null) ? NeatOrganismEvaluator.class : evaluatorClass;
	}
}
