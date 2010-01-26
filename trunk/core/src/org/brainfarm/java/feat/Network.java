package org.brainfarm.java.feat;

import java.util.List;

import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.ILink;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;

/**
 * This is the base implementation of Network, it contains
 * the bare essentials required for a FEAT experiment.
 * 
 * @author dtuohy
 *
 */
public class Network implements INetwork {

	/** Is a name of this network */
	private String name;

	/** Numeric identification of this network */
	private int id;
	
	/**
	 * Is a collection of object NNode can be mapped in a Vector container; this
	 * collection represent a group of references to all nodes of this net;
	 */
	private List<INode> allNodes;
	
	/** is a reference to genotype can has originate this phenotype */
	private IGenome genotype;
	
	public Network(){}
	
	public Network(List<INode> allList, int id) {
		setAllNodes(allList);
		setId(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public List<INode> getAllNodes() {
		return allNodes;
	}

	public void setAllNodes(List<INode> allnodes) {
		this.allNodes = allnodes;
	}
	
	public IGenome getGenotype() {
		return genotype;
	}

	public void setGenotype(IGenome genotype) {
		this.genotype = genotype;
	}
	
	/*****************************************
	 *   Helper methods used by FEAT logic.  *
	 *****************************************/	

	public boolean pathExists(INode potin, INode potout, int level, int threshold) {

		// reset all link to state no traversed
		//for (int j = 0; j < allnodes.size(); j++) {
		for (INode node : getAllNodes())
			node.setTraversed(false);

		// call the control if has a link intra node potin , potout
		return isRecurrent(potin, potout, level, threshold);
	}
	
	public boolean isRecurrent(INode potin_node, INode potout_node, int level, int thresh) {
		level++;

		if (level > thresh)
			return false;

		if (potin_node == potout_node)
			return true;

		else {
			for (ILink _link : potin_node.getIncoming()) {
				if (!_link.isRecurrent()) {

					if (!_link.getInputNode().isTraversed()) {
						_link.getInputNode().setTraversed(true);
						if (isRecurrent(_link.getInputNode(), potout_node, level, thresh))
							return true;
					}
				}
			}
			potin_node.setTraversed(true);
			return false;
		}
	}
}