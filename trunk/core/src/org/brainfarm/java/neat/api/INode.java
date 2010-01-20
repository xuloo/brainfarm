package org.brainfarm.java.neat.api;

import java.util.List;

/**
 * This interface must always be implemented by a FEAT 
 * experiment, as it's fields are required by the core
 * algorithm.
 * 
 * @author dtuohy
 *
 */
public interface INode {
	
	int getId();
		
	List<ILink> getIncoming();
	
	List<ILink> getOutgoing();
	
	/*****************************************
	 *   Helper methods used by FEAT logic.  *
	 *****************************************/
	
	boolean isTraversed();
	
	void setTraversed(boolean traversed);
	
}
