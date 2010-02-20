package org.brainfarm.java.feat.api;

import org.brainfarm.java.feat.api.params.IEvolutionParameters;

public interface IEvolutionContext {
	
	IEvolution getEvolution();
	
	IEvolutionParameters getEvolutionParameters();
	void setEvolutionParameters(IEvolutionParameters evolutionParameters);
	
	IExperiment getExperiment();
	void setExperiment(IExperiment experiment);
	
	public abstract void addListener(IEvolutionContextListener listener);	
	public abstract void removeListener(IEvolutionContextListener listener);
}
