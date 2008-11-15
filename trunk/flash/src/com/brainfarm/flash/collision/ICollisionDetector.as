/*
 * Part of the Artificial Intelligence for Games system.
 *
 * Copyright (c) Ian Millington 2003-2006. All Rights Reserved.
 *
 * This software is distributed under licence. Use of this software
 * implies agreement with all terms and conditions of the accompanying
 * software licence.
 * 
 * Actionscript port - Trevor Burton [worldofpaper@googlemail.com]
 */
package com.brainfarm.flash.collision 
{
	import com.brainfarm.flash.steering.Kinematic;
	import com.brainfarm.flash.util.math.Vector3;	

	/**
	 * @author Trevor
	 */
	public interface ICollisionDetector 
	{
		/**
		 * Interface to the collision detection engine. The user must
		 * provide it when instantiating some of the behaviours.
		 *
		 * @param current The current kinematic state of the agent
		 * @param target The agent's target
		 * @param collisionPoint A pointer to a structure into which the
		 * coordinates of the collision point should be entered.
		 * @param collisionNormal A pointer to a structure into which the
		 * vector normal to the collision surface should be entered.
		 *
		 * @return False if no collision is detected.
		 */
		function detectCollisions(current : Kinematic,
                                  target : Kinematic,
                                  collisionPoint : Vector3,
                                  collisionNormal : Vector3) : Boolean
	}
}
