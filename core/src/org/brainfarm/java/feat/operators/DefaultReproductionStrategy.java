package org.brainfarm.java.feat.operators;

import java.util.Iterator;
import java.util.List;

import org.brainfarm.java.feat.EvolutionStrategy;
import org.brainfarm.java.feat.Neat;
import org.brainfarm.java.feat.Organism;
import org.brainfarm.java.feat.Species;
import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ISpecies;
import org.brainfarm.java.feat.api.enums.MutationType;
import org.brainfarm.java.feat.api.operators.IReproductionStrategy;
import org.brainfarm.java.util.RandomUtils;

/**
 * This is the reproduction strategy employed by the the original
 * JNeat by Ugo Vierucci.
 * 
 * @author dtuohy, orig. Ugo Vierucci
 *
 */
public class DefaultReproductionStrategy implements IReproductionStrategy{

	private IEvolutionStrategy evolutionStrategy;
	
	public DefaultReproductionStrategy(IEvolutionStrategy evolutionStrategy) {
		this.evolutionStrategy = evolutionStrategy;
	}
	
	public boolean reproduce(ISpecies specie, int generation, IPopulation pop, List<ISpecies> sorted_species) {

		List<IOrganism> organisms = specie.getOrganisms();
		int expectedOffspring = specie.getExpectedOffspring();

		boolean champ_done = false; // Flag the preservation of the champion

		// outside the species
		int giveup = 0; // For giving up finding a mate
		int count = 0;
		int poolsize = 0;
		int orgnum = 0;
		int randspeciesnum = 0;

		double randmult = 0.0;

		ISpecies newspecies = null;
		IOrganism compare_org = null;
		IOrganism thechamp = null;
		IOrganism mom = null;
		IOrganism baby = null;
		IGenome new_genome = null;
		IOrganism _organism = null;
		IOrganism _dad = null;
		ISpecies randspecies = null;

		if (expectedOffspring > 0 && organisms.size() == 0) {
			//			logger.error("ERROR:  ATTEMPT TO REPRODUCE OUT OF EMPTY SPECIES");
			return false;
		}

		// elements for this species
		poolsize = organisms.size() - 1;

		// the champion of the 'this' species is the first element of the specie;
		thechamp = organisms.get(0);

		// Create the designated number of offspring for the Species one at a time.
		for (count = 0; count < expectedOffspring; count++) {

			// If we have a super_champ (Population champion), finish off some special clones.
			if (thechamp.getSuperChampOffspring() > 0) {

				// logger.debug("analysis of champion #" + count);
				// save in mom current champ;
				mom = thechamp;
				// create a new genome from this copy
				new_genome = mom.getGenome().duplicate(count);
				if ((thechamp.getSuperChampOffspring()) > 1) {
					if (RandomUtils.randomDouble() < .8 || Neat.mutate_add_link_prob == 0.0) {

						evolutionStrategy.getMutationStrategy().mutateLinkWeight(new_genome, Neat.weight_mut_power, 1.0, MutationType.GAUSSIAN);

					} else {
						// Sometimes we add a link to a superchamp
						new_genome.generatePhenotype(generation);

						evolutionStrategy.getMutationStrategy().mutateAddLink(new_genome,pop);
					}
				}

				baby = FeatFactory.newOrganism(0.0, new_genome, generation);

				thechamp.incrementSuperChampOffspring();

			} // end population champ

			// If we have a Species champion, just clone it
			else if ((!champ_done) && (expectedOffspring > 5)) {
				mom = thechamp; // Mom is the champ
				new_genome = mom.getGenome().duplicate(count);
				baby = FeatFactory.newOrganism(0.0, new_genome, generation); // Baby is
				// just like
				// mommy
				champ_done = true;

			} else if (RandomUtils.randomDouble() < Neat.mutate_only_prob || poolsize == 1) {

				// Choose the random parent
				orgnum = RandomUtils.randomInt(0, poolsize);
				_organism = (Organism) organisms.get(orgnum);
				mom = _organism;
				new_genome = mom.getGenome().duplicate(count);

				// Do the mutation depending on probabilities of various mutations
				evolutionStrategy.getMutationStrategy().mutate(new_genome,pop,generation);

				baby = FeatFactory.newOrganism(0.0, new_genome, generation);
			}

			// Otherwise we should mate
			else {
				// Choose the random mom
				//				logger.debug("mating .............");

				orgnum = RandomUtils.randomInt(0, poolsize);

				_organism = organisms.get(orgnum);

				// save in mom
				mom = _organism;
				// Choose random dad
				// Mate within Species
				if (RandomUtils.randomDouble() > Neat.interspecies_mate_rate) {
					orgnum = RandomUtils.randomInt(0, poolsize);
					_organism = organisms.get(orgnum);
					_dad = _organism;
				}

				// Mate outside Species
				else {
					// save current species
					randspecies = specie;
					// Select a random species
					giveup = 0;
					int sp_ext = 0;
					// Give up if you cant find a different Species
					while ((randspecies == specie) && (giveup < 5)) {
						// This old way just chose any old species
						// randspeciesnum=NeatRoutine.randint(0,pop.species.size()-1);
						// Choose a random species tending towards better
						// species
						randmult = RandomUtils.randomGaussian() / 4;
						if (randmult > 1.0) {
							randmult = 1.0;
						}
						// This tends to select better species
						randspeciesnum = (int) Math.floor((randmult * (sorted_species.size() - 1.0)) + 0.5);
						for (sp_ext = 0; sp_ext < randspeciesnum; sp_ext++) {
						}
						randspecies = sorted_species.get(sp_ext);
						++giveup;
					}

					_dad = randspecies.getOrganisms().get(0);
				}

				new_genome = evolutionStrategy.getCrossoverStrategy().performCrossover(mom,_dad,count);

				// Determine whether to mutate the baby's Genome
				// This is done randomly or if the mom and dad are the same
				// organism

				if (RandomUtils.randomDouble() > Neat.mate_only_prob || 
						_dad.getGenome().getId() == mom.getGenome().getId() || 
						_dad.getGenome().compatibility(mom.getGenome()) == 0.0) {

					evolutionStrategy.getMutationStrategy().mutate(new_genome,pop,generation);

					baby = FeatFactory.newOrganism(0.0, new_genome, generation);

				} // end block of prob
				// Determine whether to mutate the baby's Genome
				// This is done randomly or if the mom and dad are the same
				// organism

				else {
					// Create the baby without mutating first
					baby = FeatFactory.newOrganism(0.0, new_genome, generation);
				}

			}

			// Add the baby to its proper Species
			// If it doesn't fit a Species, create a new one

			// if list species is empty , create the first species!
			if (pop.getSpecies().isEmpty()) {
				pop.incrementLastSpecies();
				newspecies = new Species(pop.getLastSpecies(), true); // create a
				// new
				// specie
				pop.getSpecies().add(newspecies); // add this species to list of
				// species
				newspecies.addOrganism(baby); // add this baby to species
				baby.setSpecies(newspecies); // Point baby to owner specie
			} else {
				// looop in all species.... (each species is a Vector of
				// organism...) of population 'pop'
				//				logger.debug("this is case of population with species pree-existent");
				Iterator<ISpecies> speciesIterator = pop.getSpecies().iterator();
				boolean done = false;

				while (!done && speciesIterator.hasNext()) {
					// point _species-esima
					ISpecies _specie = speciesIterator.next();
					// point to first organism of this _specie-esima
					compare_org = _specie.getOrganisms().get(0);
					// compare _organism-esimo('_organism') with first organism
					// in current specie('compare_org')
					double curr_compat = baby.getGenome().compatibility(compare_org.getGenome());

					// System.out.print("\n     affinity = "+curr_compat);
					if (curr_compat < Neat.compat_threshold) {
						// Found compatible species, so add this baby to it
						_specie.addOrganism(baby);
						// update in baby pointer to its species
						baby.setSpecies(_specie);
						// force exit from this block ...
						done = true;
					}
				}

				if (!done) {
					pop.incrementLastSpecies();
					newspecies = new Species(pop.getLastSpecies(), true); // create
					// a new species
					pop.getSpecies().add(newspecies); // add this species to list of
					// species
					newspecies.addOrganism(baby); // add this baby to species
					baby.setSpecies(newspecies); // Point baby to owner specie
				}

			} // end block control and update species

		} // end offspring cycle

		return true;
	}

}