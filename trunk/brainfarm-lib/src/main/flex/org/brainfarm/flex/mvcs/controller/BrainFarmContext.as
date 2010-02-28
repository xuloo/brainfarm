package org.brainfarm.flex.mvcs.controller
{
	import org.brainfarm.flex.api.connection.IClient;
	import org.brainfarm.flex.mvcs.model.BrainFarmModel;
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;

	public class BrainFarmContext
	{
		[Bindable]
		public var model:BrainFarmModel;
		
		//public var controller:IBrainFarmController;
		
		public var service:IBrainFarmService;
		
		public var controller:IBrainFarmController;
		
		public var client:IClient;
		
		public function BrainFarmContext()
		{
			
		}
	}
}