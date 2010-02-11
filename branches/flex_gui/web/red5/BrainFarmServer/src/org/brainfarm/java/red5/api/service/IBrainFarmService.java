package org.brainfarm.java.red5.api.service;

import java.util.List;

import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.params.AbstractNeatParameter;
import org.brainfarm.java.mvcs.service.remote.ExperimentEntry;

public interface IBrainFarmService {

	public abstract List<AbstractNeatParameter> loadNeatParameters();
	
	public abstract List<ExperimentEntry> getAvailableExperiments();
	
	public abstract void loadExperiment(String location);
	
	public INeatContext getFeatContext();
}
