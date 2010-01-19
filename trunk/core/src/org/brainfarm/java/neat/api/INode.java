package org.brainfarm.java.neat.api;

import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.neat.Network;
import org.brainfarm.java.neat.api.enums.NodeLabel;
import org.brainfarm.java.neat.api.enums.NodeType;

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
	
	int depth(int xlevel, Network mynet, int xmax_level);
}
