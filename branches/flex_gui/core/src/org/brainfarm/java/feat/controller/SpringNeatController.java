package org.brainfarm.java.feat.controller;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.EvolutionStrategy;
import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.context.IExperiment;
import org.brainfarm.java.feat.context.SpringNeatContext;
import org.brainfarm.java.util.FileUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

public class SpringNeatController extends AbstractNeatController {
	
	private static Logger logger = Logger.getLogger(SpringNeatController.class);
	
	private String experimentDirectory = "experiment";
	
	public SpringNeatController(INeatContext context) {
		this.context = context;
	}
	
	public void loadDefaultParameters() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"neat-context.xml"});
		((SpringNeatContext)context).setApplicationContext(appContext);
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
		
		// Create a temporary directory for the experiment.		
		File experimentDir = new File(experimentDirectory);
		
		if (experimentDir.exists())
			FileUtils.deleteDirectory(experimentDir);
		
		boolean success = experimentDir.mkdir();
		
		if (success) {
			
			// Extract the JAR file that contains the experiment's 
			// contents into the experiments directory.
			FileUtils.extractZip(location, experimentDir);
			
			// Load the experiment classes onto the classpath so they're available to neat.
			jarClassLoader = new JarClassLoader();
			jarClassLoader.add(location);
			
			setupExperiment(experimentDir);
		  	
		} else {
			logger.error("There was an error creating the experiment directory");
		}
	}
	
	public void handleSpecialisations(IExperiment experiment) {
		Map<String, String> specialisations = experiment.getSpecialisations();
			if (specialisations != null) {
			System.out.println("setting specialisations for experiment\n" + specialisations);
			IEvolutionStrategy evolutionStrategy = EvolutionStrategy.getInstance();
			Class<?>[] parameterTypes = new Class<?>[]{Class.class};
			for (String key : specialisations.keySet()) {
				
				String methodName = "set" + key.substring(0,1).toUpperCase() + key.substring(1);
				String className = specialisations.get(key);
				
				try {
					Class<?> parameter = Class.forName(className, false, jarClassLoader);
					Method method = evolutionStrategy.getClass().getDeclaredMethod(methodName, parameterTypes);
					method.invoke(evolutionStrategy, new Object[]{parameter});
				} catch (ClassNotFoundException e) {
					System.out.println("Unable to find a class for " + className);
				} catch (NoSuchMethodException e) {
					System.out.println("Unable to set the value of the property " + key + " on EvolutionStrategy.getInstance() - are you sure there's a setter for this value?");
				} catch (Exception e) {
					System.out.println("Unable to invoke the setter method " + methodName + " on " + className + "\n" + e.getMessage());
				} 
			}
		}
	}

	private void setupExperiment(File experimentDirectory) {
		
		System.out.println("setting up experiment");
		
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
		
		IEvolutionStrategy evolutionStrategyBean = (IEvolutionStrategy)factory.getBean("evolutionStrategy");
		
		if (evolutionStrategyBean != null) {
			EvolutionStrategy.setStrategyFactory(evolutionStrategyBean);
		} else {
			EvolutionStrategy.setStrategyFactory(new EvolutionStrategy());
		}
		
		handleSpecialisations(experiment);
		// Get the 'experiment' bean - this contains the settings for the loaded experiment.
		refresh(experiment);
		
		context.setExperiment(experiment);
	}
	
	/**
	 * Uses the existing experiment/ directory as the location of
	 * configuration files, results output, and custom java classes. 
	 */
	public void loadExperiment(){
		File experimentDir = new File(experimentDirectory);
		setupExperiment(experimentDir);
	}
	
	@Override
	public void startEvolution() {
		context.getEvolution().run();
	}

	public void setExperimentDirectory(String experimentDirectory) {
		this.experimentDirectory = experimentDirectory;
	}
}
