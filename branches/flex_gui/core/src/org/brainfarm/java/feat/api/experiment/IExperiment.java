package org.brainfarm.java.feat.api.experiment;

import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.evolution.IEvolution;

public interface IExperiment {
	
	public void refresh();
	
	public IEvolution evolution();
	
	public IEvolutionStrategy getEvolutionStrategy();
}
