package org.brainfarm.java.neat.context;

import org.brainfarm.java.neat.api.IPopulation;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.neat.api.enums.DataSource;
import org.brainfarm.java.neat.api.enums.StartFrom;

public interface IExperiment {

	public abstract DataSource getDataSource();

	public abstract void setDataSource(DataSource dataSource);

	public abstract void setDataInput(String dataInput);

	public abstract String getDataInput();

	public abstract void setDataOutput(String dataOutput);

	public abstract String getDataOutput();

	public abstract void setFitnessClass(String fitnessClass);

	public abstract String getFitnessClass();

	public abstract void setStartFrom(StartFrom startFrom);

	public abstract StartFrom getStartFrom();

	public abstract void setGenomeFile(String genomeFile);

	public abstract String getGenomeFile();

	public abstract void setEpoch(int epoch);

	public abstract int getEpoch();

	public abstract void setActivation(int activation);

	public abstract int getActivation();
	
	public abstract IPopulation getPopulation(INeatContext context);
	
	public abstract IOrganismEvaluator getEvaluator(INeatContext context);

	public abstract String toString();

}