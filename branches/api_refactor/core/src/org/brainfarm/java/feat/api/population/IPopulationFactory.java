package org.brainfarm.java.feat.api.population;

import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.context.INeatContext;

public interface IPopulationFactory {

	public abstract IPopulation getPopulation(INeatContext context);
}
