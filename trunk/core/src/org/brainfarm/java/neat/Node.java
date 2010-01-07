/* Generated by Together */

package org.brainfarm.java.neat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.neat.api.ILink;
import org.brainfarm.java.neat.api.INetwork;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.ITrait;
import org.brainfarm.java.neat.api.types.ActivationFunction;
import org.brainfarm.java.neat.api.types.NodeType;


/**
 * A NODE is either a NEURON or a SENSOR. If it's a sensor, it can be loaded
 * with a value for output If it's a neuron, it has a list of its incoming input
 * signals Use an activation count to avoid flushing
 */
public class Node implements INode {
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

	/** vector of real values for hebbian or other advanced function future */
	private double[] params = new double[Neat.num_trait_params];

	/** A list of pointers to incoming weighted signals from other nodes */
	private List<ILink> incoming = new ArrayList<ILink>();

	/** A list of pointers to links carrying this node's signal */
	private List<ILink> outgoing = new ArrayList<ILink>();

	/** Numeric identification of node */
	private int id;

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

	/** Points to a trait of parameters */
	private ITrait trait;

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

	public double[] getParams() {
		return params;
	}

	public void setParams(double[] params) {
		this.params = params;
	}

	public List<ILink> getIncoming() {
		return incoming;
	}

	public void setIncoming(List<ILink> incoming) {
		this.incoming = incoming;
	}

	public List<ILink> getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(List<ILink> outgoing) {
		this.outgoing = outgoing;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public void setDup(Node dup) {
		this.setDuplicate(dup);
	}

	public Node(NodeType ntype, int nodeid) {
		activeFlag = false;
		activeSum = 0;
		activation = 0;
		output = 0;
		lastActivation = 0;
		lastActivation2 = 0;
		type = ntype; // NEURON or SENSOR type
		activationCount = 0; // Inactive upon creation
		id = nodeid; // id del nodo
		activationFunction = ActivationFunction.SIGMOID; // funt act : signmoide
		genNodeLabel = NodeLabel.HIDDEN;
		setTrait(null);
		// incoming = new Vector(1, 0);
		// outgoing = new Vector(1, 0);
		setDuplicate(null);
		setAnalogue(null);
		setTraversed(false);
		setInnerLevel(0);
	}

	public Node(NodeType ntype, int nodeid, NodeLabel placement) {
		activeFlag = false;
		activeSum = 0;
		activation = 0;
		output = 0;
		lastActivation = 0;
		lastActivation2 = 0;
		type = ntype; // NEURON or SENSOR type
		activationCount = 0; // Inactive upon creation
		id = nodeid; // id del nodo
		activationFunction = ActivationFunction.SIGMOID; // funt act : signmoide
		genNodeLabel = placement;
		setTrait(null);
		// incoming = new Vector(1, 0);
		// outgoing = new Vector(1, 0);
		setDuplicate(null);
		setAnalogue(null);
		setTraversed(false);
		setInnerLevel(0);
	} //                                    

	// Construct the node out of a file specification
	// using given list of traits
	//
	/*public NNode(String xline, ArrayList<Trait> traits) {

		active_flag = false;
		inner_level = 0;
		activesum = 0;
		activation = 0;
		output = 0;
		last_activation = 0;
		last_activation2 = 0;
		activation_count = 0; // Inactive upon creation
		ftype = NeatConstant.SIGMOID; // funt act : signmoide
		// incoming = new Vector(1, 0);
		// outgoing = new Vector(1, 0);
		dup = null;
		analogue = null;

		Iterator itr_trait;
		int _trait_id;
		StringTokenizer st;
		String s1;
		st = new StringTokenizer(xline);

		// skip keyword
		s1 = st.nextToken();

		// Get the node parameters
		s1 = st.nextToken();
		id = Integer.parseInt(s1);

		// get trait
		s1 = st.nextToken();
		_trait_id = Integer.parseInt(s1);

		// get type of node
		s1 = st.nextToken();
		type = Integer.parseInt(s1);

		// get genetic typw of node
		s1 = st.nextToken();
		//gen_node_label = Integer.parseInt(s1);

		setTrait(null);
		is_traversed = false;

		if (_trait_id > 0 && traits != null) {

			itr_trait = traits.iterator();
			while (itr_trait.hasNext()) {
				Trait _trait = ((Trait) itr_trait.next());
				if (_trait.getId() == _trait_id) {
					setTrait(_trait);
					break;
				}
			}
		}

	}*/
	
	/**
	 * Empty Constructor.
	 */
	public Node() {
		
	}

	public Node(INode n, ITrait t) {
		activeFlag = false;
		activeSum = 0;
		activation = 0;
		output = 0;
		lastActivation = 0;
		lastActivation2 = 0;
		type = n.getType(); // NEURON or SENSOR type
		activationCount = 0; // Inactive upon creation
		id = n.getId(); // id del nodo
		activationFunction = ActivationFunction.SIGMOID; // funt act : sigmoid
		genNodeLabel = n.getGenNodeLabel();
		setTrait(t);
		setDuplicate(null);
		setAnalogue(null);
		setTraversed(false);
		setInnerLevel(0);
	}
	
	public Node clone() {
		Node newNode = new Node();
		
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
		newNode.id = id;
		newNode.genNodeLabel = genNodeLabel;
		newNode.setTrait(trait.clone());
		
		return newNode;
	}

	public void deriveTrait(ITrait trait) {
		if (trait != null) {
			for (int count = 0; count < Neat.num_trait_params; count++)
				params[count] = trait.getParams()[count];
		} else {
			for (int count = 0; count < Neat.num_trait_params; count++)
				params[count] = 0;
		}
	}

	/**
   * 
   * 
   */
	public int depth(int xlevel, Network mynet, int xmax_level) {

		//Iterator itr_link;

		//StringBuffer cost1 = null;
		//String cost2 = null;

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

		for (ILink _link : incoming) {
			INode _ynode = _link.getInputNode();

			if (!_ynode.isTraversed()) {
				_ynode.setTraversed(true);
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

		System.out.print(fmtf.format(id) + " activation count "
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

	/**
	 * .
	 * 
	 */
	public boolean mark(int xlevel, INetwork mynet) {

		//Iterator itr_link;

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

		for (ILink _link : incoming) {
			if (!_link.getInputNode().isTraversed()) {
				_link.getInputNode().setTraversed(true);
				rc = _link.getInputNode().mark(xlevel + 1, mynet);
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
			for (ILink _link : incoming) {
				_link.setAddedWeight(0.0);

				_link.setTraversed(false);

				if (_link.getInputNode().getActivationCount() > 0)
					_link.getInputNode().flushbackOLD();
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
		for (ILink _link : incoming) {
			_link.setAddedWeight(0.0);
			_link.setTraversed(false);
		}

		// Flush forw link
		for (ILink _link : outgoing) {
			_link.setAddedWeight(0.0);
			_link.setTraversed(false);
		}

	}

	public void setTrait(ITrait trait) {
		this.trait = trait;
	}

	public ITrait getTrait() {
		return trait;
	}

	public void setDuplicate(INode duplicate) {
		this.duplicate = duplicate;
	}

	public INode getDuplicate() {
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