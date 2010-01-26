package org.brainfarm.java.feat.population;

import org.brainfarm.java.feat.Genome;
import org.brainfarm.java.feat.Population;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.population.PopulationFactory;
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
