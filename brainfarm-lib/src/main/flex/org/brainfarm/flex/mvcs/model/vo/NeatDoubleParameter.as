package org.brainfarm.flex.mvcs.model.vo
{
	import flash.net.registerClassAlias;
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;
	
	public class NeatDoubleParameter extends NeatParameter implements IExternalizable
	{
		public function NeatDoubleParameter() 
		{
		}
		
		public override function readExternal(input:IDataInput):void 
		{
			super.readExternal(input);
			
			value = input.readDouble();
		}
		
		public override function writeExternal(output:IDataOutput):void 
		{
			super.writeExternal(output);
			
			output.writeDouble(value);
		}
	}
}