package org.brainfarm.java.feat.context;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.Neat;
import org.brainfarm.java.feat.api.context.IEvolutionContext;
import org.brainfarm.java.feat.api.context.IEvolutionContextListener;
import org.brainfarm.java.feat.api.evolution.IEvolution;
import org.brainfarm.java.feat.api.experiment.IExperiment;

public class EvolutionContext implements IEvolutionContext {
	
	private static Logger logger = Logger.getLogger(EvolutionContext.class);
	
	private List<IEvolutionContextListener> listeners = new ArrayList<IEvolutionContextListener>();
	
	protected Neat neat;

	protected IExperiment experiment;
	
	private IEvolution evolution;
	
	public EvolutionContext() {
		
	}

	@Override
	public IExperiment getExperiment() {
		return experiment;
	}

	@Override
	public Neat getNeat() {
		return neat;
	}

	@Override
	public void setExperiment(org.brainfarm.java.feat.api.experiment.IExperiment experiment) {
		this.experiment = experiment;
		
		experimentChanged();
	}
	
	@Override
	public void setNeat(Neat neat) {
		this.neat = neat;
	}

	public void addListener(IEvolutionContextListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(IEvolutionContextListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
	
	public void contextChanged() {
		
		logger.debug("informing " + listeners.size() + " listeners of context change");
		
		for (IEvolutionContextListener listener : listeners) {
			listener.contextChanged(this);
		}
	}
	
	public void experimentChanged() {
		
		logger.debug("informing " + listeners.size() + " listeners of experiment change");
		
		for (IEvolutionContextListener listener : listeners) {
			listener.experimentChanged(this);
		}
	}
	
	public IEvolution getEvolution() {
	
		if (evolution == null) {
			return evolution = experiment.evolution();
		}
		
		return evolution;
	}
}
