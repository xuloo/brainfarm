package org.brainfarm.flex.mvcs.controller
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.events.Event;
	import flash.net.NetConnection;
	
	import mx.managers.PopUpManager;
	
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;
	import org.brainfarm.flex.mvcs.view.connection.ConnectionPanelView;
	
	import spark.components.Group;

	public class FlexBrainFarmController implements IBrainFarmController
	{
		private var $service:IBrainFarmService;
		
		private var $connection:NetConnection;
		
		private var $viewLayer:Group;
		
		private var $connectionPanel:ConnectionPanelView;
		
		public function FlexBrainFarmController(viewLayer:Group, service:IBrainFarmService)
		{
			$viewLayer = viewLayer;
			$service = service;
		}
		
		public function loadNeatParameters():void
		{
			var operation:IOperation = $service.loadNeatParameters($connection);
			operation.addEventListener(Event.COMPLETE, onNeatParametersLoadComplete);
			operation.execute();
		}
		
		private function onNeatParametersLoadComplete(evt:Event):void 
		{
			trace("neat parameters loaded");
		}
		
		public function saveNeatParameters():void
		{
			var operation:IOperation = $service.saveNeatParameters($connection);
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
			$connection = IOperation(evt.target).result as NetConnection;
			
			PopUpManager.removePopUp($connectionPanel);
		}
		
		public function showConnectionPanel():void 
		{
			$connectionPanel = PopUpManager.createPopUp($viewLayer, ConnectionPanelView, true) as ConnectionPanelView;
			PopUpManager.centerPopUp($connectionPanel);
			$connectionPanel.controller = this;
		}
	}
}