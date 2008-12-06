package com.brainfarm.flash.scheduling 
{
	/**
	 * @author Trevor
	 */
	public interface Scheduleable 
	{
		function run(time : int = 0) : void;
	}
}
