package org.brainfarm.flex.mvcs.view.evolutionresult
{
	import mx.binding.utils.BindingUtils;
	
	import org.brainfarm.flex.mvcs.controller.BrainFarmContext;
	import org.brainfarm.flex.mvcs.model.vo.ExperimentEntry;
	
	import spark.components.supportClasses.SkinnableComponent;
	
	public class EvolutionResultPanel extends SkinnableComponent
	{
		[Bindable]
		public var evolutionResult:XMLList;
		
		public function set context(value:BrainFarmContext):void 
		{
			if (value)
			{
				BindingUtils.bindSetter(invalidateSelectedExperiment, value, ["model", "selectedExperiment"]);
			}
		}
		
		public function EvolutionResultPanel()
		{
			super();
		}
		
		public function invalidateSelectedExperiment(experiment:ExperimentEntry):void 
		{
			trace("new experiment selected");
			
			BindingUtils.bindProperty(this, "evolutionResult", experiment, "result");
		}
	}
}