package org.brainfarm.flex.mvcs.view.connection
{
	import flash.events.MouseEvent;
	
	import mx.events.FlexEvent;
	
	import org.brainfarm.flex.mvcs.controller.BrainFarmContext;
	import org.brainfarm.flex.mvcs.controller.IBrainFarmController;
	
	import spark.components.Button;
	import spark.components.Panel;
	import spark.components.TextInput;
	
	public class ConnectionPanel extends Panel
	{
		public var uriInput:TextInput;
		public var connectButton:Button;
		
		[Bindable]
		public var uri:String = "rtmp://localhost/brainfarm-server";
		
		private var $connecting:Boolean = false;
		
		private var $controller:IBrainFarmController;
		
		public function set controller(value:IBrainFarmController):void 
		{
			$controller = value;
		}
		
		public function ConnectionPanel()
		{
			super();
			
			addEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
		}
		
		private function onCreationComplete(evt:FlexEvent):void 
		{
			removeEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
			
			createHandlers();
			createBindings();
			
			uriInput.text = uri;
		}
		
		private function createHandlers():void 
		{
			connectButton.addEventListener(MouseEvent.CLICK, onConnectButtonClick);
		}
		
		private function createBindings():void 
		{
			
		}
		
		private function onConnectButtonClick(evt:MouseEvent):void 
		{
			$controller.connect(uriInput.text);
		}
	}
}