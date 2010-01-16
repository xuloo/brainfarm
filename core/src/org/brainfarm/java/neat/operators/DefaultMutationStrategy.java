package org.brainfarm.java.neat.operators;

import org.brainfarm.java.neat.Neat;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.enums.MutationType;
import org.brainfarm.java.neat.api.operators.IMutationStrategy;
import org.brainfarm.java.util.RandomUtils;

public class DefaultMutationStrategy implements IMutationStrategy {

	// The weight mutation power is species specific depending on its age
	double mut_power = Neat.weight_mut_power;
	
	@Override
	public boolean mutate(IGenome genome, IPopulation pop, int generation) {
		boolean mutatedStructure = false;
		
		if (RandomUtils.randomDouble() < Neat.mutate_add_node_prob) {
//			logger.debug("....species.reproduce.mutate add node");
			genome.mutateAddNode(pop);
			mutatedStructure = true;
		} else if (RandomUtils.randomDouble() < Neat.mutate_add_link_prob) {
//			logger.debug("....mutate add link");
			genome.genesis(generation);
			genome.mutateAddLink(pop, Neat.newlink_tries);
			mutatedStructure = true;
		} else {
			// If we didn't do a structural mutation, we do the other kinds.
			if (RandomUtils.randomDouble() < Neat.mutate_random_trait_prob) {
//				logger.debug("...mutate random trait");
				genome.mutateRandomTrait();
			}

			if (RandomUtils.randomDouble() < Neat.mutate_link_trait_prob) {
//				logger.debug("...mutate linktrait");
				genome.mutateLinkTrait(1);
			}

			if (RandomUtils.randomDouble() < Neat.mutate_node_trait_prob) {
//				logger.debug("...mutate node trait");
				genome.mutateNodeTrait(1);
			}

			if (RandomUtils.randomDouble() < Neat.mutate_link_weights_prob) {
//				logger.debug("...mutate link weight");
				genome.mutateLinkWeight(mut_power, 1.0,
						MutationType.GAUSSIAN);
			}

			if (RandomUtils.randomDouble() < Neat.mutate_toggle_enable_prob) {
//				logger.debug("...mutate toggle enable");
				genome.mutateToggleEnable(1);
			}

			if (RandomUtils.randomDouble() < Neat.mutate_gene_reenable_prob) {
//				logger.debug("...mutate gene_reenable:");
				genome.mutateGeneReenable();
			}
		}
		return mutatedStructure;
	}
	
}
