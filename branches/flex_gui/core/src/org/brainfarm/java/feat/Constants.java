package org.brainfarm.java.feat;

/**
 * Holds the literal strings for the application.
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public interface Constants {
	
	// Default Directories.
	public final static String DEFAULT_EXPERIMENT_DIR 				= "experiment";
	
	// Default File Names.
	public final static String EXPERIMENT_CONTEXT_FILENAME 			= "experiment-context.xml";
	public final static String EXPERIMENT_PROPERTIES_FILENAME 		= "experiment-parameters.properties";
	public final static String NEAT_CONTEXT_FILENAME 				= "neat-context.xml";

	// Default Bean Names.
	public final static String EVOLUTION_BEAN_NAME 					= "evolution";
	public final static String EVOLUTION_STRATEGY_BEAN_NAME 		= "evolutionStrategy";
	public final static String EXPERIMENT_BEAN_NAME 				= "experiment";
}
