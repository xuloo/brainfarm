package org.brainfarm.flex.mvcs.view.connection
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.binding.utils.BindingUtils;
	import mx.containers.Panel;
	import mx.controls.Button;
	import mx.controls.CheckBox;
	import mx.controls.TextInput;
	import mx.events.FlexEvent;
	import mx.managers.PopUpManager;
	
	import org.brainfarm.flex.mvcs.controller.BrainFarmContext;
		
	public class ConnectionPanel extends Panel
	{
		public var uriCheckBox:CheckBox;
		public var uriInput:TextInput;
		public var connectButton:Button;
		
		[Bindable]
		public var uri:String = "rtmp://localhost/brainfarm-web";
		
		private var $connecting:Boolean = false;
		
		private var $context:BrainFarmContext;
		
		public function set context(value:BrainFarmContext):void 
		{
			$context = value;
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
			BindingUtils.bindSetter(invalidateCheckBox, uriCheckBox, "selected");
		}
		
		private function invalidateCheckBox(selected:Boolean):void 
		{			
			uriInput.enabled = !selected;
		}
		
		private function onConnectButtonClick(evt:MouseEvent):void 
		{			
			var op:IOperation = $context.service.connect(uri);
			op.addEventListener(Event.COMPLETE, onConnectionComplete);
			op.execute();
		}
		
		private function onConnectionComplete(evt:Event):void 
		{
			$context.model.connected = true;
			
			PopUpManager.removePopUp(this);
		}
	}
}