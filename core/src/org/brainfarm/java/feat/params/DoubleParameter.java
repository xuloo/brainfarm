package org.brainfarm.java.feat.params;

import java.lang.reflect.Field;

import org.red5.io.amf3.IDataInput;
import org.red5.io.amf3.IDataOutput;

public class DoubleParameter extends EvolutionParameter {

	private double value;
	
	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public void set(EvolutionParameters ep) {
		try
		{
			Field field = ep.getClass().getField(name);
			
			try
			{
				field.setDouble(ep, value);
			}
			catch (IllegalAccessException e) {
				// Not allowed to access field.
			}
		}
		catch (NoSuchFieldException e) {
			// Field doesn't exist.
		}
	}

	public String getVal() {
		return String.valueOf(value);
	}
	
	public void setVal(String val) {
		value = Double.parseDouble(val);
	}
	
	public void readExternal(IDataInput input) {
		super.readExternal(input);
		
		value = input.readDouble();
	}
	
	public void writeExternal(IDataOutput output) {
		super.writeExternal(output);
		
		output.writeDouble(value);
	}
}
