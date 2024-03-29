package org.brainfarm.feat.experiments.xor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.brainfarm.java.feat.EvolutionContext;
import org.brainfarm.java.feat.EvolutionController;
import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.IEvolutionContext;
import org.brainfarm.java.feat.api.IEvolutionController;
import org.brainfarm.java.feat.api.IEvolutionListener;
import org.brainfarm.java.util.RandomUtils;
import org.junit.Test;

/**
 * 
 * Tests for XOR experiment.
 * 
 * @author dtuohy
 *
 */
public class TestXOR {
	
	/**
	 * Runs the experiment defined by test/xor-experiment.jar.  Because
	 * we seed the NEAT Random number generator ahead of time, we can 
	 * assume the results of evolution to be deterministically 
	 * reproducible.
	 */
	@Test
	public void validateDeterministicXorExperiment(){
		RandomUtils.seedRandom(28930);

		IEvolutionContext context = new EvolutionContext();

		//load experiment
		IEvolutionController controller = new TestXorController(context);
		controller.loadExperiment("src/test/resources/brainfarm-experiment-xor-1.0-SNAPSHOT.jar");

		//run experiment
		TestEvolutionListener listener = new TestEvolutionListener();
		context.getEvolution().addListener(listener);
		controller.startEvolution();

		//verify that appropriate events were received
		assertEquals(1,listener.evolutionStarted);
		assertEquals(1,listener.evolutionCompleted);
		assertEquals(40,listener.epochsStarted);
		assertEquals(40,listener.epochsCompleted);
		
		//sample and validate results of evolution
		List<Double> maxFitnesses = context.getEvolution().getMaxFitnessEachEpoch();
		
		for(double d : maxFitnesses)
			System.out.print(d + ", ");
		
		assertEquals(6.178315187424375, maxFitnesses.get(0),.000001);
		assertEquals(9.0, maxFitnesses.get(28),.000001);
		assertEquals(9.0, maxFitnesses.get(35),.000001);
	}

	public class TestXorController extends EvolutionController {
		public TestXorController(IEvolutionContext context) {
			super(context);
		}
	}

	public class TestEvolutionListener implements IEvolutionListener {

		int epochsStarted = 0;
		int epochsCompleted = 0;
		int evolutionStarted = 0;
		int evolutionCompleted = 0;
		
		@Override
		public void onEpochComplete(IEvolution evolution) {
			epochsCompleted++;
		}

		@Override
		public void onEpochStart(IEvolution evolution) {
			epochsStarted++;
		}

		@Override
		public void onEvolutionComplete(IEvolution evolution) {
			evolutionCompleted++;
		}

		@Override
		public void onEvolutionStart(IEvolution evolution) {
			evolutionStarted++;
		}

		@Override
		public void onRunComplete(IEvolution evolution) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRunStart(IEvolution evolution) {
			// TODO Auto-generated method stub
			
		}
	}

}