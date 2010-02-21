package org.brainfarm.flex.mvcs.service
{
	import org.brainfarm.flex.mvcs.service.IEvolutionService;
	
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	public class EvolutionService implements IEvolutionService
	{
		private var ro:RemoteObject;
		
		public function EvolutionService(destination:String) 
		{
			ro = new RemoteObject(destination);
		}
		
		public function connect(uri:String):AsyncToken
		{
			return null;
		}
		
		public function loadNeatParameters():AsyncToken
		{
			return null;
		}
		
		public function saveNeatParameters():AsyncToken
		{
			return null;
		}
		
		public function getAvailableExperiments():AsyncToken
		{
			return null;
		}
		
		public function loadExperiment(experiment:String):AsyncToken
		{
			return null;
		}
		
		public function runExperiment():AsyncToken
		{
			return null;
		}
	}
}