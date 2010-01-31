package org.brainfarm.java.feat.api.evolution;

public interface IEvolutionInput {

	int getNumSamples();

	int getNumUnit();

	double getInput(int _plist[]);
}
