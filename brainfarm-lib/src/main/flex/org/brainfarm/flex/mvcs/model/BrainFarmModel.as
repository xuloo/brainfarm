package org.brainfarm.flex.mvcs.model
{
	import mx.collections.ArrayCollection;
	
	import org.brainfarm.flex.mvcs.model.vo.ExperimentEntry;

	public class BrainFarmModel
	{
		[Bindable]
		public var connected:Boolean = false;
		
		[Bindable]
		public var evolutionParameters:ArrayCollection;
		
		[Bindable]
		public var availableExperiments:ArrayCollection;
		
		[Bindable]
		public var selectedExperiment:ExperimentEntry;
		
		public function BrainFarmModel() 
		{
			
		}
	}
}