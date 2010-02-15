package org.brainfarm.java.feat;

import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.feat.api.ILink;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;

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
	 * Is a reference to a Node ; Has used for generate and point from a genetic
	 * node (genotype) to a real node (phenotype) during 'generatePhenotype' process
	 * 
	 */
	private transient INode analogue;
	
	/**
	 * Is a temporary reference to a Node ; Has used for generate a new genome
	 * during duplicate phase of genotype.
	 *
	 */
	private transient INode duplicate;
	
	/** Used fleetingly by network traversing algorithms */
	private transient boolean traversed = false;
	
	/** Used fleetingly by network traversing algorithms */
	private transient int innerLevel = 0;
	
	public Node(){}
	
	/**
	 * Default constructor.
	 */
	public Node(INode n) {
		setId(n.getId());
	}
	
	public Node(int id) {
		setId(id);
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
	
	public INode getAnalogue() {
		return analogue;
	}

	public void setAnalogue(INode analogue) {
		this.analogue = analogue;
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
		INode newnode = EvolutionStrategy.getInstance().getModelObjectFactory().createOffspringNodeFrom(this);
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
	
	/**
	 * No validation is currently performed for
	 * the basic Node.
	 */
	public boolean validate(){
		return true;
	}
}