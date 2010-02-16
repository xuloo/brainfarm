package org.brainfarm.java.feat.api.context;

import org.brainfarm.java.feat.Neat;
import org.brainfarm.java.feat.api.evolution.IEvolution;
import org.brainfarm.java.feat.api.experiment.IExperiment;

public interface IEvolutionContext {
	
	IEvolution getEvolution();
	
	Neat getNeat();
	void setNeat(Neat neat);
	
	IExperiment getExperiment();
	void setExperiment(IExperiment experiment);
	
	public abstract void addListener(IEvolutionContextListener listener);
	
	public abstract void removeListener(IEvolutionContextListener listener);
}
