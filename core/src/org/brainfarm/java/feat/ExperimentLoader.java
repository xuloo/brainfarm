package org.brainfarm.java.feat;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.brainfarm.java.feat.Constants;
import org.brainfarm.java.feat.api.IExperiment;
import org.brainfarm.java.feat.operators.FeatFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.xeustechnologies.jcl.JarClassLoader;

public class ExperimentLoader implements Constants {

	private static ClassLoader classLoader;
	
	public static IExperiment loadExperiment(File location) {
		
		System.out.println("loading experiment " + location);
		
		// Create the appropriate class loader.
		createClassLoader(location);
		
		// Create an ApplicationContext.
		ClassPathXmlApplicationContext cxt = createApplicationContext();
		
		// Load the "experiment" bean from the ApplicationContext.
		IExperiment experiment = createExperimentBean(cxt, location.getAbsolutePath());

		// Ensure the IEvolutionStrategy instance has a reference to the class loader.
		experiment.getEvolutionStrategy().setClassLoader(classLoader);
		
		// Give the IEvolutionStrategy to the FeatFactory.
		FeatFactory.setEvolutionStrategy(experiment.getEvolutionStrategy());
		
		return experiment;
	}
	
	private static void createClassLoader(File location) {
		
		if (!location.isDirectory() && location.getAbsolutePath().endsWith(".jar")) {
			classLoader = new JarClassLoader(new Object[]{location.getAbsolutePath()});
		} else {
			try {
				URL[] urls = new URL[]{location.toURI().toURL()};
				classLoader = new URLClassLoader(urls);
			} catch (MalformedURLException e) {
				System.out.println ("Problem creating URLClassLoader " + e.getMessage());
			}
		}
	}
	
	private static ClassPathXmlApplicationContext createApplicationContext() {

		ClassPathXmlApplicationContext cxt = new ClassPathXmlApplicationContext();

		cxt.setConfigLocation("experiment-context.xml");
		cxt.setClassLoader(classLoader);
		cxt.refresh();
		
		return cxt;
	}
	
	private static IExperiment createExperimentBean(ClassPathXmlApplicationContext cxt, String path) {		
				
		InputStream is = classLoader.getResourceAsStream("experiment-parameters.properties");
		
		// Check if there's a properties file for the context - if there is post process the object factory with it.
		File propertiesFile = new File(path + "/experiment-parameters.properties");

		if (propertiesFile.exists()) {
			PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
			cfg.setLocation(new InputStreamResource(is));
			cxt.addBeanFactoryPostProcessor(cfg);
		}
		
		// Grab the experiment object.
		return (IExperiment)cxt.getBean(EXPERIMENT_BEAN_NAME);
	}
}
