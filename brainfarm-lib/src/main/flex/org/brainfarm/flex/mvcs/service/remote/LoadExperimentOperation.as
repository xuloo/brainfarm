package org.brainfarm.flex.mvcs.service.remote
{
	import flash.net.NetConnection;
	import flash.net.Responder;
	
	public class LoadExperimentOperation extends RemoteServiceOperation
	{
		private var experiment:String;
		
		public function LoadExperimentOperation(connection:NetConnection, method:String, experiment:String, service:String=null)
		{
			super(connection, method, service);
			
			this.experiment = experiment;
		}
		
		public override function execute():void 
		{
			connection.call(command, new Responder(onResult, onFault), experiment);
		}
	}
}