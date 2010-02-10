package org.brainfarm.java.feat.context;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.Evolution;
import org.brainfarm.java.feat.EvolutionStrategy;
import org.brainfarm.java.feat.FeatEvolutionStrategy;
import org.brainfarm.java.feat.Neat;
import org.springframework.context.ApplicationContext;

public class SpringNeatContext extends AbstractNeatContext {

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
		System.out.println("Evolution Bean: " + context.getBean("evolution"));
		EvolutionStrategy.getInstance().setActiveExperiment(experiment, this);
		Evolution evolution = (Evolution)context.getBean("evolution");
		logger.debug("bean cast " + evolution);
		evolution.setNeat(neat);
		evolution.setExperiment(experiment);
		evolution.setPopulation(experiment.getPopulation(this));
		return evolution;
	}
}
