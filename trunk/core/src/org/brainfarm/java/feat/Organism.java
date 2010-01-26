package org.brainfarm.java.feat;

import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.ISpecies;

/**
 * Organisms are Genomes and Networks with fitness information i.e. The genotype
 * and phenotype together
 */
public class Organism implements IOrganism {
	/** A measure of fitness for the Organism */
	private double fitness = 0.0010;

	/** A fitness measure that won't change during adjustments */
	private double originalFitness;

	/** Win marker (if needed for a particular task) */
	private boolean winner;

	/** The Organism's phenotype */
	private INetwork phenotype;

	/** The Organism's genotype */
	private IGenome genome;

	/** The Organism's Species */
	private ISpecies species;

	/** Number of children this Organism may have */
	private double expectedOffspring;

	/** Tells which generation this Organism is from */
	private int generation;

	/** Marker for destruction of inferior Organisms */
	private boolean eliminated;

	/** Marks the species champ */
	private boolean champion;

	/** Number of reserved offspring for a population leader */
	private int superChampOffspring;

	/** Marks the best in population */
	private boolean populationChampion;

	/** Marks the duplicate child of a champion (for tracking purposes) */
	private boolean populationChampionChild;

	/** DEBUG variable- high fitness of champ */
	private double highestFitness;

	/** has a change in a structure of baby ? */
	private boolean mutStructBaby;

	/** has a mating in baby ? */
	private boolean mateBaby;
	
	public Organism(double xfitness, IGenome xgenome, int xgeneration) {
		fitness = xfitness;
		originalFitness = xfitness;
		genome = xgenome;
		phenotype = genome.generatePhenotype(xgenome.getId());
		species = null;
		expectedOffspring = 0;
		generation = xgeneration;
		eliminated = false;
		winner = false;
		champion = false;
		superChampOffspring = 0;
		populationChampion = false;
		populationChampionChild = false;
		highestFitness = 0;
		mutStructBaby = false;
		mateBaby = false;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public double getOriginalFitness() {
		return originalFitness;
	}

	public void setOriginalFitness(double originalFitness) {
		this.originalFitness = originalFitness;
	}

	public boolean isWinner() {
		return winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}

	public INetwork getPhenotype() {
		return phenotype;
	}

	public void setPhenotype(INetwork phenotype) {
		this.phenotype = phenotype;
	}

	public ISpecies getSpecies() {
		return species;
	}

	public void setSpecies(ISpecies species) {
		this.species = species;
	}

	public double getExpectedOffspring() {
		return expectedOffspring;
	}

	public void setExpectedOffspring(double expectedOffspring) {
		this.expectedOffspring = expectedOffspring;
	}

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}

	public boolean isEliminated() {
		return eliminated;
	}

	public void setEliminated(boolean eliminated) {
		this.eliminated = eliminated;
	}

	public boolean getChampion() {
		return champion;
	}

	public void setChampion(boolean champion) {
		this.champion = champion;
	}

	public int getSuperChampOffspring() {
		return superChampOffspring;
	}

	public void setSuperChampOffspring(int superChampOffspring) {
		this.superChampOffspring = superChampOffspring;
	}
	
	public void incrementSuperChampOffspring() {
		superChampOffspring++;
	}
	
	public void incrementSuperChampOffspring(int value) {
		superChampOffspring += value;
	}

	public boolean isPopulationChampion() {
		return populationChampion;
	}

	public void setPopulationChampion(boolean populationChampion) {
		this.populationChampion = populationChampion;
	}

	public boolean isPopulationChampionChild() {
		return populationChampionChild;
	}

	public void setPopulationChampionChild(boolean populationChampionChild) {
		this.populationChampionChild = populationChampionChild;
	}

	public double getHighestFitness() {
		return highestFitness;
	}

	public void setHighestFitness(double highFitness) {
		this.highestFitness = highFitness;
	}

	public boolean getMutStructBaby() {
		return mutStructBaby;
	}

	public void setMutStructBaby(boolean mutStructBaby) {
		this.mutStructBaby = mutStructBaby;
	}

	public boolean getMateBaby() {
		return mateBaby;
	}

	public void setMateBaby(boolean mateBaby) {
		this.mateBaby = mateBaby;
	}

	public IGenome getGenome() {
		return genome;
	}

	public void setGenome(Genome genome) {
		this.genome = genome;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		
		s.append("-ORGANISM -[genomew_id=" + genome.getId() + "]");
		s.append("Champ(" + champion + ")");
		s.append(", fit=" + fitness);
		s.append(", Elim=" + eliminated);
		s.append(", offspring=" + expectedOffspring);
		
		return s.toString();
	}
}