package org.gatech.feat.experiments.star;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.feat.api.ILink;
import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IOrganismEvaluator;

/**
 * Fitness for this experiment is based on the degree to which
 * an IOrganism's network structure matches a predefined target.
 * 
 * @author dtuohy
 *
 */
public class StarOrganismEvaluator implements IOrganismEvaluator {
	
	public StarOrganismEvaluator(){
		
	}
	
	@Override
	public boolean evaluate(IOrganism organism) {
				
		INetwork network = organism.getPhenotype();
		
		List<INode> nodes = new ArrayList<INode>();
		for(INode n : network.getAllNodes()) {
			nodes.add(n);
		}
		
		int targetNodes = 7;
		
		//adjust fitness for the number of nodes
		//  * targetNodes-n where n is difference from targetNodes
		//  * min is 0
		int numNodesFitness = targetNodes - Math.abs(nodes.size() - targetNodes);
		if(numNodesFitness<0)
			numNodesFitness = 0;
		
		int existingLinksFitness = 0;
		
		//target is a "star" configuration
		for(int i = 0;i<targetNodes&&i<nodes.size();i++){
			int priorFit = existingLinksFitness;
			for(int j = 0;j<nodes.size();j++){
				if(connectionExists(i,j,nodes)){
					if((j)==((i+3)%targetNodes) || j==((i+4)%targetNodes))
						existingLinksFitness++;
					else
						existingLinksFitness--;
				}
			}
			
			//color the nodes based on their individual fitness
			int thisNodesFit = existingLinksFitness - priorFit;
			if(thisNodesFit==2)
				((StarNode)nodes.get(i)).setColor(new Color(50,255,50));
			else if(thisNodesFit==1)
				((StarNode)nodes.get(i)).setColor(new Color(255,255,50));
			else
				((StarNode)nodes.get(i)).setColor(new Color(255,50,50));
		}
		for(int i = targetNodes;i<nodes.size();i++){
			existingLinksFitness -= nodes.get(i).getOutgoing().size();
			existingLinksFitness -= nodes.get(i).getIncoming().size();
		}
			
		double totalFitness = numNodesFitness + existingLinksFitness;
		organism.setFitness(totalFitness);
		return true;
	}

	private boolean connectionExists(int node1, int node2, List<INode> nodes) {
		if(nodes.size()<=Math.max(node1, node2))
			return false;
		
		INode n1 = nodes.get(node1);
		INode n2 = nodes.get(node2);
		
		for(ILink link : n1.getOutgoing())
			if(link.getOutputNode() == n2)
				return true;
		
		return false;
	}

}