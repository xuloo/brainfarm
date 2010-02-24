package org.brainfarm.java.feat.params;

import org.red5.io.amf3.IDataInput;
import org.red5.io.amf3.IDataOutput;
import org.red5.io.amf3.IExternalizable;

public class IntegerEvolutionParameter extends EvolutionParameter implements IExternalizable {

	public void setIntegerValue(Integer value) {
		System.out.println("Setting integer value" + value);
		this.value = value;
	}
	
	@Override
	public void readExternal(IDataInput input) {
		super.readExternal(input);
		
		value = input.readInt();
	}

	@Override
	public void writeExternal(IDataOutput output) {
		super.writeExternal(output);
		
		output.writeInt((Integer)value);
	}
}
