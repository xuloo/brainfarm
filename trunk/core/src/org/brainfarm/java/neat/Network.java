package org.brainfarm.java.neat;

import java.util.List;

import org.brainfarm.java.neat.api.IGenome;
import org.brainfarm.java.neat.api.INetwork;
import org.brainfarm.java.neat.api.INode;

public class Network implements INetwork {

	@Override
	public List<INode> getAllNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isMinimal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int isStabilised(int period) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int maxDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean pathExists(INode potin, INode potout, int level,
			int threshold) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setGenotype(IGenome genome) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setId(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStatus(int status) {
		// TODO Auto-generated method stub

	}

}
