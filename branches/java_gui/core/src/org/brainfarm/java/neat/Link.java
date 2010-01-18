package org.brainfarm.java.neat;

import org.brainfarm.java.neat.api.ILink;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.ITrait;

/**
 * Link is a connection from one node to another with an associated weight; It
 * can be marked as recurrent; Its parameters are made public for efficiency.
 */
public class Link implements ILink {
	
	/** 
	 * Weight of connection. 
	 */
	private double weight;
	
	/** 
	 * Reference to the input node.
	 */
	private INode inputNode;

	/** 
	 * Reference to the output node. 
	 */
	private INode outputNode;

	/**
	 * Flags whether this Link is recurrent.
	 */
	private boolean recurrent;

	/**
	 * Flags if this Link is tapped (delayed).
	 */
	private boolean timeDelayed;

	/**
	 * Points to a trait of parameters for genetic creation.
	 */
	private ITrait trait;
	
	/**
	 * Used during computeDepth(). Flags whether this Link has already been traversed.
	 */
	private boolean traversed = false;

	/** 
	 * The amount of weight adjustment 
	 */
	private double addedWeight;

	/** 
	 * Link-related parameters that change during Hebbian type learning. 
	 */
	private double[] params = new double[Neat.num_trait_params];

	/**
	 * Insert the method's description here. Creation date: (12/01/2002
	 * 10.41.28)
	 * 
	 * @param trait
	 *            jneat.Trait
	 * @param weight
	 *            double
	 * @param inputNode
	 *            jneat.NNode
	 * @param outputNode
	 *            jneat.NNode
	 * @param recurrent
	 *            boolean
	 */
	public Link(ITrait trait, double weight, INode inputNode, INode outputNode, boolean recurrent) {

		setWeight(weight);
		setInputNode(inputNode);
		setOutputNode(outputNode);
		setRecurrent(recurrent);
		setAddedWeight(0.0);
		setTrait(trait);
		setTimeDelayed(false);
	}

	/**
	 * Insert the method's description here. Creation date: (15/01/2002 7.53.27)
	 * 
	 * @param c
	 *            int
	 */
	public Link(double weight, INode inputNode, INode outputNode, boolean recurrent) {
		setWeight(weight);
		setInputNode(inputNode);
		setOutputNode(outputNode);
		setRecurrent(recurrent);
		setAddedWeight(0.0);
		setTrait(null);
		setTimeDelayed(false);

	}

	/**
	 * Insert the method's description here. Creation date: (15/01/2002 8.05.44)
	 */
	public void deriveTrait(ITrait trait) {
		if (trait != null) {
			for (int count = 0; count < Neat.num_trait_params; count++) {
				params[count] = trait.getParam(count);
			}
		} else {
			for (int count = 0; count < Neat.num_trait_params; count++) {
				params[count] = 0.0;
			}
		}
	}

	public String toString() {
		
		StringBuilder s = new StringBuilder();
		
		s.append("\n +LINK : ");
		s.append("weight=" + getWeight());
		s.append(", weight-add=" + getAddedWeight());
		s.append(", i(" + getInputNode().getId());
		s.append(")--<CONNECTION>--o(");
		s.append(getOutputNode().getId() + ")");
		s.append(", recurrent=" + isRecurrent());
		s.append(", tapped=" + isTimeDelayed());

		if (getTrait() != null) {
			s.append(getTrait().toString());
		} else {
			s.append("\n         *warning* linktrait for this gene is null ");
		}
		
		return s.toString();
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double[] getParams() {
		return params;
	}

	public void setParams(double[] params) {
		this.params = params;
	}

	public ITrait getTrait() {
		return trait;
	}

	public void setTrait(ITrait trait) {
		this.trait = trait;
	}

	public void setInputNode(INode inputNode) {
		this.inputNode = inputNode;
	}

	public INode getInputNode() {
		return inputNode;
	}

	public void setOutputNode(INode outputNode) {
		this.outputNode = outputNode;
	}

	public INode getOutputNode() {
		return outputNode;
	}

	public void setRecurrent(boolean recurrent) {
		this.recurrent = recurrent;
	}

	public boolean isRecurrent() {
		return recurrent;
	}

	public void setTraversed(boolean traversed) {
		this.traversed = traversed;
	}

	public boolean isTraversed() {
		return traversed;
	}

	public void setAddedWeight(double addedWeight) {
		this.addedWeight = addedWeight;
	}

	public double getAddedWeight() {
		return addedWeight;
	}

	public void setTimeDelayed(boolean timeDelayed) {
		this.timeDelayed = timeDelayed;
	}

	public boolean isTimeDelayed() {
		return timeDelayed;
	}
}