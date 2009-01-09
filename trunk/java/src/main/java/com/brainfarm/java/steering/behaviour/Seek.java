/*
 * Part of the Artificial Intelligence for Games system.
 *
 * Copyright (c) Ian Millington 2003-2006. All Rights Reserved.
 *
 * This software is distributed under licence. Use of this software
 * implies agreement with all terms and conditions of the accompanying
 * software licence.
 * 
 * Java port - Trevor Burton [worldofpaper@googlemail.com]
 */
package com.brainfarm.java.steering.behaviour;

import com.actionengine.java.data.Input;
import com.brainfarm.java.data.State;
import com.brainfarm.java.steering.AbstractSteeringBehaviour;
import com.brainfarm.java.steering.SteeringOutput;
import com.brainfarm.java.util.math.Vector3;

/**
 * The seek steering behaviour takes a target and aims right for it with maximum
 * acceleration.
 */
public class Seek extends AbstractSteeringBehaviour
{
	/**
	 * The target may be any vector (i.e. it might be something that has no
	 * orientation, such as a point in space).
	 */
	public Vector3	target;
	
	/**
	 * The maximum acceleration that can be used to reach the target.
	 */
	public Double	maxAcceleration;
	
	/**
	 * Works out the desired steering and writes it into the given steering
	 * output structure.
	 */
	public void getSteering(SteeringOutput output)
	{
		// First work out the direction
		output.linear = target;
		output.linear.returnSubtraction(character.position);
		
		// If there is no direction, do nothing
		if (output.linear.getSquareMagnitude() > 0)
		{
			output.linear.normalise();
			output.linear.returnScale(maxAcceleration);
		}
	}
	
	@Override
	public void getSteering(SteeringOutput output, Input input) {

	}
	
	@Override
	public void getSteering(State state, Input input) {
		
	}
}
