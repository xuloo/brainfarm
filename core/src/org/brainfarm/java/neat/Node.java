package org.brainfarm.java.neat;

import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.neat.api.ILink;
import org.brainfarm.java.neat.api.INode;

/**
 * Must be implemented by *all* nodes to be used in a 
 * FEAT experiment.
 * 
 * @author dtuohy
 *
 */
public class Node implements INode {

	/** A list of pointers to incoming weighted signals from other nodes */
	private List<ILink> incoming = new ArrayList<ILink>();

	/** A list of pointers to links carrying this node's signal */
	private List<ILink> outgoing = new ArrayList<ILink>();
	
	/** Numeric identification of node */
	private int id;
	
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
