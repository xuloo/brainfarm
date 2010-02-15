package org.gatech.feat.experiments.star;

import java.awt.Color;

import org.brainfarm.java.feat.Node;
import org.brainfarm.java.feat.api.INode;

public class StarNode extends Node implements IVisualizableNode{

	public Color nodeColor;
	
	public StarNode(INode n){
		super(n);
	}
	
	public StarNode(int id){
		super(id);
	}

	public void setColor(Color color) {
		nodeColor = color;
	}

	@Override
	public Color getColor() {
		return nodeColor;
	}
}
