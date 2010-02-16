package org.brainfarm.java.mvcs.service.remote;

import org.brainfarm.java.feat.Evolution;
import org.brainfarm.java.feat.api.evolution.IEvolution;
import org.brainfarm.java.feat.api.evolution.IEvolutionListener;
import org.brainfarm.java.red5.message.BaseMessage;
import org.brainfarm.java.util.writers.CommandAwareEvolutionListener;
import org.brainfarm.java.util.writers.FileWritingResultGenerator;
import org.brainfarm.java.util.writers.PopulationFilenameGenerator;
import org.brainfarm.java.util.writers.SpringXMLSourceGenerator;
import org.brainfarm.java.util.writers.TransformerCommand;

/**
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class RunExperimentMessage extends BaseMessage implements IEvolutionListener {

	@Override
	public Object read() {
		
		IEvolution evolution = service.getFeatContext().getEvolution();
		
		configureEvolutionListeners(evolution);
		
		evolution.addListener(this);
		
		evolution.run();
		
		return null;
	}

	private void configureEvolutionListeners(IEvolution evolution) {
		
		PopulationFilenameGenerator fileNameGenerator = new PopulationFilenameGenerator();
		fileNameGenerator.setBasePath("webapps/brainfarm-webapp/working");
		
		FileWritingResultGenerator resultGenerator = new FileWritingResultGenerator();
		resultGenerator.setFilenameGenerator(fileNameGenerator);
		
		TransformerCommand command = new TransformerCommand(evolution);
		command.setResult(resultGenerator);
		
		SpringXMLSourceGenerator sourceGenerator = new SpringXMLSourceGenerator();
		command.setSource(sourceGenerator);
		
		CommandAwareEvolutionListener listener = new CommandAwareEvolutionListener();
		listener.setEpochCompleteCommand(command);
		
		evolution.addListener(listener);
	}
	
	@Override
	public void onEpochComplete(IEvolution evolution) {
		// TODO Auto-generated method stub
		System.out.println("EpochComplete");
	}

	@Override
	public void onEpochStart(IEvolution evolution) {
		// TODO Auto-generated method stub
		System.out.println("EpochStart");
	}

	@Override
	public void onEvolutionComplete(IEvolution evolution) {
		// TODO Auto-generated method stub
		System.out.println("EvolutionComplete");
	}

	@Override
	public void onEvolutionStart(IEvolution evolution) {
		// TODO Auto-generated method stub
		System.out.println("EvolutionStart");
	}
}
