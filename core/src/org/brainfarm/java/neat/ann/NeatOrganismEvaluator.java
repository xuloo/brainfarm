package org.brainfarm.java.neat.ann;

import org.brainfarm.java.neat.api.IOrganism;
import org.brainfarm.java.neat.api.ann.INeatNetwork;
import org.brainfarm.java.neat.api.ann.INeatNode;
import org.brainfarm.java.neat.api.context.INeatContext;
import org.brainfarm.java.neat.api.evolution.IEvolutionFitness;
import org.brainfarm.java.neat.api.evolution.IEvolutionInput;
import org.brainfarm.java.neat.api.evolution.IEvolutionOutput;
import org.brainfarm.java.neat.evaluators.AbstractOrganismEvaluator;

public class NeatOrganismEvaluator extends AbstractOrganismEvaluator {	
	
	private IEvolutionInput inputImpl;
	
	private IEvolutionOutput outputImpl;
	
	protected INeatNetwork net;
	
	protected double[] in;
	
	protected double[][] out;
	
	protected double tgt[][];
	
	protected boolean success = false;
	
	private IEvolutionFitness fitnessImpl;
	
	public NeatOrganismEvaluator(INeatContext context) {
		super(context);
		this.fitnessImpl = context.getFitnessImpl();
		this.inputImpl = context.getInputImpl();
		this.outputImpl = context.getOutputImpl();
	}
	
	public IEvolutionFitness getFitnessImpl(){
		return fitnessImpl;
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

		in = new double[neat.getNumberOfInputs() + 1];

		// setting bias

		in[neat.getNumberOfInputs()] = 1.0;

		out = new double[neat.getNumberOfSamples()][neat.getNumberOfOutputUnits()];

		tgt = new double[neat.getNumberOfSamples()][neat.getNumberOfOutputUnits()];

		net = (INeatNetwork)organism.getPhenotype();
	
		if (evaluate()) {
			double[] fitness = getFitnessImpl().computeFitness(neat.getNumberOfSamples(), net.getAllNodes().size(), out, tgt);
			//System.out.println("SETTING FITNESS FROM FITNESS CLASS");
			fit_dyn = fitness[0];
			err_dyn = fitness[1];
			win_dyn = fitness[2];
			
			organism.setFitness(fit_dyn);
			organism.setError(err_dyn);
		} else {
			errorsum = 999.0;
			organism.setFitness(0.001);
			organism.setError(errorsum);
		}
				
		if (win_dyn == 1.0) {
			organism.setWinner(true);
			return true;
		}

		if (win_dyn == 2.0) {
			organism.setWinner(true);
			return true;
		}

		organism.setWinner(false);
		
		return false;
	}
	
	protected boolean evaluate() {
		
		int input[] = new int[2];

		for (int count = 0; count < neat.getNumberOfSamples(); count++) {
			input[0] = count;
			// first activation from sensor to first next level of
			// neurons
			for (int j = 0; j < neat.getNumberOfInputs(); j++) {
				input[1] = j;
				in[j] = inputImpl.getInput(input);
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
			for (int j = 0; j < neat.getNumberOfOutputUnits(); j++) {
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
			for (int count = 0; count < neat.getNumberOfSamples(); count++) {	
				target[0] = count;
				for (int j = 0; j < neat.getNumberOfOutputUnits(); j++) {
					target[1] = j;
					tgt[count][j] = outputImpl.getTarget(target);
				}
			}
		}
		
		return success;
	}

}
