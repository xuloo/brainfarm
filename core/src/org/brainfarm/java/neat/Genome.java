package org.brainfarm.java.neat;

import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.INetwork;
import org.brainfarm.java.neat.api.INode;

public abstract class Genome implements IGenome {

	protected static Logger logger = Logger.getLogger(Genome.class);
	
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
	 * Creates a new Genome using the supplied nodes and genes and with the supplied id.
	 * 
	 * @param id
	 * @param nodes
	 * @param genes
	 */
	public Genome(int id, List<INode> nodes, List<IGene> genes) {
		this.id = id;
		this.nodes = nodes;
		this.genes = genes;
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

		for (INode _node : nodes) 
			s.append(_node.toString());

		for (IGene _gene : genes) {
			s.append(_gene.toString());
		}
		
		return s.toString();
	}
}