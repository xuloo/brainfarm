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
package com.brainfarm.flash.learning 
{
	import com.actionengine.flash.core.BaseClass;
	import com.actionengine.flash.core.interfaces.IDestroyable;		

	/**
	 * A state in a reinforcement problem will be indexed by number,
	 * so all we provide is a value and a slot for more data. If you
	 * know the problem in advance, this can be far more concrete.
	 */
	public class LearningProblemState extends BaseClass
	{
		/** Holds the index number of this state. */
		public var index : int;

		/** Holds additional data used by the state. */
		public var data : *;

		override public function destroy() : void
		{
			index = NaN;
			
			if (data is IDestroyable) IDestroyable(data).destroy();
		}
	}
}
