package org.brainfarm.java.feat.comparators;

import org.brainfarm.java.feat.api.IOrganism;

public class CompareOrganismsByFitness implements java.util.Comparator<IOrganism> {
	/**
	 * order_orgs constructor comment.
	 */
	public CompareOrganismsByFitness() {
		// super();
	}

	/**
	*/
	public int compare(IOrganism o1, IOrganism o2) {

		if (o1.getFitness() < o2.getFitness()) {
			return 1;
		}
		
		if (o1.getFitness() > o2.getFitness()) {
			return -1;
		}
		
		return 0;
	}
}