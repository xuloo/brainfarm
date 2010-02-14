package org.brainfarm.java.feat.context;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.FEATConstants;
import org.brainfarm.java.feat.Neat;
import org.brainfarm.java.feat.api.evolution.IEvolution;
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
	public IEvolution getEvolution() {
		
		return experiment.evolution();
	}
}
