package org.brainfarm.java.neat.evaluators;

import org.brainfarm.java.neat.Neat;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evaluators.IOrganismEvaluator;

public abstract class AbstractOrganismEvaluator implements IOrganismEvaluator {

	protected Neat neat;
	
	public AbstractOrganismEvaluator(INeatContext context) {
		this.neat = context.getNeat();
	}
	
	public AbstractOrganismEvaluator(){
		
	}
	
}