package org.brainfarm.java.mvcs.service.remote;

import org.brainfarm.java.red5.message.BaseMessage;

public class RunExperimentMessage extends BaseMessage {

	@Override
	public Object read() {
		service.runExperiment();
		return null;
	}
}
