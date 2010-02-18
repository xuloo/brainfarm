package org.brainfarm.flex.mvcs.service.remote
{
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import org.brainfarm.flex.api.connection.IClient;
	import org.brainfarm.flex.comm.client.BasicClient;
	import org.brainfarm.flex.comm.messages.BaseMessage;
	import org.brainfarm.flex.mvcs.model.vo.ExperimentEntry;
	import org.brainfarm.flex.mvcs.model.vo.NeatDoubleParameter;
	import org.brainfarm.flex.mvcs.model.vo.NeatIntParameter;
	import org.brainfarm.flex.mvcs.service.IBrainFarmService;
	
	public class RemoteBrainFarmService implements IBrainFarmService
	{
		private var $client:IClient;
		
		public function RemoteBrainFarmService(client:IClient)
		{
			$client = client;
			$client.handshake.addClassToRegister(NeatDoubleParameter);
			$client.handshake.addClassToRegister(NeatIntParameter);
			$client.handshake.addClassToRegister(BaseMessage);
			$client.handshake.addClassToRegister(ExperimentEntry);
			$client.handshake.addClassToRegister(LoadNeatParametersMessage);
			$client.handshake.addClassToRegister(GetAvailableExperimentsMessage);
			$client.handshake.addClassToRegister(LoadExperimentMessage);
			$client.handshake.addClassToRegister(RunExperimentMessage);
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
		
		public function getAvailableExperiments():IOperation
		{
			return $client.sendToServer(new GetAvailableExperimentsMessage());
		}
		
		public function loadExperiment(experiment:String):IOperation
		{
			return $client.sendToServer(new LoadExperimentMessage(experiment));
		}
		
		public function runExperiment():IOperation
		{
			return $client.sendToServer(new RunExperimentMessage());
		}
	}
}