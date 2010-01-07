package org.brainfarm.java.neat.api.population;

import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.context.INeatContext;

public interface IPopulationFactory {

	public abstract IPopulation getPopulation(INeatContext context);
}
