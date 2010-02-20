package org.brainfarm.java.feat.api;

public interface IExperiment {
	
	public void refresh();
	
	public IEvolution evolution();
	
	public IEvolutionStrategy getEvolutionStrategy();
}
