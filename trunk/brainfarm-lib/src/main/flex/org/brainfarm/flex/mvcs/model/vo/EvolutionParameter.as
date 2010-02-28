package org.brainfarm.flex.mvcs.model.vo
{
	import flash.net.registerClassAlias;
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;

	public class EvolutionParameter implements IExternalizable
	{		
		public var name:String = "";
		public var description:String = "";
		public var value:*;
		
		public function EvolutionParameter() 
		{
		}
		
		public function readExternal(input:IDataInput):void 
		{
			name = input.readUTF();
			description = input.readUTF();
		}
		
		public function writeExternal(output:IDataOutput):void 
		{
			output.writeUTF(name);
			output.writeUTF(description);
		}
	}
}