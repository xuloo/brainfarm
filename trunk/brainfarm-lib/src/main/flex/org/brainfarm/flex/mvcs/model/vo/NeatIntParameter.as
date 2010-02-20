package org.brainfarm.flex.mvcs.model.vo
{
	import flash.net.registerClassAlias;
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	
	import org.as3commons.lang.ClassUtils;

	public class NeatIntParameter extends NeatParameter
	{
		public function NeatIntParameter() 
		{
			registerClassAlias("org.brainfarm.java.feat.params.NeatIntParameter", ClassUtils.forInstance(this));
		}
		
		public override function readExternal(input:IDataInput) : void
		{
			super.readExternal(input);
			
			value = input.readInt();
		}
		
		public override function writeExternal(output:IDataOutput) : void
		{
			super.writeExternal(output);
			
			output.writeInt(value);
		}
	}
}