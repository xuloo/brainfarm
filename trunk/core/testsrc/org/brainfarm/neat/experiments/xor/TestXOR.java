package org.brainfarm.neat.experiments.xor;

import static org.junit.Assert.*;

import java.util.List;
import org.brainfarm.java.neat.api.context.INeatContext;
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
 * Tests for XOR experiments
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
		RandomUtils.seedRandom(1092839);
		
		INeatContext context = new SpringNeatContext();
		
		//load default parameters
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"neat-context.xml"});
		((SpringNeatContext)context).setApplicationContext(appContext);

		//load experiment
		SpringNeatController controller = new TestXorController(context);
		controller.loadExperiment("test/xor-experiment.jar");
		
		//run experiment
		controller.startEvolution();
		
		//validate results
		List<Double> maxFitnesses = context.getEvolution().getMaxFitnessEachEpoch();
		assertEquals(6.178315187424375, maxFitnesses.get(0),.000001);
		assertEquals(14.834554047831746, maxFitnesses.get(28),.000001);
		assertEquals(9.0, maxFitnesses.get(20),.000001);
		assertEquals(14.886822986159396, maxFitnesses.get(38),.000001);
	}

	public class TestXorController extends SpringNeatController{
		public TestXorController(INeatContext context) {
			this.context = context;
		}
	}
}