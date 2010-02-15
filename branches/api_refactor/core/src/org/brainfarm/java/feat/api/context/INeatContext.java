package org.brainfarm.java.feat.api.context;

import org.brainfarm.java.feat.Neat;
import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.context.IExperiment;
import org.brainfarm.java.feat.context.INeatContextListener;

public interface INeatContext {
	
	IEvolution getEvolution();
	
	Neat getNeat();
	void setNeat(Neat neat);
	
	IExperiment getExperiment();
	void setExperiment(IExperiment experiment);
	
	public abstract void addListener(INeatContextListener listener);	
	public abstract void removeListener(INeatContextListener listener);
}
