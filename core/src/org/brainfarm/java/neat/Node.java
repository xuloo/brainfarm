package org.brainfarm.java.neat;

import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.neat.ann.NeatNetwork;
import org.brainfarm.java.neat.api.ILink;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.enums.NodeLabel;
import org.brainfarm.java.neat.api.enums.NodeType;

public class Node implements INode {

	/** A list of pointers to incoming weighted signals from other nodes */
	private List<ILink> incoming = new ArrayList<ILink>();

	/** A list of pointers to links carrying this node's signal */
	private List<ILink> outgoing = new ArrayList<ILink>();
	
	/** Numeric identification of node */
	private int id;
	
	@Override
	public int depth(int xlevel, NeatNetwork mynet, int xmaxLevel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public INode generateDuplicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INode getAnalogue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INode getCachedDuplicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeLabel getGenNodeLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getInnerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NodeType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAnalogue(INode analogue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGenNodeLabel(NodeLabel genNodeLabel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInnerLevel(int innerLevel) {
		// TODO Auto-generated method stub

	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public List<ILink> getIncoming() {
		return incoming;
	}

	public void setIncoming(List<ILink> incoming) {
		this.incoming = incoming;
	}

	public List<ILink> getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(List<ILink> outgoing) {
		this.outgoing = outgoing;
	}

}
