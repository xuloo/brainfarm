package org.brainfarm.java.feat.api;

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
	
	INode getCachedDuplicate();
	
	INode generateDuplicate();
	
	/***************************************************
	 *   Helper methods used by FEAT during mutation   *
	 *            and phenotype generation.            *
	 ***************************************************/
	
	INode getAnalogue();
	
	void setAnalogue(INode analogue);
	
	/**********************************************************
	 *   Helper methods used by FEAT during graph traversal.  *
	 **********************************************************/
	
	boolean isTraversed();
	
	void setTraversed(boolean traversed);
	
	int getInnerLevel();
	
	void setInnerLevel(int innerLevel);
	
	int depth(int xlevel, INetwork mynet, int xmax_level);
	
	boolean validate();
}