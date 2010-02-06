package org.brainfarm.flex.mvcs.service.remote
{
	import org.brainfarm.flex.comm.messages.BaseMessage;
	
	public class GetAvailableExperimentsMessage extends BaseMessage
	{
		public override function get aliasName() : String
		{
			return "org.brainfarm.java.mvcs.service.remote.GetAvailableExperimentsMessage";
		}
	}
}