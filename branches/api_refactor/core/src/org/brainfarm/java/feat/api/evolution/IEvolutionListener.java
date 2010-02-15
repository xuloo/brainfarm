package org.brainfarm.java.feat.api.evolution;

import org.brainfarm.java.feat.api.IEvolution;

public interface IEvolutionListener {

	void onEvolutionStart(IEvolution evolution);
	
	void onEpochStart(IEvolution evolution);
	
	void onEpochComplete(IEvolution evolution);
	
	void onEvolutionComplete(IEvolution evolution);
}
