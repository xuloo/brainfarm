package org.brainfarm.flex.mvcs.model.vo
{
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	import flash.utils.IExternalizable;

	public class ExperimentEntry implements IExternalizable
	{
		[Bindable]
		public var running:Boolean = false;
		
		[Bindable]
		public var progress:Number = 0;
		
		[Bindable]
		public var complete:Boolean = false;
		
		private var _name:String = "";
		
		[Bindable]
		public function get name():String
		{
			return _name.toUpperCase();
		}
		
		public function set name(value:String):void 
		{
			
		}
		
		private var _fileName:String = "";
		
		public function get fileName():String
		{
			return _fileName;
		}
		
		private var _description:String = "";
		
		[Bindable]
		public function get description():String 
		{
			return _description;
		}
		
		public function set description(value:String):void 
		{
			
		}
		
		public function ExperimentEntry()
		{

		}
		
		public function toString():String 
		{
			return "ExperimentEntry[" + _name + ", " + _fileName + "]";
		}
		
		public function readExternal(input:IDataInput):void 
		{
			_name = input.readUTF();
			_fileName = input.readUTF();
			_description = input.readUTF();
		}
		
		public function writeExternal(output:IDataOutput):void 
		{
			output.writeUTF(_name);
			output.writeUTF(_fileName);
			output.writeUTF(_description);
		}
	}
}