package org.brainfarm.java.neat.context;

import org.brainfarm.java.neat.api.enums.DataSource;
import org.brainfarm.java.neat.api.enums.StartFrom;

public abstract class AbstractExperiment implements IExperiment {
	
	public AbstractExperiment() {
		
	}
	
	private DataSource dataSource;
	
	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#getDataSource()
	 */
	public DataSource getDataSource() {
		return dataSource;
	}
	
	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#setDataSource(org.brainfarm.java.neat.api.types.DataSource)
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private String dataInput;
	
	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#setDataInput(java.lang.String)
	 */
	public void setDataInput(String dataInput) {
		this.dataInput = dataInput;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#getDataInput()
	 */
	public String getDataInput() {
		return dataInput;
	}
	
	private String dataOutput;

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#setDataOutput(java.lang.String)
	 */
	public void setDataOutput(String dataOutput) {
		this.dataOutput = dataOutput;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#getDataOutput()
	 */
	public String getDataOutput() {
		return dataOutput;
	}
	
	private String fitnessClass;

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#setFitnessClass(java.lang.String)
	 */
	public void setFitnessClass(String fitnessClass) {
		this.fitnessClass = fitnessClass;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#getFitnessClass()
	 */
	public String getFitnessClass() {
		return fitnessClass;
	}
	
	private StartFrom startFrom;

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#setStartFrom(org.brainfarm.java.neat.api.types.StartFrom)
	 */
	public void setStartFrom(StartFrom startFrom) {
				this.startFrom = startFrom;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#getStartFrom()
	 */
	public StartFrom getStartFrom() {
		return startFrom;
	}
	
	private String genomeFile;

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#setGenomeFile(java.lang.String)
	 */
	public void setGenomeFile(String genomeFile) {
		this.genomeFile = genomeFile;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#getGenomeFile()
	 */
	public String getGenomeFile() {
		return genomeFile;
	}
	
	private int epoch;

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#setEpoch(int)
	 */
	public void setEpoch(int epoch) {
		this.epoch = epoch;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#getEpoch()
	 */
	public int getEpoch() {
		return epoch;
	}
	
	private int activation;

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#setActivation(int)
	 */
	public void setActivation(int activation) {
		this.activation = activation;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#getActivation()
	 */
	public int getActivation() {
		return activation;
	}
	
	/* (non-Javadoc)
	 * @see org.brainfarm.java.neat.context.IExperiment#toString()
	 */
	public String toString() {
		StringBuilder s = new StringBuilder("Experiment Details:\n");
		
		s.append("Data Source: " + getDataSource().toString());
		
		return s.toString();
	}
}