package org.brainfarm.java.feat.api.context;

import org.brainfarm.java.feat.Evolution;
import org.brainfarm.java.feat.Neat;
import org.brainfarm.java.feat.api.evolution.IEvolutionFitness;
import org.brainfarm.java.feat.api.evolution.IEvolutionInput;
import org.brainfarm.java.feat.api.evolution.IEvolutionOutput;
import org.brainfarm.java.feat.api.experiment.IExperiment;
import org.brainfarm.java.feat.context.INeatContextListener;

public interface INeatContext {
	
	Evolution getEvolution();
	
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
