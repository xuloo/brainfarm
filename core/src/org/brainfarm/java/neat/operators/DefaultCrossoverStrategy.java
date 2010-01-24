package org.brainfarm.java.neat.operators;

import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.neat.EvolutionStrategy;
import org.brainfarm.java.neat.Gene;
import org.brainfarm.java.neat.Neat;
import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.IOrganism;
import org.brainfarm.java.neat.api.operators.ICrossoverStrategy;
import org.brainfarm.java.neat.api.operators.IFeatFactory;
import org.brainfarm.java.util.EvolutionUtils;
import org.brainfarm.java.util.RandomUtils;

/**
 * This is the default crossover strategy originally provided by JNeat.
 * 
 * @author dtuohy, orig. Ugo Vierucci
 *
 */
public class DefaultCrossoverStrategy implements ICrossoverStrategy{

	IFeatFactory offspringFact;
	
	public IGenome performCrossover(IOrganism mom, IOrganism dad, int count) {
		offspringFact = EvolutionStrategy.getInstance().getModelObjectFactory();
		IGenome new_genome;
		if (RandomUtils.randomDouble() < Neat.mate_multipoint_prob) {
			//			logger.debug("mate multipoint baby: ");
			new_genome = mateMultipoint(mom.getGenome(), dad.getGenome(), count, mom.getOriginalFitness(), dad.getOriginalFitness());
		} else if (RandomUtils.randomDouble() < (Neat.mate_multipoint_avg_prob / (Neat.mate_multipoint_avg_prob + Neat.mate_singlepoint_prob))) {
			//			logger.debug("mate multipoint_avg baby: ");
			new_genome = mateMultipointAverage(mom.getGenome(), dad.getGenome(), count, mom.getOriginalFitness(), dad.getOriginalFitness());
		} else {
			//			logger.debug("mate siglepoint baby: ");
			new_genome = mateSinglepoint(mom.getGenome(), dad.getGenome(), count);
		}
		return new_genome;
	}

	public IGenome mateMultipoint(IGenome mom, IGenome dad, int id, double fitness1, double fitness2) {

		//get fields from mom
		List<IGene> momGenes = mom.getGenes();
		List<INode> momNodes = mom.getNodes();

		IGenome new_genome = null;
		boolean disable = false; // Set to true if we want to disabled a chosen gene.

		INode curnode = null;

		IGene chosengene = null;
		IGene _p1gene = null;
		IGene _p2gene = null;
		double p1innov = 0;
		double p2innov = 0;

		int j1;
		int j2;

		// Tells if the first genome (this one) has better fitness or not
		boolean skip = false;

		// Figure out which genome is better.
		// The worse genome should not be allowed to add extra structural baggage.
		// If they are the same, use the smaller one's disjoint and excess genes only.

		boolean p1better = false;

		int size1 = momGenes.size();
		int size2 = dad.getGenes().size();

		if (fitness1 > fitness2)
			p1better = true;

		else if (fitness1 == fitness2) {
			if (size1 < size2)
				p1better = true;
		}

		int len_genome = Math.max(size1, size2);
		int len_nodes = momNodes.size();

		ArrayList<IGene> newgenes = new ArrayList<IGene>(len_genome);
		ArrayList<INode> newnodes = new ArrayList<INode>(len_nodes);

		j1 = 0;
		j2 = 0;

		int control_disable = 0;
		int exist_disable = 0;

		while (j1 < size1 || j2 < size2) {

			IGene newgene = null;

			// chosen of 'just' gene	

			skip = false; // Default to not skipping a chosen gene
			if (j1 >= size1) {
				chosengene = dad.getGenes().get(j2);
				j2++;
				if (p1better)
					skip = true; // Skip excess from the worse genome
			} else if (j2 >= size2) {
				chosengene = momGenes.get(j1);
				j1++;
				if (!p1better)
					skip = true; // Skip excess from the worse genome
			} else {

				_p1gene = momGenes.get(j1);
				_p2gene = dad.getGenes().get(j2);

				p1innov = _p1gene.getInnovationNumber();
				p2innov = _p2gene.getInnovationNumber();

				if (p1innov == p2innov) {
					if (RandomUtils.randomDouble() < 0.5)
						chosengene = _p1gene;
					else
						chosengene = _p2gene;

					// If one is disabled, the corresponding gene in the
					// offspring
					// will likely be disabled
					disable = false;
					if ((_p1gene.isEnabled() == false)
							|| (_p2gene.isEnabled() == false)) {
						exist_disable++;
						if (RandomUtils.randomDouble() < 0.75) {
							disable = true;
							control_disable++;
						}
					}
					j1++;
					j2++;

				} else if (p1innov < p2innov) {
					chosengene = _p1gene;
					j1++;
					if (!p1better)
						skip = true;
				} else if (p2innov < p1innov) {
					chosengene = _p2gene;
					j2++;
					if (p1better)
						skip = true;
				}
			}// end chosen gene

			// Check to see if the chosen gene conflicts with an already chosen gene.
			// i.e. do they represent the same link.

			for (IGene _curgene2 : newgenes) {

				if (_curgene2.getLink().getInputNode().getId() 	== chosengene.getLink().getInputNode().getId() && 
						_curgene2.getLink().getOutputNode().getId() == chosengene.getLink().getOutputNode().getId() && 
						_curgene2.getLink().isRecurrent() 			== chosengene.getLink().isRecurrent()) {
					skip = true;
					break;
				}

				if (_curgene2.getLink().getInputNode().getId() == chosengene.getLink().getOutputNode().getId() && 
						_curgene2.getLink().getOutputNode().getId() == chosengene.getLink().getInputNode().getId() && 
						!_curgene2.getLink().isRecurrent() && 
						!chosengene.getLink().isRecurrent()) {
					skip = true;
					break;
				}
			}

			if (!skip) {

				INode new_inode = null;
				INode new_onode = null;

				// Next check for the nodes, add them if not in the baby Genome already.
				INode inode = chosengene.getLink().getInputNode();
				INode onode = chosengene.getLink().getOutputNode();

				// --------------------------------------------------------------------------------
				boolean found;
				if (inode.getId() < onode.getId()) {

					// search the inode
					found = false;
					for (int ix = 0; ix < newnodes.size(); ix++) {
						curnode = newnodes.get(ix);
						if (curnode.getId() == inode.getId()) {
							found = true;
							break;
						}

					}

					// if exist , point to exitsting version
					if (found)
						new_inode = curnode;

					// else create the inode
					else {
						new_inode = offspringFact.createOffspringNodeFrom(inode);

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

					// if exist , point to exitsting version
					if (found)
						new_onode = curnode;

					// else create the onode
					else {
						new_onode = offspringFact.createOffspringNodeFrom(onode);

						// insert in newnodes list
						EvolutionUtils.nodeInsert(newnodes, new_onode);
					}

				} // end block : inode.node_id < onode.node_id

				else {

					// search the onode
					found = false;
					for (int ix = 0; ix < newnodes.size(); ix++) {
						curnode = newnodes.get(ix);
						if (curnode.getId() == onode.getId()) {
							found = true;
							break;
						}

					}

					// if exist , point to exitsting version
					if (found)
						new_onode = curnode;

					// else create the onode
					else {
						new_onode = offspringFact.createOffspringNodeFrom(onode);

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

					// if exist , point to exitsting version
					if (found)
						new_inode = curnode;

					// else create the inode
					else {
						new_inode = offspringFact.createOffspringNodeFrom(inode);

						// insert in newnodes list
						EvolutionUtils.nodeInsert(newnodes, new_inode);
					}

				}

				// --------------------------------------------------------------------------------

				// Add the Gene
				newgene = new Gene(chosengene, new_inode, new_onode);
				if (disable) {
					newgene.setEnabled(false);
					disable = false;
				}
				newgenes.add(newgene);
			}

		} // end block genome (while)

		new_genome = offspringFact.createOffspringGenome(id, newnodes, newgenes);
		return new_genome;
	}


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

		// Set up the avgene
		IGene avgene = new Gene(0.0, null, null, false, 0.0, 0.0);

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
			avgene.setEnabled(true); // Default to true

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
						
						// WEIGHTS AVERAGED HERE
						avgene.getLink().setWeight((geneA.getLink().getWeight() + geneB
								.getLink().getWeight()) / 2.0);

						if (RandomUtils.randomDouble() > 0.5)
							avgene.getLink().setInputNode(geneA.getLink().getInputNode());
						else
							avgene.getLink().setInputNode(geneB.getLink().getInputNode());

						if (RandomUtils.randomDouble() > 0.5)
							avgene.getLink().setOutputNode(geneA.getLink().getOutputNode());
						else
							avgene.getLink().setOutputNode(geneB.getLink().getOutputNode());

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

					if (_curgene2.getLink().getInputNode().getId() == chosengene
							.getLink().getInputNode().getId()
							&& _curgene2.getLink().getOutputNode().getId() == chosengene
							.getLink().getOutputNode().getId()
							&& _curgene2.getLink().isRecurrent() == chosengene
							.getLink().isRecurrent()) {
						skip = true;
						break;
					}

					if (_curgene2.getLink().getInputNode().getId() == chosengene
							.getLink().getOutputNode().getId()
							&& _curgene2.getLink().getOutputNode().getId() == chosengene
							.getLink().getInputNode().getId()
							&& !_curgene2.getLink().isRecurrent()
							&& !chosengene.getLink().isRecurrent()) {
						skip = true;
						break;
					}

				} // and else for control of position in gennomeA/B

				if (!skip) {

					// Next check for the nodes, add them if not in the baby
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
						// if exist , point to exitsting version
						if (found)
							new_inode = curnode;
						// else create the inode
						else {
							new_inode = offspringFact.createOffspringNodeFrom(inode);

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
						// if exist , point to exitsting version
						if (found)
							new_onode = curnode;
						// else create the onode
						else {
							new_onode = offspringFact.createOffspringNodeFrom(onode);

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
						// if exist , point to exitsting version
						if (found)
							new_onode = curnode;
						// else create the onode
						else {
							new_onode = offspringFact.createOffspringNodeFrom(onode);
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

						// if exist , point to exitsting version
						if (found)
							new_inode = curnode;
						// else create the inode
						else {
							new_inode = offspringFact.createOffspringNodeFrom(inode);

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

		new_genome = offspringFact.createOffspringGenome(id, newnodes, newgenes);
		return new_genome;

	}
	
	public IGenome mateMultipointAverage(IGenome mom, IGenome g, int id, double fitness1, double fitness2) {

		//get fields from mom
		List<IGene> genes = mom.getGenes();
		List<INode> nodes = mom.getNodes();
		
		IGenome new_genome = null;
		boolean disable = false; // Set to true if we want to disabled a chosen gene.

		int control_disable = 0;
		int exist_disable = 0;

		IGene chosengene = null;
		IGene newgene = null;
		INode inode = null;
		INode onode = null;
		INode new_inode = null;
		INode new_onode = null;
		INode curnode = null;

		IGene _p1gene = null;
		IGene _p2gene = null;
		double p1innov = 0;
		double p2innov = 0;

		int j1;
		int j2;
		boolean skip = false;

		// Set up the avgene
		IGene avgene = new Gene(0.0, null, null, false, 0.0, 0.0);

		// Figure out which genome is better. The worse genome should not be 
		// allowed to add extra structural baggage. If they are the same, use 
		// the smaller one's disjoint and excess genes only.

		boolean p1better = false;
		int size1 = genes.size();
		int size2 = g.getGenes().size();

		if (fitness1 > fitness2)
			p1better = true;
		else if (fitness1 == fitness2) {
			if (size1 < size2)
				p1better = true;
		}
		int len_genome = Math.max(size1, size2);
		int len_nodes = nodes.size();

		List<IGene> newgenes = new ArrayList<IGene>(len_genome);
		List<INode> newnodes = new ArrayList<INode>(len_nodes);

		j1 = 0;
		j2 = 0;

		while (j1 < size1 || j2 < size2)

		// while (newgenes.size() < len_genome)

		{
			//
			// chosen of 'just' gene
			//
			avgene.setEnabled(true); // Default to enabled
			skip = false; // Default to not skipping a chosen gene

			if (j1 >= size1) {
				chosengene = g.getGenes().get(j2);
				j2++;
				if (p1better)
					skip = true; // Skip excess from the worse genome
			} else if (j2 >= size2) {
				chosengene = (Gene) genes.get(j1);
				j1++;
				if (!p1better)
					skip = true; // Skip excess from the worse genome
			} else {

				_p1gene = genes.get(j1);
				_p2gene = g.getGenes().get(j2);
				p1innov = _p1gene.getInnovationNumber();
				p2innov = _p2gene.getInnovationNumber();
				if (p1innov == p2innov) {

					// WEIGHTS AVERAGED HERE
					avgene.getLink().setWeight((_p1gene.getLink().getWeight() + _p2gene
							.getLink().getWeight()) / 2.0);

					if (RandomUtils.randomDouble() > 0.5)
						avgene.getLink().setInputNode(_p1gene.getLink().getInputNode());
					else
						avgene.getLink().setInputNode(_p2gene.getLink().getInputNode());

					if (RandomUtils.randomDouble() > 0.5)
						avgene.getLink().setOutputNode(_p1gene.getLink().getOutputNode());
					else
						avgene.getLink().setOutputNode(_p2gene.getLink().getOutputNode());

					if (RandomUtils.randomDouble() > 0.5)
						avgene.getLink().setRecurrent(_p1gene.getLink().isRecurrent());
					else
						avgene.getLink().setRecurrent(_p2gene.getLink().isRecurrent());

					avgene.setInnovationNumber(_p1gene.getInnovationNumber());
					avgene.setMutationNumber(_p1gene.getMutationNumber()
							+ _p2gene.getMutationNumber() / 2.0);

					// If one is disabled, the corresponding gene in the
					// offspring
					// will likely be disabled
					disable = false;
					if ((_p1gene.isEnabled() == false)
							|| (_p2gene.isEnabled() == false)) {
						exist_disable++;

						if (RandomUtils.randomDouble() < 0.75) {
							disable = true;
							control_disable++;
						}
					}

					chosengene = avgene;

					j1++;
					j2++;

				} else if (p1innov < p2innov) {
					chosengene = _p1gene;
					j1++;
					if (!p1better)
						skip = true;
				} else if (p2innov < p1innov) {
					chosengene = _p2gene;
					j2++;
					if (p1better)
						skip = true;
				}
			} // end chosen gene

			//
			//
			// Check to see if the chosengene conflicts with an already chosen
			// gene
			// i.e. do they represent the same link
			//

			for (IGene _curgene2 : newgenes) {

				if (_curgene2.getLink().getInputNode().getId() == chosengene.getLink().getInputNode()
						.getId()
						&& _curgene2.getLink().getOutputNode().getId() == chosengene
								.getLink().getOutputNode().getId()
						&& _curgene2.getLink().isRecurrent() == chosengene
								.getLink().isRecurrent()) {
					skip = true;
					break;
				}
				if (_curgene2.getLink().getInputNode().getId() == chosengene.getLink().getOutputNode()
						.getId()
						&& _curgene2.getLink().getOutputNode().getId() == chosengene
								.getLink().getInputNode().getId()
						&& !_curgene2.getLink().isRecurrent()
						&& !chosengene.getLink().isRecurrent()) {
					skip = true;
					break;
				}

			}

			//
			// 
			//

			if (!skip) {
				// Now add the chosengene to the baby

				// Next check for the nodes, add them if not in the baby Genome
				// already
				inode = chosengene.getLink().getInputNode();
				onode = chosengene.getLink().getOutputNode();

				// Check for inode in the newnodes list
				//

				// --------------------------------------------------------------------------------
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

					// if exist , point to exitsting version
					if (found)
						new_inode = curnode;

					// else create the inode
					else {
						new_inode = offspringFact.createOffspringNodeFrom(inode);
						
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

					// if exist , point to exitsting version
					if (found)
						new_onode = curnode;

					// else create the onode
					else {
						new_onode = offspringFact.createOffspringNodeFrom(onode);
						
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

					// if exist , point to exitsting version
					if (found)
						new_onode = curnode;

					// else create the onode
					else {
						new_onode = offspringFact.createOffspringNodeFrom(onode);
						
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

					// if exist , point to exitsting version
					if (found)
						new_inode = curnode;

					// else create the inode
					else {
						new_inode = offspringFact.createOffspringNodeFrom(inode);
						
						// insert in newnodes list
						EvolutionUtils.nodeInsert(newnodes, new_inode);
					}

				}

				// --------------------------------------------------------------------------------

				// Add the Gene
				newgene = new Gene(chosengene, new_inode, new_onode);
				if (disable) {
					newgene.setEnabled(false);
					disable = false;
				}
				newgenes.add(newgene);

			}

		} // end block genome

		new_genome = offspringFact.createOffspringGenome(id, newnodes, newgenes);
		return new_genome;
	}
}