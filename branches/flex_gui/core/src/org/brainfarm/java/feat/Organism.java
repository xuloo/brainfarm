package org.brainfarm.java.feat;

import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.ISpecies;

/**
 * Organisms are Genomes and Networks with fitness 
 * information (i.e. The genotype and phenotype 
 * together)
 * 
 * @author dtuohy, orig. Ugo Vierucci
 * @author Trevor Burton [trevor@flashmonkey.org]
 */
public class Organism implements IOrganism {
	/** A measure of fitness for the Organism */
	private double fitness = 0.0010;

	/** A fitness measure that won't change during adjustments */
	private double originalFitness;

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

	/** Number of reserved offspring for a population leader */
	private int superChampOffspring;
	
	public Organism(double xfitness, IGenome xgenome, int xgeneration) {
		fitness = xfitness;
		originalFitness = xfitness;
		genome = xgenome;
		phenotype = genome.generatePhenotype(xgenome.getId());
		species = null;
		expectedOffspring = 0;
		generation = xgeneration;
		eliminated = false;
		superChampOffspring = 0;
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

	public IGenome getGenome() {
		return genome;
	}

	public void setGenome(Genome genome) {
		this.genome = genome;
	}
}