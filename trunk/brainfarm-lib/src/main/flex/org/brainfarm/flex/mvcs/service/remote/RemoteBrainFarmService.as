package org.brainfarm.flex.mvcs.service.remote
{	
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import org.brainfarm.flex.api.connection.IClient;
	import org.brainfarm.flex.mvcs.model.vo.ExperimentEntry;
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;
	
	public class RemoteBrainFarmService implements IBrainFarmService
	{		
		private var client:IClient;
		
		public function RemoteBrainFarmService(client:IClient) 
		{
			this.client = client;
		}
				
		public function connect(uri:String):IOperation
		{
			return client.connect(uri, ["username", "password"]);	
		}
				
		public function loadEvolutionParameters():IOperation
		{
			return new RemoteServiceOperation(client.connection, "getEvolutionParameters", "brainfarm");
		}
		
		public function saveNeatParameters():IOperation
		{
			return new RemoteServiceOperation(client.connection, "saveNeatParameters", "brainfarm");
		}
		
		public function getAvailableExperiments():IOperation
		{
			return new RemoteServiceOperation(client.connection, "getExperimentList", "brainfarm");
		}
		
		public function loadExperiment(experiment:String):IOperation
		{
			return new LoadExperimentOperation(client.connection, "loadExperiment", experiment, "brainfarm");
		}
		
		public function runExperiment(experiment:ExperimentEntry):IOperation
		{
			return new RemoteServiceOperation(client.connection, "runExperiment", "brainfarm");
		}
		
		public function getEvolutionProgress():IOperation
		{
			return new RemoteServiceOperation(client.connection, "getEvolutionProgress", "brainfarm");
		}
	}
}