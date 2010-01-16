package org.brainfarm.java.neat.operators;

import java.util.Iterator;
import java.util.List;

import org.brainfarm.java.neat.Gene;
import org.brainfarm.java.neat.Innovation;
import org.brainfarm.java.neat.Neat;
import org.brainfarm.java.neat.Node;
import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.IInnovation;
import org.brainfarm.java.neat.api.ILink;
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
			genome.mutateAddLink(pop, Neat.newlink_tries);
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
				genome.mutateNodeTrait(1);
			}
			if (RandomUtils.randomDouble() < Neat.mutate_link_weights_prob) {
//				logger.debug("...mutate link weight");
				genome.mutateLinkWeight(mut_power, 1.0,
						MutationType.GAUSSIAN);
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
					new_node.setTrait(traits.get(0));

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
						new_node.setTrait(traits.get(0));

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
}