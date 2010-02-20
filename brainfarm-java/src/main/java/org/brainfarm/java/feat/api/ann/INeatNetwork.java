package org.brainfarm.java.feat.api.ann;

import java.util.List;

import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;

/**
 * This interface adds methods required by networks representing
 * conventional NEAT networks (ANNs), but not required by generic
 * FEAT networks.
 * 
 * @author dtuohy
 *
 */
public interface INeatNetwork extends INetwork{

	boolean activate();

	void loadSensors(double[] sensvals);
	
	void flush();
	
	boolean isMinimal();
	
	List<INode> getInputs();
	
	List<INode> getOutputs();
	
	int isStabilised(int period);
	
	public int maxDepth();
}