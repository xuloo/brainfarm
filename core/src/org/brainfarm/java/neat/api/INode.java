package org.brainfarm.java.neat.api;

import java.util.List;

import org.brainfarm.java.neat.Network;
import org.brainfarm.java.neat.api.enums.ActivationFunction;
import org.brainfarm.java.neat.api.enums.NodeLabel;
import org.brainfarm.java.neat.api.enums.NodeType;

public interface INode {

	ITrait getTrait();
	
	void setTrait(ITrait trait);
	
	NodeType getType();
	
	int getId();
	
	NodeLabel getGenNodeLabel();
	
	void setGenNodeLabel(NodeLabel genNodeLabel);
	
	INode getDuplicate();
	
	void setDuplicate(INode node);
	
	void deriveTrait(ITrait trait);
	
	void setInnerLevel(int innerLevel);

	//int getInnerLevel();
	
	boolean isTraversed();
	void setTraversed(boolean traversed);
	
	INode getAnalogue();
	void setAnalogue(INode analogue);
	
	int getInnerLevel();
	
	List<ILink> getIncoming();
	
	List<ILink> getOutgoing();
	
	double getActiveSum();
	void setActiveSum(double activeSum);
	
	void incrementActiveSum(double value);
	
	void setActiveFlag(boolean activeFlag);
	boolean getActiveFlag();
	
	double getActiveOut();
	
	double getActiveOutTimeDelayed();
	
	void resetNNode();
	
	boolean mark(int xlevel, INetwork mynet);
	
	int depth(int xlevel, Network mynet, int xmax_level);
}
