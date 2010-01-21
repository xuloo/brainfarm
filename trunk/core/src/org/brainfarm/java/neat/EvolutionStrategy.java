package org.brainfarm.java.neat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.brainfarm.java.neat.ann.NeatMutationStrategy;
import org.brainfarm.java.neat.ann.NeatOffspringFactory;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.neat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.neat.api.operators.IMutationStrategy;
import org.brainfarm.java.neat.api.operators.IOffspringFactory;
import org.brainfarm.java.neat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.neat.api.operators.IReproductionStrategy;
import org.brainfarm.java.neat.api.operators.ISpeciationStrategy;
import org.brainfarm.java.neat.context.IExperiment;
import org.brainfarm.java.neat.operators.DefaultCrossoverStrategy;
import org.brainfarm.java.neat.operators.DefaultPopulationInitializationStrategy;
import org.brainfarm.java.neat.operators.DefaultReproductionStrategy;
import org.brainfarm.java.neat.operators.DefaultSpeciationStrategy;

/**
 * Singleton providing access to methods for executing various
 * pieces of the NEAT algorithm.
 * 
 * @author dtuohy
 *
 */
public class EvolutionStrategy {

	public static EvolutionStrategy _instance;

	//evaluator for IOrganisms in the current experiment
	IOrganismEvaluator organismEvaluator;

	//strategies for various parts of the NEAT algorithm
	ICrossoverStrategy crossoverStrategy;
	IMutationStrategy mutationStrategy;
	IPopulationInitializationStrategy populationInitializationStrategy;
	IReproductionStrategy reproductionStrategy;
	ISpeciationStrategy speciationStrategy;
	IOffspringFactory offspringFactory;

	private EvolutionStrategy(){}

	public void setActiveExperiment(IExperiment experiment, INeatContext context){
		
		organismEvaluator = experiment.getEvaluator(context);
		
		//initialize with default strategies
		crossoverStrategy = new DefaultCrossoverStrategy();
		mutationStrategy = new NeatMutationStrategy();
		populationInitializationStrategy = new DefaultPopulationInitializationStrategy();
		reproductionStrategy = new DefaultReproductionStrategy();
		speciationStrategy = new DefaultSpeciationStrategy();
		offspringFactory = new NeatOffspringFactory();

		//consult the package specified by the experiment for customizations
		//TODO: Need to validate that all required classes are specified
		try{
			String customizationsPackage = experiment.getFeatCustomizationsPackage();
			Class<?>[] classes = getClasses(customizationsPackage);
			for(Class<?> c : classes){
				if(implementationOf(ICrossoverStrategy.class,c))
					crossoverStrategy = (ICrossoverStrategy)c.newInstance();
				if(implementationOf(IMutationStrategy.class,c))
					mutationStrategy = (IMutationStrategy)c.newInstance();
				if(implementationOf(IPopulationInitializationStrategy.class,c))
					populationInitializationStrategy = (IPopulationInitializationStrategy)c.newInstance();
				if(implementationOf(IReproductionStrategy.class,c))
					reproductionStrategy = (IReproductionStrategy)c.newInstance();
				if(implementationOf(ISpeciationStrategy.class,c))
					speciationStrategy = (ISpeciationStrategy)c.newInstance();
				if(implementationOf(IOffspringFactory.class,c))
					offspringFactory = (IOffspringFactory)c.newInstance();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private boolean implementationOf(Class<?> iface, Class<?> c) {
		Class<?>[]interfaces = c.getInterfaces();
		for(int i = 0;i<interfaces.length;i++)
			if(interfaces[i] == iface)
				return true;
		return false;
	}

	public static EvolutionStrategy getInstance(){
		if(_instance == null)
			_instance = new EvolutionStrategy();
		return _instance;
	}

	public IOrganismEvaluator getOrganismEvaluator(){
		return organismEvaluator;
	}

	public ICrossoverStrategy getCrossoverStrategy() {
		return crossoverStrategy;
	}

	public IMutationStrategy getMutationStrategy(){
		return mutationStrategy;
	}

	public IReproductionStrategy getReproductionStrategy(){
		return reproductionStrategy;
	}

	public ISpeciationStrategy getSpeciationStrategy(){
		return speciationStrategy;
	}

	public IPopulationInitializationStrategy getPopulationInitializationStrategy(){
		return populationInitializationStrategy;
	}

	public IOffspringFactory getOffspringFactory(){
		return offspringFactory;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
	 *
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static Class<?>[] getClasses(String packageName){
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		try{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			assert classLoader != null;
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList<File>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(new File(resource.getFile()));
			}
			for (File directory : dirs) {
				classes.addAll(findClasses(directory, packageName));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}
}