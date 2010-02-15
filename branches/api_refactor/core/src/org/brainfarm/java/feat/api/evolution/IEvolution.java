package org.brainfarm.java.feat.api.evolution;

import org.brainfarm.java.feat.api.IPopulation;


public interface IEvolution {

	int getRun();
	
	int getEpoch();
	
	IPopulation getPopulation();
}
