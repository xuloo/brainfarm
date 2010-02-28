package org.brainfarm.flex.mvcs.service.remote
{
	import org.brainfarm.flex.api.connection.INetConnection;
	
	public class RunExperimentOperation extends RemoteServiceOperation
	{
		public function set result(value:*):void 
		{
			_result = value;
		}
		
		public function RunExperimentOperation(connection:INetConnection, method:String, service:String=null)
		{
			super(connection, method, service);
		}
		
		public function complete():void 
		{
			handleComplete(null);
		}
		
		protected override function onResult(obj:Object):void 
		{
			_result = obj;
		}
	}
}