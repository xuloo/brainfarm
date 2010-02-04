package org.brainfarm.flex.mvcs.service.remote
{
	import com.joeberkovitz.moccasin.service.AbstractOperation;
	
	import flash.events.NetStatusEvent;
	import flash.net.NetConnection;
	
	public class RemoteConnectionOperation extends AbstractOperation
	{
		private var $uri:String;
		
		private var $connection:NetConnection;
		
		public function RemoteConnectionOperation(uri:String)
		{
			super();
			
			$uri = uri;
		}
		
		public override function execute() : void
		{
			$connection = new NetConnection();
			$connection.addEventListener(NetStatusEvent.NET_STATUS, onNetStatus);
			$connection.connect($uri);
		}
		
		private function onNetStatus(evt:NetStatusEvent):void 
		{
			var code:String = evt.info.code;
			trace(code);
			switch (code)
			{
				case "NetConnection.Connect.Success":
					handleComplete(null);
					break;
				
				default:
					break;
			}
		}
		
		public override function get result() : *
		{
			return $connection;
		}
	}
}