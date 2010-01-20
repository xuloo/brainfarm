package org.brainfarm.java.neat.api;

import java.util.List;

import org.brainfarm.java.neat.ann.NeatNetwork;
import org.brainfarm.java.neat.api.enums.NodeLabel;
import org.brainfarm.java.neat.api.enums.NodeType;

/**
 * This interface must always be implemented by a FEAT 
 * experiment, as it's fields are required by the core
 * algorithm.
 * 
 * @author dtuohy
 *
 */
public interface INode {
	
	NodeType getType();
	
	int getId();
	
	NodeLabel getGenNodeLabel();
	
	void setGenNodeLabel(NodeLabel genNodeLabel);
	
	INode getCachedDuplicate();
	
	INode generateDuplicate();
	
	void setInnerLevel(int innerLevel);
	
	INode getAnalogue();
	
	void setAnalogue(INode analogue);
	
	int getInnerLevel();
	
	List<ILink> getIncoming();
	
	List<ILink> getOutgoing();
	
	int depth(int xlevel, NeatNetwork mynet, int xmax_level);
}
