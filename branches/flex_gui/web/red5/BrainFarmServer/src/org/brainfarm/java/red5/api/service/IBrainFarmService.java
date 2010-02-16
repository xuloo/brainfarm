package org.brainfarm.java.red5.api.service;

import java.util.List;

import org.brainfarm.java.feat.api.IEvolutionContext;
import org.brainfarm.java.feat.params.EvolutionParameter;
import org.brainfarm.java.mvcs.service.remote.ExperimentEntry;

public interface IBrainFarmService {

	public abstract List<EvolutionParameter> loadNeatParameters();
	
	public abstract List<ExperimentEntry> getAvailableExperiments();
	
	public abstract void loadExperiment(String location);
	
	public IEvolutionContext getFeatContext();
}
