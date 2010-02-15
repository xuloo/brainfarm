package org.brainfarm.java.feat.context;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.Neat;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.evolution.IEvolutionFitness;
import org.brainfarm.java.feat.api.evolution.IEvolutionInput;
import org.brainfarm.java.feat.api.evolution.IEvolutionOutput;

public abstract class AbstractNeatContext implements INeatContext {
	
	private static Logger logger = Logger.getLogger(AbstractNeatContext.class);
	
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
		
		experimentChanged();
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
	
	public void contextChanged() {
		
		logger.debug("informing " + listeners.size() + " listeners of context change");
		
		for (INeatContextListener listener : listeners) {
			listener.contextChanged(this);
		}
	}
	
	public void experimentChanged() {
		
		logger.debug("informing " + listeners.size() + " listeners of experiment change");
		
		for (INeatContextListener listener : listeners) {
			listener.experimentChanged(this);
		}
	}
}
