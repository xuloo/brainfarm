package org.brainfarm.java.feat.api.evolution;

import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;

/**
 * Defines the methods a class must implement to run an experiment.
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public interface IEvolution {

	int getRun();
	
	int getEpoch();
	
	IPopulation getPopulation();
	
	boolean hasWinner();
	
	IOrganism getWinner();
	
	IOrganism getSuperWinner();
}
