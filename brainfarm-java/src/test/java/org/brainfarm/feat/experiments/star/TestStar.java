package org.brainfarm.feat.experiments.star;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.brainfarm.java.feat.EvolutionContext;
import org.brainfarm.java.feat.EvolutionController;
import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.IEvolutionContext;
import org.brainfarm.java.feat.api.IEvolutionController;
import org.brainfarm.java.feat.api.IEvolutionListener;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.util.RandomUtils;
import org.junit.Test;

/**
 * Tests for Star experiment.  This is a trivial experiment 
 * that evolves a graph structure that looks like a 7-point
 * star if the nodes in the genome are laid out on a circle.  
 * It serves to exercise the default FEAT functionality, as
 * it only provides implementations of a custom IOrganismEvaluator
 * and a custom INode.
 * 
 * @author dtuohy
 *
 */
public class TestStar {

	public TestStar() {
		BasicConfigurator.configure();
	}
	
	/**
	 * Runs the experiment defined by test/xor-experiment.jar.  Because
	 * we seed the NEAT Random number generator ahead of time, we can 
	 * assume the results of evolution to be deterministically 
	 * reproducible.
	 */
	@Test
	public void validateDeterministicMatchExperiment(){
		
		
		
		RandomUtils.seedRandom(290761);

		IEvolutionContext context = new EvolutionContext();

		//load default parameters
		//ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"neat-context.xml"});
		//Neat neat = (Neat)appContext.getBean("neat");
		//context.setNeat(neat);

		//load experiment
		IEvolutionController controller = new TestXorController(context);
		controller.loadDefaultParameters();
		controller.loadExperiment("src/test/resources/brainfarm-experiment-star-1.0-SNAPSHOT.jar");

		//run experiment
		TestEvolutionListener listener = new TestEvolutionListener();
		context.getEvolution().addListener(listener);
		controller.startEvolution();

		//verify that appropriate events were received
		assertEquals(1,listener.evolutionStarted);
		assertEquals(1,listener.evolutionCompleted);
		assertEquals(25,listener.epochsStarted);
		assertEquals(25,listener.epochsCompleted);

		//sample and validate results of evolution
		List<Double> maxFitnesses = context.getEvolution().getMaxFitnessEachEpoch();
		for(double d : maxFitnesses)
			System.out.print(d + ", ");
		assertEquals(1.0, maxFitnesses.get(0),.000001);
		assertEquals(12.0, maxFitnesses.get(7),.000001);
		assertEquals(14.0, maxFitnesses.get(17),.000001);
		assertEquals(14.0, maxFitnesses.get(21),.000001);
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

	private IOrganism getBestOrganism(IPopulation population) {
		IOrganism max = population.getOrganisms().get(0);
		for(IOrganism o : population.getOrganisms())
			if(o.getFitness() > max.getFitness())
				max = o;
		return max;
	}

}