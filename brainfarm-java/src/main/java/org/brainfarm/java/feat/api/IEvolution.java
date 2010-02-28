package org.brainfarm.java.feat.api;

import java.util.List;

import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;

/**
 * Defines the methods a class must implement to run an experiment.
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public interface IEvolution extends Runnable {

	int getRun();
	
	int getTotalRuns();
	
	int getEpoch();
	
	int getTotalEpochs();
	
	IPopulation getPopulation();
	
	boolean hasWinner();
	
	IOrganism getWinner();
	
	IOrganism getSuperWinner();
	
	List<Double> getMaxFitnessEachEpoch();
	
	void addListener(IEvolutionListener listener);
	
	void removeListener(IEvolutionListener listener);
}
