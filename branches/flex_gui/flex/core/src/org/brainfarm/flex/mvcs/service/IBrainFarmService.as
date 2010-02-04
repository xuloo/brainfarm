package org.brainfarm.flex.mvcs.service
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.net.NetConnection;
	
	public interface IBrainFarmService
	{
		function connect(uri:String):IOperation;
		
		function loadNeatParameters(connection:NetConnection):IOperation;
		
		function saveNeatParameters(connection:NetConnection):IOperation;
	}
}