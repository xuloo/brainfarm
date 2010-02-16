package org.brainfarm.java.feat.api;

import org.brainfarm.java.feat.params.EvolutionParameters;

public interface IEvolutionContext {
	
	IEvolution getEvolution();
	
	/*EvolutionParameters getNeat();
	void setNeat(EvolutionParameters neat);*/
	
	IExperiment getExperiment();
	void setExperiment(IExperiment experiment);
	
	public abstract void addListener(IEvolutionContextListener listener);
	
	public abstract void removeListener(IEvolutionContextListener listener);
}
