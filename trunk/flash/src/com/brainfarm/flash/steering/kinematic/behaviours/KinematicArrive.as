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
	import com.brainfarm.flash.steering.SteeringOutput;		

	/**
	 * @author Trevor
	 */
	public class KinematicArrive extends TargetedKinematicMovement 
	{		
		/**
		 * At each step the character tries to reach its target in
		 * this duration. This means it moves more slowly when nearby.
		 */
		public var timeToTarget : Number;

		/**
		 * If the character is closer than this to the target, it will
		 * not attempt to move.
		 */
		public var radius : Number;
		
		/**
         * Works out the desired steering and writes it into the given
         * steering output structure.
         */
		override public function getSteering(output : SteeringOutput) : void
		{
			// First work out the direction
			output.linear = target;
			output.linear.minusEq( character.position );

			// If there is no direction, do nothing
			if (output.linear.isMagnitudeLessThan( radius ))
			{
				output.linear.clear( );
			}
        	else
			{
				// We'd like to arrive in timeToTarget seconds
				output.linear.multiplyEq( 1.0 / timeToTarget );

				// If that is too fast, then clip the speed
				if (output.linear.isMagnitudeGreaterThan( maxSpeed ))
				{
					output.linear.normalise( );
					output.linear.multiplyEq( maxSpeed );
				}
			}
		}
	}
}
