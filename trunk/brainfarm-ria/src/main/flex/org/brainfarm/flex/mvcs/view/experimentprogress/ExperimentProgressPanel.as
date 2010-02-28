package org.brainfarm.flex.mvcs.view.experimentprogress
{
	import flash.events.NetStatusEvent;
	import flash.events.SyncEvent;
	
	import mx.containers.Panel;
	import mx.controls.ProgressBar;
	import mx.managers.PopUpManager;
	
	import org.brainfarm.flex.connection.RemoteSharedObject;
	import org.brainfarm.flex.mvcs.controller.BrainFarmContext;
	
	public class ExperimentProgressPanel extends Panel
	{
		public var evolutionProgressBar:ProgressBar;
		
		private var rso:RemoteSharedObject;
		
		/*public function set experiment(value:ExperimentEntry):void 
		{
			if (value)
			{
				BindingUtils.bindProperty(evolutionProgressBar, "percentComplete", value, "progress");
				BindingUtils.bindSetter(experimentComplete, value, "complete");
			} 
		}*/
		
		public function set context(value:BrainFarmContext):void 
		{
			rso = new RemoteSharedObject("evolution", value.client.connection);
			rso.addEventListener(NetStatusEvent.NET_STATUS, onNetStatus);
			rso.addEventListener(SyncEvent.SYNC, onSync);
			rso.connect();
		}
		
		public function ExperimentProgressPanel()
		{
			super();
		}
		
		private function onNetStatus(evt:NetStatusEvent):void 
		{
			trace("NetStatus :: " + evt.info.code);
		}
		
		private function onSync(evt:SyncEvent):void 
		{
			trace("Sync :: " + evt.type);
						
			for each (var obj:* in evt.changeList)
			{
				//trace(obj + " " + rso.sharedObject.data[obj]);
			}
		}
		
		public function experimentComplete(complete:Boolean):void 
		{
			if (complete)
			{
				PopUpManager.removePopUp(this);
			}
		}
	}
}