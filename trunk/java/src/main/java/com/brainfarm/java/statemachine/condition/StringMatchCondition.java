package com.brainfarm.java.statemachine.condition;

import com.brainfarm.java.statemachine.Condition;

public class StringMatchCondition implements Condition
{
	public String	watch;
	
	public String	target;
	
	public boolean test()
	{
		return watch.equals(target);
	}
	
}
