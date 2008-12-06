package com.brainfarm.java.statemachine.condition;

import com.brainfarm.java.statemachine.Condition;

public class DoubleMatchCondition implements Condition
{
	public double watch;
	
	public double target;
	
	public boolean test()
	{
		return watch == target;
	}
	
}
