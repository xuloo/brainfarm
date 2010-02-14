package org.brainfarm.java.feat.ann;

import org.brainfarm.java.feat.api.evolution.IEvolutionFitness;
import org.brainfarm.java.feat.api.evolution.IEvolutionInput;
import org.brainfarm.java.feat.api.evolution.IEvolutionOutput;
import org.brainfarm.java.feat.experiment.Experiment;

public class NeatExperiment extends Experiment {

	private IEvolutionFitness fitnessImpl;
	
	private IEvolutionInput inputImpl;
	
	private IEvolutionOutput outputImpl;

	public void setFitness(IEvolutionFitness fitnessImpl) {
		this.fitnessImpl = fitnessImpl;
	}

	public IEvolutionFitness getFitness() {
		return fitnessImpl;
	}

	public void setInput(IEvolutionInput inputImpl) {
		this.inputImpl = inputImpl;
	}

	public IEvolutionInput getInput() {
		return inputImpl;
	}

	public void setOutput(IEvolutionOutput outputImpl) {
		this.outputImpl = outputImpl;
	}

	public IEvolutionOutput getOutput() {
		return outputImpl;
	}
}
