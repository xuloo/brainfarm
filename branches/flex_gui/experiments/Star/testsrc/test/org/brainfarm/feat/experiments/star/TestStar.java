package test.org.brainfarm.feat.experiments.star;

import static org.junit.Assert.assertEquals;

import java.awt.Container;
import java.util.List;

import javax.swing.JFrame;

import omit.org.gatech.feat.jung.MyHmmVisualizer;

import org.brainfarm.java.feat.api.INetwork;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.evolution.IEvolution;
import org.brainfarm.java.feat.api.evolution.IEvolutionListener;
import org.brainfarm.java.feat.context.SpringNeatContext;
import org.brainfarm.java.feat.controller.SpringNeatController;
import org.brainfarm.java.util.RandomUtils;
import org.gatech.feat.experiments.star.StarOrganismEvaluator;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * Tests for SimpleMatchNetwork experiment.
 * 
 * @author dtuohy
 *
 */
public class TestStar {
	
	public TestStar() {
	}
	
	StarOrganismEvaluator eval = new StarOrganismEvaluator();

	/**
	 * Runs the experiment defined by test/xor-experiment.jar.  Because
	 * we seed the NEAT Random number generator ahead of time, we can 
	 * assume the results of evolution to be deterministically 
	 * reproducible.
	 */
	@Test
	public void validateDeterministicMatchExperiment(){
		RandomUtils.seedRandom(290761);

		INeatContext context = new SpringNeatContext();

		//load default parameters
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"neat-context.xml"});
		((SpringNeatContext)context).setApplicationContext(appContext);

		//load experiment
		SpringNeatController controller = new TestStarController(context);
		controller.loadExperiment();

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
		assertEquals(14.0, maxFitnesses.get(7),.000001);
		assertEquals(17.0, maxFitnesses.get(17),.000001);
		assertEquals(19.0, maxFitnesses.get(21),.000001);
	}
	
	public class TestStarController extends SpringNeatController {

		public TestStarController(INeatContext context) {
			super(context);
		}
	}

	public class TestEvolutionListener implements IEvolutionListener{
		
		MyHmmVisualizer ui;

		private void initializeUI(INetwork net) {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Container content = frame.getContentPane();
			ui = new MyHmmVisualizer(net);
			content.add(ui);
			frame.pack();
			frame.setVisible(true);
		}

		int epochsStarted = 0;
		int epochsCompleted = 0;
		int evolutionStarted = 0;
		int evolutionCompleted = 0;

		@Override
		public void onEpochComplete(IEvolution evolution) {
			epochsCompleted++;
			for(IOrganism org : evolution.getPopulation().getOrganisms())
				eval.evaluate(org);
			IOrganism best = getBestOrganism(evolution.getPopulation());
			INetwork net = best.getPhenotype();
			//			System.out.println("Best has fitness " + best.getFitness());
			if(ui==null)
				initializeUI(net);
			else
				ui.setNetwork(net);
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

	private IOrganism getBestOrganism(IPopulation population) {
		IOrganism max = population.getOrganisms().get(0);
		for(IOrganism o : population.getOrganisms())
			if(o.getFitness() > max.getFitness())
				max = o;
		return max;
	}

}