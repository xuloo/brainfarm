package org.brainfarm.flex.api.connection.messages
{
	import flash.events.IEventDispatcher;
	
	import org.brainfarm.flex.api.connection.IProcessor;
	
	public interface IMessageProcessingService extends IEventDispatcher
	{
		function receiveMessage(message:IMessage):void;
		
		function addMessageProcessor(processor:IProcessor):void;
	}
}