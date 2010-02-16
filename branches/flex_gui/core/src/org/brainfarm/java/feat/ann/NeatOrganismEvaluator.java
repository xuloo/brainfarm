package org.brainfarm.java.feat.ann;

import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.ann.INeatNetwork;
import org.brainfarm.java.feat.api.ann.INeatNode;
import org.brainfarm.java.feat.api.evolution.IEvolutionFitness;
import org.brainfarm.java.feat.api.evolution.IEvolutionInput;
import org.brainfarm.java.feat.api.evolution.IEvolutionOutput;
import org.brainfarm.java.feat.evaluators.AbstractOrganismEvaluator;

/**
 * Provides an IOrganismEvaluator implementation for use with NEAT experiments.
 * Allows the experiment to use custom classes for providing Input data to the evaluator
 * and for testing the fitness of an organism.
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class NeatOrganismEvaluator extends AbstractOrganismEvaluator {	
	
	private int numberOfInputs;
	
	private int numberOfOutputs;
	
	private int numberOfSamples;
	
	private IEvolutionInput input;
	
	private IEvolutionOutput output;
	
	private IEvolutionFitness fitness;
	
	protected INeatNetwork net;
	
	protected double[] in;
	
	protected double[][] out;
	
	protected double tgt[][];
	
	/**
	 * Constructor. Creates a new NeatOrganismEvaluator.
	 */
	public NeatOrganismEvaluator() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean evaluate(IOrganism org) {
		
		NeatOrganism organism = (NeatOrganism)org;
		
		double fit_dyn = 0.0;
		double err_dyn = 0.0;
		double win_dyn = 0.0;

		double errorsum = 0.0;

		in = new double[numberOfInputs + 1];

		// setting bias
		in[numberOfInputs] = 1.0;

		out = new double[numberOfSamples][numberOfOutputs];

		tgt = new double[numberOfSamples][numberOfOutputs];

		net = (INeatNetwork)organism.getPhenotype();
	
		if (evaluate()) {
			double[] fit = fitness.computeFitness(numberOfSamples, net.getAllNodes().size(), out, tgt);

			fit_dyn = fit[0];
			err_dyn = fit[1];
			win_dyn = fit[2];
			
			organism.setFitness(fit_dyn);
			organism.setError(err_dyn);
		} else {
			errorsum = 999.0;
			organism.setFitness(0.001);
			organism.setError(errorsum);
		}
				
		boolean winner = false;
		if (win_dyn == 1.0 || win_dyn == 2.0)
			winner = true;

		
		return winner;
	}
	
	protected boolean evaluate() {
		
		int inputs[] = new int[2];
		
		boolean success = false;

		for (int count = 0; count < numberOfSamples; count++) {
			
			inputs[0] = count;
			
			// first activation from sensor to first next level of neurons.
			for (int j = 0; j < numberOfInputs; j++) {
				
				inputs[1] = j;
				
				in[j] = input.getInput(inputs);
			}

			// load sensor
			net.loadSensors(in);

			// first activation from sensor to next layer....
			success = net.activate();

			int maxnet_depth = net.maxDepth();
			
			// Next activation while last level is reached !
			// Use depth to ensure relaxation.
			for (int relax = 0; relax <= maxnet_depth; relax++) {
				success = net.activate();
			}

			// for each sample save each output
			for (int j = 0; j < numberOfOutputs; j++) {
				out[count][j] = ((INeatNode)net.getOutputs().get(j)).getActivation();
			}
			
			// clear net
			net.flush();
		}
		
		if (success) {
			int target[] = new int[2];
			
			for (int count = 0; count < numberOfSamples; count++) {	
				
				target[0] = count;
				
				for (int j = 0; j < numberOfOutputs; j++) {
					
					target[1] = j;
					
					tgt[count][j] = output.getTarget(target);
				}
			}
		}
		
		return success;
	}
	
	////////////////////////////////////////////////////////////////////
	// SETTERS - INVOKED WHEN THIS EVALUATOR IS LOADED VIA SPRING IOC //
	////////////////////////////////////////////////////////////////////

	/**
	 * Set the number of input nodes the organism will have.
	 */
	public void setNumberOfInputs(int numberOfInputs) {
		this.numberOfInputs = numberOfInputs;
	}

	/**
	 * Set the number of output nodes the organism will have.
	 * 
	 * @param numberOfOutputs
	 */
	public void setNumberOfOutputs(int numberOfOutputs) {
		this.numberOfOutputs = numberOfOutputs;
	}

	/**
	 * Set the number of samples in each test.
	 * 
	 * @param numberOfSamples
	 */
	public void setNumberOfSamples(int numberOfSamples) {
		this.numberOfSamples = numberOfSamples;
	}

	/**
	 * Set the IEvolutionInput implementation used to provide input data.
	 * 
	 * @param input
	 */
	public void setInput(IEvolutionInput input) {
		this.input = input;
	}

	/**
	 * Set the IEvolutionOutput implementation used to provide target outputs.
	 * 
	 * @param output
	 */
	public void setOutput(IEvolutionOutput output) {
		this.output = output;
	}

	/**
	 * Set the IEvolutionFitness implementation used to test organism's fitness.
	 * 
	 * @param fitness
	 */
	public void setFitness(IEvolutionFitness fitness) {
		this.fitness = fitness;
	}
}
