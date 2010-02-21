package org.brainfarm.flex.mvcs.model.vo
{
	public class ExperimentEntry
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

		}
		
		public function toString():String 
		{
			return "ExperimentEntry[" + _name + ", " + _fileName + "]";
		}
	}
}