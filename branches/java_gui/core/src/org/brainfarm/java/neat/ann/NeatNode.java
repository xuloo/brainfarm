package org.brainfarm.java.neat.ann;

import java.text.DecimalFormat;

import org.brainfarm.java.neat.Node;
import org.brainfarm.java.neat.api.ILink;
import org.brainfarm.java.neat.api.INetwork;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.ann.INeatNetwork;
import org.brainfarm.java.neat.api.ann.INeatNode;
import org.brainfarm.java.neat.api.enums.ActivationFunction;
import org.brainfarm.java.neat.api.enums.NodeLabel;
import org.brainfarm.java.neat.api.enums.NodeType;


/**
 * This class contains fields and logic that are specific to NEAT 
 * experiments (that is, those which evolve ANNs).
 * 
 * A NODE is either a NEURON or a SENSOR. If it's a sensor, it can be loaded
 * with a value for output If it's a neuron, it has a list of its incoming input
 * signals Use an activation count to avoid flushing
 */
public class NeatNode extends Node implements INeatNode {
	
	/** type is either SIGMOID ..or others that can be added */
	private ActivationFunction activationFunction;

	/** type is either NEURON or SENSOR */
	private NodeType type;

	/** The incoming activity before being processed */
	private double activeSum;

	/** The total activation entering in this Node */
	private double activation;

	/** when are signal's to node, the node switch this field from FALSE to TRUE */
	private boolean activeFlag;

	/** not used */
	private double output;

	/**
	 * Used for genetic marking of nodes. are 4 type of node :
	 * input,bias,hidden,output
	 */
	private NodeLabel genNodeLabel;

	/**
	 * this value is how many time this node are activated during activation of
	 * network
	 */
	private double activationCount;

	/**
	 * activation value of node at time t-1; Holds the previous step's
	 * activation for recurrency
	 */
	private double lastActivation;

	/**
	 * activation value of node at time t-2 Holds the activation before the
	 * previous step's
	 */
	private double lastActivation2;

	/**
	 * Is a reference to a Node ; Has used for generate and point from a genetic
	 * node (genotype) to a real node (fenotype) during 'genesis' process
	 * 
	 * @clientCardinality 1
	 * @supplierCardinality 1
	 */
	private INode analogue;

	/**
	 * Is a temporary reference to a Node ; Has used for generate a new genome
	 * during duplicate phase of genotype.
	 * 
	 * @supplierCardinality 1
	 * @clientCardinality 1
	 */
	private INode duplicate;

	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public void setActiveSum(int activesum) {
		this.activeSum = activesum;
	}

	public double getActivation() {
		return activation;
	}

	public void setActivation(double activation) {
		this.activation = activation;
	}

	public boolean getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}

	public NodeLabel getGenNodeLabel() {
		return genNodeLabel;
	}

	public void setGenNodeLabel(NodeLabel genNodeLabel) {
		this.genNodeLabel = genNodeLabel;
	}

	public double getActivationCount() {
		return activationCount;
	}

	public void setActivationCount(double activationCount) {
		this.activationCount = activationCount;
	}
	
	public void incrementActivationCount() {
		activationCount++;
	}

	public double getLastActivation() {
		return lastActivation;
	}

	public void setLastActivation(double lastActivation) {
		this.lastActivation = lastActivation;
	}

	public double getLastActivation2() {
		return lastActivation2;
	}

	public void setLastActivation2(double lastActivation2) {
		this.lastActivation2 = lastActivation2;
	}

	public INode getAnalogue() {
		return analogue;
	}

	public void setAnalogue(INode analogue) {
		this.analogue = analogue;
	}

	public NeatNode(NodeType ntype, int nodeid) {
		activeFlag = false;
		activeSum = 0;
		activation = 0;
		output = 0;
		lastActivation = 0;
		lastActivation2 = 0;
		type = ntype; // NEURON or SENSOR type
		activationCount = 0; // Inactive upon creation
		setId(nodeid); // id del nodo
		activationFunction = ActivationFunction.SIGMOID; // funt act : signmoide
		genNodeLabel = NodeLabel.HIDDEN;
		setDuplicate(null);
		setAnalogue(null);
		setTraversed(false);
		setInnerLevel(0);
	}

	public NeatNode(NodeType ntype, int nodeid, NodeLabel placement) {
		activeFlag = false;
		activeSum = 0;
		activation = 0;
		output = 0;
		lastActivation = 0;
		lastActivation2 = 0;
		type = ntype; // NEURON or SENSOR type
		activationCount = 0; // Inactive upon creation
		setId(nodeid); // id del nodo
		activationFunction = ActivationFunction.SIGMOID; // funt act : signmoide
		genNodeLabel = placement;
		// incoming = new Vector(1, 0);
		// outgoing = new Vector(1, 0);
		setDuplicate(null);
		setAnalogue(null);
		setTraversed(false);
		setInnerLevel(0);
	}

	
	/**
	 * Empty Constructor.
	 */
	public NeatNode() {
		
	}

	public NeatNode(INode n) {
		activeFlag = false;
		activeSum = 0;
		activation = 0;
		output = 0;
		lastActivation = 0;
		lastActivation2 = 0;
		type = ((INeatNode)n).getType(); // NEURON or SENSOR type
		activationCount = 0; // Inactive upon creation
		setId(n.getId()); // id del nodo
		activationFunction = ActivationFunction.SIGMOID; // funt act : sigmoid
		genNodeLabel = ((INeatNode)n).getGenNodeLabel();
		setDuplicate(null);
		setAnalogue(null);
		setTraversed(false);
		setInnerLevel(0);
	}
	
	public NeatNode clone() {
		NeatNode newNode = new NeatNode();
		
		// defaults
		newNode.activeFlag = false;
		newNode.activeSum = 0;
		newNode.activation = 0;
		newNode.output = 0;
		newNode.lastActivation = 0;
		newNode.lastActivation2 = 0;
		newNode.activationCount = 0;
		newNode.setDuplicate(null);
		newNode.setAnalogue(null);
		newNode.setTraversed(false);
		newNode.setInnerLevel(0);
		newNode.activationFunction = ActivationFunction.SIGMOID;
		
		// cloned
		newNode.type = type;
		newNode.setId(getId());
		newNode.genNodeLabel = genNodeLabel;
		
		return newNode;
	}

	public int depth(int xlevel, INeatNetwork mynet, int xmax_level) {

		// control for loop
		if (xlevel > 100) {
			System.out
					.print("\n ** DEPTH NOT DETERMINED FOR NETWORK WITH LOOP ");
			// System.out.print("\n Fenotype is " + mynet.getNet_id());
			// System.out.print("\n Genotype is  " + mynet.getNet_id());
			// mynet.genotype.op_view();
			return 10;
		}

		// Base Case
		if (this.type == NodeType.SENSOR) {
			return xlevel;
		}

		xlevel++;

		// recursion case
		//itr_link = this.getIncoming().iterator();
		int cur_depth = 0; // The depth of the current node

		for (ILink _link : getIncoming()) {
			INeatNode _ynode = (INeatNode)_link.getInputNode();

			if (!((INeatNode)_ynode).isTraversed()) {
				((INeatNode)_ynode).setTraversed(true);
				cur_depth = _ynode.depth(xlevel, mynet, xmax_level);
				_ynode.setInnerLevel(cur_depth - xlevel);
			} else
				cur_depth = xlevel + _ynode.getInnerLevel();

			if (cur_depth > xmax_level)
				xmax_level = cur_depth;
		}
		return xmax_level;

	}

	public double getActiveOut() {
		if (activationCount > 0)
			return activation;
		else
			return 0.0;
	}

	/**
	 * Return activation currently in node from PREVIOUS (time-delayed) time
	 * step, if there is one
	 */
	public double getActiveOutTimeDelayed() {
		if (activationCount > 1)
			return lastActivation;
		else
			return 0.0;
	}

	

	public void op_view() {

		String maskf = " #,##0";
		DecimalFormat fmtf = new DecimalFormat(maskf);

		String mask5 = " #,##0.000";
		DecimalFormat fmt5 = new DecimalFormat(mask5);

		if (type == NodeType.SENSOR)
			System.out.print("\n (Sensor)");
		if (type == NodeType.NEURON)
			System.out.print("\n (Neuron)");

		System.out.print(fmtf.format(getId()) + " activation count "
				+ fmt5.format(activationCount) + " activation="
				+ fmt5.format(activation) + ")");

	}

	public boolean sensorLoad(double value) {
		if (type == NodeType.SENSOR) {
			// Time delay memory
			lastActivation2 = lastActivation;
			lastActivation = activation;

			activationCount++; // Puts sensor into next time-step
			activation = value; // ovviamente non viene applicata la f(x)!!!!
			return true;
		} else
			return false;
	}
	
	public double getActiveSum() {
		return activeSum;
	}

	public void setActiveSum(double activesum) {
		this.activeSum = activesum;
	}
	
	public void incrementActiveSum(double value) {
		activeSum += value;
	}

	private int innerLevel;
	
	private boolean traversed;

	public boolean mark(int xlevel, INetwork mynet) {
		
		// loop control
		if (xlevel > 100) {
			// System.out.print("\n ** DEPTH NOT DETERMINED FOR NETWORK WITH LOOP ");
			// System.out.print("\n Network name is " + mynet.getNet_id());
			// mynet.genotype.op_view();
			return false;
		}

		// base Case
		if (this.type == NodeType.SENSOR) {
			this.setTraversed(true);
			return true;
		}

		// recurrency case
		//itr_link = this.getIncoming().iterator();
		boolean rc = false;

		for (ILink _link : getIncoming()) {
			if (!((INeatNode)_link.getInputNode()).isTraversed()) {
				((INeatNode)_link.getInputNode()).setTraversed(true);
				rc = ((INeatNode)_link.getInputNode()).mark(xlevel + 1, mynet);
				if (rc == false)
					return false;
			}
		}
		
		return true;
	}

	public void flushbackOLD() {
		//Iterator itr_link;

		// A sensor should not flush black
		if (type != NodeType.SENSOR) {
			if (activationCount > 0) {
				activationCount = 0;
				activation = 0;
				lastActivation = 0;
				lastActivation2 = 0;
			}
			// Flush back recursively
			for (ILink _link : getIncoming()) {
				_link.setAddedWeight(0.0);

				_link.setTraversed(false);

				if (((INeatNode)_link.getInputNode()).getActivationCount() > 0)
					((INeatNode)_link.getInputNode()).flushbackOLD();
			}
		} else {
			// Flush the SENSOR
			activationCount = 0;
			activation = 0;
			lastActivation = 0;
			lastActivation2 = 0;
		}
	}

	public void resetNNode() {
		//Iterator itr_link;

		activationCount = 0;
		activation = 0;
		lastActivation = 0;
		lastActivation2 = 0;

		// Flush back link
		for (ILink _link : getIncoming()) {
			_link.setAddedWeight(0.0);
			_link.setTraversed(false);
		}

		// Flush forw link
		for (ILink _link : getOutgoing()) {
			_link.setAddedWeight(0.0);
			_link.setTraversed(false);
		}

	}

	private void setDuplicate(INode duplicate) {
		this.duplicate = duplicate;
	}
	
	public INode generateDuplicate(){
		INode newnode = new NeatNode(this);
		setDuplicate(newnode);
		return newnode;
	}

	public INode getCachedDuplicate() {
		return duplicate;
	}

	public void setInnerLevel(int innerLevel) {
		this.innerLevel = innerLevel;
	}

	public int getInnerLevel() {
		return innerLevel;
	}

	public void setTraversed(boolean traversed) {
		this.traversed = traversed;
	}

	public boolean isTraversed() {
		return traversed;
	}
}