package org.brainfarm.flex.connection.client
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import org.brainfarm.flex.api.connection.IClient;
	import org.brainfarm.flex.api.connection.INetConnection;
	import org.brainfarm.flex.api.connection.ISharedObject;
	import org.brainfarm.flex.connection.Red5Connection;
	import org.brainfarm.flex.connection.handshake.ConnectToServerOperation;
	import org.brainfarm.flex.connection.messages.SendMessageOperation;

	/**
	 * A simple implementation of the IClient interface. This client wraps a single Red5Connection/RemoteSharedObject
	 * pair. It uses the NetConnection to send messages to the server and the RemoteSharedObject to receive
	 * Synchronisation messages from the server.
	 */
	public class BasicClient extends EventDispatcher implements IClient
	{
		private var _defaultService:String;
		
		public function set defaultService(value:String):void 
		{
			_defaultService = value;
		}
		
		/**
		 * @private
		 */
		private var _id:String;
		
		/**
		 * The unique id assigned to this client's connection by the server.
		 */
		public function get id():String 
		{
			return _id;
		}
		
		/**
		 * @private
		 *//*
		public function set id(value:String):void
		{
			_id = value;
		}*/
		
		/**
		 * @private
		 */
		private var _connection:INetConnection;
		
		/**
		 * The underlying NetConnection this client uses to send/receive messages.
		 */
		public function get connection():INetConnection
		{
			return _connection;
		}
		
		/**
		 * @private.
		 */
		public function set connection(value:INetConnection):void 
		{
			_connection = value;
			connection.client = this;
			
			if (sharedObject)
			{
				sharedObject.connection = connection;
			}
		}
		
		private var _sharedObject:ISharedObject;
		
		public function get sharedObject():ISharedObject
		{
			return _sharedObject;
		}
		
		public function set sharedObject(value:ISharedObject):void 
		{
			if (value != null)
			{
				_sharedObject = value;
				
				if (connection)
				{
					sharedObject.connection = connection;
				}
				
				//sharedObject.addEventListener(SyncEvent.SYNC, onSync);
			}
		}
						
		public function BasicClient(connection:INetConnection = null, sharedObject:ISharedObject = null)
		{
			super(this);
			
			if (connection)
				this.connection = connection;
				
			if (sharedObject)
				this.sharedObject = sharedObject;
			
			init();
		}
		
		protected function init():void 
		{
			
		}
		
		/**
		 * Begins the quick handshake procedure - extend the handshake class to 
		 * implement a custom handshake for your game.
		 */
		public function connect(uri:String = null, connectionArgs:Array = null):IOperation 
		{
			if (uri != null)
			{
				trace("creating connection");
				connection = new Red5Connection();
				connection.rtmpURI = uri;
			}
			
			if (connectionArgs != null)
			{
				connection.connectionArgs = connectionArgs;
			}
			
			trace("BasicClient connecting " + connection.rtmpURI);
			
			return new ConnectToServerOperation(connection, connectionArgs[0], connectionArgs[1]);
		}
		
		/**
		 * Once the connection with the server is established we need
		 * to set the connection's client property back to this object
		 * so we can catch incoming calls to receiveMessage.
		 */
		private function onConnectionEstablished(e:Event):void 
		{
			connection.client = this;
		}
		
		public function sendToServer(message:IOperation):IOperation 
		{
			return _sendMessage(message);
		}
		
		private function _sendMessage(message:IOperation):IOperation 
		{
			trace("Sending " + message + " to server");

			return new SendMessageOperation(message, connection);
		}
		
		public function setClientID(id:String):void 
		{
			trace("Setting client id: " + id);
			
			_id = id;
		}
				
		/**
		 * Handles SyncEvent.SYNC events from the remote shared object.
		 * If a new message has been added to the shared object then
		 * we need to handle it.
		 *//*
		private function onSync(event:SyncEvent):void 
		{
			var changeList:Array = event.changeList;
			var so:SharedObject = sharedObject.sharedObject;
			
			// Iterate over each client whose properties have changed.
			for (var i:int; i < changeList.length; i++)
			{
				var id:String = changeList[i].name;

				// Grab the object related to the client.
				var object:Object = so.data[id];
				var message:IMessage = IMessage(so.data[id]);

				// If the object is of type IMessage then process it.
				if (message)
				{
					if (message is BatchMessage)
					{
						for each (var m:IMessage in BatchMessage(message).messages)
						{
							receiveMessage(m);
						}
					}
					else
					{
						receiveMessage(message);
					}
				}
			}
		}*/
	}
}