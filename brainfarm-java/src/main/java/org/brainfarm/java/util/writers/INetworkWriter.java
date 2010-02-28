package org.brainfarm.java.util.writers;

import org.brainfarm.java.feat.api.INetwork;
import org.w3c.dom.Document;

public interface INetworkWriter {
	
	void write(Document xml, INetwork network);
}
