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
package com.brainfarm.flash.steering.behaviours 
{
	import com.brainfarm.flash.steering.SteeringBehaviour;
	import com.brainfarm.flash.steering.SteeringOutput;
	import com.brainfarm.flash.util.math.Vector3;	

	/**
	 * @author Trevor
	 */
	public class Seek extends SteeringBehaviour 
	{
		/**
		 * The target may be any vector (i.e. it might be something
		 * that has no orientation, such as a point in space).
		 */
		public var target : Vector3;

		/**
		 * The maximum acceleration that can be used to reach the
		 * target.
		 */
		public var maxAcceleration : Number;

		/**
		 * Works out the desired steering and writes it into the given
		 * steering output structure.
		 */
		override public function getSteering(output : SteeringOutput) : void
		{
		}
	}
}
