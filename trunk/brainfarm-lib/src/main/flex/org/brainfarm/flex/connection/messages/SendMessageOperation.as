package org.brainfarm.flex.connection.messages
{
	import com.joeberkovitz.moccasin.service.AbstractOperation;
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import org.brainfarm.flex.api.connection.INetConnection;

	public class SendMessageOperation extends AbstractOperation
	{				
		private var _connection:INetConnection;
		
		public function SendMessageOperation(message:IOperation, connection:INetConnection)
		{
			super();
			
			_connection = connection;
		}
		
		override public function execute():void 
		{			
		}
		
		override public function get result():*
		{
			return null;
		}
		
	}
}