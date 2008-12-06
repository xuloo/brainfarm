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
package com.brainfarm.flash.sm 
{
	import com.actionengine.flash.action.Action;	
	
	/**
	 * This represents one internal state a character be in: such as
     * angry, or searching-for-ammo.
     */
	public class StateMachineState 
	{
		/**
		 * The first in the sequence of transitions that can leave
		 * this state.
		 */
		public var firstTransition : Transition;

		/**
		 * Returns the first in a sequence of actions that should be
		 * performed while the character is in this state.
		 *
		 * Note that this method should return one or more newly
		 * created action instances, and the caller of this method
		 * should be responsible for the deletion. In the default
		 * implementation, it returns nothing.
		 */
		public function getActions() : Action
		{
			return null;	
		}

		/**
		 * Returns the sequence of actions to perform when arriving in
		 * this state.
		 *
		 * Note that this method should return one or more newly
		 * created action instances, and the caller of this method
		 * should be responsible for the deletion. In the default
		 * implementation, it returns nothing.
		 */
		public function getEntryActions() : Action
		{
			return null;	
		}

		/**
		 * Returns the sequence of actions to perform when leaving
		 * this state.
		 *
		 * Note that this method should return one or more newly
		 * created action instances, and the caller of this method
		 * should be responsible for the deletion. In the default
		 * implementation, it returns nothing.
		 */
		public function getExitActions() : Action
		{
			return null;	
		}
	}
}
