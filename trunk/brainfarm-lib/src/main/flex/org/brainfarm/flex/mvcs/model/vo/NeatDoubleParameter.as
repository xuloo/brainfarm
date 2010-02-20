package org.brainfarm.flex.mvcs.model.vo
{
	import flash.net.registerClassAlias;
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	
	import org.as3commons.lang.ClassUtils;

	public class NeatDoubleParameter extends NeatParameter
	{
		public function NeatDoubleParameter() 
		{
			registerClassAlias("org.brainfarm.java.feat.params.NeatDoubleParameter", ClassUtils.forInstance(this));
		}
		
		public override function readExternal(input:IDataInput) : void
		{
			super.readExternal(input);
			
			value = input.readDouble();
		}
		
		public override function writeExternal(output:IDataOutput) : void
		{
			super.writeExternal(output);
			
			output.writeDouble(value);	
		}
	}
}