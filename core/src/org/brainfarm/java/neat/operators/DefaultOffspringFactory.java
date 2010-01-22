package org.brainfarm.java.neat.operators;

import java.lang.reflect.Constructor;
import java.util.List;

import org.brainfarm.java.neat.EvolutionStrategy;
import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.operators.IOffspringFactory;

/**
 * Uses reflection to create instances of whichever IGenome and INode
 * implementations were provided in the customizations package, or 
 * the default Node and Genome if there were none.
 * 
 * @author dtuohy
 *
 */
public class DefaultOffspringFactory implements IOffspringFactory {

	@Override
	public IGenome createOffspringGenome(int newId, List<INode> nodes, List<IGene> genes) {
		try{
			Class<?> gClass = EvolutionStrategy.getInstance().getGenomeClass();
			Constructor<?> c = gClass.getConstructor(int.class,List.class,List.class);
			return (IGenome)c.newInstance(newId,nodes,genes);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public INode createOffspringNodeFrom(INode node) {
		try{
			Class<?> nClass = EvolutionStrategy.getInstance().getNodeClass();
			Constructor<?> c = nClass.getConstructor(INode.class);
			return (INode)c.newInstance(node);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public INode createNewNodeForId(int id) {
		try{
			Class<?> nClass = EvolutionStrategy.getInstance().getNodeClass();
			Constructor<?> c = nClass.getConstructor(int.class);
			return (INode)c.newInstance(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
