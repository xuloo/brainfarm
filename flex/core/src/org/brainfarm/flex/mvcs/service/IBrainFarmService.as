package org.brainfarm.flex.mvcs.service
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.net.NetConnection;
	
	public interface IBrainFarmService
	{
		function connect(uri:String):IOperation;
		
		function loadNeatParameters():IOperation;
		
		function saveNeatParameters():IOperation;
	}
}