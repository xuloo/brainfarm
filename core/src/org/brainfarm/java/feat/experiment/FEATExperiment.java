package org.brainfarm.java.feat.experiment;

import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.IEvolutionStrategy;
import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.enums.DataSource;
import org.brainfarm.java.feat.api.enums.StartFrom;
import org.brainfarm.java.feat.api.experiment.IExperiment;

/**
 * 
 * @author Trevor
 *
 */
public class FEATExperiment implements IExperiment {
	
	public static StartFrom DEFAULT_START_FROM = StartFrom.GENOME;
	
	public static DataSource DEFAULT_DATA_SOURCE = DataSource.CLASS;

	protected StartFrom startFrom = DEFAULT_START_FROM;
	
	private DataSource dataSource = DEFAULT_DATA_SOURCE;
	
	protected String genomeFile;
	
	protected int epoch;
	
	protected IPopulation population;
	
	protected IEvolutionStrategy evolutionStrategy;
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public IEvolution evolution() {
		return null;
	}
	
	public void setStartFrom(StartFrom startFrom) {
		this.startFrom = startFrom;
	}

	public StartFrom getStartFrom() {
		return startFrom;
	}

	public void setGenomeFile(String genomeFile) {
		this.genomeFile = genomeFile;
	}

	public String getGenomeFile() {
		return genomeFile;
	}

	public void setEpoch(int epoch) {
		this.epoch = epoch;
	}

	public int getEpoch() {
		return epoch;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public IPopulation getPopulation() {
		return population;
	}
	
	public void setPopulation(IPopulation population) {
		this.population = population;
	}
	
	public IEvolutionStrategy getEvolutionStrategy() {
		return evolutionStrategy;
	}
	
	public void setEvolutionStrategy(IEvolutionStrategy evolutionStrategy) {
		this.evolutionStrategy = evolutionStrategy;
	}
}
