package org.brainfarm.java.feat.ann;

import org.brainfarm.java.feat.api.IOrganism;
import org.brainfarm.java.feat.api.IOrganismEvaluator;
import org.brainfarm.java.feat.api.ann.INeatNetwork;
import org.brainfarm.java.feat.api.ann.INeatNode;
import org.brainfarm.java.feat.api.evolution.IEvolutionFitness;
import org.brainfarm.java.feat.api.evolution.IEvolutionInput;
import org.brainfarm.java.feat.api.evolution.IEvolutionOutput;

public class NeatOrganismEvaluator implements IOrganismEvaluator {	
	
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
	
	protected boolean success = false;
	
	public NeatOrganismEvaluator() {
		
	}
	
	@Override
	public boolean evaluate(IOrganism org) {
		
		NeatOrganism organism = (NeatOrganism)org;
		
		double fit_dyn = 0.0;
		double err_dyn = 0.0;
		double win_dyn = 0.0;

		// per evitare errori il numero di ingressi e uscite viene calcolato in
		// base
		// ai dati ;
		// per le unit di input a tale numero viene aggiunto una unit bias
		// di tipo neuron
		// le classi di copdifica input e output quindi dovranno fornire due
		// metodi : uno per restituire l'input j-esimo e uno per restituire
		// il numero di ingressi/uscite
		// se I/O è da file allora è il metodo di acesso ai files che avrà lo
		// stesso nome e che farà la stessa cosa.

		double errorsum = 0.0;

		// System.out.print("\n evaluate.step 1 ");

		in = new double[numberOfInputs + 1];

		// setting bias

		in[numberOfInputs] = 1.0;

		out = new double[numberOfSamples][numberOfOutputs];

		tgt = new double[numberOfSamples][numberOfOutputs];

		net = (INeatNetwork)organism.getPhenotype();
	
		if (evaluate()) {
			double[] fit = fitness.computeFitness(numberOfSamples, net.getAllNodes().size(), out, tgt);
			//System.out.println("SETTING FITNESS FROM FITNESS CLASS");
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

		for (int count = 0; count < numberOfSamples; count++) {
			inputs[0] = count;
			// first activation from sensor to first next level of
			// neurons
			for (int j = 0; j < numberOfInputs; j++) {
				inputs[1] = j;
				in[j] = input.getInput(inputs);
			}

			// load sensor
			net.loadSensors(in);

			/*if (EnvConstant.ACTIVATION_PERIOD == EnvConstant.MANUAL) {
				for (int relax = 0; relax < EnvConstant.ACTIVATION_TIMES; relax++) {
					success = net.activate();
				}
			} else {*/
				// first activation from sensor to next layer....
				success = net.activate();

				int maxnet_depth = net.maxDepth();
				// next activation while last level is reached !
				// use depth to ensure relaxation
				for (int relax = 0; relax <= maxnet_depth; relax++) {
					success = net.activate();
				}
			//}

			// for each sample save each output
			for (int j = 0; j < numberOfOutputs; j++) {
				out[count][j] = ((INeatNode)net.getOutputs().get(j)).getActivation();
			}
			
			// clear net
			net.flush();
		}
		
		if (success) {
			// prima di passare a calcolare il fitness legge il tgt da
			// ripassare
			// al chiamante;
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

	public void setInput(IEvolutionInput input) {
		this.input = input;
	}

	public void setOutput(IEvolutionOutput output) {
		this.output = output;
	}

	public void setFitness(IEvolutionFitness fitness) {
		this.fitness = fitness;
	}

	public void setNumberOfInputs(int numberOfInputs) {
		this.numberOfInputs = numberOfInputs;
	}

	public void setNumberOfOutputs(int numberOfOutputs) {
		this.numberOfOutputs = numberOfOutputs;
	}

	public void setNumberOfSamples(int numberOfSamples) {
		this.numberOfSamples = numberOfSamples;
	}
}
