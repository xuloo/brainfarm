package org.brainfarm.feat.util.writers;

import org.brainfarm.java.feat.Evolution;

public class DummyEvolution extends Evolution {

	public DummyEvolution() {
		super(null, null, 5, 5);
		
		for (int i = 1; i <= 5; i++) {
			maxFitnessEachEpoch.add(1.0 * i);
		}
	}
}
