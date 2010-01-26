package omit.org.gatech.feat.jung;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;

import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.INode;
import org.gatech.feat.experiments.star.IVisualizableNode;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.util.Pair;

public class MyNetworkTheme {

	static INetwork net;

	public static Color getColorFor(Integer i){
		INode n = net.getAllNodes().get(i);
		if(n instanceof IVisualizableNode)
			return ((IVisualizableNode)n).getColor();
		return Color.white;
	}

	public static void setModel(INetwork network) {
		net = network;
	}

	static DecimalFormat df = new DecimalFormat("#.###");
	public static String getLabelFor(Pair<Integer> endpoints) {
//		State from = hmm.getStates().get(endpoints.getFirst());
//		State to = hmm.getStates().get(endpoints.getSecond());
//		return df.format(hmm.getTransitionWeight(from, to));
		return "";
	}

	public static String getLabelFor(Integer i) {
		return "" + i;
	}

	public static Shape getShapeFor(Integer i) {
		return new Ellipse2D.Float(0,0,25,25);
	}

	public static void initializeLayout(Layout<Integer, Number> layout) {

	}

}