package org.brainfarm.java.neat;

import java.text.DecimalFormat;

import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.ILink;
import org.brainfarm.java.neat.api.INode;

/** 
 * A genetic codification of gene. 
 */
public class Gene implements IGene
{
	/** 
	 * Reference to object for identify input/output node and features. 
	 */
	private ILink link;

	/** 
	 * Historical marking of node.
	 */
	private double innovationNumber;

	/** 
	 * How much mutation has changed the link.
	 */
	private double mutationNumber;

	/** 
	 * TRUE the gene is enabled. 
	 */
	private boolean enabled;

	/**
	 * Empty Constructor.
	 */
	public Gene() {
	}
	
	/**
	 * Creates a new Gene from references to the Trait and Input/Output Nodes.
	 * 
	 * @param gene
	 * @param inputNode
	 * @param outputNode
	 */
	public Gene(IGene gene, INode inputNode, INode outputNode) {
		
		// Create a new Link.
		link = new Link(gene.getLink().getWeight(), inputNode, outputNode, gene.getLink().isRecurrent());
		
		// Copy the supplied gene's properties.
		setInnovationNumber(gene.getInnovationNumber());
		setMutationNumber(gene.getMutationNumber());
		setEnabled(gene.isEnabled());
	}
	
	/**
	 * Creates a new Gene from the supplied Input/Output nodes and gene properties.
	 * 
	 * @param weight
	 * @param inputNode
	 * @param outputNode
	 * @param recurrent
	 * @param innovationNumber
	 * @param mutationNumber
	 */
	public Gene(double weight, INode inputNode, INode outputNode, boolean recurrent, double innovationNumber, double mutationNumber) {
		
		// Create the Link.
		link = new Link(weight, inputNode, outputNode, recurrent);
		
		// Set the gene properties.
		setInnovationNumber(innovationNumber);
		setMutationNumber(mutationNumber);
		setEnabled(true);
	}

	public String toString() {

		StringBuilder s = new StringBuilder();
		
		String mask03 = " 0.000;-0.000";
		DecimalFormat fmt03 = new DecimalFormat(mask03);

		String mask5 = " 0000";
		DecimalFormat fmt5 = new DecimalFormat(mask5);

		s.append("\n [Link (" + fmt5.format(link.getInputNode().getId()));
		s.append("," + fmt5.format(link.getOutputNode().getId()));
		s.append("]  innov (" + fmt5.format(getInnovationNumber()));

		s.append(", mut=" + fmt03.format(getMutationNumber()) + ")");
		s.append(" Weight " + fmt03.format(link.getWeight()));

		if (isEnabled() == false)
			s.append(" -DISABLED-");

		if (link.isRecurrent())
			s.append(" -RECUR-");
		
		return s.toString();
	}
	
	public ILink getLink() {
		return link;
	}

	public void setLink(ILink link) {
		this.link = link;
	}

	public void setInnovationNumber(double innovationNumber) {
		this.innovationNumber = innovationNumber;
	}

	public double getInnovationNumber() {
		return innovationNumber;
	}

	public void setMutationNumber(double mutationNumber) {
		this.mutationNumber = mutationNumber;
	}

	public double getMutationNumber() {
		return mutationNumber;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
}