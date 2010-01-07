package org.brainfarm.java.neat.api;

import java.util.List;

public interface INetwork {
	
	int getId();
	void setId(int id);

	boolean pathExists(INode potin, INode potout, int level, int threshold);
	
	boolean isMinimal();
	
	int maxDepth();
	
	int isStabilised(int period);
	
	void setGenotype(IGenome genome);
	
	int getStatus();
	void setStatus(int status);
	
	boolean isRecurrent(INode potin_node, INode potout_node, int level, int thresh);
	
	List<INode> getAllNodes();
	
	boolean activate();
	
	void loadSensors(double[] sensvals);
	
	void flush();
	
	List<INode> getInputs();
	List<INode> getOutputs();
}
