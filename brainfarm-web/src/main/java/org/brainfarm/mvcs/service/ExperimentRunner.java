package org.brainfarm.mvcs.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.IEvolutionListener;
import org.brainfarm.java.util.FileUtils;
import org.brainfarm.java.util.XMLUtils;
import org.brainfarm.java.util.writers.EvolutionResultWriter;
import org.brainfarm.java.util.writers.IEvolutionWriter;
import org.brainfarm.java.util.writers.INetworkWriter;
import org.brainfarm.java.util.writers.RaVisNetworkWriter;
import org.red5.server.api.IConnection;
import org.w3c.dom.Document;

public class ExperimentRunner implements IEvolutionListener, IExperimentRunner {

	private IConnection connection;
	
	private IEvolution evolution;
	
	private float progress = 0.0f;
	
	private IEvolutionWriter resultWriter;
	
	private INetworkWriter networkWriter;
	
	private File workingDir;
	
	private EpochData[][] epochData;
	
	private List<IExperimentRunnerListener> listeners = new ArrayList<IExperimentRunnerListener>();
	
	public ExperimentRunner(IConnection connection, File workingDir, IEvolution evolution) {
		this.connection = connection;
		this.workingDir = workingDir;
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
		
		EpochData ed = new EpochData();
		ed.hasWinner = evolution.hasWinner();
		
		if (evolution.hasWinner()) {
			System.out.println("there's a winner in the epoch");
			Document xml = XMLUtils.createNewDocument("Winner");
			
			if (networkWriter == null) {
				networkWriter = new RaVisNetworkWriter();
			}
			
			networkWriter.write(xml, evolution.getWinner().getPhenotype());
			
			ed.winnerFileName = workingDir.getAbsolutePath() + "/" + evolution.getRun() + "/" + evolution.getEpoch() + ".xml";
			System.out.println("FILENAME " + ed.winnerFileName);
			XMLUtils.saveDocument(xml, FileUtils.resolvePath(ed.winnerFileName));
		} else {
			System.out.println("there's NO winner in the epoch");
		}
		
		epochData[evolution.getRun() - 1][evolution.getEpoch() - 1] = ed;
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
		
		epochData = new EpochData[evolution.getTotalRuns()][evolution.getTotalEpochs()];
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
			resultWriter = new FileSystemEvolutionResultWriter(epochData);
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
