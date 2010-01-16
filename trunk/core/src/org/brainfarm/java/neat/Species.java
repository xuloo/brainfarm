/* Generated by Together */

package org.brainfarm.java.neat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.IOrganism;
import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.ISpecies;
import org.brainfarm.java.neat.api.enums.MutationType;
import org.brainfarm.java.neat.comparators.CompareOrganismsByFitness;
import org.brainfarm.java.util.RandomUtils;


public class Species implements ISpecies {
	
	private static Logger logger = Logger.getLogger(Species.class);
	
	/**
	 * id(-entification) of this species
	 */
	private int id;

	/**
	 * The age of the Species
	 */
	private int age;

	/**
	 * The average fitness of the Species
	 */
	private double averageFitness;

	/**
	 * Max fitness of the Species
	 */
	private double max_fitness;

	/**
	 * The max it ever had
	 */
	private double max_fitness_ever;

	/**
	 * how many child expected
	 */
	private int expectedOffspring;

	/**
	 * is new species ?
	 */
	private boolean novel;

	/**
	 * has tested ?
	 */
	private boolean checked;
	
	/**
	 * how many time from last updt? If this is too long ago, the Species will
	 * goes extinct.
	 */
	private int ageOfLastImprovement;

	/**
	 * list of all organisms in the Species
	 */
	public List<IOrganism> organisms = new ArrayList<IOrganism>();

	/**
	 * costructor with inly ID of specie
	 */
	public Species(int i) {
		id = i;
		age = 1;
		averageFitness = 0.0;
		expectedOffspring = 0;
		novel = false;
		ageOfLastImprovement = 0;
		max_fitness = 0;
		max_fitness_ever = 0;
	}
	
	/**
	 * 
	 * costructor with identification and flag for signaling if its a new specie
	 * 
	 */
	public Species(int i, boolean n) {
		id = i;
		age = 1;
		averageFitness = 0.0;
		expectedOffspring = 0;
		novel = n;
		ageOfLastImprovement = 0;
		max_fitness = 0;
		max_fitness_ever = 0;
	}

	/**
	 * add an organism to list of organisms in this specie
	 */

	public void addOrganism(IOrganism xorganism) {
		organisms.add(xorganism);
	}

	/**
	 * Can change the fitness of the organisms in the Species to be higher for
	 * very new species (to protect them); Divides the fitness by the size of
	 * the Species, so that fitness is "shared" by the species At end mark the
	 * organisms can be eliminated from this specie
	 */
	public void adjustFitness() {

		//Iterator itr_organism;
		IOrganism _organism = null;
		int num_parents = 0;
		int count = 0;
		int age_debt = 0;
		int j;
		age_debt = (age - ageOfLastImprovement + 1) - Neat.dropoff_age;
		if (age_debt == 0)
			age_debt = 1;

		int size1 = organisms.size();

		for (j = 0; j < size1; j++) {
			_organism = organisms.get(j);

			// Remember the original fitness before it gets modified
			_organism.setOriginalFitness(_organism.getFitness());

			// Make fitness decrease after a stagnation point dropoff_age
			// Added an if to keep species pristine until the dropoff point
			if (age_debt >= 1) {
				_organism.setFitness(_organism.getFitness() * 0.01);
				logger.debug("Dropped fitness to " + _organism.getFitness());
			}
			// Give a fitness boost up to some young age (niching)
			// The age_significance parameter is a system parameter
			// if it is 1, then young species get no fitness boost
			if (age <= 10)
				_organism.setFitness(_organism.getFitness() * Neat.age_significance);
			// Do not allow negative fitness
			if (_organism.getFitness() < 0.0)
				_organism.setFitness(0.0001);
			// Share fitness with the species
			_organism.setFitness(_organism.getFitness() / size1);

		}

		// Sort the population and mark for death those after survival_thresh *
		// pop_size

		Comparator<IOrganism> cmp = new CompareOrganismsByFitness();
		Collections.sort(organisms, cmp);

		// Update age_of_last_improvement here
		// (the first organism has the best fitness)
		if (organisms.get(0).getOriginalFitness() > max_fitness_ever) {
			ageOfLastImprovement = age;
			max_fitness_ever =  organisms.get(0).getOriginalFitness();
		}

		// Decide how many get to reproduce based on survival_thresh*pop_size
		// Adding 1.0 ensures that at least one will survive
		// floor is the largest (closest to positive infinity) double value that
		// is not greater
		// than the argument and is equal to a mathematical integer

		num_parents = (int) Math.floor((Neat.survival_thresh * ((double) size1)) + 1.0);

		// Mark for death those who are ranked too low to be parents
		// Mark the champ as such
		organisms.get(0).setChampion(true);

		Iterator<IOrganism> itr_organism = organisms.iterator();
		count = 1;
		
		while (itr_organism.hasNext() && count <= num_parents) {
			_organism = ((Organism) itr_organism.next());
			count++;
		}

		// found organism can be eliminated !
		while (itr_organism.hasNext()) {
			_organism = itr_organism.next();
			// Mark for elimination
			_organism.setEliminated(true);
		}
	}

	/**
	 * Read all organisms in this species and compute the summary of fitness; at
	 * and compute the average fitness (ave_fitness) with : ave_fitness =
	 * summary / (number of organisms) this is an average fitness for this
	 * specie
	 */
	public void computeAverageFitness() {

		double total = 0.0;

		for (IOrganism organism : organisms) {
			total += organism.getFitness();
		}

		averageFitness = total / organisms.size();
	}

	/**
	 * Read all organisms in this specie and return the maximum fitness of all
	 * organisms.
	 */
	public void computeMaxFitness() {
		
		double max = 0.0;

		for (IOrganism organism : organisms) {
			if (organism.getFitness() > max) {
				max = organism.getFitness();
			}
		}
		
		max_fitness = max;
	}

	/**
	 * Compute the collective offspring the entire species (the sum of all
	 * organism's offspring) is assigned skim is fractional offspring left over
	 * from a previous species that was counted. These fractional parts are kept
	 * unil they add up to 1
	 */
	public double countOffspring(double skim) {

		expectedOffspring = 0;

		double x1 = 0.0;
		double y1 = 1.0;
		double r1 = 0.0;
		double r2 = skim;
		int n1 = 0;
		int n2 = 0;

		for (IOrganism organism : organisms) {
			
			x1 = organism.getExpectedOffspring();

			n1 = (int) (x1 / y1);
			r1 = x1 - ((int) (x1 / y1) * y1);
			n2 = n2 + n1;
			r2 = r2 + r1;

			if (r2 >= 1.0) {
				n2 = n2 + 1;
				r2 = r2 - 1.0;
			}
		}

		expectedOffspring = n2;
		return r2;
	}

	public String toString() {

		StringBuilder s = new StringBuilder();
		
		s.append("SPECIES : ");
		s.append("  id < " + id + " >");
		s.append(" age=" + age);
		s.append(", ave_fitness=" + averageFitness);
		s.append(", max_fitness=" + max_fitness);
		s.append(", max_fitness_ever =" + max_fitness_ever);
		s.append(", expected_offspring=" + expectedOffspring);
		s.append(", age_of_last_improvement=" + ageOfLastImprovement);
		s.append("\n  This Species has " + organisms.size() + " organisms :");
		s.append("\n ---------------------------------------");

		for (IOrganism organism : organisms) {
			s.append(organism.toString());
		}
		
		return s.toString();
	}

	/**
	 * Compute generations since last improvement
	 */
	public int lastImproved() {
		return (age - ageOfLastImprovement);
	}

	/**
	 * Eliminate the organism passed in parameter list, from a list of organisms
	 * of this specie
	 */
	public void removeOrganism(IOrganism organism) {

		boolean rc = organisms.remove(organism);
		
		if (!rc) {
			logger.error("\n ALERT: Attempt to remove nonexistent Organism from Species");
		}
	}

	public boolean reproduce(int generation, IPopulation pop, List<ISpecies> sorted_species) {

		//boolean found; // When a Species is found
		boolean champ_done = false; // Flag the preservation of the champion

		// outside the species
		boolean mut_struct_baby;
		boolean mate_baby;
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
			logger.error("ERROR:  ATTEMPT TO REPRODUCE OUT OF EMPTY SPECIES");
			return false;
		}

		// elements for this specie
		poolsize = organisms.size() - 1;

		// the champion of the 'this' specie is the first element of the specie;
		thechamp = organisms.get(0);

		// Create the designated number of offspring for the Species one at a time.
		for (count = 0; count < expectedOffspring; count++) {

			mut_struct_baby = false;
			mate_baby = false;

			if (expectedOffspring > Neat.pop_size) {
				logger.warn("ALERT: EXPECTED OFFSPRING = " + expectedOffspring);
			}

			// If we have a super_champ (Population champion), finish off some special clones.
			if (thechamp.getSuperChampOffspring() > 0) {

				logger.debug("analysis of champion #" + count);
				// save in mom current champ;
				mom = thechamp;
				// create a new genome from this copy
				new_genome = mom.getGenome().duplicate(count);
				if ((thechamp.getSuperChampOffspring()) > 1) {
					if (RandomUtils.randomDouble() < .8 || Neat.mutate_add_link_prob == 0.0) {
						EvolutionStrategy.getMutationStrategy().mutateLinkWeight(new_genome, Neat.weight_mut_power, 1.0, MutationType.GAUSSIAN);
					} else {
						// Sometimes we add a link to a superchamp
						new_genome.genesis(generation);
						EvolutionStrategy.getMutationStrategy().mutateAddLink(new_genome,pop);
						mut_struct_baby = true;
					}
				}

				baby = new Organism(0.0, new_genome, generation);

				if (thechamp.getSuperChampOffspring() == 1) {
					if (thechamp.isPopulationChampion()) {
						// System.out.print("\n The new org baby's (champion) genome is : "+baby.genome.getGenome_id());
						baby.setPopulationChampionChild(true);
						baby.setHighestFitness(mom.getOriginalFitness());

					}
				}
				thechamp.incrementSuperChampOffspring();

			} // end population champ

			// If we have a Species champion, just clone it
			else if ((!champ_done) && (expectedOffspring > 5)) {
				mom = thechamp; // Mom is the champ
				new_genome = mom.getGenome().duplicate(count);
				baby = new Organism(0.0, new_genome, generation); // Baby is
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
				boolean mutatedStructure = EvolutionStrategy.getMutationStrategy().mutate(new_genome,pop,generation);
				mut_struct_baby = (mut_struct_baby || mutatedStructure);

				baby = new Organism(0.0, new_genome, generation);
			}

			// Otherwise we should mate
			else {
				// Choose the random mom
				logger.debug("mating .............");
				
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
					randspecies = this;
					// Select a random species
					giveup = 0;
					int sp_ext = 0;
					// Give up if you cant find a different Species
					while ((randspecies == this) && (giveup < 5)) {
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

				new_genome = EvolutionStrategy.getCrossoverStrategy().performCrossover(mom,_dad,count);

				mate_baby = true;

				// Determine whether to mutate the baby's Genome
				// This is done randomly or if the mom and dad are the same
				// organism

				if (RandomUtils.randomDouble() > Neat.mate_only_prob || 
					_dad.getGenome().getId() == mom.getGenome().getId() || 
					_dad.getGenome().compatibility(mom.getGenome()) == 0.0) {

					boolean mutatedStructure = EvolutionStrategy.getMutationStrategy().mutate(new_genome,pop,generation);
					mut_struct_baby = (mut_struct_baby || mutatedStructure);

					baby = new Organism(0.0, new_genome, generation);

				} // end block of prob
				// Determine whether to mutate the baby's Genome
				// This is done randomly or if the mom and dad are the same
				// organism

				else {
					// Create the baby without mutating first
					baby = new Organism(0.0, new_genome, generation);
				}

			}

			// Add the baby to its proper Species
			// If it doesn't fit a Species, create a new one

			baby.setMutStructBaby(mut_struct_baby);
			baby.setMateBaby(mate_baby);

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
				logger.debug("this is case of population with species pree-existent");
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
																		// a new
																		// specie
					pop.getSpecies().add(newspecies); // add this species to list of
													// species
					newspecies.addOrganism(baby); // add this baby to species
					baby.setSpecies(newspecies); // Point baby to owner specie
				}

			} // end block control and update species

		} // end offspring cycle

		return true;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public void incrementAge() {
		age++;
	}

	public double getAverageFitness() {
		return averageFitness;
	}

	public void setAverageFitness(double averageFitness) {
		this.averageFitness = averageFitness;
	}

	public double getMax_fitness() {
		return max_fitness;
	}

	public void setMax_fitness(double max_fitness) {
		this.max_fitness = max_fitness;
	}

	public double getMax_fitness_ever() {
		return max_fitness_ever;
	}

	public void setMax_fitness_ever(double max_fitness_ever) {
		this.max_fitness_ever = max_fitness_ever;
	}

	public int getExpectedOffspring() {
		return expectedOffspring;
	}

	public void setExpectedOffspring(int expectedOffspring) {
		this.expectedOffspring = expectedOffspring;
	}
	
	public void incrementExpectedOffspring() {
		expectedOffspring++;
	}
	
	public void incrementExpectedOffspring(double value) {
		expectedOffspring += value;
	}
	
	public void decrementExpectedOffspring(double value) {
		expectedOffspring -= value;
	}

	public boolean isNovel() {
		return novel;
	}

	public void setNovel(boolean novel) {
		this.novel = novel;
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<IOrganism> getOrganisms() {
		return organisms;
	}

	public void setOrganisms(List<IOrganism> organisms) {
		this.organisms = organisms;
	}

	public int getAgeOfLastImprovement() {
		return ageOfLastImprovement;
	}

	public void setAgeOfLastImprovement(int ageOfLastImprovement) {
		this.ageOfLastImprovement = ageOfLastImprovement;
	}
}