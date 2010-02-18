package org.brainfarm.java.mvcs.service.remote;

import org.brainfarm.java.red5.message.BaseMessage;

/**
 * 
 * @author Trevor Burton [trevor@flashmonkey.org]
 *
 */
public class GetAvailableExperimentsMessage extends BaseMessage {

	@Override
	public Object read() {
		return service.getAvailableExperiments();
	}
}
