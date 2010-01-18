package org.brainfarm.java.neat.api.evolution;

public interface IEvolutionListener {

	void onEvolutionStart(IEvolution evolution);
	
	void onEpochStart(IEvolution evolution);
	
	void onEpochComplete(IEvolution evolution);
	
	void onEvolutionComplete(IEvolution evolution);
}
