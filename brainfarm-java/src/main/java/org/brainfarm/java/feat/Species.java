package org.brainfarm.java.feat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.ISpecies;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.brainfarm.java.feat.comparators.CompareOrganismsByFitness;

/**
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class Species implements ISpecies {

	private static Logger logger = Logger.getLogger(Species.class);

	protected IEvolutionParameters evolutionParameters;

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
	 * how many time from last updt? If this is too long ago, the Species will
	 * goes extinct.
	 */
	private int ageOfLastImprovement;

	/**
	 * list of all organisms in the Species
	 */
	public List<IOrganism> organisms = new ArrayList<IOrganism>();

	/**
	 * 
	 * costructor with identification and flag for signaling if its a new specie
	 * 
	 */
	public Species(int i) {
		id = i;
		age = 1;
		averageFitness = 0.0;
		expectedOffspring = 0;
		novel = true;
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
	 * organisms can be eliminated from this species.
	 */
	public void adjustFitness() {
		
		int age_debt = (age - ageOfLastImprovement + 1) - evolutionParameters.getIntParameter(DROPOFF_AGE);
		if (age_debt == 0)
			age_debt = 1;

		int size1 = organisms.size();
		for (int j = 0; j < size1; j++) {
			IOrganism _organism = organisms.get(j);

			// Remember the original fitness before it gets modified
			_organism.setOriginalFitness(_organism.getFitness());

			// Make fitness decrease after a stagnation point dropoff_age
			// Added an if to keep species pristine until the dropoff point
			if (age_debt >= 1)
				_organism.setFitness(_organism.getFitness() * 0.01);
			
			// Give a fitness boost up to some young age (niching)
			// The age_significance parameter is a system parameter
			// if it is 1, then young species get no fitness boost
			if (age <= 10)
				_organism.setFitness(_organism.getFitness() * evolutionParameters.getDoubleParameter(AGE_SIGNIFICANCE));
			// Do not allow negative fitness
			if (_organism.getFitness() < 0.0)
				_organism.setFitness(0.0001);
			// Share fitness with the species
			_organism.setFitness(_organism.getFitness() / size1);

		}

		// Sort the population and mark for death those after survival_thresh * pop_size
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
		int num_parents = (int) Math.floor((evolutionParameters.getDoubleParameter(SURVIVAL_THRESH) * ((double) size1)) + 1.0);

		// Mark for death those who are ranked too low to be parents
		Iterator<IOrganism> itr_organism = organisms.iterator();
		int count = 1;

		while (itr_organism.hasNext() && count <= num_parents) {
			itr_organism.next();
			count++;
		}

		// found organism can be eliminated !
		while (itr_organism.hasNext()) 
			((IOrganism)itr_organism.next()).setEliminated(true);

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

	@Override
	public void setEvolutionParameters(IEvolutionParameters evolutionParameters) {
		this.evolutionParameters = evolutionParameters;
	}
}