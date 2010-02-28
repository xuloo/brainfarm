package org.brainfarm.java.feat.api;

public interface IEvolutionListener {

	void onEvolutionStart(IEvolution evolution);
	
	void onRunStart(IEvolution evolution);
	
	void onEpochStart(IEvolution evolution);
	
	void onEpochComplete(IEvolution evolution);
	
	void onRunComplete(IEvolution evolution);
	
	void onEvolutionComplete(IEvolution evolution);
}
