package org.brainfarm.java.feat.operators;

import java.lang.reflect.Constructor;
import java.util.List;

import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IGene;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;
import org.brainfarm.java.feat.api.IOrganism;

/**
 * Uses reflection to create instances of the custom FEAT model object
 * implementations that are provided in the customizations package, or 
 * the defaults if there were none. 
 * 
 * @author dtuohy
 *
 */
public class FeatFactory {

	private static IEvolutionStrategy evolutionStrategy;
	
	public static void setEvolutionStrategy(IEvolutionStrategy strategy) {
		evolutionStrategy = strategy;
	}
	
	public static IGenome newOffspringGenome(int newId, List<INode> nodes, List<IGene> genes) {

		try{
			Class<?> gClass = evolutionStrategy.getGenomeClass();
			Constructor<?> c = gClass.getConstructor(int.class,List.class,List.class);
			IGenome genome = (IGenome)c.newInstance(newId,nodes,genes);
			genome.setEvolutionParameters(evolutionStrategy.getEvolutionParameters());
			return genome;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static INode newOffspringNodeFrom(INode node) {
		try{
			Class<?> nClass = evolutionStrategy.getNodeClass();
			Constructor<?> c = nClass.getConstructor(INode.class);
			return (INode)c.newInstance(node);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static INode newNodeForId(int id) {
		try{
			Class<?> nClass = evolutionStrategy.getNodeClass();
			Constructor<?> c = nClass.getConstructor(int.class);
			return (INode)c.newInstance(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static IOrganism newOrganism(double xfitness, IGenome xgenome, int xgeneration){
		try{
			Class<?> oClass = evolutionStrategy.getOrganismClass();
			Constructor<?> c = oClass.getConstructor(double.class,IGenome.class,int.class);
			return (IOrganism)c.newInstance(xfitness,xgenome,xgeneration);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;		
	}
	
	public static INetwork newNetwork(List<INode> allList, int id) {
		try{
			Class<?> oClass = evolutionStrategy.getNetworkClass();
			Constructor<?> c = oClass.getConstructor(List.class,int.class);
			return (INetwork)c.newInstance(allList,id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;	
	}
}
