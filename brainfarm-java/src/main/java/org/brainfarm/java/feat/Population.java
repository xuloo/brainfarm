package org.brainfarm.java.feat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IInnovation;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ISpecies;
import org.brainfarm.java.feat.api.enums.InnovationType;
import org.brainfarm.java.feat.api.params.IEvolutionConstants;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.brainfarm.java.feat.comparators.CompareSpeciesByOriginalFitness;

/** A Population is a group of Organisms including their species. 
 *
 * @author dtuohy, orig. Ugo Vierucci
 * @author Trevor Burton [trevor@flashmonkey.org]
 */
public class Population implements IPopulation, IEvolutionConstants {

	protected IEvolutionParameters evolutionParameters;

	/** The organisms in the Population */
	private List<IOrganism> organisms;

	/** Species in the Population the species should comprise all the genomes */
	private List<ISpecies> species = new ArrayList<ISpecies>();

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

	/** When > 0, Indicates epoch when first winner appeared */
	private int winnergen;

	/** maximum fitness. (is used for delta code and stagnation detection) */
	private double highest_fitness;

	/** If too high, leads to delta coding process. */
	private int highest_last_changed;

	/** the (usually minimal) genome from which the population is initialized */
	private IGenome genome;

	private IEvolutionStrategy evolutionStrategy;

	private int size;

	public Population(IEvolutionStrategy evolutionStrategy, IGenome g, int size) {
		winnergen = 0;
		highest_fitness = 0.0;
		highest_last_changed = 0;		
		this.evolutionStrategy = evolutionStrategy;
		this.genome = g;
		this.size = size;
	}

	public Population() {}

	public void init() {
		evolutionStrategy.getPopulationInitializationStrategy().initialize(this, genome, size);
	}

	/**
	 * Turns over a Population to the next generation based on fitness.
	 */
	public void epoch(int generation) {

		//fitness is adjusted based on (a) species age and (b) fitness sharing within species
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
			// Then the whole population plumets in fitness
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

		if (curspecies.getOrganisms().get(0).getOriginalFitness() > highest_fitness) {
			highest_fitness = curspecies.getOrganisms().get(0).getOriginalFitness();
			highest_last_changed = 0;
		} else 
			highest_last_changed++;

		// Check for stagnation- if there is stagnation, perform delta-coding
		if (highest_last_changed >= evolutionParameters.getIntParameter(DROPOFF_AGE) + 5)
			performDeltaCoding(sorted_species);

		// ---------- phase of elimination of organism with flag eliminate ------------
		List<IOrganism> vdel = new ArrayList<IOrganism>(organisms.size());

		for (IOrganism organism : organisms)
			if (organism.isEliminated()) {
				// Remove the organism from its Species
				organism.getSpecies().removeOrganism(organism);
				// store the organism can be eliminated;
				vdel.add(organism);
			}

		// eliminate organism from master list
		for (IOrganism organism : vdel)
			organisms.remove(organism);
		vdel.clear();

		// ---------- phase of reproduction -----------
		evolutionStrategy.getReproductionStrategy().reproduce(generation, this, sorted_species);

		// Destroy and remove the old generation from the organisms and species
		// (because we have pointer to organisms , the new organisms created
		// are not in organisms and can't be eliminated;
		// thus are eliminate only current organisms !)
		for (IOrganism organism : organisms)
			organism.getSpecies().removeOrganism(organism);
		organisms.clear();

		// Remove all empty Species and age ones that survive
		// As this happens, create master organism list for the new generation.
		List<ISpecies> sdel = new ArrayList<ISpecies>(species.size());
		int orgcount = 0;

		for (ISpecies specie : species) {
			if (specie.getOrganisms().size() == 0)
				sdel.add(specie);
			else {
				// Age any Species that is not newly created in this generation
				if (specie.isNovel()) 
					specie.setNovel(false);
				else
					specie.incrementAge();
				// From the current species reconstruct the master list
				for (int j = 0; j < specie.getOrganisms().size(); j++) {
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
	}

	private void performDeltaCoding(List<ISpecies> sorted_species) {

		// ------------------ block delta coding ----------------------------
		System.out.print("\n+  <PERFORMING DELTA CODING>");
		highest_last_changed = 0;
		int half_pop = organisms.size() / 2;
		int tmpi = organisms.size() - half_pop;
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

	@Override
	public IInnovation getExistingNodeInnovation(int inNodeId, int outNodeId, double innovationNumber) {
		for(IInnovation _innov : getInnovations())
			if ((_innov.getInnovationType() == InnovationType.NEW_NODE) && (_innov.getInputNodeId() == inNodeId)
					&& (_innov.getOutputNodeId() == outNodeId) && (_innov.getOldInnovationNumber() == innovationNumber)) 
				return _innov;
		return null;
	}
	@Override
	public IInnovation getExistingLinkInnovation(int inNodeId, int outNodeId, boolean isRecurrent) {
		for(IInnovation _innov : getInnovations())
			if ((_innov.getInnovationType() == InnovationType.NEW_LINK) && (_innov.getInputNodeId() == inNodeId)
					&& (_innov.getOutputNodeId() == outNodeId) && (_innov.isRecurrent() == isRecurrent)) 
				return _innov;
		return null;
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