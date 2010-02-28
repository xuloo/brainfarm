package org.brainfarm.mvcs.service;

import org.brainfarm.java.feat.api.IEvolution;
import org.brainfarm.java.feat.api.IEvolutionListener;
import org.red5.server.api.so.ISharedObject;

public class ExperimentRunner implements Runnable, IEvolutionListener {

	private ISharedObject so;
	
	private IEvolution evolution;
	
	private float progress = 0.0f;
	
	public ExperimentRunner(ISharedObject so, IEvolution evolution) {
		this.so = so;
		this.evolution = evolution;
	}

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
		so.setAttribute("epoch", evolution.getEpoch());
	}

	@Override
	public void onEvolutionComplete(IEvolution evolution) {
		System.out.println("Evolution Complete");
		so.setAttribute("complete", true);
	}

	@Override
	public void onEvolutionStart(IEvolution evolution) {
		System.out.println("Evolution Started");
		so.beginUpdate();
		so.setAttribute("complete", false);
		so.setAttribute("run", 0);
		so.setAttribute("epoch", 0);
		so.endUpdate();
	}
	
	protected void invalidateProgress() {
		progress = (evolution.getTotalRuns() * evolution.getTotalEpochs()) / (evolution.getRun() * evolution.getEpoch());
	}
	
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
		so.setAttribute("run", evolution.getRun());
	}
}
