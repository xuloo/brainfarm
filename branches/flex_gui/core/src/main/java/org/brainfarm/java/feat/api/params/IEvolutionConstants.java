package org.brainfarm.java.feat.api.params;

public interface IEvolutionConstants {

	/** 
	 * The power of a linkweight mutation 
	 */
	public static final String WEIGHT_MUT_POWER = "weight_mut_power";
	
	//public static double weight_mut_power;
	
	/**
	 * Probability that a link mutation which doesn't have to be recurrent will be made recurrent
	 */
	public static final String RECURE_PROB = "recur_prob";
	
	//public static double recur_prob;
	
	/** 
	 * factor multiply for gene not equal 
	 */
	public static final String DISJOINT_COEFF = "disjoint_coeff";
	
	//public static double disjoint_coeff;
	
	/** 
	 * factor multiply for gene excedeed 
	 */
	public static final String EXCESS_COEFF = "excess_coeff";
	
	//public static double excess_coeff;
	
	/** 
	 * factor multiply weight difference 
	 */
	public static final String MUTDIFF_COEFF = "mutdiff_coeff";
	
	//public static double mutdiff_coeff;
	
	/** 
	 * threshold under which two Genomes are the same species 
	 */
	public static final String COMPAT_THRESHOLD = "compat_threshold";
	
	//public static double compat_threshold;
	
	/** 
	 * How much does age matter in epoch cycle 
	 */
	public static final String AGE_SIGNIFICANCE = "age_significance";
	
	//public static double age_significance;
	
	/** 
	 * Percent of ave fitness for survival 
	 */
	public static final String SURVIVAL_THRESH = "survival_thresh";
	
	//public static double survival_thresh;
	
	/** 
	 * Probability of a non-mating reproduction 
	 */
	public static final String MUTATE_ONLY_PROB = "mutate_only_prob";
	
	//public static double mutate_only_prob;
	
	/** 
	 * Probability of mutate link weight 
	 */
	public static final String MUTATE_LINK_WEIGHTS_PROB = "mutate_link_weights_prob";
	
	//public static double mutate_link_weights_prob;
	
	/** 
	 * Probability of mutate status ena->dis | dis-ena of gene 
	 */
	public static final String MUTATE_TOGGLE_ENABLE_PROB = "mutate_toggle_enable_prob";
	
	//public static double mutate_toggle_enable_prob;
	
	/** 
	 * Probability of switch status to ena of gene 
	 */
	public static final String MUTATE_GENE_REENABLE_PROB = "mutate_gene_reenable_prob";
	
	//public static double mutate_gene_reenable_prob;
	
	/** 
	 * Probability of add a node to struct of genome 
	 */
	public static final String MUTATE_ADD_NODE_PROB = "mutate_add_node_prob";
	
	//public static double mutate_add_node_prob;
	
	/** 
	 * Probability of add a link to struct of genome 
	 */
	public static final String MUTATE_ADD_LINK_PROB = "mutate_add_link_prob";
	
	//public static double mutate_add_link_prob;
	
	/** 
	 * Probability of a mate being outside species 
	 */
	public static final String INTERSPECIES_MATE_RATE = "interspecies_mate_rate";
	
	//public static double interspecies_mate_rate;
	
	/** 
	 * Probability of cross in a many point of two genome 
	 */
	public static final String MATE_MULTIPOINT_PROB = "mate_multipoint_prob";
	
	//public static double mate_multipoint_prob;
	
	/** 
	 * Probability of cross in a many point of two genome with media 
	 */
	public static final String MATE_MULTIPOINT_AVG_PROB = "mate_multipoint_avg_prob";
	
	//public static double mate_multipoint_avg_prob;
	
	/** 
	 * Probability of cross in a single point of two genome 
	 */
	public static final String MATE_SINGLEPOINT_PROB = "mate_singlepoint_prob";
	
	//public static double mate_singlepoint_prob;
	
	/** 
	 * Probability of mating without mutation 
	 */
	public static final String MATE_ONLY_PROB = "mate_only_prob";
	
	//public static double mate_only_prob;
	
	/**
	 * Probability of forcing selection of ONLY links that are naturally recurrent
	 */
	public static final String RECUR_ONLY_PROB = "recur_only_prob";
	
	//public static double recur_only_prob;
	
	/** 
	 * Age where Species starts to be penalized 
	 */
	public static final String DROPOFF_AGE = "dropoff_age";
	
	//public static int dropoff_age;
	
	/** 
	 * Number of tries mutate_add_link will attempt to find an open link 
	 */
	public static final String NEWLINK_TRIES = "newlink_tries";
	
	//public static int newlink_tries;
	
	/** 
	 * Tells to print population to file every n generations 
	 */
	public static final String PRINT_EVERY = "print_every";
	
	//public static int print_every;
	
	/** 
	 * The number of babies to siphen off to the champions 
	 */
	public static final String BABIES_STOLEN = "babies_stolen";
	
	//public static int babies_stolen;
}
