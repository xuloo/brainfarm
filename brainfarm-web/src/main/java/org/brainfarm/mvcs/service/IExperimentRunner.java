package org.brainfarm.mvcs.service;

import org.red5.server.api.IConnection;
import org.w3c.dom.Document;

public interface IExperimentRunner extends Runnable {

	public abstract float getProgress();
	
	public abstract Document getResultXML();

	public abstract void addListener(IExperimentRunnerListener listener);

	public abstract void removeListener(IExperimentRunnerListener listener);
	
	public abstract IConnection getConnection();
}