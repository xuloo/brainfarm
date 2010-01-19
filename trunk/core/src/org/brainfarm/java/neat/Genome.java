package org.brainfarm.java.neat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.ILink;
import org.brainfarm.java.neat.api.INetwork;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.ITrait;
import org.brainfarm.java.neat.api.enums.NodeLabel;
import org.brainfarm.java.neat.api.enums.NodeType;
import org.brainfarm.java.util.RandomUtils;

public class Genome implements IGenome {

	private static Logger logger = Logger.getLogger(Genome.class);
	
	/** Is a reference from this genotype to phenotype */
	private INetwork phenotype;

	/** Numeric identification for this genotype */
	private int id;

	/**
	 * Each Gene in (3) has a marker telling when it arose historically; Thus,
	 * these Genes can be used to speciate the population, and the list of Genes
	 * provide an evolutionary history of innovation and link-building
	 */
	private List<IGene> genes;

	/** Is a collection of NNode mapped in a Vector; */
	private List<INode> nodes;

	/**
	 * note are two String for store statistics information when genomes are
	 * readed (if exist : null otherwise);
	 */
	//private String notes;

	public List<IGene> getGenes() {
		return genes;
	}

	public void setGenes(List<IGene> genes) {
		this.genes = genes;
	}

	public List<ITrait> getTraits() {
		return null;
	}

	public void setTraits(List<ITrait> traits) {
	}

	public List<INode> getNodes() {
		return nodes;
	}

	public void setNodes(List<INode> nodes) {
		this.nodes = nodes;
	}

	public INetwork getPhenotype() {
		return phenotype;
	}

	public void setPhenotype(INetwork phenotype) {
		this.phenotype = phenotype;
	}
	
	/**
	 * Empty Constructor.
	 */
	public Genome() {
		
	}
	
	/**
	 * Creates a new Genome using the supplied traits, nodes and genes and with the supplied id.
	 * 
	 * @param id
	 * @param traits
	 * @param nodes
	 * @param genes
	 */
	public Genome(int id, List<INode> nodes, List<IGene> genes) {
		this.id = id;
		this.nodes = nodes;
		this.genes = genes;
	}

	public Genome duplicate(int new_id) {		
		
		ArrayList<INode> nodes_dup = new ArrayList<INode>(nodes.size());
		ArrayList<IGene> genes_dup = new ArrayList<IGene>(genes.size());

		// Duplicate Nodes.
		for (INode _node : nodes) {
			INode newnode = _node.generateDuplicate();
			nodes_dup.add(newnode);
		}

		// Duplicate Genes.
		for (IGene gene : genes) {
			// point to news nodes created at precedent step
			INode inode = gene.getLink().getInputNode().getCachedDuplicate();
			INode onode = gene.getLink().getOutputNode().getCachedDuplicate();

			// creation of new gene with a pointer to new node
			genes_dup.add(new Gene(gene, inode, onode));
		}

		// okay all nodes created, the new genome can be generate
		return new Genome(new_id, nodes_dup, genes_dup);
	}

	/**
	 * Generates a Network (Phenotype) from this Genome (Genotype).
	 * 
	 * @param id
	 * @return
	 */
	public INetwork genesis(int id) {

		INetwork newnet = null;
		// Vector nodes_dup = new Vector(1, 0);
		INode newnode = null;
		List<INode> inlist = new ArrayList<INode>(1);
		List<INode> outlist = new ArrayList<INode>(1);
		List<INode> all_list = new ArrayList<INode>(nodes.size());

		ILink curlink = null;
		ILink newlink = null;
		INode inode = null;
		INode onode = null;
		
		for (INode _node : nodes) {
			// create a copy of gene node for phenotype.
			newnode = new Node(_node.getType(), _node.getId());

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

		if (genes.size() == 0) {
			logger.error("Network has no GENES - results will be unpredictable");
		}

		if (outlist.size() == 0) {
			logger.error("Network has no OUTPUTS - results will be unpredictable");
			logger.debug(toString());
		}

		for (IGene _gene : genes) {
			
			// Only create the link if the gene is enabled
			if (_gene.isEnabled()) {

				curlink = _gene.getLink();

				inode = curlink.getInputNode().getAnalogue();
				onode = curlink.getOutputNode().getAnalogue();
				// NOTE: This line could be run through a recurrency check if desired
				// (no need to in the current implementation of NEAT)
				newlink = new Link(curlink.getWeight(), inode, onode, curlink.isRecurrent());
				onode.getIncoming().add(newlink);
				inode.getOutgoing().add(newlink);
			}

		}
		
		// Create the new network
		newnet = new Network(inlist, outlist, all_list, id);
		// Attach genotype and phenotype together:
		// newnet point to owner genotype (this)
		newnet.setGenotype(this);
		// genotype point to owner phenotype (newnet)

		phenotype = newnet;
		
		return newnet;
	}

	/**
	 * This function gives a measure of compatibility between two Genomes by
	 * computing a linear combination of 3 characterizing variables of their
	 * compatibilty. The 3 variables represent PERCENT DISJOINT GENES, PERCENT
	 * EXCESS GENES, MUTATIONAL DIFFERENCE WITHIN MATCHING GENES. So the formula
	 * for compatibility is:
	 * disjoint_coeff*pdg+excess_coeff*peg+mutdiff_coeff*mdmg. The 3
	 * coefficients are global system parameters
	 */
	public double compatibility(IGenome g) {

		// Innovation numbers
		double p1innov;
		double p2innov;

		// Intermediate value
		double mut_diff;

		// Set up the counters
		double num_disjoint = 0.0;
		double num_excess = 0.0;
		double mut_diff_total = 0.0;
		double num_matching = 0.0; // Used to normalize mutation_num differences

		IGene _gene1 = null;
		IGene _gene2 = null;

		double max_genome_size; // Size of larger Genome

		// Get the length of the longest Genome for percentage computations
		int size1 = genes.size();
		int size2 = g.getGenes().size();
		max_genome_size = Math.max(size1, size2);
		// Now move through the Genes of each potential parent
		// until both Genomes end
		int j = 0;
		int j1 = 0;
		int j2 = 0;

		for (j = 0; j < max_genome_size; j++) {

			if (j1 >= size1) {
				num_excess += 1.0;
				j2++;
			} else if (j2 >= size2) {
				num_excess += 1.0;
				j1++;
			} else {
				_gene1 = genes.get(j1);
				_gene2 = g.getGenes().get(j2);

				// Extract current innovation numbers
				p1innov = _gene1.getInnovationNumber();
				p2innov = _gene2.getInnovationNumber();

				if (p1innov == p2innov) {
					num_matching += 1.0;
					mut_diff = Math.abs(_gene1.getMutationNumber()
							- _gene2.getMutationNumber());
					mut_diff_total += mut_diff;
					j1++;
					j2++;
				} else if (p1innov < p2innov) {
					j1++;
					num_disjoint += 1.0;
				} else if (p2innov < p1innov) {
					j2++;
					num_disjoint += 1.0;
				}

			}

		}

		// Return the compatibility number using compatibility formula
		// Note that mut_diff_total/num_matching gives the AVERAGE
		// difference between mutation_nums for any two matching Genes
		// in the Genome.
		// Look at disjointedness and excess in the absolute (ignoring size)

		return (Neat.disjoint_coeff * (num_disjoint / 1.0)
				+ Neat.disjoint_coeff * (num_excess / 1.0) + Neat.mutdiff_coeff
				* (mut_diff_total / num_matching));

	}

	public double getLastGeneInnovationId() {
		return genes.get(genes.size() - 1).getInnovationNumber() + 1;
	}

	public int getLastNodeId() {
		return nodes.get(genes.size() - 1).getId() + 1;
	}

	

	public boolean verify() {

		INode inputNode = null;
		INode outputNode = null;
		int i1 = 0;
		int o1 = 0;
		boolean r1 = false;
		boolean disab = false;
		int last_id = 0;

		if (genes.size() == 0) {
			logger.error("Problem creating random Genome - There are no Genes!");
			return false;
		}

		if (nodes.size() == 0) {
			logger.error("Problem creating random Genome - There are no Nodes!");
			return false;
		}

		// control if nodes in gene are defined and are the same nodes il nodes list
		for (IGene gene : genes) {

			inputNode = gene.getLink().getInputNode();
			outputNode = gene.getLink().getOutputNode();

			if (inputNode == null) {
				logger.error("input node is null in genome #" + getId());
				return false;
			}
			if (outputNode == null) {
				logger.error("output node is null in genome #" + getId());
				return false;
			}
			if (!nodes.contains(inputNode)) {
				logger.error("Missing Input Node (" + inputNode.getId() + ") - node defined in gene not found in Vector nodes of genome #" + getId());
				return false;
			}
			if (!nodes.contains(outputNode)) {
				logger.error("Missing Output Node (" + outputNode.getId() + ") - node defined in gene not found in Vector nodes of genome #" + getId());
				return false;
			}
		}

		for (INode node : nodes) {
			if (node.getId() < last_id) {
				System.out.println("ALERT: NODES OUT OF ORDER : ");
				System.out.println(" last node_id is= " + last_id + " , current node_id=" + node.getId());
				return false;
			}
			last_id = node.getId();
		}

		// control in genes are gene duplicate for contents
		for (IGene gene : genes) {
			i1 = gene.getLink().getInputNode().getId();
			o1 = gene.getLink().getOutputNode().getId();
			r1 = gene.getLink().isRecurrent();

			for (IGene gene1 : genes) {
				if (gene1.getLink().getInputNode().getId() == i1
						&& gene1.getLink().getOutputNode().getId() == o1
						&& gene1.getLink().isRecurrent() == r1) {
					
					logger.error("DUPLICATE GENES! :");
					logger.error("input node = " + i1 + " output node = " + o1);
					logger.error("in GENOME id --> " + getId());
					logger.error("gene1 is : \n" + gene.toString());
					logger.error("gene2 is : " + gene1.toString());

					return false;
				}

			}

		}

		if (nodes.size() >= 500) {
			disab = false;
			for (IGene gene : genes) {
				if (!gene.isEnabled() && disab) {
					logger.error("2 DISABLES IN A ROW! : " + gene.getLink().getInputNode().getId());
					logger.error("input node = " + gene.getLink().getInputNode().getId());
					logger.error("output node = " + gene.getLink().getOutputNode().getId());
					logger.error("for GENOME " + getId());
					logger.error("Gene is : " + gene.toString());
				}
				
				disab = !gene.isEnabled();
			}
		}

		return true;
	}

	/**
	 * 
	 * Creation of a new random genome with : new_id = numerical identification
	 * of genome i = number of input nodes o = number of output nodes n = number
	 * of hidden nodes nmax = number max of node this number must be >= (i + n +
	 * o) r = the network can have a nodes recurrent ? linkprob = probability of
	 * a link from nodes ( must be in interval ]0,1[);
	 */
	public Genome(int new_id, int i, int o, int n, int nmax, boolean r,
			double linkprob) {
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
		//int pointer = 0;
		//int gene_number = 0;

		boolean flag_recurrent = false;
		boolean create_gene = false;

		double new_weight = 0.0;

		Trait newtrait = null;
		INode newnode = null;
		INode in_node = null;
		INode out_node = null;
		IGene newgene = null;

		//notes = null;

		//Iterator itr_node;

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

		nodes = new ArrayList<INode>(totalnodes);
		genes = new ArrayList<IGene>(totalnodes);

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
				newnode = new Node(NodeType.SENSOR, count, NodeLabel.INPUT);
			else
				newnode = new Node(NodeType.SENSOR, count, NodeLabel.BIAS);

			// Add the node to the list of nodes
			nodes.add(newnode);
		}

		// Build the hidden nodes
		for (count = i + 1; count <= i + n; count++) {
			newnode = new Node(NodeType.NEURON, count, NodeLabel.HIDDEN);

			// Add the node to the list of nodes
			nodes.add(newnode);
		}

		// Build the output nodes
		for (count = first_output; count <= totalnodes; count++) {
			newnode = new Node(NodeType.NEURON, count, NodeLabel.OUTPUT);

			// Add the node to the list of nodes
			nodes.add(newnode);
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
							Iterator<INode> nodeIterator = nodes.iterator(); // Retrieve the in_node
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
							genes.add(newgene);
						}
					} // end condition for a correct link in genome
					innov_number++;
				}
			}

			rc1 = verify();

			// System.out.print("\n      -> +rc1 = "+rc1);

			if (rc1) {
				INetwork net = genesis(getId());
				rc2 = net.isMinimal();

				// System.out.print("\n         -> +rc2 = "+rc2);

				if (rc2) {
					int lx = net.maxDepth();
					int dx = net.isStabilised(lx);

					// System.out.print("\n        lx = " + lx);
					// System.out.print(", dx = " + dx);

					if (((dx == lx) && (!r)) || ((lx > 0) && (r) && (dx == 0)))
						done = true;

				}
				net.setGenotype(null);
				this.phenotype = null;
			}
			if (!done)
				genes.clear();
			// else
			// System.out.print("\n * CREATION Genome #"+genome_id+" okay");
		}

	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public String toString() {

		StringBuilder s = new StringBuilder();
		
		s.append("GENOME START   id=" + getId());
		s.append("\n  genes are :" + genes.size());
		s.append("\n  nodes are :" + nodes.size());

		for (INode _node : nodes) {
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

		for (IGene _gene : genes) {
			s.append(_gene.toString());
		}
		
		return s.toString();
	}
}