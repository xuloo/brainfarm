package org.brainfarm.java.util.writers;

import javax.xml.transform.Source;

import org.brainfarm.java.feat.api.evolution.IEvolution;

public interface ISourceGenerator {

	Source getSource(IEvolution evolution);
}
