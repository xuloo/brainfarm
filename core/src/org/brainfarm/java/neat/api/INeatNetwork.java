package org.brainfarm.java.neat.api;

import java.util.List;

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
	
	boolean isRecurrent(INode potin_node, INode potout_node, int level, int thresh);

	List<INode> getInputs();
	
	List<INode> getOutputs();
	
}