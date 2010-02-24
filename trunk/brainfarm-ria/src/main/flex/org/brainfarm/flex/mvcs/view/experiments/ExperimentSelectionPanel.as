package org.brainfarm.flex.mvcs.view.experiments
{		
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.binding.utils.BindingUtils;
	import mx.collections.ArrayList;
	import mx.containers.Panel;
	import mx.controls.Button;
	import mx.controls.List;
	import mx.events.FlexEvent;
	import mx.managers.PopUpManager;
	
	import org.brainfarm.flex.mvcs.controller.BrainFarmContext;
	import org.brainfarm.flex.mvcs.controller.IBrainFarmController;
	import org.brainfarm.flex.mvcs.model.vo.ExperimentEntry;
	
	public class ExperimentSelectionPanel extends mx.containers.Panel
	{	
		public var loadButton:Button;
		public var cancelButton:Button;
		public var experimentsList:List;
		
		[Bindable]
		public var availableExperiments:ArrayList;
		
		private var $selectedExperiment:ExperimentEntry;
		
		private var $context:BrainFarmContext;
		
		public function set context(value:BrainFarmContext):void
		{
			trace("setting context on ExperimentPanel");
			
			if (value)
			{
				BindingUtils.bindProperty(this, "availableExperiments", value, ["model", "availableExperiments"]);
				BindingUtils.bindSetter(invalidateConnectionState, value, ["model", "connected"]);
			}
			
			$context = value;
		}
		
		private var $controller:IBrainFarmController;
		
		public function set controller(value:IBrainFarmController):void 
		{
			$controller = value;
		}
		
		public function ExperimentSelectionPanel()
		{
			super();
			
			addEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
		}
		
		private function onCreationComplete(evt:FlexEvent):void 
		{
			removeEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
			
			addHandlers();
			setBindings();
		}
		
		private function addHandlers():void 
		{
			//cancelButton.addEventListener(MouseEvent.CLICK, onCancelButtonClick);
			//loadButton.addEventListener(MouseEvent.CLICK, onLoadButtonClick);
		}
		
		private function setBindings():void 
		{
			BindingUtils.bindSetter(invalidateExperimentSelection, experimentsList, "selectedItem");
		}
		
		private function invalidateConnectionState(state:Boolean):void 
		{
			trace("Experiment Panel handling connection state change");
			
			if (state)
			{
				loadAvailableExperiments();
			}
		}
		
		private function loadAvailableExperiments():void 
		{
			trace("loading experiments");
			var op:IOperation = $context.service.getAvailableExperiments();
			op.addEventListener(Event.COMPLETE, onExperimentsLoaded);
			op.execute();
		}
		
		private function invalidateExperimentSelection(value:ExperimentEntry):void 
		{
			
			$selectedExperiment = value;
		}
		
		private function onExperimentsLoaded(obj:Object):void 
		{
			trace("experiments loaded")
			
			var list:Array = obj as Array;
			
			availableExperiments = new ArrayList(list);
		}
		
		private function onLoadButtonClick(evt:MouseEvent):void 
		{
			if ($selectedExperiment)
			{
				trace("Loading experiment: " + $selectedExperiment);
				loadExperiment($selectedExperiment.fileName);
			}
		}
		
		private function onCancelButtonClick(evt:MouseEvent):void 
		{
			PopUpManager.removePopUp(this);
		}
		
		public function loadExperiment(experiment:String):void 
		{
			var op:IOperation = $context.service.loadExperiment(experiment);
			op.addEventListener(Event.COMPLETE, onExperimentLoaded);
			op.execute();
		}
		
		private function onExperimentLoaded(obj:Object):void 
		{
			trace("Experiment Loaded");
			
			PopUpManager.removePopUp(this);
		}
		
		private function fault(obj:Object):void 
		{
			
		}
	}
}