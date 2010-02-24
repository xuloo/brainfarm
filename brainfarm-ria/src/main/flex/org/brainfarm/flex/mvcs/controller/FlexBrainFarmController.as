package org.brainfarm.flex.mvcs.controller
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.events.Event;
	
	import mx.collections.ArrayList;
	import mx.core.UIComponent;
	import mx.managers.PopUpManager;
	
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;
	import org.brainfarm.flex.mvcs.view.connection.ConnectionPanelView;
	import org.brainfarm.flex.mvcs.view.experimentbuilder.ExperimentBuilderView;
	import org.brainfarm.flex.mvcs.view.experiments.ExperimentSelectionPanelView;

	public class FlexBrainFarmController implements IBrainFarmController
	{		
		private var $context:BrainFarmContext;
		
		private var $viewLayer:UIComponent;
		
		private var $connectionPanel:ConnectionPanelView;
				
		private var $experimentsSelectionPanel:ExperimentSelectionPanelView;
		
		private var $experimentBuilderPanel:ExperimentBuilderView;
		
		public function FlexBrainFarmController(viewLayer:UIComponent, context:BrainFarmContext)
		{
			$viewLayer = viewLayer;
			$context = context;
		}
		
		public function showConnectionPanel():void 
		{
			$connectionPanel = PopUpManager.createPopUp($viewLayer, ConnectionPanelView, true) as ConnectionPanelView;
			PopUpManager.centerPopUp($connectionPanel);
			$connectionPanel.context = $context;
		}
		
		private function onConnectionComplete(evt:Event):void 
		{
			trace("")
		}
			
		public function saveNeatParameters():void
		{
			var op:IOperation = $context.service.saveNeatParameters();
			op.addEventListener(Event.COMPLETE, onNeatParametersSaveComplete);
			op.execute();
		}
		
		private function onNeatParametersSaveComplete(obj:Object):void 
		{
			
		}
		
		public function showAvailableExperiments():void
		{
			$experimentsSelectionPanel = PopUpManager.createPopUp($viewLayer, ExperimentSelectionPanelView, true) as ExperimentSelectionPanelView;
			PopUpManager.centerPopUp($experimentsSelectionPanel);
			//$experimentsSelectionPanel.service = $service;
			$experimentsSelectionPanel.controller = this;
		}
		
		public function showExperimentBuilder():void 
		{
			$experimentBuilderPanel = PopUpManager.createPopUp($viewLayer, ExperimentBuilderView, true) as ExperimentBuilderView;
			PopUpManager.centerPopUp($experimentBuilderPanel);
			
		}
		
		public function runExperiment():void 
		{
			$context.service.runExperiment().execute();
		}
		
		private function fault(obj:Object):void 
		{
			
		}
	}
}