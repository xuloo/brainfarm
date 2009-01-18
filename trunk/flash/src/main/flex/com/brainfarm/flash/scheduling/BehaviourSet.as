package com.brainfarm.flash.scheduling 
{
	import org.pranaframework.collections.IMap;
	import org.pranaframework.collections.Map;
	
	import com.actionengine.flash.core.BaseClass;		

	/**
	 * @author Trevor
	 */
	public class BehaviourSet extends BaseClass
	{
		public var functionLists:IMap;
		
		public var frequency:int = 0;
		
		public function BehaviourSet()
		{
			super();	
		}
		
		override public function initialise(...args):void
		{
			functionLists = new Map();	
		}
	}
}
