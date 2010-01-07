package org.brainfarm.java.neat.api;

import org.brainfarm.java.neat.api.context.INeatContext;

public interface IPopulationFactory {

	public abstract IPopulation getPopulation(INeatContext context);
}
