package org.brainfarm.flex.comm
{
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;
	
	public class BasicMessage implements IMessage, IExternalizable
	{
		public function BasicMessage()
		{
		}
		
		public function read():void
		{
			var obj:Object = {};
		}
		
		public function write():void
		{
		}
		
		public function writeExternal(output:IDataOutput):void
		{
		}
		
		public function readExternal(input:IDataInput):void
		{
		}
	}
}