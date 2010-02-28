package org.brainfarm.flex.mvcs.controller
{
	import org.brainfarm.flex.mvcs.model.vo.ExperimentEntry;

	public interface IBrainFarmController
	{						
		function saveNeatParameters():void;
		
		function runExperiment(experiment:ExperimentEntry):void
	}
}