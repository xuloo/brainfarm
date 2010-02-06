package org.brainfarm.flex.mvcs.controller
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.events.Event;
	
	import mx.collections.ArrayList;
	import mx.managers.PopUpManager;
	
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;
	import org.brainfarm.flex.mvcs.view.connection.ConnectionPanelView;
	import org.brainfarm.flex.mvcs.view.experiments.ExperimentSelectionPanelView;
	
	import spark.components.Group;

	public class FlexBrainFarmController implements IBrainFarmController
	{
		private var $service:IBrainFarmService;
		
		private var $context:BrainFarmContext;
		
		private var $viewLayer:Group;
		
		private var $connectionPanel:ConnectionPanelView;
		
		private var $experimentsSelectionPanel:ExperimentSelectionPanelView;
		
		public function FlexBrainFarmController(viewLayer:Group, service:IBrainFarmService, context:BrainFarmContext)
		{
			$viewLayer = viewLayer;
			$service = service;
			$context = context;
		}
		
		public function loadNeatParameters():void
		{
			var operation:IOperation = $service.loadNeatParameters();
			operation.addEventListener(Event.COMPLETE, onNeatParametersLoadComplete);
			operation.execute();
		}
		
		private function onNeatParametersLoadComplete(evt:Event):void 
		{
			trace("neat parameters loaded");
			var params:Array = IOperation(evt.target).result as Array;
			
			trace("params: " + (params is Array));
			
			$context.model.neatParams = new ArrayList(params);
		}
		
		public function saveNeatParameters():void
		{
			var operation:IOperation = $service.saveNeatParameters();
			operation.addEventListener(Event.COMPLETE, onNeatParametersSaveComplete);
			operation.execute();
		}
		
		private function onNeatParametersSaveComplete(evt:Event):void 
		{
			
		}
		
		public function connect(uri:String):void
		{
			var operation:IOperation = $service.connect(uri);
			operation.addEventListener(Event.COMPLETE, onConnectionComplete);
			operation.execute();
		}
		
		private function onConnectionComplete(evt:Event):void 
		{	
			PopUpManager.removePopUp($connectionPanel);
		}
		
		public function showConnectionPanel():void 
		{
			$connectionPanel = PopUpManager.createPopUp($viewLayer, ConnectionPanelView, true) as ConnectionPanelView;
			PopUpManager.centerPopUp($connectionPanel);
			$connectionPanel.controller = this;
		}
		
		public function showAvailableExperiments():void
		{
			$experimentsSelectionPanel = PopUpManager.createPopUp($viewLayer, ExperimentSelectionPanelView, true) as ExperimentSelectionPanelView;
			PopUpManager.centerPopUp($experimentsSelectionPanel);
			$experimentsSelectionPanel.service = $service;
			$experimentsSelectionPanel.controller = this;
		}
		
		
	}
}