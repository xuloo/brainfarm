package org.brainfarm.java.neat.comparators;

import org.brainfarm.java.neat.api.ann.INeatNode;

public class CompareNodesByInnerLevel implements java.util.Comparator<INeatNode> {
	/**
	 * order_inner constructor comment.
	 */
	public CompareNodesByInnerLevel() {
		super();
	}

	public int compare(INeatNode o1, INeatNode o2) {
		
		if (o1.getInnerLevel() < o2.getInnerLevel()) {
			return -1;
		}
		
		if (o1.getInnerLevel() > o2.getInnerLevel()) {
			return 1;
		}
		
		return 0;
	}
}