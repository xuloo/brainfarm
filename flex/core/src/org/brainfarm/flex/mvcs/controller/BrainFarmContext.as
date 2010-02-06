package org.brainfarm.flex.mvcs.controller
{
	import org.brainfarm.flex.mvcs.model.BrainFarmModel;

	public class BrainFarmContext
	{
		[Bindable]
		public var model:BrainFarmModel;
		
		public var controller:IBrainFarmController;
	}
}