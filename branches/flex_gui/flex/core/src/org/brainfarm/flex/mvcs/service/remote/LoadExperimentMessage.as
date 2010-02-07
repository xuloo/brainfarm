package org.brainfarm.flex.mvcs.service.remote
{
	import flash.utils.IDataInput;
	import flash.utils.IDataOutput;
	
	import org.brainfarm.flex.comm.messages.BaseMessage;
	
	public class LoadExperimentMessage extends BaseMessage
	{
		public override function get aliasName():String
		{
			return "org.brainfarm.java.mvcs.service.remote.LoadExperimentMessage";
		}
		
		private var $experiment:String = "";
		
		public function LoadExperimentMessage(experiment:String = null)
		{
			if (experiment)
			{
				$experiment = experiment;
			}
		}
		
		public override function readExternal(input:IDataInput):void 
		{
			super.readExternal(input);

			$experiment = input.readUTF();
		}
		
		public override function writeExternal(output:IDataOutput):void 
		{
			super.writeExternal(output);
			
			output.writeUTF($experiment);
		}
	}
}