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
	 * @author Trevor
	 */
	public class StateMachine 
	{
		/**
		 * Holds the initial state (a pointer into the 'state' array).
		 */
		public var initialState : StateMachineState;

		/**
		 * Holds the current state of the machine.
		 */
		public var currentState : StateMachineState;

		/**
		 * This method runs the state machine - it checks for
		 * transitions, applies them and returns a list of actions.
		 */
		public function update() : Action
		{
			// The variable to hold the actions to perform
			var actions : Action;
	
			// First case - we have no current state.
			if (currentState == null)
			{
				// In this case we use the entry action for the initial state.
				if (initialState != null) 
				{	
					// Transition to the first state
					currentState = initialState;
	
					// Returns the initial states entry actions
					actions = currentState.getEntryActions();
				}
			}
	
	        // Otherwise we have a current state to work with
	        else 
			{
				// Start off with no transition
				var transition : Transition;
	
				// Check through each transition in the current state.
				var testTransition : BaseTransition = currentState.firstTransition;
				
				while (testTransition) 
				{
					if (testTransition.isTriggered()) 
					{
						transition = Transition(testTransition);
						break;
					}
					
					testTransition = testTransition.next;
				}
	
				// Check if we found a transition
				if (transition) 
				{
					// Find our destination
					var nextState : StateMachineState = transition.getTargetState();
	
					// Accumulate our list of actions
					var tempList : Action;
					var last : Action;
	
					// Add each element to the list in turn
					actions = currentState.getExitActions();
					last = actions.last;
	
					tempList = transition.getActions();
					last.next = tempList;
					last = tempList.last;
	
					tempList = nextState.getActions();
					last.next = tempList;
	
					// Update the change of state
					currentState = nextState;
				}
	
	            // Otherwise our actions to perform are simply those for the
	            // current state.
	            else 
				{	
					actions = currentState.getActions();
				}
			}
	
			return actions;	
		}
	}
}
