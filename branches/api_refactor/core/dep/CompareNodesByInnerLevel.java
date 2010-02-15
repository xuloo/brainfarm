package org.brainfarm.java.feat.comparators;

import org.brainfarm.java.feat.api.ann.INeatNode;

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