package org.brainfarm.flex.mvcs.view.experiments
{		
	import com.joeberkovitz.moccasin.service.IOperation;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.binding.utils.BindingUtils;
	import mx.collections.ArrayCollection;
	import mx.controls.Image;
	import mx.events.FlexEvent;
	
	import org.brainfarm.flex.mvcs.controller.BrainFarmContext;
	import org.brainfarm.flex.mvcs.controller.IBrainFarmController;
	import org.brainfarm.flex.mvcs.model.vo.ExperimentEntry;
	
	import spark.components.List;
	import spark.components.Panel;
	
	public class ExperimentSelectionPanel extends Panel
	{	
		[SkinPart]
		public var loadExperimentButton:Image;
		
		[SkinPart]
		public var runExperimentButton:Image;
		
		[SkinPart]
		public var experimentsList:List;
		
		[Bindable]
		public var availableExperiments:ArrayCollection;
		
		private var $selectedExperiment:ExperimentEntry;
		
		private var $context:BrainFarmContext;
		
		public function set context(value:BrainFarmContext):void
		{
			trace("setting context on ExperimentPanel");
			
			if (value)
			{
				BindingUtils.bindProperty(this, "availableExperiments", value, ["model", "availableExperiments"]);
				BindingUtils.bindSetter(invalidateConnectionState, value, ["model", "connected"]);
			}
			
			$context = value;
		}
		
		private var $controller:IBrainFarmController;
		
		public function set controller(value:IBrainFarmController):void 
		{
			$controller = value;
		}
		
		public function ExperimentSelectionPanel()
		{
			super();
			
			addEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
		}
		
		private function onCreationComplete(evt:FlexEvent):void 
		{
			removeEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
			
			addHandlers();
			setBindings();
		}
		
		private function addHandlers():void 
		{
			loadExperimentButton.addEventListener(MouseEvent.CLICK, onLoadButtonClick);
			runExperimentButton.addEventListener(MouseEvent.CLICK, onRunButtonClick);
		}
		
		private function setBindings():void 
		{
			BindingUtils.bindSetter(invalidateExperimentSelection, experimentsList, "selectedItem");
		}
		
		private function invalidateConnectionState(state:Boolean):void 
		{
			trace("Experiment Panel handling connection state change");
			
			if (state)
			{
				loadAvailableExperiments();
			}
		}
		
		private function loadAvailableExperiments():void 
		{
			trace("loading experiments");
			var op:IOperation = $context.service.getAvailableExperiments();
			op.addEventListener(Event.COMPLETE, onExperimentsLoaded);
			op.execute();
		}
		
		private function invalidateExperimentSelection(value:*):void 
		{
			trace("selection " + value);
			$selectedExperiment = value;
		}
		
		private function onExperimentsLoaded(evt:Event):void 
		{			
			var list:Array = IOperation(evt.target).result as Array;
			
			trace("experiments loaded " + list);
			
			$context.model.availableExperiments = new ArrayCollection(list);
			
			trace($context.model.availableExperiments.length + " " + availableExperiments);
		}
		
		private function onLoadButtonClick(evt:MouseEvent):void 
		{
			if ($selectedExperiment)
			{
				trace("Loading experiment: " + $selectedExperiment);
				loadExperiment($selectedExperiment.fileName);
			}
		}
			
		public function loadExperiment(experiment:String):void 
		{
			var op:IOperation = $context.service.loadExperiment(experiment);
			op.addEventListener(Event.COMPLETE, onExperimentLoaded);
			op.execute();
		}
		
		private function onExperimentLoaded(obj:Object):void 
		{
			trace("Experiment Loaded");
		}
		
		private function onRunButtonClick(evt:Event):void 
		{
			$context.controller.runExperiment($selectedExperiment);
		}
		
		private function onRunExperimentComplete(evt:Event):void
		{
			
		}
	}
}