package org.brainfarm.flex.mvcs.view.experiments
{	
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.binding.utils.BindingUtils;
	import mx.collections.ArrayList;
	import mx.events.FlexEvent;
	import mx.managers.PopUpManager;
	
	import org.brainfarm.flex.mvcs.controller.IBrainFarmController;
	import org.brainfarm.flex.mvcs.model.vo.ExperimentEntry;
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;
	
	import spark.components.Button;
	import spark.components.List;
	import spark.components.Panel;
	
	public class ExperimentSelectionPanel extends Panel
	{	
		public var loadButton:Button;
		public var cancelButton:Button;
		public var experimentsList:List;
		
		[Bindable]
		public var availableExperiments:ArrayList;
		
		private var $selectedExperiment:ExperimentEntry;
		
		private var $service:IBrainFarmService;
		
		public function set service(value:IBrainFarmService):void
		{
			if (value)
			{
				$service = value;
				loadAvailableExperiments();
			}
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
			cancelButton.addEventListener(MouseEvent.CLICK, onCancelButtonClick);
			loadButton.addEventListener(MouseEvent.CLICK, onLoadButtonClick);
		}
		
		private function setBindings():void 
		{
			BindingUtils.bindSetter(invalidateExperimentSelection, experimentsList, "selectedItem");
		}
		
		private function loadAvailableExperiments():void 
		{
			var operation:IOperation = $service.getAvailableExperiments();
			operation.addEventListener(Event.COMPLETE, onExperimentsLoaded);
			operation.execute();
		}
		
		private function invalidateExperimentSelection(value:ExperimentEntry):void 
		{
			$selectedExperiment = value;
		}
		
		private function onExperimentsLoaded(evt:Event):void 
		{
			var list:Array = IOperation(evt.target).result as Array;
			
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
			var operation:IOperation = $service.loadExperiment(experiment);
			operation.addEventListener(Event.COMPLETE, onExperimentLoaded);
			operation.execute();
		}
		
		private function onExperimentLoaded(evt:Event):void 
		{
			trace("Experiment Loaded");
		}
	}
}