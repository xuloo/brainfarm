package org.brainfarm.java.neat.ann;

import java.util.Iterator;
import java.util.List;

import org.brainfarm.java.neat.Network;
import org.brainfarm.java.neat.api.ILink;
import org.brainfarm.java.neat.api.INode;
import org.brainfarm.java.neat.api.ann.INeatNetwork;
import org.brainfarm.java.neat.api.ann.INeatNode;
import org.brainfarm.java.neat.api.enums.ActivationFunction;
import org.brainfarm.java.neat.api.enums.NodeType;
import org.brainfarm.java.util.NeatRoutine;

/**
 * Implementation of Network used for NEAT experiments (i.e.
 * those which evolve ANNs).
 * 
 * @author dtuohy, orig. Ugo Vierucci
 */
public class NeatNetwork extends Network implements INeatNetwork {
	
	/**
	 * Is a collection of object NNode can be mapped in a Vector container; this
	 * collection represent a group of references to input nodes;
	 */
	private List<INode> inputs;

	/**
	 * Is a collection of object NNode can be mapped in a Vector container; this
	 * collection represent a group of references to output nodes;
	 */
	private List<INode> outputs;

	/** Number of NNodes of this net */
	private int numnodes;

	/** Number of Link in this net */
	private int numlinks;

	public List<INode> getInputs() {
		return inputs;
	}

	public void setInputs(List<INode> inputs) {
		this.inputs = inputs;
	}

	public List<INode> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<INode> outputs) {
		this.outputs = outputs;
	}

	public int getNumnodes() {
		return numnodes;
	}

	public void setNumnodes(int numnodes) {
		this.numnodes = numnodes;
	}

	public int getNumlinks() {
		return numlinks;
	}

	public void setNumlinks(int numlinks) {
		this.numlinks = numlinks;
	}

	/**
	 * Insert the method's description here. Creation date: (15/01/2002 8.08.13)
	 */
	public NeatNetwork(List<INode> in, List<INode> out, List<INode> all, int xnet_id) {
		inputs = in;
		outputs = out;
		setAllNodes(all);
		setName(null);
		numnodes = -1;
		numlinks = -1;
		setId(xnet_id);

	}

	public boolean activate() {
		double add_amount = 0.0; // For adding to the activesum
		boolean onetime = false; // Make sure we at least activate once
		int abortcount = 0; // Used in case the output is somehow

		while (outputsoff() || !onetime) {

			++abortcount;
			if (abortcount >= 30) {
				System.out.print("\n *ERROR* Inputs disconnected from output!");
				return false;
			}

			// For each node, compute the sum of its incoming activation
			//itr_node = allnodes.iterator();
			//while (itr_node.hasNext()) {
			for (INode inode : getAllNodes()) {

				INeatNode node = (INeatNode)inode;
				
				if (node.getType() != NodeType.SENSOR) {

					node.setActiveSum(0.0); // reset activation value
					node.setActiveFlag(false); // flag node disabled

					for (ILink _link : node.getIncoming()) {
						if (!_link.isTimeDelayed()) {

							add_amount = _link.getWeight() * ((INeatNode)_link.getInputNode()).getActiveOut();
							
							if (((INeatNode)_link.getInputNode()).getActiveFlag() || 
								((INeatNode)_link.getInputNode()).getType() == NodeType.SENSOR) {
								node.setActiveFlag(true);
							}
							
							node.incrementActiveSum(add_amount);
						} else {
							add_amount = _link.getWeight() * ((INeatNode)_link.getInputNode()).getActiveOutTimeDelayed();
							node.incrementActiveSum(add_amount);
						}
					} // End for over incoming links
				} // End if _node.type !=SENSOR
			} // End for over all nodes

			// Now activate all the non-sensor nodes off their incoming
			// activation

			//itr_node = allnodes.iterator();
			for (INode inode : getAllNodes()) {
				INeatNode node = (INeatNode)inode;

				if (node.getType() != NodeType.SENSOR) {
					// Only activate if some active input came in
					if (node.getActiveFlag()) {
						node.setLastActivation2(node.getLastActivation());
						node.setLastActivation(node.getActivation());

						if (node.getActivationFunction() == ActivationFunction.SIGMOID)
							node.setActivation(NeatRoutine.fsigmoid(node.getActiveSum(), 4.924273, 2.4621365));
						
						node.setActivationCount(node.getActivationCount() + 1.0);
					}
				}
			}
			onetime = true;
		}

		return true;
	}

	public void flush() {
		//Iterator itr_node;

		/*
		 * // old version itr_node = outputs.iterator(); while
		 * (itr_node.hasNext()) { NNode _node = ((NNode) itr_node.next());
		 * _node.flushback(); }
		 */
		// new version : the number of connection >> num of node defined
		// thus is good to reset all nodes without respect connection
		//itr_node = allnodes.iterator();
		//while (itr_node.hasNext()) {
		for (INode node : getAllNodes())
			((INeatNode)node).resetNNode();

	}

	public void loadSensors(double[] sensvals) {
		int counter = 0;
		
		for (INode inode : inputs) {
			INeatNode _node = (INeatNode)inode;
			if (_node.getType() == NodeType.SENSOR) {
				_node.sensorLoad(sensvals[counter++]);
			}
		}
	}

	/**
	 * 
	 * Find the maximum number of neurons between an ouput and an input
	 */
	public int maxDepth() {

		INeatNode _node;
		Iterator<INode> itr_node;

		int cur_depth = 0;
		int max = 0;

		for (INode node : getAllNodes()) {
			((INeatNode)node).setInnerLevel(0);
			((INeatNode)node).setTraversed(false);
		}

		itr_node = outputs.iterator();
		while (itr_node.hasNext()) {
			_node = ((INeatNode) itr_node.next());
			cur_depth = _node.depth(0, this, max);
			if (cur_depth > max)
				max = cur_depth;
		}

		return max;
	}

	public boolean outputsoff() {

		Iterator<INode> itr_node = outputs.iterator();

		itr_node = outputs.iterator();
		while (itr_node.hasNext()) {
			INeatNode _node = (INeatNode)itr_node.next();
			if (_node.getActivationCount() == 0)
				return true;

		}
		return false;

	}

	/**
	 * This module control if has at leat one link from out and all sensor It
	 * flow in all link and if at end are one sensor not 'marked' , return false
	 */
	public boolean isMinimal() {

		boolean rc = false;
		boolean ret_code = true;
		INode _node = null;
		Iterator<INode> itr_node;

		// reset all pending situation
		//itr_node = allnodes.iterator();
		//while (itr_node.hasNext()) {
		for (INode node : getAllNodes())
			((INeatNode)node).setTraversed(false);

		// attempted to excite all sensor
		itr_node = outputs.iterator();
		while (itr_node.hasNext()) {
			_node = itr_node.next();
			rc = ((INeatNode)_node).mark(0, this);
			// the false conditions is for a net with loop
			// or an output without connection direct or indirect
			// 
			if (rc == false)
				return false;
		}

		// okay the virtual signal is flowed,
		// now control if all sensor of fenotype are touched :

		itr_node = inputs.iterator();
		while (itr_node.hasNext()) {
			_node = ((NeatNode) itr_node.next());
			if (!((INeatNode)_node).isTraversed())
				ret_code = false;
		}

		return ret_code;

	}

	/**
	 * starting from sensor , send a signal forward the net after all nodes are
	 * active , control if last activation is == current activation in output
	 * node if activation of output nodes remain stable for 'period' interval ,
	 * return the difference from total cycle and time passed from fist level
	 * stable.
	 */

	public int isStabilised(int period) {

		double add_amount = 0.0; // For adding to the activesum

		if (period == 0)
			period = 30;

		// first step : activation of sensor nodes
		//
		for (INode inode : inputs) {
			INeatNode _node = (INeatNode)inode;
			if (_node.getType() == NodeType.SENSOR) {
				_node.setLastActivation2(_node.getLastActivation());
				_node.setLastActivation(_node.getActivation());
				_node.incrementActivationCount();
				_node.setActivation(1);
			}
		}

		// activation of net
		//	
		boolean done = false;
		int time_passed = 0;
		int counter_stable = 0;
		int level = 0;
		int limit = period + 90;

		while (!done) {
			// if loop exit with signal of error
			if (time_passed >= limit) {
				// System.out.print("\n *ALERT* this net is not STABLE after "+limit);
				return 0;
			}

			// For each node, compute the sum of its incoming activation
			//itr_node = allnodes.iterator();
			//while (itr_node.hasNext()) {
			for (INode inode : getAllNodes()) {
				INeatNode node = (INeatNode)inode;
				if (node.getType() != NodeType.SENSOR) {

					node.setActiveSum(0.0);
					node.setActiveFlag(false); // flag node disabled

					for (ILink _link : node.getIncoming()) {
						if (!_link.isTimeDelayed()) {
							add_amount = ((INeatNode)_link.getInputNode()).getActiveOut();
							if (((INeatNode)_link.getInputNode()).getActiveFlag()
									|| ((INeatNode)_link.getInputNode()).getType() == NodeType.SENSOR) {
								node.setActiveFlag(true);
							}
							node.incrementActiveSum(add_amount);
						} else {
							add_amount = ((INeatNode)_link.getInputNode()).getActiveOutTimeDelayed();
							node.incrementActiveSum(add_amount);
						}
					} // End for over incoming links
				} // End if _node.type !=SENSOR
			} // End for over all nodes

			// Now activate all the non-sensor nodes off their incoming
			// activation

			//itr_node = allnodes.iterator();
			//while (itr_node.hasNext()) {
				//_node = ((NNode) itr_node.next());
			for (INode inode : getAllNodes()) {
				INeatNode node = (INeatNode)inode;
				if (node.getType() != NodeType.SENSOR) {
					// Only activate if some active input came in
					if (node.getActiveFlag()) {
						node.setLastActivation2(node.getLastActivation());
						node.setLastActivation(node.getActivation());
						node.setActivation(node.getActiveSum());
						node.setActivationCount(node.getActivation() + 1.0);
					}
				}
			}

			//onetime = true;

			if (!outputsoff()) {
				//
				// verify is has a changement in any output(j-esimo)
				//
				boolean has_changed = false;
				for (INode inode : outputs) {
					INeatNode _node = (INeatNode)inode;
					if (_node.getLastActivation() != _node.getActivation()) {
						has_changed = true;
						break;
					}
				}

				if (!has_changed) {
					// if has not changement, increment the counter
					// of no change....
					counter_stable += 1;
					// if counter no change >= 'period' parameter (virtual
					// depth) , the net is relaxed
					// and stable , thus can be return the 'delta' passed
					if (counter_stable >= period) {
						done = true;
						level = time_passed;
						break;
					}
				}

				// must be re-start until correct exit or abort for loop
				//
				else
					counter_stable = 0;
			}
			time_passed++;

		}

		// return delta = total time passed (real) - period (depth virtual)
		return (level - period + 1);
	}
}