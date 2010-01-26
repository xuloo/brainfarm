package org.brainfarm.feat.experiments.xor;

import org.brainfarm.java.feat.api.evolution.IEvolutionOutput;

public class Output implements IEvolutionOutput {

	public int getNumUnit() {
		return 1;
	}

	public double getTarget(int _plist[]) {

		int _index = _plist[0];

		if (_index < 0)
			_index = -_index;

		if (_index >= 4)
			_index = _index % 4;

		double d[] = new double[4];

		d[0] = 0;
		d[1] = 1;
		d[2] = 1;
		d[3] = 0;

		return d[_index];

	}

}
