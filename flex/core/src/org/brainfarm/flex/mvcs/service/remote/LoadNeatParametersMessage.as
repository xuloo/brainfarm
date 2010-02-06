package org.brainfarm.flex.mvcs.service.remote
{
	import org.brainfarm.flex.comm.messages.BaseMessage;
	
	public class LoadNeatParametersMessage extends BaseMessage
	{
		public override function get aliasName():String
		{
			return "org.brainfarm.java.mvcs.service.remote.LoadNeatParametersMessage";
		}
		
		public function LoadNeatParametersMessage()
		{
			super();
		}
		
		public override function toString():String 
		{
			return "LoadNeatParametersMessage[" + senderId + "]";
		}
	}
}