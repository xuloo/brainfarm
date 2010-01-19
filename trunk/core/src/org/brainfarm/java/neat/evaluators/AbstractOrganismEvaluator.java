package org.brainfarm.java.neat.evaluators;

import org.brainfarm.java.neat.Neat;
import org.brainfarm.java.neat.api.INeatNetwork;
import org.brainfarm.java.neat.api.INetwork;
import org.brainfarm.java.neat.api.IOrganism;
import org.brainfarm.java.neat.api.evaluators.IOrganismEvaluator;
import org.brainfarm.java.neat.api.evolution.IEvolutionFitness;

public abstract class AbstractOrganismEvaluator implements IOrganismEvaluator {

	protected Neat neat;
	
	private IEvolutionFitness fitnessImpl;
	
	protected INeatNetwork net;
	
	protected double[] in;
	
	protected double[][] out;
	
	protected double tgt[][];
	
	protected int net_depth = 0;
	
	protected boolean success = false;
	
	public AbstractOrganismEvaluator(Neat neat, IEvolutionFitness fitnessImpl) {
		this.neat = neat;
		this.fitnessImpl = fitnessImpl;
	}
	
	@Override
	public boolean evaluate(IOrganism organism) {
		
		
		
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
		// se I/O � da file allora � il metodo di acesso ai files che avr� lo
		// stesso nome e che far� la stessa cosa.

		double errorsum = 0.0;

		// System.out.print("\n evaluate.step 1 ");

		in = new double[neat.getNumberOfInputs() + 1];

		// setting bias

		in[neat.getNumberOfInputs()] = 1.0;

		out = new double[neat.getNumberOfSamples()][neat.getNumberOfOutputUnits()];

		tgt = new double[neat.getNumberOfSamples()][neat.getNumberOfOutputUnits()];

		net = (INeatNetwork)organism.getPhenotype();
		net_depth = net.maxDepth();
	
		if (evaluate()) {
			double[] fitness = fitnessImpl.computeFitness(neat.getNumberOfSamples(), net.getAllNodes().size(), out, tgt);
			//System.out.println("SETTING FITNESS FROM FITNESS CLASS");
			fit_dyn = fitness[0];
			err_dyn = fitness[1];
			win_dyn = fitness[2];
			
			organism.setFitness(fit_dyn);
			organism.setError(err_dyn);
		} else {
			//System.out.println("SETTING FITNESS FROM DEFAULT MINIMUM");
			errorsum = 999.0;
			organism.setFitness(0.001);
			organism.setError(errorsum);
		}
		
		//System.out.println(fit_dyn + " " + err_dyn + " " + win_dyn);
		//System.out.println("FITNESS AFTER EVALUATION == " + organism.getFitness());
		
		if (win_dyn == 1.0) {
			organism.setWinner(true);
			return true;
		}

		if (win_dyn == 2.0) {
			organism.setWinner(true);
			//EnvConstant.SUPER_WINNER_ = true;
			return true;
		}

		organism.setWinner(false);
		
		return false;
	}
	
	protected abstract boolean evaluate();
}
