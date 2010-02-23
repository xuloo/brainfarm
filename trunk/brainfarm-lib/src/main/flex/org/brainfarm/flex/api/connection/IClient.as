package org.brainfarm.flex.api.connection
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	public interface IClient
	{
		function get connection():INetConnection;
		
		function set connection(value:INetConnection):void;
		
		function get sharedObject():ISharedObject;
		
		function set sharedObject(value:ISharedObject):void;
				
		function get id():String;
		
		//function set id(value:String):void;
		
		function set defaultService(value:String):void;
		
		function sendToServer(message:IOperation):IOperation;
		
		function connect(uri:String = null, connectionArgs:Array = null):IOperation;
	}
}