package org.brainfarm.java.neat.api;

import java.util.List;

/**
 * The base interface that any FEAT Network must implement.
 * 
 * @author dtuohy
 *
 */
public interface INetwork {
	
	int getId();
	
	void setId(int id);
	
	String getName();
	
	void setName(String name);
	
	void setGenotype(IGenome genome);
	
	List<INode> getAllNodes();
	
	void setAllNodes(List<INode> allNodes);

	/*****************************************
	 *   Helper methods used by FEAT logic.  *
	 *****************************************/
	
	boolean isRecurrent(INode potin_node, INode potout_node, int level, int thresh);
	
	boolean pathExists(INode potin, INode potout, int level, int threshold);
	
}