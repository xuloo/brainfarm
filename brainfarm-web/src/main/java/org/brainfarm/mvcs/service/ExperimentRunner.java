package org.brainfarm.mvcs.service;

import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.IEvolutionListener;
import org.brainfarm.java.util.XMLUtils;
import org.brainfarm.java.util.writers.EvolutionResultWriter;
import org.brainfarm.java.util.writers.IEvolutionWriter;
import org.red5.server.api.IConnection;
import org.red5.server.api.service.ServiceUtils;
import org.red5.server.api.so.ISharedObject;
import org.w3c.dom.Document;

public class ExperimentRunner implements IEvolutionListener, IExperimentRunner {

	private IConnection connection;
	
	private IEvolution evolution;
	
	private float progress = 0.0f;
	
	private IEvolutionWriter resultWriter;
	
	private List<IExperimentRunnerListener> listeners = new ArrayList<IExperimentRunnerListener>();
	
	public ExperimentRunner(IConnection connection, IEvolution evolution) {
		this.connection = connection;
		this.evolution = evolution;
	}

	/* (non-Javadoc)
	 * @see org.brainfarm.mvcs.service.IExperimentRunner#run()
	 */
	@Override
	public void run() {
		evolution.addListener(this);
		evolution.run();
	}

	@Override
	public void onEpochComplete(IEvolution evolution) {
		System.out.println("Epoch Complete");
		if (evolution.hasWinner()) {
			System.out.println("there's a winner in the epoch");
		} else {
			System.out.println("there's NO winner in the epoch");
		}
	}

	@Override
	public void onEpochStart(IEvolution evolution) {
		System.out.println("Epoch Started");
	}

	@Override
	public void onEvolutionComplete(IEvolution evolution) {
		System.out.println("Evolution Complete");
		
		for (IExperimentRunnerListener listener : listeners) {
			listener.experimentRunComplete(this);
		}
	}

	@Override
	public void onEvolutionStart(IEvolution evolution) {
		System.out.println("Evolution Started");
	}
	
	protected void invalidateProgress() {
		progress = (evolution.getTotalRuns() * evolution.getTotalEpochs()) / (evolution.getRun() * evolution.getEpoch());
	}
	
	@Override 
	public float getProgress() {
		return progress;
	}

	@Override
	public void onRunComplete(IEvolution evolution) {
		System.out.println("Run Complete");
	}

	@Override
	public void onRunStart(IEvolution evolution) {
		System.out.println("Run Start");
	}
	
	/* (non-Javadoc)
	 * @see org.brainfarm.mvcs.service.IExperimentRunner#getResultXML()
	 */
	public Document getResultXML() {
		Document xml = XMLUtils.createNewDocument(EvolutionResultWriter.ROOT_NODE_NAME);
		
		if (resultWriter == null) {
			resultWriter = new EvolutionResultWriter();
		}
		
		resultWriter.write(xml, evolution);
		
		return xml;
	}
	
	public void addListener(IExperimentRunnerListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(IExperimentRunnerListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}
	
	public IConnection getConnection() {
		return connection;
	}
}
