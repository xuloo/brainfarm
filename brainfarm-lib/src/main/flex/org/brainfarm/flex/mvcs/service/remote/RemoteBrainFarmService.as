package org.brainfarm.flex.mvcs.service.remote
{	
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.events.Event;
	
	import org.brainfarm.flex.api.connection.IClient;
	import org.brainfarm.flex.mvcs.model.vo.ExperimentEntry;
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;
	
	public class RemoteBrainFarmService implements IBrainFarmService
	{		
		private var client:IClient;
		
		private var runExperimentOperation:RunExperimentOperation;
		
		public function RemoteBrainFarmService(client:IClient) 
		{
			this.client = client;
		}
				
		public function connect(uri:String):IOperation
		{
			var operation:IOperation = client.connect(uri, ["username", "password"]);
			operation.addEventListener(Event.COMPLETE, onClientConnected);
			return operation;	
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
			return runExperimentOperation = new RunExperimentOperation(client.connection, "runExperiment", "brainfarm");
		}
		
		public function getEvolutionProgress():IOperation
		{
			return new RemoteServiceOperation(client.connection, "getEvolutionProgress", "brainfarm");
		}
		
		public function evolutionComplete(result:String):void
		{
			runExperimentOperation.result = new XML(result);
			runExperimentOperation.complete();
		}
		
		private function onClientConnected(evt:Event):void 
		{
			trace("client connected - setting service as client on connection");
			client.connection.client = this;
		}
	}
}