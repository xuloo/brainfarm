package org.brainfarm.java.neat.population;

import org.brainfarm.java.neat.Genome;
import org.brainfarm.java.neat.Population;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.population.PopulationFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

public class GenomePopulationFactory extends PopulationFactory {

	@Override
	public IPopulation getPopulation(INeatContext context) {
		XmlBeanFactory factory = new XmlBeanFactory(new FileSystemResource( "experiment/" + context.getExperiment().getGenomeFile()));
		IGenome genome = (Genome)factory.getBean("genome");
		return new Population(genome, context.getNeat().pop_size);
	}
}
