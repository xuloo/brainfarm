package org.brainfarm.java.neat;

import java.util.List;

import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.INetwork;
import org.brainfarm.java.neat.api.INode;

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
	
	@Override
	public int maxDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean pathExists(INode potin, INode potout, int level,
			int threshold) {
		// TODO Auto-generated method stub
		return false;
	}
}