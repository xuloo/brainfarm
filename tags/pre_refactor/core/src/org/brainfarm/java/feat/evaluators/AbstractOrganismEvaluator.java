package org.brainfarm.java.feat.evaluators;

import org.brainfarm.java.feat.Neat;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;

public abstract class AbstractOrganismEvaluator implements IOrganismEvaluator {

	protected Neat neat;
	
	public AbstractOrganismEvaluator(INeatContext context) {
		this.neat = context.getNeat();
	}
	
	public AbstractOrganismEvaluator(){
		
	}
	
}