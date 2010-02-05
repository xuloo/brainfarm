package org.brainfarm.flex.mvcs.controller
{
	public class MenuHelper
	{
		private var $controller:FlexBrainFarmController;
		
		public function MenuHelper(controller:FlexBrainFarmController)
		{
			$controller = controller;
		}
		
		public function handleMenuItem(item:Object):void 
		{
			var data:String = item.@data;
			
			switch (data)
			{
				case "connect":
					$controller.showConnectionPanel();
					break;
				
				case "loadNeatParameters":
					$controller.loadNeatParameters();
					break;
				
				default:
					break;
			}
		}
	}
}