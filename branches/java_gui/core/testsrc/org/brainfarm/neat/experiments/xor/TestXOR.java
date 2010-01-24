package org.brainfarm.neat.experiments.xor;

import static org.junit.Assert.*;

import java.util.List;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evolution.IEvolution;
import org.brainfarm.java.neat.api.evolution.IEvolutionListener;
import org.brainfarm.java.neat.context.SpringNeatContext;
import org.brainfarm.java.neat.controller.SpringNeatController;
import org.brainfarm.java.util.FileUtils;
import org.brainfarm.java.util.RandomUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * Tests for XOR experiment.
 * 
 * @author dtuohy
 *
 */
public class TestXOR {


	@BeforeClass
	public static void setup(){
		//we must delete the experiment directory in order to re-run the experiment
		FileUtils.deleteDirectory("experiment");
	}

	/**
	 * Runs the experiment defined by test/xor-experiment.jar.  Because
	 * we seed the NEAT Random number generator ahead of time, we can 
	 * assume the results of evolution to be deterministically 
	 * reproducible.
	 */
	@Test
	public void validateDeterministicXorExperiment(){
		RandomUtils.seedRandom(28930);

		INeatContext context = new SpringNeatContext();

		//load default parameters
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"neat-context.xml"});
		((SpringNeatContext)context).setApplicationContext(appContext);

		//load experiment
		SpringNeatController controller = new TestXorController(context);
		controller.loadExperiment("test/xor-experiment.jar");

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
		assertEquals(15.999766675576327, maxFitnesses.get(28),.000001);
		assertEquals(10.043538023300911, maxFitnesses.get(35),.000001);
	}

	public class TestXorController extends SpringNeatController{
		public TestXorController(INeatContext context) {
			this.context = context;
		}
	}

	public class TestEvolutionListener implements IEvolutionListener{

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
	}

}