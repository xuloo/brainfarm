package org.brainfarm.flex.mvcs.controller
{
	import mx.collections.ArrayList;
	import mx.managers.PopUpManager;
	import mx.rpc.Responder;
	
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;
	import org.brainfarm.flex.mvcs.view.experimentbuilder.ExperimentBuilderView;
	import org.brainfarm.flex.mvcs.view.experiments.ExperimentSelectionPanelView;
	
	import spark.components.Group;

	public class FlexBrainFarmController implements IBrainFarmController
	{
		private var $service:IBrainFarmService;
		
		private var $context:BrainFarmContext;
		
		private var $viewLayer:Group;
				
		private var $experimentsSelectionPanel:ExperimentSelectionPanelView;
		
		private var $experimentBuilderPanel:ExperimentBuilderView;
		
		public function FlexBrainFarmController(viewLayer:Group, service:IBrainFarmService, context:BrainFarmContext)
		{
			$viewLayer = viewLayer;
			$service = service;
			$context = context;
		}
		
		public function loadNeatParameters():void
		{
			$service.loadNeatParameters().addResponder(new Responder(onNeatParametersLoadComplete, fault));
		}
		
		private function onNeatParametersLoadComplete(obj:Object):void 
		{
			trace("neat parameters loaded");
			var params:Array = obj as Array;
			
			trace("params: " + (params is Array));
			
			$context.model.neatParams = new ArrayList(params);
		}
		
		public function saveNeatParameters():void
		{
			$service.saveNeatParameters().addResponder(new Responder(onNeatParametersSaveComplete, fault));
		}
		
		private function onNeatParametersSaveComplete(obj:Object):void 
		{
			
		}
		
		public function showAvailableExperiments():void
		{
			$experimentsSelectionPanel = PopUpManager.createPopUp($viewLayer, ExperimentSelectionPanelView, true) as ExperimentSelectionPanelView;
			PopUpManager.centerPopUp($experimentsSelectionPanel);
			$experimentsSelectionPanel.service = $service;
			$experimentsSelectionPanel.controller = this;
		}
		
		public function showExperimentBuilder():void 
		{
			$experimentBuilderPanel = PopUpManager.createPopUp($viewLayer, ExperimentBuilderView, true) as ExperimentBuilderView;
			PopUpManager.centerPopUp($experimentBuilderPanel);
			
		}
		
		public function runExperiment():void 
		{
			$service.runExperiment().execute();
		}
		
		private function fault(obj:Object):void 
		{
			
		}
	}
}