package org.brainfarm.java.feat.context;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.Evolution;
import org.brainfarm.java.feat.FEATConstants;
import org.brainfarm.java.feat.Neat;
import org.brainfarm.java.feat.api.IPopulation;
import org.springframework.context.ApplicationContext;

public class SpringNeatContext extends AbstractNeatContext implements FEATConstants {

	private static Logger logger = Logger.getLogger(SpringNeatContext.class);
	
	private ApplicationContext context;
	
	public SpringNeatContext() {
		
	}
	
	public SpringNeatContext(ApplicationContext context) {
		setApplicationContext(context);
	}
	
	public void setApplicationContext(ApplicationContext context) {
		this.context = context;
		
		neat = (Neat)context.getBean("neat");

		contextChanged();
	}
	
	@Override
	public Evolution getEvolution() {

		Evolution evolution = (Evolution)context.getBean("evolution");
		
		System.out.println("Evolution Bean: " + evolution);
		//EvolutionStrategy.getInstance().setActiveExperiment(experiment, this);

		
		
		System.out.println("bean cast " + evolution);
		System.out.println("population " + experiment.getPopulation());

		IPopulation population = experiment.getPopulation();
		population.init();
		
		evolution.setNeat(neat);
		evolution.setExperiment(experiment);
		evolution.setPopulation(population);
		evolution.setEvolutionStrategy(experiment.getEvolutionStrategy());
		
		
		return evolution;
	}
}
