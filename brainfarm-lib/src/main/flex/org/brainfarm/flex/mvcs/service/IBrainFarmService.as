package org.brainfarm.flex.mvcs.service
{	
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import org.brainfarm.flex.mvcs.model.vo.ExperimentEntry;

	public interface IBrainFarmService
	{		
		function connect(uri:String):IOperation;
		
		function loadEvolutionParameters():IOperation;
		
		function saveNeatParameters():IOperation;
		
		function getAvailableExperiments():IOperation;
		
		function loadExperiment(experiment:String):IOperation;
		
		function runExperiment(experiment:ExperimentEntry):IOperation;
		
		function getEvolutionProgress():IOperation;
	}
}