package org.brainfarm.java.neat.api.operators;

import java.util.List;

import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.INode;

public interface IOffspringFactory {

	public INode createOffspringNodeFrom(INode node);
	
	public INode createNewNodeForId(int id);
	
	public IGenome createOffspringGenome(int newId, List<INode> nodes, List<IGene> genes);
	
}
