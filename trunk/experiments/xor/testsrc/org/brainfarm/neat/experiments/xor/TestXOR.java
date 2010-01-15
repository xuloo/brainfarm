package org.brainfarm.neat.experiments.xor;

import java.io.File;

import org.brainfarm.java.neat.api.IOrganism;
import org.brainfarm.java.neat.api.ISpecies;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.context.SpringNeatContext;
import org.brainfarm.java.neat.controller.SpringNeatController;
import org.brainfarm.java.util.RandomUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestXOR {

	@BeforeClass
	public static void setup(){
		File dir = new File("experiment");
		if(dir.exists())
			deleteDir(dir);
	}

	@Test
	public void testXOR(){
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
//		for(ISpecies spec : context.getExperiment().getPopulation(context).getSpecies())
//			System.out.println(spec);
	}

	public class TestXorController extends SpringNeatController{
		public TestXorController(INeatContext context) {
			this.context = context;
		}
	}

	// Deletes all files and subdirectories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns false.
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	} 
}