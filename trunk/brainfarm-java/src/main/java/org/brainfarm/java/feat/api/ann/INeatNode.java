package org.brainfarm.java.feat.api.ann;

import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;
import org.brainfarm.java.feat.api.enums.ActivationFunction;
import org.brainfarm.java.feat.api.enums.NodeLabel;
import org.brainfarm.java.feat.api.enums.NodeType;

/**
 * Implemented by nodes which constitute INeatNetworks.
 * 
 * @author dtuohy
 *
 */
public interface INeatNode extends INode {

	void flushbackOLD();
	
	boolean mark(int xlevel, INetwork mynet);
	
	double getActivation();
	void setActivation(double activation);
	
	ActivationFunction getActivationFunction();
	void setActivationFunction(ActivationFunction activationFunction);
	
	double getLastActivation();
	void setLastActivation(double lastActivation);
	
	double getLastActivation2();
	void setLastActivation2(double lastActivation2);
	
	double getActivationCount();
	void setActivationCount(double activationCount); 
	
	double getActiveSum();
	void setActiveSum(double activeSum);
	
	void incrementActiveSum(double value);
	
	void setActiveFlag(boolean activeFlag);
	boolean getActiveFlag();
	
	double getActiveOut();
	
	double getActiveOutTimeDelayed();
	
	void resetNNode();
	
	boolean sensorLoad(double value);
	
	void incrementActivationCount();
	
	NodeLabel getGenNodeLabel();
	
	void setGenNodeLabel(NodeLabel genNodeLabel);
	
	NodeType getType();

}
