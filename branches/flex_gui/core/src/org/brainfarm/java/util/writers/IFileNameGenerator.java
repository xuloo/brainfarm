package org.brainfarm.java.util.writers;

import org.brainfarm.java.feat.api.evolution.IEvolution;

public interface IFileNameGenerator {

	String generateFileName(IEvolution evolution);
}
