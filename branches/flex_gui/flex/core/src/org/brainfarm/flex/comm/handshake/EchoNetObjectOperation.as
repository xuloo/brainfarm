package org.brainfarm.flex.comm.handshake
{
	import com.joeberkovitz.moccasin.service.AbstractOperation;
	
	import flash.net.Responder;
	
	import org.brainfarm.flex.api.connection.INetConnection;
	import org.brainfarm.flex.comm.NetObject;

	public class EchoNetObjectOperation extends AbstractOperation
	{
		private var _connection:INetConnection;
		
		private var _command:String;
		
		private var _object:NetObject;
		
		private var _another:int;
		
		public function EchoNetObjectOperation(connection:INetConnection, command:String, object:NetObject, i:int)
		{
			super();
			
			_connection = connection;
			_command = command;
			_object = object;
			_another = i;
			
			trace("setting object == " + _object);
		}
		
		override public function execute():void
		{
			trace("echoing " + _object + " " + _another + " to server on " + _connection);
			_connection.call(_command, new Responder(handleComplete, handleError), _object);
		}
	}
}