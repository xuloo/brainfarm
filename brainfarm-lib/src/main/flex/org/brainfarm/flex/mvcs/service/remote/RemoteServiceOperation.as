package org.brainfarm.flex.mvcs.service.remote
{
	import com.joeberkovitz.moccasin.service.AbstractOperation;
	
	import flash.net.NetConnection;
	import flash.net.Responder;
	
	public class RemoteServiceOperation extends AbstractOperation
	{
		protected var connection:NetConnection;
		
		protected var command:String;
		
		protected var _result:*;
		
		public override function get result():*
		{
			return _result;
		}
		
		public function RemoteServiceOperation(connection:NetConnection, method:String, service:String = null)
		{
			super();
			
			this.connection = connection;
			command = createCommand(service, method);
		}
		
		private function createCommand(method:String, service:String):String 
		{
			if (service == null)
			{
				return method;
			}
			
			return service + "." + method;
		}
		
		public override function execute() : void
		{
			connection.call(command, new Responder(onResult, onFault));
		}
		
		protected function onResult(obj:Object):void 
		{
			_result = obj;
			
			handleComplete(null);
		}
		
		protected function onFault(obj:Object):void 
		{
			handleError(null);	
		}
	}
}