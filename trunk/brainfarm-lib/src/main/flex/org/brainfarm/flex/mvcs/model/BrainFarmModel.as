package org.brainfarm.flex.mvcs.model
{
	import mx.collections.ArrayCollection;

	public class BrainFarmModel
	{
		[Bindable]
		public var connected:Boolean = false;
		
		[Bindable]
		public var neatParams:ArrayCollection;
		
		[Bindable]
		public var availableExperiments:ArrayCollection;
		
		public function BrainFarmModel() 
		{
			
		}
	}
}