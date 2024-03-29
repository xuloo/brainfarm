package org.brainfarm.feat.experiments.xor;

import org.brainfarm.java.feat.api.ann.IEvolutionInput;

public class Input implements IEvolutionInput {

	public double getInput(int _plist[]) {

		int _index = _plist[0];
		int _col = _plist[1];

		if (_index < 0)
			_index = -_index;

		if (_index >= 4)
			_index = _index % 4;

		double d[][] = new double[4][2];

		d[0][0] = 0;
		d[0][1] = 0;

		d[1][0] = 1;
		d[1][1] = 0;

		d[2][0] = 0;
		d[2][1] = 1;

		d[3][0] = 1;
		d[3][1] = 1;

		return d[_index][_col];

	}

}
