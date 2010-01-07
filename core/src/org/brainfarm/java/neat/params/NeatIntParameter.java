package org.brainfarm.java.neat.params;

import java.lang.reflect.Field;

import org.brainfarm.java.neat.Neat;

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
}
