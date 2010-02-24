package org.brainfarm.flex.mvcs.model.vo
{
	import flash.net.registerClassAlias;
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;

	public class NeatIntParameter extends NeatParameter
	{
		public function NeatIntParameter() 
		{
		}
		
		public override function readExternal(input:IDataInput):void 
		{
			super.readExternal(input);
			
			value = input.readInt();
		}
		
		public override function writeExternal(output:IDataOutput):void 
		{
			super.writeExternal(output);
			
			output.writeInt(value);
		}
	}
}