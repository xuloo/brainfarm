package org.brainfarm.java.feat.experiment;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;

import org.brainfarm.java.feat.EvolutionStrategy;
import org.brainfarm.java.feat.FEATConstants;
import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.feat.api.experiment.IExperiment;
import org.brainfarm.java.feat.operators.FeatFactory;
import org.brainfarm.java.util.FileUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.xeustechnologies.jcl.JarClassLoader;

public class ExperimentLoader implements FEATConstants {

	private static ClassLoader classLoader;
	
	public static IExperiment loadExperiment(File location) {
		System.out.println("loading experiment " + location);
		// First of all, create a temp directory to work in.
		File workingDir = createWorkingDirectory(location);
		
		String path = workingDir.getAbsolutePath();

		// Load the experiment's context into a bean factory.
		ClassPathXmlApplicationContext factory = new ClassPathXmlApplicationContext();

		factory.setConfigLocation("experiment-context.xml");
		factory.setClassLoader(classLoader);
		factory.refresh();
		
		IExperiment experiment = createExperimentBean(factory, path);

		experiment.getEvolutionStrategy().setClassLoader(classLoader);
		
		FeatFactory.setEvolutionStrategy(experiment.getEvolutionStrategy());
		
		return experiment;
	}
	
	private static File createWorkingDirectory(File location) {
		// First of all, create a temp directory to work in.
		File tempDir = null;
		
		try { 
			tempDir = FileUtils.createTempDir();
		} catch (Exception e) {
			System.out.println("Problem creating temp dir: " + e.getMessage());
		}
		
		System.out.println("Temp dir " + tempDir.getAbsolutePath());
		
		if (!location.isDirectory() && location.getAbsolutePath().endsWith(".jar")) {
			classLoader = new JarClassLoader(new Object[]{location.getAbsolutePath()});

			//System.out.println("jarclassloader " + jarClassLoader.getParent() + " " + (jarClassLoader.getParent() == ClassLoader.getSystemClassLoader()));
			
			//FileUtils.extractZip(location, tempDir);
		} else {
			//FileUtils.copyDirectory(location, tempDir);
			classLoader = ClassLoader.getSystemClassLoader();
		}
		
		return tempDir;
	}
	
	private static IExperiment createExperimentBean(ClassPathXmlApplicationContext factory, String path) {		
				
		InputStream is = classLoader.getResourceAsStream("experiment-parameters.properties");
		
		// Check if there's a properties file for the context - if there is post process the object factory with it.
		File propertiesFile = new File(path + "/experiment-parameters.properties");
		//factory.
		if (propertiesFile.exists()) {
			PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
			cfg.setLocation(new InputStreamResource(is));
			//cfg.setLocation(new FileSystemResource(propertiesFile));
			//cfg.postProcessBeanFactory((ConfigurableListableBeanFactory) factory.getParentBeanFactory());
			factory.addBeanFactoryPostProcessor(cfg);
		}
		
		// Grab the experiment object.
		return (IExperiment)factory.getBean(EXPERIMENT_BEAN_NAME);
	}
	
	/**
	 * If the experiment defines a custom EvolutionStrategy in its context
	 * then it's loaded and set here.
	 * 
	 * @param factory
	 *//*
	private static IEvolutionStrategy checkForCustomStrategyBean(XmlBeanFactory factory) {
				
		if (factory.containsBean(EVOLUTION_STRATEGY_BEAN_NAME)) {
			System.out.println("There's a custom strategy here");
			return (IEvolutionStrategy)factory.getBean(EVOLUTION_STRATEGY_BEAN_NAME);
		}
		
		return new EvolutionStrategy();
	}*/
	
	/**
	 * If the experiment requires specialisation of any area of the EvolutionStrategy
	 * this is found and handled here.
	 * 
	 * @param experiment
	 *//*
	@SuppressWarnings("unchecked")
	private static void checkForStrategySpecialisations(XmlBeanFactory factory, IEvolutionStrategy evolutionStrategy) {
		
		// If any specialisations are defined.
		if (factory.containsBean("specialisations")) {
			
			Map<String, String> specialisations = (Map<String, String>)factory.getBean("specialisations");
				
			System.out.println("setting specialisations for experiment\n" + specialisations);
			
			// All EvolutionStrategy setters take a Class object as their argument.
			Class<?>[] parameterTypes = null;
			Class<?> parameter = null;
			
			// For each declared specialisation.
			for (String key : specialisations.keySet()) {
				
				String methodName = "set" + key.substring(0,1).toUpperCase() + key.substring(1);
				
				if (key == "organismEvaluator") {
					parameterTypes = new Class<?>[]{IOrganismEvaluator.class};
					
				} else {
					parameterTypes = new Class<?>[]{Class.class};
					
					String className = specialisations.get(key);
					
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
				}
				
				
				
				try {
					Method method = evolutionStrategy.getClass().getDeclaredMethod(methodName, parameterTypes);
					method.invoke(evolutionStrategy, new Object[]{parameter});
				} catch (NoSuchMethodException e) {
					System.out.println("Unable to set the value of the property " + key + " on EvolutionStrategy.getInstance() - are you sure there's a setter for this value?");
				} catch (Exception e) {
					System.out.println("Unable to invoke the setter method " + methodName + " -- " + e.getMessage());
				} 
			}
		}
	}*/
}
