package org.brainfarm.java.feat.params;

import org.red5.io.amf3.IDataInput;
import org.red5.io.amf3.IDataOutput;
import org.red5.io.amf3.IExternalizable;

public class DoubleEvolutionParameter extends EvolutionParameter implements IExternalizable {
 
	public void setDoubleValue(Double value) {
		this.value = value;
	}
	
	@Override
	public void readExternal(IDataInput input) {
		super.readExternal(input);
		
		value = input.readDouble();
	}

	@Override
	public void writeExternal(IDataOutput output) {
		super.writeExternal(output);
		
		output.writeDouble((Double)value);
	}
}
