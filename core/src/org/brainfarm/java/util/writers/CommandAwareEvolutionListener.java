package org.brainfarm.java.util.writers;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.IEvolutionListener;
import org.brainfarm.java.util.ICommand;

public class CommandAwareEvolutionListener implements IEvolutionListener {
	
	private static Logger logger = Logger.getLogger(CommandAwareEvolutionListener.class);

	private List<ICommand> epochCompleteCommands = Collections.synchronizedList(new CopyOnWriteArrayList<ICommand>()); 
	
	public void setEpochCompleteCommand(ICommand command) {
		epochCompleteCommands.add(command);
	}
	
	public void setEpochCompleteCommands(List<ICommand> commands) {
		epochCompleteCommands = commands;
	}
	 
	@Override
	public void onEpochComplete(IEvolution evolution) {
		logger.debug("firing " + epochCompleteCommands.size() + " commands");
		for (ICommand command : epochCompleteCommands) {
			logger.debug("executing " + command);
			command.execute();
		}
	}

	@Override
	public void onEpochStart(IEvolution evolution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvolutionComplete(IEvolution evolution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvolutionStart(IEvolution evolution) {
		// TODO Auto-generated method stub

	}

}
