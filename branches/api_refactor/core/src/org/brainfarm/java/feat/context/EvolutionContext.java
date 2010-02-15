package org.brainfarm.java.feat.context;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.Neat;
import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.springframework.context.ApplicationContext;

public class EvolutionContext implements INeatContext {
	
	private static Logger logger = Logger.getLogger(EvolutionContext.class);
	
	private List<INeatContextListener> listeners = new ArrayList<INeatContextListener>();
	
	protected Neat neat;

	protected IExperiment experiment;
	
	private IEvolution evolution;

	@Override
	public IExperiment getExperiment() {
		return experiment;
	}

	@Override
	public Neat getNeat() {
		return neat;
	}

	@Override
	public void setExperiment(IExperiment experiment) {
		this.experiment = experiment;
		
		experimentChanged();
	}
	
	@Override
	public void setNeat(Neat neat) {
		this.neat = neat;
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
	
	@Override
	public IEvolution getEvolution() {
		if (evolution == null) {
			System.out.println("Creating new Evolution instance");
			evolution = experiment.evolution();			
		}
		
		return evolution;
	}
}
