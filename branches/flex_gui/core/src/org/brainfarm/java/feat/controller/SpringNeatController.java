package org.brainfarm.java.feat.controller;

import java.io.File;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.FEATConstants;
import org.brainfarm.java.feat.Neat;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.experiment.IExperiment;
import org.brainfarm.java.feat.context.EvolutionContext;
import org.brainfarm.java.feat.experiment.ExperimentLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringNeatController extends AbstractNeatController implements FEATConstants {
	
	private static Logger logger = Logger.getLogger(SpringNeatController.class);
	
	private String experimentDirectory = DEFAULT_EXPERIMENT_DIR;
	
	public SpringNeatController(INeatContext context) {
		this.context = context;
	}
	
	public void loadDefaultParameters() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{NEAT_CONTEXT_FILENAME});
		Neat neat = (Neat)appContext.getBean("neat");
		context.setNeat(neat);
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
		
		IExperiment experiment = ExperimentLoader.loadExperiment(new File(location));
		context.setExperiment(experiment);
		/*
		// Create a temporary directory for the experiment.		
		File experimentDir = new File(experimentDirectory);
		
		if (experimentDir.exists())
			FileUtils.deleteDirectory(experimentDir);
		
		boolean success = experimentDir.mkdir();
		System.out.println("success " + success);
		
		
		
		if (success) {
			
			// Extract the JAR file that contains the experiment's 
			// contents into the experiments directory.
			FileUtils.extractZip(location, experimentDir);
			
			// Load the experiment classes onto the classpath so they're available to neat.
			jarClassLoader = new JarClassLoader();
			jarClassLoader.add(location);
			System.out.println("jar class loader " + jarClassLoader);
			setupExperiment(experimentDir);
		  	
		} else {
			logger.error("There was an error creating the experiment directory");
		}*/
	}
/*
	private void setupExperiment(File experimentDirectory) {
		
		System.out.println("setting up experiment");
		
		factory = JclObjectFactory.getInstance();
  
		// Grab the experiments directory's path.
		String path = experimentDirectory.getAbsolutePath();
		experimentDirectory = null;
  
		// Load the experiment's context into a bean factory.
		XmlBeanFactory factory = new XmlBeanFactory(new FileSystemResource( path + "/" + EXPERIMENT_CONTEXT_FILENAME));
		PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
		cfg.setLocation(new FileSystemResource(path + "/" + EXPERIMENT_PROPERTIES_FILENAME));
		cfg.postProcessBeanFactory(factory);
		
		// Grab the experiment object.
		IExperiment experiment = (IExperiment)factory.getBean(EXPERIMENT_BEAN_NAME);
		
		// Set up the application to handle the new experiment.
		checkForCustomStrategyBean(factory);
		handleStrategySpecialisations(experiment);
		refresh(experiment);
		
		// Setting the new experiment on the context will 
		// update any listeners that a new experiment is ready.
		context.setExperiment(experiment);
	}
	*/
	/**
	 * If the experiment defines a custom EvolutionStrategy in its context
	 * then it's loaded and set here.
	 * 
	 * @param factory
	 *//*
	private void checkForCustomStrategyBean(XmlBeanFactory factory) {
		if (factory.containsBean(EVOLUTION_STRATEGY_BEAN_NAME)) {
			IEvolutionStrategy evolutionStrategyBean = (IEvolutionStrategy)factory.getBean(EVOLUTION_STRATEGY_BEAN_NAME);
			EvolutionStrategy.setStrategyFactory(evolutionStrategyBean);
		} else {
			EvolutionStrategy.setStrategyFactory(new EvolutionStrategy());
		}
	}*/
	
	/**
<<<<<<< .mine
	 * If the experiment requires specialisation of any area of the EvolutionStrategy
	 * this is found and handled here.
	 * 
	 * @param experiment
	 *//*
	private void handleStrategySpecialisations(IExperiment experiment) {
		
		Map<String, String> specialisations = experiment.getSpecialisations();
		System.out.println("setting specialisations for experiment\n" + specialisations);
		// If any specialisations are defined.
		if (specialisations != null) {
				
			System.out.println("setting specialisations for experiment\n" + specialisations);
			
			// Get a reference to the EvolutionStrategy.
			IEvolutionStrategy evolutionStrategy = EvolutionStrategy.getInstance();
			
			// All EvolutionStrategy setters take a Class object as their argument.
			Class<?>[] parameterTypes = new Class<?>[]{Class.class};
			
			// For each declared specialisation.
			for (String key : specialisations.keySet()) {
				
				String methodName = "set" + key.substring(0,1).toUpperCase() + key.substring(1);
				String className = specialisations.get(key);
				
				Class<?> parameter = null;
				
				try {
					// Try and find the class in the main class loader.
					parameter = Class.forName(className);
					
				} catch (ClassNotFoundException e) {
					System.out.println("Unable to find a class for " + className + " in experiment jar");
					
					try {
						// If it's not there then try and find it in the loaded experiment jar.
						parameter = Class.forName(className, false, jarClassLoader);
					} catch (ClassNotFoundException ee) {
						System.out.println("Unable to find a class for " + className);
					}					
				} 
				
				try {
					Method method = evolutionStrategy.getClass().getDeclaredMethod(methodName, parameterTypes);
					method.invoke(evolutionStrategy, new Object[]{parameter});
				} catch (NoSuchMethodException e) {
					System.out.println("Unable to set the value of the property " + key + " on EvolutionStrategy.getInstance() - are you sure there's a setter for this value?");
				} catch (Exception e) {
					System.out.println("Unable to invoke the setter method " + methodName + " on " + className + "\n" + e.getMessage());
				} 
			}
		}
	}*/
	
	/**
	 * If the experiment defines a custom EvolutionStrategy in its context
	 * then it's loaded and set here.
	 * 
	 * @param factory
	 *//*
	private void checkForCustomStrategyBean(XmlBeanFactory factory) {
		if (factory.containsBean(EVOLUTION_STRATEGY_BEAN_NAME)) {
			IEvolutionStrategy evolutionStrategyBean = (IEvolutionStrategy)factory.getBean(EVOLUTION_STRATEGY_BEAN_NAME);
			EvolutionStrategy.setStrategyFactory(evolutionStrategyBean);
		} else {
			EvolutionStrategy.setStrategyFactory(new EvolutionStrategy());
		}
	}*/
	
	/**
	 * If the experiment requires specialisation of any area of the EvolutionStrategy
	 * this is found and handled here.
	 * 
	 * @param experiment
	 *//*
	private void handleStrategySpecialisations(IExperiment experiment) {
		
		Map<String, String> specialisations = experiment.getSpecialisations();
		
		// If any specialisations are defined.
		if (specialisations != null) {
				
			System.out.println("setting specialisations for experiment\n" + specialisations);
			
			// Get a reference to the EvolutionStrategy.
			IEvolutionStrategy evolutionStrategy = EvolutionStrategy.getInstance();
			
			// All EvolutionStrategy setters take a Class object as their argument.
			Class<?>[] parameterTypes = new Class<?>[]{Class.class};
			
			// For each declared specialisation.
			for (String key : specialisations.keySet()) {
				
				String methodName = "set" + key.substring(0,1).toUpperCase() + key.substring(1);
				String className = specialisations.get(key);
				
				Class<?> parameter = null;
				
				try {
					// Try and find the class in the main class loader.
					parameter = Class.forName(className);
					
				} catch (ClassNotFoundException e) {
					System.out.println("Unable to find a class for " + className + " in experiment jar");
					
					try {
						// If it's not there then try and find it in the loaded experiment jar.
						parameter = Class.forName(className, false, jarClassLoader);
					} catch (ClassNotFoundException ee) {
						System.out.println("Unable to find a class for " + className);
					}					
				} 
				
				try {
					Method method = evolutionStrategy.getClass().getDeclaredMethod(methodName, parameterTypes);
					method.invoke(evolutionStrategy, new Object[]{parameter});
				} catch (NoSuchMethodException e) {
					System.out.println("Unable to set the value of the property " + key + " on EvolutionStrategy.getInstance() - are you sure there's a setter for this value?");
				} catch (Exception e) {
					System.out.println("Unable to invoke the setter method " + methodName + " on " + className + "\n" + e.getMessage());
				} 
			}
		}
	}*/
	
	/**
	 * Uses the existing experiment/ directory as the location of
	 * configuration files, results output, and custom java classes. 
	 */
	public void loadExperiment(){
		//File experimentDir = new File(experimentDirectory);
		//setupExperiment(experimentDir);
		
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
