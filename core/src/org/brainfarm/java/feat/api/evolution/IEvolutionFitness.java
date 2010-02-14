package org.brainfarm.java.feat.api.evolution;

public interface IEvolutionFitness {
	
	double[] computeFitness(int sample, int numNodes, double out[][], double tgt[][]);
}
