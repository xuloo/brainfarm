package org.brainfarm.java.neat.operators;

import java.util.List;

import org.brainfarm.java.neat.Genome;
import org.brainfarm.java.neat.Node;
import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.operators.IOffspringFactory;

public class DefaultOffspringFactory implements IOffspringFactory {

	@Override
	public IGenome createOffspringGenome(int newId, List<INode> nodes,
			List<IGene> genes) {
		return new Genome(newId,nodes,genes);
	}

	@Override
	public INode createOffspringNodeFrom(INode node) {
		return new Node(node);
	}

}
