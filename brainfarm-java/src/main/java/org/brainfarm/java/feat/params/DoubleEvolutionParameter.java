package org.brainfarm.java.feat.params;

public class DoubleEvolutionParameter extends EvolutionParameter {

	@Override 
	public void setValue(Object value) {
		if (value instanceof String) {
			this.value = Double.parseDouble((String)value);
		} else {
			super.setValue(value);
		}
	}
}
