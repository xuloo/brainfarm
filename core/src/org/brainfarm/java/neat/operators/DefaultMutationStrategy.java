package org.brainfarm.java.neat.operators;

import java.util.Iterator;
import java.util.List;

import org.brainfarm.java.neat.Gene;
import org.brainfarm.java.neat.Innovation;
import org.brainfarm.java.neat.Neat;
import org.brainfarm.java.neat.Node;
import org.brainfarm.java.neat.Trait;
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
import org.brainfarm.java.neat.api.operators.IMutationStrategy;
import org.brainfarm.java.util.EvolutionUtils;
import org.brainfarm.java.util.RandomUtils;

/**
 * This is the mutation strategy employed by Ugo's original
 * JNeat.
 * 
 * @author dtuohy, orig. Ugo Vierucci
 *
 */
public class DefaultMutationStrategy implements IMutationStrategy {

	// The weight mutation power is species specific depending on its age
	double mut_power = Neat.weight_mut_power;
	
	@Override
	public boolean mutate(IGenome genome, IPopulation pop, int generation) {
		boolean mutatedStructure = false;
		
		if (RandomUtils.randomDouble() < Neat.mutate_add_node_prob) {
//			logger.debug("....species.reproduce.mutate add node");
			mutateAddNode(genome,pop);
			mutatedStructure = true;
		} else if (RandomUtils.randomDouble() < Neat.mutate_add_link_prob) {
//			logger.debug("....mutate add link");
			genome.genesis(generation);
			mutateAddLink(genome, pop);
			mutatedStructure = true;
		} else {
			// If we didn't do a structural mutation, we do the other kinds.
			if (RandomUtils.randomDouble() < Neat.mutate_random_trait_prob) {
//				logger.debug("...mutate random trait");
				mutateRandomTrait(genome);
			}
			if (RandomUtils.randomDouble() < Neat.mutate_link_trait_prob) {
//				logger.debug("...mutate linktrait");
				mutateLinkTrait(genome,1);
			}
			if (RandomUtils.randomDouble() < Neat.mutate_node_trait_prob) {
//				logger.debug("...mutate node trait");
				mutateNodeTrait(genome,1);
			}
			if (RandomUtils.randomDouble() < Neat.mutate_link_weights_prob) {
//				logger.debug("...mutate link weight");
				mutateLinkWeight(genome, mut_power, 1.0,MutationType.GAUSSIAN);
			}
			if (RandomUtils.randomDouble() < Neat.mutate_toggle_enable_prob) {
//				logger.debug("...mutate toggle enable");
				mutateToggleEnable(genome,1);
			}
			if (RandomUtils.randomDouble() < Neat.mutate_gene_reenable_prob) {
//				logger.debug("...mutate gene_reenable:");
				mutateGeneReenable(genome);
			}
		}
		return mutatedStructure;
	}
	
	public boolean mutateAddLink(IGenome genome, IPopulation population) {
		
		int attempts = Neat.newlink_tries;
		
		//get fields from the genome
		List<INode> nodes = genome.getNodes();
		List<IGene> genes = genome.getGenes();
		INetwork phenotype = genome.getPhenotype();
		List<ITrait> traits = genome.getTraits();
		
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
	
	public void mutateLinkWeight(IGenome genome, double power, double rate, MutationType mutationType) {

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
		gene_total = (double) genome.getGenes().size();
		endpart = gene_total * 0.8;
		powermod = 1.0;

		for (IGene gene : genome.getGenes()) {

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
	
	public boolean mutateAddNode(IGenome genome, IPopulation population) {

			List<IGene> genes = genome.getGenes();
			List<ITrait> traits = genome.getTraits();
		
			IGene _gene = null;

			ILink thelink = null;
			double oldweight = 0;

			IGene newgene1 = null;
			IGene newgene2 = null;
			INode in_node = null;
			INode out_node = null;
			INode new_node = null;
			//Iterator itr_innovation;
			ITrait traitptr = null;

			int j;
			int genenum = 0;
			int trycount = 0;

			boolean found = false;
			//boolean bypass = false;
			//boolean step1 = true;
			boolean step2 = false;
			double gene_innov1;
			double gene_innov2;

			if (genes.size() < 15) {

				step2 = false;
				for (j = 0; j < genes.size(); j++) {
					_gene = genes.get(j);
					if (_gene.isEnabled()
							&& (_gene.getLink().getInputNode().getGenNodeLabel() != NodeLabel.BIAS))
						break;
				}

				for (; j < genes.size(); j++) {
					_gene = genes.get(j);
					if ((RandomUtils.randomDouble() >= 0.3)
							&& (_gene.getLink().getInputNode().getGenNodeLabel() != NodeLabel.BIAS)) {
						step2 = true;
						break;
					}
				}

				if ((step2) && (_gene.isEnabled())) {
					found = true;

				}

			} else {
				while ((trycount < 20) && (!found)) {
					// Pure random splittingNeatRoutine.randint
					genenum = RandomUtils.randomInt(0, genes.size() - 1);
					_gene = genes.get(genenum);
					if (_gene.isEnabled()
							&& (_gene.getLink().getInputNode().getGenNodeLabel() != NodeLabel.BIAS))
						found = true;
					++trycount;

				}
			}

			if (!found)
				return false;

			_gene.setEnabled(false);

			// Extract the link
			thelink = _gene.getLink();
			// Extract the weight;
			oldweight = thelink.getWeight();
			// Get the old link's trait
			traitptr = thelink.getTrait();

			// Extract the nodes
			in_node = thelink.getInputNode();
			out_node = thelink.getOutputNode();

			boolean done = false;
			Iterator<IInnovation> innovationIterator = population.getInnovations().iterator();

			while (!done) {
				// Check to see if this innovation already occured in the population
				if (!innovationIterator.hasNext()) {

					// The innovation is totally novel
					// Create the new Genes
					// Create the new NNode
					// By convention, it will point to the first trait
					// get the current node id with postincrement

					int curnode_id = population.getCurrentNodeIdAndIncrement();

					// pass this current nodeid to newnode and create the new node
					new_node = new Node(NodeType.NEURON, curnode_id, NodeLabel.HIDDEN);

					// get the current gene inovation with post increment
					gene_innov1 = population.getCurrentInnovationNumberAndIncrement();

					// create gene with the current gene inovation
					newgene1 = new Gene(traitptr, 1.0, in_node, new_node, thelink.isRecurrent(), gene_innov1, 0);

					// re-read the current innovation with increment
					gene_innov2 = population.getCurrentInnovationNumberAndIncrement();

					// create the second gene with this innovation incremented
					newgene2 = new Gene(traitptr, oldweight, new_node, out_node, false, gene_innov2, 0);

					population.getInnovations().add(new Innovation(in_node.getId(), out_node .getId(), gene_innov1, gene_innov2, new_node.getId(), _gene.getInnovationNumber()));
					
					done = true;
				}
				// end for new innovation case
				else {
					IInnovation _innov = innovationIterator.next();

					if ((_innov.getInnovationType() == InnovationType.NEW_NODE)
							&& (_innov.getInputNodeId() == in_node.getId())
							&& (_innov.getOutputNodeId() == out_node.getId())
							&& (_innov.getOldInnovationNumber() == _gene.getInnovationNumber())) {
						// Create the new Genes
						// pass this current nodeid to newnode
						new_node = new Node(NodeType.NEURON, _innov.getNewNodeId(), NodeLabel.HIDDEN);

						newgene1 = new Gene(traitptr, 1.0, in_node, new_node, thelink.isRecurrent(), _innov.getInnovationNumber1(), 0);
						newgene2 = new Gene(traitptr, oldweight, new_node, out_node, false, _innov.getInnovationNumber2(), 0);
						done = true;

					}
				}

			}

			// Now add the new NNode and new Genes to the Genome

			genes.add(newgene1);
			genes.add(newgene2);
			EvolutionUtils.nodeInsert(genome.getNodes(), new_node);

			return true;

		}
	
	public void mutateGeneReenable(IGenome genome) {
		for (IGene gene : genome.getGenes()) {
			if (!gene.isEnabled()) {
				gene.setEnabled(true);
				break;
			}
		}
	}
	
	/**
	 * Toggle genes from enable on to enable off or
	 * vice versa. Do it times times.
	 */
	public void mutateToggleEnable(IGenome genome, int repeats) {
		int genenum;
		int count;
		
		IGene _gene = null;
		IGene _jgene = null;

		int len_gene = genome.getGenes().size();
		boolean done = false;

		for (count = 1; count <= repeats; count++) {

			// Choose a random genenum
			genenum = RandomUtils.randomInt(0, genome.getGenes().size() - 1);
			// find the gene
			_gene = genome.getGenes().get(genenum);
			// Toggle the enable on this gene

			if (_gene.isEnabled()) {
				// We need to make sure that another gene connects out of the
				// in-node
				// Because if not a section of network will break off and become
				// isolated
				done = false;
				for (int j = 0; j < len_gene; j++) {
					_jgene = genome.getGenes().get(j);
					if ((_gene.getLink().getInputNode() == _jgene.getLink().getInputNode())
							&& _jgene.isEnabled()
							&& (_jgene.getInnovationNumber() != _gene
									.getInnovationNumber())) {
						done = true;
						break;
					}
				}
				// Disable the gene if it's safe to do so
				if (done)
					_gene.setEnabled(false);

			} else
				_gene.setEnabled(true);
		}

	}
	
	/**
	 * Insert the method's description here. Creation date: (24/01/2002 9.03.36)
	 */
	public void mutateRandomTrait(IGenome genome) {
		
		// Choose a random traitnum
		int traitnum = RandomUtils.randomInt(0, genome.getTraits().size() - 1);
		// Retrieve the trait and mutate it
		genome.getTraits().get(traitnum).mutate();

		// TRACK INNOVATION? (future possibility)
	}
	
	/**
	 * This chooses a random gene, extracts the link from it, and repoints the
	 * link to a random trait
	 */
	public void mutateLinkTrait(IGenome genome, int repeats) {
		int traitnum;
		int genenum;
		//int count;
		int loop;
		IGene _gene = null;
		ITrait _trait = null;

		for (loop = 1; loop <= repeats; loop++) {

			// Choose a random traitnum
			traitnum = RandomUtils.randomInt(0, genome.getTraits().size() - 1);
			// Choose a random linknum
			genenum = RandomUtils.randomInt(0, genome.getGenes().size() - 1);
			// set the link to point to the new trait
			_gene = genome.getGenes().get(genenum);
			_trait = genome.getTraits().get(traitnum);
			_gene.getLink().setTrait(_trait);

			// TRACK INNOVATION- future use
			// (*thegene)->mutation_num+=randposneg()*randfloat()*linktrait_mut_sig;
		}
	}
	
	//TODO: remove this code, we don't have traits
	/**
	 * This chooses a random node and repoints the node to a random trait
	 */
	public void mutateNodeTrait(IGenome genome, int repeats) {
		int traitnum;
		int nodenum;
		//int count;
		int loop;
		INode _node = null;
		//ITrait _trait = null;

		for (loop = 1; loop <= repeats; loop++) {

			// Choose a random traitnum
			traitnum = RandomUtils.randomInt(0, (genome.getTraits().size()) - 1);
			// Choose a random nodenum
			nodenum = RandomUtils.randomInt(0, genome.getNodes().size() - 1);
			// set the link to point to the new trait
			_node = genome.getNodes().get(nodenum);

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
}