package org.brainfarm.java.neat.operators;

import java.lang.reflect.Constructor;
import java.util.List;

import org.brainfarm.java.neat.EvolutionStrategy;
import org.brainfarm.java.neat.api.IGene;
import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.IOrganism;
import org.brainfarm.java.neat.api.operators.IFeatFactory;

/**
 * Uses reflection to create instances of the custom FEAT model object
 * implementations that are provided in the customizations package, or 
 * the defaults if there were none. 
 * 
 * @author dtuohy
 *
 */
public class FeatFactory implements IFeatFactory {

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
	
	public IOrganism createOrganism(double xfitness, IGenome xgenome, int xgeneration){
		try{
			Class<?> oClass = EvolutionStrategy.getInstance().getOrganismClass();
			Constructor<?> c = oClass.getConstructor(double.class,IGenome.class,int.class);
			return (IOrganism)c.newInstance(xfitness,xgenome,xgeneration);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;		
	}

}
