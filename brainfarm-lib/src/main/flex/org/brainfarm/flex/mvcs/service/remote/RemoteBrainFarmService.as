package org.brainfarm.flex.mvcs.service.remote
{	
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.RemoteObject;
	
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;
	
	public class RemoteBrainFarmService implements IBrainFarmService
	{
		private var ro:RemoteObject;
		
		public function RemoteBrainFarmService(destination:String) 
		{
			ro = new RemoteObject(destination);
		}
				
		public function loadNeatParameters():AsyncToken
		{
			return ro.loadNeatParameters();
		}
		
		public function saveNeatParameters():AsyncToken
		{
			return ro.saveNeatParameters();
		}
		
		public function getAvailableExperiments():AsyncToken
		{
			return ro.getAvailableExperiments();
		}
		
		public function loadExperiment(experiment:String):AsyncToken
		{
			return ro.loadExperiment(experiment);
		}
		
		public function runExperiment():AsyncToken
		{
			return ro.runExperiment();
		}
	}
}