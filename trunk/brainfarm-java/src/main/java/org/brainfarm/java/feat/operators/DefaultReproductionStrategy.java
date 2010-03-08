package org.brainfarm.java.feat.operators;

import java.util.List;

import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ISpecies;
import org.brainfarm.java.feat.api.enums.MutationType;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.brainfarm.java.util.RandomUtils;

/**
 * This is the reproduction strategy employed by the the original
 * JNeat by Ugo Vierucci.
 * 
 * @author dtuohy, orig. Ugo Vierucci
 *
 */
public class DefaultReproductionStrategy implements IReproductionStrategy {

	private IEvolutionStrategy evolutionStrategy;
	
	protected IEvolutionParameters evolutionParameters;
	
	public DefaultReproductionStrategy(IEvolutionStrategy evolutionStrategy) {
		this.evolutionStrategy = evolutionStrategy;
		evolutionParameters = evolutionStrategy.getEvolutionParameters();
	}
	
	public boolean reproduce(ISpecies specie, int generation, IPopulation pop, List<ISpecies> sorted_species) {

		List<IOrganism> organisms = specie.getOrganisms();

		// outside the species
		int orgnum = 0;

		IOrganism mom = null;
		IOrganism baby = null;
		IGenome new_genome = null;
		IOrganism _organism = null;
		IOrganism _dad = null;

		if (specie.getExpectedOffspring() > 0 && organisms.size() == 0)
			return false;

		// elements for this species
		int poolsize = organisms.size() - 1;

		// the champion of the 'this' species is the first element of the species;
		IOrganism thechamp = organisms.get(0);
		boolean champ_done = false; // Flag the preservation of the champion
		
		// Create the designated number of offspring for the Species one at a time.
		for (int count = 0; count < specie.getExpectedOffspring(); count++) {

			// If we have a super_champ (Population champion), finish off some special clones.
			if (thechamp.getSuperChampOffspring() > 0) {
				
				// save in mom current champ;
				mom = thechamp;
				// create a new genome from this copy
				new_genome = mom.getGenome().duplicate(count);
				if ((thechamp.getSuperChampOffspring()) > 1) {
					if (RandomUtils.randomDouble() < .8 || evolutionParameters.getDoubleParameter(MUTATE_ADD_LINK_PROB) == 0.0)
						evolutionStrategy.getMutationStrategy().mutateLinkWeight(new_genome, evolutionParameters.getDoubleParameter(WEIGHT_MUT_POWER), 1.0, MutationType.GAUSSIAN);
					else {
						// Sometimes we add a link to a superchamp
						new_genome.generatePhenotype(generation);
						evolutionStrategy.getMutationStrategy().mutateAddLink(new_genome,pop);
					}
				}
				baby = FeatFactory.newOrganism(0.0, new_genome, generation);
				thechamp.incrementSuperChampOffspring();
			} // end population champ

			// If we have a Species champion, just clone it
			else if ((!champ_done) && (specie.getExpectedOffspring() > 5)) {
				mom = thechamp; // Mom is the champ
				new_genome = mom.getGenome().duplicate(count);
				baby = FeatFactory.newOrganism(0.0, new_genome, generation); // Baby is
				// just like mommy
				champ_done = true;

			} else if (RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(MUTATE_ONLY_PROB) || poolsize == 1) {

				// Choose the random parent
				orgnum = RandomUtils.randomInt(0, poolsize);
				_organism = organisms.get(orgnum);
				mom = _organism;
				new_genome = mom.getGenome().duplicate(count);

				// Do the mutation depending on probabilities of various mutations
				evolutionStrategy.getMutationStrategy().mutate(new_genome,pop,generation);

				baby = FeatFactory.newOrganism(0.0, new_genome, generation);
			}
			// Otherwise we should mate
			else {
				// Choose the random mom
				orgnum = RandomUtils.randomInt(0, poolsize);
				_organism = organisms.get(orgnum);

				// save in mom
				mom = _organism;
				// Choose random dad...
				// Mate within Species
				if (RandomUtils.randomDouble() > evolutionParameters.getDoubleParameter(INTERSPECIES_MATE_RATE)) {
					orgnum = RandomUtils.randomInt(0, poolsize);
					_organism = organisms.get(orgnum);
					_dad = _organism;
				}
				// Mate outside Species
				else {
					// save current species
					ISpecies randspecies = specie;
					// Select a random species
					int giveup = 0;
					// Give up if you can't find a different Species
					while ((randspecies == specie) && (giveup < 5)) {

						// Choose a random species tending towards better species
						double randmult = Math.min(1.0, RandomUtils.randomGaussian() / 4);

						// This tends to select better species
						int sp_ext = Math.max(0, (int) Math.floor((randmult * (sorted_species.size() - 1.0)) + 0.5));
						randspecies = sorted_species.get(sp_ext);
						++giveup;
					}
					_dad = randspecies.getOrganisms().get(0);
				}

				new_genome = evolutionStrategy.getCrossoverStrategy().performCrossover(mom,_dad,count);

				// Determine whether to mutate the baby's Genome
				// This is done randomly or if the mom and dad are the same organism
				if (RandomUtils.randomDouble() > evolutionParameters.getDoubleParameter(MATE_ONLY_PROB) || 
						_dad.getGenome().getId() == mom.getGenome().getId() || 
						_dad.getGenome().compatibility(mom.getGenome()) == 0.0) {

					evolutionStrategy.getMutationStrategy().mutate(new_genome,pop,generation);
					baby = FeatFactory.newOrganism(0.0, new_genome, generation);
				} 
				// end block of prob
				// Determine whether to mutate the baby's Genome
				// This is done randomly or if the mom and dad are the same organism

				// Create the baby without mutating first
				else 
					baby = FeatFactory.newOrganism(0.0, new_genome, generation);
			}
			evolutionStrategy.getSpeciationStrategy().addOrganismToSpecies(pop, baby);
		} // end offspring cycle
//		evolutionStrategy.getSpeciationStrategy().speciate(pop);
		return true;
	}
}