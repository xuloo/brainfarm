package org.brainfarm.java.feat;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.IEvolutionContext;
import org.brainfarm.java.feat.api.IEvolutionContextListener;
import org.brainfarm.java.feat.api.IExperiment;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;

public class EvolutionContext implements IEvolutionContext {
	
	private static Logger logger = Logger.getLogger(EvolutionContext.class);
	
	private List<IEvolutionContextListener> listeners = new ArrayList<IEvolutionContextListener>();
	
	protected IEvolutionParameters evolutionParameters;

	protected IExperiment experiment;
	
	private IEvolution evolution;
	
	public EvolutionContext() {
		
	}

	@Override
	public IExperiment getExperiment() {
		return experiment;
	}

	/*@Override
	public EvolutionParameters getNeat() {
		return neat;
	}*/

	@Override
	public void setExperiment(IExperiment experiment) {
		this.experiment = experiment;
		
		experiment.getEvolutionStrategy().setEvolutionParameters(evolutionParameters);
		
		experimentChanged();
	}
	
	/*@Override
	public void setNeat(EvolutionParameters neat) {
		this.neat = neat;
	}*/

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

	@Override
	public IEvolutionParameters getEvolutionParameters() {
		return evolutionParameters;
	}

	@Override
	public void setEvolutionParameters(IEvolutionParameters evolutionParameters) {
		this.evolutionParameters = evolutionParameters;
	}
}
