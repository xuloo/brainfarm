package org.brainfarm.flex.connection
{
	import flash.events.AsyncErrorEvent;
	import flash.events.EventDispatcher;
	import flash.events.NetStatusEvent;
	import flash.events.SyncEvent;
	import flash.net.NetConnection;
	import flash.net.SharedObject;
	
	import org.brainfarm.flex.api.connection.INetConnection;
	import org.brainfarm.flex.api.connection.ISharedObject;
	import org.brainfarm.flex.connection.events.Red5Event;

	public class RemoteSharedObject extends EventDispatcher implements ISharedObject
	{
		private var _persistent:Boolean;
		
		public function get persistent():Boolean
		{
			return _persistent;
		}
		
		public function set persistent(value:Boolean):void 
		{
			_persistent = value;
		}
		
		private var _secure:Boolean;
		
		public function get secure():Boolean
		{
			return _secure;
		}
		
		public function set secure(value:Boolean):void 
		{
			_secure = value;
		}
		
		private var _connection:INetConnection;
		
		public function get connection():INetConnection
		{
			return _connection;
		}
		
		public function set connection(value:INetConnection):void 
		{
			if (value != null)
			{
				_connection = value;
				
				if( connection.connected ) 
				{
					connect();
				} 
				else 
				{
					connection.addEventListener( Red5Event.CONNECTED, onConnectionConnected );
					connection.addEventListener( Red5Event.DISCONNECTED, onConnectionDisconnected );
				}
			}
		}
		
		private var _name:String;
		
		public function get name():String 
		{
			return _name;
		}
		
		public function set name(value:String):void 
		{
			_name = value;
		}
		
		private var _sharedObject:SharedObject;
		
		public function get sharedObject():SharedObject
		{
			return _sharedObject;
		}
		
		public function set sharedObject(value:SharedObject):void 
		{
			_sharedObject = value;
		}
		
		public function RemoteSharedObject(name:String, connection:INetConnection, persistent:Boolean = false, secure:Boolean = false )
		{
			super(this);
			
			this.name = name;
			this.persistent = persistent;
			this.secure = secure;
			this.connection = connection;
		}
		
		public function connect():void
		{
			sharedObject = SharedObject.getRemote( name, connection.rtmpURI, persistent, secure )
			sharedObject.connect( NetConnection(connection) );
			sharedObject.addEventListener(NetStatusEvent.NET_STATUS, onNetStatus);
			sharedObject.addEventListener(SyncEvent.SYNC, onSync);
			sharedObject.addEventListener(AsyncErrorEvent.ASYNC_ERROR, onAsyncError);
		}
		
		protected function onNetStatus(event:NetStatusEvent) : void 
		{
			dispatchEvent(event);
		}
		
		protected function onSync(event:SyncEvent) : void 
		{
			dispatchEvent(event);
		}
		
		protected function onAsyncError(event:AsyncErrorEvent) : void 
		{
			dispatchEvent(event);
		}
		
		private function onConnectionConnected( value:Red5Event ):void
		{
			connect();
		}
		
		private function onConnectionDisconnected( value:Red5Event ):void
		{
			
		}
	}
}