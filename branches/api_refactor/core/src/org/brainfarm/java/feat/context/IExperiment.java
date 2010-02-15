package org.brainfarm.java.feat.context;

import org.brainfarm.java.feat.api.IPopulation;
import org.brainfarm.java.feat.api.context.INeatContext;
import org.brainfarm.java.feat.api.enums.DataSource;
import org.brainfarm.java.feat.api.enums.StartFrom;
import org.brainfarm.java.feat.api.evaluators.IOrganismEvaluator;

public interface IExperiment {

	public abstract String getFeatCustomizationsPackage();
	
	public abstract void setFeatCustomizationsPackage(String packageName);
	
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

	public abstract String toString();

}