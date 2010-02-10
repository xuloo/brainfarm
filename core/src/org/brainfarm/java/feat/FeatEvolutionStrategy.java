package org.brainfarm.java.feat;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
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
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.feat.api.operators.ISpeciationStrategy;
import org.brainfarm.java.feat.context.IExperiment;

/**
 * Singleton providing access to customization classes for customizing various
 * pieces of the FEAT algorithm.
 * 
 * @author dtuohy
 * 
 */
public class FeatEvolutionStrategy extends EvolutionStrategy {

	public static FeatEvolutionStrategy _instance;

	public FeatEvolutionStrategy() {
	}

	public void setActiveExperiment(IExperiment experiment, INeatContext context) {

		super.setActiveExperiment(experiment, context);

		String customizationsPackage = experiment
				.getFeatCustomizationsPackage();
		// consult the package specified by the experiment for customizations
		// TODO: Need to validate that all required classes are specified, and
		// that there is only one of each type
		try {
			Class<?>[] classes = getClasses(customizationsPackage);
			for (Class<?> c : classes) {
				if (implementationOf(ICrossoverStrategy.class, c))
					crossoverStrategy = (ICrossoverStrategy) c.newInstance();
				if (implementationOf(IMutationStrategy.class, c))
					mutationStrategy = (IMutationStrategy) c.newInstance();
				if (implementationOf(IPopulationInitializationStrategy.class, c))
					populationInitializationStrategy = (IPopulationInitializationStrategy) c.newInstance();
				if (implementationOf(IReproductionStrategy.class, c))
					reproductionStrategy = (IReproductionStrategy) c.newInstance();
				if (implementationOf(ISpeciationStrategy.class, c))
					speciationStrategy = (ISpeciationStrategy) c.newInstance();
				if (implementationOf(IGenome.class, c))
					genomeClass = c;
				if (implementationOf(INode.class, c))
					nodeClass = c;
				if (implementationOf(INetwork.class, c))
					networkClass = c;
				if (implementationOf(ILink.class, c))
					linkClass = c;
				if (implementationOf(IOrganism.class, c))
					organismClass = c;
				if (implementationOf(IOrganismEvaluator.class, c))
					evaluatorClass = c;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (evaluatorClass == null)
			throw new FeatConfigurationException(
					"No evaluator found for experiment.  Must create implementation of "
							+ " IOrganismEvaluator in " + customizationsPackage);

		try {
			Constructor<?> c = evaluatorClass.getConstructor(INeatContext.class);
			organismEvaluator = (IOrganismEvaluator) c.newInstance(context);
		} catch (Exception e) {
			throw new FeatConfigurationException(
					"Unable to instantiate the Organism Evaluator using "
							+ evaluatorClass.getName() + " and context "
							+ context.toString() + "\noriginal error\n"
							+ e.getMessage());
		}
	}

	private boolean implementationOf(Class<?> iface, Class<?> c) {
		Class<?>[] interfaces = c.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i] == iface)
				return true;

			// recursively check super Interfaces
			Class<?>[] superInterfaces = interfaces[i].getInterfaces();
			for (int j = 0; j < superInterfaces.length; j++) {
				if (implementationOf(iface, superInterfaces[j]))
					return true;
			}
		}

		// recursively check super Class
		Class<?> superClass = c.getSuperclass();
		if (superClass != null && implementationOf(iface, superClass))
			return true;

		return false;
	}

	public IOrganismEvaluator getOrganismEvaluator() {
		return organismEvaluator;
	}

	public ICrossoverStrategy getCrossoverStrategy() {
		return crossoverStrategy;
	}

	public IMutationStrategy getMutationStrategy() {
		return mutationStrategy;
	}

	public IReproductionStrategy getReproductionStrategy() {
		return reproductionStrategy;
	}

	public ISpeciationStrategy getSpeciationStrategy() {
		return speciationStrategy;
	}

	public IPopulationInitializationStrategy getPopulationInitializationStrategy() {
		return populationInitializationStrategy;
	}

	public Class<?> getNodeClass() {
		return nodeClass;
	}

	public Class<?> getNetworkClass() {
		return networkClass;
	}

	public Class<?> getLinkClass() {
		return linkClass;
	}

	public Class<?> getGenomeClass() {
		return genomeClass;
	}

	public Class<?> getOrganismClass() {
		return organismClass;
	}

	public Class<?> getEvaluatorClass() {
		return evaluatorClass;
	}

	private static void ls(File f) {
		File list[] = f.listFiles();
		for (File file : list) {
			if (file.isDirectory()) {
				ls(file);
			} else {
				System.out.println(file);
			}
		}
	}

	/**
	 * Scans all classes which belong to the given package and subpackages. It
	 * does so with a custom class loader that looks in both (a) the local build
	 * directory (e.g. "bin" in a conventional eclipse project) and (b) the
	 * "experiment" folder, where classes may have been unpacked from the
	 * experiment JAR.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static Class<?>[] getClasses(String packageName) {

		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		try {

			/** get directory for local classes **/
			String path = packageName.replace('.', '/');
			File localDir = new File("bin/classes/" + path);
			System.out.println("Local Dir: " + localDir.getAbsolutePath());

			File currentDir = new File("");
			System.out.println("this dir is " + currentDir.getAbsolutePath());

			final String pathSep = System.getProperty("path.separator");
			final String list = System.getProperty("java.class.path");
			for (final String p : list.split(pathSep)) {
				final File object = new java.io.File(p);
				if (object.isDirectory()) {
					ls(object);
				} else {
					System.out.println(object);
				}
			}

			// allow for conventional eclipse bin directory
			if (!localDir.exists())
				localDir = new File("bin/" + path);
			File experimentDir = new File("experiment/");

			// create class loader
			URL[] urls = new URL[] { localDir.toURI().toURL(),
					experimentDir.toURI().toURL() };

			// Create a new class loader with the directory
			ClassLoader cl = new URLClassLoader(urls);

			if (localDir.exists())
				classes.addAll(findClasses(localDir, packageName, cl));
			if (!localDir.exists() && experimentDir.exists())
				classes.addAll(findClasses(experimentDir, packageName, cl));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 */
	private static List<Class<?>> findClasses(File directory,
			String packageName, ClassLoader cl) throws ClassNotFoundException,
			MalformedURLException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists())
			return classes;

		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory())
				classes.addAll(findClasses(file, packageName, cl));
			if (file.getName().endsWith(".class")) {
				String p = packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - 6);
				try {
					classes.add(cl.loadClass(p));
				} catch (Exception e) {
					System.err.println(e.getClass() + ": Problem loading "
							+ file.getName() + " from " + p);
				}
			}
		}
		return classes;
	}

	public class FeatConfigurationException extends RuntimeException {

		public FeatConfigurationException(String msg) {
			super(msg);
		}
	}
}