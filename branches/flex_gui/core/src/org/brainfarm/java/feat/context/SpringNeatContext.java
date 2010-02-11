package org.brainfarm.java.feat.context;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.Evolution;
import org.brainfarm.java.feat.EvolutionStrategy;
import org.brainfarm.java.feat.FEATConstants;
import org.brainfarm.java.feat.Neat;
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
		
		// Register the current experiment with the EvolutionStrategy.
		EvolutionStrategy.getInstance().setActiveExperiment(experiment, this);
		
		Evolution evolution = null;
		
		// Check to see if there's an 'evolution' bean defined in the context.
		if (context.containsBean(EVOLUTION_BEAN_NAME)) {
			evolution = (Evolution)context.getBean(EVOLUTION_BEAN_NAME);
		}
		
		// If not create a fresh instance.
		if (evolution == null) {
			evolution = new Evolution();
		}

		// Either way, configure it.
		evolution.setNeat(neat);
		evolution.setExperiment(experiment);
		evolution.setPopulation(experiment.getPopulation(this));
		
		return evolution;
	}
}
