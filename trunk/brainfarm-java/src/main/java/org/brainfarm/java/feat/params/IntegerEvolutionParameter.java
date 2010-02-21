package org.brainfarm.java.feat.params;

public class IntegerEvolutionParameter extends EvolutionParameter {

	@Override 
	public void setValue(Object value) {
		if (value instanceof String) {
			this.value = Integer.parseInt((String)value);
		} else {
			super.setValue(value);
		}
	}
}
