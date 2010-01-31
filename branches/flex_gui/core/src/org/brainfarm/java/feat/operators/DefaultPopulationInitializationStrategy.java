package org.brainfarm.java.feat.operators;

import java.util.ArrayList;

import org.brainfarm.java.feat.EvolutionStrategy;
import org.brainfarm.java.feat.Organism;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.enums.MutationType;
import org.brainfarm.java.feat.api.operators.IPopulationInitializationStrategy;

public class DefaultPopulationInitializationStrategy implements
		IPopulationInitializationStrategy {

	@Override
	public void initialize(IPopulation pop, IGenome genome, int size) {
		IGenome newgenome = null;
		pop.setOrganisms(new ArrayList<IOrganism>(size));
		
		for (int i = 1; i <= size; i++) {
			newgenome = genome.duplicate(i);
			EvolutionStrategy.getInstance().getMutationStrategy().mutateLinkWeight(newgenome,1.0, 1.0, MutationType.GAUSSIAN);
			IOrganism neworganism = EvolutionStrategy.getInstance().getModelObjectFactory().createOrganism(0.0, newgenome, 1);
			pop.getOrganisms().add(neworganism);
		}

		// Keep a record of the innovation and node number we are on
		pop.setCur_node_id(newgenome.getLastNodeId()+1);
		pop.setCur_innov_num(newgenome.getLastGeneInnovationId());

		// Separate the new Population into species
		EvolutionStrategy.getInstance().getSpeciationStrategy().speciate(pop);
	}

}
