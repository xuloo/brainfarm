package org.brainfarm.java.neat.api.evolution;

public interface IEvolutionInput {

	int getNumSamples();

	int getNumUnit();

	double getInput(int _plist[]);
}
