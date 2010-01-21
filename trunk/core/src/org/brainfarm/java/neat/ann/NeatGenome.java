package org.brainfarm.java.neat.ann;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.brainfarm.java.neat.EvolutionStrategy;
import org.brainfarm.java.neat.Gene;
import org.brainfarm.java.neat.Genome;
import org.brainfarm.java.neat.Link;
import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.ILink;
import org.brainfarm.java.neat.api.INetwork;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.ann.INeatNetwork;
import org.brainfarm.java.neat.api.ann.INeatNode;
import org.brainfarm.java.neat.api.enums.NodeLabel;
import org.brainfarm.java.neat.api.enums.NodeType;
import org.brainfarm.java.util.RandomUtils;

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

	/**
	 * 
	 * Creation of a new random genome with : new_id = numerical identification
	 * of genome i = number of input nodes o = number of output nodes n = number
	 * of hidden nodes nmax = number max of node this number must be >= (i + n +
	 * o) r = the network can have a nodes recurrent ? linkprob = probability of
	 * a link from nodes ( must be in interval ]0,1[);
	 */
	public NeatGenome(int new_id, int i, int o, int n, int nmax, boolean r, double linkprob) {
		int totalnodes = 0;
		int matrixdim = 0;
		int maxnode = 0;
		int first_output = 0;
		int count = 0;
		int ccount = 0;
		int innov_number = 0;
		int col = 0;
		int row = 0;
		int fnd = 0;

		boolean flag_recurrent = false;
		boolean create_gene = false;

		double new_weight = 0.0;

		INode newnode = null;
		INode in_node = null;
		INode out_node = null;
		IGene newgene = null;

		//
		// i i i n n n n n n n n n n n n n n n n . . . . . . . . o o o o
		// | | ^ |
		// |<----------- maxnode ------------->| | |
		// | | |
		// |<-----------------------total nodes -----------------|---->|
		// |
		// |
		// first output ----------------------------------------+
		//
		//
		totalnodes = i + o + nmax;

		setNodes(new ArrayList<INode>(totalnodes));
		setGenes(new ArrayList<IGene>(totalnodes));

		matrixdim = totalnodes * totalnodes;

		boolean[] cm = new boolean[matrixdim]; // Dimension the connection
												// matrix
		boolean[] cmp;

		maxnode = i + n;
		first_output = totalnodes - o + 1;

		// Assign the id
		setId(new_id);

		// Build the input nodes
		for (count = 1; count <= i; count++) {
			if (count < i)
				newnode = new NeatNode(NodeType.SENSOR, count, NodeLabel.INPUT);
			else
				newnode = new NeatNode(NodeType.SENSOR, count, NodeLabel.BIAS);

			// Add the node to the list of nodes
			getNodes().add(newnode);
		}

		// Build the hidden nodes
		for (count = i + 1; count <= i + n; count++) {
			newnode = new NeatNode(NodeType.NEURON, count, NodeLabel.HIDDEN);

			// Add the node to the list of nodes
			getNodes().add(newnode);
		}

		// Build the output nodes
		for (count = first_output; count <= totalnodes; count++) {
			newnode = new NeatNode(NodeType.NEURON, count, NodeLabel.OUTPUT);

			// Add the node to the list of nodes
			getNodes().add(newnode);
		}

		boolean done = false;
		boolean rc1 = false;
		boolean rc2 = false;
		//boolean rc3 = false;

		//int min_required = i * o;
		double forced_probability = 0.5;
		int abort = 0;

		while (!done) {

			abort++;
			if (abort >= 20) {
				// if (abort == 10)
				// System.out.print("\n ALERT  force new probability from 0.5 to 1 step .01");
				linkprob = forced_probability;
				forced_probability += .01;
			}

			if (abort >= 700) {
				System.out
						.print("\n SEVERE ERROR in genome random creation costructor : genome has not created");
				System.exit(12);
			}

			//
			// creation of connections matrix
			// Step through the connection matrix, randomly assigning bits
			//

			cmp = cm;
			ccount = 0;
			for (count = 0; count < matrixdim; count++) {
				if (RandomUtils.randomDouble() < linkprob) {
					ccount++;
					cmp[count] = true;
				} else
					cmp[count] = false;
			}

			// Connect the nodes

			innov_number = 0; // counter for labelling the innov_num of genes
			//gene_number = 0; // counter gene created

			// Step through the connection matrix, creating connection genes

			cmp = cm;

			for (col = 1; col <= totalnodes; col++) {
				for (row = 1; row <= totalnodes; row++) {

					if ((cmp[innov_number] && (col > i))
							&& ((col <= maxnode) || (col >= first_output))
							&& ((row <= maxnode) || (row >= first_output))) {
						// If it isn't recurrent, create the connection no
						// matter what

						create_gene = true;
						if (col > row)
							flag_recurrent = false;
						else {
							if (!r)
								create_gene = false;
							flag_recurrent = true;
						}

						if (create_gene) {
							Iterator<INode> nodeIterator = getNodes().iterator(); // Retrieve the in_node
															// , out_node
							fnd = 0;
							while (nodeIterator.hasNext() && (fnd < 2)) {
								INode _node = nodeIterator.next();
								if (_node.getId() == row) {
									fnd++;
									in_node = _node;
								}
								if (_node.getId() == col) {
									fnd++;
									out_node = _node;
								}
							}
							
							// Create the gene + link
							new_weight = RandomUtils.randomBinomial() * RandomUtils.randomDouble();
							newgene = new Gene(new_weight, in_node, out_node, flag_recurrent, innov_number, new_weight);
							
							// Add the gene to the genome
							getGenes().add(newgene);
						}
					} // end condition for a correct link in genome
					innov_number++;
				}
			}

			rc1 = verify();
			if (rc1) {
				INeatNetwork net = (INeatNetwork)generatePhenotype(getId());
				rc2 = net.isMinimal();
				if (rc2) {
					int lx = net.maxDepth();
					int dx = net.isStabilised(lx);

					if (((dx == lx) && (!r)) || ((lx > 0) && (r) && (dx == 0)))
						done = true;

				}
				net.setGenotype(null);
				setPhenotype(null);
			}
			if (!done)
				getGenes().clear();
		}

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
