package org.brainfarm.flex.mvcs.service
{	
	import mx.rpc.AsyncToken;

	public interface IBrainFarmService
	{		
		function loadNeatParameters():AsyncToken;
		
		function saveNeatParameters():AsyncToken;
		
		function getAvailableExperiments():AsyncToken;
		
		function loadExperiment(experiment:String):AsyncToken;
		
		function runExperiment():AsyncToken;
	}
}