package org.brainfarm.mvcs.service;

import java.util.List;

import org.brainfarm.java.feat.params.EvolutionParameter;
import org.brainfarm.mvcs.model.vo.ExperimentEntry;

public interface IBrainFarmService {

	public abstract List<EvolutionParameter> loadNeatParameters();
	
	public abstract List<ExperimentEntry> getAvailableExperiments();
	
	public abstract void loadExperiment(String location);
}
