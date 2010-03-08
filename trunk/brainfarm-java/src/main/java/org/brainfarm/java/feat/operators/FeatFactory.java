package org.brainfarm.java.feat.operators;

import java.lang.reflect.Constructor;
import java.util.List;

import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IGene;
import org.brainfarm.java.feat.api.IGenome;
import org.brainfarm.java.feat.api.IInnovation;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ISpecies;

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

	public static ISpecies newSpecies(IPopulation pop) {
		try{
			Class<?> oClass = evolutionStrategy.getSpeciesClass();
			Constructor<?> c = oClass.getConstructor(int.class);
			ISpecies species = (ISpecies)c.newInstance(pop.getLastSpecies());
			pop.setLastSpecies(pop.getLastSpecies()+1);
			return species;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;	
	}
	
	public static IGene newGene(double weight, INode inputNode, INode outputNode, boolean recurrent, double innovationNumber, double mutationNumber) {
		try{
			Class<?> oClass = evolutionStrategy.getGeneClass();
			Constructor<?> c = oClass.getConstructor(double.class, INode.class, INode.class, boolean.class, double.class, double.class);
			return (IGene)c.newInstance(weight, inputNode, outputNode, recurrent, innovationNumber, mutationNumber);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;	
	}
	
	public static IInnovation newSingleInnovation(int nin, int nout, double num1, double w) {
		try{
			Class<?> oClass = evolutionStrategy.getInnovationClass();
			Constructor<?> c = oClass.getConstructor(int.class, int.class, double.class, double.class);
			return (IInnovation)c.newInstance(nin, nout, num1, w);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;	
	}
	
	public static IInnovation newDoubleInnovation(int nin, int nout, double num1, double num2, int newid, double oldinnov) {
		try{
			Class<?> oClass = evolutionStrategy.getInnovationClass();
			Constructor<?> c = oClass.getConstructor(int.class, int.class, double.class, double.class, int.class, double.class);
			return (IInnovation)c.newInstance(nin, nout, num1, num2, newid, oldinnov);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;	
	}
}
