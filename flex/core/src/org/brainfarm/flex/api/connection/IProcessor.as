package org.brainfarm.flex.api.connection
{
	public interface IProcessor
	{
		function addType(type:Class):void;
		
		function canProcess(object:*):Boolean;
		
		function process(object:*):void;
	}
}