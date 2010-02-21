package org.brainfarm.flex.mvcs.service 
{
	import mx.rpc.AsyncToken;
	
	public interface IEvolutionService
	{
		function connect(uri:String):AsyncToken;
		
		function loadNeatParameters():AsyncToken;
		
		function saveNeatParameters():AsyncToken;
		
		function getAvailableExperiments():AsyncToken;
		
		function loadExperiment(experiment:String):AsyncToken;
		
		function runExperiment():AsyncToken;
	}
}