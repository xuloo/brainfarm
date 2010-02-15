package org.brainfarm.java.feat;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.ILink;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.feat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.feat.api.operators.IFeatFactory;
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.feat.api.operators.ISpeciationStrategy;
import org.brainfarm.java.feat.context.IExperiment;
import org.brainfarm.java.feat.evaluators.ClassEvaluatorFactory;
import org.brainfarm.java.feat.operators.DefaultCrossoverStrategy;
import org.brainfarm.java.feat.operators.DefaultMutationStrategy;
import org.brainfarm.java.feat.operators.DefaultPopulationInitializationStrategy;
import org.brainfarm.java.feat.operators.DefaultReproductionStrategy;
import org.brainfarm.java.feat.operators.DefaultSpeciationStrategy;
import org.brainfarm.java.feat.operators.FeatFactory;

/**
 * Singleton providing access to customization classes
 * for customizing various pieces of the FEAT algorithm.
 * 
 * @author dtuohy
 *
 */
public class EvolutionStrategy {

	public static EvolutionStrategy _instance;

	//evaluator for IOrganisms in the current experiment
	IOrganismEvaluator organismEvaluator;

	/** Data Classes - the data manipulated by the FEAT algorithm*/
	Class<?> nodeClass;
	Class<?> networkClass;
	Class<?> linkClass;
	Class<?> genomeClass;
	Class<?> organismClass;
	Class<?> evaluatorClass;

	/** Logic Classes - encapsulate various parts of the FEAT algorithm */
	ICrossoverStrategy crossoverStrategy;
	IMutationStrategy mutationStrategy;
	IPopulationInitializationStrategy populationInitializationStrategy;
	IReproductionStrategy reproductionStrategy;
	ISpeciationStrategy speciationStrategy;
	//TODO: Eliminate this field - make FeatFactory statically invokable itself
	IFeatFactory modelObjectFactory;

	private EvolutionStrategy(){}

	public void setActiveExperiment(IExperiment experiment, INeatContext context){

		//initialize with defaults
		crossoverStrategy = new DefaultCrossoverStrategy();
		mutationStrategy = new DefaultMutationStrategy();
		populationInitializationStrategy = new DefaultPopulationInitializationStrategy();
		reproductionStrategy = new DefaultReproductionStrategy();
		speciationStrategy = new DefaultSpeciationStrategy();
		modelObjectFactory = new FeatFactory();
		nodeClass = Node.class;
		networkClass = Network.class;
		linkClass = Link.class;
		genomeClass = Genome.class;
		organismClass = Organism.class;

		String customizationsPackage = experiment.getFeatCustomizationsPackage();
		//consult the package specified by the experiment for customizations
		//TODO: Need to validate that all required classes are specified, and
		//that there is only one of each type
		try{
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
				if(implementationOf(IFeatFactory.class,c))
					modelObjectFactory = (IFeatFactory)c.newInstance();
				if(implementationOf(IGenome.class,c))
					genomeClass = c;
				if(implementationOf(INode.class,c))
					nodeClass = c;
				if(implementationOf(INetwork.class,c))
					networkClass = c;
				if(implementationOf(ILink.class,c))
					linkClass = c;
				if(implementationOf(IOrganism.class,c))
					organismClass = c;
				if(implementationOf(IOrganismEvaluator.class,c))
					evaluatorClass = c;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(evaluatorClass==null)
			throw new FeatConfigurationException("No evaluator found for experiment.  Must create implementation of " +
					" IOrganismEvaluator in " + customizationsPackage);

		organismEvaluator = ClassEvaluatorFactory.getFactory(experiment).getEvaluator(context);
	}

	private boolean implementationOf(Class<?> iface, Class<?> c) {
		Class<?>[]interfaces = c.getInterfaces();
		for(int i = 0;i<interfaces.length;i++){
			if(interfaces[i] == iface)
				return true;

			//recursively check super Interfaces
			Class<?>[] superInterfaces = interfaces[i].getInterfaces();
			for(int j = 0;j<superInterfaces.length;j++){
				if(implementationOf(iface,superInterfaces[j]))
					return true;
			}
		}

		//recursively check super Class
		Class<?> superClass = c.getSuperclass();
		if(superClass!=null && implementationOf(iface,superClass))
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

	public IFeatFactory getModelObjectFactory(){
		return modelObjectFactory;
	}

	public Class<?> getNodeClass(){
		return nodeClass;
	}

	public Class<?> getNetworkClass(){
		return networkClass;
	}

	public Class<?> getLinkClass(){
		return linkClass;
	}

	public Class<?> getGenomeClass(){
		return genomeClass;
	}

	public Class<?> getOrganismClass() {
		return organismClass;
	}

	public Class<?> getEvaluatorClass(){
		return evaluatorClass;
	}

	/**
	 * Scans all classes which belong to the given package and
	 * subpackages.  It does so with a custom class loader that
	 * looks in both (a) the local build directory (e.g. "bin" in
	 * a conventional eclipse project) and (b) the "experiment"
	 * folder, where classes may have been unpacked from the
	 * experiment JAR.
	 *
	 * @param packageName The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static Class<?>[] getClasses(String packageName){

		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		try{
			
			/** get directory for local classes **/
			String path = packageName.replace('.', '/');
			File localDir = new File("bin/classes/" + path);
			//allow for conventional eclipse bin directory
			if(!localDir.exists())
				localDir = new File("bin/" + path);
			File experimentDir = new File("experiment/");

			//create class loader
			URL[] urls = new URL[]{localDir.toURI().toURL(),experimentDir.toURI().toURL()};

			// Create a new class loader with the directory
			ClassLoader cl = new URLClassLoader(urls);

			if(localDir.exists())
				classes.addAll(findClasses(localDir, packageName, cl));
			if(!localDir.exists() && experimentDir.exists())
				classes.addAll(findClasses(experimentDir, packageName, cl));
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
	 * @throws MalformedURLException 
	 */
	private static List<Class<?>> findClasses(File directory, String packageName,ClassLoader cl) throws ClassNotFoundException, MalformedURLException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists())
			return classes;

		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) 
				classes.addAll(findClasses(file, packageName,cl));
			if (file.getName().endsWith(".class")){
				String p = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
				try{
					classes.add(cl.loadClass(p));
				}catch(Exception e){
					System.err.println(e.getClass() + ": Problem loading " + file.getName() + " from " + p);
				}
			}
		}
		return classes;
	}

	public class FeatConfigurationException extends RuntimeException{

		public FeatConfigurationException(String msg){
			super(msg);
		}
	}
}