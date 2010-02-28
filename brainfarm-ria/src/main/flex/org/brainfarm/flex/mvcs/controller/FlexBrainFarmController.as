package org.brainfarm.flex.mvcs.controller
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.events.Event;
	
	import mx.core.UIComponent;
	import mx.managers.PopUpManager;
	
	import org.brainfarm.flex.connection.RemoteSharedObject;
	import org.brainfarm.flex.mvcs.model.vo.ExperimentEntry;
	import org.brainfarm.flex.mvcs.view.connection.ConnectionPanelView;
	import org.brainfarm.flex.mvcs.view.experimentbuilder.ExperimentBuilderView;
	import org.brainfarm.flex.mvcs.view.experimentprogress.ExperimentProgressPanelView;

	public class FlexBrainFarmController implements IBrainFarmController
	{		
		private var $context:BrainFarmContext;
		
		private var $viewLayer:UIComponent;
		
		private var $connectionPanel:ConnectionPanelView;
		
		private var $experimentBuilderPanel:ExperimentBuilderView;
		
		private var $experimentProgressPanel:ExperimentProgressPanelView;
		
		public function FlexBrainFarmController(viewLayer:UIComponent, context:BrainFarmContext)
		{
			$viewLayer = viewLayer;
			$context = context;
			
			$context.model.selectedExperiment
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
			trace("saving neat parameters");
			
			var op:IOperation = $context.service.saveNeatParameters();
			op.addEventListener(Event.COMPLETE, onNeatParametersSaveComplete);
			op.execute();
		}
		
		private function onNeatParametersSaveComplete(obj:Object):void 
		{
			
		}
		
		public function runExperiment(experiment:ExperimentEntry):void 
		{
			trace("running experiment: " + experiment.name);
			
			$experimentProgressPanel = PopUpManager.createPopUp($viewLayer, ExperimentProgressPanelView, true) as ExperimentProgressPanelView;
			PopUpManager.centerPopUp($experimentProgressPanel);
			$experimentProgressPanel.context = $context;
			
			var op:IOperation = $context.service.runExperiment(experiment);
			op.addEventListener(Event.COMPLETE, onRunExperimentComplete);
			op.execute();
		}
		
		private function onRunExperimentComplete(evt:Event):void 
		{
			trace("experiment run complete");
		}
		
		public function showExperimentBuilder():void 
		{
			$experimentBuilderPanel = PopUpManager.createPopUp($viewLayer, ExperimentBuilderView, true) as ExperimentBuilderView;
			PopUpManager.centerPopUp($experimentBuilderPanel);
			
		}
		
		private function fault(obj:Object):void 
		{
			
		}
	}
}