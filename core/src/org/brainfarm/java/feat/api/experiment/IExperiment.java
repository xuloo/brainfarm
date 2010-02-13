package org.brainfarm.java.feat.api.experiment;

import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IPopulation;

public interface IExperiment {
	
	public void refresh();
	
	public IEvolution evolution();
	
	public IEvolutionStrategy getEvolutionStrategy();
	
	public int getEpoch();
	
	public IPopulation getPopulation();
}
