package org.brainfarm.java.feat;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.IGene;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.ILink;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.brainfarm.java.feat.operators.FeatFactory;

/**
 * The base implementation of a Genome, it contains
 * everything required by the FEAT everything.
 * 
 * @author dtuohy
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class Genome implements IGenome {

	private static Logger logger = Logger.getLogger(Genome.class);
	
	public IEvolutionParameters evolutionParameters;
	
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
	public double compatibility(IGenome other) {

		// Set up the counters
		int num_disjoint = 0;
		int num_excess = 0;
		double mut_diff_total = 0.0;
		int num_matching = 0; // Used to normalize mutation_num differences
		int j1 = 0;
		int j2 = 0;
		
		for (int j = 0; j < Math.max(genes.size(), other.getGenes().size()); j++) {
			if (j1 >= genes.size()) {
				num_excess++;
				j2++;
			} else if (j2 >= other.getGenes().size()) {
				num_excess++;
				j1++;
			} else {
				IGene gene1 = genes.get(j1);
				IGene gene2 = other.getGenes().get(j2);

				// Extract current innovation numbers
				double p1innov = gene1.getInnovationNumber();
				double p2innov = gene2.getInnovationNumber();

				if (p1innov == p2innov) {
					num_matching++;
					mut_diff_total += Math.abs(gene1.getMutationNumber() - gene2.getMutationNumber());
					j1++;
					j2++;
				} else if (p1innov < p2innov) {
					j1++;
					num_disjoint++;
				} else if (p2innov < p1innov) {
					j2++;
					num_disjoint++;
				}
			}
		}

		// Return the compatibility number using compatibility formula
		// Note that mut_diff_total/num_matching gives the AVERAGE
		// difference between mutation_nums for any two matching Genes
		// in the Genome.
		// Look at disjointedness and excess in the absolute (ignoring size)

		return (evolutionParameters.getDoubleParameter(DISJOINT_COEFF) * (num_disjoint / 1.0) + 
				evolutionParameters.getDoubleParameter(DISJOINT_COEFF) * (num_excess / 1.0) + 
				evolutionParameters.getDoubleParameter(MUTDIFF_COEFF)  * (mut_diff_total / num_matching));

	}

	public double getLastGeneInnovationId() {
		return genes.get(genes.size() - 1).getInnovationNumber() + 1;
	}

	public int getLastNodeId() {
		return nodes.get(genes.size() - 1).getId() + 1;
	}

	@Override
	public IGenome duplicate(int id) {
		ArrayList<INode> duplicateNodes = new ArrayList<INode>(getNodes().size());
		ArrayList<IGene> duplicateGenes = new ArrayList<IGene>(getGenes().size());

		// Duplicate Nodes.
		for (INode node : getNodes()) {
			duplicateNodes.add(node.generateDuplicate());
		}

		// Duplicate Genes.
		for (IGene gene : getGenes()) {
			// creation of new gene with a pointer to new node
			duplicateGenes.add(new Gene(gene, 
										gene.getLink().getInputNode().getCachedDuplicate(), 
										gene.getLink().getOutputNode().getCachedDuplicate()));
		}

		// okay all nodes created, the new genome can be generate
		return FeatFactory.newOffspringGenome(id, duplicateNodes, duplicateGenes);
	}

	@Override
	/**
	 * Generates a Network (Phenotype) from this Genome (Genotype).
	 * 
	 * @param id
	 * @return
	 */
	public INetwork generatePhenotype(int id) {

		List<INode> nodes = new ArrayList<INode>(getNodes().size());
		
		for (INode n : getNodes()) {
			
			// create a copy of gene node for phenotype.
			INode node = FeatFactory.newOffspringNodeFrom(n);

			// add to genotype the pointer to phenotype node
			nodes.add(node);
			n.setAnalogue(node);
		}

		for (IGene gene : getGenes()) {
			
			// Only create the link if the gene is enabled
			if (gene.isEnabled()) {

				ILink link = gene.getLink();
				
				// NOTE: This line could be run through a recurrency check if desired
				// (no need to in the current implementation of NEAT)
				new Link(link.getWeight(), 
						 link.getInputNode().getAnalogue(), 
						 link.getOutputNode().getAnalogue(), 
						 link.isRecurrent());
			}
		}
		
		// Create the new network
		INetwork network = FeatFactory.newNetwork(nodes, id);
	
		network.setGenotype(this);
		setPhenotype(network);
		
		return network;
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

	@Override
	public void setEvolutionParameters(IEvolutionParameters evolutionParameters) {
		this.evolutionParameters = evolutionParameters;
	}

	@Override
	public boolean validate() {
		boolean valid = true;
		for(INode node : getNodes())
			if(!node.validate())
				valid = false;
		return valid;
	}
}
