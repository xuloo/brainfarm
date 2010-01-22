package org.brainfarm.java.neat.comparators;

import org.brainfarm.java.neat.api.IOrganism;
import org.brainfarm.java.neat.api.ISpecies;


public class CompareSpeciesByOriginalFitness implements java.util.Comparator<ISpecies> {
	/**
	 * order_species constructor comment.
	 */
	public CompareSpeciesByOriginalFitness() {
		//super();
	}
	/**
	 */
	public int compare(ISpecies o1, ISpecies o2) {

		IOrganism _ox = o1.getOrganisms().get(0);
		IOrganism _oy = o2.getOrganisms().get(0);

		if (_ox.getOriginalFitness() < _oy.getOriginalFitness()) {
			return 1;
		}

		if (_ox.getOriginalFitness() > _oy.getOriginalFitness()) {
			return -1;
		}

		return 0;

	}
}