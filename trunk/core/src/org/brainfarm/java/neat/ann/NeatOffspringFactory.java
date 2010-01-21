package org.brainfarm.java.neat.ann;

import java.util.List;

import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.operators.IOffspringFactory;

public class NeatOffspringFactory implements IOffspringFactory{

	@Override
	public IGenome createOffspringGenome(int newId, List<INode> nodes, List<IGene> genes) {
		return new NeatGenome(newId,nodes,genes);
	}

	@Override
	public INode createOffspringNodeFrom(INode node) {
		return new NeatNode(node);
	}

}
