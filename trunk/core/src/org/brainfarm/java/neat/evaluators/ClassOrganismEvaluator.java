package org.brainfarm.java.neat.evaluators;

import org.brainfarm.java.neat.Neat;
import org.brainfarm.java.neat.api.evolution.IEvolutionFitness;
import org.brainfarm.java.neat.api.evolution.IEvolutionInput;
import org.brainfarm.java.neat.api.evolution.IEvolutionOutput;

public class ClassOrganismEvaluator extends AbstractOrganismEvaluator {	
	
	private IEvolutionInput inputImpl;
	
	private IEvolutionOutput outputImpl;
	
	public ClassOrganismEvaluator(Neat neat, IEvolutionFitness fitnessImpl, IEvolutionInput inputImpl, IEvolutionOutput outputImpl) {
		super(neat, fitnessImpl);
		
		System.out.println("output impl " + outputImpl);
		
		this.inputImpl = inputImpl;
		this.outputImpl = outputImpl;
	}
	
	@Override
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

				// next activation while last level is reached !
				// use depth to ensure relaxation
				for (int relax = 0; relax <= net_depth; relax++) {
					success = net.activate();
				}
			//}

			// for each sample save each output
			for (int j = 0; j < neat.getNumberOfOutputUnits(); j++) {
				out[count][j] = net.getOutputs().get(j).getActivation();
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
