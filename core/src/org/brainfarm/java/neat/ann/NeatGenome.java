package org.brainfarm.java.neat.ann;

import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.neat.Gene;
import org.brainfarm.java.neat.Genome;
import org.brainfarm.java.neat.Link;
import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.ILink;
import org.brainfarm.java.neat.api.INetwork;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.ann.INeatNode;
import org.brainfarm.java.neat.api.enums.NodeLabel;

/**
 * This class provides functionality specifically
 * required by NEAT experiments.
 * 
 * @author dtuohy
 *
 */
public class NeatGenome extends Genome {

	public NeatGenome(){}
	
	public NeatGenome(int newId, List<INode> nodes,
			List<IGene> genes) {
		super(newId,nodes,genes);
	}

	public NeatGenome(int count, int numberOfInputs, int numberOfOutputs,
			int randomInt, int maxIndexOfNodes, boolean recurrent,
			double linkProbability) {
		super(count,numberOfInputs,numberOfOutputs,randomInt,maxIndexOfNodes,recurrent,linkProbability);
	}
	
	@Override
	/**
	 * Generates a Network (Phenotype) from this Genome (Genotype).
	 * 
	 * @param id
	 * @return
	 */
	public INetwork generatePhenotype(int id) {

		INetwork newnet = null;
		INeatNode newnode = null;
		List<INode> inlist = new ArrayList<INode>(1);
		List<INode> outlist = new ArrayList<INode>(1);
		List<INode> all_list = new ArrayList<INode>(getNodes().size());

		ILink curlink = null;
		ILink newlink = null;
		INode inode = null;
		INode onode = null;
		
		for (INode _inode : getNodes()) {
			INeatNode _node = (INeatNode)_inode;
			
			// create a copy of gene node for phenotype.
			newnode = new NeatNode(_node.getType(), _node.getId());

			newnode.setGenNodeLabel(_node.getGenNodeLabel());
			
			if (_node.getGenNodeLabel() == NodeLabel.INPUT)
				inlist.add(newnode);
			if (_node.getGenNodeLabel() == NodeLabel.BIAS)
				inlist.add(newnode);
			if (_node.getGenNodeLabel() == NodeLabel.OUTPUT)
				outlist.add(newnode);

			// add to genotype the pointer to phenotype node
			all_list.add(newnode);
			_node.setAnalogue(newnode);
		}

		if (getGenes().size() == 0) 
			logger.error("Network has no GENES - results will be unpredictable");

		if (outlist.size() == 0) {
			logger.error("Network has no OUTPUTS - results will be unpredictable");
			logger.debug(toString());
		}

		for (IGene _gene : getGenes()) {
			
			// Only create the link if the gene is enabled
			if (_gene.isEnabled()) {

				curlink = _gene.getLink();

				inode = ((INeatNode)curlink.getInputNode()).getAnalogue();
				onode = ((INeatNode)curlink.getOutputNode()).getAnalogue();
				// NOTE: This line could be run through a recurrency check if desired
				// (no need to in the current implementation of NEAT)
				newlink = new Link(curlink.getWeight(), inode, onode, curlink.isRecurrent());
				onode.getIncoming().add(newlink);
				inode.getOutgoing().add(newlink);
			}

		}
		
		// Create the new network
		newnet = new NeatNetwork(inlist, outlist, all_list, id);
		// Attach genotype and phenotype together:
		// newnet point to owner genotype (this)
		newnet.setGenotype(this);
		// genotype point to owner phenotype (newnet)

		setPhenotype(newnet);
		
		return newnet;
	}

	@Override
	public Genome duplicate(int new_id) {		
		
		ArrayList<INode> nodes_dup = new ArrayList<INode>(getNodes().size());
		ArrayList<IGene> genes_dup = new ArrayList<IGene>(getGenes().size());

		// Duplicate Nodes.
		for (INode _node : getNodes()) {
			INode newnode = ((INeatNode)_node).generateDuplicate();
			nodes_dup.add(newnode);
		}

		// Duplicate Genes.
		for (IGene gene : getGenes()) {
			// point to news nodes created at precedent step
			INode inode = ((INeatNode)gene.getLink().getInputNode()).getCachedDuplicate();
			INode onode = ((INeatNode)gene.getLink().getOutputNode()).getCachedDuplicate();

			// creation of new gene with a pointer to new node
			genes_dup.add(new Gene(gene, inode, onode));
		}

		// okay all nodes created, the new genome can be generate
		return new NeatGenome(new_id, nodes_dup, genes_dup);
	}

	public String toString() {

		StringBuilder s = new StringBuilder();
		
		s.append("GENOME START   id=" + getId());
		s.append("\n  genes are :" + getGenes().size());
		s.append("\n  nodes are :" + getNodes().size());

		for (INode _inode : getNodes()) {
			INeatNode _node = (INeatNode)_inode;
			if (_node.getGenNodeLabel() == NodeLabel.INPUT)
				s.append("\n Input ");
			if (_node.getGenNodeLabel() == NodeLabel.OUTPUT)
				s.append("\n Output");
			if (_node.getGenNodeLabel() == NodeLabel.HIDDEN)
				s.append("\n Hidden");
			if (_node.getGenNodeLabel() == NodeLabel.BIAS)
				s.append("\n Bias  ");
			s.append(_node.toString());
		}

		for (IGene _gene : getGenes()) {
			s.append(_gene.toString());
		}
		
		return s.toString();
	}	
}
