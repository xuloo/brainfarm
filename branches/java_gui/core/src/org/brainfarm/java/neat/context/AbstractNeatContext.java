package org.brainfarm.java.neat.context;

import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.neat.Neat;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evolution.IEvolutionFitness;
import org.brainfarm.java.neat.api.evolution.IEvolutionInput;
import org.brainfarm.java.neat.api.evolution.IEvolutionOutput;

public abstract class AbstractNeatContext implements INeatContext {
	
	private List<INeatContextListener> listeners = new ArrayList<INeatContextListener>();
	
	protected Neat neat;

	protected IExperiment experiment;
	
	private IEvolutionFitness fitnessImpl;
	
	private IEvolutionInput inputImpl;
	
	private IEvolutionOutput outputImpl;

	@Override
	public IExperiment getExperiment() {
		return experiment;
	}

	@Override
	public IEvolutionFitness getFitnessImpl() {
		return fitnessImpl;
	}

	@Override
	public IEvolutionInput getInputImpl() {
		return inputImpl;
	}

	@Override
	public Neat getNeat() {
		return neat;
	}

	@Override
	public IEvolutionOutput getOutputImpl() {
		return outputImpl;
	}

	@Override
	public void setExperiment(IExperiment experiment) {
		this.experiment = experiment;
	}

	@Override
	public void setFitnessImpl(IEvolutionFitness fitness) {
		this.fitnessImpl = fitness;
	}

	@Override
	public void setInputImpl(IEvolutionInput input) {
		this.inputImpl = input;
	}
	
	@Override
	public void setNeat(Neat neat) {
		this.neat = neat;
	}

	@Override
	public void setOutputImpl(IEvolutionOutput output) {
		this.outputImpl = output;
	}

	public void addListener(INeatContextListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(INeatContextListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
}
