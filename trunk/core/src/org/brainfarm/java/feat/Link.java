package org.brainfarm.java.feat;

import org.brainfarm.java.feat.api.ILink;
import org.brainfarm.java.feat.api.INode;

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
	 * Used during computeDepth(). Flags whether this Link has already been traversed.
	 */
	private boolean traversed = false;

	/** 
	 * The amount of weight adjustment 
	 */
	private double addedWeight;

	/**
	 * Insert the method's description here. Creation date: (12/01/2002
	 * 10.41.28)
	 * 
	 * @param weight
	 *            double
	 * @param inputNode
	 *            jneat.NNode
	 * @param outputNode
	 *            jneat.NNode
	 * @param recurrent
	 *            boolean
	 */
	public Link(double weight, INode inputNode, INode outputNode, boolean recurrent) {
		setWeight(weight);
		setInputNode(inputNode);
		setOutputNode(outputNode);
		setRecurrent(recurrent);
		setAddedWeight(0.0);
		setTimeDelayed(false);
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

		return s.toString();
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
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