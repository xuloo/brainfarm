package org.brainfarm.java.feat.operators;

import java.util.ArrayList;

import org.brainfarm.java.feat.FeatEvolutionStrategy;
import org.brainfarm.java.feat.Organism;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.enums.MutationType;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;

public class DefaultPopulationInitializationStrategy implements
		IPopulationInitializationStrategy {

	/**
	 * Initializes the population based on the initial genome.  The
	 * default strategy is to duplicate the initial genome but
	 * perturb it's Link Weights slightly for each individual.
	 */
	@Override
	public void initialize(IPopulation pop, IGenome genome, int size) {
		IGenome newgenome = null;
		pop.setOrganisms(new ArrayList<IOrganism>(size));
		
		for (int i = 1; i <= size; i++) {
			//duplicate the initial genome
			newgenome = genome.duplicate(i);
			
			//perturb it's link weights
			FeatEvolutionStrategy.getInstance().getMutationStrategy().mutateLinkWeight(newgenome,1.0, 1.0, MutationType.GAUSSIAN);
			
			//add the organism to the population
			IOrganism neworganism = FeatFactory.newOrganism(0.0, newgenome, 1);
			pop.getOrganisms().add(neworganism);
		}

		// Keep a record of the innovation and node number we are on
		pop.setCur_node_id(newgenome.getLastNodeId()+1);
		pop.setCur_innov_num(newgenome.getLastGeneInnovationId());

		// Separate the new Population into species
		FeatEvolutionStrategy.getInstance().getSpeciationStrategy().speciate(pop);
	}

}
