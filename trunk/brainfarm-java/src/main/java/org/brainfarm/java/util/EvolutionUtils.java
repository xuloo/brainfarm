package org.brainfarm.java.util;

import java.util.List;

import org.brainfarm.java.feat.api.INode;

public class EvolutionUtils {

	/**
	 * Inserts an INode into the INode list only if
	 * it doesn't have the same id as an existing INode.
	 *  
	 * @param nlist
	 * @param n
	 */
	public static void nodeInsert(List<INode> nlist, INode n) {
		int j;
		int id = n.getId();
		int sz = nlist.size();

		for (j = 0; j < sz; j++) {
			if (nlist.get(j).getId() >= id)
				break;
		}
		nlist.add(j, n);
	}
	
}
