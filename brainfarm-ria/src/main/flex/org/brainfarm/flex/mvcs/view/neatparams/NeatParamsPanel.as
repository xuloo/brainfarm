package org.brainfarm.flex.mvcs.view.neatparams
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import mx.binding.utils.BindingUtils;
	import mx.collections.ArrayList;
	import mx.containers.Panel;
	import mx.events.FlexEvent;
	
	import org.brainfarm.flex.mvcs.controller.BrainFarmContext;
		
	public class NeatParamsPanel extends Panel
	{
		[Bindable]
		public var neatParams:ArrayList;
		
		private var $context:BrainFarmContext;
		
		public function set context(value:BrainFarmContext):void 
		{
			trace("setting context " + value);
			
			if (value)
			{
				BindingUtils.bindProperty(this, "neatParams", value, ["model", "neatParams"]);
				BindingUtils.bindSetter(invalidateConnectionState, value, ["model", "connected"]);
			}
			
			$context = value;
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
		
		public function invalidateConnectionState(connected:Boolean):void 
		{
			if (connected)
			{
				trace("loading parameters");
				$context.service.loadEvolutionParameters().execute();
			}
		}
	}
}