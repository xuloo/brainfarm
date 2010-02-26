package org.brainfarm.java.feat.operators;

import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.feat.Gene;
import org.brainfarm.java.feat.api.IGene;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.INode;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.feat.api.params.IEvolutionParameters;
import org.brainfarm.java.util.EvolutionUtils;
import org.brainfarm.java.util.RandomUtils;

/**
 * This is the default crossover strategy originally provided by JNeat, it supplies
 * multipoint, singlepoint and averaged multipoint crossover.
 * 
 * The "multipoint" Xover methods, which were those originally specified
 * in Ken's dissertation, have been merged into one and cleaned up
 * extensively.  The "singlepoint" method could use some cleanup.
 * 
 * @author dtuohy, orig. Ugo Vierucci
 *
 */
public class DefaultCrossoverStrategy implements ICrossoverStrategy{

	protected IEvolutionParameters evolutionParameters;
	
	public DefaultCrossoverStrategy(IEvolutionParameters evolutionParameters) {
		this.evolutionParameters = evolutionParameters;
	}

	public IGenome performCrossover(IOrganism mom, IOrganism dad, int count) {
		IGenome child_genome;

		//straight multi-point crossover
		if (RandomUtils.randomDouble() < evolutionParameters.getDoubleParameter(MATE_MULTIPOINT_PROB))
			child_genome = mateMultipoint(mom.getGenome(), dad.getGenome(), count, mom.getOriginalFitness(), dad.getOriginalFitness(), false);

		//average multi-point crossover
		else if (RandomUtils.randomDouble() < (evolutionParameters.getDoubleParameter(MATE_MULTIPOINT_AVG_PROB) / (evolutionParameters.getDoubleParameter(MATE_MULTIPOINT_AVG_PROB) + evolutionParameters.getDoubleParameter(MATE_SINGLEPOINT_PROB))))
			child_genome = mateMultipoint(mom.getGenome(), dad.getGenome(), count, mom.getOriginalFitness(), dad.getOriginalFitness(), true);

		//single point crossover
		else
			child_genome = mateSinglepoint(mom.getGenome(), dad.getGenome(), count);
		return child_genome;
	}

	/**
	 * Performs multi-point crossover between two genomes.  The boolean
	 * 'avg' determines whether nodes come straight from the parents
	 * or are averaged.
	 * @return the child IGenome
	 */
	public IGenome mateMultipoint(IGenome mom, IGenome dad, int id, double momFitness, double dadFitness, boolean avg) {

		int momSize = mom.getGenes().size();
		int dadSize = dad.getGenes().size();

		// Figure out which genome is better. The worse genome should not be 
		// allowed to add extra structural baggage. If they are the same, use 
		// the smaller one's disjoint and excess genes only.
		boolean momIsMoreFit = false;
		if (momFitness > dadFitness)
			momIsMoreFit = true;
		else if (momFitness == dadFitness && momSize < dadSize)
			momIsMoreFit = true;

		//allocate lists for new genes and nodes
		List<IGene> newgenes = new ArrayList<IGene>(Math.max(momSize, dadSize));
		List<INode> newnodes = new ArrayList<INode>(mom.getNodes().size());

		int momInc = 0;
		int dadInc = 0;

		//proceed across genomes
		while (momInc < momSize || dadInc < dadSize)
		{
			//choose the correct gene from mom or dad (or average the two)
			boolean disable = false;
			boolean skip = false; // Default to not skipping a chosen gene
			IGene chosengene = null;
			if (momInc >= momSize) {
				chosengene = dad.getGenes().get(dadInc);
				dadInc++;
				if (momIsMoreFit)
					skip = true; // Skip excess from the worse genome
			} else if (dadInc >= dadSize) {
				chosengene = (Gene) mom.getGenes().get(momInc);
				momInc++;
				if (!momIsMoreFit)
					skip = true; // Skip excess from the worse genome
			} else {
				IGene momGene = mom.getGenes().get(momInc);
				IGene dadGene = dad.getGenes().get(dadInc);
				double momInnov = momGene.getInnovationNumber();
				double dadInnov = dadGene.getInnovationNumber();
				
				if (momInnov == dadInnov) {

					//average the two genes or...
					if(avg)
						chosengene = computeAverageGene(momGene, dadGene);

					//...copy from one parent
					else{
						if (RandomUtils.randomDouble() < 0.5)
							chosengene = momGene;
						else
							chosengene = dadGene;
					}

					// If one is disabled, the corresponding gene in the offspring will likely be disabled
					// TODO: If we're not averaging, shouldn't we just take "disable" from the chosen gene? 
					disable = false;
					if ((momGene.isEnabled() == false) || (dadGene.isEnabled() == false)) 
						if (RandomUtils.randomDouble() < 0.75) 
							disable = true;

					momInc++;
					dadInc++;
				} 

				//disjoint genes
				else if (momInnov < dadInnov) {
					chosengene = momGene;
					momInc++;
					if (!momIsMoreFit)
						skip = true;
				} else if (dadInnov < momInnov) {
					chosengene = dadGene;
					dadInc++;
					if (momIsMoreFit)
						skip = true;
				}
			} // gene is chosen

			// Check to see if the chosengene conflicts with an already chosen
			// gene (i.e. do they represent the same link?)
			for (IGene _curgene2 : newgenes) 
				if(chosengene.sameAs(_curgene2)){
					skip = true;
					break;
				}

			//add the gene and, if not already in child, the nodes
			if (!skip) {

				// Check the nodes, add them if not in the child Genome already
				INode parent_inode = chosengene.getLink().getInputNode();
				INode parent_onode = chosengene.getLink().getOutputNode();

				INode child_inode = null;
				INode child_onode = null;

				//find the child input node
				for (INode curnode : newnodes) 
					if (curnode.getId() == parent_inode.getId()) {
						child_inode = curnode;
						break;
					}

				//find the child output node
				for (INode curnode : newnodes) 
					if (curnode.getId() == parent_onode.getId()) {
						child_onode = curnode;
						break;
					}

				//create and insert the input and output nodes if they don't exist
				if (parent_inode.getId() < parent_onode.getId()) {
					child_inode = createAndAddIfNull(newnodes,parent_inode,child_inode);
					child_onode = createAndAddIfNull(newnodes,parent_onode,child_onode);
				}
				else {
					child_onode = createAndAddIfNull(newnodes,parent_onode,child_onode);
					child_inode = createAndAddIfNull(newnodes,parent_inode,child_inode);
				}

				// Add the Gene
				IGene newgene = new Gene(chosengene, child_inode, child_onode);
				if (disable)
					newgene.setEnabled(false);
				newgenes.add(newgene);
			}
		}
		return FeatFactory.newOffspringGenome(id, newnodes, newgenes);
	}

	private INode createAndAddIfNull(List<INode> newnodes, INode parent_node, INode child_node) {
		if(child_node == null){
			child_node = FeatFactory.newOffspringNodeFrom(parent_node);
			EvolutionUtils.nodeInsert(newnodes, child_node);
		}
		return child_node;
	}

	/**
	 * Averages the link weights between 2 genes, chooses random
	 * input and output nodes from one of the genes.
	 * @return the averaged IGene
	 */
	private IGene computeAverageGene(IGene momGene, IGene dadGene) {
		IGene avgene = new Gene(0.0, null, null, false, 0.0, 0.0);
		avgene.setEnabled(true); // Default to enabled

		// WEIGHTS AVERAGED HERE
		avgene.getLink().setWeight((momGene.getLink().getWeight() + dadGene.getLink().getWeight()) / 2.0);

		if (RandomUtils.randomDouble() > 0.5)
			avgene.getLink().setInputNode(momGene.getLink().getInputNode(),false);
		else
			avgene.getLink().setInputNode(dadGene.getLink().getInputNode(),false);

		if (RandomUtils.randomDouble() > 0.5)
			avgene.getLink().setOutputNode(momGene.getLink().getOutputNode(),false);
		else
			avgene.getLink().setOutputNode(dadGene.getLink().getOutputNode(),false);

		if (RandomUtils.randomDouble() > 0.5)
			avgene.getLink().setRecurrent(momGene.getLink().isRecurrent());
		else
			avgene.getLink().setRecurrent(dadGene.getLink().isRecurrent());

		avgene.setInnovationNumber(momGene.getInnovationNumber());
		avgene.setMutationNumber(momGene.getMutationNumber() + dadGene.getMutationNumber() / 2.0);

		return avgene;
	}


	/**
	 * Performs single-point crossover.
	 * 
	 * TODO: Clean this up, preferably pulling structure from the multi-point method.
	 * @return the child IGenome
	 */
	public IGenome mateSinglepoint(IGenome mom, IGenome dad, int id) {

		List<IGene> genes = mom.getGenes();
		List<INode> nodes = mom.getNodes();

		IGenome new_genome = null;
		IGene chosengene = null;
		int stopA = 0;
		int stopB = 0;
		int j;
		int j1;
		int j2;

		int size1 = genes.size();
		int size2 = dad.getGenes().size();
		int crosspoint = 0;
		//int len_genome = 0;

		INode curnode = null;
		INode inode = null;
		INode onode = null;
		INode new_inode = null;
		INode new_onode = null;
		IGene newgene = null;

		List<IGene> genomeA;
		List<IGene> genomeB;
		IGene geneA = null;
		IGene geneB = null;

		int genecounter = 0; // Ready to count to crosspoint
		boolean skip = false; // Default to not skip a Gene

		ArrayList<IGene> newgenes = new ArrayList<IGene>(genes.size());
		ArrayList<INode> newnodes = new ArrayList<INode>(nodes.size());

		if (size1 < size2) {
			crosspoint = RandomUtils.randomInt(0, size1 - 1);
			stopA = size1;
			stopB = size2;
			//len_genome = size2;
			genomeA = genes;
			genomeB = dad.getGenes();
		} else {
			crosspoint = RandomUtils.randomInt(0, size2 - 1);
			stopA = size2;
			stopB = size1;
			//len_genome = size1;
			genomeA = dad.getGenes();
			genomeB = genes;
		}

		// System.out.print("\n crossing point is :"+crosspoint);

		genecounter = 0;

		boolean doneA = false;
		boolean doneB = false;
		boolean done = false;
		double v1 = 0.0;
		double v2 = 0.0;
		//double vmax = 0.0;
		double cellA = 0.0;
		double cellB = 0.0;

		j1 = 0;
		j2 = 0;
		j = 0;

		//
		// compute what is the hight innovation
		//

		double last_innovB = genomeB.get(stopB - 1).getInnovationNumber();
		double cross_innov = 0;

		while (!done) {

			doneA = false;
			doneB = false;
			skip = false;

			if (j1 < stopA) {
				geneA = genomeA.get(j1);
				v1 = geneA.getInnovationNumber();
				doneA = true;
			}
			if (j2 < stopB) {
				geneB = genomeB.get(j2);
				v2 = geneB.getInnovationNumber();
				doneB = true;
			}

			if (doneA && doneB) {
				//
				if (v1 < v2) {
					cellA = v1;
					cellB = 0.0;
					j1++;
				} else if (v1 == v2) {
					cellA = v1;
					cellB = v1;
					j1++;
					j2++;
				} else {
					cellA = 0.0;
					cellB = v2;
					j2++;
				}
			}

			else {
				if (doneA && !doneB) {
					cellA = v1;
					cellB = 0.0;
					j1++;
				} else if (!doneA && doneB) {
					cellA = 0.0;
					cellB = v2;
					j2++;
				} else
					done = true;
			}

			if (!done) {

				// -------------------------------------------------------------------------------
				// innovA = innovB
				// -------------------------------------------------------------------------------

				if (cellA == cellB) {
					if (genecounter < crosspoint) {
						chosengene = geneA;
						genecounter++;
					} else if (genecounter == crosspoint) {

						IGene avgene = new Gene(0.0, null, null, false, 0.0, 0.0);
						avgene.setEnabled(true); // Default to true
						// WEIGHTS AVERAGED HERE
						avgene.getLink().setWeight((geneA.getLink().getWeight() + geneB
								.getLink().getWeight()) / 2.0);

						if (RandomUtils.randomDouble() > 0.5)
							avgene.getLink().setInputNode(geneA.getLink().getInputNode(),false);
						else
							avgene.getLink().setInputNode(geneB.getLink().getInputNode(),false);

						if (RandomUtils.randomDouble() > 0.5)
							avgene.getLink().setOutputNode(geneA.getLink().getOutputNode(),false);
						else
							avgene.getLink().setOutputNode(geneB.getLink().getOutputNode(),false);

						if (RandomUtils.randomDouble() > 0.5)
							avgene.getLink().setRecurrent(geneA.getLink().isRecurrent());
						else
							avgene.getLink().setRecurrent(geneB.getLink().isRecurrent());

						avgene.setInnovationNumber(geneA.getInnovationNumber());
						avgene
						.setMutationNumber((geneA.getMutationNumber() + geneB
								.getMutationNumber()) / 2.0);

						// If one is disabled, the corresponding gene in the
						// offspring
						// will likely be disabled

						if ((geneA.isEnabled() == false)
								|| (geneB.isEnabled() == false))
							avgene.setEnabled(false);

						chosengene = avgene;
						genecounter++;
						cross_innov = cellA;
					} else if (genecounter > crosspoint) {
						chosengene = geneB;
						genecounter++;
					}
				}

				// -------------------------------------------------------------------------------
				// innovA < innovB
				// -------------------------------------------------------------------------------

				else if (cellA != 0 && cellB == 0) {
					if (genecounter < crosspoint) {
						chosengene = geneA; // make geneA
						genecounter++;
					} else if (genecounter == crosspoint) {
						chosengene = geneA;
						genecounter++;
						cross_innov = cellA;
					} else if (genecounter > crosspoint) {
						if (cross_innov > last_innovB) {
							chosengene = geneA;
							genecounter++;
						} else {
							skip = true;
						}
					}
				}

				// -------------------------------------------------------------------------------
				// innovA > innovB
				// -------------------------------------------------------------------------------

				else {
					if (cellA == 0 && cellB != 0) {
						if (genecounter < crosspoint) {
							skip = true; // skip geneB
						} else if (genecounter == crosspoint) {
							skip = true; // skip an illogic case
						} else if (genecounter > crosspoint) {
							if (cross_innov > last_innovB) {
								chosengene = geneA; // make geneA
								genecounter++;
							} else {
								chosengene = geneB; // make geneB : this is a
								// pure case o single
								// crossing
								genecounter++;
							}
						}

					}
				}

				for (IGene _curgene2 : newgenes) {

					if(chosengene.sameAs(_curgene2)){
						skip = true;
						break;
					}
				} // and else for control of position in gennomeA/B

				if (!skip) {

					// Next check for the nodes, add them if not in the child
					// Genome already

					inode = chosengene.getLink().getInputNode();
					onode = chosengene.getLink().getOutputNode();

					//
					// Check for inode, onode in the newnodes list
					//
					boolean found;
					if (inode.getId() < onode.getId()) {
						//
						// search the inode
						// 
						found = false;
						for (int ix = 0; ix < newnodes.size(); ix++) {
							curnode = newnodes.get(ix);
							if (curnode.getId() == inode.getId()) {
								found = true;
								break;
							}
						}
						// if exist , point to existting version
						if (found)
							new_inode = curnode;
						// else create the inode
						else {
							new_inode = FeatFactory.newOffspringNodeFrom(inode);

							// insert in newnodes list
							EvolutionUtils.nodeInsert(newnodes, new_inode);
						}

						//
						// search the onode
						// 
						found = false;
						for (int ix = 0; ix < newnodes.size(); ix++) {
							curnode = newnodes.get(ix);
							if (curnode.getId() == onode.getId()) {
								found = true;
								break;
							}
						}
						// if exist , point to existting version
						if (found)
							new_onode = curnode;
						// else create the onode
						else {
							new_onode = FeatFactory.newOffspringNodeFrom(onode);

							// insert in newnodes list
							EvolutionUtils.nodeInsert(newnodes, new_onode);
						}
					} // end block : inode.node_id < onode.node_id

					else {
						//
						// search the onode
						// 
						found = false;
						for (int ix = 0; ix < newnodes.size(); ix++) {
							curnode = newnodes.get(ix);
							if (curnode.getId() == onode.getId()) {
								found = true;
								break;
							}
						}
						// if exist , point to existting version
						if (found)
							new_onode = curnode;
						// else create the onode
						else {
							new_onode = FeatFactory.newOffspringNodeFrom(onode);
							// insert in newnodes list
							EvolutionUtils.nodeInsert(newnodes, new_onode);
						}
						//
						// search the inode
						// 
						found = false;
						for (int ix = 0; ix < newnodes.size(); ix++) {
							curnode = newnodes.get(ix);
							if (curnode.getId() == inode.getId()) {
								found = true;
								break;
							}

						}

						// if exist , point to existting version
						if (found)
							new_inode = curnode;
						// else create the inode
						else {
							new_inode = FeatFactory.newOffspringNodeFrom(inode);

							// insert in newnodes list
							EvolutionUtils.nodeInsert(newnodes, new_inode);
						}
					}

					// Add the Gene
					newgene = new Gene(chosengene,new_inode, new_onode);
					newgenes.add(newgene);

				} // end of block gene creation if !skip

			}
			j++;

		}

		new_genome = FeatFactory.newOffspringGenome(id, newnodes, newgenes);
		return new_genome;

	}
}