package org.brainfarm.flex.mvcs.model.vo
{
	import flash.net.registerClassAlias;
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;
	
	import org.as3commons.reflect.ClassUtils;
	
	public class ExperimentEntry implements IExternalizable
	{
		private var _name:String = "";
		
		public function get label():String
		{
			return _name;
		}
		
		private var _fileName:String = "";
		
		public function get fileName():String
		{
			return _fileName;
		}
		
		public function ExperimentEntry()
		{
			registerClassAlias("org.brainfarm.java.mvcs.service.remote.ExperimentEntry", ClassUtils.forInstance(this));
		}
		
		public function writeExternal(output:IDataOutput):void
		{
			output.writeUTF(_name);
			output.writeUTF(_fileName);
		}
		
		public function readExternal(input:IDataInput):void
		{
			_name = input.readUTF();
			_fileName = input.readUTF();
		}
		
		public function toString():String 
		{
			return "ExperimentEntry[" + _name + ", " + _fileName + "]";
		}
	}
}