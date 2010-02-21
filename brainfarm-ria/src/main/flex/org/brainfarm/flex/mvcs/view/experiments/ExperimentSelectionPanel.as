package org.brainfarm.flex.mvcs.view.experiments
{		
	import flash.events.MouseEvent;
	
	import mx.binding.utils.BindingUtils;
	import mx.collections.ArrayList;
	import mx.events.FlexEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.Responder;
	
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
			$service.getAvailableExperiments().addResponder(new Responder(onExperimentsLoaded, fault));
		}
		
		private function invalidateExperimentSelection(value:ExperimentEntry):void 
		{
			$selectedExperiment = value;
		}
		
		private function onExperimentsLoaded(obj:Object):void 
		{
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
			$service.loadExperiment(experiment).addResponder(new Responder(onExperimentLoaded, fault));
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