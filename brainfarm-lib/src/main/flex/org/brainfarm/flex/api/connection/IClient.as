package org.brainfarm.flex.api.connection
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import org.brainfarm.flex.api.connection.messages.IGroupMessage;
	import org.brainfarm.flex.api.connection.messages.IMessage;
	import org.brainfarm.flex.api.connection.messages.IMessageProcessingService;
	import org.brainfarm.flex.api.connection.messages.IPlayerMessage;
	
	public interface IClient extends org.brainfarm.flex.api.connection.messages.IMessageProcessingService
	{
		function get connection():INetConnection;
		
		function set connection(value:INetConnection):void;
		
		function get sharedObject():ISharedObject;
		
		function set sharedObject(value:ISharedObject):void;
		
		function get handshake():IHandshake;
		
		function set handshake(value:IHandshake):void;
		
		function get id():String;
		
		function set id(value:String):void;
		
		function set defaultService(value:String):void;
		
		function sendToServer(message:IMessage):IOperation;
		
		function sendToPlayer(message:IPlayerMessage):IOperation;
		
		function sendToGroup(message:IGroupMessage):IOperation;
		
		function connect(uri:String = null, connectionArgs:Array = null):IOperation;
	}
}