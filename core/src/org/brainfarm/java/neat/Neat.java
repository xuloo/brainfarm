package org.brainfarm.java.neat;


import java.util.List;

import org.brainfarm.java.neat.params.AbstractNeatParameter;
import org.brainfarm.java.neat.params.NeatParameter;

/**
 * Definition of all parameters , threshold and other values.
 */
public class Neat {

	/** 
	 * The power of a linkweight mutation 
	 */
	public static final String WEIGHT_MUT_POWER = "weight_mut_power";
	
	@NeatParameter
	public static double weight_mut_power;
	
	/**
	 * Probability that a link mutation which doesn't have to be recurrent will be made recurrent
	 */
	public static final String RECURE_PROB = "recur_prob";
	
	@NeatParameter
	public static double recur_prob;
	
	/** 
	 * factor multiply for gene not equal 
	 */
	public static final String DISJOINT_COEFF = "disjoint_coeff";
	
	@NeatParameter
	public static double disjoint_coeff;
	
	/** 
	 * factor multiply for gene excedeed 
	 */
	public static final String EXCESS_COEFF = "excess_coeff";
	
	@NeatParameter
	public static double excess_coeff;
	
	/** 
	 * factor multiply weight difference 
	 */
	public static final String MUTDIFF_COEFF = "mutdiff_coeff";
	
	@NeatParameter
	public static double mutdiff_coeff;
	
	/** 
	 * threshold under which two Genomes are the same species 
	 */
	public static final String COMPAT_THRESHOLD = "compat_threshold";
	
	@NeatParameter
	public static double compat_threshold;
	
	/** 
	 * How much does age matter in epoch cycle 
	 */
	public static final String AGE_SIGNIFICANCE = "age_significance";
	
	@NeatParameter
	public static double age_significance;
	
	/** 
	 * Percent of ave fitness for survival 
	 */
	public static final String SURVIVAL_THRESH = "survival_thresh";
	
	@NeatParameter
	public static double survival_thresh;
	
	/** 
	 * Probability of a non-mating reproduction 
	 */
	public static final String MUTATE_ONLY_PROB = "mutate_only_prob";
	
	@NeatParameter
	public static double mutate_only_prob;
	
	/** 
	 * Probability of mutate link weight 
	 */
	public static final String MUTATE_LINK_WEIGHTS_PROB = "mutate_link_weights_prob";
	
	@NeatParameter
	public static double mutate_link_weights_prob;
	
	/** 
	 * Probability of mutate status ena->dis | dis-ena of gene 
	 */
	public static final String MUTATE_TOGGLE_ENABLE_PROB = "mutate_toggle_enable_prob";
	
	@NeatParameter
	public static double mutate_toggle_enable_prob;
	
	/** 
	 * Probability of switch status to ena of gene 
	 */
	public static final String MUTATE_GENE_REENABLE_PROB = "mutate_gene_reenable_prob";
	
	@NeatParameter
	public static double mutate_gene_reenable_prob;
	
	/** 
	 * Probability of add a node to struct of genome 
	 */
	public static final String MUTATE_ADD_NODE_PROB = "mutate_add_node_prob";
	
	@NeatParameter
	public static double mutate_add_node_prob;
	
	/** 
	 * Probability of add a link to struct of genome 
	 */
	public static final String MUTATE_ADD_LINK_PROB = "mutate_add_link_prob";
	
	@NeatParameter
	public static double mutate_add_link_prob;
	
	/** 
	 * Probability of a mate being outside species 
	 */
	public static final String INTERSPECIES_MATE_RATE = "interspecies_mate_rate";
	
	@NeatParameter
	public static double interspecies_mate_rate;
	
	/** 
	 * Probability of cross in a many point of two genome 
	 */
	public static final String MATE_MULTIPOINT_PROB = "mate_multipoint_prob";
	
	@NeatParameter
	public static double mate_multipoint_prob;
	
	/** 
	 * Probability of cross in a many point of two genome with media 
	 */
	public static final String MATE_MULTIPOINT_AVG_PROB = "mate_multipoint_avg_prob";
	
	@NeatParameter
	public static double mate_multipoint_avg_prob;
	
	/** 
	 * Probability of cross in a single point of two genome 
	 */
	public static final String MATE_SINGLEPOINT_PROB = "mate_singlepoint_prob";
	
	@NeatParameter
	public static double mate_singlepoint_prob;
	
	/** 
	 * Probability of mating without mutation 
	 */
	public static final String MATE_ONLY_PROB = "mate_only_prob";
	
	@NeatParameter
	public static double mate_only_prob;
	
	/**
	 * Probability of forcing selection of ONLY links that are naturally recurrent
	 */
	public static final String RECUR_ONLY_PROB = "recur_only_prob";
	
	@NeatParameter
	public static double recur_only_prob;
	
	/** 
	 * Size of population 
	 */
	public static final String POP_SIZE = "pop_size";
	
	@NeatParameter
	public static int pop_size;
	
	/** 
	 * Age where Species starts to be penalized 
	 */
	public static final String DROPOFF_AGE = "dropoff_age";
	
	@NeatParameter
	public static int dropoff_age;
	
	/** 
	 * Number of tries mutate_add_link will attempt to find an open link 
	 */
	public static final String NEWLINK_TRIES = "newlink_tries";
	
	@NeatParameter
	public static int newlink_tries;
	
	/** 
	 * Tells to print population to file every n generations 
	 */
	public static final String PRINT_EVERY = "print_every";
	
	@NeatParameter
	public static int print_every;
	
	/** 
	 * The number of babies to siphen off to the champions 
	 */
	public static final String BABIES_STOLEN = "babies_stolen";
	
	@NeatParameter
	public static int babies_stolen;
	
	/** 
	 * The number of runs for an experiment 
	 */
	public static final String NUM_RUNS = "num_runs";
	
	@NeatParameter
	public int num_runs;
	
	
	///////////////////////////////////////
	// NOT SURE IF THESE ARE NEEDED HERE //
	///////////////////////////////////////
	private double maxFitness = 0.0;
	
	public void setMaxFitness(double maxFitness) {
		this.maxFitness = maxFitness;
	}

	public double getMaxFitness() {
		return maxFitness;
	}
	
	private int numberOfInputs = 0;
	
	public void setNumberOfInputs(int numberOfInputs) {
		this.numberOfInputs = numberOfInputs;
	}

	public int getNumberOfInputs() {
		return numberOfInputs;
	}

	private int numberOfSamples = 0;
	
	public void setNumberOfSamples(int numberOfSamples) {
		this.numberOfSamples = numberOfSamples;
	}

	public int getNumberOfSamples() {
		return numberOfSamples;
	}	

	private int numberOfOutputUnits = 0;
	
	public void setNumberOfOutputUnits(int numberOfOutputUnits) {
		this.numberOfOutputUnits = numberOfOutputUnits;
	}

	public int getNumberOfOutputUnits() {
		return numberOfOutputUnits;
	}
	
	public List<AbstractNeatParameter> parameters;
	
	public Neat() {}
	
	public List<AbstractNeatParameter> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<AbstractNeatParameter> parameters) {
		this.parameters = parameters;
		
		for (AbstractNeatParameter parameter : parameters) {
			setParameter(parameter);			
		}
	}
	
	public void setParameter(AbstractNeatParameter parameter) {
		parameter.set(this);
	}
}