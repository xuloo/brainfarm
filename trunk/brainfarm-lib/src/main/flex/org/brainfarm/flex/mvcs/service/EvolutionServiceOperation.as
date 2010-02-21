package org.brainfarm.flex.mvcs.service
{
	import mx.rpc.AbstractOperation;
	import mx.rpc.AsyncToken;
	
	public class EvolutionServiceOperation extends AbstractOperation
	{		
		private var $operation:AbstractOperation;
		
		private var $result:*;
		
		public override function get result():*
		{
			return $result;
		}
		
		public function EvolutionServiceOperation(operation:AbstractOperation)
		{
			$operation = operation;
		}
		
		public override function execute():void 
		{
			$operation.send().addResponder(new mx.rpc.Responder(onResult, onFault));
		}
		
		private function onResult(obj:Object):void 
		{
			$result = obj;
			
			handleComplete(null);
		}
		
		private function onFault(obj:Object):void 
		{
			$result = obj;
			
			handleFault(null);
		}
	}
}