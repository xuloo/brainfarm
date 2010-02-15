package org.brainfarm.java.feat.api;

import java.util.List;

import org.brainfarm.java.feat.api.evolution.IEvolutionListener;


public interface IEvolution extends Runnable {

	int getRun();
	
	int getEpoch();
	
	List<Double> getMaxFitnessEachEpoch();
	
	IPopulation getPopulation();
	
	void addListener(IEvolutionListener listener);
	void removeListener(IEvolutionListener listener);
}
