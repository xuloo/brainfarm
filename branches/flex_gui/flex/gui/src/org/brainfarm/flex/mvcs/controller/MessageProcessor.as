package org.brainfarm.flex.mvcs.controller
{
	import org.brainfarm.flex.comm.AbstractProcessor;
	
	public class MessageProcessor extends AbstractProcessor
	{
		public function MessageProcessor()
		{
			super([ExperimentResultMessage]);
		}
		
		public function process(object:*):void
		{
		}
	}
}