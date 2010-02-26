package org.brainfarm.java.feat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IInnovation;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ISpecies;
import org.brainfarm.java.feat.api.params.IEvolutionConstants;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.brainfarm.java.feat.comparators.CompareSpeciesByOriginalFitness;
import org.brainfarm.java.util.RandomUtils;

/** A Population is a group of Organisms including their species. 
 *
 * @author dtuohy, orig. Ugo Vierucci
 * @author Trevor Burton [trevor@flashmonkey.org]
 */
public class Population implements IPopulation, IEvolutionConstants {
	
	private static Logger logger = Logger.getLogger(Population.class);
	
	protected IEvolutionParameters evolutionParameters;
	
	/** The organisms in the Population */
	private List<IOrganism> organisms;

	/** Species in the Population the species should comprise all the genomes */
	private List<ISpecies> species;

	/** For holding the genetic innovations of the newest generation */
	private List<IInnovation> innovations = new ArrayList<IInnovation>();

	/** Current label number available for nodes */
	private int cur_node_id;

	/** Current number of innovation */
	private double cur_innov_num;

	/** The highest species number */
	private int lastSpecies;

	/** The last generation played */
	private int final_gen;
	
	// Fitness Statistics
	/** the mean of fitness in current epoch */
	private double mean_fitness;

	/** current variance in this epoch */
	private double variance;

	/** Is a current standard deviation in current epoch */
	private double standard_deviation;

	/**
	 * An integer that when above zero tells when the first winner appeared; the
	 * number is epoch number.
	 */
	private int winnergen;

	/** maximum fitness. (is used for delta code and stagnation detection) */
	private double highest_fitness;

	/** If too high, leads to delta coding process. */
	private int highest_last_changed;
	
	private IEvolutionStrategy evolutionStrategy;
	
	private IGenome genome;
	
	private int size;

	public Population(IEvolutionStrategy evolutionStrategy, IGenome g, int size) {
		winnergen = 0;
		highest_fitness = 0.0;
		highest_last_changed = 0;
				
		this.evolutionStrategy = evolutionStrategy;
		this.genome = g;
		this.size = size;
	}
	
	public void init() {
		evolutionStrategy.getPopulationInitializationStrategy().initialize(this, genome, size);
	}

	/*public String toString() {

		StringBuilder s = new StringBuilder();
		
		s.append("\n*P O P U L A T I O N*");
		s.append("\n\n\t This population has " + organisms.size() + " organisms, ");
		s.append(species.size() + " species :\n");

		for (IOrganism organism : organisms) {
			s.append(organism.toString());
		}

		for (ISpecies specie : species) {
			s.append(specie.toString());
		}
		
		return s.toString();
	}*/


	/**
	 * 
	 * epoch turns over a Population to the next generation based on fitness
	 * 
	 */
	public void epoch(int generation) {

		// Use Species' ages to modify the objective fitness of organisms
		// in other words, make it more fair for younger species
		// so they have a chance to take hold
		// Also penalize stagnant species
		// Then adjust the fitness using the species size to "share" fitness
		// within a species.
		// Then, within each Species, mark for death
		// those below survival_thresh * average
		for (ISpecies specie : species)
			specie.adjustFitness();

		// Go through the organisms and add up their fitnesses to compute the overall average
		double total = 0.0000;
		for (IOrganism organism : organisms)
			total += organism.getFitness();

		int total_organisms = organisms.size();
		double overall_average = total / total_organisms;

		// Now compute expected number of offspring for each individual organism
		for (IOrganism organism : organisms)
			organism.setExpectedOffspring(organism.getFitness() / overall_average);

		// Now add those offspring up within each Species to get the number of
		// offspring per Species
		double skim = 0.0;
		int total_expected = 0;
		for (ISpecies specie : species) {
			skim = specie.countOffspring(skim);
			total_expected += specie.getExpectedOffspring();
		}

		// Need to make up for lost floating point precision in offspring assignment
		// If we lost precision, give an extra baby to the best Species

		if (total_expected < total_organisms) {

			// Find the Species expecting the most
			int max_expected = 0;
			int final_expected = 0;

			ISpecies best_specie = null;
			for (ISpecies specie : species) {
				if (specie.getExpectedOffspring() >= max_expected) {
					max_expected = specie.getExpectedOffspring();
					best_specie = specie;
				}
				final_expected += specie.getExpectedOffspring();
			}
			// Give the extra offspring to the best species
			best_specie.incrementExpectedOffspring();
			final_expected++;

			// If we still arent at total, there is a problem
			// Note that this can happen if a stagnant Species
			// dominates the population and then gets killed off by its age
			// Then the whole population plummets in fitness
			// If the average fitness is allowed to hit 0, then we no longer have an average we can use to assign offspring.
			if (final_expected < total_organisms) {
				System.out.print("\n Sorry : Population .has DIED +");
				System.out.print("\n ------------------------------");
				for (ISpecies specie : species)
					specie.setExpectedOffspring(0);
				best_specie.setExpectedOffspring(total_organisms);
			}
		}

		List<ISpecies> sorted_species = new ArrayList<ISpecies>(species.size());
		
		// copy the Species pointers into a new Species list for sorting
		for (ISpecies specie : species)
			sorted_species.add(specie);

		// Sort the population and mark for death those after survival_thresh * pop_size.
		Collections.sort(sorted_species, new CompareSpeciesByOriginalFitness());

		// sorted species has all species ordered : the species with orig_fitness maximum is first.
		ISpecies curspecies = sorted_species.get(0);
		int best_species_num = curspecies.getId();

		StringBuffer rep1 = new StringBuffer("");
		
		rep1.append("\n  the BEST  specie is #" + best_species_num);

		// report current situation
		for (ISpecies specie : sorted_species) {
			rep1.append("\n  orig fitness of Species #" + specie.getId());
			rep1.append(" (Size " + specie.getOrganisms().size() + "): ");
			rep1.append(" is " + specie.getOrganisms().get(0).getOriginalFitness());
			rep1.append(" last improved ");
			rep1.append(specie.getAge() - specie.getAgeOfLastImprovement());
			rep1.append(" offspring " + specie.getExpectedOffspring());
		}

		rep1 = new StringBuffer("");

		curspecies = sorted_species.get(0);

		if (curspecies.getOrganisms().get(0).getOriginalFitness() > highest_fitness) {
			highest_fitness = curspecies.getOrganisms().get(0).getOriginalFitness();
			highest_last_changed = 0;
			rep1.append("\n    population has reached a new *RECORD FITNESS* -> " + highest_fitness);
		} else {
			++highest_last_changed;
			rep1.append("\n    are passed " + highest_last_changed
					+ " generations from last population fitness record: "
					+ highest_fitness);
		}

		// Check for stagnation- if there is stagnation, perform delta-coding
		if (highest_last_changed >= evolutionParameters.getIntParameter(DROPOFF_AGE) + 5)
			performDeltaCoding(sorted_species);
		else {
			// --------------------------------- block baby stolen (if baby
			// stolen > 0) -------------------------

			if (evolutionParameters.getIntParameter(BABIES_STOLEN) > 0) {
				ISpecies _specie = null;
				// Take away a constant number of expected offspring from the worst few species
				int stolen_babies = 0;
				for (int j = sorted_species.size() - 1; (j >= 0)
						&& (stolen_babies < evolutionParameters.getIntParameter(BABIES_STOLEN)); j--) {
					_specie = sorted_species.get(j);
					// System.out.print("\n Analisis SPECIE #"+j+" (size = "+_specie.organisms.size()+" )");
					if ((_specie.getAge() > 5) && (_specie.getExpectedOffspring() > 2)) {
						// System.out.print("\n ....STEALING!");
						int tmpi = evolutionParameters.getIntParameter(BABIES_STOLEN) - stolen_babies;
						if ((_specie.getExpectedOffspring() - 1) >= tmpi) {
							_specie.incrementExpectedOffspring(tmpi);
							stolen_babies = evolutionParameters.getIntParameter(BABIES_STOLEN);
						} else
						// Not enough here to complete the pool of stolen
						{
							stolen_babies += _specie.getExpectedOffspring() - 1;
							_specie.setExpectedOffspring(1);
						}
					}
				}

				// Mark the best champions of the top species to be the super champs
				// who will take on the extra offspring for cloning or mutant cloning
				// Determine the exact number that will be given to the top three
				// They get, in order, 1/5 1/5 and 1/10 of the stolen babies

				int tb_four[] = new int[3];
				tb_four[0] = evolutionParameters.getIntParameter(BABIES_STOLEN) / 5;
				tb_four[1] = tb_four[0];
				tb_four[2] = evolutionParameters.getIntParameter(BABIES_STOLEN) / 10;

				boolean done = false;
				Iterator<ISpecies> itr_specie = sorted_species.iterator();
				int i_block = 0;

				while (!done && itr_specie.hasNext()) {
					_specie = ((Species) itr_specie.next());
					if (_specie.lastImproved() <= evolutionParameters.getIntParameter(DROPOFF_AGE)) {
						if (i_block < 3) {
							if (stolen_babies >= tb_four[i_block]) {
								_specie.getOrganisms().get(0).setSuperChampOffspring(tb_four[i_block]);
								_specie.incrementExpectedOffspring(tb_four[i_block]);
								stolen_babies -= tb_four[i_block];
								System.out.print("\n  give " + tb_four[i_block] + " babies to specie #" + _specie.getId());
							}
							i_block++;
						}

						else if (i_block >= 3) {
							if (RandomUtils.randomDouble() > 0.1) {
								if (stolen_babies > 3) {
									_specie.getOrganisms().get(0).setSuperChampOffspring(3);
									_specie.incrementExpectedOffspring(3);
									stolen_babies -= 3;
									
									logger.info("Give 3 babies to Species " + _specie.getId());
								} else {
									_specie.getOrganisms().get(0).setSuperChampOffspring(stolen_babies);
									_specie.incrementExpectedOffspring(stolen_babies);
									
									logger.info("Give " + stolen_babies + " babies to Species " + _specie.getId());
									
									stolen_babies = 0;
								}
							}
							if (stolen_babies == 0)
								done = true;
						}
					}
				}

				if (stolen_babies > 0) {

					sorted_species.get(0).getOrganisms().get(0).incrementSuperChampOffspring(stolen_babies);
					_specie.incrementExpectedOffspring(stolen_babies);
					
					logger.info("force +" + stolen_babies + " offspring to Species " + _specie.getId());
					
					stolen_babies = 0;
				}
			} // end baby_stolen > 0

		}
		// ---------- phase of elimination of organism with flag eliminate ------------
		List<IOrganism> vdel = new ArrayList<IOrganism>(organisms.size());
		
		for (IOrganism organism : organisms)
			if (organism.isEliminated()) {
				// Remove the organism from its Species
				organism.getSpecies().removeOrganism(organism);
				// store the organism can be elimanated;
				vdel.add(organism);
			}
		
		// eliminate organism from master list
		for (IOrganism organism : vdel)
			organisms.remove(organism);

		vdel.clear();
		
		// ---------- phase of reproduction -----------
		for (ISpecies specie : sorted_species)
			evolutionStrategy.getReproductionStrategy().reproduce(specie,generation, this, sorted_species);

		//
		// Destroy and remove the old generation from the organisms and species
		// (because we have pointer to organisms , the new organisms created
		// are not in organisms and can't be eliminated;
		// thus are eliminate only current organisms !)

		// ------before---------------------------------------
		/*
		 * 
		 * itr_organism = organisms.iterator(); vdel = new
		 * Vector(organisms.size());
		 * 
		 * while (itr_organism.hasNext()) { Organism _organism = ((Organism)
		 * itr_organism.next()); //Remove the organism from its Species _specie
		 * = _organism.species; _specie.remove_org(_organism); //store the
		 * organism can be elimanated; vdel.add(_organism); }
		 * 
		 * //eliminate organism from master list for (int i = 0; i <
		 * vdel.size(); i++) { Organism _organism = (Organism)
		 * vdel.elementAt(i); // organisms.remove(_organism);
		 * organisms.removeElement(_organism); }
		 */

		// ---------------------------------------------

		// ------after---------------------------------------
		for (IOrganism organism : organisms) {
			// Remove the organism from its Species
			organism.getSpecies().removeOrganism(organism);
		}

		organisms.clear();

		// Remove all empty Species and age ones that survive
		// As this happens, create master organism list for the new generation.
		List<ISpecies> sdel = new ArrayList<ISpecies>(species.size());
		int orgcount = 0;

		for (ISpecies specie : species) {
			int size_of_curr_specie = specie.getOrganisms().size();

			if (size_of_curr_specie == 0) {
				sdel.add(specie);
			} else {
				// Age any Species that is not newly created in this generation
				if (specie.isNovel()) {
					specie.setNovel(false);
				} else {
					specie.incrementAge();
				}
				// From the current species recostruct thge master list
				for (int j = 0; j < size_of_curr_specie; j++) {
					IOrganism organism = specie.getOrganisms().get(j);
					organism.getGenome().setId(orgcount++);
					organism.getGenome().getPhenotype().setId(organism.getGenome().getId());

					organisms.add(organism);
				}
			}
		}

		// Eliminate species marked from master list.
		for (ISpecies specie : sdel)
			species.remove(specie);

		// Remove the innovations of the current generation
		innovations.clear();

		// TODO: Check to see if the best species died somehow
		// We don't want this to happen
		boolean best_ok = false;

		for (ISpecies specie : species) {
			if (specie.getId() == best_species_num) {
				best_ok = true;
				break;
			}
		}

		if (!best_ok) {
			logger.info("The Best Species Died!"); 
		} else {
			logger.info("The best Species #" + best_species_num + " survived");
		}
	}

	private void performDeltaCoding(List<ISpecies> sorted_species) {
		int half_pop;
		int tmpi;
		// ------------------ block delta coding
		// ----------------------------
		System.out.print("\n+  <PERFORMING DELTA CODING>");
		highest_last_changed = 0;
		half_pop = organisms.size() / 2;
		tmpi = organisms.size() - half_pop;
		System.out.print("\n  Pop size is " + organisms.size());
		System.out.print(", half_pop=" + half_pop + ",   pop_size - halfpop=" + tmpi);

		Iterator<ISpecies> itr_specie = sorted_species.iterator();
		ISpecies _specie = ((Species) itr_specie.next());

		// the first organism of first species can have offspring = 1/2 pop size
		_specie.getOrganisms().get(0).setSuperChampOffspring(half_pop);
		// the first species can have offspring = 1/2 pop size
		_specie.setExpectedOffspring(half_pop);
		_specie.setAgeOfLastImprovement(_specie.getAge());

		if (itr_specie.hasNext()) {
			_specie = itr_specie.next();
			_specie.getOrganisms().get(0).setSuperChampOffspring(half_pop);
			// the second species can have offspring = 1/2 pop size
			_specie.setExpectedOffspring(half_pop);
			_specie.setAgeOfLastImprovement(_specie.getAge());
			// at this moment the offpring is terminated : the remainder
			// species has 0 offspring!
			while (itr_specie.hasNext()) {
				_specie = ((Species) itr_specie.next());
				_specie.setExpectedOffspring(0);
			}
		} else {
			_specie.getOrganisms().get(0).incrementSuperChampOffspring(organisms.size() - half_pop);
			_specie.incrementExpectedOffspring(organisms.size() - half_pop);
		}
	}

	/**
	 * the increment of cur_node_id must be executed only from a method of
	 * population for security reason
	 */
	public int getCurrentNodeIdAndIncrement() {
		return cur_node_id++;
	}

	/**
	 * the increment of cur_innov_num must be executed only from a method of
	 * population for security reason
	 */

	public double getCurrentInnovationNumberAndIncrement() {
		return cur_innov_num++;
	}

	public void incrementCur_innov_num() {
		cur_innov_num += 1;
	}

	public void incrementCur_node_id() {
		cur_node_id += 1;
	}
	
	/**
	 * Insert the method's description here. Creation date: (01/02/2002 9.48.44)
	 */
	public Population() {
	}

	/**
	 * Debug Population Note: This checks each genome by verifying each one Only
	 * useful for debugging
	 */
	public void verify() {

		for (IOrganism organism : organisms) { 
			organism.getGenome().verify();
		}
	}
	
	public List<IOrganism> getOrganisms() {
		return organisms;
	}

	public void setOrganisms(List<IOrganism> organisms) {
		this.organisms = organisms;
	}

	public List<ISpecies> getSpecies() {
		return species;
	}

	public void setSpecies(List<ISpecies> species) {
		this.species = species;
	}

	public List<IInnovation> getInnovations() {
		return innovations;
	}

	public void setInnovations(List<IInnovation> innovations) {
		this.innovations = innovations;
	}

	public int getCur_node_id() {
		return cur_node_id;
	}

	public void setCur_node_id(int cur_node_id) {
		this.cur_node_id = cur_node_id;
	}

	public double getCur_innov_num() {
		return cur_innov_num;
	}

	public void setCur_innov_num(double cur_innov_num) {
		this.cur_innov_num = cur_innov_num;
	}

	public int getLastSpecies() {
		return lastSpecies;
	}

	public void setLastSpecies(int lastSpecies) {
		this.lastSpecies = lastSpecies;
	}
	
	public void incrementLastSpecies() {
		lastSpecies++;
	}

	public int getFinal_gen() {
		return final_gen;
	}

	public void setFinal_gen(int final_gen) {
		this.final_gen = final_gen;
	}

	public double getMean_fitness() {
		return mean_fitness;
	}

	public void setMean_fitness(double mean_fitness) {
		this.mean_fitness = mean_fitness;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public double getStandard_deviation() {
		return standard_deviation;
	}

	public void setStandard_deviation(double standard_deviation) {
		this.standard_deviation = standard_deviation;
	}

	public int getWinnergen() {
		return winnergen;
	}

	public void setWinnergen(int winnergen) {
		this.winnergen = winnergen;
	}

	public double getHighest_fitness() {
		return highest_fitness;
	}

	public void setHighest_fitness(double highest_fitness) {
		this.highest_fitness = highest_fitness;
	}

	public int getHighest_last_changed() {
		return highest_last_changed;
	}

	public void setHighest_last_changed(int highest_last_changed) {
		this.highest_last_changed = highest_last_changed;
	}
	
	@Override
	public void setEvolutionParameters(IEvolutionParameters evolutionParameters) {
		this.evolutionParameters = evolutionParameters;
	}
}