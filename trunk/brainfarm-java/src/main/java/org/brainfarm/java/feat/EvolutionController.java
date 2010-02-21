package org.brainfarm.java.feat;

import java.io.File;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.IEvolutionContext;
import org.brainfarm.java.feat.api.IEvolutionController;
import org.brainfarm.java.feat.api.IExperiment;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.brainfarm.java.feat.params.EvolutionParameters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EvolutionController implements IEvolutionController, Constants {
	
	private static Logger logger = Logger.getLogger(EvolutionController.class);
	
	protected IEvolutionContext context;
	
	private String experimentDirectory = DEFAULT_EXPERIMENT_DIR;
	
	public EvolutionController(IEvolutionContext context) {
		this.context = context;
	}
	
	public void loadDefaultParameters() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{NEAT_CONTEXT_FILENAME});
		IEvolutionParameters evolutionParameters = (EvolutionParameters)appContext.getBean(DEFAULT_EVOLUTION_PARAMETERS_BEAN_NAME);
		context.setEvolutionParameters(evolutionParameters);
	}

	@Override
	/**
	 * This is where we load the experiment the user wants to execute.
	 * This method loads the .jar file the user has selected.
	 * The .jar file is then extracted to a new 'experiments' directory.
	 * The .class files in the jar are loaded onto the classpath.
	 * The Spring context file is loaded and the 'experiment' bean is instantiated
	 * and stored in this NeatContext so it can be referenced.
	 * 
	 * @param location (String) The location of the .jar file that contains the experiment's resources.
	 */
	public void loadExperiment(String location) {		
		System.out.println("loading experiment from " + location + " " + new File(""));
		IExperiment experiment = ExperimentLoader.loadExperiment(new File(location));
		context.setExperiment(experiment);
	}
	
	/**
	 * Uses the existing experiment/ directory as the location of
	 * configuration files, results output, and custom java classes. 
	 */
	public void loadExperiment(){
		loadExperiment(experimentDirectory);
	}
	
	@Override
	public void startEvolution() {
		context.getEvolution().run();
	}

	public void setExperimentDirectory(String experimentDirectory) {
		this.experimentDirectory = experimentDirectory;
	}
}
