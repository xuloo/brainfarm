package org.brainfarm.java.neat;

import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.neat.ann.NeatNode;
import org.brainfarm.java.neat.api.ILink;
import org.brainfarm.java.neat.api.INetwork;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.ann.INeatNode;
import org.brainfarm.java.neat.api.enums.ActivationFunction;

/**
 * The base Node implementation that implements
 * the bare essentials required for a FEAT experiment.
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

	/**
	 * Is a temporary reference to a Node ; Has used for generate a new genome
	 * during duplicate phase of genotype.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 */
	private INode duplicate;
	
	/** used fleetingly by network traversing algorithms */
	private boolean traversed = false;
	private int innerLevel = 0;
	
	public Node(){}
	
	/**
	 * Default constructor.
	 */
	public Node(INode n) {
		setId(n.getId());
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
	
	/*****************************************
	 *   Helper methods used by FEAT logic.  *
	 *****************************************/
	
	public void setTraversed(boolean traversed) {
		this.traversed = traversed;
	}

	public boolean isTraversed() {
		return traversed;
	}
	
	public void setInnerLevel(int innerLevel) {
		this.innerLevel = innerLevel;
	}

	public int getInnerLevel() {
		return innerLevel;
	}
	
	protected void setDuplicate(INode duplicate) {
		this.duplicate = duplicate;
	}
	
	public INode generateDuplicate(){
		INode newnode = EvolutionStrategy.getInstance().getOffspringFactory().createOffspringNodeFrom(this);
		setDuplicate(newnode);
		return newnode;
	}

	public INode getCachedDuplicate() {
		return duplicate;
	}
	
	public int depth(int xlevel, INetwork mynet, int xmax_level) {

		// control for loop
		if (xlevel > 100) {
			System.out.print("\n ** DEPTH NOT DETERMINED FOR NETWORK WITH LOOP ");
			return 10;
		}

		// Base Case
		if (getIncoming().size()==0) 
			return xlevel;

		xlevel++;

		// recursion case
		//itr_link = this.getIncoming().iterator();
		int cur_depth = 0; // The depth of the current node

		for (ILink _link : getIncoming()) {
			INode _ynode = _link.getInputNode();

			if (!_ynode.isTraversed()) {
				_ynode.setTraversed(true);
				cur_depth = _ynode.depth(xlevel, mynet, xmax_level);
				_ynode.setInnerLevel(cur_depth - xlevel);
			} else
				cur_depth = xlevel + _ynode.getInnerLevel();

			if (cur_depth > xmax_level)
				xmax_level = cur_depth;
		}
		return xmax_level;

	}
}