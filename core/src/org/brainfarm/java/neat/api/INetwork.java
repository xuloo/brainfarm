package org.brainfarm.java.neat.api;

import java.util.List;

/**
 * The base interface that any FEAT Network must implement.
 * 
 * @author dtuohy
 *
 */
public interface INetwork {
	
	int getId();
	
	void setId(int id);

	boolean pathExists(INode potin, INode potout, int level, int threshold);
	
	boolean isMinimal();
	
	int maxDepth();
	
	void setGenotype(IGenome genome);
	
	List<INode> getAllNodes();
	
	//TODO: It would be nice if we could move the following to INeatNetwork
	int isStabilised(int period);
	
	int getStatus();
	
	void setStatus(int status);
}
