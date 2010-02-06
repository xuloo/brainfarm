package org.brainfarm.java.feat.params;

import java.lang.reflect.Field;

import org.brainfarm.java.feat.Neat;
import org.red5.io.amf3.IDataInput;
import org.red5.io.amf3.IDataOutput;

public class NeatIntParameter extends AbstractNeatParameter {

	private int value;
	
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public void set(Neat neat) {
		try
		{
			Field field = neat.getClass().getField(name);
			
			try
			{
				field.setInt(neat, value);
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
		value = Integer.parseInt(val);
	}
	
	public void readExternal(IDataInput input) {
		super.readExternal(input);
		
		value = input.readInt();
	}
	
	public void writeExternal(IDataOutput output) {
		super.writeExternal(output);
		
		output.writeInt(value);
	}
}
