package org.brainfarm.java.feat.controller;

import java.io.File;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.context.IExperiment;
import org.brainfarm.java.util.FileUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

public class SpringNeatController extends AbstractNeatController {
	
	private static Logger logger = Logger.getLogger(SpringNeatController.class);
	
	private final String experiment_dir = "experiment";

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
		
		// Create a temporary directory for the experiment.		
		File experimentDirectory = new File(experiment_dir);
		
		if (experimentDirectory.exists())
			FileUtils.deleteDirectory(experimentDirectory);
		
		boolean success = experimentDirectory.mkdir();
		
		if (success) {
			
			// Extract the JAR file that contains the experiment's 
			// contents into the experiments directory.
			FileUtils.extractZip(location, experimentDirectory);
			
			// Load the experiment classes onto the classpath so they're available to neat.
			jarClassLoader = new JarClassLoader();
			jarClassLoader.add(location);
			
			setupExperiment(experimentDirectory);
		  	
		} else {
			logger.error("There was an error creating the experiment directory");
		}
	}

	private void setupExperiment(File experimentDirectory) {
		factory = JclObjectFactory.getInstance();
  
		// Grab the experiments directory's path.
		String path = experimentDirectory.getAbsolutePath();
		experimentDirectory = null;
  
		// Load the experiment's context into a bean factory.
		XmlBeanFactory factory = new XmlBeanFactory(new FileSystemResource( path + "/experiment-context.xml"));
		PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
		cfg.setLocation(new FileSystemResource(path + "/experiment-parameters.properties"));
		cfg.postProcessBeanFactory(factory);
		
		IExperiment experiment = (IExperiment)factory.getBean("experiment");
		
		// Get the 'experiment' bean - this contains the settings for the loaded experiment.
		refresh(experiment);
		
		context.setExperiment(experiment);
	}
	
	/**
	 * Uses the existing experiment/ directory as the location of
	 * configuration files, results output, and custom java classes. 
	 */
	public void loadExperiment(){
		File experimentDirectory = new File(experiment_dir);
		setupExperiment(experimentDirectory);
	}
	
	@Override
	public void startEvolution() {
		context.getEvolution().run();
	}

}
