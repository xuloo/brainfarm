package org.brainfarm.flex.mvcs.controller
{
	public interface IBrainFarmController
	{		
		function connect(uri:String):void;
		
		function loadNeatParameters():void;
		
		function saveNeatParameters():void;
	}
}