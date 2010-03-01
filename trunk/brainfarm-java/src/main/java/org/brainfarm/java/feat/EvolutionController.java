package org.brainfarm.java.feat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Map.Entry;

import org.brainfarm.java.feat.api.IEvolutionContext;
import org.brainfarm.java.feat.api.IEvolutionController;
import org.brainfarm.java.feat.api.IExperiment;
import org.brainfarm.java.feat.api.params.IEvolutionParameter;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.brainfarm.java.feat.params.EvolutionParameters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class EvolutionController implements IEvolutionController, Constants {
	
	//private static Logger logger = Logger.getLogger(EvolutionController.class);
	
	protected IEvolutionContext context;
	
	private String experimentDirectory = DEFAULT_EXPERIMENT_DIR;
	
	public EvolutionController(IEvolutionContext context) {
		this.context = context;
	}
	
	public void loadEvolutionParameters(String location) {
		
		ApplicationContext appContext = loadParameterContext(location);
			
		IEvolutionParameters evolutionParameters = (EvolutionParameters)appContext.getBean(DEFAULT_EVOLUTION_PARAMETERS_BEAN_NAME);

		loadCustomParameters(location, evolutionParameters);
		
		context.setEvolutionParameters(evolutionParameters);
	}
	
	protected ApplicationContext loadParameterContext(String location) {
			
		if (location != null && location.length() > 4 && location.endsWith(".xml")) {
			return new FileSystemXmlApplicationContext(new String[]{location});
		} else {
			return new ClassPathXmlApplicationContext(new String[]{NEAT_CONTEXT_FILENAME});
		}
	}
	
	protected void loadCustomParameters(String location, IEvolutionParameters evolutionParameters) {
		System.out.println("loading custom parameters from  " + location);
		if (location != null && location.length() > 4 && location.endsWith(".properties")) {
			Properties customProperties = new Properties();
			
			try {
				InputStream is = new FileInputStream(new File(location));
				customProperties.load(is);
				
				for (Entry<Object, Object> entry : customProperties.entrySet()) {
					String key = (String)entry.getKey();
					String value = (String)entry.getValue();
		
					IEvolutionParameter parameter = evolutionParameters.getParameter(key);
					
					if (parameter == null) {
						System.out.println("There's a problem customising the evolution parameters - it seems there's no parameter with the name " + key + " are you sure there isn't just a spelling error?");
					} else {
						parameter.setValue(value);
					}
				}
			} catch (IOException e) {
				System.out.println("Problem loading custom properties file: " + e.getMessage());
			}
		}
	}
	
	/*@Override
	public void loadCustomParameters(String contextfile) {
		ApplicationContext appContext = new FileSystemXmlApplicationContext(new String[]{contextfile});
		IEvolutionParameters evolutionParameters = (EvolutionParameters)appContext.getBean(DEFAULT_EVOLUTION_PARAMETERS_BEAN_NAME);
		context.setEvolutionParameters(evolutionParameters);
	}*/
	/*
	public void loadDefaultParameters() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{NEAT_CONTEXT_FILENAME});
		IEvolutionParameters evolutionParameters = (EvolutionParameters)appContext.getBean(DEFAULT_EVOLUTION_PARAMETERS_BEAN_NAME);
		context.setEvolutionParameters(evolutionParameters);
	}*/

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
		
		if (context.getEvolutionParameters() == null) {
			loadEvolutionParameters(null);
		}
		
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