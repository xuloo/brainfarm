package org.brainfarm.mvcs.service;

import java.util.Collection;
import java.util.List;

import org.brainfarm.java.feat.api.params.IEvolutionParameter;
import org.brainfarm.mvcs.model.vo.ExperimentEntry;

public interface IBrainFarmService {

	public abstract Collection<IEvolutionParameter> getEvolutionParameters();
	
	public abstract List<ExperimentEntry> getExperimentList();
	
	public abstract void loadExperiment(String location);
	
	public abstract void runExperiment();
	
	public abstract float getEvolutionProgress();
}
