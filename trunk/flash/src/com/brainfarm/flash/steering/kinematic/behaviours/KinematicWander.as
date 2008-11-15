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
package com.brainfarm.flash.steering.kinematic.behaviours 
{
	import com.actionengine.flash.util.number.RandomUtils;
	import com.brainfarm.flash.steering.SteeringOutput;	

	/**
	 * @author Trevor
	 */
	public class KinematicWander extends KinematicMovement 
	{
		/**
		 * The maximum rate at which the character can turn.
		 */
		public var maxRotation : Number;

		/**
		 * Works out the desired steering and writes it into the given
		 * steering output structure.
		 */
		override public function getSteering(output : SteeringOutput) : void
		{
			// Move forward in the current direction
			output.linear = character.getOrientationAsVector( );
			output.linear.multiplyEq( maxSpeed );

			// Turn a little
			var change : Number = RandomUtils.randomBinomial( );
			output.angular.w = change * maxRotation;
		}
	}
}
