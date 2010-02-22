package org.brainfarm.flex.mvcs.view.neatparams
{
	import mx.binding.utils.BindingUtils;
	import mx.collections.ArrayList;
	import mx.containers.Panel;
	import mx.events.FlexEvent;
	
	import org.brainfarm.flex.mvcs.controller.BrainFarmContext;
	import org.brainfarm.flex.mvcs.controller.IBrainFarmController;
		
	public class NeatParamsPanel extends Panel
	{
		[Bindable]
		public var neatParams:ArrayList;
		
		private var $controller:IBrainFarmController;
		
		public function set controller(value:IBrainFarmController):void 
		{
			$controller = value;
			
			addHandlers();
			addBindings();
		}
		
		public function set context(value:BrainFarmContext):void 
		{
			trace("setting context " + value);
			
			if (value)
			{
				BindingUtils.bindProperty(this, "neatParams", value, ["model", "neatParams"]);
			}
		}
		
		public function NeatParamsPanel()
		{
			super();
			
			addEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
		}
		
		private function onCreationComplete(evt:FlexEvent):void 
		{
			removeEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
		}
		
		private function addHandlers():void 
		{
			
		}
		
		private function addBindings():void 
		{
		}
		
		public function invalidateNeatParams(params:ArrayList):void 
		{
			trace("neat params have changed \n" + params);
			neatParams = params;
		}
	}
}