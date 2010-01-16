package org.brainfarm.java.neat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.IInnovation;
import org.brainfarm.java.neat.api.ILink;
import org.brainfarm.java.neat.api.INetwork;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.ITrait;
import org.brainfarm.java.neat.api.enums.InnovationType;
import org.brainfarm.java.neat.api.enums.MutationType;
import org.brainfarm.java.neat.api.enums.NodeLabel;
import org.brainfarm.java.neat.api.enums.NodeType;
import org.brainfarm.java.util.RandomUtils;


public class Genome implements IGenome {

	private static Logger logger = Logger.getLogger(Genome.class);
	
	/** Is a reference from this genotype to fenotype */
	private INetwork phenotype;

	/** Numeric identification for this genotype */
	private int id;

	/**
	 * Each Gene in (3) has a marker telling when it arose historically; Thus,
	 * these Genes can be used to speciate the population, and the list of Genes
	 * provide an evolutionary history of innovation and link-building
	 */
	private List<IGene> genes;

	/** parameter conglomerations :Reserved parameter space for future use */
	private List<ITrait> traits;

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
		return traits;
	}

	public void setTraits(List<ITrait> traits) {
		this.traits = traits;
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
	public Genome(int id, List<ITrait> traits, List<INode> nodes, List<IGene> genes) {
		this.id = id;
		this.traits = traits;
		this.nodes = nodes;
		this.genes = genes;
	}

	public Genome duplicate(int new_id) {		
		
		ArrayList<ITrait> traits_dup = new ArrayList<ITrait>(traits.size());
		ArrayList<INode> nodes_dup = new ArrayList<INode>(nodes.size());
		ArrayList<IGene> genes_dup = new ArrayList<IGene>(genes.size());
		
		ITrait assoc_trait = null;
		int traitId;

		// Duplicate Traits.
		for (ITrait trait : traits) {
			traits_dup.add(new Trait(trait));
		}

		// Duplicate Nodes.
		for (INode _node : nodes) {
			if (_node.getTrait() != null) {
				traitId = _node.getTrait().getId();
				for (ITrait _trait : traits_dup) {
					if (_trait.getId() == traitId) {
						assoc_trait = _trait;
						break;
					}
				}
			}

			INode newnode = new Node(_node, assoc_trait);

			_node.setDuplicate(newnode);
			nodes_dup.add(newnode);
		}

		// Duplicate Genes.
		for (IGene gene : genes) {
			// point to news nodes created at precedent step
			INode inode = gene.getLink().getInputNode().getDuplicate();
			INode onode = gene.getLink().getOutputNode().getDuplicate();
			ITrait traitptr = gene.getLink().getTrait();

			assoc_trait = null;
			if (traitptr != null) {
				traitId = traitptr.getId();
				for (ITrait trait : traits) {
					if (trait.getId() == traitId) {
						assoc_trait = trait;
						break;
					}
				}
			}

			// creation of new gene with a pointer to new node
			genes_dup.add(new Gene(gene, assoc_trait, inode, onode));
		}

		// okay all nodes created, the new genome can be generate
		return new Genome(new_id, traits_dup, nodes_dup, genes_dup);
	}


	

	public void mutateLinkWeight(double power, double rate, MutationType mutationType) {

		double num; // counts gene placement
		double gene_total;
		double powermod; // Modified power by gene number

		// The power of mutation will rise farther into the genome
		// on the theory that the older genes are more fit since
		// they have stood the test of time

		double randnum;
		double randchoice; // Decide what kind of mutation to do on a gene
		double endpart; // Signifies the last part of the genome
		double gausspoint;
		double coldgausspoint;

		boolean severe; // Once in a while really shake things up

		// for 50% of Prob. // severe is true

		if (RandomUtils.randomDouble() > 0.5)
			severe = true;
		else
			severe = false;

		num = 0.0;
		gene_total = (double) genes.size();
		endpart = gene_total * 0.8;
		powermod = 1.0;

		for (IGene gene : genes) {

			if (severe) {
				gausspoint = 0.3;
				coldgausspoint = 0.1;
			}

			// with other 50%.....
			else {
				if ((gene_total >= 10.0) && (num > endpart)) {
					gausspoint = 0.5;
					coldgausspoint = 0.3;
				} else {
					if (RandomUtils.randomDouble() > 0.5) {
						gausspoint = 1.0 - rate;
						coldgausspoint = 1.0 - rate - 0.1;
					} else {
						gausspoint = 1.0 - rate;
						coldgausspoint = 1.0 - rate;
					}
				}
			}

			// choise a number from ]-1,+1[
			randnum = RandomUtils.randomBinomial() * RandomUtils.randomBinomial() * power * powermod;

			if (mutationType == MutationType.GAUSSIAN) {
				
				randchoice = RandomUtils.randomDouble(); // a number from ]0,1[
				
				if (randchoice > gausspoint) {
					gene.getLink().setWeight(gene.getLink().getWeight() + randnum);
				} else if (randchoice > coldgausspoint) {
					gene.getLink().setWeight(randnum);
				}
			} else if (mutationType == MutationType.COLD_GAUSSIAN) {
				gene.getLink().setWeight(randnum);
			}
			
			// copy to mutation_num, the current weight
			gene.setMutationNumber(gene.getLink().getWeight());
			num += 1.0;
		}
	}

	/**
	 * Generates a Network (Phenotype) from this Genome (Genotype).
	 * 
	 * @param id
	 * @return
	 */
	public INetwork genesis(int id) {

		INetwork newnet = null;
		ITrait curtrait = null;
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

			// Derive link's parameters from its Trait.
			curtrait = _node.getTrait();
			newnode.deriveTrait(curtrait);
			newnode.setInnerLevel(0);

			newnode.setGenNodeLabel(_node.getGenNodeLabel());

			// new field
			newnode.setTraversed(false);
			
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

				// Derive link's parameters from its Trait.
				curtrait = curlink.getTrait();
				curlink.deriveTrait(curtrait);
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

		if (traits.size() == 0) {
			logger.error("Problem creating random Genome - There are no Traits!");
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
	 * This chooses a random node and repoints the node to a random trait
	 */
	public void mutateNodeTrait(int repeats) {
		int traitnum;
		int nodenum;
		//int count;
		int loop;
		INode _node = null;
		//ITrait _trait = null;

		for (loop = 1; loop <= repeats; loop++) {

			// Choose a random traitnum
			traitnum = RandomUtils.randomInt(0, (traits.size()) - 1);
			// Choose a random nodenum
			nodenum = RandomUtils.randomInt(0, nodes.size() - 1);
			// set the link to point to the new trait
			_node = nodes.get(nodenum);
			//_trait = traits.get(traitnum);
			_node.setTrait(traits.get(traitnum));

			// TRACK INNOVATION! - possible future use
			// for any gene involving the mutated node, perturb that gene's
			// mutation number
			// for(thegene=genes.begin();thegene!=genes.end();++thegene) {
			// if (((((*thegene)->lnk)->in_node)==(*thenode))
			// ||
			// ((((*thegene)->lnk)->out_node)==(*thenode)))
			// (*thegene)->mutation_num+=randposneg()*randfloat()*nodetrait_mut_sig;
			// }
		}

	}

	public void nodeInsert(List<INode> nlist, INode n) {
		int j;
		int id = n.getId();
		int sz = nlist.size();

		for (j = 0; j < sz; j++) {
			if (nlist.get(j).getId() >= id)
				break;
		}
		nlist.add(j, n);

	}

	public boolean mutateAddLink(IPopulation population, int attempts) {
		boolean done = false;
		boolean do_recur = false;
		boolean loop_recur = false;
		boolean found = false;
		boolean bypass = false;
		boolean recurflag = false;

		int first_nonsensor;
		int trycount = 0;

		int thresh = nodes.size() * nodes.size();
		int nodenum1;
		int nodenum2;
		int traitnum;
		double new_weight;

		INode thenode1 = null;
		INode thenode2 = null;
		IGene new_gene = null;
		IGene _gene = null;

		//Iterator itr_gene = null;
		//Iterator itr_node = null;
		//Iterator<IInnovation> itr_innovation = null;

		// Make attempts to find an unconnected pair
		trycount = 0;

		// Decide whether to make this recurrent
		if (RandomUtils.randomDouble() < Neat.recur_only_prob)
			do_recur = true;
		else
			do_recur = false;

		// Find the first non-sensor so that the to-node won't look at sensors
		// as
		// possible destinations

		Iterator<INode> nodeIterator = nodes.iterator();
		
		first_nonsensor = 0;

		while (nodeIterator.hasNext()) {
			thenode1 = nodeIterator.next();
			if (thenode1.getType() != NodeType.SENSOR) {
				break;
			}
			first_nonsensor++;

		}
		found = false;
		while (trycount < attempts) {
			//
			// recurrency case .........
			//

			if (do_recur) {
				//
				// at this point :
				// 50% of prob to decide a loop recurrency( node X to node X)
				// 50% a normal recurrency ( node X to node Y)
				if (RandomUtils.randomDouble() > 0.5)
					loop_recur = true;
				else
					loop_recur = false;

				if (loop_recur) {
					nodenum1 = RandomUtils.randomInt(first_nonsensor, nodes
							.size() - 1);
					nodenum2 = nodenum1;
				} else {
					nodenum1 = RandomUtils.randomInt(0, nodes.size() - 1);
					nodenum2 = RandomUtils.randomInt(first_nonsensor, nodes
							.size() - 1);
				}

			}
			//
			// no recurrency case .........
			//
			else {
				nodenum1 = RandomUtils.randomInt(0, nodes.size() - 1);
				nodenum2 = RandomUtils.randomInt(first_nonsensor, nodes.size() - 1);

			}

			//
			// now point to object's nodes
			//
			thenode1 = nodes.get(nodenum1);
			thenode2 = nodes.get(nodenum2);

			//
			// verify if the possible new gene already EXIST
			//
			bypass = false;
			for (int j = 0; j < genes.size(); j++) {
				_gene = genes.get(j);
				if (thenode2.getType() == NodeType.SENSOR) {
					bypass = true;
					break;
				}
				if (_gene.getLink().getInputNode() == thenode1
						&& _gene.getLink().getOutputNode() == thenode2
						&& _gene.getLink().isRecurrent() && do_recur) {
					bypass = true;
					break;
				}

				if (_gene.getLink().getInputNode() == thenode1
						&& _gene.getLink().getOutputNode() == thenode2
						&& !_gene.getLink().isRecurrent() && !do_recur) {
					bypass = true;
					break;
				}

			}

			if (!bypass) {

				phenotype.setStatus(0);
				recurflag = phenotype.pathExists(thenode1.getAnalogue(),
						thenode2.getAnalogue(), 0, thresh);

				if (phenotype.getStatus() == 8) {
					System.out
							.println("\n  network.mutate_add_link : LOOP DETECTED DURING A RECURRENCY CHECK");
					return false;
				}

				if ((!recurflag && do_recur) || (recurflag && !do_recur))
					trycount++;
				else {
					trycount = attempts;
					found = true;
				}

			} // end block bypass

			//
			// if bypass is true, this gene is not good
			// and skip to next cycle
			//
			else
				trycount++;

		} // end block trycount

		if (found) {

			// Check to see if this innovation already occured in the population
			Iterator<IInnovation> innovationsIterator = population.getInnovations().iterator();

			done = false;
			while (!done) {

				if (!innovationsIterator.hasNext()) {

					// If the phenotype does not exist, exit on false,print
					// error
					// Note: This should never happen- if it does there is a bug
					if (phenotype == null) {
						System.out
								.print("ERROR: Attempt to add link to genome with no phenotype");
						return false;
					}

					// Choose a random trait
					traitnum = RandomUtils.randomInt(0, traits.size() - 1);

					// Choose the new weight
					// newweight=(gaussrand())/1.5; //Could use a gaussian
					new_weight = RandomUtils.randomBinomial() * RandomUtils.randomDouble() * 10.0;

					// read from population current innovation value

					// read curr innovation with postincrement
					double curr_innov = population.getCurrentInnovationNumberAndIncrement();
					// Create the new gene
					new_gene = new Gene((Trait) traits.get(traitnum),
							new_weight, thenode1, thenode2, do_recur,
							curr_innov, new_weight);
					// Add the innovation
					population.getInnovations().add(new Innovation(thenode1.getId(), thenode2.getId(), curr_innov, new_weight, traitnum));
					done = true;

				}

				// OTHERWISE, match the innovation in the innovs list
				else {
					IInnovation _innov = innovationsIterator.next();
					if ((_innov.getInnovationType() == InnovationType.NEW_LINK)
							&& (_innov.getInputNodeId() == thenode1.getId())
							&& (_innov.getOutputNodeId() == thenode2.getId())
							&& (_innov.isRecurrent() == do_recur)) {

						new_gene = new Gene((Trait) traits.get(_innov.getNewTraitId()), _innov.getNewWeight(), thenode1, thenode2, do_recur, _innov.getInnovationNumber1(), 0);
						done = true;
					}
				}
			}

			genes.add(new_gene);
			return true;
		}

		return false;

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

		traits = new ArrayList<ITrait>(Neat.num_trait_params);
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

		// Create a dummy trait (this is for future expansion of the system)

		newtrait = new Trait(1, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		traits.add(newtrait);

		// Build the input nodes
		for (count = 1; count <= i; count++) {
			if (count < i)
				newnode = new Node(NodeType.SENSOR, count, NodeLabel.INPUT);
			else
				newnode = new Node(NodeType.SENSOR, count, NodeLabel.BIAS);

			newnode.setTrait(newtrait);
			// Add the node to the list of nodes
			nodes.add(newnode);
		}

		// Build the hidden nodes
		for (count = i + 1; count <= i + n; count++) {
			newnode = new Node(NodeType.NEURON, count, NodeLabel.HIDDEN);
			newnode.setTrait(newtrait);

			// Add the node to the list of nodes
			nodes.add(newnode);
		}

		// Build the output nodes
		for (count = first_output; count <= totalnodes; count++) {
			newnode = new Node(NodeType.NEURON, count, NodeLabel.OUTPUT);
			newnode.setTrait(newtrait);

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
							newgene = new Gene(newtrait, new_weight, in_node, out_node, flag_recurrent, innov_number, new_weight);
							
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

	public void View_mate_singlepoint(IGenome g, int genomeid) {

		String mask4 = " 0000";
		DecimalFormat fmt4 = new DecimalFormat(mask4);

		//IGenome new_genome = null;
		int stopA = 0;
		int stopB = 0;
		int j;
		int j1;
		int j2;

		int size1 = genes.size();
		int size2 = g.getGenes().size();

		int crosspoint = 0;

		List<IGene> genomeA;
		List<IGene> genomeB;
		int genecounter = 0; // Ready to count to crosspoint

		if (size1 < size2) {
			stopA = size1;
			stopB = size2;
			genomeA = genes;
			genomeB = g.getGenes();
		} else {
			stopA = size2;
			stopB = size1;
			genomeA = g.getGenes();
			genomeB = genes;
		}

		double v3[][] = new double[size2 * 2][2];
		double vr[] = new double[size2 * 2];

		for (crosspoint = 0; crosspoint < stopA; crosspoint++)
		{

			genecounter = 0;

			boolean doneA = false;
			boolean doneB = false;
			boolean done = false;
			double v1 = 0.0;
			double v2 = 0.0;
			//double vmax = 0.0;
			j1 = 0;
			j2 = 0;
			j = 0;

			double cross_innov = 0;
			//
			// compute what is the hight innovation
			//	

			double last_innovB = ((Gene) genomeB.get(stopB - 1))
					.getInnovationNumber();

			while (!done) {

				doneA = false;
				doneB = false;

				if (j1 < stopA) {
					v1 = ((Gene) genomeA.get(j1)).getInnovationNumber();
					doneA = true;
				}
				if (j2 < stopB) {
					v2 = ((Gene) genomeB.get(j2)).getInnovationNumber();
					doneB = true;
				}

				if (doneA && doneB) {
					//
					if (v1 < v2) {
						v3[j][0] = v1;
						v3[j][1] = 0.0;
						j1++;
					} else if (v1 == v2) {
						v3[j][0] = v1;
						v3[j][1] = v1;
						j1++;
						j2++;
					} else {
						v3[j][0] = 0.0;
						v3[j][1] = v2;
						j2++;
					}
				}

				else {
					if (doneA && !doneB) {
						v3[j][0] = v1;
						v3[j][1] = 0.0;
						j1++;
					} else if (!doneA && doneB) {
						v3[j][0] = 0.0;
						v3[j][1] = v2;
						j2++;
					} else
						done = true;
				}

				if (!done) {

					// -------------------------------------------------------------------------------
					// innovA = innovB
					// -------------------------------------------------------------------------------

					if (v3[j][0] == v3[j][1]) {
						if (genecounter < crosspoint) {
							vr[j] = 1;
							genecounter++;
						} else if (genecounter == crosspoint) {
							vr[j] = 3;
							genecounter++;
							cross_innov = v3[j][0];
						} else if (genecounter > crosspoint) {
							vr[j] = 2;
							genecounter++;
						}
					}

					// -------------------------------------------------------------------------------
					// innovA < innovB
					// -------------------------------------------------------------------------------

					else if (v3[j][0] != 0 && v3[j][1] == 0) {
						if (genecounter < crosspoint) {
							vr[j] = 1; // v3[j][0];
							genecounter++;
						} else if (genecounter == crosspoint) {
							vr[j] = 1; // v3[j][1])
							genecounter++;
							cross_innov = v3[j][0];
						} else if (genecounter > crosspoint) {

							if (cross_innov > last_innovB) {
								vr[j] = 1;
								genecounter++;
							}
						}
					}

					// -------------------------------------------------------------------------------
					// innovA > innovB
					// -------------------------------------------------------------------------------

					else if (v3[j][0] == 0 && v3[j][1] != 0) {
						if (genecounter < crosspoint) {
							vr[j] = 0; // skip v3[j][0];
						} else if (genecounter == crosspoint) {
							vr[j] = 0; // skip
						}

						else if (genecounter > crosspoint) {
							if (cross_innov > last_innovB) {
								vr[j] = 1; // v3[j][1];
								genecounter++;
							} else {
								vr[j] = 2;
								genecounter++;
							}
						}

					}
				}
				j++;

			}

			int len_max = --j;

			//
			// only for debug : view innov's genomeA,B
			//	
			System.out.print("\n\n CROSSING SINGLE at index " + crosspoint);
			System.out.print("\n -- index -- ");
			int column = 0;
			for (j2 = 0; j2 < len_max; j2++) {
				if (v3[j2][0] > 0.0)
					System.out.print(fmt4.format((long) column++));
				else
					System.out.print("     ");
			}
			System.out.print("\n ----------- ");
			for (j2 = 0; j2 < len_max; j2++)
				System.out.print("-----");
			for (j1 = 0; j1 < 2; j1++) {
				System.out.print("\n Genome  [" + j1 + "] ");
				for (j2 = 0; j2 < len_max; j2++)
					System.out.print(fmt4.format((long) v3[j2][j1]));
			}
			System.out.print("\n newgene [X] ");
			for (j2 = 0; j2 < len_max; j2++) {
				if (vr[j2] == 1)
					System.out.print("  AA ");
				else if (vr[j2] == 2)
					System.out.print("  BB ");
				else if (vr[j2] == 3)
					System.out.print("  XX ");
				else if (vr[j2] == 4)
					System.out.print("  MM ");
				else if (vr[j2] == 0)
					System.out.print("  -- ");
			}
			System.out.print("\n");
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
		s.append("\n  trait are :" + traits.size());

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

		s.append("\n Traits:\n");

		for (ITrait _trait : traits) {
			s.append(_trait.toString());
		}
		
		return s.toString();
	}
}