package org.brainfarm.flex.mvcs.view.evolutionparams
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.events.Event;
	
	import mx.binding.utils.BindingUtils;
	import mx.collections.ArrayCollection;
	import mx.controls.DataGrid;
	import mx.events.DataGridEvent;
	import mx.events.FlexEvent;
	
	import org.brainfarm.flex.mvcs.controller.BrainFarmContext;
	
	import spark.components.Panel;
		
	public class EvolutionParametersPanel extends Panel
	{
		[Bindable]
		public var evolutionParameters:ArrayCollection;
		
		[SkinPart]
		public var parametersDataGrid:DataGrid;
		
		private var $context:BrainFarmContext;
		
		public function set context(value:BrainFarmContext):void 
		{
			trace("setting context " + value);
			
			if (value)
			{
				BindingUtils.bindSetter(invalidateEvolutionParameters, value, ["model", "evolutionParameters"]);
				BindingUtils.bindSetter(invalidateConnectionState, value, ["model", "connected"]);
			}
			
			$context = value;
		}
		
		public function EvolutionParametersPanel()
		{
			super();
			
			addEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
		}
		
		private function onCreationComplete(evt:FlexEvent):void 
		{
			removeEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
			
			parametersDataGrid.addEventListener(DataGridEvent.ITEM_EDIT_END, onCellEditEnd);
		}
		
		private function addHandlers():void 
		{
			
		}
		
		private function addBindings():void 
		{
		}
		
		public function invalidateEvolutionParameters(params:ArrayCollection):void 
		{
			trace("evolution params have changed \n" + params);
			parametersDataGrid.dataProvider = params;
		}
		
		public function invalidateConnectionState(connected:Boolean):void 
		{			
			if (connected)
			{
				trace("loading parameters");
				var op:IOperation = $context.service.loadEvolutionParameters();
				op.addEventListener(Event.COMPLETE, onEvolutionParametersLoaded);
				op.execute();
			}
		}
		
		public function onCellEditEnd(evt:DataGridEvent):void 
		{
			$context.controller.saveNeatParameters();
		}
		
		private function onEvolutionParametersLoaded(evt:Event):void 
		{
			trace("params loaded");
			
			var params:Array = IOperation(evt.target).result as Array;
			
			$context.model.evolutionParameters = new ArrayCollection(params);
		}
	}
}