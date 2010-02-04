package org.brainfarm.flex.mvcs.service
{
	import com.joeberkovitz.moccasin.service.AbstractOperation;
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.events.ErrorEvent;
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
   
    public class AsyncOperationSequence extends AbstractOperation
    {
        public var failOnFault:Boolean = true;
        public var currentOperation:IOperation;
       
        protected var operations:Array = [];
       
        protected var completeOperations:Array = [];
       
        private var _opCollection:ArrayCollection;
        
        private var _autoReset:Boolean = false;
       
        public function AsyncOperationSequence(autoReset:Boolean = false)
        {
            super();
            
            _autoReset = autoReset;
        }
       
        override public function execute():void {
            //dispatchEvent(new ProgressSourceEvent(ProgressSourceEvent.PROGRESS_START, this, ""));
            executeNextCommand();
        }       
       
        public function executeNextCommand():void
        {
            if (operations.length > 0) {
                var firstOperation:IOperation = IOperation(operations.shift());
                executeOperation(firstOperation);
            }
            else
            {
              	if (_autoReset)
            	{
            		operations = completeOperations.concat();
            		completeOperations = [];
            	}
            	
                handleComplete(null);
            }
        }
       
        public function addOperation(operation:IOperation):void {
            if (operation != null)
            {
                operations.push(operation);
                _opCollection = new ArrayCollection(operations);
            }
        }
       
        protected function executeOperation(operation:IOperation):void {
            operation.addEventListener(Event.COMPLETE, onOperationResult);
            operation.addEventListener(ErrorEvent.ERROR, onOperationFault);
			
            currentOperation = operation;
           // dispatchEvent(new OperationSequenceEvent(OperationSequenceEvent.EXECUTE_OPERATION, operation));
            operation.execute();
        }
       
        private function onOperationResult(event:Event):void {
            completeOperations.push(currentOperation);
            execute();
        }
       
        private function onOperationFault(event:ErrorEvent):void {
            if (failOnFault) {
               // dispatchEvent(new OperationSequenceEvent(OperationSequenceEvent.ERROR, currentOperation));
            }
            else {
                execute();
            }
        }
    }
}