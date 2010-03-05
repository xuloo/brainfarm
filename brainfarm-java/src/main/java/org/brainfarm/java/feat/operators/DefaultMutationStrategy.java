package org.brainfarm.java.feat.operators;

import java.util.Iterator;
import java.util.List;

import org.brainfarm.java.feat.api.IGene;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IInnovation;
import org.brainfarm.java.feat.api.ILink;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.enums.InnovationType;
import org.brainfarm.java.feat.api.enums.MutationType;
import org.brainfarm.java.feat.api.operators.IMutationStrategy;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.brainfarm.java.util.EvolutionUtils;
import org.brainfarm.java.util.RandomUtils;

/**
 * Based on the mutation strategy employed by Ugo's original
 * JNeat.  A few bits of the logic which relied upon features
 * specific to ANNs have been removed.
 * 
 * @author dtuohy, orig. Ugo Vierucci
 *
 */
public class DefaultMutationStrategy implements IMutationStrategy {

	protected IEvolutionParameters evolutionParameters;

	public DefaultMutationStrategy(IEvolutionParameters evolutionParameters) {
		this.evolutionParameters = evolutionParameters;
	}

	@Override
	public void mutate(IGenome genome, IPopulation pop, int generation) {
		if (RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(MUTATE_ADD_NODE_PROB)) {
			//			logger.debug("....species.reproduce.mutate add node");
			mutateAddNode(genome,pop);
		} else if (RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(MUTATE_ADD_LINK_PROB)) {
			//			logger.debug("....mutate add link");
			genome.generatePhenotype(generation);
			mutateAddLink(genome, pop);
		} else {
			if (RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(MUTATE_LINK_WEIGHTS_PROB)) {
				//				logger.debug("...mutate link weight");
				mutateLinkWeight(genome, evolutionParameters.getDoubleParameter(WEIGHT_MUT_POWER), 1.0,MutationType.GAUSSIAN);
			}
			if (RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(MUTATE_TOGGLE_ENABLE_PROB)) {
				//				logger.debug("...mutate toggle enable");
				mutateToggleEnable(genome,1);
			}
			if (RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(MUTATE_GENE_REENABLE_PROB)) {
				//				logger.debug("...mutate gene_reenable:");
				mutateGeneReenable(genome);
			}
		}
	}

	public boolean mutateAddLink(IGenome genome, IPopulation population) {

		int attempts = evolutionParameters.getIntParameter(NEWLINK_TRIES);

		//get fields from the genome
		List<INode> nodes = genome.getNodes();
		List<IGene> genes = genome.getGenes();
		INetwork phenotype = genome.getPhenotype();

		boolean found = false;
		boolean recurflag = false;

		int trycount = 0;

		int thresh = nodes.size() * nodes.size();
		int nodenum1 = 0;
		int nodenum2 = 0;
		double new_weight;

		INode thenode1 = null;
		INode thenode2 = null;
		IGene new_gene = null;
		IGene _gene = null;

		// Make attempts to find an unconnected pair
		trycount = 0;

		found = false;
		while (trycount < attempts) {

			nodenum1 = RandomUtils.randomInt(nodes.size());
			nodenum2 = RandomUtils.randomInt(nodes.size());

			// now point to object's nodes
			thenode1 = nodes.get(nodenum1);
			thenode2 = nodes.get(nodenum2);

			// check if the possible new gene already exists
			boolean alreadyExists = false;
			for (int j = 0; j < genes.size(); j++) {
				_gene = genes.get(j);
				if (_gene.getLink().getInputNode() == thenode1
						&& _gene.getLink().getOutputNode() == thenode2) {
					alreadyExists = true;
					break;
				}
			}

			if (!alreadyExists) {
				recurflag = phenotype.pathExists(thenode1.getAnalogue(),
						thenode2.getAnalogue(), 0, thresh);
				trycount = attempts;
				found = true;
			}
			// gene already exists, try again
			else
				trycount++;
		} // end block trycount

		if (found) {

			// Check to see if this innovation already occured in the population
			IInnovation existingInnov = population.getExistingLinkInnovation(thenode1.getId(), thenode2.getId(), recurflag);
			if (existingInnov == null) {

				// Choose the new weight
				new_weight = RandomUtils.randomBinomial() * RandomUtils.randomDouble() * 10.0;

				// read from population current innovation value
				// read curr innovation with postincrement
				double curr_innov = population.getCurrentInnovationNumberAndIncrement();
				// Create the new gene
				//new_gene = new Gene(new_weight, thenode1, thenode2, recurflag, curr_innov, new_weight);
				new_gene = FeatFactory.newGene(new_weight, thenode1, thenode2, recurflag, curr_innov, new_weight);

				// Add the innovation
				population.getInnovations().add(FeatFactory.newSingleInnovation(thenode1.getId(), thenode2.getId(), curr_innov, new_weight));
			}

			// OTHERWISE, match the innovation in the innovs list
			else {

				//new_gene = new Gene(_innov.getNewWeight(), thenode1, thenode2, recurflag, _innov.getInnovationNumber1(), 0);
				new_gene = FeatFactory.newGene(existingInnov.getNewWeight(), thenode1, thenode2, recurflag, existingInnov.getInnovationNumber1(), 0);
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
		boolean step2 = false;
		double gene_innov1;
		double gene_innov2;

		if (genes.size() < 15) {

			step2 = false;
			for (j = 0; j < genes.size(); j++) {
				_gene = genes.get(j);
				if (_gene.isEnabled())
					break;
			}

			for (; j < genes.size(); j++) {
				_gene = genes.get(j);
				if ((RandomUtils.randomDouble() >= 0.3)) {
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
				if (_gene.isEnabled())
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

		// The innovation is totally novel
		if(existingInnov==null){

			// get the current node id with postincrement
			int curnode_id = population.getCurrentNodeIdAndIncrement();

			// pass this current nodeid to newnode and create the new node
			new_node = FeatFactory.newNodeForId(curnode_id);

			// get the current gene inovation with post increment
			gene_innov1 = population.getCurrentInnovationNumberAndIncrement();

			// create gene with the current gene inovation
			newgene1 = FeatFactory.newGene(1.0, in_node, new_node, thelink.isRecurrent(), gene_innov1, 0);

			// re-read the current innovation with increment
			gene_innov2 = population.getCurrentInnovationNumberAndIncrement();

			// create the second gene with this innovation incremented
			newgene2 = FeatFactory.newGene(oldweight, new_node, out_node, false, gene_innov2, 0);

			population.getInnovations().add(FeatFactory.newDoubleInnovation(in_node.getId(), out_node .getId(), gene_innov1, gene_innov2, new_node.getId(), _gene.getInnovationNumber()));
		}
		// innovation already exists
		else {
			// Create the new Genes, pass this current nodeid to newnode
			new_node = FeatFactory.newNodeForId(existingInnov.getNewNodeId());
			newgene1 = FeatFactory.newGene(1.0, in_node, new_node, thelink.isRecurrent(), existingInnov.getInnovationNumber1(), 0);
			newgene2 = FeatFactory.newGene(oldweight, new_node, out_node, false, existingInnov.getInnovationNumber2(), 0);
		}

		// Now add the new NNode and new Genes to the Genome
		genes.add(newgene1);
		genes.add(newgene2);
		EvolutionUtils.nodeInsert(genome.getNodes(), new_node);

		return true;

	}

	/**
	 * Re-enables the first disabled gene in the genome.
	 * 
	 * TODO: Should this enable a random disabled gene?
	 * 
	 * @param genome
	 */
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