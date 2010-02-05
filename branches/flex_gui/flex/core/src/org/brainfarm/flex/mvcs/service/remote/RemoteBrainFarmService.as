package org.brainfarm.flex.mvcs.service.remote
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import org.brainfarm.flex.api.connection.IClient;
	import org.brainfarm.flex.comm.client.BasicClient;
	import org.brainfarm.flex.comm.messages.BaseMessage;
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;
	
	public class RemoteBrainFarmService implements IBrainFarmService
	{
		private var $client:IClient;
		
		public function RemoteBrainFarmService()
		{
			$client = new BasicClient();
		}
		
		public function connect(uri:String):IOperation
		{
			BaseMessage.DEFAULT_SERVICE = "brainfarm";
			
			return $client.connect(uri);
		}
		
		public function loadNeatParameters():IOperation
		{
			return $client.sendToServer(new LoadNeatParametersMessage());
		}
		
		public function saveNeatParameters():IOperation
		{
			return null;
		}
	}
}