package com.brainfarm.flash.scheduling 
{
	import com.actionengine.flash.action.Action;
	import com.actionengine.flash.core.BaseClass;	

	/**
	 * @author Trevor
	 */
	public class ScheduledObject extends BaseClass implements Scheduleable
	{
		public var behaviour:Action;
		
		public var frequency:int;
		
		public var phase:int;
		
		public var priority : int;
		
		public function run(time : int = 0) : void
		{
		}
	}
}
