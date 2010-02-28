package org.brainfarm.java.util.writers;

import org.brainfarm.java.feat.api.IEvolution;
import org.w3c.dom.Document;

public interface IEvolutionWriter {

	void write(Document xml, IEvolution evolution);
}
