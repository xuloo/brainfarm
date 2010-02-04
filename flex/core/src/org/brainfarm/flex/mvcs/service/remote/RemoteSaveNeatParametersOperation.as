package org.brainfarm.flex.mvcs.service.remote
{
	import com.joeberkovitz.moccasin.service.AbstractOperation;
	
	import flash.net.NetConnection;
	
	public class RemoteSaveNeatParametersOperation extends AbstractRemoteOperation
	{
		public function RemoteSaveNeatParametersOperation(connection:NetConnection)
		{
			super(connection);
		}
	}
}