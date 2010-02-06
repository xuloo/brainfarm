package org.brainfarm.flex.mvcs.model
{
	import mx.collections.ArrayList;

	public class BrainFarmModel
	{
		[Bindable]
		public var connected:Boolean = false;
		
		[Bindable]
		public var neatParams:ArrayList;
	}
}