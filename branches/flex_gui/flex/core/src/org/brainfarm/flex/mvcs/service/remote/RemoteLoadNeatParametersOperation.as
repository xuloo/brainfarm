package org.brainfarm.flex.mvcs.service.remote
{
	import com.joeberkovitz.moccasin.service.AbstractOperation;
	
	import flash.net.NetConnection;
	
	public class RemoteLoadNeatParametersOperation extends AbstractRemoteOperation
	{
		public function RemoteLoadNeatParametersOperation(connection:NetConnection)
		{
			super(connection);
		}
		
		public override function execute() : void
		{
			handleComplete(null);
		}
	}
}