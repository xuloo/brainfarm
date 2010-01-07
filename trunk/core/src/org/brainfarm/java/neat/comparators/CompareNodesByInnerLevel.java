package org.brainfarm.java.neat.comparators;

import org.brainfarm.java.neat.api.INode;

/**
 * 
 * 
 * 
 */
public class CompareNodesByInnerLevel implements java.util.Comparator<INode> {
	/**
	 * order_inner constructor comment.
	 */
	public CompareNodesByInnerLevel() {
		super();
	}

	public int compare(INode o1, INode o2) {
		
		if (o1.getInnerLevel() < o2.getInnerLevel()) {
			return -1;
		}
		
		if (o1.getInnerLevel() > o2.getInnerLevel()) {
			return 1;
		}
		
		return 0;
	}
}