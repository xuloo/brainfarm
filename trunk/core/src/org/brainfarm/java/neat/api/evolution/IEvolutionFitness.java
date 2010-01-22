package org.brainfarm.java.neat.api.evolution;

public interface IEvolutionFitness {
	
	double getMaxFitness();
	
	double[] computeFitness(int sample, int numNodes, double out[][], double tgt[][]);

}
