package org.brainfarm.flex.mvcs.service.remote
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.net.NetConnection;
	
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;
	
	public class RemoteBrainFarmService implements IBrainFarmService
	{
		public function RemoteBrainFarmService()
		{
		}
		
		public function connect(uri:String):IOperation
		{
			return new RemoteConnectionOperation(uri);
		}
		
		public function loadNeatParameters(connection:NetConnection):IOperation
		{
			return new RemoteLoadNeatParametersOperation(connection);
		}
		
		public function saveNeatParameters(connection:NetConnection):IOperation
		{
			return null;
		}
	}
}