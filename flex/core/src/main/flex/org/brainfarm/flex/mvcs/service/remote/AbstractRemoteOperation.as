package org.brainfarm.flex.mvcs.service.remote
{
	import com.joeberkovitz.moccasin.service.AbstractOperation;
	
	import flash.net.NetConnection;
	
	public class AbstractRemoteOperation extends AbstractOperation
	{
		protected var _connection:NetConnection;
		
		public function AbstractRemoteOperation(connection:NetConnection)
		{
			super();
			
			_connection = connection;
		}
	}
}