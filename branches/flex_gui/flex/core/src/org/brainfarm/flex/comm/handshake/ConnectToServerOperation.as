package org.brainfarm.flex.comm.handshake
{
	import com.joeberkovitz.moccasin.service.AbstractOperation;
	
	import org.brainfarm.flex.api.connection.INetConnection;
	import org.brainfarm.flex.comm.events.Red5Event;
	
	public class ConnectToServerOperation extends AbstractOperation
	{
		private var _connection:INetConnection;
		
		private var _username:String;
		
		private var _password:String;
		
		public function ConnectToServerOperation(connection:INetConnection, username:String, password:String)
		{
			super();
			
			_connection = connection;
			_username = username;
			_password = password;
		}
		
		override public function execute():void
		{
			trace("connecting to server " + _connection.rtmpURI + " " + _username + " " + _password);
			_connection.addEventListener(Red5Event.CONNECTED, handleComplete);
			_connection.addEventListener(Red5Event.REJECTED, handleError);
			_connection.connect(_connection.rtmpURI, _username, _password);
		}
		
	}
}