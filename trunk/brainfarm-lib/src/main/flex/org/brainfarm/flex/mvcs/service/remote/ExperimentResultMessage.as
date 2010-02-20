package org.brainfarm.flex.mvcs.service.remote
{
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	
	import org.brainfarm.flex.comm.messages.BaseMessage;
	
	public class ExperimentResultMessage extends BaseMessage
	{
		public override function get aliasName():String 
		{
			return "org.brainfarm.java.mvcs.service.remote.ResultSummaryMessage";
		}
		
		private var _numberOfRuns:int = 0;
		
		public function get numberOfRuns():int 
		{
			return _numberOfRuns;
		}
		
		private var _numberOfEpochs:int = 0;
		
		public function get numberOfEpochs():int 
		{
			return _numberOfEpochs;
		}
		
		public function ExperimentResultMessage()
		{
			super();
		}
		
		public override function readExternal(input:IDataInput) : void
		{
			super.readExternal(input);
			
			_numberOfRuns = input.readInt();
			_numberOfEpochs = input.readInt();
		}
		
		public override function writeExternal(output:IDataOutput) : void
		{
			super.writeExternal(output);
			
			output.writeInt(_numberOfRuns);
			output.writeInt(_numberOfEpochs);
		}
	}
}