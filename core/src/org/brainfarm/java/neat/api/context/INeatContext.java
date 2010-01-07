package org.brainfarm.java.neat.api.context;

import org.brainfarm.java.neat.Neat;
import org.brainfarm.java.neat.api.evolution.IEvolutionFitness;
import org.brainfarm.java.neat.api.evolution.IEvolutionInput;
import org.brainfarm.java.neat.api.evolution.IEvolutionOutput;
import org.brainfarm.java.neat.context.IExperiment;
import org.brainfarm.java.neat.context.INeatContextListener;

public interface INeatContext {
	
	Runnable getEvolution();
	
	Neat getNeat();
	void setNeat(Neat neat);
	
	IExperiment getExperiment();
	void setExperiment(IExperiment experiment);
	
	IEvolutionFitness getFitnessImpl();
	void setFitnessImpl(IEvolutionFitness fitness);
	
	IEvolutionInput getInputImpl();
	void setInputImpl(IEvolutionInput input);
	
	IEvolutionOutput getOutputImpl();
	void setOutputImpl(IEvolutionOutput output);
	
	public abstract void addListener(INeatContextListener listener);
	
	public abstract void removeListener(INeatContextListener listener);
}
