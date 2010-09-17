package org.brainfarm.java.feat.ann;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.Gene;
import org.brainfarm.java.feat.Innovation;
import org.brainfarm.java.feat.api.IGene;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IInnovation;
import org.brainfarm.java.feat.api.ILink;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ann.INeatNode;
import org.brainfarm.java.feat.api.enums.MutationType;
import org.brainfarm.java.feat.api.enums.NodeLabel;
import org.brainfarm.java.feat.api.enums.NodeType;
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.params.IEvolutionConstants;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.brainfarm.java.util.EvolutionUtils;
import org.brainfarm.java.util.RandomUtils;

/**
 * This is the mutation strategy employed by Ugo's original JNeat.
 * 
 * @author dtuohy, orig. Ugo Vierucci
 * @author Trevor.Burton 17.09.10 General clean up of the code - removed a lot of unnecessary variables and such-like.
 *
 */
public class NeatMutationStrategy implements IMutationStrategy, IEvolutionConstants {
	
	private static Logger log = Logger.getLogger(NeatMutationStrategy.class);

	protected IEvolutionParameters evolutionParameters;

	@Override
	public void mutate(IGenome genome, IPopulation pop, int generation) {
		if (RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(MUTATE_ADD_NODE_PROB)) {
			mutateAddNode(genome,pop);
		} else if (RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(MUTATE_ADD_LINK_PROB)) {
			genome.generatePhenotype(generation);
			mutateAddLink(genome, pop);
		} else {
			
			if (RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(MUTATE_LINK_WEIGHTS_PROB)) {
				mutateLinkWeight(genome, evolutionParameters.getDoubleParameter(WEIGHT_MUT_POWER), 1.0,MutationType.GAUSSIAN);
			}
			
			if (RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(MUTATE_TOGGLE_ENABLE_PROB)) {
				mutateToggleEnable(genome,1);
			}
			
			if (RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(MUTATE_GENE_REENABLE_PROB)) {
				mutateGeneReenable(genome);
			}
		}
	}

	public boolean mutateAddLink(IGenome genome, IPopulation population) {	

		//get fields from the genome
		List<INode> nodes = genome.getNodes();
		List<IGene> genes = genome.getGenes();
		INetwork phenotype = genome.getPhenotype();

		INode node1 = null;
		INode node2 = null;

		// Decide whether to make this recurrent
		boolean recurrent = RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(RECUR_ONLY_PROB);

		// Find the first non-sensor so that the to-node won't look at sensors as possible destinations
		Iterator<INode> nodeIterator = nodes.iterator();
		int firstNonSensor = 0;

		while (nodeIterator.hasNext()) {
			if (((INeatNode)nodeIterator.next()).getType() != NodeType.SENSOR) break;
			firstNonSensor++;
		}
		
		boolean found = false;
		int maxAttempts = evolutionParameters.getIntParameter(NEWLINK_TRIES);
		int currentAttempt = 0;
		
		while (currentAttempt < maxAttempts) {

			if (recurrent) {
				// 50% of prob to decide a loop recurrency( node X to node X)
				// 50% a normal recurrency ( node X to node Y)
				if (RandomUtils.randomDouble() > 0.5) {
					node1 = node2 = nodes.get(RandomUtils.randomInt(firstNonSensor, nodes.size() - 1));
				} else {
					node1 = nodes.get(RandomUtils.randomInt(0, nodes.size() - 1));
					node2 = nodes.get(RandomUtils.randomInt(firstNonSensor, nodes.size() - 1));
				}
			} else {
				node1 = nodes.get(RandomUtils.randomInt(0, nodes.size() - 1));
				node2 = nodes.get(RandomUtils.randomInt(firstNonSensor, nodes.size() - 1));
			}

			// verify if the possible new gene already exists.
			boolean bypass = false;
			
			for (IGene gene : genes) {
				if ((((INeatNode)node2).getType() == NodeType.SENSOR) ||
					(gene.getLink().getInputNode() == node1 && 
					 gene.getLink().getOutputNode() == node2 && 
					 gene.getLink().isRecurrent() && recurrent) ||
					(gene.getLink().getInputNode() == node1 && 
					 gene.getLink().getOutputNode() == node2 && 
					 !gene.getLink().isRecurrent() && !recurrent)) {
					bypass = true;
					break;
				}
			}

			if (!bypass) {
				if (phenotype.pathExists(node1.getAnalogue(), node2.getAnalogue(), 0, nodes.size() * nodes.size()) != recurrent) {
					currentAttempt++;
				} else {
					currentAttempt = maxAttempts;
					found = true;
				}
			} else {
				currentAttempt++;
			}
		}

		if (found) {
			
			IGene newGene;
			
			// Check to see if this innovation already occured in the population
			IInnovation existingInnov = population.getExistingLinkInnovation(node1.getId(), node2.getId(), recurrent);

			if (existingInnov == null) {
				
				if (phenotype == null) {
					log.error("attempt to add link to genome with no phenotype");
					return false;
				}

				double newWeight = RandomUtils.randomBinomial() * RandomUtils.randomDouble() * 10.0;
				double innovationNumber = population.getCurrentInnovationNumberAndIncrement();
				population.getInnovations().add(new Innovation(node1.getId(), node2.getId(), innovationNumber, newWeight));
				
				newGene = new Gene(newWeight, node1, node2, recurrent, innovationNumber, newWeight);			
			} else {
				newGene = new Gene(existingInnov.getNewWeight(), node1, node2, recurrent, existingInnov.getInnovationNumber1(), 0);
			}

			genes.add(newGene);
			
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

		// for 50% of Prob. // severe is true

		boolean severe = RandomUtils.randomDouble() > 0.5;

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

		IGene _gene = null;

		ILink thelink = null;
		double oldweight = 0;

		IGene newgene1 = null;
		IGene newgene2 = null;
		INode in_node = null;
		INode out_node = null;
		INode new_node = null;

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
						&& (((INeatNode)_gene.getLink().getInputNode()).getGenNodeLabel() != NodeLabel.BIAS))
					break;
			}

			for (; j < genes.size(); j++) {
				_gene = genes.get(j);
				if ((RandomUtils.randomDouble() >= 0.3)
						&& (((INeatNode)_gene.getLink().getInputNode()).getGenNodeLabel() != NodeLabel.BIAS)) {
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
						&& (((INeatNode)_gene.getLink().getInputNode()).getGenNodeLabel() != NodeLabel.BIAS))
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

		// Extract the nodes
		in_node = thelink.getInputNode();
		out_node = thelink.getOutputNode();

		IInnovation existingInnov = population.getExistingNodeInnovation(in_node.getId(),out_node.getId(),_gene.getInnovationNumber());

		// If the innovation is totally novel
		if (existingInnov==null) {

			// Create the new Genes
			// Create the new NNode
			// get the current node id with postincrement

			int curnode_id = population.getCurrentNodeIdAndIncrement();

			// pass this current nodeid to newnode and create the new node
			new_node = new NeatNode(NodeType.NEURON, curnode_id, NodeLabel.HIDDEN);

			// get the current gene inovation with post increment
			gene_innov1 = population.getCurrentInnovationNumberAndIncrement();

			// create gene with the current gene inovation
			newgene1 = new Gene(1.0, in_node, new_node, thelink.isRecurrent(), gene_innov1, 0);

			// re-read the current innovation with increment
			gene_innov2 = population.getCurrentInnovationNumberAndIncrement();

			// create the second gene with this innovation incremented
			newgene2 = new Gene(oldweight, new_node, out_node, false, gene_innov2, 0);

			population.getInnovations().add(new Innovation(in_node.getId(), out_node .getId(), gene_innov1, gene_innov2, new_node.getId(), _gene.getInnovationNumber()));

		}
		// use existing innovation
		else {
			// Create the new Genes, user current nodeid
			new_node = new NeatNode(NodeType.NEURON, existingInnov.getNewNodeId(), NodeLabel.HIDDEN);
			newgene1 = new Gene(1.0, in_node, new_node, thelink.isRecurrent(), existingInnov.getInnovationNumber1(), 0);
			newgene2 = new Gene(oldweight, new_node, out_node, false, existingInnov.getInnovationNumber2(), 0);
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
}