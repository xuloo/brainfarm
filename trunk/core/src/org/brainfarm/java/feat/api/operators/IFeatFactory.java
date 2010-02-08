package org.brainfarm.java.feat.api.operators;

import java.util.List;

import org.brainfarm.java.feat.api.IGene;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;
import org.brainfarm.java.feat.api.IOrganism;

public interface IFeatFactory {

	public INode createOffspringNodeFrom(INode node);
	
	public INode createNewNodeForId(int id);
	
	public IGenome createOffspringGenome(int newId, List<INode> nodes, List<IGene> genes);
	
	public IOrganism createOrganism(double xfitness, IGenome xgenome, int xgeneration);

	public INetwork createNetwork(List<INode> allList, int id);

}
