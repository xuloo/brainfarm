package org.brainfarm.java.neat.controller;

import org.apache.log4j.Logger;
import org.brainfarm.java.neat.Genome;
import org.brainfarm.java.neat.Neat;
import org.brainfarm.java.neat.Population;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.INeatController;
import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evolution.IEvolutionFitness;
import org.brainfarm.java.neat.api.evolution.IEvolutionInput;
import org.brainfarm.java.neat.api.evolution.IEvolutionOutput;
import org.brainfarm.java.neat.api.types.DataSource;
import org.brainfarm.java.neat.api.types.StartFrom;
import org.brainfarm.java.neat.context.IExperiment;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.JclUtils;

public abstract class AbstractNeatController implements INeatController {
	
	private static Logger logger = Logger.getLogger(AbstractNeatController.class);

	protected INeatContext context;
	
	protected JclObjectFactory factory;
	
	protected JarClassLoader jarClassLoader;
	
	protected void refresh(IExperiment experiment) {
		// Unload the experiment-specific class implmentations.
		// So they'll be recreated next time they're called.
		context.setFitnessImpl(JclUtils.cast(factory.create(jarClassLoader, experiment.getFitnessClass()), IEvolutionFitness.class));
		context.setInputImpl(JclUtils.cast(factory.create(jarClassLoader, experiment.getDataInput()), IEvolutionInput.class));
		context.setOutputImpl(JclUtils.cast(factory.create(jarClassLoader, experiment.getDataOutput()), IEvolutionOutput.class));
		
		Neat neat = context.getNeat();
		
		// Initialise the experiment-specific NEAT parameters.
		neat.setMaxFitness(context.getFitnessImpl().getMaxFitness());
		
		if (experiment.getDataSource() == DataSource.CLASS) {
			neat.setNumberOfInputs(context.getInputImpl().getNumUnit());
			neat.setNumberOfSamples(context.getInputImpl().getNumSamples());
			neat.setNumberOfOutputUnits(context.getOutputImpl().getNumUnit());
		} else if (experiment.getDataSource() == DataSource.FILE) {
			
		}
	}
}
