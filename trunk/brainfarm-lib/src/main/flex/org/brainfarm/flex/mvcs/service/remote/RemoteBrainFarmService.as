package org.brainfarm.flex.mvcs.service.remote
{	
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.net.NetConnection;
	
	import org.brainfarm.flex.api.connection.IClient;
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;
	
	public class RemoteBrainFarmService implements IBrainFarmService
	{
		private var connection:NetConnection;
		
		private var client:IClient;
		
		public function RemoteBrainFarmService(client:IClient) 
		{
			this.client = client;	
		}
		
		public function connect(uri:String):IOperation
		{
			return client.connect(uri, ["username", "password"]);	
		}
				
		public function loadNeatParameters():IOperation
		{
			return new RemoteServiceOperation(connection, "loadNeatParameters", "brainfarm");
		}
		
		public function saveNeatParameters():IOperation
		{
			return new RemoteServiceOperation(connection, "saveNeatParameters", "brainfarm");
		}
		
		public function getAvailableExperiments():IOperation
		{
			return new RemoteServiceOperation(connection, "getAvailableExperiments", "brainfarm");
		}
		
		public function loadExperiment(experiment:String):IOperation
		{
			return new LoadExperimentOperation(connection, "loadExperiment", experiment, "brainfarm");
		}
		
		public function runExperiment():IOperation
		{
			return new RemoteServiceOperation(connection, "runExperiment", "brainfarm");
		}
	}
}