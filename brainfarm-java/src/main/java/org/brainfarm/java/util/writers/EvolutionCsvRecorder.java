package org.brainfarm.java.util.writers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.IEvolutionListener;
import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.ISpecies;

/**
 * Records the progress of evolution to a CSV file.  It may be
 * extended to do other stuff as long as you remember to 
 * invoke the super methods (e.g. super.epochCompleted()
 * as the first line of epochCompleted());
 * 
 * Usage:
 * 
 * 	IEvolutionContext context = new EvolutionContext();
 *	EvolutionCsvRecorder listener = new EvolutionCsvRecorder("most_recent_evolution.csv");
 *	context.getEvolution().addListener(listener);
 * 
 * @author dtuohy
 *
 */
public class EvolutionCsvRecorder implements IEvolutionListener{

	BufferedWriter csv_out;

	public EvolutionCsvRecorder(String fileName){
		try{
			// Create file 
			FileWriter fstream = new FileWriter(fileName);
			csv_out = new BufferedWriter(fstream);
			csv_out.write("epoch, best_fit, worst_fit, avg_fit, #species, min size, max size, avg.size\n");
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	int epochsCompleted = 0;

	@Override
	public void onEpochComplete(IEvolution evolution) {
		epochsCompleted++;
		IOrganism best = getBestOrganism(evolution.getPopulation());
		IOrganism worst = getWorstOrganism(evolution.getPopulation());
		double avgFit = getAvgFitness(evolution.getPopulation());
		List<ISpecies> species = evolution.getPopulation().getSpecies();
		double totalSize = 0.0;
		int maxSize = 0;
		int minSize = Integer.MAX_VALUE;
		for(ISpecies spec : species){
			if(spec.getOrganisms().size()<minSize)
				minSize = spec.getOrganisms().size();
			if(spec.getOrganisms().size()>maxSize)
				maxSize = spec.getOrganisms().size();
			totalSize += spec.getOrganisms().size();
		}
		double avgSize = totalSize/species.size();


		try {
			csv_out.write(epochsCompleted + "," + best.getFitness() + ", " + worst.getFitness() + ", " + avgFit + ", " + species.size() + ", " + minSize + ", " + maxSize + ", " + avgSize + "\n");
			csv_out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEpochStart(IEvolution evolution) {
	}

	@Override
	public void onEvolutionComplete(IEvolution evolution) {
		try {
			csv_out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEvolutionStart(IEvolution evolution) {
	}

	@Override
	public void onRunComplete(IEvolution evolution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRunStart(IEvolution evolution) {
		// TODO Auto-generated method stub

	}
	
	private double getAvgFitness(IPopulation population) {
		double total = 0.0;
		for(IOrganism o : population.getOrganisms())
			total += o.getFitness();
		return total/population.getOrganisms().size();
	}

	protected IOrganism getBestOrganism(IPopulation population) {
		IOrganism max = population.getOrganisms().get(0);
		for(IOrganism o : population.getOrganisms())
			if(o.getFitness() > max.getFitness())
				max = o;
		return max;
	}

	protected IOrganism getWorstOrganism(IPopulation population) {
		IOrganism min = population.getOrganisms().get(0);
		for(IOrganism o : population.getOrganisms())
			if(o.getFitness() < min.getFitness())
				min = o;
		return min;
	}
}
